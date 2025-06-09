package totolotek.kolektura;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PoleBlankietuTesty {

    // zwykle dobre pole
    @Test
    void testPoprawneDobrePole() {
        Set<Integer> poprawneLiczby = Set.of(1,2,3,4,5,49);
        PoleBlankietu poprawnePole = new PoleBlankietu(poprawneLiczby, false);
        assertNotNull(poprawnePole);
        assertEquals(poprawneLiczby, poprawnePole.dajSkresloneLiczby());
        assertTrue(poprawnePole.czyDobry());
    }
    // niepoprawne pole: z zaznaczona liczba spoza zakresu (0)
    @Test
    void testPoleZZerem() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PoleBlankietu(Set.of(0,2,3,4,5,6), true);
        });
    }
    // poprawne ale zle pole: zaznaczone mniej niz 6 liczb
    @Test
    void testPoprawneZlePoleMniejNiz6Liczb() {
        Set<Integer> Liczby = Set.of(1,2,3,4);
        PoleBlankietu pole = new PoleBlankietu(Liczby, false);
        assertNotNull(pole);
        assertEquals(Liczby, pole.dajSkresloneLiczby());
        assertFalse(pole.czyDobry());
    }

    @Test
    void testPoprawneZlePoleBoAnulowane() {
        PoleBlankietu pole = new PoleBlankietu(Set.of(1, 2, 3, 4, 5, 6), true);
        assertFalse(pole.czyDobry());
    }
}
