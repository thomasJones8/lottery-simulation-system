package totolotek.testyintegracyjne;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totolotek.domain.Zaklad;
import totolotek.system.BudzetPanstwa;
import totolotek.system.Centrala;
import totolotek.util.Stale;
import totolotek.gracz.Minimalista;
import totolotek.kolektura.Kolektura;


import static org.junit.jupiter.api.Assertions.*;

// sprzedaż kuponu (ze sprawdzeniem stanów kont centrali i gracza oraz wielkości odprowadzonego podatku do budżetu).
public class SprzedazKuponuTestIntegracyjny {

    // na razie niech minimalista kupi jeden kupon chybil trafil
    private Centrala centrala;
    private Kolektura kolektura;
    private Minimalista minimalista;

    @BeforeEach
    void przygotowanie() {
        centrala = new Centrala(0);
        kolektura = centrala.stworzKolekture();

        // DODAC RESET BUDZETU 
    }

    // transakcja nie zaszla - za malo srodkow
    @Test
    void testSprzedazNieZaszlaZaMaloSrodkow() {

        minimalista = new Minimalista("Jan" , "Kow", "123", 0L, kolektura);
        assertFalse(minimalista.czyStac(1));
        // probuje kupic 1 kupon chybil trafil
        minimalista.kupKupon();

        // centrala powinna miec zysk 0 (a wiec budzet = 0, bo tak byl inicjowany)
        assertEquals(0, centrala.dajBudzet());
        // zebrane podatki powinny byc = 0
        assertEquals(0, BudzetPanstwa.dajInstancje().dajPobranePodatkiSuma());
        // sprzedane kupony powinny byc puste
        assertTrue(kolektura.dajSprzedaneKupony().isEmpty());

    }

    //udana zwykla transakcja
    @Test
    void testZwyklaUdanaSprzedaz() {
        minimalista = new Minimalista("Jan" , "Kow", "123", Zaklad.CENA_BRUTTO, kolektura);
        assertTrue(minimalista.czyStac(Zaklad.CENA_BRUTTO));
        // probuje kupic 1 kupon chybil trafil
        minimalista.kupKupon();

        // centrala powinna miec zysk
        assertEquals(Zaklad.CENA_NETTO, centrala.dajBudzet());
        // zebrane podatki powinny byc rowne podatkowi
        assertEquals(Zaklad.PODATEK, BudzetPanstwa.dajInstancje().dajPobranePodatkiSuma());
        // sprzedane kupony NIE powinny byc puste
        assertFalse(kolektura.dajSprzedaneKupony().isEmpty());
    }
    
    


}
