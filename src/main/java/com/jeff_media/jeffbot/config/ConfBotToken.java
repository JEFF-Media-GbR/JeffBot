package com.jeff_media.jeffbot.config;

public class ConfBotToken extends Config {

    public ConfBotToken() {
        super("bot-token.yml");
    }

    public String getBotToken() {
        return (String) map.get("bot-token");
    }
}
