package com.bbutner.Utils;

import sx.blah.discord.handle.obj.IMessage;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles the timed deletion of messages
 * @author bbutner
 * @version 1.0
 */
public class TimedDeletion {

    /**
     * Deletes a message after 5 seconds
     * @param message The message to be deleted
     */
    public static void delayMessageDeletion(IMessage message) {
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
       
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                message.delete();
                timer.shutdown();
            }
        }, 5, 1, TimeUnit.SECONDS);
    }
}
