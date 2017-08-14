package core.Public;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Var;

import java.util.Arrays;

public class permsCore {

    public static boolean checkPerms(MessageReceivedEvent event){
        try {
            for (Role r : event.getGuild().getMember(event.getAuthor()).getRoles()) {

                if (Arrays.stream(Var.PERMS).parallel().anyMatch(r.getName()::contains))
                    return false;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return true;
    }

}
