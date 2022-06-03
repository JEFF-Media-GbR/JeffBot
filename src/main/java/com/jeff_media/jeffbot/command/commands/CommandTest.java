package com.jeff_media.jeffbot.command.commands;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.CommandSender;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

public class CommandTest extends Command {

    private static final JeffBot main = JeffBot.getInstance();
    @Override
    public CommandResult execute(CommandSender sender, Message message, String[] args) {
        sender.message("Donor Role: " + main.getJda().getRoleById("857258968694652998").getAsMention());
        return CommandResult.OK;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public @Nullable String getUsage() {
        return null;
    }
}
