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

public class Card {
  public enum Value {
    ACE("A", 1),
    KING("K", 10),
    QUEEN("Q", 10),
    JACK("J", 10),
    TEN("10", 10),
    NINE("9", 9),
    EIGHT("8", 8),
    SEVEN("7", 7),
    SIX("6", 6),
    FIVE("5", 5),
    FOUR("4", 4),
    THREE("3", 3),
    TWO("2", 2);

    String display;
    int value;

    Value(String display, int value) {
      this.display = display;
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    static Value fromDisplay(String display) {
      for (Value value : Value.values()) {
        if (value.display.equalsIgnoreCase(display)) {
          return value;
        }
      }
      throw new IllegalArgumentException(
          String.format("There is no card matching \"%s\"", display));
    }
  }

  public enum Suit {
    SPADES("♠"),
    DIAMONDS("♦"),
    CLUBS("♣"),
    HEARTS("♥");

    String display;
    Suit(String display) {
      this.display = display;
    }
  }

  Suit suit;
  Value value;

  public Card(Suit suit, Value value) {
    this.suit = suit;
    this.value = value;
  }

  public Suit getSuit() {
    return this.suit;
  }

  public Value getValue() {
    return this.value;
  }

  public String toString() {
    return suit.display + value.display;
  }

  public boolean equals(Card other) {
    return this.suit == other.suit && this.value == other.value;
  }
}
