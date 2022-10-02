package fr.bdeenssat.aeebot;

import fr.bdeenssat.aeebot.configuration.Configuration;
import fr.bdeenssat.aeebot.manager.BotManager;
import io.sentry.Sentry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import reactor.tools.agent.ReactorDebugAgent;

import java.io.File;
import java.io.IOException;

public class Bot {

    private static final Logger LOGGER = LogManager.getLogger(Bot.class);
    private static boolean debugMode = false;

    public static void main(String[] args) {
        Configuration configuration = Bot.loadConfiguration();

        ReactorDebugAgent.init();
        Sentry.init(configuration.getSentryDsn());

        if (args.length == 1 && "debug".equalsIgnoreCase(args[0])) {
            Bot.enableDebugMode();
        }

        BotManager botManager = new BotManager(configuration);
        botManager.initialize();

        Runtime.getRuntime().addShutdownHook(new Thread(botManager::shutdown));

        botManager.start();
    }

    private static Configuration loadConfiguration() {
        File configurationFile = new File("configuration.json");
        if (!configurationFile.exists()) {
            LOGGER.error("Configuration file not found");
            System.exit(1);
            return null;
        }

        if (!configurationFile.canRead()) {
            LOGGER.error("Configuration file not readable");
            System.exit(1);
            return null;
        }

        if (!configurationFile.isFile()) {
            LOGGER.error("Configuration file is not a file");
            System.exit(1);
            return null;
        }

        try {
            return Configuration.load(configurationFile);
        } catch (IOException exception) {
            LOGGER.error("Error while loading configuration", exception);
            System.exit(1);
            return null;
        }
    }

    private static void enableDebugMode() {
        Bot.debugMode = true;

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = loggerContext.getConfiguration();
        LoggerConfig rootLoggerConfig = config.getLoggers().get(Bot.class.getName());
        rootLoggerConfig.removeAppender("Console");
        rootLoggerConfig.addAppender(config.getAppender("Console"), Level.TRACE, null);
        loggerContext.updateLoggers();

        Bot.LOGGER.trace("====================");
        Bot.LOGGER.trace("DEBUG MODE ENABLED");
        Bot.LOGGER.trace("====================");
    }

    public static boolean isInDebugMode() {
        return debugMode;
    }
}
