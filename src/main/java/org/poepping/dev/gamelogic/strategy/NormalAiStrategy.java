package org.poepping.dev.gamelogic.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.gamelogic.Scoring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * 
 * Follows a basic set of discarding rules:
 *  1. maximize existing points in hand
 * Follows a basic set of playing rules:
 *  1. play card that would maximize pegging points
 */
public class NormalAiStrategy extends AiStrategy {
  private static final Logger LOGGER = LoggerFactory.getLogger(NormalAiStrategy.class);

  @Override
  public Card chooseCardToDiscard(Hand hand) {
    // choose the discard that leaves the highest score
    // maybe Hard AI Strategy considers whether we're going to discard a second card
    LOGGER.debug("Choosing card to discard from {}", hand);
    Card bestDiscard = null;
    int bestScore = -1;
    for (int i = 0; i < hand.size(); i++) {
      Hand thisPlayHand = Hand.copyOf(hand);
      Card thisDiscard = thisPlayHand.get(i);
      int thisScore = Scoring.scoreHand(thisPlayHand.remove(thisDiscard), null);
      LOGGER.trace("Discarding {} gives a score of {}", thisDiscard, thisScore);
      if (thisScore > bestScore) {
        bestScore = thisScore;
        bestDiscard = thisDiscard;
      }
    }
    LOGGER.debug("Discarding {} from {}", bestDiscard, hand);
    return bestDiscard;
  }

  @Override
  public Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int runningCount) {
    LOGGER.debug("Choosing card to play. hand: {}, runningCards: {}, runningCount: {}",
        hand, runningCards, runningCount);
    int bestPoints = -1;
    Card bestCard = null;
    for (Card card : hand) {
      if (card.getValue().getValue() + runningCount <= 31) {
        int points;
        try {
          points = Scoring.peggingPlay(runningCount, runningCards, card).score();
        } catch (NullPointerException e) {
          points = 0; // TODO this is gross
        }
        LOGGER.trace("Playing {} would net us {} points", card, points);
        if (points > bestPoints) {
          bestPoints = points;
          bestCard = card;
        }
      } else {
        LOGGER.trace("Can't play {} since it would put us over 31", card);
      }
    }
    LOGGER.debug("Playing {} to net us {} points", bestCard, bestPoints);
    return bestCard;
  }
}
