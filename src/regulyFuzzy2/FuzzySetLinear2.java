package regulyFuzzy2;
import java.util.ArrayList;

import parkowanie.Punkt2D;
import reulyFuzzy1.*;

public class FuzzySetLinear2 {
	FuzzySetLinear1 gornaFunkcjaPrzynaleznosci;
	FuzzySetLinear1 dolnaFunkcjaPrzynaleznosci;
	public String zmienna;
	public String nazwa = "";
	
	public FuzzySetLinear2() {
		zmienna = "pusty";
		gornaFunkcjaPrzynaleznosci = new FuzzySetLinear1();
		dolnaFunkcjaPrzynaleznosci = new FuzzySetLinear1();
	}
	
	public FuzzySetLinear2(FuzzySetLinear1 dolnaFunkcjaPrzynaleznosci, FuzzySetLinear1 gornaFunkcjaPrzynaleznosci) {
		this.dolnaFunkcjaPrzynaleznosci = dolnaFunkcjaPrzynaleznosci;
		this.gornaFunkcjaPrzynaleznosci = gornaFunkcjaPrzynaleznosci;
		//gorna i dolna funkcja przynaleznosci sa zbiorami rozmytymi typu 1 dotyczacymi oczywiscie tej samej zmiennej np skretu i majace ta sama nazwe np bardzoPrawo
		//wiec w 2 ponizszych linijkacha moge wstawic gornaFunkcjaPrzynalesnosci.zmienna rownie dobrze jak dolnaFunkcjaPrzynaleznosci.zmienna
		zmienna = dolnaFunkcjaPrzynaleznosci.zmienna;
		nazwa = dolnaFunkcjaPrzynaleznosci.nazwa;
	}
	
	public FuzzySetLinear2(FuzzySetLinear2 set) {
		this.gornaFunkcjaPrzynaleznosci = set.gornaFunkcjaPrzynaleznosci;
		this.dolnaFunkcjaPrzynaleznosci = set.dolnaFunkcjaPrzynaleznosci;
		this.zmienna = set.zmienna;
		nazwa = set.nazwa;
	}
	
	public void przytnijGornaFunkcjePrzynaleznosci(double wysokoscPrzyciecia) {
		gornaFunkcjaPrzynaleznosci.przytnij(wysokoscPrzyciecia);
	}
	
	public void przytnijDolnaFunkcjePrzynaleznosci(double wysokoscPrzyciecia) {
		dolnaFunkcjaPrzynaleznosci.przytnij(wysokoscPrzyciecia);
	}
	
	public FuzzySetLinear2 or(FuzzySetLinear2 set) {
		FuzzySetLinear1 dol = dolnaFunkcjaPrzynaleznosci.or(set.dolnaFunkcjaPrzynaleznosci);
		FuzzySetLinear1 gora = gornaFunkcjaPrzynaleznosci.or(set.gornaFunkcjaPrzynaleznosci);
		return new FuzzySetLinear2( dol, gora );
	}

	public void piszDoPlikow(String nazwaDol, String nazwaGora) {
		dolnaFunkcjaPrzynaleznosci.piszDoPliku(nazwaDol);
		gornaFunkcjaPrzynaleznosci.piszDoPliku(nazwaGora);
	}
}
