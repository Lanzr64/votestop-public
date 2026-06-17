package net.lanzr.votestopserver.command;

import com.mojang.brigadier.Command;
import net.lanzr.votestopserver.api.VoteManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


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