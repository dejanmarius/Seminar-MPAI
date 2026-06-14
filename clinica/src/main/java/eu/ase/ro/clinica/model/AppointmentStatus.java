package eu.ase.ro.clinica.model;

import java.util.List;

/**
 * Starile unei programari.
 * SOLICITATA -> CONFIRMATA -> EFECTUATA
 *      \-> ANULATA        \-> ANULATA
 */
public enum AppointmentStatus {
    SOLICITATA,
    CONFIRMATA,
    EFECTUATA,
    ANULATA;

    /** Starile in care poate trece din starea curenta (gol = stare finala). */
    public List<AppointmentStatus> nextStates() {
        return switch (this) {
            case SOLICITATA -> List.of(CONFIRMATA, ANULATA);
            case CONFIRMATA -> List.of(EFECTUATA, ANULATA);
            default         -> List.of();
        };
    }

    /** Pacientul poate anula doar cat timp programarea nu e efectuata/anulata. */
    public boolean canBeCancelledByPatient() {
        return this == SOLICITATA || this == CONFIRMATA;
    }
}
