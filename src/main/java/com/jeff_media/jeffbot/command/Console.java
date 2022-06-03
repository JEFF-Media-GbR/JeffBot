package com.jeff_media.jeffbot.command;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public class Console extends CommandSender{

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void message(String... message) {
        for(String line : message) {
            System.out.println(line);
        }
    }

    @Override
    public void message(MessageEmbed embed) {
        List<String> msg = new ArrayList<>();
        if(embed.getTitle() != null) {
            msg.add("** " + embed.getTitle() + " **");
            msg.add("");
        }
        if(embed.getDescription() != null) {
            msg.add(embed.getDescription());
        }
        for(MessageEmbed.Field field : embed.getFields()) {
            msg.add("* " + field.getName() + " *");
            msg.add(field.getValue());
            msg.add("");
        }
        CommandSender.CONSOLE.message(msg);
    }

    @Override
    public String toString() {
        return "Console";
    }
}
