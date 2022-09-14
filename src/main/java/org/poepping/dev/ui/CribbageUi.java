package org.poepping.dev.ui;

import org.poepping.dev.cards.Card;
import org.poepping.dev.event.CardPlayEvent;
import org.poepping.dev.event.CutEvent;
import org.poepping.dev.event.Event;
import org.poepping.dev.event.ScoreEvent;
import org.poepping.dev.gamelogic.Scoring;
import org.poepping.dev.gamelogic.context.GameContext;
import org.poepping.dev.player.CribbagePlayer;

/**
 * Represents the user interface for a Cribbage Game.
 *
 * CribbageUi takes in a GameContext on every call to update the Ui.
 * The choose functions are required to choose a Card from the hand of
 * whose player's turn it is based on GameState and GameContext.
 */
public interface CribbageUi {
    void displayGame(GameContext context);
    void handle(Event event);
    Card chooseCardToPlay(GameContext context);
    Card[] chooseCardsToDiscard(GameContext context);
    void gameOver(CribbagePlayer winner);
}
