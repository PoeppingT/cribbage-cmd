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
import org.poepping.dev.player.AICribbagePlayer;
import org.poepping.dev.player.CribbagePlayer;
import org.poepping.dev.player.HumanCribbagePlayer;

import java.util.Optional;

public class CribbageGame implements Runnable {

  private static final int DEFAULT_SCORE_TO_WIN = 121;

  private boolean doQuit = false;
  // cribbage is always played with a standard deck
  private final Deck deck = Deck.standard();

  private GameState gameState;
  private Card cutCard;
  private int scoreToWin;
  private int runningCount;
  private boolean aiTurn;
  private boolean aiCrib;
  // for now, we assume one AI player and one human player. can be extended for 3-handed or 4-handed
  private CribbagePlayer aiPlayer;
  private CribbagePlayer humanPlayer;

  enum GameState {
    DEAL,
    DISCARD_TO_CRIB,
    FLIP_CUT_CARD,
    PLAY_CARD,
    SCORE_HANDS,
    SCORE_CRIB
  }

  private CribbageGame(int scoreToWin, boolean aiCribFirst) {
    this.scoreToWin = scoreToWin;
    aiCrib = aiCribFirst;
    aiPlayer = new AICribbagePlayer("AI");
    humanPlayer = new HumanCribbagePlayer("HUMAN");
    gameState = GameState.DEAL;
  }

  @Override
  public void run() {
    // infinite loop of gametime? game loop.
    while(!doQuit) {
      // we check whether the game is over every time we award points
      printGameState();
      switch(gameState) {
        case DEAL: {
          // reset player hands, shuffle the deck, deal out 6 cards to each player
          aiCrib = !aiCrib;
          aiTurn = !aiCrib;
          aiPlayer.reset();
          humanPlayer.reset();
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
          Hand crib = aiCrib ? aiPlayer.getCrib() : humanPlayer.getCrib();
          aiPlayer.discardToCrib(crib, 2);
          humanPlayer.discardToCrib(crib, 2);
          gameState = GameState.FLIP_CUT_CARD;
          break;
        }
        case FLIP_CUT_CARD: {
          // TODO implement actual cutting
          cutCard = deck.draw();
          break;
        }
        case PLAY_CARD: {
          CribbagePlayer player = aiTurn ? aiPlayer : humanPlayer;
          Optional<Card> cardPlayed = player.playCard();
          if (cardPlayed.isPresent()) {
            System.out.println(player.getName() + " played " + cardPlayed.get());
            // TODO did they get points?
            runningCount += cardPlayed.get().getValue().getValue();
          } else {
            System.out.println(player.getName() + " checks");
          }
          aiTurn = !aiTurn;
          if (aiPlayer.outOfCards() && humanPlayer.outOfCards()) {
            gameState = GameState.SCORE_HANDS;
          }
          break;
        }
        case SCORE_HANDS: {
          break;
        }
        case SCORE_CRIB: {
          break;
        }
        default: {
          // impossible to reach. adding a default statement to make checkstyle happy
        }
      }
    }
  }

  private void printGameState() {
    // scoreboard or representation of cribbage board?
    System.out.println(aiPlayer.scoreboard());
    System.out.println(humanPlayer.scoreboard());
    // who's crib is it?
    System.out.println((aiCrib ? aiPlayer.getName() : humanPlayer.getName()) + "'s crib");
    // what's the cut card?
    System.out.println("cut: " + (cutCard != null ? cutCard : ""));
    // what's the running count?
    if (gameState == GameState.PLAY_CARD) {
      System.out.println("\nCount: " + runningCount);
    }
  }

  private void givePoints(CribbagePlayer player, int points) {
    player.addPoints(points);
    if (player.getScore() >= scoreToWin) {
      System.out.println(player + " wins!");
      doQuit = true;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    // ai level, score to win, display strategy, how many players,
    int scoreToWin = DEFAULT_SCORE_TO_WIN;
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
