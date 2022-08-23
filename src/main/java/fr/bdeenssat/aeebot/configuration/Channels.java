package fr.bdeenssat.aeebot.configuration;

import discord4j.common.util.Snowflake;

public enum Channels {

    INFO(864838366624350218L),
    ROLES(883149484496920586L),
    ANNONCES(1011678833939849276L),
    EVENTS_AEE(1009922215006961786L),
    EVENTS_CLUBS(1011634120008421517L),
    PRESENTATION_CLUBS(1011653726307622983L),
    PETITES_ANNONCES(1011679052953817089L),

    ;

    private final long channelId;

    Channels(long channelId) {
        this.channelId = channelId;
    }

    public long getChannelId() {
        return this.channelId;
    }

    public Snowflake getId() {
        return Snowflake.of(this.channelId);
    }
}
