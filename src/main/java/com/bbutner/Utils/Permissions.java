package com.bbutner.Utils;

import com.bbutner.Command;
import com.bbutner.Core.Alfred;
import sx.blah.discord.handle.obj.IUser;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Class used for the Permissions system
 * @author bbutner
 * @version 1.0
 */
public class Permissions {
    private static HashMap<String, Integer> permissions = new HashMap<>();
    private static HashMap<Integer, String> permNames = new HashMap<>();

    /**
     * Initializes the permissions system
     */
    public static void initPermissions() {
        Alfred.getClient().getUsers().forEach(user -> {
            try {
                if (Database.submitReturnQuery("select count(*) from user_permissions where user_id='" + user.getLongID() + "'").getInt(1) < 1) {
                    Database.submitQuery("insert into user_permissions VALUES('" + user.getLongID() + "',0)");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        permissions.put("Admin", 4);
        permissions.put("Moderator", 3);
        permissions.put("Proficient", 2);
        permissions.put("Registered", 1);
        permissions.put("@everyone", 0);
        permNames.put(4, "Admin");
        permNames.put(3, "Moderator");
        permNames.put(2, "Proficient");
        permNames.put(1, "Registered");
        permNames.put(0, "@everyone");
        Logging.infoBot("Permissions initialized.");
    }

    /**
     * Check if the user has the required permission level to run the command
     * @param command The command the user is trying to run
     * @param user The user trying to tun the command
     * @return If the user has access to the command
     */
    public static boolean hasPermissionForCommand(Command command, IUser user) {
        try {
            return Integer.parseInt(Database.submitReturnQuery("select level from user_permissions where user_id='" + user.getLongID() + "'").getString(1)) >= command.getPerm();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Get the role name for the permission level
     * @param perm The permission level
     * @return The name corresponding to the permission level
     */
    public static String getPermRole(int perm) {
        return permNames.get(perm);
    }

}
