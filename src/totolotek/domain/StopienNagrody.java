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
}
