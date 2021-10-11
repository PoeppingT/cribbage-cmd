/**
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.poepping.dev;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Deck;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.gamelogic.Scoring;
import org.poepping.dev.gamelogic.exceptions.GameOverException;
import org.poepping.dev.player.AiCribbagePlayer;
import org.poepping.dev.player.CribbagePlayer;
import org.poepping.dev.player.HumanCribbagePlayer;

import java.io.IOException;
import java.util.*;

public class CribbageGame implements Runnable {
  private boolean doQuit = false;
  // cribbage is always played with a standard deck
  private final Deck deck = Deck.standard();
  private final Scoring scoring;

  private GameState gameState;
  private Card cutCard;
  private int runningCount;
  private Stack<Card> runningCards;
  private boolean aiTurn;
  private boolean aiCrib;
  // for now, we assume one AI player and one human player. can be extended for 3-handed or 4-handed
  private CribbagePlayer aiPlayer;
  private CribbagePlayer humanPlayer;
  private boolean lastPlayerChecked = false;

  enum GameState {
    DEAL,
    DISCARD_TO_CRIB,
    FLIP_CUT_CARD,
    PLAY_CARD,
    SCORE_HANDS,
    SCORE_CRIB
  }

  private CribbageGame(int scoreToWin, boolean aiCribFirst) {
    scoring = new Scoring(scoreToWin);
    runningCount = 0;
    runningCards = new Stack<>();
    aiCrib = aiCribFirst;
    aiPlayer = new AiCribbagePlayer("AI");
    humanPlayer = new HumanCribbagePlayer("HUMAN");
    gameState = GameState.DEAL;
  }

  @Override
  public void run() {
    // infinite loop of gametime? game loop.
    while (!doQuit) {
      // we check whether the game is over every time we award points
      switch (gameState) {
        case DEAL: {
          // reset player hands, shuffle the deck, deal out 6 cards to each player
          aiCrib = !aiCrib;
          aiTurn = !aiCrib;
          aiPlayer.reset();
          humanPlayer.reset();
          runningCards.clear();
          runningCount = 0;
          cutCard = null;
          deck.shuffle();
          if (aiCrib) {
            // human gets first card, ai gets second
            for (int i = 0; i < 6; i++) {
              humanPlayer.dealCard(deck.draw());
              aiPlayer.dealCard(deck.draw());
            }
          } else {
            // ai get first card, human gets second
            for (int i = 0; i < 6; i++) {
              aiPlayer.dealCard(deck.draw());
              humanPlayer.dealCard(deck.draw());
            }
          }
          gameState = GameState.DISCARD_TO_CRIB;
          break;
        }
        case DISCARD_TO_CRIB: {
          printGameState();
          Hand crib = aiCrib ? aiPlayer.getCrib() : humanPlayer.getCrib();
          aiPlayer.discardToCrib(crib, 2);
          humanPlayer.discardToCrib(crib, 2);
          gameState = GameState.FLIP_CUT_CARD;
          break;
        }
        case FLIP_CUT_CARD: {
          // TODO implement actual cutting
          cutCard = deck.draw();
          output(cutCard + " cut.");
          if (cutCard.getValue().equals(Card.Value.JACK)) {
            CribbagePlayer dealer = aiCrib ? aiPlayer : humanPlayer;
            output(cutCard + " cut! Awarding 2 points to " + dealer);
            givePointsAndMaybeEndGame(dealer, 2);
          }
          gameState = GameState.PLAY_CARD;
          break;
        }
        case PLAY_CARD: {
          printGameState();
          CribbagePlayer player = aiTurn ? aiPlayer : humanPlayer;
          // assuming here that playCard returns a legal card to play. responsibility is on the player
          Optional<Card> cardPlayed = player.playCard(31 - runningCount);
          if (cardPlayed.isPresent()) {
            output(player.getName() + " played " + cardPlayed.get());
            try {
              scoring.peggingPlay(player, runningCount, runningCards, cardPlayed.get());
            } catch (GameOverException goe) {
              output(goe.getMessage());
              doQuit = true;
            }
            runningCards.add(cardPlayed.get());
            runningCount += cardPlayed.get().getValue().getValue();
            if (runningCount >= 31) {
              runningCount = 0;
            }
            lastPlayerChecked = false;
          } else {
            output(player.getName() + ": checks");
            if (lastPlayerChecked) {
              // TODO this logic doesn't work
              // both check. award one point to this player and continue
              output("Both players check. Awarding " + player + " 1 point.");
              givePointsAndMaybeEndGame(player, 1);
              lastPlayerChecked = false;
              runningCount = 0;
              runningCards.clear();
            }
            lastPlayerChecked = true;
          }
          aiTurn = !aiTurn;
          if (aiPlayer.outOfCards() && humanPlayer.outOfCards()) {
            gameState = GameState.SCORE_HANDS;
            if (cardPlayed.isPresent()) {
              // that means this player just played the last card
              output(player + ": last card for 1");
              givePointsAndMaybeEndGame(player, 1);
            }
          }
          break;
        }
        case SCORE_HANDS: {
          try {
            if (aiCrib) {
              scoring.scoreHand(humanPlayer, cutCard);
              scoring.scoreHand(aiPlayer, cutCard);
            } else {
              scoring.scoreHand(aiPlayer, cutCard);
              scoring.scoreHand(humanPlayer, cutCard);
            }
          } catch (GameOverException goe) {
            output(goe.getMessage());
            doQuit = true;
          }
          gameState = GameState.SCORE_CRIB;
          break;
        }
        case SCORE_CRIB: {
          // theoretically the crib is empty if it isn't your crib, but let's not take chances
          try {
            if (aiCrib) {
              scoring.scoreCrib(aiPlayer, cutCard);
            } else {
              scoring.scoreCrib(humanPlayer, cutCard);
            }
          } catch (GameOverException goe) {
            output(goe.getMessage());
            doQuit = true;
          }
          printGameState();
          gameState = GameState.DEAL;
          break;
        }
        default: {
          // impossible to reach. adding a default statement to make checkstyle happy
        }
      }
      humanPlayer.waitToContinue();
    }
  }

  private void givePointsAndMaybeEndGame(CribbagePlayer player, int points) {
    try {
      scoring.givePoints(player, points);
    } catch (GameOverException goe) {
      output(goe.getMessage());
      doQuit = true;
    }
  }

  private void printGameState() {
    System.out.println("================================\n"
        + aiPlayer.scoreboard(aiCrib) + "\t\t" + humanPlayer.scoreboard(!aiCrib)
        + "\n================================");
    if (gameState == GameState.PLAY_CARD) {
      System.out.println("Count: " + runningCount);
    }
    if (gameState == GameState.SCORE_CRIB
        || gameState == GameState.SCORE_HANDS) {
      System.out.println("cut: " + (cutCard != null ? cutCard : ""));
    }
    System.out.println();
  }

  private void output(String message) {
    System.out.println(message);
    try {
      Thread.sleep(500L);
    } catch (InterruptedException ignored) {
      // ignored
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    // ai level, score to win, display strategy, how many players,
    int scoreToWin = Scoring.DEFAULT_POINTS_TO_WIN;
    boolean aiCribFirst = true;

    private Builder() {

    }

    public Builder scoreToWin(int scoreToWin) {
      this.scoreToWin = scoreToWin;
      return this;
    }

    public Builder aiCribFirst(boolean aiCribFirst) {
      this.aiCribFirst = aiCribFirst;
      return this;
    }

    public CribbageGame build() {
      return new CribbageGame(scoreToWin, aiCribFirst);
    }
  }
}
