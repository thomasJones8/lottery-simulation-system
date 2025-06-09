package totolotek.system;

import totolotek.util.Stale;
import totolotek.domain.Zaklad;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


// ZMIENIC NUMER INDEKSOWY NA LONG?
public class Kupon {
    private boolean czyWyplacony;
    private final int idKolektury;
    // musi byc unikatowy w skali systemu!!
    private final int numerPorzadkowy;
    private final String id;
    // maks bodajze 8 !!
    private final List<Zaklad> zaklady;

    private final long cenaBrutto;
    private final long podatek;
    // jakos info ile losowan i w ktorych, i ile zakladow?

    private final int numerPierwszegoLosowania;
    private final int liczbaLosowan;
    public static final int MIN_LICZBA_ZAKLADOW= 1;
    public static final int MAX_LICZBA_ZAKLADOW = 8;

    public static final int MIN_LICZBA_LOSOWAN= 1;
    public static final int MAX_LICZBA_LOSOWAN = 10;


    /* konstruktor pakietowy. Przyjmuje, ze nie mozna kupic kuponu na 0 losowan.
        Byc moze to kontrowersyjne, ze podaje cene konstruktorowi, ale to dlatego,
        ze jak zanim stworze kupony w kolekturze, to najpierw i tak musze obliczyc ich cene,
        zeby sprawdzic, czy gracza na nie stac.
    */

    // NIE WIEM, CZY POWINIEN BYC PUBLICZNY, BYC MOZE ZMIENIC PAKIET
    Kupon(int liczbaLosowan,int idKolektury, int numerPorzadkowy, List<Zaklad> listaZakladow,
          long cenaBrutto, int numerPierwszegoLosowania) throws IllegalArgumentException {
        int liczbaZakladow = listaZakladow.size();

        if (liczbaZakladow < MIN_LICZBA_ZAKLADOW || MAX_LICZBA_ZAKLADOW < liczbaZakladow) {
            throw new IllegalArgumentException("Niepoprawna liczba zakladow: " + liczbaZakladow);
        }
        if (liczbaLosowan < MIN_LICZBA_LOSOWAN || MAX_LICZBA_LOSOWAN < liczbaLosowan) {
            throw new IllegalArgumentException("Niepoprawna liczba losowan: " + liczbaLosowan);
        }

        czyWyplacony = false;
        this.idKolektury = idKolektury;
        this.numerPorzadkowy = numerPorzadkowy;
        id = generujId(numerPorzadkowy, idKolektury);
        this.cenaBrutto = cenaBrutto;
        podatek = Zaklad.PODATEK * liczbaLosowan * liczbaZakladow;
        this.zaklady = new ArrayList<>(listaZakladow);
        this.liczbaLosowan = liczbaLosowan;
        this.numerPierwszegoLosowania = numerPierwszegoLosowania;
    }


    private String generujId(int numerPorzadkowy, int idKolektury) {

        int losowyZnacznik = generujLosowyZnacznik();
        int sumaKontrolna = (sumaCyfr(numerPorzadkowy) + sumaCyfr(idKolektury)
                + sumaCyfr(losowyZnacznik)) % Stale.ILE_MODULO_ZNACZNIK_LOSOWY;
        return numerPorzadkowy + "-" + idKolektury + "-" + String.format("%09d", losowyZnacznik)
        + "-" + String.format("%02d", sumaKontrolna);
    }

    // potrzebne do generowania id
    private static int generujLosowyZnacznik() {
        return ThreadLocalRandom.current().nextInt(Stale.OGRANICZENIE_GORNE_ZNACZNIK_LOSOWY);
    }

    // potrzebne do sumy kontrolnej w id kuponu


    private static int sumaCyfr(int x) {
        // u nas nie bedzie takiej sytuacji, ale wydaje mi sie, ze dobra praktyke jest
        // tworzenie od razu bardziej uniwersalnej metody, gdy jest to tak proste
        // bo pozniej ktos gdzies tego uzyje i nie zauwazy niuansu
        x = Math.abs(x);
        int wynik = 0;
        while (x > 0) {
            wynik += x % 10;
            x /= 10;
        }
        return wynik;
    }

    public boolean sprawdzCzyWyplacony() {return czyWyplacony;}
    public boolean czyObejmujeLosowanie(int numerLosowania) {
        return numerPierwszegoLosowania <= numerLosowania &&
                numerLosowania < numerPierwszegoLosowania + liczbaLosowan;
    }
    public String dajId() { return id;}
    public long dajCene() { return cenaBrutto;}
    public int dajLiczbeLosowan() { return liczbaLosowan;}
    public int dajNumerOstatniegoLosowania() { return numerPierwszegoLosowania + liczbaLosowan - 1 ;}
    public int dajLiczbeZakladow() { return zaklady.size();}

    public List<Zaklad> dajZaklady() { return new ArrayList<>(this.zaklady);}
    public long dajPodatek() { return podatek;}

    // UWAGA !! TO STRING NIEDOKOCZNONY
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("KUPON NR " + id + "\n");
        // petla
        s.append("LICZBA LOSOWAŃ: " + liczbaLosowan + "\n");
        // numery loosowan
        s.append("CENA: " + cenaBrutto + "\n");

        return s.toString();
    }
}
