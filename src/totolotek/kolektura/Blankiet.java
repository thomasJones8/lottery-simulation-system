package totolotek.kolektura;

import totolotek.util.Stale;
import totolotek.domain.Zaklad;

import java.util.*;

/*
W konstruktorze od razu dokonuje interpretacji liczby losowan na podstawie zaznaczonych liczb,
tak wiec nie przetrzymuje tych liczb w klasie.
 */
public class Blankiet {
    private int liczbaLosowan;
    private Map<Integer, PoleBlankietu> pola;

    public static final int LICZBA_POL_BLANKIET = 8;

    // czy na pewno publiczny? chyba tak, bo gracz tworzy
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
        for (int i = 1; i <= pola.size(); i++) {
            PoleBlankietu pole = pola.get(i);
            if (pole.czyDobry()) {
                lista.add(new Zaklad(pole.dajSkresloneLiczby()));
            }
        }
        return lista;
    }
}
