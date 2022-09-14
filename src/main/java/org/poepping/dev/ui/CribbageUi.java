package org.poepping.dev.ui;

import org.poepping.dev.cards.Card;
import org.poepping.dev.event.Event;
import org.poepping.dev.gamelogic.context.GameContext;
import org.poepping.dev.gamelogic.player.CribbagePlayer;

/**
 * Represents the user interface for a Cribbage Game.
 *
 * CribbageUi takes in a GameContext on every call to update the Ui.
 * The choose functions are required to choose a Card from the hand of
 * whose player's turn it is based on GameState and GameContext.
 * 
 */
public abstract class CribbageUi {
  final GameContext context;

  public CribbageUi(GameContext context) {
    this.context = context;
  }

  public GameContext getContext() {
    return this.context;
  }

  public abstract void displayGame();

  public abstract void handle(Event event);

  public abstract Card chooseCardToPlay();
  
  public abstract Card[] chooseCardsToDiscard(CribbagePlayer player);
}
