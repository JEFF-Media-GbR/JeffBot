package com.jeff_media.jeffbot.command;

public enum CommandResult {
    OK, NOT_FOUND, WRONG_USAGE, NO_PERMISSION, NOT_FROM_CONSOLE;

    public boolean isSucces() {
        return this == OK;
    }
}
