package com.jeff_media.jeffbot.command.commands;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.CommandSender;
import com.jeff_media.jeffbot.command.User;
import com.jeff_media.jeffbot.discord.embed.Embed;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class CommandEmbed extends Command {

    private static final JeffBot main = JeffBot.getInstance();

    @Override
    public CommandResult execute(CommandSender sender, Message message, String[] args) {
        if(args.length == 0) {
            try {
                sender.message("Available embeds: " + Files.list(new File("embeds").toPath()).map(path -> path.getFileName().toString().replace(".yml","")).collect(Collectors.joining(", ")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return CommandResult.WRONG_USAGE;
        }
        String filename = args[0] + ".yml";
        String channelId = null;
        if(sender instanceof User user) {
            channelId = user.getChannelId();
        }
        if(args.length > 1) {
            channelId = args[1];
        }
        if(channelId == null) {
            sender.message("You must enter a channel id when running this from console.");
            return CommandResult.WRONG_USAGE;
        }
        TextChannel channel = main.getJda().getTextChannelById(channelId);
        if(channel == null) {
            sender.message("Could not find channel with given id: " + channelId);
            return CommandResult.OK;
        }
        File file = new File(new File("embeds"),filename);
        try {
            MessageEmbed embed = Embed.fromFile(file);
            channel.sendMessageEmbeds(embed).queue();
        } catch (FileNotFoundException e) {
            sender.message("Embed file not found: " + filename);
        } catch (IOException e) {
            sender.message("Could not read embed file: " + filename);
        }

        Command.deleteMessage(message);

        return CommandResult.OK;
    }

    @Override
    public String getDescription() {
        return "Sends an embed message";
    }

    @Override
    public @Nullable String getPermission() {
        return "command.embed";
    }

    @Override
    public @Nullable String getUsage() {
        return "<list> | <filename> [channelId]";
    }
}
