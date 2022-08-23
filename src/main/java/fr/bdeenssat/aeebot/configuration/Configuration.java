package fr.bdeenssat.aeebot.configuration;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Configuration {

    private final String discordToken;
    private final String sentryDsn;

    public Configuration() {
        this.discordToken = "";
        this.sentryDsn = "";
    }

    public static Configuration load(File configurationFile) throws IOException {
        Gson gson = new Gson();

        try (JsonReader jsonReader = new JsonReader(new FileReader(configurationFile))) {
            return gson.fromJson(jsonReader, Configuration.class);
        }
    }

    public String getDiscordToken() {
        return this.discordToken;
    }

    public String getSentryDsn() {
        return this.sentryDsn;
    }
}
