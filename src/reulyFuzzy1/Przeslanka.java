package reulyFuzzy1;

import java.util.ArrayList;

public class Przeslanka {
	ArrayList<FuzzySetLinear1> zbiory;
	
	public Przeslanka(ArrayList<FuzzySetLinear1> zbiory){
		this.zbiory = zbiory;
	}
	
	public double getWysokoscPrzyciecia(int x, int y, double orientacja){
		FuzzySetLinear1 z;
		double minPrzynaleznosc = 1;
		double przynaleznosc;
		for(int i = 0; i < zbiory.size(); i++){
			z = zbiory.get(i);
			if( z.zmienna.equals("x") ){
				przynaleznosc = z.getPrzynaleznosc(x);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
			if( z.zmienna.equals("y") ){
				przynaleznosc = z.getPrzynaleznosc(y);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
			if( z.zmienna.equals("orientacja") ){
				przynaleznosc = z.getPrzynaleznosc(orientacja);
				if( przynaleznosc < minPrzynaleznosc )
					minPrzynaleznosc = przynaleznosc;
			}
		}
		return minPrzynaleznosc;
	}
	
	public boolean czySieZapala(int x, int y, double orientacja){
		FuzzySetLinear1 z;
		int counter = 0;
		for(int i = 0; i < zbiory.size(); i++){
			z = zbiory.get(i);
			if( z.zmienna.equals("x") )
				if( z.getPrzynaleznosc(x) > 0.01 )
					counter++;
			if( z.zmienna.equals("y") )
				if( z.getPrzynaleznosc(y) > 0.01 )
					counter++;
			if( z.zmienna.equals("orientacja") )
				if( z.getPrzynaleznosc(orientacja) > 0.01 )
					counter++;
		}
		if( counter == zbiory.size() )
			return true;
		return false;
	}

}
