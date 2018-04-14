package com.bbutner.Commands;

import com.bbutner.Core.Listeners.MessageHandler;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.ArrayList;

public class Clear {

    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        if (args.length == 1) {
            ArrayList<IMessage> tempMessages = MessageHandler.getBotMessages();

            for (IMessage message : MessageHandler.getBotMessages()) {
                //TODO Compare if the message date is older than 2 weeks, if so remove it.
            }


        } else {

        }
    }

}
