package totolotek.domain;

import org.junit.jupiter.api.Test;
import totolotek.domain.Zaklad;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class ZakladTesty {

    // zgodnie z konwencja Junit 5 pakietowa widocznosc
    @Test
    void testObstawionoZaDuzoLiczb() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Zaklad(Set.of(1,2,3,4,5,6,7));
        });
    }

    @Test
    void testObstawionoLiczbePowyzej49() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Zaklad(Set.of(999,2,3,4,5,6));
        });
    }

    @Test
    void testObstawionoPoprawnie() {
        Set<Integer> poprawneLiczby = Set.of(1,2,3,4,5,49);
        Zaklad poprawnyZaklad = new Zaklad(poprawneLiczby);
        assertNotNull(poprawnyZaklad);
        assertEquals(poprawneLiczby, poprawnyZaklad.dajLiczby());
    }
}
