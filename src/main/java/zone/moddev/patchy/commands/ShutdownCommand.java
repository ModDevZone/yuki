package zone.moddev.patchy.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zone.moddev.patchy.Patchy;

public class ShutdownCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("shutdown")) {
            String botOwnerId = Patchy.getInstance().getConfigManager().getPatchyConfig().getBotOwnerId();
            if (event.getUser().getId().equals(botOwnerId)) {
                event.reply("Shutting down...").queue();
                Patchy.getInstance().shutdown();
            } else {
                event.reply("You do not have permission to do that.").setEphemeral(true).queue();
            }
        }
    }
}
