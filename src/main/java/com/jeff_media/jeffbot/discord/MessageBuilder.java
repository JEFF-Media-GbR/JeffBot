package com.jeff_media.jeffbot.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {

    List<String> text = new ArrayList<>();
    EmbedBuilder embed = new EmbedBuilder();

    public MessageBuilder(@Nullable String title, @Nullable String description) {
        if(title != null) {
            text.add("**" + title + "");
            text.add("");
            embed.setTitle(title);
        }
        if(description != null) {
            text.add(description);
            embed.setDescription(description);
        }
    }


}
