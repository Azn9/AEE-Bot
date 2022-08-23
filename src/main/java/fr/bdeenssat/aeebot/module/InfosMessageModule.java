package fr.bdeenssat.aeebot.module;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.bdeenssat.aeebot.configuration.Channels;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.configuration.Members;
import fr.bdeenssat.aeebot.configuration.Roles;
import fr.bdeenssat.aeebot.manager.ErrorManager;
import reactor.core.publisher.Mono;

public class InfosMessageModule extends BotModule {

    public InfosMessageModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        super(gatewayDiscordClient, configuration);
    }

    @Override
    public void initialize() {
        super.gatewayDiscordClient.getChannelById(Channels.INFO.getId())
                .flatMap(channel -> {
                    if (!(channel instanceof MessageChannel messageChannel)) {
                        return Mono.error(new IllegalStateException("Le salon \"infos\" n'est pas un salon textuel !"));
                    }

                    return messageChannel.getPinnedMessages().count().flatMap(aLong -> {
                        if (aLong == 1) {
                            return Mono.empty();
                        }

                        return messageChannel.createMessage(EmbedCreateSpec.builder()
                                        .title("Bienvenue !")
                                        .description("""
                                                Bienvenue sur le serveur discord de l'AEE 2022-2023 !
                                                                                            
                                                Bureau de l'AEE :
                                                • Présidence : <@%d> et <@%d>
                                                • Secrétariat : <@%d> et <@%d>
                                                • Trésorerie : <@%d> et <@%d>
                                                                                            
                                                Membres de l'équipe : utilisateurs avec le rôle <@&%d>
                                                """
                                                .formatted(
                                                        Members.PREZ.getMemberId(),
                                                        Members.VICE_PREZ.getMemberId(),
                                                        Members.SECRETAIRE.getMemberId(),
                                                        Members.VICE_SECRETAIRE.getMemberId(),
                                                        Members.TREZ.getMemberId(),
                                                        Members.VICE_TREZ.getMemberId(),
                                                        Roles.AEE.getRoleId()
                                                ))
                                        .color(Color.of(170, 28, 60))
                                        .build(),
                                EmbedCreateSpec.builder()
                                        .description("""
                                                Pour avoir accès à tous les salons du serveur, sélectionnez votre année dans le salon <#%d>.
                                                """
                                                .formatted(Channels.ROLES.getChannelId()))
                                        .color(Color.MOON_YELLOW)
                                        .build(),
                                EmbedCreateSpec.builder()
                                        .title("Clubs de l'AEE")
                                        .description("""
                                                Les clubs sont l'épicentre de la vie étudiante enssatienne. Ils permettent aux étudiants qui y participent de découvrir des activités diverses et renforcent la cohésion et l'intégration au sein des promotions.
                                                Cette année l'AEE compte 25 clubs ! (Si vous souhaitez en créer un, n'hésitez pas à contacter <@%d>)
                                                                        
                                                Découvrez tous les clubs de l'enssat grâce aux salons <#%d> !
                                                Restez au courant des événements proposés par ces derniers dans le salon <#%d>.
                                                Accédez aux salons de discussion des clubs en séléctionnant ceux dont vous êtes membre via le salon <#%d>.
                                                """
                                                .formatted(
                                                        Members.RESPO_CLUBS.getMemberId(),
                                                        Channels.PRESENTATION_CLUBS.getChannelId(),
                                                        Channels.EVENTS_CLUBS.getChannelId(),
                                                        Channels.ROLES.getChannelId()
                                                ))
                                        .color(Color.MEDIUM_SEA_GREEN)
                                        .build()
                        ).flatMap(Message::pin);
                    });
                })
                .onErrorResume(ErrorManager::logError)
                .subscribe();
    }
}
