package org.poepping.dev.ui;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.event.CardPlayEvent;
import org.poepping.dev.event.CheckEvent;
import org.poepping.dev.event.CutEvent;
import org.poepping.dev.event.Event;
import org.poepping.dev.event.GameOverEvent;
import org.poepping.dev.event.HandScoreEvent;
import org.poepping.dev.event.ScoreEvent;
import org.poepping.dev.gamelogic.context.GameContext;
import org.poepping.dev.gamelogic.context.GameState;
import org.poepping.dev.gamelogic.player.CribbagePlayer;
import org.poepping.dev.gamelogic.player.HumanCribbagePlayer;

import java.util.Scanner;

public final class TextCribbageUi extends CribbageUi {

  private static final Scanner in = new Scanner(System.in);

  public TextCribbageUi(GameContext context) {
    super(context);
  }

  @Override
  public void displayGame() {
    System.out.println("================================");
    for (CribbagePlayer player : context.table) {
      System.out.print(player.scoreboard(context.whoseCrib.equals(player)) + "\t\t");
    }
    System.out.println("\n================================");
    if (context.state == GameState.PLAY_CARD) {
      System.out.println("Count: " + context.runningCount + "\tCut card: " + context.cutCard);
    }
    if (context.state == GameState.SCORE_CRIB
        || context.state == GameState.SCORE_HANDS) {
      System.out.println("cut: " + (context.cutCard != null ? context.cutCard : ""));
    }
  }

  @Override
  public void handle(Event event) {
    switch (event.getType()) {
      case CARD_PLAY: {
        CardPlayEvent cpe = (CardPlayEvent) event;
        output(cpe.player().getName() + " played " + cpe.card() + ".");
        break;
      }
      case CHECK: {
        CheckEvent ce = (CheckEvent) event;
        output(ce.player().getName() + " checks.");
        break;
      }
      case CUT: {
        CutEvent ce = (CutEvent) event;
        output(ce.card() + " was cut.");
        break;
      }
      case GAME_OVER: {
        GameOverEvent goe = (GameOverEvent) event;
        output(goe.winner().getName() + " wins!");
        break;
      }
      case HAND_SCORE: {
        HandScoreEvent hse = (HandScoreEvent) event;
        output(hse.player().getName() + "'s " + hse.reason() + ": "
            + hse.getHand().debugString() + "|" + context.cutCard + " scores "
            + hse.score() + " | " + hse.player().getScore() + "->" + (hse.player().getScore() + hse.score()));
        break;
      }
      case SCORE: {
        ScoreEvent se = (ScoreEvent) event;
        output(se.player().getName() + " scores " + se.score() + " point(s): " + se.reason()
            + ". | " + se.player().getScore() + "->" + (se.player().getScore() + se.score()));
        break;
      }
      default: {
        throw new RuntimeException(this.getClass() + " has not implemented a handler for " + event.getClass());
      }
    }
  }

  @Override
  public Card chooseCardToPlay() {
    CribbagePlayer activePlayer = context.whoseTurn;
    if (!(activePlayer instanceof HumanCribbagePlayer)) {
      throw new RuntimeException(this.getClass() + " was asked to choose a card for non-human player " + activePlayer);
    }
    if (!activePlayer.canPlay(context.runningCount)) {
      System.out.println(activePlayer + " cannot play!");
      return null;
    }
    output("Last Played: " + context.cardsPlayed);
    System.out.println(activePlayer.getHand().prettyToString() + "\nChoose a card to play.");
    while (true) {
      Card choice = chooseCardFromHand(activePlayer.getHand());
      if (choice.getValue().getValue() + context.runningCount > 31) {
        System.out.println("You must choose a " + (31 - context.runningCount) + " or less.");
      } else {
        return choice;
      }
    }
  }

  @Override
  public Card[] chooseCardsToDiscard(CribbagePlayer player) {
    final int numberToDiscard = player.getHand().size() - 4;
    Hand handMinusDiscards = Hand.copyOf(player.getHand());
    Card[] discardedCards = new Card[numberToDiscard];
    for (int i = 0; i < numberToDiscard; i++) {
      System.out.println("\n" + handMinusDiscards.prettyToString());
      System.out.println("Choose a card to discard to crib.");
      discardedCards[i] = chooseCardFromHand(handMinusDiscards);
      handMinusDiscards.remove(discardedCards[i]);
    }
    return discardedCards;
  }

  private Card chooseCardFromHand(Hand hand) {
    while (true) {
      String token = in.next();
      try {
        int indexChoice = Integer.parseInt(token);
        return hand.get(indexChoice);
      } catch (NumberFormatException nfe) {
        output(token + " is not a number. Try again.");
      }
    }
  }

  private void output(String message) {
    System.out.println(message);
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException ignored) {
      // ignored
    }
  }
}
