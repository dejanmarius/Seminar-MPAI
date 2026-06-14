package eu.ase.ro.inchirieri.model;

import java.util.List;

/**
 * Starile unei cereri de inchiriere.
 * CERUTA -> APROBATA -> PRELUATA -> RETURNATA
 *    |  \-> RESPINSA
 *    \-> ANULATA (de utilizator, inainte de aprobare)
 */
public enum RentalStatus {
    CERUTA,
    APROBATA,
    PRELUATA,
    RETURNATA,
    RESPINSA,
    ANULATA;

    /** Starile in care poate trece din starea curenta (gol = stare finala). */
    public List<RentalStatus> nextStates() {
        return switch (this) {
            case CERUTA   -> List.of(APROBATA, RESPINSA);
            case APROBATA -> List.of(PRELUATA);
            case PRELUATA -> List.of(RETURNATA);
            default       -> List.of();
        };
    }

    /** Utilizatorul poate anula doar inainte de aprobare. */
    public boolean canBeCancelledByUser() {
        return this == CERUTA;
    }
}
