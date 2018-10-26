package com.mcbaka.bakacraft.Modules.Util.MySQLHelper;

import java.util.Objects;

/**
 * A throwable
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface ThrowableFunction<T, R, E extends Exception> {
    R apply(T t) throws E;

    default <V> ThrowableFunction<V, R, E> compose(ThrowableFunction<? super V, ? extends T, ? extends E> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> ThrowableFunction<T, V, E> andThen(ThrowableFunction<? super R, ? extends V, ? extends E> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    static <T, E2 extends Exception> ThrowableFunction<T, T, E2> identity() {
        return t -> t;
    }
}
