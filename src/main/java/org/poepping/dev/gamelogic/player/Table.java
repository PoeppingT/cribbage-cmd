package org.poepping.dev.gamelogic.player;

import org.poepping.dev.util.CircularIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Table implements Iterable<CribbagePlayer> {
  public List<CribbagePlayer> players = new ArrayList<>();

  public void addPlayer(CribbagePlayer player) {
    players.add(player);
  }

  public CribbagePlayer leftOf(CribbagePlayer player) {
    int playerSeat = players.indexOf(player);
    int leftOfPlayerSeat = (playerSeat + 1) % players.size();
    return players.get(leftOfPlayerSeat);
  }

  @Override
  public Iterator<CribbagePlayer> iterator() {
    return players.iterator();
  }

  public Iterable<CribbagePlayer> from(CribbagePlayer player) {
    final CircularIterator<CribbagePlayer> aroundTheHorn = (CircularIterator<CribbagePlayer>)
        CircularIterator.of(players, players.indexOf(player));
    final int numberPlayers = players.size();
    return new Iterable<CribbagePlayer>() {
      @Override
      public Iterator<CribbagePlayer> iterator() {
        return new Iterator<CribbagePlayer>() {

          private int index = 0;

          @Override
          public boolean hasNext() {
            return index < numberPlayers;
          }

          @Override
          public CribbagePlayer next() {
            index++;
            return aroundTheHorn.next();
          }
          
        };
      }
    };
  }
}
