package fr.bdeenssat.aeebot.configuration;

import discord4j.common.util.Snowflake;
import discord4j.core.object.component.SelectMenu;

import java.util.Arrays;
import java.util.List;

public enum Clubs {

    CHEERLEADING("Cheerleading", 1020351105047212032L),
    CONVI("Convivialité", 1011662597239611492L),
    CYBER("Cyber", 1020305147252981771L),
    DANCE_ROCK("Dance (Rock)", 1044235630688874526L),
    ENSSALTIMBANQUE("Enssaltimbanque", 1011662770904764478L),
    ENSSAT_ET_MAT("ENSSAT et mat", 1011662943584264192L),
    ENSSAT_MOTORSPORT("Enssat Motorsport", 1011663103857004687L),
    //ENSSAT_TV("Enssat TV", 1011663424020815872L), // Club arrêté
    ENSSAT_VISUALS("Enssat visuals", 1011663549573124196L),
    ENSSATABLE("Enssatable", 1011663698177310880L),
    ENSSATELIER("Enssatelier", 1011663905623396372L),
    ENSSATELLITE("EnssatEllite", 1011666039773351946L),
    ENSSOFT("ENSSOFT", 1011666151668973678L),
    GAMING("Gaming", 1011666816617164830L),
    //GEOCACHING("Géocaching", 1011666911274217543L), // Club arrêté
    HOLOGRAPHIE("Holographie", 1011667028249153576L),
    //IA("IA", 1011667101070659654L), // Club arrêté
    //INTERNATIONAL("International", 1011667316406222848L), // Club arrêté
    JAPANIM("Japanim", 1011667491187069068L),
    //JDC("Jeux de cartes", 1011667576536957018L), // Ancien jeu de cartes, merge avec Magic
    JDC("Jeux de cartes", 1011672144964497478L),
    JDP("Jeux de plateaux", 1011667687673442385L),
    JDR("Jeux de rôles", 1011667751263285358L),
    //KFET("KFET", 1011667876219998339L), // Club arrêté
    //L_ENSSAT_A_LES_BOULES("L'Enssat a les boules", 1011668318559674473L), // Club arrêté
    //MAGIC("Magic", 1011672144964497478L), // Merge avec JDC
    MUSIQUE("Musique", 1011672186119012393L),
    PHOTO("Photo", 1011672239948697711L),
    POKER("Poker", 1011672282449575966L),
    ROBOT("Robot", 1011672315412611102L),
    //ROCK("Rock", 1011672361587720263L), // Club arrêté
    //SPEEDRUN("Speedrun", 1011672390826213486L), // Club arrêté
    SKATE("Skate", 1032976339130667018L),
    SPRAY_PAINTING("Spray Painting", 1011672426645569696L),
    //STREET_WORKOUT("Street Workout", 1011672473433034822L), // Club arrêté
    TEA_TIME("Tea time", 1011672508048613386L),
    THEATRE("Théâtre", 1011672543578558464L),

    ;

    private final String displayName;
    private final Long roleId;

    Clubs(String displayName, Long roleId) {
        this.displayName = displayName;
        this.roleId = roleId;
    }

    public static List<SelectMenu.Option> asOptionList() {
        return Arrays.stream(values()).map(Clubs::asOption).toList();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Snowflake getRoleId() {
        return Snowflake.of(this.roleId);
    }

    public SelectMenu.Option asOption() {
        return SelectMenu.Option.of(this.displayName, this.name());
    }
}
