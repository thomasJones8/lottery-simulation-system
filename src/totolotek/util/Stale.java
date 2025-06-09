package totolotek.util;

// klasa na stale - zapobieganie magic numbers
public final class Stale {
    // prywatny konstruktor aby nikt nie tworzyl
    private Stale(){}

    // kwoty w groszach

    public static final int MIN_LOSOWANY_NUMER = 1;
    public static final int MAX_LOSOWANY_NUMER = 49;
    public static final int SZESC_LICZB_W_ZAKLADZIE = 6;
    public static final long PROG_PODATEK_NAGRODA = 228000L;
    public static final int PROCENT_PODATEK_NAGRODA = 10;
    public static final int PROCENT_NA_1_STOPIEN = 44;
    public static final int PROCENT_NA_2_STOPIEN = 8;
    public static final long NAGRODA_4_STOPNIA = 2400L;
    public static final long MIN_PULA_1_STOPIEN = 200000000L;
    public static final long MIN_NAGRODA_3_STOPIEN = 15L * (CENA_ZAKLADU - PODATEK_ZAKLAD);

    public static final int MAX_LOSOWAN_BLANKIET = 10;
    public static final int DOMYSLNA_LICZBA_LOSOWAN_BLANKIET = 1;

    public static final int OGRANICZENIE_GORNE_ZNACZNIK_LOSOWY = 1_000_000_000;
    public static final int ILE_MODULO_ZNACZNIK_LOSOWY = 100;


}
