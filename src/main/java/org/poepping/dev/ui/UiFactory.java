package org.poepping.dev.ui;

import org.poepping.dev.gamelogic.context.GameContext;

public final class UiFactory {

  private final GameContext context;

  public UiFactory(GameContext context) {
    this.context = context;
  }

  public CribbageUi create(UiType type) {
    // this currently does no cacheing. probably not necessarily since 
    // we'll likely only be creating a single Ui instance per JVM
    return type.ctor.apply(context);
  }

  public CribbageUi create(String type) {
    return create(UiType.valueOf(type));
  }
}
