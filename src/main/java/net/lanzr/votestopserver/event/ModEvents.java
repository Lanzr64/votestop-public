package net.lanzr.votestopserver.event;

import net.lanzr.votestopserver.api.AFKManager;
import net.lanzr.votestopserver.api.VoteManager;
import net.lanzr.votestopserver.command.VoteCommand;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.awt.print.Printable;


@EventBusSubscriber
public class ModEvents {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        VoteCommand.register(event);
    }

// ---------------------------------------------------------
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        if (event.getPlayer() instanceof FakePlayer) {
            return;
        }
        AFKManager.updatePlayerActivity(event.getPlayer().getUUID());
    }

    private static void interactListenHandle(PlayerInteractEvent event) {
        if (event.getEntity() instanceof FakePlayer) return;
        if (!event.getLevel().isClientSide && event.getHand() != InteractionHand.OFF_HAND) {
            AFKManager.updatePlayerActivity(event.getEntity().getUUID());
        }
    }
    @SubscribeEvent
    public static void onInteractBlock(PlayerInteractEvent.RightClickBlock event) {
        interactListenHandle(event);
    }

    @SubscribeEvent
    public static void onInteractItem(PlayerInteractEvent.RightClickItem event) {
        interactListenHandle(event);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AFKManager.updatePlayerActivity(event.getEntity().getUUID());
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            AFKManager.removePlayer(event.getEntity().getUUID());
        }
    }
// ---------------------------------------------------------

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        VoteManager.tick(ServerLifecycleHooks.getCurrentServer());
    }
}
