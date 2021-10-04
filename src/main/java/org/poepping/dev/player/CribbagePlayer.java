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

package org.poepping.dev.player;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;

import java.util.ArrayList;
import java.util.Optional;

public abstract class CribbagePlayer {
  private final String name;

  private int score;

  Hand discard;
  Hand crib;
  Hand hand;

  public CribbagePlayer(String name) {
    this.name = name;
    score = 0;
    discard = new Hand();
    crib = new Hand();
    hand = new Hand();
  }

  abstract Card[] chooseCardsToDiscardToCrib(int numberToDiscard);

  public void discardToCrib(Hand crib, int numberToDiscard) {
    Card[] cards = chooseCardsToDiscardToCrib(numberToDiscard);

  }

  /**
   * should be null to indicate that no card can be played
   * @return
   */
  abstract Card chooseCardToPlay();

  public Optional<Card> playCard() {
    Card chosenCard = chooseCardToPlay();
    if (chosenCard != null) {
      hand.remove(chosenCard);
      discard.add(chosenCard);
    }
    return chosenCard == null ? Optional.empty() : Optional.of(chosenCard);
  }

  public String scoreboard() {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append(":");
    sb.append(score);
    return sb.toString();
  }

  public boolean outOfCards() {
    return hand.
  }

  public void addPoints(int points) {
    score += points;
  }

  public int getScore() {
    return score;
  }

  public Hand getCrib() {
    return crib;
  }

  public Hand getDiscard() {
    return discard;
  }

  public String getName() {
    return name;
  }

  public void dealCard(Card card) {
    hand.add(card);
  }

  public void reset() {
    crib.clear();
    discard.clear();
    hand.clear();
  }
}
