package totolotek.system;

/*
Tutaj mialem zagwozdke, jak to sensownie rozegrac - bo oczywiscie zakladamy, ze
budzet panstwa jest tylko jeden.
1) stworzenie zwyklej klasy - bez sensu, umozliwia na stworzenie kilku budzetow panstwa.
2) pola i metody static - tez bez sensu, nie rozwiazuje powyzszego problemu, ale tez
logicznie sie nie zgadza - np. pobranePodatki sa zwiazane tylko z jedna instancja, nie z klasa w ogole.
Tak wiec wyczytalem ze jest cos takiego jak wzorzec projektowy Singleton, i na to sie zdecydowalem.

Jednak po chwili pomyslalem, ze to moze byc problematyczne w sytuacji, gdybysmy chcieli
rownolegle symulowac system Totolotka w kilku panstwach. Jednak specyfikacja nic o tym nie mowi,
wiec zdecydowalem sie pozostac przy Singletonie.
 */
public class BudzetPanstwa {
    private static final BudzetPanstwa budzetPanstwa = new BudzetPanstwa();
    private long pobranePodatkiSuma;
    private long przekazaneSubwencjeSuma;

    private BudzetPanstwa(){
        pobranePodatkiSuma = 0;
        przekazaneSubwencjeSuma = 0;
    }

    public static BudzetPanstwa dajInstancje() {
        return budzetPanstwa;
    }

    public long dajPobranePodatkiSuma() { return pobranePodatkiSuma;}
    public void pobierzPodatek(long kwota) {
        pobranePodatkiSuma += kwota;
    }
    public String wypiszPobranePodatkiSuma() {
        return "Pobrane podatki: " + pobranePodatkiSuma;
    }


    public void przekazSubwencje(long kwota) {
        przekazaneSubwencjeSuma += kwota;
    }
    public String wypiszPrzekazaneSubwencjeSuma() {
        return "Przekazane subwencje: " + przekazaneSubwencjeSuma;
    }

}

