package totolotek.system;

import org.junit.jupiter.api.Test;
import totolotek.kolektura.Kolektura;
import totolotek.system.Centrala;

import static org.junit.jupiter.api.Assertions.*;


public class CentralaTesty {
    private Centrala centrala;
    // czy poprawnie tworzy kolekture
    //czy kolejne kupony maja kolejne numery
    @Test
    void testCzyPoprawnieTworzyKolekture(){
        centrala = new Centrala(0);
        Kolektura kolektura1 = centrala.stworzKolekture();
        Kolektura kolektura2 = centrala.stworzKolekture();

        assertNotNull(kolektura1);
        assertNotNull(kolektura2);
        assertNotEquals(kolektura1, kolektura2);
        assertEquals(kolektura1.dajId() + 1, kolektura2.dajId());
    }
}
