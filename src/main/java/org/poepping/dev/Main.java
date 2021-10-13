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

public class Main {
  public static void main(String[] args) {
    CribbageGame.Builder cgb = CribbageGame.builder();
    if (args.length > 0) {
      // assume they've passed the score
      try {
        int scoreToWin = Integer.parseInt(args[0]);
        cgb.scoreToWin(scoreToWin);
        System.out.println("At least one argument passed,"
            + " starting a Cribbage Game where the score to win is " + scoreToWin + "...");
      } catch (NumberFormatException nfe) {
        usage();
        System.exit(2);
      }
    } else {
      System.out.println("No arguments passed, starting a standard Cribbage Game...");
    }
    System.out.println();
    cgb.build().run();
  }

  private static void usage() {
    System.out.println("CribbageGame <scoreToWin>");
  }
}
