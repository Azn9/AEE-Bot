package fr.bdeenssat.aeebot.module;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.manager.ErrorManager;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class BotModule {

    protected final GatewayDiscordClient gatewayDiscordClient;
    protected final Configuration configuration;

    public BotModule(GatewayDiscordClient gatewayDiscordClient, Configuration configuration) {
        this.gatewayDiscordClient = gatewayDiscordClient;
        this.configuration = configuration;
    }

    public abstract void initialize();

    public <T extends Event> void registerListener(Class<T> eventClass, Function<T, Mono<?>> handler) {
        this.gatewayDiscordClient.getEventDispatcher()
                .on(eventClass)
                .flatMap(handler)
                .onErrorResume(throwable -> ErrorManager.logError(throwable, this.getClass().getName()))
                .subscribe();
    }

}
