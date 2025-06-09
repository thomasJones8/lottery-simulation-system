package totolotek.kolektura;

import totolotek.system.BudzetPanstwa;
import totolotek.util.Stale;
import totolotek.domain.Zaklad;
import totolotek.gracz.Gracz;

import java.util.*;

import totolotek.system.Centrala;
import totolotek.system.Kupon;

public class Kolektura {
    private int id;
    private Map<String, Kupon> sprzedaneKupony;
    private Centrala centrala;

    // nie mam budzetu panstwa jako zmiennej, bo bp implementuje zgodnie
    // ze wzorcem Singleton (vide BudzetPanstwa.java)


    public Kolektura(int id, Centrala centrala) {
        this.id = id;
        this.centrala = centrala;
        sprzedaneKupony = new HashMap<>();
    }

    public Map<String, Kupon> dajSprzedaneKupony() {
        // zwraca kopie, bo referencja bylaly niebezpieczna
        return new HashMap<>(sprzedaneKupony);
    }

    public int dajId() { return id;}

    /*
        bez blankietu : sprawdza czy stac gracza, jak go
        stac to generuje zaklady i wywoluje prywatna metode dokoncz sprzedaz
     */
    private static List<Zaklad> generujZakladyChybilTrafil(int ile) {
        ArrayList<Zaklad> lista = new ArrayList<Zaklad>();
        for (int i = 0; i < ile; i++) {
            lista.add(Zaklad.stworzChybilTrafil());
        }
        return lista;
    }

    // sprzedaje kupon chybil-trafil
    public boolean sprzedajKupon(int liczbaZakladow, int liczbaLosowan, Gracz gracz) {
        long cenaKuponuBrutto = Zaklad.CENA_BRUTTO * liczbaZakladow * liczbaLosowan;
        // zachodzi tylko jak gracza stac
        if (gracz.czyStac(cenaKuponuBrutto)) {
            // generujemy kupon
            dokonczSprzedaz(gracz, centrala.stworzKupon(liczbaLosowan, id,
                    generujZakladyChybilTrafil(liczbaZakladow), cenaKuponuBrutto));
            return true;
        }
        return false;
    }

    /*
    z blankietem : spradza, czy stac gracza, jak go stac to odczytuje informacje z blankietu
    i tez wywlouyje metode , te sama, dokoncz sprzedaz
    */
    public boolean sprzedajKupon(Blankiet blankiet, Gracz gracz) {
        List<Zaklad> zaklady = blankiet.dajPoprawneNieanulowaneZaklady();
        int liczbaLosowan = blankiet.dajLiczbeLosowan();
        // do ceny licza sie tylko poprawnie wypelnione zaklady
        long cenaKuponuBrutto = Zaklad.CENA_BRUTTO * zaklady.size() * liczbaLosowan;
        // w przypadku pustego blankietu (0 poprawnych zakladow) nie dochodzi do sprzedazy
        if (gracz.czyStac(cenaKuponuBrutto) && zaklady.size() >= Kupon.MIN_LICZBA_ZAKLADOW) {
            dokonczSprzedaz(gracz, centrala.stworzKupon(liczbaLosowan, id, zaklady, cenaKuponuBrutto));
            return true;
        }
        return false;
    }

    private void dokonczSprzedaz(Gracz gracz, Kupon kupon) {
        long cenaKuponuBrutto = kupon.dajCene();
        long kwotaPodatku = kupon.dajPodatek();
        gracz.zaplac(cenaKuponuBrutto);
        BudzetPanstwa.dajInstancje().pobierzPodatek(kwotaPodatku);
        // przekazanie zysku
        long zysk = cenaKuponuBrutto - kwotaPodatku;
        centrala.pobierzZysk(zysk);
        // dodanie go do sprzedanych
        sprzedaneKupony.put(kupon.dajId(), kupon);
        // przekazanie graczowi
        gracz.wezKupon(kupon);
    }


}

