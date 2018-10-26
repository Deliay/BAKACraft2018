package com.mcbaka.bakacraft.Util;

public class Holder<T> {
    Object target;
    public Holder(Object target) {
        this.target = target;
    }

    public void replace(T t) {
        target = t;
    }

    public T target() {
        return (T)target;
    }
}
