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

package org.poepping.dev.gamelogic.player;

import org.poepping.dev.cards.Card;
import org.poepping.dev.cards.Hand;
import org.poepping.dev.ui.CribbageUi;

import java.util.Optional;

public abstract class CribbagePlayer {
  final String name;
  final CribbageUi ui;

  private int score;

  Hand discard;
  Hand crib;
  Hand hand;

  public CribbagePlayer(CribbageUi ui, String name) {
    this.name = name;
    this.ui = ui;
    score = 0;
    discard = new Hand();
    crib = new Hand();
    hand = new Hand();
  }

  abstract Card[] chooseCardsToDiscardToCrib();

  public void discardToCrib(Hand crib) {
    Card[] cards = chooseCardsToDiscardToCrib();
    for (Card card : cards) {
      crib.add(card);
      hand.remove(card);
    }
  }

  /**
   * should be null to indicate that no card can be played
   * @return
   */
  abstract Card chooseCardToPlay();

  public boolean canPlay(int runningCount) {
    int numberLeftTo31 = 31 - runningCount;
    for (Card card : hand) {
      if (card.getValue().getValue() <= numberLeftTo31) {
        return true;
      }
    }
    return false;
  }

  public Optional<Card> playCard() {
    Card chosenCard = chooseCardToPlay();
    if (chosenCard != null) {
      hand.remove(chosenCard);
      discard.add(chosenCard);
    }
    return chosenCard == null ? Optional.empty() : Optional.of(chosenCard);
  }

  public String scoreboard(boolean myCrib) {
    StringBuilder sb = new StringBuilder();
    if (myCrib) {
      sb.append("(c)");
    }
    sb.append(name);
    sb.append(":");
    sb.append(score);
    return sb.toString();
  }

  public boolean outOfCards() {
    return hand.outOfCards();
  }

  public void addPoints(int points) {
    score += points;
  }

  public int getScore() {
    return score;
  }

  public Hand getHand() {
    return hand;
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

  public CribbageUi getUi() {
    return this.ui;
  }

  public void dealCard(Card card) {
    hand.add(card);
  }

  public void reset() {
    crib.clear();
    discard.clear();
    hand.clear();
  }

  public String toString() {
    return getName();
  }
}
