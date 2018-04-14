package com.bbutner.Core.Listeners;

import com.bbutner.Reflections;
import com.bbutner.Utils.Database;
import com.bbutner.Utils.Permissions;
import com.bbutner.Core.Alfred;
import com.bbutner.Utils.Logging;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * Initializes systems for Alfred after he has logged in
 * @author bbutner
 * @version 1.0
 */
public class ReadyHandler {

    /**
     * Method called after Alfred has logged in
     * @param event The event
     */
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        Reflections reflections = new Reflections("me.bbutner.Commands.");
        Logging.infoBot("Logged in.");
        Database.connectDatabase();
        CommandHandler.registerCommands();
        Permissions.initPermissions();
        Alfred.getDispatcher().registerListener(new MessageHandler());
    }

}
