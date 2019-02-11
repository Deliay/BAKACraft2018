package com.mcbaka.bakacraft.Util;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;

public class PersistentHelper {
    public static <T extends Key.Builder<?, ?>> T BuildPersistentKeysOfT(T dummyBuilder, String id, String name) {
        dummyBuilder.id(id).name(name).query(DataQuery.of(name));
        return dummyBuilder;
    }
}
