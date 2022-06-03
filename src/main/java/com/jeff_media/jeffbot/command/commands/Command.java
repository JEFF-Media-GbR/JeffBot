package com.jeff_media.jeffbot.command.commands;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.CommandSender;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

public abstract class Command {

    private static final JeffBot main = JeffBot.getInstance();

    public static String withPrefix(CommandSender sender, String command) {
        return sender == CommandSender.CONSOLE ? command : main.getConfig().getCommandPrefix() + command;
    }

    public static void deleteMessage(@Nullable Message message) {
        if (message != null) {
            message.delete().queue(unused -> {}, throwable -> {});
        }
    }

    public abstract CommandResult execute(CommandSender sender, Message message, String[] args);

    public abstract String getDescription();

    public abstract @Nullable String getPermission();

    public abstract @Nullable String getUsage();

}
