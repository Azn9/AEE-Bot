package fr.bdeenssat.aeebot.configuration;

import discord4j.common.util.Snowflake;
import discord4j.core.object.component.SelectMenu;

public enum Roles {

    AEE("AEE", 864832803044982835L),
    _1A("1A (Promo 2025)", 1011633021822173266L),
    _2A("2A (Promo 2024)", 1011633197840351333L),
    _3A("3A (Promo 2023)", 1011633230153257071L),
    PREZ_CLUB("Président de club", 1011655127255486464L),

    ;

    private final String displayName;
    private final long roleId;

    Roles(String displayName, long roleId) {
        this.displayName = displayName;
        this.roleId = roleId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public Snowflake getId() {
        return Snowflake.of(this.roleId);
    }

    public SelectMenu.Option getAsOption() {
        return SelectMenu.Option.of(this.displayName, this.name());
    }
}
