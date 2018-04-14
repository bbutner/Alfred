package com.bbutner.Commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for displaying Role information
 * @author bbutner
 * @version 1.0
 */
public class Role {

    /**
     * Handles the Role command
     * @param event Used for the channel, guild, and user
     * @param args User to get the role information for
     */
    public static void handleCommand(MessageReceivedEvent event, String[] args) {
        switch (args.length) {
            case 1: {
                getRoleInformation(event, event.getAuthor().getRolesForGuild(event.getGuild()).get(0));
                break;
            }

            default: {
                IRole role = null;

                if (event.getMessage().getMentions().size() > 0) {
                    role = event.getMessage().getMentions().get(0).getRolesForGuild(event.getGuild()).get(0);
                } else if (role == null) {
                    String roleName = "";

                    for (int ctr = 1; ctr < args.length; ctr++) {
                        roleName += String.format("%s ", args[ctr]);
                    }

                    roleName = roleName.substring(0, roleName.length() - 1);

                    if (event.getGuild().getRolesByName(roleName).size() > 0) {
                        role = event.getGuild().getRolesByName(roleName).get(0);
                    } else {
                        try {
                            role = event.getGuild().getRoleByID(Long.parseLong(args[1]));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                if (role != null) {
                    getRoleInformation(event, role);
                } else {
                    event.getChannel().sendMessage("I'm sorry but I could not find that role.");
                }
                break;
            }
        }
    }

    private static void getRoleInformation(MessageReceivedEvent event, IRole role) {
        ArrayList<String> accessiblePerms = new ArrayList<>();
        ArrayList<String> deniedPermissions = new ArrayList<>();

        for (Permissions perm : Permissions.values()) {
            if (role.getPermissions().contains(perm)) {
                accessiblePerms.add(perm.name());
            } else {
                deniedPermissions.add(perm.name());
            }
        }

       
        Collections.sort(accessiblePerms);
        Collections.sort(deniedPermissions);

       
        String accessiblePermsString = "";
        String deniedPermissionsString = "";

       
        for (String string : accessiblePerms) {
            accessiblePermsString += String.format("%s, ", string);
        }
        for (String string : deniedPermissions) {
            deniedPermissionsString += String.format("%s, ", string);
        }


        EmbedBuilder roleEmbed = new EmbedBuilder()
                .withColor(role.getColor())
                .withDescription("Role Information")
                .appendField("Name", role.getName(), true)
                .appendField("ID", String.valueOf(role.getLongID()), true)
                .appendField("Position", String.valueOf(role.getPosition()), true)
                .appendField("\u200B", "\u200B", false)
               
                .appendField("Permissions :white_check_mark:", accessiblePermsString.length() > 0 ? accessiblePermsString.substring(0, accessiblePermsString.length() - 2) : "None", false)
                .appendField("Permissions :x:", deniedPermissionsString.length() > 0 ? deniedPermissionsString.substring(0, deniedPermissionsString.length() - 2) : "None", false);

        event.getChannel().sendMessage(roleEmbed.build());
    }

}
