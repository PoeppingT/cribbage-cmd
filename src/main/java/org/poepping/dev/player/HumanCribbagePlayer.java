package org.poepping.dev.player;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.Scanner;
import java.util.Stack;

public class HumanCribbagePlayer extends CribbagePlayer {

  private Scanner in = new Scanner(System.in);

  public HumanCribbagePlayer(String name) {
    super(name);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib(int numberToDiscard) {
    Card[] discardedCards = new Card[numberToDiscard];
    Hand tempHand = Hand.copyOf(hand);
    for (int i = 0; i < numberToDiscard; i++) {
      System.out.println(tempHand.prettyToString());
      System.out.println("Choose " + (numberToDiscard - i) + " card(s) to discard to the crib.");
      discardedCards[i] = chooseCardFromHand(tempHand);
      tempHand.remove(discardedCards[i]);
    }
    return discardedCards;
  }

  @Override
  public Card chooseCardToPlay(Stack<Card> runningCards, int runningCount) {
    if (!canPlay(runningCount)) {
      System.out.println(this + " cannot play!");
      return null;
    }
    System.out.println("Last Played: " + runningCards);
    System.out.println(hand.prettyToString());
    System.out.println("Choose a card to play.");
    while (true) {
      Card choice = chooseCardFromHand(hand);
      if (choice.getValue().getValue() + runningCount > 31) {
        System.out.println("You must choose a " + (31 - runningCount) + " or less.");
      } else {
        return choice;
      }
    }
  }

  private Card chooseCardFromHand(Hand hand) {
    while (true) {
      String token = in.next();
      try {
        int indexChoice = Integer.parseInt(token);
        return hand.get(indexChoice);
      } catch (NumberFormatException nfe) {
        System.out.println(token + " is not a number. Try again.");
      }
    }
  }

  public void waitToContinue() {
    // TODO to implement
    // System.out.println("Enter to continue..\n");
    // in.nextLine();
    // do nothing
  }
}
