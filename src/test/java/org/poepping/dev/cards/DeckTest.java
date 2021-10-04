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

import org.junit.Test;

public class DeckTest {

  @Test
  public void deckTest() {
    Deck deck = Deck.standard();
    System.out.println(deck);
    printDraw5(deck);
    for (int i = 0; i < 5 ; i++) {
      deck.shuffle();
      System.out.println(deck);
      printDraw5(deck);
    }
  }

  private void printDraw5(Deck deck) {
    for (int i = 0 ; i < 5 ; i++) {
      System.out.print(deck.draw() + " ");
    }
    System.out.println();
  }
}