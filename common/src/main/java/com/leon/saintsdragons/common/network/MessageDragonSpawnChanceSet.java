package com.leon.saintsdragons.common.network;

import com.leon.saintsdragons.server.data.DragonSpawnPreferenceSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Sent by the client when the player confirms a new value on the Dragon Spawn Menu slider.
 */
public class MessageDragonSpawnChanceSet {
    private final float multiplier;

    public MessageDragonSpawnChanceSet(float multiplier) {
        this.multiplier = Math.max(0.0f, Math.min(1.0f, multiplier));
    }

    public static void encode(MessageDragonSpawnChanceSet message, FriendlyByteBuf buffer) {
        buffer.writeFloat(message.multiplier);
    }

    public static MessageDragonSpawnChanceSet decode(FriendlyByteBuf buffer) {
        return new MessageDragonSpawnChanceSet(buffer.readFloat());
    }

    public static void handle(MessageDragonSpawnChanceSet message, ServerPlayer player) {
        if (player == null) {
            return;
        }
        DragonSpawnPreferenceSavedData data = DragonSpawnPreferenceSavedData.get(player.serverLevel());
        data.setMultiplier(player.getUUID(), message.multiplier);
        NetworkHandler.sendToPlayer(player, new MessageDragonSpawnChanceSync(data.getMultiplier(player.getUUID())));
    }
}
