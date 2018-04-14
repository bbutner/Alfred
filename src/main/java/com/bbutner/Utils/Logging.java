package com.bbutner.Utils;

import com.bbutner.Command;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for logging
 * @author bbutner
 * @version 1.0
 */
public class Logging {

    private static ArrayList<String> logMessages = new ArrayList<>();

    /**
     * Log user-accessed commands
     * @param sender The user that sent the command
     * @param command The command that was run
     */
    public static void infoCommand(IUser sender, Command command) {
        String log = String.format("[COMMAND] %s %s (%s) has run the command: %s", getTimestamp(), sender.getName(), sender.getLongID(), command.getName());

        logMessages.add(log);
        System.out.println(log);
    }

    /**
     * Bot logs
     * @param message The message to be logged
     */
    public static void infoBot(String message) {
        String log = String.format("[BOT] %s JUNEAU: %s", getTimestamp(), message);

        logMessages.add(log);
        System.out.println(log);
    }

    /**
     * Logs any message sent by a user
     * @param message The message that was sent
     */
    public static void infoMessage(IMessage message) {
        String log = String.format("[MESSAGE] %s %s (%s): %s", getTimestamp(), message.getAuthor().getName(), message.getAuthor().getLongID(), message.getContent());

        logMessages.add(log);
        System.out.println(log);
    }

    private static String getTimestamp() {
        DateTime time = DateTime.now();
        String dateFormat = DateTimeFormat.forPattern("MM-dd").print(time);
        String timeFormat = DateTimeFormat.forPattern("HH:mm:ss").print(time);

        return String.format("[%s @ %s] ||", dateFormat, timeFormat);
    }

    /**
     * Gets the recent log messages
     * @return the log messages
     */
    public static ArrayList<String> getRecentLogs() {
        ArrayList<String> logs = new ArrayList<>();
        int index = logMessages.size() - 1;

        for (int ctr = 0; ctr < 15; ctr++) {
            if (index > -1) {
                logs.add(logMessages.get(index));
            }
            index--;
        }

        Collections.reverse(logs);

        return logs;
    }

}
