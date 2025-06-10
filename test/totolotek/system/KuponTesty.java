package totolotek.system;

import org.junit.jupiter.api.Test;
import totolotek.domain.Zaklad;
import totolotek.kolektura.Kolektura;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class KuponTesty {

    @Test
    void testZlyKuponZaMaloZakladow() {
        assertThrows(IllegalArgumentException.class, ()-> {
            Kupon kupon = new Kupon(1,1,1, List.of(), 1, 1);
        });
    }


    @Test
    void testZlyKuponZaDuzoZakladow() {
        assertThrows(IllegalArgumentException.class, ()-> {
            Kupon kupon = new Kupon(1,1,1,
                    Kolektura.generujZakladyChybilTrafil(Kupon.MAX_LICZBA_ZAKLADOW + 1), 1, 1);
        });
    }


    @Test
    void testZlyKuponZaMaloLosowan() {
        assertThrows(IllegalArgumentException.class, ()-> {
            Kupon kupon = new Kupon(Kupon.MIN_LICZBA_LOSOWAN - 1,1,1,
                    List.of(new Zaklad(Set.of(1,2,3,4,5,6))), 1, 1);
        });
    }
    @Test
    void testZlyKuponZaDuzoLosowan() {
        assertThrows(IllegalArgumentException.class, ()-> {
            Kupon kupon = new Kupon(Kupon.MAX_LICZBA_LOSOWAN + 1,1,1,
                    List.of(new Zaklad(Set.of(1,2,3,4,5,6))), 1, 1);
        });
    }


}
