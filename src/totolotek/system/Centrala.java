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
        HashSet<Integer> zwycieskieLiczby = HashSet<Integer>(Zaklad.losujSzostke());
        int numerLosowania = dajNumerNastepnegoLosowania();
        numerNastepnegoLosowania++;
        // policz trafienia dla kazdego stopnia
        policzTrafieniaKazdegoStopnia();
        // oblicz calkowita pule na nagrody
        // wyznacz kwoty nagrod kazdego stopnia
        // stworz obiekt losowanie
        // zapisz do historii losowan



        // tworzenie i zapisanie obiektu
        //Losowanie losowanie = new Losowanie(dajNumerNastepnegoLosowania(), Zaklad.losujSzostke());
        losowania.put(losowanie.dajNumer(), losowanie);

        long pula = obliczPuleNaNagrody(losowanie.dajNumer());

        // jak to 44% na nagrode I stopnia jest mniejsze niz 2 mln, to czy dobieram z budzetu, czy z centrali?
    }

    private


    // przechodzi po wszystkich kolekturach, po ich kuponach, sprawdza ktore
    // liczyly sie na to losowanie i dodaje ich ceny NETTO do puli
    private long obliczPuleNaNagrody(int numerLosowania){
        long wynik = 0;

        for (Kolektura kolektura: listaKolektur) {
            // petla po wszystkich kuponach sprzedanych przez kolekture
            for (Kupon kupon : kolektura.dajSprzedaneKupony().values()) {
                // czy ten kupon obejmuje to losowania
                if (kupon.czyObejmujeLosowanie(numerLosowania)) {
                    wynik += kupon.dajLiczbeZakladow() * Zaklad.CENA_NETTO;
                }
            }
        }
        // pamietaj ze ceny netto!
        return (wynik * PROCENT_NA_NAGRODY) / 100;
    }



    private Map<StopienNagrody, WynikStopnia> wyznaczNagrody(int numerLosowania) {
        Map<StopienNagrody, WynikStopnia> wyniki = new EnumMap<>();
        Map<StopienNagrody, Integer> liczbyTrafien = new HashMap<>();
        // chyba najpierw ile trafien


        return wyniki;
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
