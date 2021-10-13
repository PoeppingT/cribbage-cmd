package org.poepping.dev.gamelogic.strategy;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.Stack;

public abstract class AiStrategy {

  public static final Level DEFAULT_LEVEL = Level.EASY;

  /*
 what does it mean to represent difficulty? strategy. given a situation, how do I choose what to play?
 1. what do you discard at crib?
   2. favor keeping points in your own hand vs avoiding points in the crib
   3.
 2. what do you play?
  */
  public enum Level  {
    EASY(new EasyAiStrategy()),
    NORMAL(new NormalAiStrategy()),
    HARD(new HardAiStrategy());

    AiStrategy leveledStrategy;

    Level(AiStrategy thisStrategy) {
      this.leveledStrategy = thisStrategy;
    }

    public AiStrategy strategy() {
      return this.leveledStrategy;
    }

    public Level harder() {
      if (this == EASY) {
        return Level.NORMAL;
      }
      return Level.HARD;
    }

    public Level easier() {
      if (this == HARD) {
        return Level.NORMAL;
      }
      return Level.EASY;
    }
  }

  public abstract Card chooseCardToDiscard(Hand hand);

  public abstract Card chooseCardToPlay(Hand hand, Stack<Card> runningCards, int numberLeftTo31);

  public static AiStrategy defaultStrategy() {
    return DEFAULT_LEVEL.leveledStrategy;
  }

  public static AiStrategy forLevel(Level level) {
    return level.leveledStrategy;
  }
}
