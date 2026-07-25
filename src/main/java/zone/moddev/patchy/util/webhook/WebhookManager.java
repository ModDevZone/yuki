package zone.moddev.patchy.util.webhook;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface WebhookManager extends AutoCloseable {

    static WebhookManager of(String name) {
        return of(e -> e.trim().equals(name), name, Collections.emptyList());
    }

    static WebhookManager of(Predicate<String> matcher, String webhookName, Collection<Message.MentionType> allowedMentions, @Nullable Consumer<Webhook> creationListener) {
        return new WebhookManagerImpl(matcher, webhookName, allowedMentions, creationListener);
    }

    static WebhookManager of(Predicate<String> matcher, String webhookName, Collection<Message.MentionType> allowedMentions) {
        return of(matcher, webhookName, allowedMentions, null);
    }

    WebhookClient<Message> getWebhook(IWebhookContainer channel);

    void sendAndCrosspost(IWebhookContainer channel, @Nullable String username, @Nullable String avatarUrl, MessageCreateData message);

    @Override
    void close();
}
