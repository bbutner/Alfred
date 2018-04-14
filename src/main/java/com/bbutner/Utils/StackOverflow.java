package com.bbutner.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StackOverflow information displaying
 * @author bbutner
 * @version 1.0
 */
public class StackOverflow {

    /**
     * Displays the StackOverflow information for a link
     * @param event Used for the channel
     * @param message Message to get the URL from
     */
    public static void showStackInformation(MessageReceivedEvent event, String message) {
        try {
            Document html = Jsoup.connect(getURL(message)).get();

            EmbedBuilder overflowEmbed = new EmbedBuilder().withTitle("Stack Overflow")
                    .withDescription(String.format("[%s](%s)",
                            html.getElementById("question-header").child(0).child(0).html(),
                            html.location()))
                    .withColor(new Color(244, 128, 36))
                    .withThumbnail("https://cdn.sstatic.net/Sites/stackoverflow/company/img/logos/so/so-icon.png")
                    .appendField("\u200B", "\u200B", false)
                    .appendField("Votes", html.getElementsByClass("vote-count-post").get(0).html(), true)
                    .appendField("Favorites", html.getElementsByClass("favoritecount").get(0).child(0).html().isEmpty() ? "0" : html.getElementsByClass("favoritecount").get(0).child(0).html(), true)
                    .appendField("Answers", html.getElementsByClass("subheader answers-subheader").get(0).child(0).children().size() == 0 ? "None" : html.getElementsByClass("subheader answers-subheader").get(0).child(0).child(0).html(), true)
                    .appendField("Asked", html.getElementById("qinfo").child(0).child(0).child(1).child(0).child(0).html(), true)
                    .appendField("Views", html.getElementById("qinfo").child(0).child(1).child(1).child(0).child(0).html().replace(" times", ""), true)
                    .appendField("Tags", getPostTags(html), true)
                    .appendField("\u200B", "\u200B", false)
                    .appendField("Author", html.getElementsByClass("user-details").get(0).child(0).html(), true)
                    .appendField("Ask Date", html.getElementsByClass("relativetime").get(0).html(), true);

            event.getChannel().sendMessage(overflowEmbed.build());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getURL(String message) {
        return Arrays.stream(message.split(" ")).filter(s -> s.contains("https://stackoverflow.com/questions/")).findFirst().orElse(null);
    }

    private static String getPostTags(Document html) {
        String tags = "";

        for (Element tagElement : html.getElementsByClass("post-taglist").get(0).children()) {
            if (tagElement.children().size() > 0) {
                Pattern pattern = Pattern.compile("\\<(.*?)\\>");
                Matcher matcher = pattern.matcher(tagElement.html());
                matcher.find();

                tags += String.format("%s, ", tagElement.html().replace(matcher.group(0), ""));
            } else {
                tags += String.format("%s, ", tagElement.html());
            }
        }

        return tags.substring(0, tags.length() - 2);
    }

}
