package fr.bdeenssat.aeebot.configuration;

public enum Members {

    PREZ(551024106640769062L),
    VICE_PREZ(443694571168202762L),
    SECRETAIRE(468515442185601025L),
    VICE_SECRETAIRE(692055694014152834L),
    TREZ(175931728920182784L),
    VICE_TREZ(177000261233934336L),
    RESPO_CLUBS(247050493929586689L),

    ;

    private final long memberId;

    Members(long memberId) {
        this.memberId = memberId;
    }

    public long getMemberId() {
        return this.memberId;
    }
}
