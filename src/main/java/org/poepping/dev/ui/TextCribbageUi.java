package org.poepping.dev.ui;

import org.poepping.dev.cards.Card;
import org.poepping.dev.event.CardPlayEvent;
import org.poepping.dev.event.Event;
import org.poepping.dev.event.ScoreEvent;
import org.poepping.dev.gamelogic.Scoring;
import org.poepping.dev.gamelogic.context.GameContext;
import org.poepping.dev.gamelogic.context.GameState;
import org.poepping.dev.player.CribbagePlayer;

public final class TextCribbageUi implements CribbageUi {

  @Override
  public void displayGame(GameContext context) {
    System.out.println("================================");
    for (CribbagePlayer player : context.players) {
      System.out.print(player.scoreboard(context.whoseCrib.equals(player)) + "\t\t");
    }
    System.out.print("\n================================");
    if (context.state == GameState.PLAY_CARD) {
      System.out.println("Count: " + context.runningCount);
    }
    if (context.state == GameState.SCORE_CRIB
        || context.state == GameState.SCORE_HANDS) {
      System.out.println("cut: " + (context.cutCard != null ? context.cutCard : ""));
    }
  }

  @Override
  public void handle(Event event) {
    switch (event.getType()) {
      default: {
        throw new RuntimeException(this.getClass() + " has not implemented a handler for " + event.getClass());
      }
    }
  }

  @Override
  public Card chooseCardToPlay(GameContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Card[] chooseCardsToDiscard(GameContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void gameOver(CribbagePlayer winner) {
    // TODO Auto-generated method stub
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
