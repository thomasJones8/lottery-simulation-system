package totolotek.system;

import totolotek.domain.Losowanie;
import totolotek.domain.StopienNagrody;
import totolotek.domain.WynikStopnia;
import totolotek.domain.Zaklad;
import totolotek.kolektura.Kolektura;

import java.nio.file.StandardOpenOption;
import java.util.*;

import static java.lang.Math.max;
import static totolotek.domain.StopienNagrody.I_STOPIEN;




/*
mam enum stopien nagrody, record wynik stopnia i mape ktora wiaze jedno z drugim?

*/

// pamietaj o nadpisaniu hashcode itd
public class Centrala {

    public static final int PROCENT_NA_NAGRODY = 51;
    public static final long NAGRODA_4_STOPNIA = 2400L;
    public static final long MIN_PULA_1_STOPIEN = 200000000L;

    public static final long MIN_NAGRODA_3_STOPIEN = Zaklad.CENA_NETTO * 36;
    public static final int PROCENT_NA_1_STOPIEN = 44;

    public static final int PROCENT_NA_2_STOPIEN = 8;

    private int numerNastepnegoLosowania;
    private int numerNastepnegoKuponu;
    private int numerNastepnejKolektury;
    private long budzet;

    private final List<Kolektura> listaKolektur;
    private final Map<Integer, Losowanie> losowania;

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
        Losowanie losowanie = new Losowanie(numerLosowania, zwycieskieLiczby, wyniki);
        // zapisz do historii losowan
        losowania.put(losowanie.dajNumer(), losowanie);
    }

    private Map<StopienNagrody, WynikStopnia> wyznaczNagrody(int numerLosowania, EnumMap<StopienNagrody,
            Integer> trafienia, long calkowitaPula) {
        Map<StopienNagrody, WynikStopnia> wyniki = new EnumMap<>(StopienNagrody.class);
        //najpierw 4 stopien
        //long pula4Stopien = trafienia.getOrDefault(StopienNagrody.4_STOPIEN, 0) * NAGRODA_4_STOPNIA;
        wyniki.put(StopienNagrody.IV_STOPIEN, obliczWynik4Stopnia(trafienia.getOrDefault(StopienNagrody.IV_STOPIEN, 0)));
        wyniki.put(I_STOPIEN, obliczWynikIStopnia(calkowitaPula, trafienia.getOrDefault(I_STOPIEN, 0)));
        wyniki.put(StopienNagrody.II_STOPIEN, obliczWynikIIStopnia(calkowitaPula,
                trafienia.getOrDefault(StopienNagrody.II_STOPIEN, 0)));
        // od calkowitej puli odejmij pule pierwszych 3 nagrod
        long resztaPuli = calkowitaPula - wyniki.values().stream().mapToLong(WynikStopnia::lacznaPulaNagrod).sum();
        wyniki.put(StopienNagrody.III_STOPIEN, obliczWynikIIIStopnia(resztaPuli,
                trafienia.getOrDefault(StopienNagrody.III_STOPIEN, 0)));
        // dopiero teraz uwzgledniamy minimum puli 1 stopnia, aby nie uszczuplilo puli 3 stopnia
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

    // TRZEBA JESZCZE UWZGLEDNIC KUMULACJE!
    private static WynikStopnia obliczWynikIStopnia(long calkowitaPula, int liczbaTrafien) {
        long mojaPula = (calkowitaPula * PROCENT_NA_1_STOPIEN)/ 100;
        // zabezpieczenie przed dzieleniem przez 0
        return new WynikStopnia((liczbaTrafien > 0) ? mojaPula/ liczbaTrafien : 0,
                liczbaTrafien, mojaPula);
    }

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
