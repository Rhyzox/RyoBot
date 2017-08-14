package commands.Private;

import commands.Intefaces.CommandPrivate;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import core.Private.permsCorePrivate;
import utils.Var;

public class cmdHelpPrivate implements CommandPrivate {

    @Override
    public boolean called(String[] args, PrivateMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, PrivateMessageReceivedEvent event) {
        if(permsCorePrivate.checkPerms(event)) {
            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.error.setDescription(":no_entry_sign: Du besitzt keine Berechtigung um diesen Befehl auszuführen!").build()).queue();
            return;
        }else{
            event.getAuthor().openPrivateChannel().complete().sendMessage(Var.error.setDescription(
                    ":information_source: ------Help------ :information_source:" +
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
                            "➥ ?music shuffle » Lässt die Songliste random durchspielen." +
                            "\n" +
                            "➥ ?music volume » Stelle die Lautstärke um." +
                            "\n" +
                            "➥ ?music stop » Stop den aktuellen Song."
            ).build()).queue();
        }
    }

    @Override
    public void executed(boolean success, PrivateMessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return "Help";
    }
}
