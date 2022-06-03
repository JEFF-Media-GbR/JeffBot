package com.jeff_media.jeffbot.discord;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.command.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    private static final JeffBot main = JeffBot.getInstance();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(!event.getMessage().getContentRaw().startsWith(main.getConfig().getCommandPrefix())) return;
        if(main.getConfig().isRemoveCommandMessages()) event.getMessage().delete().queue();
        String[] command = event.getMessage().getContentRaw().split(" ");
        command[0] = command[0].substring(main.getConfig().getCommandPrefix().length());
        main.handleCommand(new User(event),event.getMessage(), command);
    }
}
