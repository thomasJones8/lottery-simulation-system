package totolotek.gracz;

import totolotek.domain.Losowanie;
import totolotek.kolektura.Kolektura;
import totolotek.system.Centrala;
import totolotek.system.Kupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
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
    public List<Kupon> dajKupony(){ return new ArrayList<>(kupony);}
    public void zaplac(long kwota) { portfel -= kwota;}
    public void przyjmijWplate(long kwota) { portfel += kwota;}

    public boolean czyStac(long kwota) {
        return portfel >= kwota;
    }

    public void wezKupon(Kupon kupon) { kupony.add(kupon);}
    public abstract void wykonajTure(Centrala centrala);


    /*
    Po każdym losowaniu każdy gracz powinien sprawdzić, czy któryś* z jego kuponów ma już
    wykonane wszystkie losowania. Jeśli tak, i jeśli ten kupon coś wygrał, gracz powinien odebrać wygraną.

    Domyslam sie, ze chodzi o wykonanie takiego sprawdzenia dla kazdego kuponu.
     */

    public void sprawdzKuponyIOdbierzWygrane(Centrala centrala){
        Iterator<Kupon> iterator = this.kupony.iterator();
        while (iterator.hasNext()) {
            Kupon kupon = iterator.next();
            // to znaczy, ze ostanieLosowanie bylo tez ostatnim losowaniem danego kuponu
            if (!kupon.sprawdzCzyZrealizowany() &&
                    centrala.dajNumerNastepnegoLosowania() - 1 == kupon.dajNumerOstatniegoLosowania()) {
                Kolektura kolektura = centrala.dajKolekture(kupon.dajIdKolektury());
                kolektura.zrealizujKupon(this, kupon);
                iterator.remove();
            }
        }
    }
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
