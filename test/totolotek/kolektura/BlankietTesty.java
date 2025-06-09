package totolotek.kolektura;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import totolotek.util.Stale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BlankietTesty {

    private Map<Integer, PoleBlankietu> mapaPoprawnaPelna;
    @BeforeEach
    void przygotowanie() {
        mapaPoprawnaPelna = new HashMap<>();
        for (int i = 1; i <= Blankiet.LICZBA_POL_BLANKIET; i++) {
            mapaPoprawnaPelna.put(i, new PoleBlankietu(Set.of(i, i + 1, i + 2, i + 3, i + 4, 49), false));
        }
    }

    // poprawny blankiet
    @Test
    void testPoprawnyBlankiet(){
        Blankiet blankiet = new Blankiet(mapaPoprawnaPelna, Set.of(8));

        assertNotNull(blankiet);
        assertEquals(8, blankiet.dajLiczbeLosowan());
        assertEquals(Blankiet.LICZBA_POL_BLANKIET, blankiet.dajPoprawneNieanulowaneZaklady().size());
    }
    
    // czy liczba losowan wychodzi 1 jak nic nie jest zaznaczone
    @Test
    void testPoprawnyBlankietBezZaznaczonejLiczbyLosowan() {
        Blankiet blankiet = new Blankiet(mapaPoprawnaPelna, Set.of());
        assertNotNull(blankiet);
        assertEquals(1, blankiet.dajLiczbeLosowan());
        assertEquals(Blankiet.LICZBA_POL_BLANKIET, blankiet.dajPoprawneNieanulowaneZaklady().size());
    }
    // czy liczba losowan wychodzi 5 jak jest kilka zaznaczonych (w tym 5)
    @Test
    void testPoprawnyBlankietZaznaczoneKilkaLiczbLosowan() {
        Blankiet blankiet = new Blankiet(mapaPoprawnaPelna, Set.of(1,2,3,4,5));
        assertNotNull(blankiet);
        assertEquals(5, blankiet.dajLiczbeLosowan());
        assertEquals(Blankiet.LICZBA_POL_BLANKIET, blankiet.dajPoprawneNieanulowaneZaklady().size());
    }
    // czy rzuca wyjatek jak jest za duzo liczb losowan zaznaczonych
    @Test
    void testNiepoprawnyBlankietZaDuzoLiczbLosowanZaznaczonych() {
        assertThrows(IllegalArgumentException.class, () -> {
            Blankiet blankiet = new Blankiet(mapaPoprawnaPelna, Set.of(1,2,3,4,5,6,7,8,9,10,11));
        });
    }
    // zadne pole nie jest poprawne - powinna wyjsc pusta lista
    @Test
    void testPoprawnyBlankietAleZlePola() {
        Map<Integer, PoleBlankietu> mapaZlePola = new HashMap<>();
        // za malo skreslonych liczb
        mapaZlePola.put(1, new PoleBlankietu(Set.of(1, 2, 3), false));
        // za duzo skreslonych liczb
        mapaZlePola.put(2, new PoleBlankietu(Set.of(1, 2, 3, 4, 5, 6, 7), false));
        // anulowane
        mapaZlePola.put(3, new PoleBlankietu(Set.of(1, 2, 3, 4, 5, 6), true));

        Blankiet blankiet = new Blankiet(mapaZlePola, null);
        assertEquals(new ArrayList<>(), blankiet.dajPoprawneNieanulowaneZaklady());
        assertEquals(Stale.DOMYSLNA_LICZBA_LOSOWAN_BLANKIET, blankiet.dajLiczbeLosowan());
    }
}
