package com.bbutner.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

/**
 * Class that displays the information of a hex or RGB color
 * @author bbutner
 * @version 1.0
 */
public class Color {

    /**
     * Handles the Color command
     * @param event Used for the channel
     * @param args The color supplied by the user
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        if (args.length > 1) {
            try {
                if (args.length == 4) {
                   
                    int red = Integer.parseInt(args[1].replace("(", "").replace(",", ""));
                    int green = Integer.parseInt(args[2].replace(",", ""));
                    int blue = Integer.parseInt(args[3].replace(")", ""));

                    EmbedBuilder colorEmbed = new EmbedBuilder().withTitle("Color Information")
                            .withDescription("\u200B")
                            .withColor(new java.awt.Color(red, green, blue))
                            .appendField("RGB (0-255)", String.format("(%s, %s, %s)", red, green, blue), false)
                            .appendField("RGB (0.0-1.0)", String.format("(%.6f, %.6f, %.6f)", red / 255.0, green / 255.0, blue / 255.0), false)
                            .appendField("Hexadecimal", String.format("#%02x%02x%02x", red, green, blue), false)
                            .appendField("Integer", String.valueOf(getIntegerFromColor(red, green, blue)), false);

                    event.getChannel().sendMessage(colorEmbed.build());
                } else {
                    java.awt.Color color = java.awt.Color.decode(args[1]);

                    EmbedBuilder colorEmbed = new EmbedBuilder().withTitle("Color Information")
                            .withDescription("\u200B")
                            .withColor(color)
                            .appendField("RGB (0-255)", String.format("(%s, %s, %s)", color.getRed(), color.getGreen(), color.getBlue()), false)
                            .appendField("RGB (0.0-1.0)", String.format("(%.6f, %.6f, %.6f)", color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0), false)
                            .appendField("Hexadecimal", String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()), false)
                            .appendField("Integer", String.valueOf(getIntegerFromColor(color.getRed(), color.getGreen(), color.getBlue())), false);

                    event.getChannel().sendMessage(colorEmbed.build());
                }
            } catch (NumberFormatException ex) {
                event.getChannel().sendMessage("I'm sorry but I could not decode your color!");
            }
        } else {
            event.getChannel().sendMessage("Please supply me with a color.");
        }
    }

    private static int getIntegerFromColor(int red, int green, int blue) {
       
        red = (red << 16) & 0x00FF0000;
        green = (green << 8) & 0x0000FF00;
        blue = blue & 0x000000FF;

        return 0xFF000000 | red | green | blue;
    }

}
