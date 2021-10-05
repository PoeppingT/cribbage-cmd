package org.poepping.dev.gamelogic;

import org.poepping.dev.cards.Card;
import org.poepping.dev.gamelogic.exceptions.GameOverException;
import org.poepping.dev.player.CribbagePlayer;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Utility holding scoring logic
 */
public final class Scoring {

  public static final int DEFAULT_POINTS_TO_WIN = 121;

  private final int pointsToWin;

  public Scoring() {
    pointsToWin = DEFAULT_POINTS_TO_WIN;
  }

  public Scoring(int pointsToWin) {
    this.pointsToWin = pointsToWin;
  }

  public void scoreHand(CribbagePlayer player, Card cutCard) throws GameOverException {
    System.out.println("Scoring hand for " + player + "...");
    // really scoring discard
    System.out.println("NOT IMPLEMENTED!");
  }
  
  public void scoreCrib(CribbagePlayer player, Card cutCard) throws GameOverException {
    System.out.println("Scoring crib for " + player + "...");
    System.out.println("NOT IMPLEMENTED!");
  }
  
  public void peggingPlay(
      CribbagePlayer player,
      int runningCount,
      Stack<Card> cardsPlayed,
      Card cardPlayed) throws GameOverException {
    final int cardsInRunningCards = cardsPlayed.size();
    // TODO did they
    //  - pair, 3oa-kind, 4oa-kind
    //  - runo3, runo4, runo5
    //  - 15 or 31
    // assume that runningCount and cardsPlayed has NOT been edited yet, and cardPlayed is going on top
    // that's a bad assumption to make, TODO fix that

    // PAIRS in cribbage:
    // 1 pair is 2 points
    // 2 pairs is 6 points
    // 3 pairs is 12 points
    // 2 * (cards choose 2)
    //   n choose r = (n!) / (r! * (n - r)!)
    // I guess I don't really need that though, because it's either 1 2 or 3 pairs.
    int numPairs = 0;
    boolean pairing = true;
    Stack<Card> inUseStack = new Stack<>();
    while (pairing) {
      try {
        Card prevCard = cardsPlayed.pop();
        inUseStack.push(prevCard);
        if (cardPlayed.getValue().equals(prevCard.getValue())) {
          numPairs++;
        } else {
          pairing = false;
        }
      } catch (EmptyStackException ese) {
        // this means there are no more running cards, so we're done trying
        pairing = false;
      }
    }
    if (numPairs > 0) {
      int pointsToPlayer = 0;
      if (numPairs == 1) {
        pointsToPlayer = 2;
      } else if (numPairs == 2) {
        pointsToPlayer = 6;
      } else if (numPairs == 3) {
        pointsToPlayer = 12;
      } else {
        assert (false);
      }
      System.out.println(player + ": " + numPairs + (numPairs > 1 ? " pairs " : " pair ")
          + "for " + pointsToPlayer + "!");
      givePoints(player, pointsToPlayer);
    }

    // put the cards back
    while (!inUseStack.empty()) {
      cardsPlayed.push(inUseStack.pop());
    }
    assert inUseStack.empty();
    assert cardsPlayed.size() == cardsInRunningCards;

    // RUNS in cribbage:
    // runo3 for 3, runo4 for 4, runo5 for 5
    // we know we can't have runo6, not allowed in the rules.
    // okay, given the last k cards (0 < k < 9)
    // are the last 3 a runo3? are the last 4 a runo4? are the last 5 a runo5?
    // take the last k, sort them. if the distance between each (sorted) is exactly 1, it's a run
    ArrayList<Card> sortedLastK = new ArrayList<>();
    boolean checkingRuns = true;
    int highestRun = 0;
    for (int i = 1; i <= 5 && checkingRuns; i++) {
      try {
        Card oneCard = cardsPlayed.pop();
        sortedLastK.add(oneCard);
        inUseStack.push(oneCard);
      } catch (EmptyStackException ese) {
        // this means there are no more running cards. check the ones we have if applicable
        checkingRuns = false;
      }
      sortedLastK.sort(Card::compareTo);
      Card lastCard = null;
      boolean foundRun = true;
      for (Card card : sortedLastK) {
        if (lastCard == null) {
          lastCard = card;
        } else {
          if (lastCard.distanceTo(card) != 1) {
            // if it isn't directly sequential, it's no good. continue.
            foundRun = false;
            break;
          }
        }
      }
      if (foundRun) {
        highestRun = i;
      }
    }
    // we've looked at the last 5 cards and found the highest run we could
    if (highestRun > 2) {
      System.out.println(player + ": run of " + highestRun + " for " + highestRun + "!");
      givePoints(player, highestRun);
    }

    // put the cards back
    while (!inUseStack.empty()) {
      cardsPlayed.push(inUseStack.pop());
    }
    assert inUseStack.empty();
    assert cardsPlayed.size() == cardsInRunningCards;

    if (cardPlayed.getValue().getValue() + runningCount == 15) {
      System.out.println(player + ": 15 for 2!");
      givePoints(player, 2);
    }
    if (cardPlayed.getValue().getValue() + runningCount == 31) {
      System.out.println(player + ": 31 for 2!");
      givePoints(player, 2);
    }
  }

  public void givePoints(CribbagePlayer player, int points) throws GameOverException {
    player.addPoints(points);
    if (player.getScore() >= pointsToWin) {
      throw new GameOverException(player + " wins!");
    }
  }
}
