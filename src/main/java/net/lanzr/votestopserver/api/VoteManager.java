package net.lanzr.votestopserver.api;

import net.lanzr.votestopserver.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class VoteManager {
    public static boolean isVoteActive = false;
    public static int remainingSeconds = 0;
    private static int tickCount = 20;
    private static final Set<UUID> yesVoters = new HashSet<>();
    public static void startVote(int seconds, ServerPlayer initiator) {
        if (initiator.getServer().getTickCount() < Config.startLimitMinutes * 60 * 20) { // 事件小于开服限制时间则不能开服
            initiator.sendSystemMessage(
                    Component.literal(
                            String.format(Config.msg_failed,
                            Config.startLimitMinutes,
                            (Config.startLimitMinutes * 60 - initiator.getServer().getTickCount() / 20)))
                    ,
                    true);
            return;
        }
        if (isVoteActive) {
            castVote(initiator, true);
        } else {
            isVoteActive = true;
            remainingSeconds = seconds;
            tickCount = 20;
            yesVoters.clear();

            castVote(initiator, true);
            broadcastStatus(initiator.getServer());
        }
    }
    public static void castVote(ServerPlayer player, boolean isYes) {
        if (!isVoteActive) return;
        if (!isYes) {
            stopVote(Component.literal(
                    String.format(Config.msg_cancel_player,player.getScoreboardName()))
            );
            return;
        }
        yesVoters.add(player.getUUID());
        checkConditions(player.getServer());
    }
    public static void checkConditions(MinecraftServer server) {
        if (!isVoteActive) return;
        List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();

        long activePlayerCount = allPlayers.stream()
                .filter(p -> !AFKManager.isAFK(p))
                .count();

        long activeYesVotes = yesVoters.stream()
                .filter(uuid -> {
                    ServerPlayer p = server.getPlayerList().getPlayer(uuid);
                    return p != null && !AFKManager.isAFK(p);
                }).count();

//        if (activeYesVotes >= activePlayerCount && activePlayerCount > 0) {
        if (activeYesVotes >= activePlayerCount) {
            executeRestart(server);
        }
    }
    public static void tick(MinecraftServer server) {
        if (!isVoteActive) return;
        tickCount--;
        if (tickCount <= 0)  {
            tickCount = 20;
            remainingSeconds--;
            if (remainingSeconds <= 0) {
                stopVote(Component.literal(Config.msg_cancel_timeout));
            } else if (remainingSeconds % Config.broadcastIntervalSeconds == 0) {
                broadcastStatus(server);
            }
        }
    }
    private static void broadcastStatus(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        long activeCount = players.stream().filter(p -> !AFKManager.isAFK(p)).count();
        server.getPlayerList().broadcastSystemMessage(
                Component.literal(String.format(Config.msg_interval_broadcast,
                        yesVoters.size(), activeCount, remainingSeconds)),
                true);
    }
    public static void stopVote(Component reason) {
        isVoteActive = false;
        yesVoters.clear();
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastSystemMessage(
                Component.literal(String.format(Config.msg_cancel_prefix)).append(reason),
                true);
    }
    private static void executeRestart(MinecraftServer server) {
        isVoteActive = false;

        server.getPlayerList().broadcastSystemMessage(Component.literal(Config.msg_success),
                true);
        server.halt(false);
    }
}