package eu.ase.ro.examen.model;

import java.util.List;

public enum OrderStatus {
    PLASATA,
    PROCESATA,
    EXPEDIATA,
    LIVRATA,
    ANULATA;

    /**
     * Returneaza starile in care comanda poate trece din starea curenta.
     * O lista goala inseamna ca starea curenta este finala.
     */
    public List<OrderStatus> nextStates() {
        return switch (this) {
            case PLASATA   -> List.of(PROCESATA, ANULATA);
            case PROCESATA -> List.of(EXPEDIATA);
            case EXPEDIATA -> List.of(LIVRATA);
            default        -> List.of();
        };
    }
}
