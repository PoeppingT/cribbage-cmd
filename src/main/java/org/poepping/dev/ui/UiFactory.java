package org.poepping.dev.ui;

import java.util.function.Supplier;

public final class UiFactory {
  public enum UiType {
    TEXT(TextUi::new);

    private final Supplier<? extends Ui> ctor;

    private UiType(Supplier<? extends Ui> ctor) {
      this.ctor = ctor;
    }
  }

  public static Ui create(UiType type) {
    // this currently does no cacheing. probably not necessarily since 
    // we'll likely only be creating a single Ui instance per JVM
    return type.ctor.get();
  }

  public static Ui create(String type) {
    return create(UiType.valueOf(type));
  }
}
