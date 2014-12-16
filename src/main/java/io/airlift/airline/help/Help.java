package io.airlift.airline.help;

import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.model.CommandGroupMetadata;
import io.airlift.airline.model.CommandMetadata;
import io.airlift.airline.model.GlobalMetadata;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.collect.Lists.newArrayList;

@Command(name = "help", description = "Display help information")
public class Help implements Runnable, Callable<Void> {
    public static boolean USAGE_AS_HTML = false;
    public static boolean USAGE_AS_RONN = false;

    @Inject
    @Nullable
    public GlobalMetadata global;

    @Arguments
    public List<String> command = newArrayList();

    @Override
    public void run()
    {
        help(global, command);
    }

    @Override
    public Void call()
    {
        run();
        return null;
    }

    public static void help(CommandMetadata command)
    {
        StringBuilder stringBuilder = new StringBuilder();
        help(command, stringBuilder);
        System.out.println(stringBuilder.toString());
    }

    public static void help(CommandMetadata command, StringBuilder out)
    {
        new CommandUsage().usage(null, null, command.getName(), command, out);
    }

    public static void help(GlobalMetadata global, List<String> commandNames)
    {
        StringBuilder stringBuilder = new StringBuilder();
        help(global, commandNames, stringBuilder);
        System.out.println(stringBuilder.toString());
    }

    public static void help(GlobalMetadata global, List<String> commandNames, StringBuilder out)
    {
        if (commandNames.isEmpty()) {
            new GlobalUsageSummary().usage(global, out);
            return;
        }

        String name = commandNames.get(0);

        // main program?
        if (name.equals(global.getName())) {
            new GlobalUsage().usage(global, out);
            return;
        }

        // command in the default group?
        for (CommandMetadata command : global.getDefaultGroupCommands()) {
            if (name.equals(command.getName())) {
                if (USAGE_AS_HTML) {
                    out.append(new CommandUsage().usageHTML(global.getName(), null, command));
                }
                else if (USAGE_AS_RONN) {
                    out.append(new CommandUsage().usageRonn(global.getName(), null, command));
                }
                else {
                    new CommandUsage().usage(global.getName(), null, command.getName(), command, out);
                }
                return;
            }
        }

        // command in a group?
        for (CommandGroupMetadata group : global.getCommandGroups()) {
            if (name.endsWith(group.getName())) {
                // general group help or specific command help?
                if (commandNames.size() == 1) {
                    new CommandGroupUsage().usage(global, group, out);
                    return;
                }
                else {
                    String commandName = commandNames.get(1);
                    for (CommandMetadata command : group.getCommands()) {
                        if (commandName.equals(command.getName())) {
                            if (USAGE_AS_HTML) {
                                out.append(new CommandUsage().usageHTML(global.getName(), group.getName(), command));
                            }
                            else if (USAGE_AS_RONN) {
                                out.append(new CommandUsage().usageRonn(global.getName(), group.getName(), command));
                            }
                            else {
                                new CommandUsage().usage(global.getName(), group.getName(), command.getName(), command, out);
                            }

                            return;
                        }
                    }
                    System.out.println("Unknown command " + name + " " + commandName);
                }
            }
        }

        System.out.println("Unknown command " + name);
    }
}
