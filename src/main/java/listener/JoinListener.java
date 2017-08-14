package listener;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

    public void onReady(ReadyEvent event) {
        event.getJDA().getSelfUser().getManager().setName("\uD83D\uDC51 RyoBot").queue();
        if (!event.getJDA().getGuilds().get(0).getAudioManager().isConnected()) {
            VoiceChannel channel = event.getJDA().getGuilds().get(0).getVoiceChannelById("346059233147617280");
            event.getJDA().getGuilds().get(0).getAudioManager().openAudioConnection(channel);
        }
    }
}
