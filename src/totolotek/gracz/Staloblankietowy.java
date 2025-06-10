package totolotek.gracz;

import totolotek.kolektura.Blankiet;
import totolotek.kolektura.Kolektura;
import totolotek.kolektura.PoleBlankietu;
import totolotek.system.Centrala;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Staloblankietowy extends Staly{
    private int coIleLosowan;
    private int numerMojegoOstatniegoLosowania;
    private Blankiet blankiet;

    public Staloblankietowy(String imie, String nazwisko, String pesel, long poczatkoweSrodki,
                         List<Kolektura> ulubioneKolektury, Blankiet blankiet) {
        super(imie, nazwisko, pesel, poczatkoweSrodki, ulubioneKolektury);
        this.blankiet = blankiet;
        numerMojegoOstatniegoLosowania = 0;
    }

    //Gracz stałoblankietowy ma swój blankiet i kupuje kupon
    //zawsze w oparciu o ten blankiet, przy czym robi to co pewną stałą - sobie znaną - liczbę losowań

    @Override
    public void wykonajTure(Centrala centrala) {
        // pierwsze losowanie lub minal interwal
        int numerOstaniegoLosowania = centrala.dajNumerNastepnegoLosowania() - 1;
        if (numerMojegoOstatniegoLosowania == 0 ||
            numerMojegoOstatniegoLosowania + coIleLosowan == numerOstaniegoLosowania) {
            numerMojegoOstatniegoLosowania = numerOstaniegoLosowania;
            wybierzNastepnaKolekture().sprzedajKupon(blankiet, this);
        }
    }
}
