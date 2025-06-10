package totolotek.gracz;

import totolotek.kolektura.Kolektura;

import java.util.ArrayList;
import java.util.List;

public abstract class Staly extends Gracz{

    // specyfikacja nic nie mowi, o nie zmienianiu tej listy, a wiec nie ustawiam jej na final
    public static final int MIN_ULUBIONYCH_KOLEKTUR = 1;
    private List<Kolektura> ulubioneKolektury;
    // przechowuje indeks kolektury, z ktorej bedzie najblizszy zakup
    private int indeksNastepnejKolektury;

    public Staly(String imie, String nazwisko, String pesel, long poczatkoweSrodki,
                 List<Kolektura> ulubioneKolektury) throws IllegalArgumentException  {
        super(imie, nazwisko, pesel, poczatkoweSrodki);
        if (ulubioneKolektury == null || ulubioneKolektury.size() < MIN_ULUBIONYCH_KOLEKTUR) {
            throw new IllegalArgumentException("Podano zbyt mala liczbe ulubionych kolektur");
        }
        this.ulubioneKolektury = new ArrayList<>(ulubioneKolektury);
        this.indeksNastepnejKolektury = 0;

    }

    protected Kolektura wybierzNastepnaKolekture() {
        Kolektura wynik = ulubioneKolektury.get(indeksNastepnejKolektury);
        indeksNastepnejKolektury = (indeksNastepnejKolektury + 1) % ulubioneKolektury.size();
        return wynik;
    }

}
