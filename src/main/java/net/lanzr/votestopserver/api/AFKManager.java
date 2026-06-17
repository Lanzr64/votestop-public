package net.lanzr.votestopserver.api;

import net.lanzr.votestopserver.Config;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKManager {
//    玩家操作记录表，uuid:最后操作tick
    private static final Map<UUID, Integer> lastActionMap = new HashMap<>();

//    记录玩家活动
    public static void updatePlayerActivity(UUID uuid) {
        lastActionMap.put(uuid, ServerLifecycleHooks.getCurrentServer().getTickCount());
    }

    public static boolean isAFK(ServerPlayer player) {
        int lastAction = lastActionMap.getOrDefault(player.getUUID(), 0);
        return (ServerLifecycleHooks.getCurrentServer().getTickCount() - lastAction) > Config.afkThresholdMinutes * 60 * 20;
    }

    public static void removePlayer(UUID uuid) {
        lastActionMap.remove(uuid);
    }
}