package com.jeff_media.jeffbot.command.commands;

import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.CommandSender;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

public class CommandRenameChannel extends Command {

    @Override
    public CommandResult execute(CommandSender sender, Message message, String[] args) {
        TextChannel channel = message.getChannel().asTextChannel();
        String newTitle = String.join(" ", args).replace(' ', '\u00A0');
        channel.getManager().setName(newTitle).queue();
        return CommandResult.OK;
    }

    @Override
    public String getDescription() {
        return "Renames the current channel.";
    }

    @Override
    public @Nullable String getPermission() {
        return "command.renamechannel";
    }

    @Override
    public @Nullable String getUsage() {
        return "<new name>";
    }
}
