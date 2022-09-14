package org.poepping.dev.ui;

import org.poepping.dev.cards.Card;
import org.poepping.dev.event.Event;
import org.poepping.dev.gamelogic.context.GameContext;
import org.poepping.dev.player.CribbagePlayer;

public final class NoOpCribbageUi extends CribbageUi {

  public NoOpCribbageUi(GameContext context) {
    super(context);
    //TODO Auto-generated constructor stub
  }

  @Override
  public void displayGame() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handle(Event event) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Card chooseCardToPlay() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Card[] chooseCardsToDiscard(CribbagePlayer player) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
