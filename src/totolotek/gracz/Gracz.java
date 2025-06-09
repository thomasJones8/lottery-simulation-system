package totolotek.gracz;

import totolotek.system.Kupon;

import java.util.ArrayList;
import java.util.List;
public abstract class Gracz {

    // rozwazalem uczynienie imienia i nazwiska final, ale, choc rzadko, to mozna je przeciez zmienic...
    private String imie;
    private String nazwisko;
    private final String pesel;
    private long portfel;

    private List<Kupon> kupony;

    public Gracz(String imie, String nazwisko, String pesel, long poczatkoweSrodki) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.portfel = poczatkoweSrodki;
        this.kupony = new ArrayList<>();
    }
    public long dajPortfel(){ return portfel;}
    public void zaplac(long kwota) { portfel -= kwota;}

    public boolean czyStac(long kwota) {
        return portfel >= kwota;
    }

    public void wezKupon(Kupon kupon) { kupony.add(kupon);}

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(imie + " " + nazwisko + " " + pesel + "\n");
        s.append("Posiadane srodki: " + portfel + "\n");
        if (kupony.isEmpty()) {
            s.append("Brak kuponow");
        }
        else {
            s.append("Identyfikatory posiadanych kuponow:" + "\n");
            for (Kupon kupon : kupony) {
                s.append(kupon.dajId() + "\n");
            }
        }

        return s.toString();
    }
}
