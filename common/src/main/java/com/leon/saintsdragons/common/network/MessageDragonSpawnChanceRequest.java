package com.leon.saintsdragons.common.network;

import com.leon.saintsdragons.server.data.DragonSpawnPreferenceSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Sent by the client when the Dragon Spawn Menu is opened, to fetch the player's
 * currently stored spawn-chance preference from the server.
 */
public class MessageDragonSpawnChanceRequest {
    public static final MessageDragonSpawnChanceRequest INSTANCE = new MessageDragonSpawnChanceRequest();

    private MessageDragonSpawnChanceRequest() {
    }

    public static void encode(MessageDragonSpawnChanceRequest message, FriendlyByteBuf buffer) {
    }

    public static MessageDragonSpawnChanceRequest decode(FriendlyByteBuf buffer) {
        return INSTANCE;
    }

    public static void handle(MessageDragonSpawnChanceRequest message, ServerPlayer player) {
        if (player == null) {
            return;
        }
        float multiplier = DragonSpawnPreferenceSavedData.get(player.serverLevel()).getMultiplier(player.getUUID());
        NetworkHandler.sendToPlayer(player, new MessageDragonSpawnChanceSync(multiplier));
    }
}
