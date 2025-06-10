package totolotek.domain;

public record WynikStopnia (long kwotaNagrody, int liczbaZwycieskichZakladow, long lacznaPulaNagrod){
    // akurat rekord fajnie automatyzuje toStringa, ale w zadaniu byly specjalne wymagania
    // i ten domyslny jest czytelniejszy
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Kwota nagrody: ");
;       if (liczbaZwycieskichZakladow > 0) {
            s.append(kwotaNagrody + " ");
        }
        else {
            s.append("NIKT NIE WYGRAL  ");
        }
        s.append("Liczba zwycieskich zakladow: " + liczbaZwycieskichZakladow + " ");
        s.append("Laczna pula nagrod: " + lacznaPulaNagrod);
        return s.toString();
    }
}

