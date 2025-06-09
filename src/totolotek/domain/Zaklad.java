package totolotek.domain;

import totolotek.util.Stale;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Zaklad {

    public static final long CENA_BRUTTO = 300L;
    public static final long PODATEK = 60L;
    public static final long CENA_NETTO = CENA_BRUTTO - PODATEK;
    private final HashSet<> liczby;

    // moze niekoniecznie public?
    public Zaklad(Set<Integer> liczby) throws IllegalArgumentException {
        // czy musze sprawdzac, czy |set| = 6 -- raczej tak
        if (liczby.size() != Stale.SZESC_LICZB_W_ZAKLADZIE) {
            throw new IllegalArgumentException("Obstawiono " + liczby.size()
            + " liczb zamiast " + Stale.SZESC_LICZB_W_ZAKLADZIE);
        }
        // czy nie jest null -- raczej nie

        // sprawdzenie czy liczby sa z zakresu 1-49
        for (int liczba: liczby) {
            if (liczba < Stale.MIN_LOSOWANY_NUMER  || Stale.MAX_LOSOWANY_NUMER < liczba) {
                throw new IllegalArgumentException(liczba + " jest poza zakresem " +
                        Stale.MIN_LOSOWANY_NUMER + "-" + Stale.MAX_LOSOWANY_NUMER);
            }
        }
        this.liczby = new HashSet<Integer>(liczby);
    }

    // tworzy losowy zaklad
    public static Zaklad stworzChybilTrafil() {
        return new Zaklad(losujSzostke());
    }

    // co prawda nazwa nie jest uniwersalna (odwoluje sie do 6, a teoretycznie
    // ta liczba moze sie zmienic), ale nie mialem pomyslu na inna czytelna nazwe
    public static Set<Integer> losujSzostke() {
        Set<Integer> losowaSzostka = new HashSet<>();
        while (losowaSzostka.size() < Stale.SZESC_LICZB_W_ZAKLADZIE) {
            losowaSzostka.add(Zaklad.losujLiczbe());
        }
        return losowaSzostka;
    }

    private static int losujLiczbe(){
        // dodaje jedynke, zeby nie moglo wyjsc 0
        return ThreadLocalRandom.current().nextInt(Stale.MAX_LOSOWANY_NUMER) + Stale.MIN_LOSOWANY_NUMER;
    }

    public Set<Integer> dajLiczby() {
        return liczby;
    }

    /*
    public int ileTrafien() {

    }
    */

    // to String ?

}
