package totolotek.domain;

import java.util.*;

public class Losowanie {
    private final int numerLosowania;
    private final Set<Integer> zwycieskieLiczby;
    private final Map<StopienNagrody, WynikStopnia> wyniki;

    public Losowanie(int numerLosowania, Set<Integer> zwycieskieLiczby, Map<StopienNagrody, WynikStopnia> wyniki) {
        this.numerLosowania = numerLosowania;
        this.zwycieskieLiczby = zwycieskieLiczby;
        this.wyniki = wyniki;
    }

    public int dajNumer() { return numerLosowania;}
    public Set<Integer> dajZwycieskieLiczby() { return zwycieskieLiczby;};

    public Map<StopienNagrody, WynikStopnia> dajWyniki() { return Collections.unmodifiableMap(this.wyniki);}



    @Override public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Losowanie nr " + numerLosowania + "\n");
        s.append("Wyniki: ");
        for (Integer liczba : zwycieskieLiczby) {
            s.append(String.format("%2d", liczba) + " ");
        }
        s.append("\n");
        return s.toString();
    }
}
