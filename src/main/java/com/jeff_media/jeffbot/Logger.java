package com.jeff_media.jeffbot;


import org.slf4j.LoggerFactory;

public class Logger {

    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(JeffBot.class);

    public static void info(String text) {
        logger.info(text);
    }

    public static void debug(String text) {
        logger.debug(text);
    }

    public static void warn(String text) {
        logger.warn(text);
    }

    public static void error(String text) {
        logger.error(text);
    }

    public static void error(String text, Throwable throwable) {
        logger.error(text, throwable);
    }
}
