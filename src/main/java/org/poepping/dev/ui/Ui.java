package org.poepping.dev.ui;

import org.poepping.dev.cards.Card;
import org.poepping.dev.gamelogic.context.GameContext;

public interface Ui {
    void display(GameContext context);
    Card chooseCardToPlay(GameContext context);
    Card[] chooseCardsToDiscard(GameContext context);
}
