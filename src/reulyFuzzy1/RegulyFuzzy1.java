package reulyFuzzy1;
import parkowanie.*;
import java.awt.Point;
import java.util.ArrayList;

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
	FuzzySetLinear1 skrecPrawo;
	FuzzySetLinear1 skrecLewo;
	//dotyczace decyzji o zwrocie
	FuzzySetLinear1 jedzNaprzod;
	FuzzySetLinear1 cofaj;
	
	ArrayList<Regula> listaRegul;
	
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
		polozenieBardzoPrawo = new FuzzySetLinear1( wierzcholki1, "x" );
		//2
		ArrayList<Punkt2D> wierzcholki2 = new ArrayList<Punkt2D>();
		wierzcholki2.add(new Punkt2D( (r + 0.5*(zeroZero.x + w - R - r)), 0 ));
		wierzcholki2.add(new Punkt2D( (zeroZero.x + w -R), 1 ));
		wierzcholki2.add(new Punkt2D( (zeroZero.x + w), 0 ));
		polozeniePrawo = new FuzzySetLinear1( wierzcholki2, "x" );
		//3
		ArrayList<Punkt2D> wierzcholki3 = new ArrayList<Punkt2D>();
		wierzcholki3.add(new Punkt2D( r, 0 ));
		wierzcholki3.add(new Punkt2D( (r + 0.5*(zeroZero.x + w - R - r)), 1 ));
		wierzcholki3.add(new Punkt2D( (zeroZero.x + w -R), 0 ));
		polozenieSrodek = new FuzzySetLinear1( wierzcholki3, "x" );
		//4
		ArrayList<Punkt2D> wierzcholki4 = new ArrayList<Punkt2D>();
		wierzcholki4.add(new Punkt2D( zeroZero.x, 0 ));
		wierzcholki4.add(new Punkt2D( r, 1 ));
		wierzcholki4.add(new Punkt2D( (r + 0.5*(zeroZero.x + w - R - r)), 0 ));
		polozenieLewo = new FuzzySetLinear1( wierzcholki4, "x" );
		//5
		ArrayList<Punkt2D> wierzcholki5 = new ArrayList<Punkt2D>();
		wierzcholki5.add(new Punkt2D( zeroZero.x, 1 ));
		wierzcholki5.add(new Punkt2D( r, 0 ));
		polozenieBardzoLewo = new FuzzySetLinear1( wierzcholki5, "x" );
		//===========================================================
		//goradol
		//===========================================================
		//1
		ArrayList<Punkt2D> wierzcholki6 = new ArrayList<Punkt2D>();
		wierzcholki6.add( new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()), 1 ) ); //x wymiarow samochodu to jego mniejszy bok czyli szerokosc patrz klasa Car
		wierzcholki6.add( new Punkt2D( srodekParkingu.y, 0 ) );
		hWysoko = new FuzzySetLinear1( wierzcholki6, "y" );
		//2
		ArrayList<Punkt2D> wierzcholki7 = new ArrayList<Punkt2D>();
		wierzcholki7.add(new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()), 0 ));
		wierzcholki7.add(new Punkt2D( srodekParkingu.y, 1 ));
		wierzcholki7.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()), 0 ));
		hSrednio = new FuzzySetLinear1( wierzcholki7, "y" );
		//3
		ArrayList<Punkt2D> wierzcholki8 = new ArrayList<Punkt2D>();
		wierzcholki8.add(new Punkt2D( srodekParkingu.y, 0 ));
		wierzcholki8.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()), 1 ));
		hNisko = new FuzzySetLinear1( wierzcholki8, "y" );
		//=============================================================
		//orientacja
		//=============================================================
		//1
		ArrayList<Punkt2D> wierzcholki9 = new ArrayList<Punkt2D>();
		wierzcholki9.add( new Punkt2D( 0, 1 ) );
		wierzcholki9.add( new Punkt2D( Math.PI/4, 0 ) );
		orientacjaBardzoPrawo = new FuzzySetLinear1( wierzcholki9, "orientacja", "orientacjaBardzoPrawo" );
		//2
		ArrayList<Punkt2D> wierzcholki10 = new ArrayList<Punkt2D>();
		wierzcholki10.add(new Punkt2D( 0, 0 ));
		wierzcholki10.add(new Punkt2D( Math.PI/4, 1 ));
		wierzcholki10.add(new Punkt2D( Math.PI/2, 0 ));
		orientacjaPrawo = new FuzzySetLinear1( wierzcholki10, "orientacja", "orientacjaPrawo" );
		//3
		ArrayList<Punkt2D> wierzcholki11 = new ArrayList<Punkt2D>();
		wierzcholki11.add(new Punkt2D( Math.PI/4, 0 ));
		wierzcholki11.add(new Punkt2D( Math.PI/2, 1 ));
		wierzcholki11.add(new Punkt2D( Math.PI*3/4.0, 0 ));
		orientacjaSrodek = new FuzzySetLinear1( wierzcholki11, "orientacja", "orientacjaSrodek" );
		//4
		ArrayList<Punkt2D> wierzcholki12 = new ArrayList<Punkt2D>();
		wierzcholki12.add(new Punkt2D( Math.PI/2, 0 ));
		wierzcholki12.add(new Punkt2D( Math.PI*3/4.0, 1 ));
		wierzcholki12.add(new Punkt2D( Math.PI, 0 ));
		orientacjaLewo = new FuzzySetLinear1( wierzcholki12, "orientacja", "orientacjaLewo" );
		//5
		ArrayList<Punkt2D> wierzcholki13 = new ArrayList<Punkt2D>();
		wierzcholki13.add(new Punkt2D( Math.PI*3/4.0, 0 ));
		wierzcholki13.add(new Punkt2D( Math.PI, 1 ));
		orientacjaBardzoLewo = new FuzzySetLinear1( wierzcholki13,"orientacja", "orientacjaBardzoLewo" );
		//Zbiory na wyjsciu - konkluzje implikacji, narazie niech beda tylko 2 prawo i lewo
		//max skret na wyjsciu to bedzie +/- Math.Pi/45
		//1
		ArrayList<Punkt2D> wierzcholki14 = new ArrayList<Punkt2D>();
		wierzcholki14.add(new Punkt2D( -Math.PI/45.0, 0 ));
		wierzcholki14.add(new Punkt2D( Math.PI/45.0, 1 ));
		skrecPrawo = new FuzzySetLinear1(wierzcholki14,"orientacja", "skrecPrawo" );
		//2
		ArrayList<Punkt2D> wierzcholki15 = new ArrayList<Punkt2D>();
		wierzcholki15.add(new Punkt2D( -Math.PI/45.0, 1 ));
		wierzcholki15.add(new Punkt2D( Math.PI/45.0, 0 ));
		skrecLewo = new FuzzySetLinear1(wierzcholki15,"orientacja", "skrecLewo" );
		//zbiory na wyjsciu dotyczace zwrotu przod tyl, -1 cofaj, 0 i wiecej - jedz do przodu
		//1
		ArrayList<Punkt2D> wierzcholki16 = new ArrayList<Punkt2D>();
		wierzcholki16.add(new Punkt2D( -1, 0 ));
		wierzcholki16.add(new Punkt2D( 0, 1 ));
		jedzNaprzod = new FuzzySetLinear1(wierzcholki16, "zwrot", "jedzNaprzod");
		//2
		ArrayList<Punkt2D> wierzcholki17 = new ArrayList<Punkt2D>();
		wierzcholki17.add(new Punkt2D( -1, 1 ));
		wierzcholki17.add(new Punkt2D( 0, 0 ));
		cofaj = new FuzzySetLinear1(wierzcholki17, "zwrot", "cofaj");
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------
		/*FuzzySetLinear1 polozenieBardzoPrawo;
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
		FuzzySetLinear1 orientacjaBardzoLewo;*/
		//Zbior regul ----------------------------------------------------------------------------------------------------------------------------------------
		listaRegul = new ArrayList<Regula>();
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
		 * 
		 */
		//jesli jestes bardzo na prawo to cofaj
		ArrayList<FuzzySetLinear1> listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoPrawo);
		Przeslanka przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		Regula regula = new Regula(przeslanka, cofaj);
		listaRegul.add(regula);
		/*
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
		
		//po wszystkich regulach sprawdz czy sie zapalaja
		Regula r;
		ArrayList<FuzzySetLinear1> listaKonkluzji = new ArrayList<FuzzySetLinear1>();
		for(int i = 0; i < listaRegul.size(); i++){
			r = listaRegul.get(i);
			if(r.czySieZapala(polozenieSamochodu.x, polozenieSamochodu.y, orientacjaSamochodu)){
				listaKonkluzji.add(r.getKonkluzjaPrzycieta(polozenieSamochodu.x, polozenieSamochodu.y, orientacjaSamochodu));
			}
		}
		//teraz robie OR na koncu bede mial zbior do defuzzyfikacji
		double[] konkluzjaKoncowa = { 0, 0 };
		if(listaKonkluzji.size() > 0){
			konkluzjaKoncowa = getKonkluzjaKoncowa(listaKonkluzji);
		}
		System.out.println("skret kol = "+konkluzjaKoncowa[0]+"  przodtyl = "+konkluzjaKoncowa[1]);
		//koniec ustalania decyzji =========================
		/*double[] result = new double[2];
		result[0] = skretKol;
		result[1] = przodTyl;
		return result;*/
		return konkluzjaKoncowa;
	}
	
	private double[] getKonkluzjaKoncowa(ArrayList<FuzzySetLinear1> listaKonkluzji){
		FuzzySetLinear1 konkluzjaKoncowaZwrot, konkluzjaKoncowaSkret;
		FuzzySetLinear1 konkluzjaTmp;
		ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychZwrotu = new ArrayList<FuzzySetLinear1>();
		ArrayList<FuzzySetLinear1>listaKonkluzjiDotyczacychSkretu = new ArrayList<FuzzySetLinear1>();
		for(int i = 0; i < listaKonkluzji.size(); i++){
			konkluzjaTmp = listaKonkluzji.get(i);
			if(konkluzjaTmp.zmienna.equals("zwrot"))
				listaKonkluzjiDotyczacychZwrotu.add(listaKonkluzji.get(i));
			if(konkluzjaTmp.zmienna.equals("kierunek"))
				listaKonkluzjiDotyczacychSkretu.add(listaKonkluzji.get(i));
		}
		konkluzjaKoncowaZwrot = getKonkluzjaDotyczacaZwrotu(listaKonkluzjiDotyczacychZwrotu);
		konkluzjaKoncowaSkret = getKonkluzjaDotyczacaSkretu(listaKonkluzjiDotyczacychSkretu);
		
		
		double[] konkluzjaKoncowa;
		konkluzjaKoncowa = defuzzyfication(konkluzjaKoncowaSkret, konkluzjaKoncowaZwrot);

		
		return konkluzjaKoncowa;
	}
	
	private FuzzySetLinear1 getKonkluzjaDotyczacaZwrotu(ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychZwrotu){
		ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychZwrotuNaprzod = new ArrayList<FuzzySetLinear1>();
		ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychZwrotuCofaj = new ArrayList<FuzzySetLinear1>();
		FuzzySetLinear1 konkluzjaKoncowaZwrotPrzod = new FuzzySetLinear1();
		FuzzySetLinear1 konkluzjaKoncowaZwrotTyl = new FuzzySetLinear1();
		FuzzySetLinear1 konkluzjaTmp;
		for(int i = 0; i < listaKonkluzjiDotyczacychZwrotu.size(); i++){
			konkluzjaTmp = listaKonkluzjiDotyczacychZwrotu.get(i);
			if(konkluzjaTmp.nazwa.equals("jedzNaprzod"))
				listaKonkluzjiDotyczacychZwrotuNaprzod.add(listaKonkluzjiDotyczacychZwrotu.get(i));
			if(konkluzjaTmp.nazwa.equals("cofaj"))
				listaKonkluzjiDotyczacychZwrotuCofaj.add(listaKonkluzjiDotyczacychZwrotu.get(i));
		}
		if(listaKonkluzjiDotyczacychZwrotuNaprzod.size() > 0)
			konkluzjaKoncowaZwrotPrzod = listaKonkluzjiDotyczacychZwrotuNaprzod.get(0);
		for(int i = 1; i < listaKonkluzjiDotyczacychZwrotuNaprzod.size(); i++)
			konkluzjaKoncowaZwrotPrzod = konkluzjaKoncowaZwrotPrzod.or(listaKonkluzjiDotyczacychZwrotuNaprzod.get(i));
		
		if(listaKonkluzjiDotyczacychZwrotuCofaj.size() > 0)
			konkluzjaKoncowaZwrotTyl = listaKonkluzjiDotyczacychZwrotuCofaj.get(0);
		for(int i = 1; i < listaKonkluzjiDotyczacychZwrotuCofaj.size(); i++)
			konkluzjaKoncowaZwrotTyl = konkluzjaKoncowaZwrotTyl.or(listaKonkluzjiDotyczacychZwrotuCofaj.get(i));
		
		if(maxPrzynaleznosc(konkluzjaKoncowaZwrotPrzod) > maxPrzynaleznosc(konkluzjaKoncowaZwrotTyl))
			return konkluzjaKoncowaZwrotPrzod;
		return konkluzjaKoncowaZwrotTyl;
		
	}
	
	private double maxPrzynaleznosc(FuzzySetLinear1 zbior){
		double max = 0;
		for(int i = 0; i < zbior.wierzcholki.size(); i++){
			if(zbior.wierzcholki.get(i).y > max)
				max = zbior.wierzcholki.get(i).y;
		}
		return max;
	}
	
	private FuzzySetLinear1 getKonkluzjaDotyczacaSkretu(ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychSkretu){
		ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychSkretuPrawo = new ArrayList<FuzzySetLinear1>();
		ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychSkretuLewo = new ArrayList<FuzzySetLinear1>();
		FuzzySetLinear1 konkluzjaKoncowaSkretPrawo = new FuzzySetLinear1();
		FuzzySetLinear1 konkluzjaKoncowaSkretLewo = new FuzzySetLinear1();
		FuzzySetLinear1 konkluzjaTmp;
		for(int i = 0; i < listaKonkluzjiDotyczacychSkretu.size(); i++){
			konkluzjaTmp = listaKonkluzjiDotyczacychSkretu.get(i);
			if(konkluzjaTmp.nazwa.equals("skrecPrawo"))
				listaKonkluzjiDotyczacychSkretuPrawo.add(listaKonkluzjiDotyczacychSkretu.get(i));
			if(konkluzjaTmp.nazwa.equals("SkrecLewo"))
				listaKonkluzjiDotyczacychSkretuLewo.add(listaKonkluzjiDotyczacychSkretu.get(i));
		}
		if(listaKonkluzjiDotyczacychSkretuPrawo.size() > 0)
			konkluzjaKoncowaSkretPrawo = listaKonkluzjiDotyczacychSkretuPrawo.get(0);
		for(int i = 1; i < listaKonkluzjiDotyczacychSkretuPrawo.size(); i++)
			konkluzjaKoncowaSkretPrawo = konkluzjaKoncowaSkretPrawo.or(listaKonkluzjiDotyczacychSkretuPrawo.get(i));
		
		if(listaKonkluzjiDotyczacychSkretuLewo.size() > 0)
			konkluzjaKoncowaSkretLewo = listaKonkluzjiDotyczacychSkretuLewo.get(0);
		for(int i = 1; i < listaKonkluzjiDotyczacychSkretuLewo.size(); i++)
			konkluzjaKoncowaSkretLewo = konkluzjaKoncowaSkretLewo.or(listaKonkluzjiDotyczacychSkretuLewo.get(i));
		
		if(maxPrzynaleznosc(konkluzjaKoncowaSkretPrawo) > maxPrzynaleznosc(konkluzjaKoncowaSkretLewo))
			return konkluzjaKoncowaSkretPrawo;
		return konkluzjaKoncowaSkretLewo;
	}
	
	private double[] defuzzyfication(FuzzySetLinear1 zbiorKierunek, FuzzySetLinear1 zbiorZwrot){
		double zwrot = 0, skret;
		if(zbiorZwrot.nazwa.equals("jedzNaprzod"))
			zwrot = 0;
		if(zbiorZwrot.nazwa.equals("cofaj"))
			zwrot = -1;
		skret = getSkretZMaxPrzynaleznoscia(zbiorKierunek);
		double[] result = { skret, zwrot };
		return result;
	}
	
	private double getSkretZMaxPrzynaleznoscia(FuzzySetLinear1 zbior){
		double maxPrzynaleznosc = 0 , skret = 0;
		for(int i = 0; i < zbior.wierzcholki.size(); i++){
			if(zbior.wierzcholki.get(i).y > maxPrzynaleznosc){
				maxPrzynaleznosc = zbior.wierzcholki.get(i).y;
				skret = zbior.wierzcholki.get(i).x;
			}
		}
		return skret;
	}

}


