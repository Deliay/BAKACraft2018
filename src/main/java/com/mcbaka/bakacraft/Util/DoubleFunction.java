package com.mcbaka.bakacraft.Util;

@FunctionalInterface
public interface DoubleFunction<T1, T2, TR> {
    TR apply(T1 t1, T2 t2);
}
