package totolotek.gracz;

import totolotek.kolektura.Kolektura;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static totolotek.system.Kupon.*;

/*
"Gracz losowy w losowo wybranej kolekturze kupuje losową liczbę kuponów chybił-trafił (od 1 do 100 szt.);
każdy kupon z losową liczbą zakładów i na losową liczbę losowań (w ramach ograniczeń kuponu).
Gracz tego rodzaju dysponuje losowo wybraną początkową ilością środków (mniej od miliona zł)."

Nie jest jasno sprecyzowane, czy te liczby (poza srodkami) sa losowane raz czy kazdorazowo,
przyjmuje druga wersje.
 */
public class Losowy extends Gracz{
    public static final long MAX_POCZATKOWE_SRODKI = 99_999_999L;
    public static final int MAX_KUPOWANYCH_KUPONOW = 100;
    public static final int MIN_KUPOWANYCH_KUPONOW = 1;
    public Losowy(String imie, String nazwisko, String pesel) {
        super(imie, nazwisko, pesel, obliczPoczatkoweSrodki());
    }

    private static long obliczPoczatkoweSrodki(){
        return ThreadLocalRandom.current().nextLong(MAX_POCZATKOWE_SRODKI);
    }

    public void kupKupon(List<Kolektura> listaKolektur) {

        int ileKuponow = ThreadLocalRandom.current().nextInt(MIN_KUPOWANYCH_KUPONOW, MAX_KUPOWANYCH_KUPONOW) ;
        // indeks do wylosowania kolektury
        int i = ThreadLocalRandom.current().nextInt(listaKolektur.size());
        Kolektura kolektura = listaKolektur.get(i);

        /*
        Zgodnie z dyskusja na forum przyjalem interpretacje, ze
        "Liczbę kuponów, które w danym momencie chce kupić gracz losowy,
        traktujemy jako liczbę prób kupienia kuponu."
         */
        for (int j = 0; j < ileKuponow; j++) {
            int ileZakladow = ThreadLocalRandom.current().nextInt(MIN_LICZBA_ZAKLADOW, MAX_LICZBA_ZAKLADOW);
            int ileLosowan = ThreadLocalRandom.current().nextInt(MIN_LICZBA_LOSOWAN, MAX_LICZBA_LOSOWAN);
            kolektura.sprzedajKupon(ileZakladow, ileLosowan, this);
        }
    }

}
