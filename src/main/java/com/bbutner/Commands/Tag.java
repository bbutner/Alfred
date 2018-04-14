package com.bbutner.Commands;

import com.vdurmont.emoji.EmojiManager;
import com.bbutner.Utils.TagSystem;
import com.bbutner.Utils.TimedDeletion;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Class for handling the Tag command
 * @author bbutner
 * @version 1.0
 */
public class Tag {

    /**
     * Handles the tag command
     * @param event Used for the message, channel, and message
     * @param args Supplied tag information
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        switch (args.length) {
            case 1: {
                event.getChannel().sendMessage(TagSystem.getTagHelp());
                break;
            }
            case 2: {
                switch (args[1].toLowerCase()) {
                    case "add": {
                        TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("Please supply me with an **ID** and some **content** so I can add a tag for you."));
                        break;
                    }
                    case "delete": {
                        TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I need an **ID** if you want me to delete a tag for you."));
                        break;
                    }
                    case "list": {
                        TagSystem.listTags(event);
                        break;
                    }
                    default: {
                       
                        IMessage tagMessage = event.getChannel().sendMessage("``Loading Tag...``");

                        if (TagSystem.tagExists(args[1])) {
                            TagSystem.getTag(event, args[1].toLowerCase(), tagMessage);
                        } else {
                            TimedDeletion.delayMessageDeletion(event.getMessage());
                            TimedDeletion.delayMessageDeletion(tagMessage.edit(String.format("I'm sorry, but I could not find the tag **%s** in my database.", args[1])));
                        }
                    }
                }
                break;
            }
            case 3: {
                if (args[1].toLowerCase().equals("delete")) {
                    if (TagSystem.tagExists(args[2])) {
                        if (TagSystem.isOwner(args[2], event.getAuthor().getLongID())) {
                            TagSystem.deleteTag(event, args[2]);

                            if (!TagSystem.tagExists(args[2])) {
                                TimedDeletion.delayMessageDeletion(event.getMessage());
                                TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I have successfully deleted your tag."));
                            } else {
                                TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I'm sorry but there was an error deleting your tag."));
                            }
                        } else {
                            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I cannot delete a tag that you do not own!"));
                        }
                    } else {
                        TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I'm sorry but that tag does not exist."));
                    }
                } else if (args[1].toLowerCase().equals("search")) {
                    if (event.getMessage().getMentions().size() == 0) {
                        TagSystem.searchTagByID(event, args[2]);
                    } else {
                        TagSystem.searchTagByUser(event, event.getMessage().getMentions().get(0).getLongID());
                    }
                } else {
                    event.getMessage().addReaction(EmojiManager.getForAlias("question"));
                }
            }
            default: {
                switch (args[1].toLowerCase()) {
                    case "add": {
                        if (TagSystem.tagExists(args[2])) {
                            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("There is already a tag with that ID!"));
                        } else {
                            event.getMessage().delete();
                            TagSystem.addNewTag(event, args);
                        }
                        break;
                    }
                    case "update": {
                        TimedDeletion.delayMessageDeletion(event.getMessage());

                        if (TagSystem.tagExists(args[2])) {
                            if (TagSystem.isOwner(args[2], event.getAuthor().getLongID())) {
                                TagSystem.updateTag(event, args[2], args);
                            } else {
                                TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I cannot modify a tag that you do not own!"));
                            }
                        } else {
                            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I'm sorry but that tag does not exist."));
                        }
                        break;
                    }
                }
            }
        }
    }

}
