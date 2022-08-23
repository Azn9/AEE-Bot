import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.ExtendedPermissionOverwrite;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.NewsChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.gateway.intent.IntentSet;
import fr.bdeenssat.aeebot.configuration.Roles;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class Migrations {

    public static void main(String[] args) {
        GatewayDiscordClient gatewayDiscordClient = DiscordClientBuilder.create("")
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .login()
                .block();

        Guild guild = gatewayDiscordClient.getGuildById(Snowflake.of(831645904384557097L)).block();

        guild.getChannels().flatMap(guildChannel -> {
           if (guildChannel instanceof TextChannel textChannel) {
               List<PermissionOverwrite> permissionOverwriteList = new ArrayList<>();

               for (ExtendedPermissionOverwrite permissionOverwrite : textChannel.getPermissionOverwrites()) {
                   if (permissionOverwrite.getRoleId().orElse(Snowflake.of(0L)).asLong() != Roles.AEE.getRoleId()) {
                       permissionOverwriteList.add(permissionOverwrite);
                   }
               }

               textChannel.edit().withPermissionOverwrites(permissionOverwriteList).block();
           } else if (guildChannel instanceof NewsChannel newsChannel) {
               List<PermissionOverwrite> permissionOverwriteList = new ArrayList<>();

               for (ExtendedPermissionOverwrite permissionOverwrite : newsChannel.getPermissionOverwrites()) {
                   if (permissionOverwrite.getRoleId().orElse(Snowflake.of(0L)).asLong() != Roles.AEE.getRoleId()) {
                       permissionOverwriteList.add(permissionOverwrite);
                   }
               }

               newsChannel.edit().withPermissionOverwrites(permissionOverwriteList).block();
           }

           return Mono.empty();
        }).blockLast();

/*

        for (Clubs value : Clubs.values()) {
            Role role = guild.createRole(RoleCreateSpec.builder()
                    .name("Bureau " + value.getDisplayName())
                    .color(Color.SEA_GREEN)
                    .build()).block();

            guild.createTextChannel(TextChannelCreateSpec.builder()
                    .name(value.getDisplayName())
                    .parentId(Snowflake.of(1011653726307622983L))
                    .permissionOverwrites(
                            PermissionOverwrite.forRole(Snowflake.of(831645904384557097L), PermissionSet.none(), PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES)),
                            PermissionOverwrite.forRole(Roles._1A.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none()),
                            PermissionOverwrite.forRole(Roles._2A.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none()),
                            PermissionOverwrite.forRole(Roles._3A.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none()),
                            PermissionOverwrite.forRole(role.getId(), PermissionSet.of(Permission.SEND_MESSAGES), PermissionSet.none()),
                            PermissionOverwrite.forRole(Roles.AEE.getId(), PermissionSet.of(Permission.SEND_MESSAGES), PermissionSet.none())
                    )
                    .build()).block();

            Category category = guild.createCategory(CategoryCreateSpec.builder()
                    .name("Club " + value.getDisplayName())
                    .permissionOverwrites(
                            PermissionOverwrite.forRole(Snowflake.of(831645904384557097L), PermissionSet.none(), PermissionSet.of(Permission.VIEW_CHANNEL)),
                            PermissionOverwrite.forRole(value.getRoleId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none()),
                            PermissionOverwrite.forRole(Roles.AEE.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none())
                    )
                    .build()).block();

            Snowflake parentId = category.getId();

            guild.createNewsChannel(NewsChannelCreateSpec.builder()
                    .name("informations")
                    .permissionOverwrites(
                            PermissionOverwrite.forRole(Snowflake.of(831645904384557097L), PermissionSet.none(), PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES)),
                            PermissionOverwrite.forRole(value.getRoleId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none()),
                            PermissionOverwrite.forRole(role.getId(), PermissionSet.of(Permission.SEND_MESSAGES), PermissionSet.none()),
                            PermissionOverwrite.forRole(Roles.AEE.getId(), PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES), PermissionSet.none())
                    )
                    .parentId(parentId)
                    .build()).block();

            guild.createTextChannel(TextChannelCreateSpec.builder()
                    .name("général")
                    .permissionOverwrites(
                            PermissionOverwrite.forRole(Snowflake.of(831645904384557097L), PermissionSet.none(), PermissionSet.of(Permission.VIEW_CHANNEL)),
                            PermissionOverwrite.forRole(value.getRoleId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none()),
                            PermissionOverwrite.forRole(Roles.AEE.getId(), PermissionSet.of(Permission.VIEW_CHANNEL), PermissionSet.none())
                    )
                    .parentId(parentId)
                    .build()).block();
        }
*/

        while (true) {
            ;
        }

        //gatewayDiscordClient.logout().block();
    }

}
