package org.poepping.dev.gamelogic;

import org.poepping.dev.cards.Card;
import org.poepping.dev.player.AiCribbagePlayer;
import org.poepping.dev.player.CribbagePlayer;
import org.poepping.dev.player.HumanCribbagePlayer;

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
  public List<CribbagePlayer> players;
  public CribbagePlayer whoseCrib;
  public CribbagePlayer whoseTurn;
  public Card cutCard;
  public Stack<Card> cardsPlayed;
  public int runningCount;

  private GameContext(Builder b) {
    this.state = GameState.DEAL;
    this.config = b.config;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(GameContext otherContext) {
    return new Builder()
      .config(otherContext.config)
      .state(otherContext.state)
      .players(otherContext.players)
      .whoseCrib(otherContext.whoseCrib)
      .whoseTurn(otherContext.whoseTurn)
      .cutCard(otherContext.cutCard)
      .cardsPlayed(otherContext.cardsPlayed)
      .runningCount(otherContext.runningCount);
  }

  public static class Builder {
    private Config config = Config.defaultConfig();
    private GameState state = GameState.DEAL;
    private List<CribbagePlayer> players;
    private CribbagePlayer whoseCrib;
    private CribbagePlayer whoseTurn;
    private Card cutCard;
    private Stack<Card> cardsPlayed;
    private int runningCount;
    
    private Builder() {

    }

    public Builder state(GameState state) {
      this.state = state;
      return this;
    }

    public Builder players(CribbagePlayer... players) {
      this.players = List.of(players);
      return this;
    }

    public Builder players(List<CribbagePlayer> players) {
      this.players = players;
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

    public GameContext build() {
      if (players == null) {
        players = new ArrayList<>();
        int numberHumans = config.humanPlayers;
        for (int i = 0; i < config.numberOfPlayers; i++) {
          if (numberHumans > 0) {
            players.add(new HumanCribbagePlayer("HUMAN" + i));
            numberHumans--;
          } else {
            players.add(new AiCribbagePlayer("AI" + i));
          }
        }
      }
      return new GameContext(this);
    }
  }
}
