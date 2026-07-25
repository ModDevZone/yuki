package zone.moddev.patchy.util.webhook;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.attribute.IWebhookContainer;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class WebhookManagerImpl implements WebhookManager {
    private final Predicate<String> predicate;
    private final String webhookName;
    private final Collection<Message.MentionType> allowedMentions;
    private final Long2ObjectMap<WebhookClient<Message>> webhooks = new Long2ObjectOpenHashMap<>();
    @Nullable
    private final Consumer<Webhook> creationListener;

    public WebhookManagerImpl(final Predicate<String> predicate, final String webhookName, final Collection<Message.MentionType> allowedMentions, @Nullable final Consumer<Webhook> creationListener) {
        this.predicate = predicate;
        this.webhookName = webhookName;
        this.allowedMentions = allowedMentions;
        this.creationListener = creationListener;
    }

    @Override
    public WebhookClient<Message> getWebhook(final IWebhookContainer channel) {
        return webhooks.computeIfAbsent(channel.getIdLong(), k -> getOrCreateWebhook(channel));
    }

    @Override
    public void sendAndCrosspost(final IWebhookContainer channel, @Nullable final String username, @Nullable final String avatarUrl, final MessageCreateData message) {
        WebhookMessageCreateAction<Message> action = getWebhook(channel).sendMessage(message).setAllowedMentions(allowedMentions);
        if (username != null) {
            action = action.setUsername(username);
        }
        if (avatarUrl != null) {
            action = action.setAvatarUrl(avatarUrl);
        }

        action.queue(msg -> {
            if (channel.getType() == ChannelType.NEWS) {
                ((NewsChannel) channel).retrieveMessageById(msg.getId()).flatMap(Message::crosspost).queue();
            }
        });
    }

    private Webhook getOrCreateWebhook(IWebhookContainer channel) {
        final var alreadyExisted = unwrap(Objects.requireNonNull(channel).retrieveWebhooks()
                .submit(false))
                .stream()
                .filter(w -> predicate.test(w.getName()))
                .findAny();
        return alreadyExisted.orElseGet(() -> {
            final var webhook = unwrap(channel.createWebhook(webhookName).submit(false));
            if (creationListener != null) {
                creationListener.accept(webhook);
            }
            return webhook;
        });
    }

    private static <T> T unwrap(CompletableFuture<T> completableFuture) {
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        webhooks.clear();
    }
}
