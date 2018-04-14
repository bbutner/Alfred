package com.bbutner.Core;

import com.bbutner.Core.Listeners.ReadyHandler;
import com.bbutner.Utils.Logging;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;

import java.awt.*;

/**
 * Main class for Alfred
 * @author bbutner
 * @version 1.0
 */
public class Alfred {
    public static final String CMD_PREFIX = ".";
    public static final Color EMBED_COLOR = new Color(255, 162, 56);
    private static IDiscordClient client;
    private static EventDispatcher dispatcher;
    public static final long LAUNCH_TIME = System.currentTimeMillis();
    private static String hostname = "";
    private static String password = "";

    /**
     * Main method for launching Alfred and connecting him to Discord
     * @param args Runtime Arguments
     */
    public static void main(String[] args) {
        Logging.infoBot("Starting up...");
        client = createClient(args[0]);
        assert client != null;
        dispatcher = client.getDispatcher();
        dispatcher.registerListener(new ReadyHandler());
        hostname = args[1];
        password = args[2];
    }

    private static IDiscordClient createClient(String token) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            return clientBuilder.login();
        } catch (DiscordException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the client.
     * @return the client
     */
    public static IDiscordClient getClient() {
        return client;
    }

    /**
     * Returns the master guild
     * @return the master guild
     */
    public static IGuild getMasterGuild() {
        return getClient().getGuildByID(Long.parseLong("322180328578088960"));
    }

    /**
     * Returns the Database password
     * @return the database password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Returns the database hostname
     * @return the database hostname
     */
    public static String getHostname() {
        return hostname;
    }

    /**
     * Returns the client dispatcher
     * @return the client dispatcher
     */
    public static EventDispatcher getDispatcher() {
        return dispatcher;
    }


}
