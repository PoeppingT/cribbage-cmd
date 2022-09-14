package org.poepping.dev.gamelogic.context;

import org.poepping.dev.cards.Card;
import org.poepping.dev.gamelogic.Config;
import org.poepping.dev.gamelogic.player.CribbagePlayer;
import org.poepping.dev.gamelogic.player.Table;
import org.poepping.dev.ui.CribbageUi;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * What do we need in a GameContext object? All the public information
 * - cards played in order
 */
public class GameContext {
  public final Config config;

  public GameState state;
  public Table table;
  public CribbagePlayer whoseCrib;
  public CribbagePlayer whoseTurn;
  public Card cutCard;
  public Stack<Card> cardsPlayed;
  public int runningCount;
  public CribbageUi ui;

  private GameContext(Builder b) {
    this.config = b.config;

    this.state = b.state;
    this.table = b.table;
    this.whoseCrib = b.whoseCrib;
    this.whoseTurn = b.whoseTurn;
    this.cutCard = b.cutCard;
    this.cardsPlayed = b.cardsPlayed;
    this.runningCount = b.runningCount;
    this.ui = b.ui;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(GameContext otherContext) {
    return new Builder()
      .config(otherContext.config)
      .state(otherContext.state)
      .table(otherContext.table)
      .whoseCrib(otherContext.whoseCrib)
      .whoseTurn(otherContext.whoseTurn)
      .cutCard(otherContext.cutCard)
      .cardsPlayed(otherContext.cardsPlayed)
      .runningCount(otherContext.runningCount);
  }

  public static class Builder {
    private Config config = Config.defaultConfig();
    private GameState state = GameState.NOT_STARTED;
    private Table table = new Table();
    private CribbagePlayer whoseCrib;
    private CribbagePlayer whoseTurn;
    private Card cutCard;
    private Stack<Card> cardsPlayed;
    private int runningCount;
    private CribbageUi ui;
    
    private Builder() {

    }

    public Builder state(GameState state) {
      this.state = state;
      return this;
    }

    public Builder table(Table table) {
      this.table = table;
      return this;
    }

    public Builder whoseCrib(CribbagePlayer whoseCrib) {
      this.whoseCrib = whoseCrib;
      return this;
    }

    public Builder whoseTurn(CribbagePlayer whoseTurn) {
      this.whoseTurn = whoseTurn;
      return this;
    }

    public Builder cutCard(Card cutCard) {
      this.cutCard = cutCard;
      return this;
    }

    public Builder cardsPlayed(Stack<Card> cardsPlayed) {
      this.cardsPlayed = cardsPlayed;
      return this;
    }

    public Builder runningCount(int runningCount) {
      this.runningCount = runningCount;
      return this;
    }

    public Builder config(Config config) {
      this.config = config;
      return this;
    }

    public Builder ui(CribbageUi ui) {
      this.ui = ui;
      return this;
    }

    public GameContext build() {
      return new GameContext(this);
    }
  }
}
