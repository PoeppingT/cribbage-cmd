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
import java.util.Arrays;
import java.util.Iterator;

public class Hand implements Iterable<Card> {
  private final ArrayList<Card> cards;

  private int sum = 0;

  public Hand() {
    cards = new ArrayList<>();
  }

  public Hand(Card... cards) {
    this.cards = new ArrayList<>(Arrays.asList(cards));
    for (Card card : cards) {
      sum += card.getValue().getValue();
    }
  }

  public static Hand copyOf(Hand hand) {
    Hand newHand = new Hand();
    for (Card card : hand) {
      newHand.add(card);
    }
    return newHand;
  }

  public Hand clear() {
    cards.clear();
    return this;
  }

  public Hand add(Card card) {
    cards.add(card);
    sum += card.getValue().getValue();
    return this;
  }

  public Hand remove(Card card) {
    cards.remove(card);
    sum -= card.getValue().getValue();
    return this;
  }

  public Hand sorted() {
    cards.sort(Card::compareTo);
    return this;
  }

  public boolean outOfCards() {
    return cards.isEmpty();
  }

  public Card choose(int choice) {
    return cards.get(choice);
  }

  public int size() {
    return cards.size();
  }

  public int sum() {
    return sum;
  }

  public Iterator<Card> iterator() {
    return cards.iterator();
  }

  public String toString() {
    StringBuilder indices = new StringBuilder();
    StringBuilder cardDisplays = new StringBuilder();
    for (int i = 0; i < cards.size(); i++) {
      indices.append(i + "\t");
      cardDisplays.append(cards.get(i) + "\t");
    }
    return indices + "\n" + cardDisplays;
  }

  public String debugString() {
    String out = "[";
    for (int i = 0; i < size(); i++) {
      out += choose(i).toString();
      if (i != (size() - 1)) {
        out += ",";
      }
    }
    return out + "]";
  }
}
