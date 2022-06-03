package com.jeff_media.jeffbot.command;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Collection;

public abstract class CommandSender {

    public static final Console CONSOLE = new Console();

    public abstract boolean hasPermission(String permission);

    public abstract void message(String... message);

    public void message(Collection<String> message) {
        this.message(message.toArray(String[]::new));
    }

    public abstract void message(MessageEmbed embed);

}
