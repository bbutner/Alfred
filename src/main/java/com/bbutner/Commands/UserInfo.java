package com.bbutner.Commands;

import com.bbutner.Core.Alfred;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceState;
import sx.blah.discord.util.EmbedBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class for getting and displaying User information
 * @author bbutner
 * @version 1.0
 */
public class UserInfo {

    /**
     * Handles the UserInfo command
     * @param event Used for the guild, author, message, and channel
     * @param args User to get the informetion of
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        switch (args.length) {
            case 1: {
                event.getChannel().sendMessage(getUserInformation(event.getAuthor(), event.getGuild()));
                break;
            }
            default: {
                IUser user = null;

                if (event.getMessage().getMentions().size() > 0) {
                    user = event.getMessage().getMentions().get(0);
                } else if (user == null) {
                    String userName = "";

                    for (int ctr = 1; ctr < args.length; ctr++) {
                        userName += String.format("%s ", args[ctr]);
                    }

                    userName = userName.substring(0, userName.length() - 1);

                    if (event.getGuild().getUsersByName(userName).size() > 0) {
                        user = event.getGuild().getUsersByName(userName).get(0);
                    }
                } else {
                    try {
                        user = event.getGuild().getUserByID(Long.parseLong(args[1]));
                    } catch (NumberFormatException ex) {
                    }
                }

                if (user != null) {
                    event.getChannel().sendMessage(getUserInformation(user, event.getGuild()));
                } else {
                    event.getChannel().sendMessage("I'm sorry but I could not find that user.");
                }
                break;
            }
        }
    }

    private static EmbedObject getUserInformation(IUser user, IGuild guild) {
        EmbedBuilder embed = new EmbedBuilder().withAuthorName(String.format("%s#%s", user.getName(), user.getDiscriminator()))
                .withAuthorIcon(user.getAvatarURL())
                .withColor(Alfred.EMBED_COLOR)
                .withDescription("Relevant user information")
                .appendField("\u200B", "\u200B", false)
                .appendField("ID", String.valueOf(user.getLongID()), true)
                .appendField("Nickname", user.getNicknameForGuild(guild) != null ? user.getNicknameForGuild(guild) : "None", true)
                .appendField("Avatar URL", user.getAvatarURL(), false)
                .appendField("Status", user.getPresence().getStatus().toString(), true)
                .appendField("Playing", user.getPresence().getActivity().isPresent() ? user.getPresence().getActivity().get().name() : "Nothing", true)
                .appendField("Streaming", user.getPresence().getStreamingUrl().isPresent() ? user.getPresence().getStreamingUrl().get() : "Nothing", true)
                .appendField("Account Creation Date", getTimestamp(user.getCreationDate()), true)
                .appendField("Guild Join Date", getTimestamp(guild.getJoinTimeForUser(user)), true)
                .appendField("Current Role", user.getRolesForGuild(guild).get(0).getName(), true);

        if (user.getVoiceStateForGuild(guild).getChannel() != null) {
            IVoiceState state = user.getVoiceStateForGuild(guild);

            String voiceInfo = String.format("**%s**:\r\n", state.getChannel().getName());

            for (IUser conUser : state.getChannel().getConnectedUsers()) {
                if (conUser == user) {
                    voiceInfo += String.format(" | ***%s***\r\n", conUser.getDisplayName(guild));
                } else {
                    voiceInfo += String.format(" | %s\r\n", conUser.getDisplayName(guild));
                }
            }

            embed.appendField("\u200B", "\u200B", false);
            embed.appendField("Current Voice Channel", voiceInfo, false);
        }

        return embed.build();
    }

    private static String getTimestamp(Instant local) {
        LocalDateTime inst = LocalDateTime.ofInstant(local, ZoneId.systemDefault());
        DateTime time = new DateTime(inst.getYear(), inst.getMonthValue(), inst.getDayOfMonth(), inst.getHour(), inst.getMinute(), inst.getSecond());
        String dateFormat = DateTimeFormat.forPattern("MM-dd-yy").print(time);
        String timeFormat = DateTimeFormat.forPattern("HH:mm:ss").print(time);

        return String.format("%s @ %s", dateFormat, timeFormat);
    }

}
