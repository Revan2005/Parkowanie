package reulyFuzzy1;

public class Regula {
	
	Przeslanka przeslanka;
	FuzzySetLinear1 konkluzja;
	
	public Regula(Przeslanka p, FuzzySetLinear1 k){
		przeslanka = p; 
		konkluzja = k;
	}
	
	public boolean czySieZapala(int w, int h, double alfa){
		return przeslanka.czySieZapala(w, h, alfa);
	}
	
	public FuzzySetLinear1 getKonkluzjaPrzycieta(int w, int h, double alfa){
		FuzzySetLinear1 konkluzjaPrzycieta = new FuzzySetLinear1(konkluzja);
		double przyciecie = przeslanka.getWysokoscPrzyciecia(w, h, alfa);
		konkluzjaPrzycieta.przytnij(przyciecie);
		return konkluzjaPrzycieta;
	}

}
