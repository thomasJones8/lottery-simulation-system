package totolotek.gracz;

import totolotek.kolektura.Blankiet;
import totolotek.kolektura.Kolektura;

import java.util.List;

public class Staloblankietowy extends Staly{
    private int coIleLosowan;
    private Blankiet blankiet;

    public Staloblankietowy(String imie, String nazwisko, String pesel, long poczatkoweSrodki,
                         List<Kolektura> ulubioneKolektury, Blankiet blankiet) {
        super(imie, nazwisko, pesel, poczatkoweSrodki, ulubioneKolektury);
        this.blankiet = blankiet;
    }
}
