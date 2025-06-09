package totolotek.app;


import totolotek.system.Centrala;
import totolotek.kolektura.Kolektura;

import java.util.ArrayList;

// CHYBA POWINNO BYC W INNYM KATALOGU
public class Main {
    public static void main(String[] args) {
        Centrala centrala = new Centrala(0);
        ArrayList<Kolektura> listaKolektur = new ArrayList<>();
        for (int i = 0; i < 10; i++) { listaKolektur.add(centrala.stworzKolekture());}


    }
}