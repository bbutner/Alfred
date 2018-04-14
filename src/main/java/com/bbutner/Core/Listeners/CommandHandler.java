package com.bbutner.Core.Listeners;

import com.bbutner.Command;
import com.bbutner.Reflections;
import com.bbutner.Utils.Permissions;
import com.vdurmont.emoji.EmojiManager;
import com.bbutner.Core.Alfred;
import com.bbutner.Utils.Logging;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import static com.bbutner.Reflections.getCommand;
import static com.bbutner.Reflections.isCommand;

/**
 * Handles commands being passed to Alfred
 * @author bbutner
 * @version 1.0
 */
public class CommandHandler {
    /**
     * Processes the command
     * @param event Used for the message and the author
     */
    public static void processCommand(MessageReceivedEvent event) {
        String commandName = event.getMessage().getContent().split(" ")[0].replace(Alfred.CMD_PREFIX, "");
        String[] args = event.getMessage().getContent().replace(Alfred.CMD_PREFIX + commandName, "").split(" ");

        if (isCommand(commandName.toLowerCase()) && Permissions.hasPermissionForCommand(getCommand(commandName.toLowerCase()), event.getAuthor())) {
            Command userCommand = getCommand(commandName.toLowerCase());
            userCommand.runCommand(event, args);
            Logging.infoCommand(event.getAuthor(), userCommand);
        } else {
            event.getMessage().addReaction(EmojiManager.getForAlias(isCommand(commandName.toLowerCase()) ? "x" : "question"));
        }
    }

    static void registerCommands() {
        Reflections.addCommand("help", "List all commands, what they do, and how to use them.", "help", 1);
        Reflections.addCommand("info", "Shows general information and statistics for Alfred.", "info", 1);
        Reflections.addCommand("tag", "Tags system", "tag", 1);
        Reflections.addCommand("color", "Gives the Hex, RGB, RGB 1.0, and Integer information of a color.", "color [color]", 1);
        Reflections.addCommand("log", "Shows the latest 15 lines of Alfred's log.", "log", 4);
        Reflections.addCommand("userinfo", "Gets information about yourself or another user.", "userinfo [user]", 1);
        Reflections.addCommand("role", "Gets information about yourself or another user.", "role [user]", 1);
        Reflections.addCommand("clear", "Clears all the recent bot messages.", "clear", 1);

        Logging.infoBot("Registered commands.");
    }

}
