package eu.ase.ro.schelet.model;

import java.util.List;

// TODO: rename enum si valori
// S1 comenzi:    PLASATA, PROCESATA, EXPEDIATA, LIVRATA, ANULATA
// S2 programari: SOLICITATA, CONFIRMATA, EFECTUATA, ANULATA
// S3 inchirieri: CERUTA, APROBATA, PRELUATA, RETURNATA, RESPINSA
public enum ItemStatus {
    STARE_INITIALA,   // TODO: rename
    STARE_2,          // TODO: rename
    STARE_3,          // TODO: rename
    STARE_FINALA,     // TODO: rename
    ANULATA;          // TODO: rename sau sterge

    public List<ItemStatus> nextStates() {
        return switch (this) {
            // TODO: defineste tranzitiile permise
            // S1: PLASATA -> PROCESATA, ANULATA
            //     PROCESATA -> EXPEDIATA
            //     EXPEDIATA -> LIVRATA
            // S2: SOLICITATA -> CONFIRMATA, ANULATA
            //     CONFIRMATA -> EFECTUATA, ANULATA
            // S3: CERUTA -> APROBATA, ANULATA (RESPINSA)
            //     APROBATA -> PRELUATA
            //     PRELUATA -> RETURNATA
            case STARE_INITIALA -> List.of(STARE_2, ANULATA);
            case STARE_2        -> List.of(STARE_3);
            case STARE_3        -> List.of(STARE_FINALA);
            default             -> List.of();
        };
    }
}
