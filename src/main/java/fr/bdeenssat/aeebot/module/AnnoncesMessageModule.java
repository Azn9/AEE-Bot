package fr.bdeenssat.aeebot.module;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.bdeenssat.aeebot.configuration.Channels;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.manager.ErrorManager;
import reactor.core.publisher.Mono;

public class AnnoncesMessageModule extends BotModule {

    public AnnoncesMessageModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        super(gatewayDiscordClient, configuration);
    }

    @Override
    public void initialize() {
        super.gatewayDiscordClient.getChannelById(Channels.ANNONCES.getId())
                .flatMap(channel -> {
                    if (!(channel instanceof MessageChannel messageChannel)) {
                        return Mono.error(new IllegalStateException("Le salon \"annonces\" n'est pas un salon textuel !"));
                    }

                    return messageChannel.getPinnedMessages().count().flatMap(aLong -> {
                        if (aLong == 1) {
                            return Mono.empty();
                        }

                        return messageChannel.createMessage(EmbedCreateSpec.builder()
                                        .description("""
                                                Ce salon sera utilisé pour communiquer toutes les informations importantes de l'AEE aux élèves
                                                """)
                                        .color(Color.of(170, 28, 60))
                                        .build())
                                .flatMap(Message::pin);
                    });
                })
                .onErrorResume(ErrorManager::logError)
                .subscribe();
    }
}
