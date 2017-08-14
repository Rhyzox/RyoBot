package commands.Public;

import commands.Intefaces.Command;
import core.Public.permsCore;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Var;

public class cmdHelp implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!event.getChannelType().equals(ChannelType.PRIVATE)) {
            if (permsCore.checkPerms(event)) {
                event.getMessage().delete().queue();
                event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.error.setDescription(":no_entry_sign: Du besitzt keine Berechtigung um diesen Befehl auszuführen!").build()).queue();
                return;
            } else {
                if (event.getTextChannel().getName().equals("eg_music")) {
                    event.getMessage().delete().queue();
                    event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.error.setDescription(
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
                } else {
                    event.getMessage().delete().queue();
                    event.getMember().getUser().openPrivateChannel().complete().sendMessage(Var.warning.setDescription(":warning: Der Befehl kann nur im TextChannel ``eg_music`` genutzt werden!").build()).queue();
                }
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
