package commands.Intefaces;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public interface CommandPrivate {

    boolean called(String[] args, PrivateMessageReceivedEvent event);
    void action(String[] args, PrivateMessageReceivedEvent event);
    void executed(boolean success, PrivateMessageReceivedEvent event);
    String help();

}