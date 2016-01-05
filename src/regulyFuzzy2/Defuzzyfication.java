package regulyFuzzy2;

import java.util.ArrayList;

import parkowanie.Punkt2D;

public class Defuzzyfication {
	FuzzySetLinear2 zbior;
	final int LICZBA_PUNKTOW_DYSKRETYZACJI = 100;
	double[] punktyDyskretyzacji;
	double[] tetaMin;
	double[] tetaMax;
	
	public Defuzzyfication(FuzzySetLinear2 zbior){
		this.zbior = zbior;
		punktyDyskretyzacji = dyskretyzuj();
		setTetas();
	}
	
	public double getResult(){
		return skretDefuzzyfication();
	}
	
	private double[] dyskretyzuj(){
		double[] punktyDyskretyzacji = new double[LICZBA_PUNKTOW_DYSKRETYZACJI];
		ArrayList<Punkt2D> wierzcholki = zbior.gornaFunkcjaPrzynaleznosci.wierzcholki;
		double min = wierzcholki.get(0).x;
		double max = wierzcholki.get(wierzcholki.size() - 1).x;
		if(max < min){
			double tmp = min;
			min = max;
			max = tmp;
		}
		double interval = (max - min) / LICZBA_PUNKTOW_DYSKRETYZACJI;	
		for(int i = 0; i < LICZBA_PUNKTOW_DYSKRETYZACJI; i++){
			punktyDyskretyzacji[i] = min + i*interval;
			//System.out.println("i: "+i+"  punkt pomiarowy = "+punktyDyskretyzacji[i] + "  interval = " + interval + "  min = " + min + "  max = " + max);
		}
		return punktyDyskretyzacji;
	}
	
	private void setTetas(){
		tetaMin = new double[LICZBA_PUNKTOW_DYSKRETYZACJI];
		tetaMax = new double[LICZBA_PUNKTOW_DYSKRETYZACJI];
		for(int i = 0; i < LICZBA_PUNKTOW_DYSKRETYZACJI; i++){
			double x = punktyDyskretyzacji[i];
			//System.out.println(zbior.dolnaFunkcjaPrzynaleznosci.getPrzynaleznosc(x));
			tetaMin[i] = zbior.dolnaFunkcjaPrzynaleznosci.getPrzynaleznosc(x);
			tetaMax[i] = zbior.gornaFunkcjaPrzynaleznosci.getPrzynaleznosc(x);
		}
	}
	
	private double skretDefuzzyfication(){
		//redukcja typu
		double[] przedzial = typeReduction();
		return step2(przedzial);
	}
	
	private double[] typeReduction(){
		double min = getMinS();
		double max = getMaxS();
		double[] przedzial = { min, max };
		return przedzial;
	}
	
	private double getMaxS(){
		//teta dol tetagora to max i min wartosc J(x) (przynaleznosc 1rzedu w punkcie x)
		//Punkty po kolei jak w ksiazce Leszka Rutkowskiego 
		//krok 1
		double[] tetaK = new double[LICZBA_PUNKTOW_DYSKRETYZACJI];
		for(int k = 0; k < LICZBA_PUNKTOW_DYSKRETYZACJI; k++){
			tetaK[k] = ( tetaMin[k] + tetaMax[k] ) / 2.0;
		}
		double sPrim = obliczS(tetaK);
		//krok 2 szukanie j spelniajacego warunek
		int j = 0;
		while(true){
		for(int i = 0 ; i < LICZBA_PUNKTOW_DYSKRETYZACJI - 1; i++){
			if( (punktyDyskretyzacji[i] <= sPrim) && (sPrim < punktyDyskretyzacji[i+1]) )
				j = i;
		}
		//krok 3
		for(int k = 0; k < LICZBA_PUNKTOW_DYSKRETYZACJI; k++){
			if(k <= j)
				tetaK[k] = tetaMin[k];
			else
				tetaK[k] = tetaMax[k];
		}
		double sPrim2 = obliczS(tetaK);
		//krok 4 i 5
		if(sPrim2 == sPrim)
			return sPrim2;
		else
			sPrim = sPrim2;
		}	
	}
	
	private double getMinS(){
		//teta dol tetagora to max i min wartosc J(x) (przynaleznosc 1rzedu w punkcie x)
		//Punkty po kolei jak w ksiazce Leszka Rutkowskiego 
		//krok 1
		double[] tetaK = new double[LICZBA_PUNKTOW_DYSKRETYZACJI];
		for(int k = 0; k < LICZBA_PUNKTOW_DYSKRETYZACJI; k++){
			tetaK[k] = ( tetaMin[k] + tetaMax[k] ) / 2.0;
		}
		double sPrim = obliczS(tetaK);
		//krok 2 szukanie j spelniajacego warunek
		int j = 0;
		while(true){
		for(int i = 0 ; i < LICZBA_PUNKTOW_DYSKRETYZACJI - 1; i++){
			if( (punktyDyskretyzacji[i] < sPrim) && (sPrim <= punktyDyskretyzacji[i+1]) )
				j = i;
		}
		//krok 3
		for(int k = 0; k < LICZBA_PUNKTOW_DYSKRETYZACJI; k++){
			if(k < j)
				tetaK[k] = tetaMax[k];
			else
				tetaK[k] = tetaMin[k];
		}
		double sPrim2 = obliczS(tetaK);
		//krok 4 i 5
		if(sPrim2 == sPrim)
			return sPrim2;
		else
			sPrim = sPrim2;
		}
	}
	
	private double obliczS(double[] tetaK){
		double s;
		double licznik = 0, mianownik = 0;
		double xK;
		for(int k = 0; k < LICZBA_PUNKTOW_DYSKRETYZACJI; k++){
			xK = punktyDyskretyzacji[k];
			licznik += xK * tetaK[k];
			mianownik += tetaK[k];
		}
		s = licznik / mianownik;
		return s;
	}
	
	private double step2(double[] przedzial){
		return (przedzial[0] + przedzial[1]) / 2.0;
	}
	

}
