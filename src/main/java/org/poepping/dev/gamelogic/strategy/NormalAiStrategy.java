package org.poepping.dev.gamelogic.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.Stack;

public class NormalAiStrategy extends AiStrategy {
  @Override
  public Card chooseCardToDiscard(Hand hand) {
    return null;
  }

  @Override
  public Card chooseCardToPlay(Stack<Card> runningCards, int numberLeftTo31) {
    return null;
  }
}
