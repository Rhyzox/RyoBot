package listener;

import core.Private.commandHandlerPrivate;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utils.Var;

public class messageListenerPrivate extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        if (event.getMessage().getContent().startsWith(Var.Prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            commandHandlerPrivate.handleCommandPrivate(commandHandlerPrivate.parser.parse(event.getMessage().getContent(), event));
        }
    }
}
