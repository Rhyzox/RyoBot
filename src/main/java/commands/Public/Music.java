package commands.Public;

import audioCore.AudioInfo;
import audioCore.PlayerSendHandler;
import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.Intefaces.Command;
import core.Public.permsCore;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Var;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zekro on 18.06.2017 / 11:47
 * supremeBot.commands
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Music implements Command {


    private static final int PLAYLIST_LIMIT = 1000;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    public static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    private static String clueURL = "http://stream01.iloveradio.de/iloveradio1.mp3";
    private static boolean radio = false;
    private TrackManager trackManager;

    /**
     * Audio Manager als Audio-Stream-Recource deklarieren.
     */
    public Music() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    /**
     * Erstellt einen Audioplayer und fügt diesen in die PLAYERS-Map ein.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }


    /**
     * Returnt, ob die Guild einen Eintrag in der PLAYERS-Map hat.
     *
     * @param g Guild
     * @return Boolean
     */
    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    /**
     * Returnt den momentanen Player der Guild aus der PLAYERS-Map,
     * oder erstellt einen neuen Player für die Guild.
     *
     * @param g Guild
     * @return AudioPlayer
     */
    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }

    /**
     * Returnt den momentanen TrackManager der Guild aus der PLAYERS-Map.
     *
     * @param g Guild
     * @return TrackManager
     */
    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }

    /**
     * Returnt, ob die Guild einen Player hat oder ob der momentane Player
     * gerade einen Track spielt.
     *
     * @param g Guild
     * @return Boolean
     */
    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }

    /**
     * Läd aus der URL oder dem Search String einen Track oder eine Playlist
     * in die Queue.
     *
     * @param identifier URL oder Search String
     * @param author     Member, der den Track / die Playlist eingereiht hat
     * @param msg        Message des Contents
     */
    private void loadTrack(String identifier, Member author, Message msg) {

        Guild guild = author.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

    }

    /**
     * Stoppt den momentanen Track, worauf der nächste Track gespielt wird.
     *
     * @param g Guild
     */
    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    /**
     * Erzeugt aus dem Timestamp in Millisekunden ein hh:mm:ss - Zeitformat.
     *
     * @param milis Timestamp
     * @return Zeitformat
     */
    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    /**
     * Returnt aus der AudioInfo eines Tracks die Informationen als String.
     *
     * @param info AudioInfo
     * @return Informationen als String
     */
    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }



    /**
     * Sendet eine Embed-Message in der Farbe Rot mit eingegebenen Content.
     *
     * @param event   MessageReceivedEvent
     * @param content Error Message Content
     */
    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getAuthor().openPrivateChannel().complete().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription(content)
                        .build()
        ).queue();
    }

    private void setVolume(int volume){
        getPlayer(guild).setVolume(volume);
    }

    private TrackManager getTrackManager(Guild guild) {
        return PLAYERS.get(guild.getId()).getValue();
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

            guild = event.getGuild();
            trackManager = new TrackManager(getPlayer(guild));
            if (args.length < 1) {
                sendErrorMsg(event, help());
                return;
            }

            switch (args[0].toLowerCase()) {

                case "play":
                case "p":
                    event.getMessage().delete().queue();
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if (event.getTextChannel().getName().equals("eg_music")) {
                            if (!event.getJDA().getGuilds().get(0).getAudioManager().isConnected()) {
                                VoiceChannel channel = event.getJDA().getGuilds().get(0).getVoiceChannelById("346059233147617280");
                                event.getJDA().getGuilds().get(0).getAudioManager().openAudioConnection(channel);
                            }
                            if (args.length < 2) {
                                sendErrorMsg(event, "Please enter a valid source!");
                                return;
                            }

                            if(radio == true){
                                radio = false;
                                getPlayer(guild).stopTrack();
                                getManager(guild).purgeQueue();
                                String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                                if (!(input.startsWith("http://") || input.startsWith("https://")))
                                    input = "ytsearch: " + input;

                                loadTrack(input, event.getMember(), event.getMessage());


                            }else{
                                String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                                if (!(input.startsWith("http://") || input.startsWith("https://")))
                                    input = "ytsearch: " + input;

                                loadTrack(input, event.getMember(), event.getMessage());


                                if(getManager(guild).queue.size() == 1){

                                    new Timer().schedule(
                                            new TimerTask() {
                                                @Override
                                                public void run() {
                                                    AudioTrack track = getPlayer(guild).getPlayingTrack();
                                                    AudioTrackInfo info = track.getInfo();
                                                    System.out.println("1");
                                                    event.getTextChannel().sendMessage(Var.noerror.setDescription(
                                                            ":musical_note: Nächster Song :musical_note: " +
                                                                    "\n" +
                                                                    "Titel: " + info.title + "\n" +
                                                                    "Länge: " + getTimestamp(track.getDuration()) + "\n" +
                                                                    "Interpret: " + info.author).build()).queue();
                                                    System.out.println("2");
                                                }
                                            }, 5000);
                                }
                            }
                        } else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                    break;


                case "skip":
                case "s":
                    event.getMessage().delete().queue();
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if (event.getTextChannel().getName().equals("eg_music")) {
                        if(permsCore.checkPerms(event)) {
                            event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.error.setDescription(":no_entry_sign: Du besitzt keine Berechtigung um diesen Befehl auszuführen!").build()).queue();
                            return;
                        }else {

                                if (isIdle(guild)) return;
                                for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                                    skip(guild);
                                }
                                event.getAuthor().openPrivateChannel().complete().sendMessage(Var.noerror.setDescription(":white_check_mark: Der Song wurde erfolgreich geskipt").build()).queue();
                            }

                        }else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                    break;


                case "stop":
                    event.getMessage().delete().queue();
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if(event.getTextChannel().getName().equals("eg_music")) {
                            if (permsCore.checkPerms(event)) {
                                event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.error.setDescription(":no_entry_sign: Du besitzt keine Berechtigung um diesen Befehl auszuführen!").build()).queue();
                                return;
                            } else {
                                if (isIdle(guild)) return;

                                getManager(guild).purgeQueue();
                                skip(guild);
                                event.getAuthor().openPrivateChannel().complete().sendMessage(Var.noerror.setDescription(":white_check_mark: Der Song wurde erfolgreich gestopt").build()).queue();
                            }
                        }else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                    break;


                case "shuffle":
                    event.getMessage().delete().queue();
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if(event.getTextChannel().getName().equals("eg_music")){
                        if(permsCore.checkPerms(event)) {
                            event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.error.setDescription(":no_entry_sign: Du besitzt keine Berechtigung um diesen Befehl auszuführen!").build()).queue();
                            return;
                        }else {
                                if (isIdle(guild)) return;
                                getManager(guild).shuffleQueue();
                                event.getAuthor().openPrivateChannel().complete().sendMessage(Var.noerror.setDescription(":white_check_mark: Die Songliste wurde erfolgreich geshuffelt").build()).queue();
                            }
                        }else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                    break;

                case "now":
                case "info":
                    event.getMessage().delete().queue();
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if (event.getTextChannel().getName().equals("eg_music")) {

                            if (isIdle(guild)) return;

                            AudioTrack track = getPlayer(guild).getPlayingTrack();
                            AudioTrackInfo info = track.getInfo();
                            event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                    .setDescription("**Aktueller Song:**")
                                    .addField("Titel: ", info.title, false)
                                    .addField("Länge: ", "`[ " + getTimestamp(track.getPosition()) + "/" + getTimestamp(track.getDuration()) + " ]`", false)
                                    .addField("Interpret: ", info.author, false)
                                    .build()
                            ).queue();
                        } else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                    break;

                case "queue":
                    event.getMessage().delete().queue();
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if (event.getTextChannel().getName().equals("eg_music")) {
                            if (isIdle(guild)) return;

                            int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;
                            List<String> tracks = new ArrayList<>();
                            List<String> trackSublist;

                            getManager(guild).getQueuedTracks().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                            if (tracks.size() > 20)
                                trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
                            else
                                trackSublist = tracks;

                            String out = trackSublist.stream().collect(Collectors.joining("\n"));
                            int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                            event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                    .setDescription(
                                            "**Aktuelle Songliste:**\n" +
                                                    "*[" + getManager(guild).getQueuedTracks().size() + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*\n" +
                                                    out
                                    )
                                    .build()
                            ).queue();
                        } else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                        break;

                case "ilr":
                    if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
                        if(event.getTextChannel().getName().equals("eg_music")) {
                            if (permsCore.checkPerms(event)) {
                                event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.error.setDescription(":no_entry_sign: Du besitzt keine Berechtigung um diesen Befehl auszuführen!").build()).queue();
                                return;
                            } else {
                                event.getMessage().delete().queue();
                                if(radio == false) {
                                    getManager(guild).purgeQueue();
                                    getPlayer(guild).stopTrack();
                                    loadTrack(clueURL, event.getMember(), event.getMessage());
                                    radio = true;
                                    if (getPlayer(guild).isPaused())
                                        getPlayer(guild).setPaused(false);
                                }

                            }
                        }else {
                            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                        }
                    }
                    break;
                }
            }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return  ":information_source: ------Help------ :information_source:" +
                "\n" +
                "➥ ?m | ?music » Listet dir die Music Befehle auf." +
                "\n" +
                "➥ ?m p | ?music play <url> » Startet einen beliebigen Youtube Song." +
                "\n" +
                "➥ ?m s | ?music skip » Skippe einen Song." +
                "\n" +
                "➥ ?music queue » Zeigt dir die Songliste an." +
                "\n" +
                "➥ ?music now | ?music info » Zeigt dir Infos zu dem aktuellen Song." +
                "\n" +
                "➥ ?music skip » Überspringt den aktuellen Song." +
                "\n" +
                "➥ ?music ilr » Spielt I Love Radio" +
                "\n" +
                "➥ ?music stop » Stop die Queue und löscht sie.";
    }
}