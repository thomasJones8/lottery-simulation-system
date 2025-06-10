package totolotek.gracz;

import totolotek.system.Centrala;
import totolotek.kolektura.Kolektura;

public class Minimalista extends Gracz {

    // zakladam, ze to caly czas jest jedna i ta sama kolektura
    private final Kolektura ulubionaKolektura;
    private static final int ileZakladow = 1;
    private static final int ileLosowan = 1;

    public Minimalista(String imie, String nazwisko, String pesel, long poczatkoweSrodki, Kolektura kolektura) {
        super(imie, nazwisko, pesel, poczatkoweSrodki);
        this.ulubionaKolektura = kolektura;
    }

    // minimalista nie potrzebuje centrali, ale zeby symulacja ladniej wyszla,
    // chce miec takie same argumenty metody dla kazdego gracza
    public void wykonajTure(Centrala centrala) {
        ulubionaKolektura.sprzedajKupon(ileZakladow, ileLosowan, this);
    }

}
