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
import org.poepping.dev.event.*;
import org.poepping.dev.gamelogic.Config;
import org.poepping.dev.gamelogic.Scoring;
import org.poepping.dev.gamelogic.context.GameContext;
import org.poepping.dev.gamelogic.context.GameState;
import org.poepping.dev.gamelogic.exceptions.GameOverException;
import org.poepping.dev.player.AiCribbagePlayer;
import org.poepping.dev.player.CribbagePlayer;
import org.poepping.dev.player.HumanCribbagePlayer;
import org.poepping.dev.ui.CribbageUi;
import org.poepping.dev.ui.UiFactory;

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

  private GameContext context;
  private CribbageUi ui;

  private CribbageGame(Builder b) {
    Config config = b.config;
    ui = UiFactory.create(config.uiType);
    scoring = new Scoring(config.maxScore);
    
    runningCount = 0;
    runningCards = new Stack<>();
    
    aiPlayer = new AiCribbagePlayer("AI");
    humanPlayer = new HumanCribbagePlayer("HUMAN");
    gameState = GameState.DEAL;
    context = GameContext.builder()
      .config(config)
      .players(aiPlayer, humanPlayer)
      .state(gameState)
      .build();
    aiCrib = config.aiCribFirst;
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
          ui.displayGame(context);
          Hand crib = aiCrib ? aiPlayer.getCrib() : humanPlayer.getCrib();
          aiPlayer.discardToCrib(crib, 2);
          humanPlayer.discardToCrib(crib, 2);
          gameState = GameState.FLIP_CUT_CARD;
          break;
        }
        case FLIP_CUT_CARD: {
          // TODO implement actual cutting
          final int NOBS = 2;
          cutCard = deck.draw();
          ui.displayCutEvent(CutEvent.builder().card(cutCard).build());
          if (cutCard.getValue().equals(Card.Value.JACK)) {
            CribbagePlayer dealer = aiCrib ? aiPlayer : humanPlayer;
            ui.displayScoreEvent(ScoreEvent.builder()
                .score(NOBS).player(dealer).reason("Nobs!").build());
            givePointsAndMaybeEndGame(dealer, NOBS);
          }
          gameState = GameState.PLAY_CARD;
          break;
        }
        case PLAY_CARD: {
          ui.displayGame(context);
          CribbagePlayer player = aiTurn ? aiPlayer : humanPlayer;
          // assuming here that playCard returns a legal card to play. responsibility is on the player
          Optional<Card> cardPlayed = player.playCard(runningCards, runningCount);
          if (cardPlayed.isPresent()) {
            ui.displayCardPlayEvent(CardPlayEvent.builder().player(player).card(cardPlayed.get()).build());
            ScoreEvent peggingPoints = Scoring.peggingPlay(runningCount, runningCards, cardPlayed.get());
            if (peggingPoints != null) {
              System.out.println(peggingPoints.reason());
              givePointsAndMaybeEndGame(player, peggingPoints.score());
            }
            runningCards.add(cardPlayed.get());
            runningCount += cardPlayed.get().getValue().getValue();
            if (runningCount >= 31) {
              runningCount = 0;
              runningCards.clear(); // we start over from scratch after hitting 31.
            }
            lastPlayerChecked = false;
          } else {
            ui.handle(CheckEvent.builder().player(player).build());
            if (lastPlayerChecked) {
              // TODO this logic doesn't work
              // both check. award one point to this player and continue
              ui.handle(ScoreEvent.builder()
                  .score(1).player(player).reason("Both players check.").build());
              givePointsAndMaybeEndGame(player, 1);
              lastPlayerChecked = false;
              runningCount = 0;
              runningCards.clear();
            } else {
              lastPlayerChecked = true;
            }
          }
          aiTurn = !aiTurn;
          if (aiPlayer.outOfCards() && humanPlayer.outOfCards()) {
            gameState = GameState.SCORE_HANDS;
            if (cardPlayed.isPresent()) {
              // that means this player just played the last card
              ui.handle(ScoreEvent.builder()
                  .player(player).score(1).reason("Last card.").build());
              givePointsAndMaybeEndGame(player, 1);
            }
          }
          break;
        }
        case SCORE_HANDS: {
          // do this for ordering: non-crib counts first
          CribbagePlayer[] players = aiCrib 
            ? new CribbagePlayer[]{humanPlayer, aiPlayer}
            : new CribbagePlayer[]{aiPlayer, humanPlayer};
          for (CribbagePlayer player : players) {
            int handPoints = Scoring.scoreHand(player.getDiscard(), cutCard);
            System.out.println(player.getDiscard().debugString() + " | " + cutCard.toString());
            System.out.println(player + "'s hand scores " + handPoints);
            System.out.println();
            givePointsAndMaybeEndGame(player, handPoints);
          }
          gameState = GameState.SCORE_CRIB;
          break;
        }
        case SCORE_CRIB: {
          // theoretically the crib is empty if it isn't your crib, but let's not take chances
          CribbagePlayer player = aiCrib ? aiPlayer : humanPlayer;
          int cribPoints = Scoring.scoreCrib(player.getCrib(), cutCard);
          System.out.println(player.getCrib().debugString() + " | " + cutCard.toString());
          System.out.println(player + "'s crib scores " + cribPoints);
          System.out.println();
          givePointsAndMaybeEndGame(player, cribPoints);
          output("");
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
      ui.handle(GameOverEvent.builder().winner(player).build());
      doQuit = true;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    private Config config;

    private Builder() {

    }

    public Builder config(Config config) {
      this.config = config;
      return this;
    }

    public CribbageGame build() {
      return new CribbageGame(this);
    }
  }
}
