package fr.bdeenssat.aeebot.module;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import fr.bdeenssat.aeebot.configuration.Configuration;
import reactor.core.publisher.Mono;

public class PinMessageDeleterModule extends BotModule {

    public PinMessageDeleterModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        super(gatewayDiscordClient, configuration);
    }

    @Override
    public void initialize() {
        super.registerListener(MessageCreateEvent.class, messageCreateEvent -> {
            if (messageCreateEvent.getMessage().getType() == Message.Type.CHANNEL_PINNED_MESSAGE) {
                return messageCreateEvent.getMessage().delete();
            }

            return Mono.empty();
        });
    }
}
