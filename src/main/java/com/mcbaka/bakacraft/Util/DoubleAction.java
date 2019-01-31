package com.mcbaka.bakacraft.Util;

@FunctionalInterface
public interface DoubleAction<T1, T2> {
    void apply(T1 t1, T2 t2);
}
