package audioCore;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import commands.Public.Music;
import core.RyoBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import utils.Var;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer player;
    public final Queue<AudioInfo> queue;

    public TrackManager(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    private TrackManager getTrackManager(Guild guild) {
        return Music.PLAYERS.get(guild.getId()).getValue();
    }

    public void queue(AudioTrack track, Member author) {

        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);

        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {

        player.setVolume(Var.volume);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild g = queue.poll().getAuthor().getGuild();

        Set<AudioInfo> queue1 = getTrackManager(g).getQueuedTracks();
        ArrayList<AudioInfo> tracks = new ArrayList<>();
        queue1.forEach(audioInfo -> tracks.add(audioInfo));


        if (queue.isEmpty()) {
            g.getTextChannelById("345341174720233472").sendMessage(Var.noerror.setDescription(
                    ":no_entry_sign:  Die Queue ist leer :no_entry_sign:").build()).queue();
        } else {
            g.getTextChannelById("345341174720233472").sendMessage(Var.noerror.setDescription(
                    ":musical_note: Nächster Song :musical_note: " +
                            "\n" +
                            "Titel: " + tracks.get(1).getTrack().getInfo().title + "\n" +
                            "Länge: " + getTimestamp(tracks.get(1).getTrack().getDuration()) + "\n" +
                            "Interpret: " + tracks.get(1).getTrack().getInfo().author).build()).queue();
            player.playTrack(queue.element().getTrack());
        }
    }

    public void shuffleQueue() {
        List<AudioInfo> tQueue = new ArrayList<>(getQueuedTracks());
        AudioInfo current = tQueue.get(0);
        tQueue.remove(0);
        Collections.shuffle(tQueue);
        tQueue.add(0, current);
        purgeQueue();
        queue.addAll(tQueue);
    }

    public Set<AudioInfo> getQueuedTracks() {
        return new LinkedHashSet<>(queue);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public AudioInfo getTrackInfo(AudioTrack track) {
        return queue.stream().filter(audioInfo -> audioInfo.getTrack().equals(track)).findFirst().orElse(null);
    }

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }
}