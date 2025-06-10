package totolotek.system;

import org.junit.jupiter.api.Test;
import totolotek.domain.StopienNagrody;
import totolotek.domain.WynikStopnia;
import totolotek.kolektura.Kolektura;


import java.util.Map;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;
import static totolotek.domain.StopienNagrody.*;


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


    @Test
    void testZwykleLosowanie() {
        centrala = new Centrala(0);
        EnumMap<StopienNagrody, Integer> trafienia = new EnumMap<>(StopienNagrody.class);
        trafienia.put(I_STOPIEN, 1);
        trafienia.put(II_STOPIEN, 10);
        trafienia.put(III_STOPIEN, 100);
        trafienia.put(IV_STOPIEN, 1000);
        Map<StopienNagrody, WynikStopnia> wyniki = centrala.wyznaczNagrody(trafienia, 10_000_000_00L);

        assertEquals(new WynikStopnia(4_400_000_00L, 1,
                4_400_000_00L), wyniki.get(I_STOPIEN));
        assertEquals(new WynikStopnia(8_000_000L, 10, 80_000_000L), wyniki.get(II_STOPIEN));
        assertEquals(new WynikStopnia(4_776_000L, 100, 477_600_000L),
                wyniki.get(III_STOPIEN));
        assertEquals(new WynikStopnia(2400L, 1000, 2_400_000L),
                wyniki.get(IV_STOPIEN));
    }



    @Test
    void testLosowanieZKumulacja() {
        centrala = new Centrala(0);
        centrala.ustawKumulacje(5_000_000_00L);
        EnumMap<StopienNagrody, Integer> trafienia = new EnumMap<>(StopienNagrody.class);
        trafienia.put(I_STOPIEN, 0);
        trafienia.put(II_STOPIEN, 10);
        trafienia.put(III_STOPIEN, 100);
        trafienia.put(IV_STOPIEN, 1000);
        Map<StopienNagrody, WynikStopnia> wyniki = centrala.wyznaczNagrody(trafienia, 10_000_000_00L);

        assertEquals(new WynikStopnia(0L, 0, 940_000_000L),
                wyniki.get(I_STOPIEN));
        assertEquals(new WynikStopnia(8_000_000L, 10, 80_000_000L), wyniki.get(II_STOPIEN));
        assertEquals(new WynikStopnia(4_776_000L, 100, 477_600_000L),
                wyniki.get(III_STOPIEN));
        assertEquals(new WynikStopnia(2400L, 1000, 2_400_000L),
                wyniki.get(IV_STOPIEN));
        assertEquals(940_000_000L, centrala.dajKumulacje());
    }
}
