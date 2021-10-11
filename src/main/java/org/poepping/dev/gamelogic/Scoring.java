package org.poepping.dev.gamelogic;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.gamelogic.exceptions.GameOverException;
import org.poepping.dev.player.CribbagePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Utility holding scoring logic
 */
public final class Scoring {

  private static final Logger LOGGER = LoggerFactory.getLogger(Scoring.class);
  public static final int DEFAULT_POINTS_TO_WIN = 121;

  private final int pointsToWin;

  public Scoring() {
    pointsToWin = DEFAULT_POINTS_TO_WIN;
  }

  public Scoring(int pointsToWin) {
    this.pointsToWin = pointsToWin;
  }

  public void scoreHand(CribbagePlayer player, Card cutCard) throws GameOverException {
    int handPoints = scoreInternal(player.getDiscard(), cutCard, false);
    System.out.println(player.getDiscard().debugString() + " | " + cutCard.toString());
    System.out.println(player + "'s hand scores " + handPoints);
    System.out.println();
    givePoints(player, handPoints);
  }
  
  public void scoreCrib(CribbagePlayer player, Card cutCard) throws GameOverException {
    int cribPoints = scoreInternal(player.getCrib(), cutCard, true);
    System.out.println(player.getCrib().debugString() + " | " + cutCard.toString());
    System.out.println(player + "'s crib scores " + cribPoints);
    System.out.println();
    givePoints(player, cribPoints);
  }

  private int scoreInternal(Hand hand, Card cutCard, boolean isCrib) {
    Hand handAndCut = Hand.copyOf(hand);
    handAndCut.add(cutCard);
    int score = 0;
    // 15
    score += 2 * (howMany15s(handAndCut));
    // pairs
    score += 2 * (howManyPairs(handAndCut));
    // runs
    score += pointsFromRuns(handAndCut);
    // flushes
    score += pointsFromFlushes(hand, cutCard, isCrib);
    return score;
  }

  int howMany15s(Hand cards) {
    return howMany15s(cards, new Hand());
  }

  private int howMany15s(Hand cards, Hand handSoFar) {
    if (handSoFar.sum() == 15) {
      return 1;
    }
    if (handSoFar.sum() > 15 || cards.size() == 0) {
      // there will be no card in Cards that we could add to make sum = 15
      return 0;
    }
    int num15s = 0;
    Hand cardsLeft = Hand.copyOf(cards);
    Card card = cards.choose(0);
    cardsLeft.remove(card);
    num15s += howMany15s(cardsLeft, handSoFar.add(card));
    num15s += howMany15s(cardsLeft, handSoFar.remove(card));
    return num15s;
  }

  int howManyPairs(Hand cards) {
    int numPairs = 0;
    for (int i = 0; i < cards.size(); i++) {
      for (int j = i + 1; j < cards.size(); j++) {
        if (cards.choose(i).getValue().equals(cards.choose(j).getValue())) {
          numPairs++;
        }
      }
    }
    return numPairs;
  }

  int pointsFromRuns(Hand cards) {
    // you can only have a maximum of a 5 card run
    // so, just sort according to sortOrder, like we did in peggingPlay
    LinkedList<Card> run = new LinkedList<>();
    int multiplier = 1;
    for (Card card : Hand.copyOf(cards).sorted()) {
      if (run.size() == 0) {
        run.add(card);
      } else {
        int distanceFromRunToNext = run.getLast().distanceTo(card);
        if (distanceFromRunToNext == 0) {
          // we've found a pair, so multiply if we find a run with this card
          // but *don't* add it to the run
          multiplier++;
        } else {
          if (distanceFromRunToNext != 1) {
            // we've found a gap, so try a new run
            // but wait, TODO only try a new run if we didn't already find one.
            if (run.size() >= 3) {
              // if we found a run, we've found the end. so quit looping.
              break;
            }
            // otherwise, we need to see if there's a run to find.
            // luckily there's no way to find two disjoint runs.
            // TODO interview question? lol
            multiplier = 1;
            run.clear();
          }
          run.add(card);
        }
      }
    }
    // can only have a run of 3, 4, or 5
    if (run.size() >= 3) {
      return multiplier * run.size();
    }
    return 0;
  }

  int pointsFromFlushes(Hand hand, Card cutCard, boolean isCrib) {
    if (hand.size() != 4) {
      return 0;
    }
    Card.Suit flushSuit = null;
    for (Card card : hand) {
      if (flushSuit == null) {
        flushSuit = card.getSuit();
      }
      if (!card.getSuit().equals(flushSuit)) {
        return 0;
      }
    }
    // all cards in the hand match flushSuit
    if (cutCard.getSuit().equals(flushSuit)) {
      return 5;
    } else if (!isCrib) {
      return 4;
    } else {
      return 0;
    }
  }
  
  public void peggingPlay(
      CribbagePlayer player,
      int runningCount,
      Stack<Card> cardsPlayed,
      Card cardPlayed) throws GameOverException {
    final int cardsInRunningCards = cardsPlayed.size();
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
