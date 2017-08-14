package core.Private;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import utils.Var;

import java.util.Arrays;

public class permsCorePrivate {

    public static boolean checkPerms(PrivateMessageReceivedEvent event){

        for ( Role r :  event.getAuthor().getMutualGuilds().get(0).getMember(event.getAuthor()).getRoles()) {
            if(Arrays.stream(Var.PERMS).parallel().anyMatch(r.getName()::contains))
                return false;
        }
        return true;
    }

}
