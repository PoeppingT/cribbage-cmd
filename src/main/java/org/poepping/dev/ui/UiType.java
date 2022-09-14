package org.poepping.dev.ui;

import java.util.function.Supplier;

public enum UiType {
  TEXT(TextCribbageUi::new);

  final Supplier<? extends CribbageUi> ctor;

  UiType(Supplier<? extends CribbageUi> ctor) {
    this.ctor = ctor;
  }
}
