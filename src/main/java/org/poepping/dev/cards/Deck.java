/**
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.poepping.dev.cards;

import java.util.ArrayList;
import java.util.LinkedList;

public class Deck {
  private final LinkedList<Card> defaultDeck;
  private LinkedList<Card> deck;

  private Deck(LinkedList<Card> deck) {
    this.defaultDeck = deck;
    this.deck = new LinkedList<>(defaultDeck);
  }

  public Card draw() {
    return deck.removeFirst();
  }

  public void shuffle() {
    ArrayList<Card> deckToShuffle = new ArrayList<>(defaultDeck);
    deck = new LinkedList<Card>();
    while (!deckToShuffle.isEmpty()) {
      int randomIndex = (int)(Math.random() * (double)deckToShuffle.size());
      deck.add(deckToShuffle.remove(randomIndex));
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("=== Deck ===");
    sb.append("\nDefault: ");
    sb.append(defaultDeck);
    sb.append("\nCurrent: ");
    sb.append(deck);
    return sb.toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Deck standard() {
    return builder()
        .suits(Card.Suit.SPADES, Card.Suit.DIAMONDS, Card.Suit.CLUBS, Card.Suit.HEARTS)
        .values(Card.Value.ACE, Card.Value.KING, Card.Value.QUEEN, Card.Value.JACK, Card.Value.TEN,
            Card.Value.NINE, Card.Value.EIGHT, Card.Value.SEVEN, Card.Value.SIX, Card.Value.FIVE,
            Card.Value.FOUR, Card.Value.THREE,  Card.Value.TWO)
        .build();
  }

  static class Builder {
    // standard 52, add jokers?
    Card.Suit[] suits;
    Card.Value[] values;

    private Builder() {

    }

    public Builder suits(Card.Suit... suits) {
      this.suits = suits;
      return this;
    }

    public Builder values(Card.Value... values) {
      this.values = values;
      return this;
    }

    public Deck build() {
      // if any of suits or values is null or empty, just return an empty deck.
      LinkedList<Card> deck = new LinkedList<>();
      if (suits != null && values != null) {
        for (Card.Suit suit : suits) {
          for (Card.Value value : values) {
            deck.add(new Card(suit, value));
          }
        }
      }
      return new Deck(deck);
    }
  }
}
