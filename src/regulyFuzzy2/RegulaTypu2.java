package regulyFuzzy2;
import regulyFuzzy2.PrzeslankaTypu2;;


public class RegulaTypu2 {
	PrzeslankaTypu2 przeslanka;
	FuzzySetLinear2 konkluzja;
	
	public RegulaTypu2(PrzeslankaTypu2 p, FuzzySetLinear2 k){
		przeslanka = p; 
		konkluzja = k;
	}
	
	public boolean czySieZapala(int w, int h, double alfa, boolean czyDotychczasDoPrzodu){
		return przeslanka.czySieZapala(w, h, alfa, czyDotychczasDoPrzodu);
	}
	
	public FuzzySetLinear2 getKonkluzjaPrzycieta(int w, int h, double alfa){
		FuzzySetLinear2 konkluzjaPrzycieta = new FuzzySetLinear2(konkluzja);
		double przyciecieGory = przeslanka.getWysokoscPrzycieciaGornejFunkcjiPrzynaleznosci(w, h, alfa);
		konkluzjaPrzycieta.przytnijGornaFunkcjePrzynaleznosci(przyciecieGory);
		double przyciecieDolu = przeslanka.getWysokoscPrzycieciaDolnejFunkcjiPrzynaleznosci(w, h, alfa);
		konkluzjaPrzycieta.przytnijDolnaFunkcjePrzynaleznosci(przyciecieDolu);
		return konkluzjaPrzycieta;
	}
}
