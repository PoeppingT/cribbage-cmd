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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Deck {
  private static final Logger LOGGER = LoggerFactory.getLogger(Deck.class);

  private final ArrayList<Card> defaultDeck;
  private ArrayList<Card> deck;

  private Deck(ArrayList<Card> deck) {
    this.defaultDeck = deck;
    this.deck = new ArrayList<>(defaultDeck);
  }

  public Card draw() {
    if (deck.size() == 0) {
      throw new IllegalArgumentException("Cannot draw a card from an empty deck!");
    }
    Card drew = deck.remove(0);
    LOGGER.debug("Drew {}", drew);
    return drew;
  }

  public void shuffle() {
    ArrayList<Card> deckToShuffle = new ArrayList<>(defaultDeck);
    LOGGER.debug("Shuffling {}", deckToShuffle);
    deck = new ArrayList<Card>();
    while (!deckToShuffle.isEmpty()) {
      int randomIndex = (int)(Math.random() * (double)deckToShuffle.size());
      deck.add(deckToShuffle.remove(randomIndex));
    }
    LOGGER.debug("Shuffled {}", deck);
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
      ArrayList<Card> deck = new ArrayList<>();
      LOGGER.debug("Building a deck from suits {} values {}", suits, values);
      if (suits != null && values != null) {
        for (Card.Suit suit : suits) {
          for (Card.Value value : values) {
            Card c = new Card(suit, value);
            LOGGER.trace("Adding {} to deck", c);
            deck.add(c);
          }
        }
      }
      return new Deck(deck);
    }
  }
}
