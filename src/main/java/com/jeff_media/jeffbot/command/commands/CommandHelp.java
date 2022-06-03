package com.jeff_media.jeffbot.command.commands;

import com.jeff_media.jeffbot.JeffBot;
import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.CommandSender;
import com.jeff_media.jeffbot.command.Console;
import com.jeff_media.jeffbot.command.User;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandHelp extends Command {

    private static final JeffBot main = JeffBot.getInstance();

    @Override
    public CommandResult execute(CommandSender sender, Message message, String[] args) {
        if (sender instanceof User user) {
            if (args.length == 0) {
                return listAllCommandsToUser(user);
            } else {
                return showCommandHelpToUser(user, args[0].toLowerCase(Locale.ROOT));
            }
        } else {
            if (args.length == 0) {
                return listAllCommandsToConsole();
            } else {
                return showCommandHelpToConsole(args[0].toLowerCase(Locale.ROOT));
            }
        }

    }

    @NotNull
    private CommandResult listAllCommandsToUser(User sender) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("JeffBot: Available Commands");
        eb.setDescription("Here's a list of all available commands that you have permission for.\n\nEnter `" + Command.withPrefix(sender, "help") + " <command>` to get more information about a specific command.");
        getAvailableCommands(sender).forEach((name, command) -> {
            eb.addField(Command.withPrefix(sender, name), command.getDescription(), false);
        });
        sender.getChannel().sendMessageEmbeds(eb.build()).queue();
        return CommandResult.OK;
    }

    private CommandResult showCommandHelpToUser(User sender, String commandName) {
        Command command = main.getCommandMap().get(commandName);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("JeffBot: Command information");
        if (command == null || !sender.hasPermission(command.getPermission())) {
            eb.setDescription("Unknown command: \"" + Command.withPrefix(sender, commandName) + "\". Enter \"" + Command.withPrefix(sender, "help") + "\" for a list of available commands.");
            sender.getChannel().sendMessageEmbeds(eb.build()).queue();
            return CommandResult.OK;
        }
        eb.addField("Usage",Command.withPrefix(sender,commandName) + (command.getUsage() != null ? " " + command.getUsage() : ""),false);
        if (command.getDescription() != null) {
                eb.addField("Description",command.getDescription(), false);
        }
        if(command.getPermission() != null) {
                eb.addField("Required permission",command.getPermission(),false);
        }
        sender.getChannel().sendMessageEmbeds(eb.build()).queue();
        return CommandResult.OK;
    }

    private CommandResult listAllCommandsToConsole() {
        List<String> msg = new ArrayList<>();
        msg.add("Available commands: ");
        msg.add(String.join(", ", new ArrayList<String>(getAvailableCommands(CommandSender.CONSOLE).keySet())));
        CommandSender.CONSOLE.message(msg);
        return CommandResult.OK;
    }

    private CommandResult showCommandHelpToConsole(String commandName) {
        Command command = main.getCommandMap().get(commandName);
        Console sender = CommandSender.CONSOLE;
        if (command == null) {
            sender.message("Unknown command: \"" + Command.withPrefix(sender, commandName) + "\". Enter \"" + Command.withPrefix(sender, commandName) + "\" for a list of available commands.");
            return CommandResult.OK;
        }
        String usage = command.getUsage() == null ? "" : " " + command.getUsage();
        List<String> message = new ArrayList<>();
        message.addAll(Arrays.asList("Usage:", Command.withPrefix(sender, commandName) + usage, ""));
        if (command.getDescription() != null) {
            message.addAll(Arrays.asList("Description:", command.getDescription(), ""));
        }
        if(command.getPermission() != null) {
                message.addAll(Arrays.asList("Required Permission: ", command.getPermission(),""));
        }
        sender.message(message.toArray(new String[0]));
        return CommandResult.OK;
    }

    private static Map<String, Command> getAvailableCommands(CommandSender sender) {
        LinkedHashMap<String, Command> map = new LinkedHashMap();
        main.getCommandMap().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEachOrdered(entry -> {
            if (sender.hasPermission(entry.getValue().getPermission())) {
                map.put(entry.getKey(), entry.getValue());
            }
        });
        return map;
    }

    @Override
    public String getDescription() {
        return "Shows this help message";
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public @Nullable String getUsage() {
        return "[command]";
    }
}
