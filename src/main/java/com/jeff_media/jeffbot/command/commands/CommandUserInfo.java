package com.jeff_media.jeffbot.command.commands;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.CommandSender;
import com.jeff_media.jeffbot.exceptions.NotAMentionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class CommandUserInfo extends Command {

    private static final JeffBot MAIN = JeffBot.getInstance();
    private static final String USER_NOT_FOUND_MESSAGE = "Couldn't find any user with the given ID.";

    @Override
    public CommandResult execute(CommandSender sender, Message message, String[] args) {
        if (args.length == 0) return CommandResult.WRONG_USAGE;
        Member member;
        try {
            Guild guild;
            if(message != null) {
                guild = message.getGuild();
            } else {
                guild = MAIN.getJda().getGuilds().get(0);
            }
            member = com.jeff_media.jeffbot.command.User.fromString(guild, args[0]);
        } catch (NotAMentionException e) {
            sender.message(USER_NOT_FOUND_MESSAGE);
            return CommandResult.OK;
        }
        LinkedHashMap<String, String> perms = MAIN.getPermissionCache().getPermissionsWithOrigin(member);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("JeffBot: User information");
        eb.addField("Name", member.getEffectiveName(), true);
        eb.addField("ID", member.getId(), true);
        eb.addField("Permissions", perms.entrySet().stream().map(stringStringEntry -> stringStringEntry.getKey() + " (" + stringStringEntry.getValue() + ")").collect(Collectors.joining("\n")), false);
        sender.message(eb.build());
        return CommandResult.OK;
    }

    @Override
    public String getDescription() {
        return "Shows information about a given user.";
    }

    @Override
    public @Nullable String getPermission() {
        return "command.userinfo";
    }

    @Override
    public @Nullable String getUsage() {
        return "<@user>|<id>";
    }
}
