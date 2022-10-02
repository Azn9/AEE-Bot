package fr.bdeenssat.aeebot.manager;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.gateway.intent.IntentSet;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.module.AnnoncesMessageModule;
import fr.bdeenssat.aeebot.module.BotModule;
import fr.bdeenssat.aeebot.module.ClubsRolesModule;
import fr.bdeenssat.aeebot.module.EventAeeMessageModule;
import fr.bdeenssat.aeebot.module.EventClubsMessageModule;
import fr.bdeenssat.aeebot.module.InfosMessageModule;
import fr.bdeenssat.aeebot.module.PetitesAnnoncesMessageModule;
import fr.bdeenssat.aeebot.module.PinMessageDeleterModule;
import fr.bdeenssat.aeebot.module.YearRolesModule;

import java.util.ArrayList;
import java.util.List;

public class BotManager {

    private final List<Class<? extends BotModule>> botModules = new ArrayList<>();
    private final Configuration configuration;
    private GatewayDiscordClient gatewayClient;

    public BotManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public void initialize() {
        this.botModules.add(PinMessageDeleterModule.class);
        this.botModules.add(InfosMessageModule.class);
        this.botModules.add(YearRolesModule.class);
        this.botModules.add(ClubsRolesModule.class);
        this.botModules.add(AnnoncesMessageModule.class);
        this.botModules.add(EventAeeMessageModule.class);
        this.botModules.add(EventClubsMessageModule.class);
        this.botModules.add(PetitesAnnoncesMessageModule.class);
    }

    public void start() {
        DiscordClient discordClient = DiscordClient.create(this.configuration.getDiscordToken().trim());

        this.gatewayClient = discordClient.gateway()
                .setEnabledIntents(IntentSet.all())
                .login()
                .block();

        if (this.gatewayClient == null) {
            throw new IllegalStateException("Gatewayclient is null");
        }

        for (Class<? extends BotModule> botModule : this.botModules) {
            try {
                BotModule module = botModule.getDeclaredConstructor(GatewayDiscordClient.class, Configuration.class).newInstance(gatewayClient, this.configuration);
                module.initialize();
            } catch (Exception exception) {
                ErrorManager.logError(exception, botModule.getName());
            }
        }

        synchronized (this) {
            try {
                this.wait();  // Wait indefinitely
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void shutdown() {
        if (this.gatewayClient != null) {
            try {
                this.gatewayClient.logout().block();
            } catch (Exception exception) {
                ErrorManager.logError(exception, this.getClass().getName());
            }
        }

        synchronized (BotManager.this) {
            BotManager.this.notify();
        }
    }

}
