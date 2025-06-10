package totolotek.util;

// klasa na stale - zapobieganie magic numbers
// w trakcie pisania zmienilem koncepcje na przerzucanie stalych do klas
// chcialem przetestowac obie opje, i chyba trzymanie w klasach jest lepsze
// bardziej obiektowe
public final class Stale {
    // prywatny konstruktor aby nikt nie tworzyl
    private Stale(){}

    // kwoty w groszach

    public static final int MIN_LOSOWANY_NUMER = 1;
    public static final int MAX_LOSOWANY_NUMER = 49;
    public static final int SZESC_LICZB_W_ZAKLADZIE = 6;

    public static final int MAX_LOSOWAN_BLANKIET = 10;
    public static final int DOMYSLNA_LICZBA_LOSOWAN_BLANKIET = 1;

    public static final int OGRANICZENIE_GORNE_ZNACZNIK_LOSOWY = 1_000_000_000;
    public static final int ILE_MODULO_ZNACZNIK_LOSOWY = 100;


}
