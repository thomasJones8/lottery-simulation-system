package totolotek.system;

import totolotek.domain.Losowanie;
import totolotek.domain.StopienNagrody;
import totolotek.domain.WynikStopnia;
import totolotek.domain.Zaklad;
import totolotek.kolektura.Kolektura;

import java.util.*;

import static java.lang.Math.max;
import static totolotek.domain.StopienNagrody.I_STOPIEN;




/*
mam enum stopien nagrody, record wynik stopnia i mape ktora wiaze jedno z drugim?

*/

public class Centrala {

    public static final int PROCENT_NA_NAGRODY = 51;
    public static final long NAGRODA_4_STOPNIA = 2400L;
    public static final long MIN_PULA_1_STOPIEN = 200000000L;

    public static final long MIN_NAGRODA_3_STOPIEN = Zaklad.CENA_NETTO * 15;
    public static final int PROCENT_NA_1_STOPIEN = 44;

    public static final int PROCENT_NA_2_STOPIEN = 8;

    private int numerNastepnegoLosowania;
    private int numerNastepnegoKuponu;
    private int numerNastepnejKolektury;
    private long budzet;
    private long kumulacja;

    private final Map<Integer, Kolektura> listaKolektur;
    private final Map<Integer, Losowanie> losowania;

    public Centrala(long budzetPoczatkowy) {
        budzet = budzetPoczatkowy;
        // myslalem, zeby stworzyc na to stala, ale to chyba przesada ;))
        numerNastepnegoLosowania = 1;
        numerNastepnegoKuponu = 1;
        numerNastepnejKolektury = 1;
        listaKolektur = new HashMap<>();
        losowania = new HashMap<>();
        kumulacja = 0;
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
        Map<StopienNagrody, WynikStopnia> wyniki = wyznaczNagrody(trafienia, calkowitaPula);

        // stworz obiekt losowanie
        Losowanie losowanie = new Losowanie(numerLosowania, zwycieskieLiczby, wyniki);
        // zapisz do historii losowan
        losowania.put(losowanie.dajNumer(), losowanie);
    }

    // miala byc prywatna, ale musialem ja upublicznic dla testu
    public Map<StopienNagrody, WynikStopnia> wyznaczNagrody(EnumMap<StopienNagrody,
            Integer> trafienia, long calkowitaPula) {
        Map<StopienNagrody, WynikStopnia> wyniki = new EnumMap<>(StopienNagrody.class);
        // ustalam wyniki : IV, I i II stopnia
        wyniki.put(StopienNagrody.IV_STOPIEN, obliczWynik4Stopnia(trafienia.getOrDefault(StopienNagrody.IV_STOPIEN, 0)));
        wyniki.put(StopienNagrody.I_STOPIEN, obliczWynikIStopniaBezKumulacji(calkowitaPula, trafienia.getOrDefault(I_STOPIEN, 0), this));
        wyniki.put(StopienNagrody.II_STOPIEN, obliczWynikIIStopnia(calkowitaPula,
                trafienia.getOrDefault(StopienNagrody.II_STOPIEN, 0)));
        // od calkowitej puli odejmuje pozostale nagrody, aby obliczyc wynik III stopnia
        long resztaPuli = calkowitaPula - wyniki.values().stream().mapToLong(WynikStopnia::lacznaPulaNagrod).sum();
        wyniki.put(StopienNagrody.III_STOPIEN, obliczWynikIIIStopnia(resztaPuli,
                trafienia.getOrDefault(StopienNagrody.III_STOPIEN, 0)));
        // dopiero teraz uwzgledniamy minimum puli 1 stopnia, aby nie uszczuplilo puli III stopnia
        // nie jest to dokladnie opisane w specyfikacji, ale zakladam, ze logiczne jest, ze
        // kumulacje uwzgledniamy przed uwzglednieniem minimum 2mln
        wyniki.put(StopienNagrody.I_STOPIEN, obliczWynikIStopniaZKumulacja(calkowitaPula,
                trafienia.getOrDefault(I_STOPIEN, 0), this));
        WynikStopnia staryWynik1Stopien = wyniki.get(I_STOPIEN);
        WynikStopnia nowyWynik1Stopien;
        if (staryWynik1Stopien.lacznaPulaNagrod() < MIN_PULA_1_STOPIEN) {
            if (staryWynik1Stopien.liczbaZwycieskichZakladow() != 0) {
                // to moj zyciowy rekord jesli chodzi o dlugosc linijki kodu
                nowyWynik1Stopien = new WynikStopnia(MIN_PULA_1_STOPIEN /
                        staryWynik1Stopien.liczbaZwycieskichZakladow(),staryWynik1Stopien.liczbaZwycieskichZakladow(),
                        MIN_PULA_1_STOPIEN);
            }
            // nie ma wygranych - chcemy uniknac dzielenia przez 0
            else  {
                nowyWynik1Stopien = new WynikStopnia(0, staryWynik1Stopien.liczbaZwycieskichZakladow(),
                        MIN_PULA_1_STOPIEN);
            }

            wyniki.put(I_STOPIEN, nowyWynik1Stopien);
        }



        return wyniki;
    }

    private static WynikStopnia obliczWynikIStopniaBezKumulacji(long calkowitaPula, int liczbaTrafien,
                                                                Centrala centrala) {
        long mojaPula = (calkowitaPula * PROCENT_NA_1_STOPIEN) / 100;
        return new WynikStopnia((liczbaTrafien > 0) ? mojaPula/ liczbaTrafien : 0,
                liczbaTrafien, mojaPula);
    }
    private static WynikStopnia obliczWynikIStopniaZKumulacja(long calkowitaPula, int liczbaTrafien, Centrala centrala) {
        long mojaPula = (calkowitaPula * PROCENT_NA_1_STOPIEN)/ 100 + centrala.dajKumulacje();
        // zabezpieczenie przed dzieleniem przez 0
        // jak ktos trafil to zeruj kumulacje
        if (liczbaTrafien > 0) {
            centrala.ustawKumulacje(0);
            return new WynikStopnia((mojaPula/ liczbaTrafien), liczbaTrafien, mojaPula);
        }
        //jak nikt to ustaw na aktualna pule (ktora juz ma wliczone stare kumulacje)
        else {
            centrala.ustawKumulacje(mojaPula);
            return new WynikStopnia(0, liczbaTrafien, mojaPula);
        }
    }
    // uwzglednia kumulacje

    private static WynikStopnia obliczWynikIIStopnia(long calkowitaPula, int liczbaTrafien) {
        long mojaPula = (calkowitaPula * PROCENT_NA_2_STOPIEN) / 100;
        return new WynikStopnia((liczbaTrafien > 0) ? mojaPula/ liczbaTrafien : 0,
                liczbaTrafien, mojaPula);
    }

    private static WynikStopnia obliczWynikIIIStopnia(long resztaPuli, int liczbaTrafien) {
        long nagroda = max(MIN_NAGRODA_3_STOPIEN, (liczbaTrafien > 0) ? resztaPuli/ liczbaTrafien : 0);
        return new WynikStopnia(nagroda, liczbaTrafien, nagroda * liczbaTrafien);
    }
    private static WynikStopnia obliczWynik4Stopnia(int liczbaTrafien) {
         return new WynikStopnia(NAGRODA_4_STOPNIA, liczbaTrafien, NAGRODA_4_STOPNIA * liczbaTrafien);
    }

    private EnumMap<StopienNagrody, Integer> policzTrafieniaKazdegoStopnia(int numerLosowania,
                                                                           Set<Integer> zwycieskieLiczby) {
        EnumMap<StopienNagrody, Integer> trafienia = new EnumMap<>(StopienNagrody.class);


        for (Kolektura kolektura: listaKolektur.values()) {
            // petla po wszystkich kuponach sprzedanych przez kolekture
            for (Kupon kupon : kolektura.dajSprzedaneKupony().values()) {
                // czy ten kupon obejmuje to losowanie && niewyplacony
                if (!kupon.sprawdzCzyZrealizowany() && kupon.czyObejmujeLosowanie(numerLosowania)) {
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

    public void zaplac(long kwota) {
        if (kwota > dajBudzet()) {
            // pobierz subwencje
            long kwotaSubwencji = kwota - dajBudzet();
            pobierzKwote(kwotaSubwencji);
            BudzetPanstwa.dajInstancje().przekazSubwencje(kwotaSubwencji);
        }
        budzet -= kwota;
    }

    // przechodzi po wszystkich kolekturach, po ich kuponach, sprawdza ktore
    // liczyly sie na to losowanie i dodaje ich ceny NETTO do puli
    private long obliczPuleNaNagrody(int numerLosowania){
        long wynik = 0;

        for (Kolektura kolektura: listaKolektur.values()) {
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
        listaKolektur.put(kolektura.dajId(), kolektura);
        return kolektura;
    }
    public Kupon stworzKupon(int liczbaLosowan, int idKolektury, List<Zaklad> listaZakladow,
                             long cenaBrutto){
        return new Kupon(liczbaLosowan, idKolektury, numerNastepnegoKuponu++, listaZakladow,
        cenaBrutto, numerNastepnegoLosowania);
    }

    public List<Kolektura> dajListeKolektur() { return new ArrayList<>(listaKolektur.values());}
    public int dajNumerNastepnegoLosowania() { return numerNastepnegoLosowania;}
    public long dajBudzet() { return budzet;}
    public void pobierzKwote(long kwota) {
        budzet += kwota;
    }
    public long dajKumulacje() {return kumulacja;}
    public void ustawKumulacje(long kwota) {kumulacja = kwota;}
    // zwrocone Losowanie bedzie niemutowalne - sama klasa losowanie o to dba
    public Losowanie dajLosowanie(int numerLosowania) {return losowania.get(numerLosowania);}
    public Kolektura dajKolekture(int idKolektury) {return listaKolektur.get(idKolektury);}

    public String wypiszWynikiWszystkichLosowan() {
        StringBuilder s = new StringBuilder();
        for (Losowanie losowanie : losowania.values()) {
            for (WynikStopnia wynik : losowanie.dajWyniki().values()) {
                // POTENCJALNIE ZMIENIC - TAK, BY NIE OMIJAL NIETRAFIONYCH
                if (wynik.liczbaZwycieskichZakladow() != 0) {
                    s.append(wynik.toString());
                    s.append("\n");
                }
            }
            s.append("\n");
        }

        return s.toString();
    }
}
