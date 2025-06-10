package totolotek.gracz;

import totolotek.kolektura.Blankiet;
import totolotek.kolektura.Kolektura;
import totolotek.kolektura.PoleBlankietu;
import totolotek.system.Centrala;
import totolotek.util.Stale;

import java.util.*;


/*
"Gracz ten kupuje nowy kupon dopiero wtedy, gdy przeprowadzone zostaną
wszystkie losowania obstawione w poprzednim kuponie. "
Czyli tak naprawde nigdy nie powinien kupowac kuponu ;))
bo warunek jest paradoksalny. Ale przyjme, ze pierwszy kupon kupuje przy pierwszej
mozliwej okazji.
 */

public class Staloliczbowy extends Staly{
    public static final int LICZBA_LOSOWAN = 10;
    private Set<Integer> ulubioneLiczby;

    public Staloliczbowy(String imie, String nazwisko, String pesel, long poczatkoweSrodki,
                         List<Kolektura> ulubioneKolektury, Set<Integer> ulubioneLiczby)
            throws IllegalArgumentException {
        super(imie, nazwisko, pesel, poczatkoweSrodki, ulubioneKolektury);
        if (ulubioneLiczby.size() != Stale.SZESC_LICZB_W_ZAKLADZIE) {
            throw new IllegalArgumentException("Podano niepoprawna liczbe ulubionych liczb");
        }
        this.ulubioneLiczby = new HashSet<>(ulubioneLiczby);
    }

    //Gracz ten kupuje nowy kupon dopiero wtedy, gdy przeprowadzone zostaną wszystkie
    // losowania obstawione w poprzednim kuponie.
    public void wykonajTure(Centrala centrala) {
        // sprawdzamy, czy wszystkie losowania ostatniego kuponu sie odbyly
        int numerOstaniegoLosowania = centrala.dajNumerNastepnegoLosowania() - 1;
        // jesli nie ma zadnych kuponow, to kupujemy - vide komenatrz nad deklaracja tej klasy
        if (dajKupony().isEmpty() || dajKupony().get(dajKupony().size() - 1).dajNumerOstatniegoLosowania() <=
                numerOstaniegoLosowania) {
            Map<Integer, PoleBlankietu> mapa = new HashMap<>();
            mapa.put(1, new PoleBlankietu(ulubioneLiczby, false));
            Blankiet blankiet = new Blankiet(mapa, Set.of(LICZBA_LOSOWAN));
            wybierzNastepnaKolekture().sprzedajKupon(blankiet, this);
        }
    }

}
