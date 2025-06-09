package totolotek.system;

import totolotek.domain.Losowanie;
import totolotek.domain.StopienNagrody;
import totolotek.domain.WynikStopnia;
import totolotek.domain.Zaklad;
import totolotek.kolektura.Kolektura;

import java.util.*;




/*
mam enum stopien nagrody, record wynik stopnia i mape ktora wiaze jedno z drugim?

*/

// pamietaj o nadpisaniu hashcode itd
public class Centrala {

    public static final int PROCENT_NA_NAGRODY = 51;
    private int numerNastepnegoLosowania;
    private int numerNastepnegoKuponu;
    private int numerNastepnejKolektury;
    private long budzet;

    private List<Kolektura> listaKolektur;
    private Map<Integer, Losowanie> losowania;

    public Centrala(long budzetPoczatkowy) {
        budzet = budzetPoczatkowy;
        // myslalem, zeby stworzyc na to stala, ale to chyba przesada ;))
        numerNastepnegoLosowania = 1;
        numerNastepnegoKuponu = 1;
        numerNastepnejKolektury = 1;
        listaKolektur = new ArrayList<>();
        losowania = new HashMap<>();
    }

    /*
    "44% (puli) przeznaczane jest na nagrody I stopnia (trafione 6 liczb)"
    ale
    "Pula na nagrody I stopnia jest gwarantowana i wynosi minimum 2 mln. zł
    (nie wpływa to na wielkości pul pozostałych stopni wyliczone wyżej)"

    Nie jest to wporst okreslone, ale z mechanizmu dzialania subwencji wnioskuje,
    ze gdy 44% puli nie wystarczy, dobiera sie z budzetu centrali (z zyskow).
     */
    public void przeprowadzLosowanie() {

        // wylosuj zwycieskie liczby
        Set<Integer> zwycieskieLiczby = new HashSet<Integer>(Zaklad.losujSzostke());
        int numerLosowania = dajNumerNastepnegoLosowania();
        numerNastepnegoLosowania++;
        // policz trafienia dla kazdego stopnia
        EnumMap<StopienNagrody, Integer> trafienia = policzTrafieniaKazdegoStopnia(numerLosowania,
                zwycieskieLiczby);
        // oblicz calkowita pule na nagrody
        long calkowitaPula = obliczPuleNaNagrody(numerLosowania);
        // wyznacz kwoty nagrod kazdego stopnia
        Map<StopienNagrody, WynikStopnia> wyniki = wyznaczNagrody(numerLosowania, trafienia, calkowitaPula);

        // stworz obiekt losowanie
        // zapisz do historii losowan



        // tworzenie i zapisanie obiektu
        //Losowanie losowanie = new Losowanie(dajNumerNastepnegoLosowania(), Zaklad.losujSzostke());
        //losowania.put(losowanie.dajNumer(), losowanie);

        //long pula = obliczPuleNaNagrody(losowanie.dajNumer());

        // jak to 44% na nagrode I stopnia jest mniejsze niz 2 mln, to czy dobieram z budzetu, czy z centrali?
    }

    private Map<StopienNagrody, WynikStopnia> wyznaczNagrody(int numerLosowania, EnumMap<StopienNagrody,
            Integer> trafienia, long calkowitaPula) {
        Map<StopienNagrody, WynikStopnia> wyniki = new EnumMap<>();
        // chyba najpierw ile trafien
        long pula1

        return wyniki;
    }

    private WynikStopnia obliczWynikStopnia(StopienNagrody stopien, long calkowitaPula, EnumMap<StopienNagrody,
            Integer> trafienia) {

    }

    private EnumMap<StopienNagrody, Integer> policzTrafieniaKazdegoStopnia(int numerLosowania,
                                                                           Set<Integer> zwycieskieLiczby) {
        EnumMap<StopienNagrody, Integer> trafienia = new EnumMap<>(StopienNagrody.class);


        for (Kolektura kolektura: listaKolektur) {
            // petla po wszystkich kuponach sprzedanych przez kolekture
            for (Kupon kupon : kolektura.dajSprzedaneKupony().values()) {
                // czy ten kupon obejmuje to losowanie && niewyplacony
                if (!kupon.sprawdzCzyWyplacony() && kupon.czyObejmujeLosowanie(numerLosowania)) {
                    // petla po wszystkich kuponach
                    for (Zaklad zaklad : kupon.dajZaklady()) {
                        StopienNagrody stopien = StopienNagrody.naPodstawieTrafien(zaklad.ileTrafien(zwycieskieLiczby));
                        if (stopien != null) {
                            // jak nie bylo jeszcze trafien, to zwroci 0 + 1 = 1
                            int nowaLiczba = trafienia.getOrDefault(stopien,0) + 1;
                            trafienia.put(stopien, nowaLiczba);
                        }

                    }
                }
            }
        }

        return trafienia;
    }


    // przechodzi po wszystkich kolekturach, po ich kuponach, sprawdza ktore
    // liczyly sie na to losowanie i dodaje ich ceny NETTO do puli
    private long obliczPuleNaNagrody(int numerLosowania){
        long wynik = 0;

        for (Kolektura kolektura: listaKolektur) {
            // petla po wszystkich kuponach sprzedanych przez kolekture
            for (Kupon kupon : kolektura.dajSprzedaneKupony().values()) {
                // czy ten kupon obejmuje to losowanie
                if (kupon.czyObejmujeLosowanie(numerLosowania)) {
                    wynik += kupon.dajLiczbeZakladow() * Zaklad.CENA_NETTO;
                }
            }
        }
        // pamietaj ze ceny netto!
        return (wynik * PROCENT_NA_NAGRODY) / 100;
    }





    public Kolektura stworzKolekture() {
        Kolektura kolektura = new Kolektura(numerNastepnejKolektury++, this);
        listaKolektur.add(kolektura);
        return kolektura;
    }
    public Kupon stworzKupon(int liczbaLosowan, int idKolektury, List<Zaklad> listaZakladow,
                             long cenaBrutto){
        return new Kupon(liczbaLosowan, idKolektury, numerNastepnegoKuponu++, listaZakladow,
        cenaBrutto, numerNastepnegoLosowania);
    }
    public int dajNumerNastepnegoLosowania() { return numerNastepnegoLosowania;}
    public long dajBudzet() { return budzet;}
    public void pobierzZysk(long kwota) {
        budzet += kwota;
    }
}
