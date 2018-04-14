package com.bbutner.Core.Listeners;

import com.bbutner.Core.Alfred;
import com.bbutner.Utils.Logging;
import com.bbutner.Utils.StackOverflow;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageSendEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;

/**
 * Class that handles messages
 * @author bbutner
 * @version 1.0
 */
public class MessageHandler {
    private static int messageCount = 0;
    private static int commandCount = 0;
    private static ArrayList<IMessage> botMessages = new ArrayList<>();

    /**
     * Ran when a message is received, check the message for a command
     * @param event Used for the message and author
     */
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        Logging.infoMessage(event.getMessage());
        messageCount++;

       
        if (!event.getAuthor().isBot() && event.getMessage().getAttachments().size() == 0 && event.getMessage().getContent().substring(0, 1).equals(Alfred.CMD_PREFIX)) {
            commandCount++;
            botMessages.add(event.getMessage());
            CommandHandler.processCommand(event);
        } else if (event.getMessage().getContent().contains("https://stackoverflow.com/questions/")) {
            StackOverflow.showStackInformation(event, event.getMessage().getContent());
        }
    }

    /**
     * Ran when Alfred sends a message
     * @param event Used for the message
     */
    @EventSubscriber
    public void onMessageSent(MessageSendEvent event) {
        botMessages.add(event.getMessage());
    }

    /**
     * Gets the message count
     * @return the count of messages
     */
    public static int getMessageCount() {
        return messageCount;
    }

    /**
     * Gets the command count
     * @return the count of commands
     */
    public static int getCommandCount() {
        return commandCount;
    }

    /**
     * Gets the messages sent by the bot
     * @return the bot messages
     */
    public static ArrayList<IMessage> getBotMessages() {
        return botMessages;
    }

}
