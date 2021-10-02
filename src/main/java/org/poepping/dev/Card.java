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

package org.poepping.dev;

public enum Card {
  ACE("A", 1, false),
  KING("K", 10, true),
  QUEEN("Q", 10, true),
  JACK("J", 10, true),
  TEN("10", 10, false),
  NINE("9", 9, false),
  EIGHT("8", 8, false),
  SEVEN("7", 7, false),
  SIX("6", 6, false),
  FIVE("5", 5, false),
  FOUR("4", 4, false),
  THREE("3", 3, false),
  TWO("2", 2, false),
  ONE("1", 1, false);

  String display;
  int value;
  boolean faceCard;

  Card(String display, int value, boolean faceCard) {
    this.display = display;
    this.value = value;
    this.faceCard = faceCard;
  }

  static Card fromDisplay(String display) {
    for (Card card : Card.values()) {
      if (card.display.equalsIgnoreCase(display)) {
        return card;
      }
    }
    throw new IllegalArgumentException(String.format("There is no card matching \"%s\"", display));
  }
}
