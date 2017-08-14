package core;

import audioCore.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import commands.Private.cmdHelpPrivate;
import commands.Public.Music;
import commands.Public.Musik2;
import commands.Public.cmdHelp;
import core.Private.commandHandlerPrivate;
import core.Public.commandHandler;
import listener.JoinListener;
import listener.messageDeleteListener;
import listener.messageListener;
import listener.messageListenerPrivate;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import utils.Var;

import javax.security.auth.login.LoginException;

public class RyoBot {

    public static JDABuilder builder;


    public static void main(String[] args) {
        builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Var.Token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setAutoReconnect(true);
        builder.setGame(new Game() {
            @Override
            public String getName() {
                return "| ?help | v1.1 by Rhyzox";
            }

            @Override
            public String getUrl() {
                return null;
            }

            @Override
            public GameType getType() {
                return GameType.TWITCH;
            }
        });

        initListener();
        initCommandPublic();
        initCommandPrivate();

        try {
            JDA jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }

    }

    public static void initListener() {
        builder.addListener(new JoinListener());
        builder.addListener(new messageListener());
        builder.addListener(new messageListenerPrivate());
        builder.addListener(new messageDeleteListener());
    }

    public static void initCommandPublic() {
        commandHandler.commands.put("help", new cmdHelp());
        commandHandler.commands.put("music", new Music());
        commandHandler.commands.put("m", new Music());
    }

    public static void initCommandPrivate() {
        commandHandlerPrivate.commands.put("help", new cmdHelpPrivate());
    }
}