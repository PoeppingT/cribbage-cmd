package org.poepping.dev.gamelogic.player.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.Stack;

/*
 * Maximize hand points
 * but don't give away points (or prospective points, like 5s)
 * Discard to crib as a pair, not one at a time
 * Don't play into 31s
 * Don't play into runs (if you can't answer)
 * Sometimes go for a gut shot?
 * play into pairs if you can triple them
 * 
 * Occasional risk-taking to balance safe/defensive play, and be less predictable, 
 * especially would change when winning or losing big
Setting yourself up to get 15s or 31s when pegging
End game strategies trying to eke out the win/skunk
Don't forget about the flush
Try to guess what the player has based on the cards they've played already or based on what you have (sometimes)

Tom
  11:26 AM
when would you discard to a lower hand points on a risk for the cut? 
just in a tight situation where getting the cut means you win?

Bennet
  11:27 AM
Well you're down big so you need to come back, maybe you go with the 
gut shot because otherwise you're likely to lose anyway

A little unpredictability is a good thing, especially with:
Lead from a pair
Be careful about playing into a run, especially early in the hand
Weigh positive and negative in card play - potential positives can be too risky
Generally play higher cards first but play low cards occasionally if 
there is no specific strategy of high potential value.
Play to a teen number if you have another card that will give you 31 if the opponent 
plays a ten or face card (high probably); e.g. if the count is 9 and you have
 */
public class HardAiStrategy extends AiStrategy {
  @Override
  public Card chooseCardToDiscard(Hand hand) {
    return null;
  }

  @Override
  public Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int runningCount) {
    return null;
  }
}
