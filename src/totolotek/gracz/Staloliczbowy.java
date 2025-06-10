package totolotek.gracz;

import totolotek.kolektura.Kolektura;
import totolotek.system.Centrala;
import totolotek.util.Stale;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    }

}
