package parkowanie;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import reulyFuzzy1.RegulyFuzzy1;
import regulyFuzzy2.RegulyFuzzy2;


public class ModulDecyzyjny {
	Car samochod;
	ModelOtoczenia modelOtoczenia;
	BufferedImage mapaParkingu;
	
	//dane miejsca parkingowego
	//na razie zakladam ze jest tylko jedno miejsce parkingowe na mapie
	Point greenFieldStart;
	Point greenFieldEnd;
	Point centrumMiejscaParkingowego;
	//Czujnik czujnik;
	int dlugoscMiejscaParkingowego, wysokoscMiejscaParkingowego;
	int xSamochodu, ySamochodu;
	double orientacjaSamochodu;
	int x0, y0;
	int xSrodkaParkingu, ySrodkaParkingu;
	RegulyFuzzy1 regulyFuzzy1;
	RegulyFuzzy2 regulyFuzzy2;
	
	
	public ModulDecyzyjny(Car c, ModelOtoczenia model){
		samochod = c;
		modelOtoczenia = model;
		xSamochodu = samochod.polozenie.x;
		ySamochodu = samochod.polozenie.y;
		orientacjaSamochodu = samochod.kierunek;
		x0 = modelOtoczenia.x0;
		y0 = modelOtoczenia.y0;
		xSrodkaParkingu = model.xSrodkaParkingu;
		ySrodkaParkingu = model.ySrodkaParkingu;
		dlugoscMiejscaParkingowego = modelOtoczenia.dlugoscMiejscaParkingowego;
		wysokoscMiejscaParkingowego = modelOtoczenia.wysokoscMiejscaParkingowego;
		
		regulyFuzzy1 = new RegulyFuzzy1(samochod, modelOtoczenia);
		regulyFuzzy2 = new RegulyFuzzy2(samochod, modelOtoczenia);
		//czujnik = new Czujnik(c, mapaParkingu);
		//System.out.println(mapaParkingu.toString());
		//bedzie sie staral umiescic srodek tylnej osi - polozenie, na srodku zielonego pola
		//lokalizuje zielone, zakladam ze pole do parkowania to prostokat z bokami rownoleglymi do osi parkingu
		
		//int x1 = samochod.polozenie.x, y1 = samochod.polozenie.y;
		//int x2 = centrumMiejscaParkingowego.x, y2 = centrumMiejscaParkingowego.y;
		
		//System.out.println("x1="+x1+" y1="+y1+" x2="+x2+" y2="+y2);
		//double alfa = Math.atan((double)(y2-y1)/(x2-x1))+Math.PI/2; //wykombinowane w zeszycie 
		// kat licze tak jak w przypadku kierunku (samochod.kierunek) od ustawienia przednimi swiatlami do gory ekranu i zgodnie z ruchem wskazowek zegara
		//System.out.println("alfa="+(alfa*180)/Math.PI+"; kierunek="+(samochod.kierunek*180)/Math.PI);
		
	}
	
	
	public void restart(BufferedImage map){
		modelOtoczenia.restart();
		xSamochodu = samochod.polozenie.x;
		ySamochodu = samochod.polozenie.y;
		orientacjaSamochodu = samochod.kierunek;
		mapaParkingu = map;
		x0 = modelOtoczenia.x0;
		y0 = modelOtoczenia.y0;
		//czujnik = new Czujnik(samochod, mapaParkingu);
		
		//int x1 = samochod.polozenie.x, y1 = samochod.polozenie.y;
		//int x2 = centrumMiejscaParkingowego.x, y2 = centrumMiejscaParkingowego.y;
		//xSrodkaParkingu = x2; ySrodkaParkingu = y2;
		//System.out.println("x1="+x1+" y1="+y1+" x2="+x2+" y2="+y2);
		//double alfa = Math.atan((double)(y2-y1)/(x2-x1))+Math.PI/2; //wykombinowane w zeszycie 
		//if(x1>x2)
		//	alfa = Math.atan((double)(y2-y1)/(x2-x1))+Math.PI*3/2;//bo atan przyjmuje wartosci -pi/2 do pi/2
		// kat licze tak jak w przypadku kierunku (samochod.kierunek) od ustawienia przednimi swiatlami do gory ekranu i zgodnie z ruchem wskazowek zegara
		//System.out.println("alfa="+(alfa*180)/Math.PI+"; kierunek="+(samochod.kierunek*180)/Math.PI);
		
	}
	
	public double[] decyzjaRegulyOstre(Car c, boolean dotychczasDoPrzodu){
		xSamochodu = c.polozenie.x;
		ySamochodu = c.polozenie.y;
		orientacjaSamochodu = c.kierunek;
		//czujnik = new Czujnik(c, mapaParkingu);
		//InfoZCzujnika infoZCzujnika = czujnik.getDistanceAndDirectionToClosestObstacle();
		System.out.println("podejmuje decyzje"+xSamochodu+" " +ySamochodu+" odleglosc od tylo do osi: "+ samochod.odleglosc_od_tylu_do_osi);
		
		double skretKol = 0;
		double przodTyl = 0;
		if(dotychczasDoPrzodu)
			przodTyl = 0;
		else
			przodTyl = -1;
		//System.out.println("promien dlugi = "+c.promienDlugi+" promien krotki = "+c.promienKrotki+"  x0="+x0+"  xSamochodu"+xSamochodu);
		//-----------------------------------REGULY============================================
		//jezeli jestes na prawo to cofaj
		if(((x0+dlugoscMiejscaParkingowego)-xSamochodu)<c.promienDlugi){
			System.out.println("niby za bardzo z przodu"+c.promienDlugi);
			przodTyl = -1;
		}
		//jezeli za bardzo na lewo to do przodu
		if((xSamochodu-x0)<c.promienKrotki){
			System.out.println(xSamochodu+" "+x0+" "+(xSamochodu-x0)+"za blisko tylem");
			przodTyl = 0;
		}
		//jezeli jestes wysoko i twoja orientacja jest bliska poziomu to cofajac skrecaj w lewo
		if( ((ySrodkaParkingu-ySamochodu)>120) && (orientacjaSamochodu>Math.PI/6.0) )
			if(przodTyl !=0)
				skretKol = Math.PI;
			else{
				//jezel przodTyl == 0 to jade do przodu
				skretKol = -Math.PI;
			}
		//jezeli jestes nisko  i orientacja jest bliska pionu skrec auto w prawo
		if( (ySrodkaParkingu-ySamochodu) < 50 && (orientacjaSamochodu<Math.PI/3.0) ) 
			if(przodTyl !=0)
				skretKol = -Math.PI;
			else
				skretKol = Math.PI;
		//jezeli jestes na wysokosci bliskiej celu ustawiaj sie poziomo (skrec w prawo)
		if( (Math.abs(ySrodkaParkingu-ySamochodu) < 20) && (orientacjaSamochodu<Math.PI/2) )
			if(przodTyl !=0)
				skretKol = -Math.PI;
			else
				skretKol = Math.PI;
		
		System.out.println("orientacja = "+orientacjaSamochodu*180/Math.PI+"   skret = "+skretKol);

		/*
		//przeszkody:
		if(infoZCzujnika.dystans < 20)
			//czujnik powinien zwracac tylko odleglosci od rogow bo dla tamtych pozostalych punktow nie dziala cos
			switch(infoZCzujnika.kierunek){
				case "prawyPrzedniRog":
					System.out.println("prawy przedni rog                                        =============");
					skretKol = Math.PI;
					przodTyl = -1;
					break;
				default:
					break;
			}
		*/
		//jezeli jestes pionowo i masz skrecac w lewo to nie skrecaj
		if( (orientacjaSamochodu<Math.PI/18) && (skretKol < 0) )
			skretKol = 0;
		//jezeli jestes za bardz ow prawo i masz skrecac w prawo to zaniechaj
		if((orientacjaSamochodu > Math.PI) && (skretKol > 0))
			skretKol = 0;

		skretKol /= 90.0;
		double[] decyzja = new double[2];
		decyzja[0] = skretKol; // kierunek
		decyzja[1] = przodTyl; //0 do przodu, cokolwiek innego do tylu
		
		return decyzja;
	}
	
	
	public double[] decyzjaFuzzy1(Car c, boolean dotychczasDoPrzodu){
		//reguly =========================================================================================
		
		double[] decyzja = regulyFuzzy1.podejmijDecyzje(c, dotychczasDoPrzodu);
		
		//koniec regul ===================================================================================
		return decyzja;
	}
	
	public double[] decyzjaFuzzy2(Car c, boolean dotychczasDoPrzodu){
		
		double[] decyzja = regulyFuzzy2.podejmijDecyzje(c, dotychczasDoPrzodu);
		return decyzja;
		
		/*xSamochodu = c.polozenie.x;
		ySamochodu = c.polozenie.y;
		orientacjaSamochodu = c.kierunek;
		//czujnik = new Czujnik(c, mapaParkingu);
		//InfoZCzujnika infoZCzujnika = czujnik.getDistanceAndDirectionToClosestObstacle();
		
		double skretKol = 0;
		double przodTyl = 0;
		if(dotychczasDoPrzodu)
			przodTyl = 0;
		else
			przodTyl = -1;
		
		//reguly =========================================================================================
		
		
		
		//koniec regul ===================================================================================
		
		
		double[] decyzja = new double[2];
		decyzja[0] = skretKol; // kierunek
		decyzja[1] = przodTyl; //0 do przodu, cokolwiek innego do tylu
		return decyzja;*/
	}
	
	public double roznicaKierunkow(){
		//System.out.println(mapaParkingu.toString());
		//bedzie sie staral umiescic srodek tylnej osi - polozenie, na srodku zielonego pola
		//lokalizuje zielone, zakladam ze pole do parkowania to prostokat z bokami rownoleglymi do osi parkingu
		greenFieldStart = modelOtoczenia.getGreenFieldStart();
		greenFieldEnd = modelOtoczenia.getGreenFieldEnd();
		if((greenFieldStart.x==greenFieldEnd.x)||(greenFieldStart.y==greenFieldEnd.y)){
			System.out.println("Nie ma miejsca parkingowego!");
		}
		int x = greenFieldStart.x+(greenFieldEnd.x - greenFieldStart.x)/2;
		int y = greenFieldStart.y+(greenFieldEnd.y - greenFieldStart.y)/2;
		//System.out.println(greenFieldStart.toString()+"  "+greenFieldEnd.toString());
		centrumMiejscaParkingowego = new Point(x, y);
		//System.out.println(centrumMiejscaParkingowego.toString());
		
		int x1 = samochod.polozenie.x, y1 = samochod.polozenie.y;
		int x2 = centrumMiejscaParkingowego.x, y2 = centrumMiejscaParkingowego.y;
		//System.out.println("x1="+x1+" y1="+y1+" x2="+x2+" y2="+y2);
		double alfa = Math.atan((double)(y2-y1)/(x2-x1))+Math.PI/2; //wykombinowane w zeszycie 
		if(x1>x2)
			alfa = Math.atan((double)(y2-y1)/(x2-x1))+Math.PI*3/2;//bo atan przyjmuje wartosci -pi/2 do pi/2
		// kat licze tak jak w przypadku kierunku (samochod.kierunek) od ustawienia przednimi swiatlami do gory ekranu i zgodnie z ruchem wskazowek zegara
		//wyswietlam w katach ale licze w radianach
		System.out.println("alfa="+(alfa*180)/Math.PI+"; kierunek="+(samochod.kierunek*180)/Math.PI);
		
		return alfa-samochod.kierunek; //alfa - kat miedzy osia oY a prosta od srodka auta do srodka miejsca parkingowego
		//samochod.kierunek - kierunek/zwrot auta liczony tez od osi oY (gorna czesc oy)
	}

}
