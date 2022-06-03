package com.jeff_media.jeffbot.config;

public class ConfConfig extends Config {
    public ConfConfig() {
        super("config.yml");
    }

    public String getCommandPrefix() {
        return (String) map.get("command-prefix");
    }

    public boolean isRemoveCommandMessages() {
        return (boolean) map.get("remove-command-messages");
    }
}
