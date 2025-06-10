package totolotek.app;


import totolotek.domain.Zaklad;
import totolotek.gracz.*;
import totolotek.kolektura.Blankiet;
import totolotek.system.Centrala;
import totolotek.kolektura.Kolektura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// CHYBA POWINNO BYC W INNYM KATALOGU
public class Main {
    public static void main(String[] args) {

        //     Utworzyć centralę Totolotka i 10 kolektur.
        Centrala centrala = new Centrala(0);
        ArrayList<Kolektura> listaKolektur = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listaKolektur.add(centrala.stworzKolekture());
        }


        //Utworzyć po 200 graczy każdego rodzaju, przydzielając graczy
        // mniej więcej po równo między wszystkie kolektury (dot. tych graczy dla których jest to możliwe).
        List<Gracz> wszyscyGracze = new ArrayList<>();
        Integer pesel = 0;
        for (int i = 0; i < 200; i++) {
            wszyscyGracze.add(new Minimalista("a", "b", pesel.toString(),
                    10000L,  listaKolektur.get(i % listaKolektur.size())));
            pesel++;
        }

        for (int i = 0; i < 200; i++) {
            wszyscyGracze.add(new Losowy("a", "b", pesel.toString()));
            pesel++;
        }

        for (int i = 0; i < 200; i++) {
            wszyscyGracze.add(new Staloliczbowy("a", "b", pesel.toString(),
                    10000L, wylosujPodzbior(listaKolektur) , Zaklad.losujSzostke()));
            pesel++;
        }

        for (int i = 0; i < 200; i++) {
            wszyscyGracze.add(new Staloblankietowy("a", "b", pesel.toString(),
                    10000L, wylosujPodzbior(listaKolektur), Blankiet.stworzLosowyPoprawny());
            pesel++;
        }

        // Przeprowadzić 20 losowań, poprzedzając każde z nich kupowaniem kuponów przez graczy.
        // Po każdym losowaniu każdy gracz powinien sprawdzić, czy któryś z jego kuponów ma już wykonane wszystkie losowania. Jeśli tak, i jeśli ten kupon coś wygrał, gracz powinien odebrać wygraną.
        // Wypisać na koniec:
        //     pełną informację z centrali o przeprowadzonych losowaniach (p. sekcja Centrala Totolotka);
        //    dotychczasową wielkość wpływów do budżetu państwa;
        //    dotychczasową kwotę subwencji pobranej przez centralę z budżetu.
    }


    private static <T> List<T> wylosujPodzbior(List<T> oryginalnaLista) {
        if (oryginalnaLista == null || oryginalnaLista.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> kopia = new ArrayList<>(oryginalnaLista);
        Collections.shuffle(kopia);
        int ileWziac = ThreadLocalRandom.current().nextInt(1, kopia.size() + 1);
        return kopia.subList(0, ileWziac);
    }
}