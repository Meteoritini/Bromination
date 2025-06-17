package me.meteoritini.bromination.util;

import me.meteoritini.bromination.BrominationClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    private static final Logger logger = LoggerFactory.getLogger(BrominationClient.MOD_ID);

    public static void log(String s) {log(s, 0);}
    public static void log(String s, int level) {
        switch (level) {
            case 0 -> logger.info(s);
            case 1 -> logger.warn(s);
            case 2 -> logger.error(s);
        }
    }
}
