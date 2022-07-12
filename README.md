# cribbage-cmd
Play cribbage on the command line using java

## How to Build
maven

## How to Play
java -cp /Users/poeppt/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar:/Users/poeppt/.m2/repository/org/apache/logging/log4j/log4j-slf4j-impl/2.13.3/log4j-slf4j-impl-2.13.3.jar:/Users/poeppt/.m2/repository/org/apache/logging/log4j/log4j-core/2.13.2/log4j-core-2.13.2.jar:/Users/poeppt/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.2/log4j-api-2.13.2.jar:target/cribbage-cmd-1.0-SNAPSHOT.jar org.poepping.dev.Main

## Todo
fix up text user interface
pass game context via context object
add basic AI
add tests
add lombok?
add proper logging
add java launcher + releasing

## Feature Ideas

## Tech Debt
1. Extract UIs through an interface that game engine interacts with
2. Extract player interaction through an interface (dependent on UI?)

## Design Notes
Consider the game engine as a server and each player as a client
Interacting via a communication engine that translates the interfaces between the two. Useful only for pluggable client interfaces, but helpful in this case if we consider switching UIs.
Speaking of which, switchable UIs needs to use a UI engine as well. The engine updates the UI when game state changes, probably via the context object noted above. Doesn't need to be game-time UI switching either, abstracting to an interface allows me to experiment without ripping out code and making it harder to replace.

What's the difference between the UI and the communication engine? You can perceive that the communication engine knows how to pass data back and forth, but if we're in one JVM it's all in memory and it's the simplest facade. Communication with the player is achieved via the UI, as well as getting player choices for cards.

In fact, the UI needs a way to API into the game engine basically. If we think of it as a REST-y model (?) then we have an engine API that the client can send requests to. E.g. POST /game creates a new game, GET /stats/${player-id} gets the statistics for a specific player, etc. The game engine doesn't need to know anything about how the UI is built, e.g. whether we have a menu, or how the information is presented to the player. the engine just has ways of returning that information to the client through the engine API.

And then we've actually separated the engine from the UI, instead of the engine saying "UI, whoever you are, display this information" it's up to the UI to actually present the information to the player and retrieve information otherwise.

Here's the question though: what do we do in the event of an AI player? there's no information to present or choices to grab, other than the "choices" the AI makes itself. so we could consider the AI as its own client..
the engine still needs to actually run the game. should the UI be the one saying "I choose to discard these cards"? 
like only the HumanPlayer interacts with the UI, right? so the GameEngine asks the player (human or not) for a choice. if it's a human player, pass the UI manager the information
that's also the pattern of the Observer.. once the game state changes the UI should change to match the game state.

### Game Engine Flow/Responsibilities
1. 
