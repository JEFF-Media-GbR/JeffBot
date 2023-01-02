package com.jeff_media.jeffbot.command;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.Logger;
import com.jeff_media.jeffbot.exceptions.NotAMentionException;
import lombok.Getter;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends CommandSender {
    private static final JeffBot MAIN = JeffBot.getInstance();
    private static final Pattern REGEX_USER_MENTION = Pattern.compile("<@([0-9]+)>");
    @Getter private final String channelId;
    @Getter private final TextChannel channel;
    private final Set<String> permissions;
    private final Member member;

    public User(MessageReceivedEvent event) {
        this.channelId = event.getChannel().getId();
        this.channel = event.getChannel().asTextChannel();
        this.member = event.getMember();
        this.permissions = MAIN.getPermissionCache().getPermissions(member);
    }

    @Override
    public boolean hasPermission(String permission) {
        return permission == null || permissions.contains(permission);
    }

    private static String getIdFromMention(String mention) {
        Matcher matcher = REGEX_USER_MENTION.matcher(mention);
        if(matcher.matches()) {
            Logger.debug("It matches!");
            return matcher.group(1);
        } else {
            Logger.debug("It does not match :(");
            return null;
        }
    }

    public static Member fromString(Guild guild, String string) throws NotAMentionException {
        String id = null;
        Logger.debug("Trying to get User from Mention or ID: " + string);
        try {
            Long.parseLong(string);
            id = string;
            Logger.debug("It's a valid ID!");
        } catch (NumberFormatException exception) {
            Logger.debug("It's not a valid ID, but maybe it's a mention...");
            id = getIdFromMention(string);
            Logger.debug("Maybe this is a mention? " + id);
        }
        if(id == null) throw new NotAMentionException(string);
        net.dv8tion.jda.api.entities.User user = MAIN.getJda().getUserById(id);
        if(user == null) throw new NotAMentionException(string);
        Member member = guild.getMember(user);
        if(member == null) throw new NotAMentionException(string);
        return member;
    }

    @Override
    public void message(String... message) {
        TextChannel channel = MAIN.getJda().getTextChannelById(channelId);
        if(channel != null) {
            channel.sendMessage(String.join("\n",message)).queue();
        }
    }

    @Override
    public void message(MessageEmbed embed) {
        getChannel().sendMessageEmbeds(embed).queue();
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return member.getEffectiveName() + "[id=" + member.getId() + ", channel=" + channelId;
    }

}
