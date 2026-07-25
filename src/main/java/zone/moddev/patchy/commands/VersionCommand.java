package zone.moddev.patchy.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import zone.moddev.patchy.Patchy;

public class VersionCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("version")) {
            String version = Patchy.class.getPackage().getImplementationVersion();
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(0x2e8b57) // SeaGreen
                    .setTitle("Patchy Information")
                    .setDescription("Patchy! A Discord bot that can post about updates and new releases of tools and game versions in configurable channels. Find out more with the links below!")
                    .addField("Version", version == null ? "DEVELOPMENT_BUILD" : version, true)
                    .addField("Website", "[moddev.zone](<" + Patchy.WEBSITE_URL + ">)", true)
                    .addField("Source Code", "[GitHub](<" + Patchy.GITHUB_REPO + ">)", true);

            event.replyEmbeds(embed.build()).queue();
        }
    }
}
