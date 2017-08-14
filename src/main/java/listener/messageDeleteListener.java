package listener;

import audioCore.TrackManager;
import commands.Public.Music;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class messageDeleteListener extends ListenerAdapter {

    private static Guild guild;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {

            if (event.getTextChannel().getName().equals("eg_music")) {
                    if(!event.getAuthor().equals(event.getJDA().getSelfUser())){
                        event.getMessage().delete().queue();
                    }
                }
        }catch (Exception e){

        }
    }
}
