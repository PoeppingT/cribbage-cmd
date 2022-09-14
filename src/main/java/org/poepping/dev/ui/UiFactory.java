package org.poepping.dev.ui;

import org.poepping.dev.gamelogic.context.GameContext;

public final class UiFactory {

  public static CribbageUi create(UiType type, GameContext context) {
    // this currently does no cacheing. probably not necessarily since 
    // we'll likely only be creating a single Ui instance per JVM
    return type.ctor.apply(context);
  }

  public static CribbageUi create(String type, GameContext context) {
    return create(UiType.valueOf(type), context);
  }
}
