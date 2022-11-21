package fr.bdeenssat.aeebot.module;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import fr.bdeenssat.aeebot.configuration.Channels;
import fr.bdeenssat.aeebot.configuration.Clubs;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.manager.ErrorManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClubsRolesModule extends BotModule {

    public ClubsRolesModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
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

                        List<LayoutComponent> actionRowList = new ArrayList<>();

                        if (Clubs.values().length > 25) {
                            int neededRows = (int) Math.ceil(Clubs.values().length / 25.0);

                            for (int i = 0; i < neededRows; i++) {
                                List<SelectMenu.Option> options = Stream.of(Clubs.values())
                                        .skip(i * 25L)
                                        .limit(25)
                                        .map(Clubs::asOption)
                                        .toList();

                                SelectMenu selectMenu = SelectMenu.of("clubs-selectmenu-" + i, options).withMinValues(1).withMaxValues(options.size());

                                actionRowList.add(ActionRow.of(selectMenu));
                            }
                        } else {
                            actionRowList.add(ActionRow.of(
                                    SelectMenu.of(
                                            "clubs-selectmenu",
                                            Clubs.asOptionList()
                                    ).withMinValues(1).withMaxValues(Clubs.values().length).withPlaceholder("Sélectionnez le(s) club(s) dont vous faites partie")
                            ));
                        }

                        return messageChannel.createMessage(MessageCreateSpec.builder()
                                        .embeds(EmbedCreateSpec.builder()
                                                .title("Sélectionnez le(s) club(s) dont vous faites partie")
                                                .description("Sélectionnez à nouveau un club pour vous retirer son rôle en cas d'erreur")
                                                .color(Color.MEDIUM_SEA_GREEN)
                                                .build())
                                        .components(actionRowList)
                                        .build())
                                .flatMap(Message::pin);
                    });
                })
                .onErrorResume(ErrorManager::logError)
                .subscribe();
    }

    private Mono<?> handle(SelectMenuInteractionEvent event) {
        if (!event.getCustomId().startsWith("clubs-selectmenu-")) {
            return Mono.empty();
        }

        if (event.getInteraction().getMember().isEmpty()) {
            return Mono.empty();
        }

        Member member = event.getInteraction().getMember().get();

        return event.deferReply().withEphemeral(true)
                .then(Flux.fromArray(Clubs.values())
                        .flatMap(club -> {
                            if (!event.getValues().contains(club.name())) {
                                return Mono.empty();
                            }

                            if (member.getRoleIds().contains(club.getRoleId())) {
                                return member.removeRole(club.getRoleId())
                                        .then(Mono.just(1)); // Used to compute the added role count
                            } else {
                                return member.addRole(club.getRoleId())
                                        .then(Mono.just(1)); // Used to compute the added role count
                            }
                        })
                        .count()
                        .flatMap(aLong -> {
                            if (aLong == 0) {
                                return event.createFollowup("Veuillez choisir au moins une option");
                            } else if (aLong > 1) {
                                return event.createFollowup("Vous avez désormais accès aux salons des clubs sélectionnés !");
                            } else {
                                return event.createFollowup("Vous avez désormais accès aux salons du club sélectionné !");
                            }
                        }));
    }

}
