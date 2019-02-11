package com.mcbaka.bakacraft.Modules.Survival.MagicStoneHelper;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class PersistentKeys {
    public static Key<Value<Integer>> STONE_LEVEL = DummyObjectProvider.createExtendedFor(Key.class, "STONE_LEVEL");
    public static Key<Value<String>> CAST_TYPE = DummyObjectProvider.createExtendedFor(Key.class, "CAST_TYPE");
    public static Key<Value<Integer>> CAST_COUNT = DummyObjectProvider.createExtendedFor(Key.class, "CAST_COUNT");
    public static Key<Value<Integer>> TELEPORT_TYPE = DummyObjectProvider.createExtendedFor(Key.class, "TELEPORT_TYPE");
    public final static TypeToken<Location<World>> TELEPORT_POS_TOKEN = new TypeToken<Location<World>>() { private static final long serialVersionUID = -1; };
    public static Key<Value<Location<World>>> TELEPORT_POS = DummyObjectProvider.createExtendedFor(Key.class, "TELEPORT_POS");
    public static Key<Value<UUID>> STONE_UUID = DummyObjectProvider.createExtendedFor(Key.class, "STONE_UUID");
}
