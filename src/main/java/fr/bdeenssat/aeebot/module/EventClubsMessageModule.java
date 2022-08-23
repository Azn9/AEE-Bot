package fr.bdeenssat.aeebot.module;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.bdeenssat.aeebot.configuration.Channels;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.configuration.Roles;
import fr.bdeenssat.aeebot.manager.ErrorManager;
import reactor.core.publisher.Mono;

public class EventClubsMessageModule extends BotModule {

    public EventClubsMessageModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        super(gatewayDiscordClient, configuration);
    }

    @Override
    public void initialize() {
        super.gatewayDiscordClient.getChannelById(Channels.EVENTS_CLUBS.getId())
                .flatMap(channel -> {
                    if (!(channel instanceof MessageChannel messageChannel)) {
                        return Mono.error(new IllegalStateException("Le salon \"events_clubs\" n'est pas un salon textuel !"));
                    }

                    return messageChannel.getPinnedMessages().count().flatMap(aLong -> {
                        if (aLong == 1) {
                            return Mono.empty();
                        }

                        return messageChannel.createMessage(EmbedCreateSpec.builder()
                                        .description("""
                                                Ce salon sera utilisé pour communiquer autour des événements organisés par les différents clubs de l'AEE. Les <@&%d> peuvent y écrire.
                                                """.formatted(Roles.PREZ_CLUB.getRoleId()))
                                        .color(Color.of(170, 28, 60))
                                        .build())
                                .flatMap(Message::pin);
                    });
                })
                .onErrorResume(ErrorManager::logError)
                .subscribe();
    }
}
