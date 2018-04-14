package com.bbutner.Commands;

import com.bbutner.Utils.Logging;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Class that displays recent logs
 * @author bbutner
 * @version 1.0
 */
public class Log {

    /**
     * Handles the Log command
     * @param event Used for the channel
     * @param args Not used
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        String logMessage = "**Most recent logs:**\r\n\r\n";

        for (String log : Logging.getRecentLogs()) {
            logMessage += String.format("`%s`\r\n", log);
        }

        event.getChannel().sendMessage(logMessage);
    }

}
