package com.jeff_media.jeffbot;

import com.jeff_media.jeffbot.command.CommandSender;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        final JeffBot bot;

        try {
            bot = new JeffBot();
        } catch (RuntimeException ignored) {
            ignored.printStackTrace();
            return;
        }

        final Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("> ");
            String[] command = scanner.nextLine().split(" ");
            bot.handleCommand(CommandSender.CONSOLE, null, command);
        }
    }
}
