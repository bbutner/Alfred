package com.bbutner.Commands;

import com.bbutner.Command;
import com.bbutner.Reflections;
import com.bbutner.Utils.Permissions;
import com.vdurmont.emoji.EmojiManager;
import com.bbutner.Core.Alfred;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class that sends the help dialog for commands
 * @author bbutner
 * @version 1.0
 */
public class Help {

    /**
     * Handles the Help command
     * @param event Used for the channel
     * @param args the command to get help for
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        if (args.length == 1) {
            listCommands(event);
        } else {
            try {
                getSpecificHelp(Integer.parseInt(args[1]), event);
            } catch (NumberFormatException ex) {
                if (Reflections.isCommand(args[1])) {
                    getSpecificHelp(args[1], event);
                } else {
                    event.getMessage().addReaction(EmojiManager.getForAlias("question"));
                }
            }
        }
    }

    private static void listCommands(MessageReceivedEvent event) {
        Map<String, Command> commands = getHelpPage(1);

        EmbedBuilder embed = new EmbedBuilder().withTitle("Command Help")
                .withDescription("All commands and how to use them.")
                .withThumbnail(Alfred.getClient().getOurUser().getAvatarURL())
                .withColor(Alfred.EMBED_COLOR)
                .withFooterText(String.format("Showing %s command(s) from page 1", commands.size()));

        for (Command command : commands.values()) {
            embed.appendField(String.format("**%s**", command.getName()), String.format("***Description***: *%s*\r\n***Usage***: *%s*", command.getDescription(), command.getHelp()), false);
        }

        event.getChannel().sendMessage(embed.build());
    }

    private static Map<String, Command> getHelpPage(int page) {
        int count = page * 8;
        int pageCount = count - 8;
        int ctr = 1;

        Map<String, Command> commands = new TreeMap<>();

        for (Command command : Reflections.getCommands()) {
            if (ctr >= pageCount && ctr <= count) {
                commands.put(command.getName(), command);
            }
            ctr++;
        }

        if (commands.size() > 0) {
            return commands;
        } else {
            return null;
        }
    }

    private static void getSpecificHelp(String command, MessageReceivedEvent event) {
        Command helpCommand = Reflections.getCommand(command);

        EmbedBuilder helpEmbed = new EmbedBuilder().withTitle(helpCommand.getName().substring(0, 1).toUpperCase() + helpCommand.getName().substring(1, helpCommand.getName().length()))
                .withDescription(helpCommand.getDescription())
                .withColor(Alfred.EMBED_COLOR)
                .appendField("Usage", helpCommand.getHelp(), false)
                .appendField("Role Required", Permissions.getPermRole(helpCommand.getPerm()), false);

        event.getChannel().sendMessage(helpEmbed.build());
    }

    private static void getSpecificHelp(int page, MessageReceivedEvent event) {
        Map<String, Command> commands = getHelpPage(page);

        if (commands != null) {
            EmbedBuilder embed = new EmbedBuilder().withTitle("Command Help")
                    .withDescription("All commands and how to use them.")
                    .withThumbnail(Alfred.getClient().getOurUser().getAvatarURL())
                    .withColor(Alfred.EMBED_COLOR)
                    .withFooterText(String.format("Showing %s command(s) from page %s", commands.size(), page));

            for (Command command : commands.values()) {
                embed.appendField(String.format("**%s**", command.getName()), String.format("***Description***: *%s*\r\n***Usage***: *%s*", command.getDescription(), command.getHelp()), false);
            }

            event.getChannel().sendMessage(embed.build());
        } else {
            event.getChannel().sendMessage("I'm sorry, but there aren't that many pages of commands.");
        }
    }

}
