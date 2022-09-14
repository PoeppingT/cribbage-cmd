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
import org.poepping.dev.gamelogic.player.AiCribbagePlayer;
import org.poepping.dev.gamelogic.player.CribbagePlayer;
import org.poepping.dev.gamelogic.player.HumanCribbagePlayer;
import org.poepping.dev.ui.CribbageUi;
import org.poepping.dev.ui.UiFactory;
import org.poepping.dev.ui.UiType;

import java.util.*;
import java.util.function.Consumer;

public class CribbageGame implements Runnable {
  // cribbage is always played with a standard deck
  private final Deck deck = Deck.standard();
  private boolean lastPlayerChecked = false;

  private GameContext context;
  private Config config;

  private CribbageGame(Builder b) {
    config = b.config;
    context = GameContext.builder()
        .config(config)
        .runningCount(0)
        .cardsPlayed(new Stack<>())
        .build();
    UiFactory uiFactory = new UiFactory(context);
    CribbageUi ui = uiFactory.create(config.uiType);
    CribbageUi aiUi = uiFactory.create(UiType.NO_OP);

    CribbagePlayer ai = new AiCribbagePlayer(aiUi, "AI");
    CribbagePlayer human = new HumanCribbagePlayer(ui, "HUMAN");
    
    context.table.addPlayer(ai);
    context.table.addPlayer(human);
    context.whoseCrib = config.aiCribFirst ? ai : human;
    context.whoseTurn = config.aiCribFirst ? human : ai;
  }

  @Override
  public void run() {
    // infinite loop of gametime? game loop.
    while (context.state != GameState.FINISHED) {
      // we check whether the game is over every time we award points
      switch (context.state) {
        case NOT_STARTED: {
          context.state = GameState.DEAL;
          break;
        }
        case DEAL: {
          // reset player hands, shuffle the deck, deal out 6 cards to each player
          // move the crib to the left
          context.whoseCrib = context.table.leftOf(context.whoseCrib);
          // next turn is to the left of the crib
          context.whoseTurn = context.table.leftOf(context.whoseCrib);
          for (CribbagePlayer player : context.table) {
            player.reset();
          }
          context.cardsPlayed.clear();
          context.runningCount = 0;
          context.cutCard = null;
          deck.shuffle();
          for (int i = 0; i < 6; i++) {
            // dealer gets crib and deals to the left first
            for (CribbagePlayer player : context.table.from(context.table.leftOf(context.whoseCrib))) {
              player.dealCard(deck.draw());
            }
          }
          context.state = GameState.DISCARD_TO_CRIB;
          break;
        }
        case DISCARD_TO_CRIB: {
          callUi(CribbageUi::displayGame);
          Hand crib = context.whoseCrib.getCrib();
          for (CribbagePlayer player : context.table) {
            player.discardToCrib(crib);
          }
          context.state = GameState.FLIP_CUT_CARD;
          break;
        }
        case FLIP_CUT_CARD: {
          // TODO implement actual cutting
          final int nobsPoints = 2;
          context.cutCard = deck.draw();
          callUi(ui -> ui.handle(CutEvent.builder().card(context.cutCard).build()));
          if (context.cutCard.getValue().equals(Card.Value.JACK)) {
            CribbagePlayer dealer = context.whoseCrib;
            callUi(ui -> ui.handle(ScoreEvent.builder()
                .score(nobsPoints).player(dealer).reason("Nobs!").build()));
            givePointsAndMaybeEndGame(dealer, nobsPoints);
          }
          context.state = GameState.PLAY_CARD;
          break;
        }
        case PLAY_CARD: {
          callUi(CribbageUi::displayGame);
          CribbagePlayer player = context.whoseTurn;
          // assuming here that playCard returns a legal card to play. responsibility is on the player
          Optional<Card> cardPlayed = player.playCard();
          if (cardPlayed.isPresent()) {
            callUi(ui -> ui.handle(CardPlayEvent.builder().player(player).card(cardPlayed.get()).build()));
            ScoreEvent peggingPoints = Scoring.peggingPlay(context.runningCount, context.cardsPlayed, cardPlayed.get());
            if (peggingPoints.score() > 0) {
              callUi(ui -> ui.handle(ScoreEvent.builder(peggingPoints).player(player).build()));
              givePointsAndMaybeEndGame(player, peggingPoints.score());
            }
            context.cardsPlayed.add(cardPlayed.get());
            context.runningCount += cardPlayed.get().getValue().getValue();
            if (context.runningCount >= 31) {
              context.runningCount = 0;
              context.cardsPlayed.clear(); // we start over from scratch after hitting 31.
            }
            lastPlayerChecked = false;
          } else {
            callUi(ui -> ui.handle(CheckEvent.builder().player(player).build()));
            if (lastPlayerChecked) {
              // both check. award one point to this player and continue
              callUi(ui -> ui.handle(ScoreEvent.builder()
                  .score(1).player(player).reason("Both players check").build()));
              givePointsAndMaybeEndGame(player, 1);
              lastPlayerChecked = false;
              context.runningCount = 0;
              context.cardsPlayed.clear();
            } else {
              lastPlayerChecked = true;
            }
          }
          context.whoseTurn = context.table.leftOf(context.whoseTurn);
          boolean everyoneOutOfCards = true;
          for (CribbagePlayer p : context.table) {
            everyoneOutOfCards = everyoneOutOfCards && p.outOfCards();
          }
          if (everyoneOutOfCards) {
            context.state = GameState.SCORE_HANDS;
            if (cardPlayed.isPresent()) {
              // that means this player just played the last card
              callUi(ui -> ui.handle(ScoreEvent.builder()
                  .player(player).score(1).reason("Last card").build()));
              givePointsAndMaybeEndGame(player, 1);
            }
          }
          break;
        }
        case SCORE_HANDS: {
          int firstScorerIndex = context.table.players.indexOf(context.table.leftOf(context.whoseCrib));

          for (int i = 0; i < context.table.players.size(); i++) {
            CribbagePlayer player = context.table.players.get((firstScorerIndex + i) % context.table.players.size());
            int handPoints = Scoring.scoreHand(player.getDiscard(), context.cutCard);
            // TODO maybe the extension logic isn't great for builders? score/player functions in ScoreEvent
            //      return ScoreEvent.Builder objects (as it should) but we need a HandScoreEvent.Builder object
            // ui.handle(((HandScoreEvent.Builder) HandScoreEvent.builder()
            //     .hand(player.getDiscard())
            //     .score(handPoints)
            //     .player(player)).build());
            callUi(ui -> ui.handle(HandScoreEvent.builder()
                .hand(player.getDiscard())
                .score(handPoints)
                .reason("hand") // could be improved
                .player(player).build()));
            givePointsAndMaybeEndGame(player, handPoints);
          }
          context.state = GameState.SCORE_CRIB;
          break;
        }
        case SCORE_CRIB: {
          // theoretically the crib is empty if it isn't your crib, but let's not take chances
          CribbagePlayer player = context.whoseCrib;
          int cribPoints = Scoring.scoreCrib(player.getCrib(), context.cutCard);
          callUi(ui -> ui.handle(HandScoreEvent.builder()
              .hand(player.getCrib())
              .score(cribPoints)
              .reason("crib") // could be improved
              .player(player).build()));
          givePointsAndMaybeEndGame(player, cribPoints);
          context.state = GameState.DEAL;
          break;
        }
        default: {
          // impossible to reach. adding a default statement to make checkstyle happy
        }
      }
    }
  }

  private void givePointsAndMaybeEndGame(CribbagePlayer player, int points) {
    player.addPoints(points);
    if (player.getScore() > config.maxScore) {
      callUi(ui -> ui.handle(GameOverEvent.builder().winner(player).build()));
      context.state = GameState.FINISHED;
    }
  }

  private void callUi(Consumer<CribbageUi> uiFunction) {
    for (CribbagePlayer player : context.table) {
      uiFunction.accept(player.getUi());
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
