package totolotek.kolektura;

import totolotek.util.Stale;

import java.util.HashSet;
import java.util.Set;

/*
Pojawia sie tu drobna duplikacja kodu z klasy Zaklad ale wydaje mi sie ona:
1) na tyle drobna, ze nie warto zwracac na to uwagi
2) proby unikniecia raczej zle by sie odbily na czytelnosci kodu i tylko by go skomplikowaly
 */
public class PoleBlankietu {
    private Set<Integer> skresloneLiczby;
    private boolean czyAnulowany;

    public PoleBlankietu(Set<Integer> skresloneLiczby, boolean czyAnulowany) throws IllegalArgumentException {
        for (Integer liczba: skresloneLiczby) {
            if (liczba < Stale.MIN_LOSOWANY_NUMER  || Stale.MAX_LOSOWANY_NUMER < liczba) {
                throw new IllegalArgumentException(liczba + " jest poza zakresem " +
                        Stale.MIN_LOSOWANY_NUMER + "-" + Stale.MAX_LOSOWANY_NUMER);
            }
        }

        this.skresloneLiczby = new HashSet<>(skresloneLiczby);
        this.czyAnulowany = czyAnulowany;
    }

    // dobry = nieanulowany && poprawnie wypelniony. chcialem w ten sposob skrocic nazwe
    public boolean czyDobry() {
        return !czyAnulowany && skresloneLiczby.size() == Stale.SZESC_LICZB_W_ZAKLADZIE;
    }

    public Set<Integer> dajSkresloneLiczby() {return skresloneLiczby;}
}
