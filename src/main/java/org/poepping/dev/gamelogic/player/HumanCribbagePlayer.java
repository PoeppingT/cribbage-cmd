package org.poepping.dev.gamelogic.player;

import org.poepping.dev.cards.Card;
import org.poepping.dev.ui.CribbageUi;

public class HumanCribbagePlayer extends CribbagePlayer {

  public HumanCribbagePlayer(CribbageUi ui, String name) {
    super(ui, name);
  }

  @Override
  public Card[] chooseCardsToDiscardToCrib() {
    return ui.chooseCardsToDiscard(this);
  }

  @Override
  public Card chooseCardToPlay() {
    return ui.chooseCardToPlay();
  }
}
