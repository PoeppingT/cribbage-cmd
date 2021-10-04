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

public class Hand {
  private final ArrayList<Card> cards;

  public Hand() {
    cards = new ArrayList<>();
  }

  public void clear() {
    cards.clear();
  }

  public void add(Card card) {
    cards.add(card);
  }

  public void remove(Card card) {
    cards.remove(card);
  }

  public boolean outOfCards() {
    return cards.isEmpty();
  }
}
