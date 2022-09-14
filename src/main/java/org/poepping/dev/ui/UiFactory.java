package org.poepping.dev.ui;

import java.util.function.Supplier;

public final class UiFactory {

  public static CribbageUi create(UiType type) {
    // this currently does no cacheing. probably not necessarily since 
    // we'll likely only be creating a single Ui instance per JVM
    return type.ctor.get();
  }

  public static CribbageUi create(String type) {
    return create(UiType.valueOf(type));
  }
}
