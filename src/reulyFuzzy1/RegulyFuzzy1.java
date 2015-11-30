package reulyFuzzy1;
import parkowanie.*;
import java.awt.Point;
import java.util.ArrayList;

import parkowanie.Car;
import parkowanie.FuzzySetLinear1;
import parkowanie.ModelOtoczenia;
import parkowanie.Punkt2D;

public class RegulyFuzzy1 {
	Point srodekParkingu;
	Point zeroZero;
	double r;
	double R;
	int w;
	int h;
	double orientacjaSamochodu;
	Point polozenieSamochodu;
	
	//moje zbiory rozmyte
	//prawolewo
	FuzzySetLinear1 polozenieBardzoPrawo;
	FuzzySetLinear1 polozeniePrawo;
	FuzzySetLinear1 polozenieSrodek;
	FuzzySetLinear1 polozenieLewo;
	FuzzySetLinear1 polozenieBardzoLewo;
	//goradol
	FuzzySetLinear1 hWysoko;
	FuzzySetLinear1 hSrednio;
	FuzzySetLinear1 hNisko;
	//orientacja = kat
	FuzzySetLinear1 orientacjaBardzoPrawo;
	FuzzySetLinear1 orientacjaPrawo;
	FuzzySetLinear1 orientacjaSrodek; //srodek znaczy tutaj poziomo i w prawo skierowane auto
	FuzzySetLinear1 orientacjaLewo;
	FuzzySetLinear1 orientacjaBardzoLewo;
	
	//zbiory rozmyte dotyczace wyjscia - decyzji o skrecie
	FuzzySetLinear1 skretPrawo;
	FuzzySetLinear1 skretLewo;
	
	ArrayList<Regula> reguly;
	
	public RegulyFuzzy1(Car c, ModelOtoczenia mO){
		polozenieSamochodu = new Point(c.polozenie.x, c.polozenie.y);
		srodekParkingu = mO.centrumMiejscaParkingowego;
		zeroZero = new Point(mO.x0, mO.y0);
		r = c.promienKrotki;
		R = c.promienDlugi;
		w = mO.dlugoscMiejscaParkingowego;
		h = mO.wysokoscMiejscaParkingowego;
		orientacjaSamochodu = c.kierunek;
		
		//definiuje zbiory rozmyte---------------------------------------------------------------------------------------------------------------------------------
		//==============================================================
		//prawolewo
		//==============================================================
		//1
		ArrayList<Punkt2D> wierzcholki1 = new ArrayList<Punkt2D>();
		wierzcholki1.add( new Punkt2D( (zeroZero.x + w -R), 0 ) );
		wierzcholki1.add( new Punkt2D( (zeroZero.x + w), 1 ) );
		polozenieBardzoPrawo = new FuzzySetLinear1( wierzcholki1 );
		//2
		ArrayList<Punkt2D> wierzcholki2 = new ArrayList<Punkt2D>();
		wierzcholki2.add(new Punkt2D( (r + 0.5*(zeroZero.x + w - R - r)), 0 ));
		wierzcholki2.add(new Punkt2D( (zeroZero.x + w -R), 1 ));
		wierzcholki2.add(new Punkt2D( (zeroZero.x + w), 0 ));
		polozeniePrawo = new FuzzySetLinear1( wierzcholki2 );
		//3
		ArrayList<Punkt2D> wierzcholki3 = new ArrayList<Punkt2D>();
		wierzcholki3.add(new Punkt2D( r, 0 ));
		wierzcholki3.add(new Punkt2D( (r + 0.5*(zeroZero.x + w - R - r)), 1 ));
		wierzcholki3.add(new Punkt2D( (zeroZero.x + w -R), 0 ));
		polozenieSrodek = new FuzzySetLinear1( wierzcholki3 );
		//4
		ArrayList<Punkt2D> wierzcholki4 = new ArrayList<Punkt2D>();
		wierzcholki4.add(new Punkt2D( zeroZero.x, 0 ));
		wierzcholki4.add(new Punkt2D( r, 1 ));
		wierzcholki4.add(new Punkt2D( (r + 0.5*(zeroZero.x + w - R - r)), 0 ));
		polozenieLewo = new FuzzySetLinear1( wierzcholki4 );
		//5
		ArrayList<Punkt2D> wierzcholki5 = new ArrayList<Punkt2D>();
		wierzcholki5.add(new Punkt2D( zeroZero.x, 1 ));
		wierzcholki5.add(new Punkt2D( r, 0 ));
		polozenieBardzoLewo = new FuzzySetLinear1( wierzcholki5 );
		//===========================================================
		//goradol
		//===========================================================
		//1
		ArrayList<Punkt2D> wierzcholki6 = new ArrayList<Punkt2D>();
		wierzcholki6.add( new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()), 1 ) ); //x wymiarow samochodu to jego mniejszy bok czyli szerokosc patrz klasa Car
		wierzcholki6.add( new Punkt2D( srodekParkingu.y, 0 ) );
		hWysoko = new FuzzySetLinear1( wierzcholki6 );
		//2
		ArrayList<Punkt2D> wierzcholki7 = new ArrayList<Punkt2D>();
		wierzcholki7.add(new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()), 0 ));
		wierzcholki7.add(new Punkt2D( srodekParkingu.y, 1 ));
		wierzcholki7.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()), 0 ));
		hSrednio = new FuzzySetLinear1( wierzcholki7 );
		//3
		ArrayList<Punkt2D> wierzcholki8 = new ArrayList<Punkt2D>();
		wierzcholki8.add(new Punkt2D( srodekParkingu.y, 0 ));
		wierzcholki8.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()), 1 ));
		hNisko = new FuzzySetLinear1( wierzcholki8 );
		//=============================================================
		//orientacja
		//=============================================================
		//1
		ArrayList<Punkt2D> wierzcholki9 = new ArrayList<Punkt2D>();
		wierzcholki9.add( new Punkt2D( 0, 1 ) );
		wierzcholki9.add( new Punkt2D( Math.PI/4, 0 ) );
		orientacjaBardzoPrawo = new FuzzySetLinear1( wierzcholki9 );
		//2
		ArrayList<Punkt2D> wierzcholki10 = new ArrayList<Punkt2D>();
		wierzcholki10.add(new Punkt2D( 0, 0 ));
		wierzcholki10.add(new Punkt2D( Math.PI/4, 1 ));
		wierzcholki10.add(new Punkt2D( Math.PI/2, 0 ));
		orientacjaPrawo = new FuzzySetLinear1( wierzcholki10 );
		//3
		ArrayList<Punkt2D> wierzcholki11 = new ArrayList<Punkt2D>();
		wierzcholki11.add(new Punkt2D( Math.PI/4, 0 ));
		wierzcholki11.add(new Punkt2D( Math.PI/2, 1 ));
		wierzcholki11.add(new Punkt2D( Math.PI*3/4.0, 0 ));
		orientacjaSrodek = new FuzzySetLinear1( wierzcholki11 );
		//4
		ArrayList<Punkt2D> wierzcholki12 = new ArrayList<Punkt2D>();
		wierzcholki12.add(new Punkt2D( Math.PI/2, 0 ));
		wierzcholki12.add(new Punkt2D( Math.PI*3/4.0, 1 ));
		wierzcholki12.add(new Punkt2D( Math.PI, 0 ));
		orientacjaLewo = new FuzzySetLinear1( wierzcholki12 );
		//5
		ArrayList<Punkt2D> wierzcholki13 = new ArrayList<Punkt2D>();
		wierzcholki13.add(new Punkt2D( Math.PI*3/4.0, 0 ));
		wierzcholki13.add(new Punkt2D( Math.PI, 1 ));
		orientacjaBardzoLewo = new FuzzySetLinear1( wierzcholki13 );
		//Zbiory na wyjsciu - konkluzje implikacji, narazie niech beda tylko 2 prawo i lewo
		//max skret na wyjsciu to bedzie +/- Math.Pi/45
		//1
		ArrayList<Punkt2D> wierzcholki14 = new ArrayList<Punkt2D>();
		wierzcholki14.add(new Punkt2D( 0, 0 ));
		wierzcholki14.add(new Punkt2D( Math.PI/45.0, 1 ));
		skretPrawo = new FuzzySetLinear1(wierzcholki14);
		//2
		ArrayList<Punkt2D> wierzcholki15 = new ArrayList<Punkt2D>();
		wierzcholki15.add(new Punkt2D( -Math.PI/45.0, 1 ));
		wierzcholki15.add(new Punkt2D( 0, 0 ));
		skretLewo = new FuzzySetLinear1(wierzcholki15);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------
		FuzzySetLinear1 polozenieBardzoPrawo;
		FuzzySetLinear1 polozeniePrawo;
		FuzzySetLinear1 polozenieSrodek;
		FuzzySetLinear1 polozenieLewo;
		FuzzySetLinear1 polozenieBardzoLewo;
		//goradol
		FuzzySetLinear1 hWysoko;
		FuzzySetLinear1 hSrednio;
		FuzzySetLinear1 hNisko;
		//orientacja = kat
		FuzzySetLinear1 orientacjaBardzoPrawo;
		FuzzySetLinear1 orientacjaPrawo;
		FuzzySetLinear1 orientacjaSrodek; //srodek znaczy tutaj poziomo i w prawo skierowane auto
		FuzzySetLinear1 orientacjaLewo;
		FuzzySetLinear1 orientacjaBardzoLewo;
		//Zbior regul ----------------------------------------------------------------------------------------------------------------------------------------
		//osobno dla skretu
		/*
		jezeli orientacja na prawo to skrec w lewo
		
		jezeli orientacja bardzo na prawo to skrec w lewo
		jezeli jestes bardzo na prawo i oeirntacja bardzo na lewo to skrec w prawo
		jezeli ejstes bardzo na prawo i orientacja na lewo to skrec w prawo
		jezeli jestes na prawo i wysoko to skrec w lewo
		jezeli jestes na prawo i srednio i orientacja bardzo lewo to skrec w prawo
		jezeli srednio i orientacja lewo to skrec w prawo
		
		//osobno dla ruchu
		jesli jestes bardzo na prawo to cofaj
		jesli jestes bardzo na lewo to jedz do przodu
		jezeli jestes srednio i orientacja bardzo lewo to jedz do przodu
		jezeli jestes nisko i orientacja lewo to jedz do przodu
		jezeli orientacja prawo to cofaj
		*/
		//----------------------------------------------------------------------------------------------------------------------------------------------------
	}
	
	
	public double[] podejmijDecyzje(Car c, boolean dotychczasDoPrzodu){
		polozenieSamochodu = new Point(c.polozenie.x, c.polozenie.y);
		orientacjaSamochodu = c.kierunek;
		//inicjalizacja wynikow dzialania metody
		double skretKol = 0;
		double przodTyl = 0;
		if(dotychczasDoPrzodu)
			przodTyl = 0;
		else
			przodTyl = -1;
		//ustalenie decyzji=================================
		
		
		
		//koniec ustalania decyzji =========================
		double[] result = new double[2];
		result[0] = skretKol;
		result[1] = przodTyl;
		return result;
	}
}
