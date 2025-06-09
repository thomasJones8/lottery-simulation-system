package totolotek.domain;

public enum StopienNagrody {
    I_STOPIEN(6),
    II_STOPIEN(5),
    III_STOPIEN(4),
    IV_STOPIEN(3);

    private final int wymaganeTrafienia;

    private StopienNagrody(int wymaganeTrafienia) {
        this.wymaganeTrafienia = wymaganeTrafienia;
    }

    public int dajWymaganeTrafienia() {
        return wymaganeTrafienia;
    }

    public static StopienNagrody naPodstawieTrafien(int liczbaTrafien) {
        for (StopienNagrody stopien: StopienNagrody.values()) {
            if (liczbaTrafien == stopien.dajWymaganeTrafienia()) {
                return stopien;
            }
        }
        // tzn ze trafien bylo mniej niz nagradzane minimum
        return null;
    }


}
