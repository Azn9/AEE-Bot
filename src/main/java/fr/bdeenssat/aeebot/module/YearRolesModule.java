package fr.bdeenssat.aeebot.module;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import fr.bdeenssat.aeebot.configuration.Channels;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.configuration.Roles;
import fr.bdeenssat.aeebot.manager.ErrorManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class YearRolesModule extends BotModule {

    public YearRolesModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        super(gatewayDiscordClient, configuration);
    }

    @Override
    public void initialize() {
        super.registerListener(SelectMenuInteractionEvent.class, this::handle);

        super.gatewayDiscordClient.getChannelById(Channels.ROLES.getId())
                .flatMap(channel -> {
                    if (!(channel instanceof MessageChannel messageChannel)) {
                        return Mono.error(new IllegalStateException("Le salon \"roles\" n'est pas un salon textuel !"));
                    }

                    return messageChannel.getPinnedMessages().count().flatMap(aLong -> {
                        if (aLong == 2) {
                            return Mono.empty();
                        }

                        return messageChannel.createMessage(MessageCreateSpec.builder()
                                        .embeds(EmbedCreateSpec.builder()
                                                .title("Sélectionnez votre promotion")
                                                .color(Color.MOON_YELLOW)
                                                .build())
                                        .components(ActionRow.of(
                                                SelectMenu.of(
                                                        "year-selectmenu",
                                                        Roles._1A.getAsOption(),
                                                        Roles._2A.getAsOption(),
                                                        Roles._3A.getAsOption()
                                                ).withMinValues(1).withMaxValues(1).withPlaceholder("Sélectionnez votre promotion")
                                        ))
                                        .build())
                                .flatMap(Message::pin);
                    });
                })
                .onErrorResume(ErrorManager::logError)
                .subscribe();
    }

    private Mono<?> handle(SelectMenuInteractionEvent event) {
        if (!"year-selectmenu".equals(event.getCustomId())) {
            return Mono.empty();
        }

        if (event.getInteraction().getMember().isEmpty()) {
            return Mono.empty();
        }

        Member member = event.getInteraction().getMember().get();

        return event.deferReply().withEphemeral(true)
                .then(Mono.just(event.getValues().get(0))
                        .flatMap(value -> {
                            long roleId = switch (value) {
                                case "_1A" -> Roles._1A.getRoleId();
                                case "_2A" -> Roles._2A.getRoleId();
                                case "_3A" -> Roles._3A.getRoleId();
                                default -> throw new IllegalArgumentException("Invalid selection: " + value);
                            };

                            return Mono.when(Flux.fromIterable(List.of(Roles._1A, Roles._2A, Roles._3A))
                                    .flatMap(role -> {
                                        return member.removeRole(role.getId());
                                    })).then(member.addRole(Snowflake.of(roleId)));
                        }))
                .then(event.createFollowup("Le rôle sélectionné vous a été attribué !"));
    }

}
