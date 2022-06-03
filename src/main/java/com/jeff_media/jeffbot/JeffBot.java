package com.jeff_media.jeffbot;

import com.jeff_media.jeffbot.command.CommandResult;
import com.jeff_media.jeffbot.command.commands.*;
import com.jeff_media.jeffbot.command.CommandSender;
import com.jeff_media.jeffbot.config.ConfBotToken;
import com.jeff_media.jeffbot.config.ConfConfig;
import com.jeff_media.jeffbot.discord.MessageListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.*;
import java.util.function.Supplier;

public class JeffBot {

    private static JeffBot instance;
    private ConfBotToken confBotToken;
    @Getter private ConfConfig config;
    @Getter private JDA jda;
    @Getter private Map<String, Command> commandMap;
    @Getter private PermissionCache permissionCache;

    public JeffBot() throws RuntimeException {
        instance = this;
        loadConfigs();
        loadCommands();
        loadPermissions();
        saveDefaultEmbeds();

        JDABuilder builder = JDABuilder.createDefault(confBotToken.getBotToken());
        builder.enableIntents(Arrays.asList(GatewayIntent.values()));
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        try {
            jda = builder.build();
            jda.awaitReady();
        } catch (LoginException e) {
            Logger.error("Could not login to Discord, check if your bot-token is setup correctly in \"bot-token.yml\"",e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(jda.getStatus() != JDA.Status.CONNECTED) {
            throw new RuntimeException();
        }

        jda.addEventListener(new MessageListener());
    }

    public void reload() {

    }

    private void loadPermissions() {
        permissionCache = new PermissionCache();
    }

    private void saveDefaultEmbeds() {
        File embedFolder = new File("embeds");
        if(!embedFolder.exists()) embedFolder.mkdirs();

        for(String fileName : new String[] {"test.yml", "donate.yml","verify-purchase.yml"}) {
            File file = new File(embedFolder, fileName);
            if(!file.exists()) {
                Utils.saveResource("embeds/" + fileName, false);
                Logger.info("Saved default embed file \"" + file.getName() + "\"");
            }
        }
    }

    private void loadCommands() {
        commandMap = new HashMap<>();
        addCommand("help",CommandHelp::new);
        addCommand("embed", CommandEmbed::new);
        addCommand("userinfo",CommandUserInfo::new);
        //addCommand("test", CommandTest::new);
    }

    private void addCommand(String name, Supplier<Command> commandSupplier) {
        commandMap.put(name.toLowerCase(Locale.ROOT), commandSupplier.get());
    }

    public void handleCommand(@NotNull CommandSender sender, @Nullable Message message, @NotNull String... args) {
        if(args.length==0) return;

        String commandName = args[0].toLowerCase(Locale.ROOT);
        Command command = commandMap.get(commandName);

        if(command == null) {
            CommandSender.CONSOLE.message("Unknown command: \"" + Command.withPrefix(CommandSender.CONSOLE, commandName) + "\". Enter \"" + Command.withPrefix(CommandSender.CONSOLE, "help") + "\" for a list of available commands.");
            return;
        }

        if(!sender.hasPermission(command.getPermission())) {
            sender.message("You do not have the required permission (" + command.getPermission() + ") to run this command.");
            return;
        }

        CommandResult result = command.execute(sender, message, Utils.shiftArray(args));

        if(result == CommandResult.WRONG_USAGE) {
            sender.message("Usage: " + commandName + " " + command.getUsage());
            return;
        } else if(result == CommandResult.NOT_FROM_CONSOLE) {
            sender.message("You cannot run this command from console.");
            return;
        }

        Logger.info(sender + " executed command: " + String.join(" ",args));

    }

    private void loadConfigs() {
        confBotToken = new ConfBotToken();
        config = new ConfConfig();
    }

    public static JeffBot getInstance() {
        return instance;
    }



}
