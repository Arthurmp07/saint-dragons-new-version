package com.leon.saintsdragons.server.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Persistent per-player multiplier applied on top of the global dragon spawn weights.
 * 1.0 = normal behavior, 0.0 = dragons never naturally spawn near that player.
 * Stored on the overworld so it is shared across dimensions.
 */
public class DragonSpawnPreferenceSavedData extends SavedData {
    private static final String DATA_NAME = "saintsdragons_spawn_preferences";
    public static final float DEFAULT_MULTIPLIER = 1.0f;

    private final Map<UUID, Float> multiplierByPlayer = new HashMap<>();

    public static DragonSpawnPreferenceSavedData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
                DragonSpawnPreferenceSavedData::load,
                DragonSpawnPreferenceSavedData::new,
                DATA_NAME
        );
    }

    public float getMultiplier(UUID playerId) {
        return multiplierByPlayer.getOrDefault(playerId, DEFAULT_MULTIPLIER);
    }

    public void setMultiplier(UUID playerId, float multiplier) {
        float clamped = Math.max(0.0f, Math.min(1.0f, multiplier));
        if (clamped >= DEFAULT_MULTIPLIER) {
            if (multiplierByPlayer.remove(playerId) != null) {
                setDirty();
            }
            return;
        }
        Float previous = multiplierByPlayer.put(playerId, clamped);
        if (previous == null || Math.abs(previous - clamped) > 1.0e-4f) {
            setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Float> entry : multiplierByPlayer.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("Player", entry.getKey());
            entryTag.putFloat("Multiplier", entry.getValue());
            list.add(entryTag);
        }
        tag.put("Preferences", list);
        return tag;
    }

    public static DragonSpawnPreferenceSavedData load(CompoundTag tag) {
        DragonSpawnPreferenceSavedData data = new DragonSpawnPreferenceSavedData();
        if (tag.contains("Preferences", Tag.TAG_LIST)) {
            ListTag list = tag.getList("Preferences", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entryTag = list.getCompound(i);
                if (entryTag.hasUUID("Player") && entryTag.contains("Multiplier")) {
                    data.multiplierByPlayer.put(entryTag.getUUID("Player"), entryTag.getFloat("Multiplier"));
                }
            }
        }
        return data;
    }
}
