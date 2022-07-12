package org.poepping.dev.gamelogic;

import org.junit.Test;
import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.function.Function;

import static org.junit.Assert.*;

public class ScoringTest {

  @Test
  public void testHowMany15s() {
    howMany15sHelper(0, "");
    howMany15sHelper(1, "♠K,♠5");
    howMany15sHelper(3, "♠A,♠K,♠Q,♠J,♠5");
    howMany15sHelper(7, "♠5,♠5,♠5,♠J,♠Q");
    howMany15sHelper(1, "♠9,♠2,♠A,♠A,♠2");
    howMany15sHelper(0, "♠7,♠9,♠10,♠10,♠J");
  }

  private void howMany15sHelper(int expected, String hand) {
    assertFunctionForHand(expected, hand, Scoring::howMany15s);
  }

  @Test
  public void testHowManyPairs() {
    howManyPairsHelper(0, "");
    howManyPairsHelper(1, "♠5,♠5");
    howManyPairsHelper(1, "♠J,♠5,♠5");
    howManyPairsHelper(2, "♠J,♠J,♠5,♠5");
    howManyPairsHelper(3, "♠5,♠5,♠5");
    howManyPairsHelper(3, "♠5,♠5,♠5,♠J,♠A");
    howManyPairsHelper(4, "♠5,♠5,♠5,♠J,♠J");
    howManyPairsHelper(6, "♠5,♠5,♠5,♠5,♠J");
  }

  private void howManyPairsHelper(int expected, String hand) {
    assertFunctionForHand(expected, hand, Scoring::howManyPairs);
  }

  @Test
  public void testPointsFromRuns() {
    pointsFromRunsHelper(0, "");
    pointsFromRunsHelper(0, "♠5,♠5,♠5,♠J,♠A");
    pointsFromRunsHelper(3, "♠5,♠6,♠J,♠A,♠7");
    pointsFromRunsHelper(5, "♠5,♠6,♠8,♠4,♠7");
    // TODO do we need to handle illegal hands? we can only have a max of 5 cards in any cribbage variant
    // pointsFromRunsHelper(5, "♠5,♠6,♠8,♠4,♠7,♠3,♠2,♠A");
    pointsFromRunsHelper(0, "♠Q,♠K,♠A");
    pointsFromRunsHelper(6, "♠7,♠7,♠8,♠9");
    pointsFromRunsHelper(9, "♠7,♠7,♠7,♠8,♠9");
  }

  private void pointsFromRunsHelper(int expected, String hand) {
    assertFunctionForHand(expected, hand, Scoring::pointsFromRuns);
  }

  @Test
  public void testPointsFromFlushes() {
    // "♠5,♠5,♠5,♠J,♠A"
    // ♣ ♦ ♥
    pointsFromFlushesHelper(0, "", "♦7", true);
    pointsFromFlushesHelper(0, "", "♦7", false);
    pointsFromFlushesHelper(0, "♠5,♥5,♠5,♥J", "♦7", false);
    pointsFromFlushesHelper(0, "♠5,♥5,♠5,♥J", "♦7", true);
    pointsFromFlushesHelper(4, "♠5,♠5,♠5,♠J", "♣J", false);
    pointsFromFlushesHelper(0, "♠5,♠5,♠5,♠J", "♣J", true);
    pointsFromFlushesHelper(5, "♠5,♠5,♠5,♠J", "♠J", false);
    pointsFromFlushesHelper(5, "♠5,♠5,♠5,♠J", "♠J", false);
  }

  private void pointsFromFlushesHelper(int expected, String hand, String cutCard, boolean isCrib) {
    assertEquals((isCrib ? "crib: " : "hand") + hand + "|" + cutCard, expected, Scoring.pointsFromFlushes(
        handFromString(hand), Card.fromString(cutCard), isCrib
    ));
  }

  private void assertFunctionForHand(int expected, String hand, Function<Hand, Integer> testFunction) {
    assertEquals(hand, expected, testFunction.apply(handFromString(hand)).intValue());
  }

  private Hand handFromString(String cards) {
    return handFromString(cards, ",");
  }

  private Hand handFromString(String cards, String separator) {
    Hand toReturn = new Hand();
    for (String cardString : cards.split(separator)) {
      if (!cardString.isEmpty()) {
        toReturn.add(Card.fromString(cardString.trim()));
      }
    }
    return toReturn;
  }
}
