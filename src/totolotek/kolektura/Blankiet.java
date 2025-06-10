package totolotek.kolektura;

import totolotek.system.Kupon;
import totolotek.util.Stale;
import totolotek.domain.Zaklad;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
W konstruktorze od razu dokonuje interpretacji liczby losowan na podstawie zaznaczonych liczb,
tak wiec nie przetrzymuje tych liczb w klasie.
 */
public class Blankiet {
    private int liczbaLosowan;
    private Map<Integer, PoleBlankietu> pola;

    public static final int LICZBA_POL_BLANKIET = 8;

    public Blankiet(Map<Integer, PoleBlankietu> pola, Set<Integer> zaznaczoneLiczbyLosowan) throws IllegalArgumentException {
        this.pola = new HashMap<>(pola);
        this.liczbaLosowan = Blankiet.obliczLiczbeLosowan(zaznaczoneLiczbyLosowan);
    }

    private static int obliczLiczbeLosowan(Set<Integer> zaznaczoneLiczbyLosowan) throws IllegalArgumentException {
        if (zaznaczoneLiczbyLosowan == null || zaznaczoneLiczbyLosowan.isEmpty()) { return Stale.DOMYSLNA_LICZBA_LOSOWAN_BLANKIET;}
        if (zaznaczoneLiczbyLosowan.size() > Stale.MAX_LOSOWAN_BLANKIET) {
            throw new IllegalArgumentException("Zaznaczono wiecej liczb w rubryce liczba losowan (" +
                      zaznaczoneLiczbyLosowan.size() + ") niz ich jest na blankiecie");
        }
        return Collections.max(zaznaczoneLiczbyLosowan);
    }

    public int dajLiczbeLosowan() {return liczbaLosowan;}
    public List<Zaklad> dajPoprawneNieanulowaneZaklady() {
        List<Zaklad> lista = new ArrayList<Zaklad>();
        for (PoleBlankietu pole : pola.values()) {
            if (pole.czyDobry()) {
                lista.add(new Zaklad(pole.dajSkresloneLiczby()));
            }
        }
        return lista;
    }

    // tworzy blankiet z jednym dzialajacym (nieanulowanym i poprawnym) zakladem
    // na losowa liczbe losowan. Jest to zgodne z dyskuja na forum na ten temat
    public static Blankiet stworzLosowyPoprawny() {
        PoleBlankietu pole = new PoleBlankietu(Zaklad.losujSzostke(), false);
        Map<Integer, PoleBlankietu> mapa = new HashMap<>();
        mapa.put(1,pole);
        int liczbaLosowan = ThreadLocalRandom.current().nextInt(Kupon.MAX_LICZBA_LOSOWAN) + 1;
        return new Blankiet(mapa, Set.of(liczbaLosowan));
    }
}
