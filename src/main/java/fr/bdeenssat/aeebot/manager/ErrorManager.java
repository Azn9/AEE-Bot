package fr.bdeenssat.aeebot.manager;

import fr.bdeenssat.aeebot.Bot;
import io.sentry.Sentry;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.protocol.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;

public class ErrorManager {

    private static final Logger LOGGER = LogManager.getLogger(Bot.class);

    public static <T> Mono<T> logError(Throwable throwable) {
        for (Throwable suppressed : throwable.getSuppressed()) {
            ErrorManager.logError(suppressed);
        }

        if (!Bot.isInDebugMode()) {
            Sentry.captureException(throwable);
        }

        ErrorManager.LOGGER.error(throwable);

        return Mono.empty();
    }

    public static <T> Mono<T> logError(Throwable throwable, String hint) {
        for (Throwable suppressed : throwable.getSuppressed()) {
            ErrorManager.logError(suppressed, hint);
        }

        if (!Bot.isInDebugMode()) {
            SentryEvent sentryEvent = new SentryEvent();
            sentryEvent.setLevel(SentryLevel.ERROR);
            sentryEvent.setThrowable(throwable);
            Message message = new Message();
            message.setMessage(throwable.getMessage() + " | " + hint);
            sentryEvent.setMessage(message);
            Sentry.captureEvent(sentryEvent);
        }

        ErrorManager.LOGGER.error(hint, throwable);

        return Mono.empty();
    }

}
