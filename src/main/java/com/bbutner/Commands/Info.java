package com.bbutner.Commands;

import com.bbutner.Core.Alfred;
import com.bbutner.Core.Listeners.MessageHandler;
import sx.blah.discord.Discord4J;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

/**
 * Class that displays Alfred's information
 * @author bbutner
 * @version 1.0
 */
public class Info {

    /**
     * Handles the Info command
     * @param event used for the channel
     * @param args TO BE IMPLEMENTED
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        switch (args.length) {
            default: {
                showGeneralInformation(event);
                break;
            }
        }
    }

    private static void showGeneralInformation(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder().withTitle("Information")
                .withDescription("General information and statistics.")
                .withColor(Alfred.EMBED_COLOR)
                .withThumbnail(Alfred.getClient().getOurUser().getAvatarURL())
                .appendField("\u200B", "\u200B", false)
                .appendField("Java Version", System.getProperty("java.version"), true)
                .appendField("API Version", Discord4J.VERSION, true)
                .appendField("\u200B", "\u200B", false)
                .appendField("Messages Received", String.valueOf(MessageHandler.getMessageCount()), true)
                .appendField("Commands Received", String.valueOf(MessageHandler.getCommandCount()), true)
                .appendField("Users", String.valueOf(Alfred.getClient().getUsers().size()), true)
                .appendField("\u200B", "\u200B", false)
                .appendField("Uptime", getUptime(), true);

        event.getChannel().sendMessage(embed.build());
    }

    private static String getUptime() {
        long totalSeconds = (System.currentTimeMillis() - Alfred.LAUNCH_TIME) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = totalSeconds / 60 % 60;
        long hours = totalSeconds / 3600;
        return (hours < 10 ? + hours : hours) + "h " + (minutes < 10 ? + minutes : minutes) + "m " + (seconds < 10 ? + seconds : seconds) + "s";
    }

}
