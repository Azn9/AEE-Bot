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

public class PetitesAnnoncesMessageModule extends BotModule {

    public PetitesAnnoncesMessageModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        super(gatewayDiscordClient, configuration);
    }

    @Override
    public void initialize() {
        super.gatewayDiscordClient.getChannelById(Channels.PETITES_ANNONCES.getId())
                .flatMap(channel -> {
                    if (!(channel instanceof MessageChannel messageChannel)) {
                        return Mono.error(new IllegalStateException("Le salon \"petites_annonces\" n'est pas un salon textuel !"));
                    }

                    return messageChannel.getPinnedMessages().count().flatMap(aLong -> {
                        if (aLong == 1) {
                            return Mono.empty();
                        }

                        return messageChannel.createMessage(EmbedCreateSpec.builder()
                                        .description("""
                                                Vous cherchez ou vendez quelque chose ou souhaitez informer les élèves de quelque chose ? N'hésitez pas à passer vos petites annonces dans ce salon !
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
