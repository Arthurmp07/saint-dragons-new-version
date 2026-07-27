package com.leon.saintsdragons.common.network;

import com.leon.saintsdragons.platform.Services;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Sent by the server to tell the client the player's current spawn-chance preference.
 */
public class MessageDragonSpawnChanceSync {
    private final float multiplier;

    public MessageDragonSpawnChanceSync(float multiplier) {
        this.multiplier = multiplier;
    }

    public float multiplier() {
        return multiplier;
    }

    public static void encode(MessageDragonSpawnChanceSync message, FriendlyByteBuf buffer) {
        buffer.writeFloat(message.multiplier);
    }

    public static MessageDragonSpawnChanceSync decode(FriendlyByteBuf buffer) {
        return new MessageDragonSpawnChanceSync(buffer.readFloat());
    }

    public static void handle(MessageDragonSpawnChanceSync message) {
        Services.PLATFORM.runOnClient(() -> com.leon.saintsdragons.client.network.ClientPacketHandlers.handleDragonSpawnChanceSync(message));
    }
}
