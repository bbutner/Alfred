package com.bbutner.Utils;

import com.bbutner.Core.Alfred;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for interacting with the Tag system
 * @author bbutner
 * @version 1.0
 */
public class TagSystem {

    private static ResultSet result;

    /**
     * Return the embed for displaying tag help
     * @return the embed
     */
    public static EmbedObject getTagHelp() {
        EmbedBuilder tagHelpEmbed = new EmbedBuilder().withTitle("Tag System")
                .withDescription("Description and help for the Tags system.")
                .withColor(Alfred.EMBED_COLOR)
                .appendField("What is a tag?", String.format("what this system is for. Say you found a cool link to some helpful programming advice and you want people to always be able to get to it easily, and " + "Look at a tag as a bookmark in a web browser. You want to save content to retrieve at a later date with ease, that's " + "can be whatever you want." + "also be able to share it easily. Simply add it to the tags system with %stag add [id] [content], then retrieve it later with %stag [id]. The content of a tag ", Alfred.CMD_PREFIX, Alfred.CMD_PREFIX), false)
                .appendField("How do I add a tag?", String.format("%stag add cute http://i.imgur.com/Z7cckxw.png" + "Its simple! All you have to do it come up with an ID for the tag, then supply the content.\r\n\r\nExample:\r\n", Alfred.CMD_PREFIX), false)
                .appendField("How do I delete my tag?", String.format("%stag delete cute" + "Well first off, only the creator of the tag can actually delete it.\r\n\r\nExample\r\n", Alfred.CMD_PREFIX), false)
                .appendField("\u200B", "\u200B", false)
                .appendField("Command Arguments:", "tag [id] - Retrieves the content of the tag.\r\n" + "tag delete [id] - Removes a tag from the system.\r\n" + "tag add [id] [content] - Adds a tag to the system.\r\n" + "tag - Shows this dialog." + "tag update [id] [content] - Change the content of a tag without deleting it.\r\n", false);

        return tagHelpEmbed.build();
    }

    /**
     * Enters a new tag into the database if it does not already exist
     * @param event The event that holds the message and author information
     * @param args The tag arguments
     */
    public static void addNewTag(MessageReceivedEvent event, String[] args) {
        String content = "";
        String id = args[2].toLowerCase();

        for (int ctr = 3; ctr < args.length; ctr++) {
            content += args[ctr] + " ";
        }

        Database.submitQuery(String.format("INSERT INTO tags (user_id, user_name, tag_id, tag_content) VALUES(\'%s\',\'%s\',\'%s\',\'%s\')",
                event.getAuthor().getLongID(), event.getAuthor().getName(), id, content));

        if (tagExists(id)) {
            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage(String.format("I have successfully added your tag **%s**.", id)));
        } else {
            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I'm sorry but I cannot add your tag right now!"));
        }
    }

    /**
     * Check if a tag currently exists in the database
     * @param id The ID of the tag
     * @return if the tag exists or not
     */
    public static boolean tagExists(String id) {
        result = Database.submitReturnQuery(String.format("SELECT tag_id FROM tags WHERE tag_id=\'%s\'", id));

        try {
            return result.getString(1).equals(id);
        } catch (SQLException e) {
            return false;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Displays the tag information
     * @param event The event
     * @param id Tag ID
     * @param tagMessage The Embed to be edited with the tag information
     */
    public static void getTag(MessageReceivedEvent event, String id, IMessage tagMessage) {
        result = Database.submitReturnQuery(String.format("SELECT * FROM tags WHERE tag_id=\'%s\'", id));

        try {
            IUser user = event.getGuild().getUserByID(Long.parseLong(result.getString(1)));
            String tagContent = result.getString(4);

            if (tagContent.contains(".png") ||
                    tagContent.contains(".jpg") ||
                    tagContent.contains(".jpeg") ||
                    tagContent.contains(".gif")) {
                tagMessage.edit(tagContent);
            } else {
                EmbedBuilder tagEmbed = new EmbedBuilder().withTitle(result.getString(3))
                        .withDescription("\u200B")
                        .withColor(Alfred.EMBED_COLOR)
                        .appendField("Content:", tagContent, false)
                        .withFooterIcon(user.getAvatarURL())
                        .withFooterText(String.format("Tag created by: %s", user.getDisplayName(event.getGuild())));

                tagMessage.edit(tagEmbed.build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the sender is the owner of the tag
     * @param tagID The ID of the tag
     * @param userID The ID of the message sender
     * @return if the sender is the creator of the tag
     */
    public static Boolean isOwner(String tagID, Long userID) {
        try {
            return Long.parseLong(Database.submitReturnQuery(String.format("SELECT user_id FROM tags WHERE tag_id=\'%s\'", tagID)).getString(1)) == userID;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Displays all the tags in the database
     * @param event The event to get the channel from
     */
    public static void listTags(MessageReceivedEvent event) {
        result = Database.submitReturnQuery("SELECT * FROM tags");

        try {
            String tagsList = String.format("Here is the current list of all **%s** tags: \r\n\r\n", Database.submitReturnQuery("SELECT COUNT(*) FROM tags").getString(1));

            result.previous();

            while (result.next()) {
                tagsList += String.format("%s**,** ", result.getString(3));
            }

            tagsList = tagsList.substring(0, tagsList.length() - 6);

            event.getChannel().sendMessage(tagsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete a tag from the database
     * @param event Used to get the author and the channel
     * @param id The ID of the tag
     */
    public static void deleteTag(MessageReceivedEvent event, String id) {
        Database.submitQuery(String.format("DELETE FROM tags WHERE tag_id=\'%s\'", id));
    }

    /**
     * Update the content of an existing tag
     * @param event The event to get the author and channel from
     * @param tag_id The ID of the tag
     * @param args Contains the tag content
     */
    public static void updateTag(MessageReceivedEvent event, String tag_id, String[] args) {
        String tagContent = "";

        for (int ctr = 3; ctr < args.length; ctr++) {
            tagContent += args[ctr] + " ";
        }

        Database.submitQuery(String.format("UPDATE tags SET tag_content=\'%s\' WHERE tag_id=\'%s\'", tagContent, tag_id));

        try {
            if (Database.submitReturnQuery(String.format("SELECT tag_content FROM tags WHERE tag_id=\'%s\'", tag_id)).getString(1).equals(tagContent)) {
                TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I have successfully updated your tag."));
            } else {
                TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I'm sorry, but there was an error updating your tag."));
            }
        } catch (SQLException e) {
            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage("I'm sorry, but there was an error updating your tag."));
        }
    }

    /**
     * Searches the database for a tag by its ID
     * @param event Used to get the channel
     * @param id The ID of the tag
     */
    public static void searchTagByID(MessageReceivedEvent event, String id) {
        result = Database.submitReturnQuery("SELECT * FROM tags WHERE tag_id LIKE \'%" + id + "%\'");

        try {
            String results = "I found these tags in my search:\r\n\r\n";

            result.previous();

            while (result.next()) {
                results += String.format("**%s** - %s\r\n", result.getString(3), event.getGuild().getUserByID(Long.parseLong(result.getString(1))).getDisplayName(event.getGuild()));
            }

            event.getChannel().sendMessage(results);
        } catch (SQLException e) {
            TimedDeletion.delayMessageDeletion(event.getChannel().sendMessage(String.format("I could not find any tags like **%s**.", id)));
        }
    }

    /**
     * To be implemented, will display all tags owned by a user
     * @param event TO BE DEFINED
     * @param id The ID of the user to be searched
     */
    public static void searchTagByUser(MessageReceivedEvent event, Long id) {

    }

}
