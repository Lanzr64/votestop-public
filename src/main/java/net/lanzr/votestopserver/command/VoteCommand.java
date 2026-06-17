package net.lanzr.votestopserver.command;

import com.mojang.brigadier.Command;
import net.lanzr.votestopserver.api.AFKManager;
import net.lanzr.votestopserver.api.VoteManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;

import java.util.List;

public class VoteCommand {
    public static void register(RegisterCommandsEvent event) {
        Command<CommandSourceStack> voteYesCommand = ctx -> {
            VoteManager.startVote(60, ctx.getSource().getPlayerOrException());
            return 1;
        };
        event.getDispatcher().register(Commands.literal("tyj")
            .then(Commands.literal("votestop").executes(voteYesCommand)
                .then(Commands.literal("no").executes(ctx -> {
                    VoteManager.castVote(ctx.getSource().getPlayerOrException(), false);
                    return 1;
                }))
                .then(Commands.literal("yes").executes(voteYesCommand))
            )
        );
    }
}