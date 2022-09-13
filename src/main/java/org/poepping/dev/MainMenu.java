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

import org.poepping.dev.gamelogic.Config;

import java.util.Scanner;

public class MainMenu {
  public static void main(String[] args) {

    boolean exited = false;
    Scanner in = new Scanner(System.in);

    while (!exited) {
      System.out.println("====================================");
      System.out.println("|         Cribbage Game!           |");
      System.out.println("====================================");
      System.out.println("Main Menu:");
      System.out.println("\t1. Play a game.");
      System.out.println("\t2. Exit.");
      String input = in.nextLine();
      if (input.equals("1")) {
        // uh..
        CribbageGame.builder().config(Config.builder().build()).build().run();
      } else if (input.equals("2")) {
        exited = true;
      } else {
        System.err.println("Input either 1 or 2.");
      }
    }

    in.close();
  }
}
