package org.poepping.dev.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class CircularIterator<T> implements Iterator<T>, Iterable<T> {

  private final T[] objs;
  private int nextIndex;

  private CircularIterator(T[] objs, int startIndex) {
    this.objs = objs;
    this.nextIndex = startIndex;
  }

  public static CircularIterator<? extends Object> of(Iterable<? extends Object> objIt) {
    return of(objIt, 0);
  }

  public static CircularIterator<? extends Object> of(Iterable<? extends Object> objIt, int startIndex) {
    List<Object> itList = new ArrayList<>();
    objIt.iterator().forEachRemaining(obj -> itList.add(obj));
    return of(itList, startIndex);
  }

  public static CircularIterator<? extends Object> of(Collection<? extends Object> objs) {
    return of(objs.toArray(), 0);
  }

  public static CircularIterator<? extends Object> of(Collection<? extends Object> objs, int startIndex) {
    return of(objs.toArray(), startIndex);
  }

  public static CircularIterator<? extends Object> of(Object[] objs) {
    return of(objs, 0);
  }

  public static CircularIterator<? extends Object> of(Object[] objs, int startIndex) {
    if (objs == null) {
      throw new IllegalArgumentException("Cannot create a CircularIterator of null");
    }
    return new CircularIterator<>(objs, startIndex);
  }

  @Override
  public boolean hasNext() {
    // we iterate in a circle
    return true;
  }

  @Override
  public T next() {
    T returnable = objs[nextIndex % objs.length];
    nextIndex = (nextIndex + 1) % objs.length;
    return returnable;
  }

  @Override
  public Iterator<T> iterator() {
    return this;
  }
  
}
