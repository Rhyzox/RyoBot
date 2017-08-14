package core.Private;

import commands.Intefaces.CommandPrivate;

import java.util.HashMap;

public class commandHandlerPrivate {

    public static final commandParserPrivate parser = new commandParserPrivate();
    public static HashMap<String, CommandPrivate> commands = new HashMap<>();

    public static void handleCommandPrivate(commandParserPrivate.commandContainer cmd) {

        if (commands.containsKey(cmd.invoke)) {

            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if (!safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            } else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }

        }

    }

}