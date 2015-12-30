package regulyFuzzy2;

import java.util.ArrayList;

import reulyFuzzy1.FuzzySetLinear1;

public class PrzeslankaTypu2 {
	ArrayList<FuzzySetLinear2> zbiory;
	
	public PrzeslankaTypu2(ArrayList<FuzzySetLinear2> zbiory){
		this.zbiory = zbiory;
	}
	
	public double getWysokoscPrzycieciaGornejFunkcjiPrzynaleznosci(int x, int y, double orientacja){
		FuzzySetLinear2 zTypu2;
		FuzzySetLinear1 gornaFunkcjaPrzynaleznosci;
		double minPrzynaleznosc = 1;
		double przynaleznosc;
		for(int i = 0; i < zbiory.size(); i++){
			zTypu2 = zbiory.get(i);
			gornaFunkcjaPrzynaleznosci = zTypu2.gornaFunkcjaPrzynaleznosci;
			if( gornaFunkcjaPrzynaleznosci.zmienna.equals("x") ){
				przynaleznosc = gornaFunkcjaPrzynaleznosci.getPrzynaleznosc(x);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
			if( gornaFunkcjaPrzynaleznosci.zmienna.equals("y") ){
				przynaleznosc = gornaFunkcjaPrzynaleznosci.getPrzynaleznosc(y);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
			if( gornaFunkcjaPrzynaleznosci.zmienna.equals("orientacja") ){
				przynaleznosc = gornaFunkcjaPrzynaleznosci.getPrzynaleznosc(orientacja);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
		}
		return minPrzynaleznosc;
	}
	
	public double getWysokoscPrzycieciaDolnejFunkcjiPrzynaleznosci(int x, int y, double orientacja){
		FuzzySetLinear2 zTypu2;
		FuzzySetLinear1 dolnaFunkcjaPrzynaleznosci;
		double minPrzynaleznosc = 1;
		double przynaleznosc;
		for(int i = 0; i < zbiory.size(); i++){
			zTypu2 = zbiory.get(i);
			dolnaFunkcjaPrzynaleznosci = zTypu2.dolnaFunkcjaPrzynaleznosci;
			if( dolnaFunkcjaPrzynaleznosci.zmienna.equals("x") ){
				przynaleznosc = dolnaFunkcjaPrzynaleznosci.getPrzynaleznosc(x);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
			if( dolnaFunkcjaPrzynaleznosci.zmienna.equals("y") ){
				przynaleznosc = dolnaFunkcjaPrzynaleznosci.getPrzynaleznosc(y);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
			if( dolnaFunkcjaPrzynaleznosci.zmienna.equals("orientacja") ){
				przynaleznosc = dolnaFunkcjaPrzynaleznosci.getPrzynaleznosc(orientacja);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
		}
		return minPrzynaleznosc;
	}
	
	public boolean czySieZapala(int x, int y, double orientacja, boolean czyDotychczasDoPrzodu){
		double doubleCzyDotychczasDoPrzodu;
		if(czyDotychczasDoPrzodu)
			doubleCzyDotychczasDoPrzodu = 0;
		else
			doubleCzyDotychczasDoPrzodu = -1;
		FuzzySetLinear1 z;
		int counter = 0;
		for(int i = 0; i < zbiory.size(); i++){
			z = zbiory.get(i).dolnaFunkcjaPrzynaleznosci;
			if( z.zmienna.equals("x") )
				if( z.getPrzynaleznosc(x) > 0.01 )
					counter++;
			if( z.zmienna.equals("y") )
				if( z.getPrzynaleznosc(y) > 0.01 )
					counter++;
			if( z.zmienna.equals("orientacja") )
				if( z.getPrzynaleznosc(orientacja) > 0.01 )
					counter++;
			if( z.zmienna.equals("zwrot"))
				if( z.getPrzynaleznosc(doubleCzyDotychczasDoPrzodu) > 0.1 )
					counter++;
		}
		if( counter == zbiory.size() )
			return true;
		return false;
	}

}
