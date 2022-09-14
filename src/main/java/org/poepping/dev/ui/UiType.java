package org.poepping.dev.ui;

import org.poepping.dev.gamelogic.context.GameContext;

import java.util.function.Function;

public enum UiType {
  NO_OP(NoOpCribbageUi::new),
  TEXT(TextCribbageUi::new);

  final Function<GameContext, ? extends CribbageUi> ctor;

  UiType(Function<GameContext, ? extends CribbageUi> ctor) {
    this.ctor = ctor;
  }
}
