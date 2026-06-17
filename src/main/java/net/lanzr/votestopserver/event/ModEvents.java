package net.lanzr.votestopserver.event;

import net.lanzr.votestopserver.api.AFKManager;
import net.lanzr.votestopserver.api.VoteManager;
import net.lanzr.votestopserver.command.VoteCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber
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

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent event) {
        if (event.getEntity() instanceof FakePlayer) {
            return;
        }
        if (!event.getLevel().isClientSide) {
            AFKManager.updatePlayerActivity(event.getEntity().getUUID());
        }
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
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            VoteManager.tick(ServerLifecycleHooks.getCurrentServer());
        }
    }
}
