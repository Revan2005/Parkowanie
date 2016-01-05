package regulyFuzzy2;

import java.awt.Point;
import java.util.ArrayList;

import parkowanie.Car;
import parkowanie.ModelOtoczenia;
import parkowanie.Punkt2D;
import reulyFuzzy1.FuzzySetLinear1;
import reulyFuzzy1.Przeslanka;
import reulyFuzzy1.Regula;

public class RegulyFuzzy2 {
	Point srodekParkingu;
	Point zeroZero;
	double r;
	double R;
	int w;
	int h;
	double orientacjaSamochodu;
	Point polozenieSamochodu;
	double PROG_PRZYNALEZNOSCI_WYMAGANEJ_DO_ZMIANY_ZWROTU = 0.01;
	
	double wspolczynnikSzerokosci = 0.03;
	
	//moje zbiory rozmyte typu 2
	//zwrot
	FuzzySetLinear2 zwrotNaprzod;
	FuzzySetLinear2 zwrotCofaj;
	//prawolewo
	FuzzySetLinear2 polozenieBardzoPrawo;
	FuzzySetLinear2 polozeniePrawo;
	FuzzySetLinear2 polozenieSrodek;
	FuzzySetLinear2 polozenieLewo;
	FuzzySetLinear2 polozenieBardzoLewo;
	//goradol
	FuzzySetLinear2 hWysoko;
	FuzzySetLinear2 hSrednio;
	FuzzySetLinear2 hNisko;
	//orientacja = kat
	FuzzySetLinear2 orientacjaBardzoPrawo;
	FuzzySetLinear2 orientacjaPrawo;
	FuzzySetLinear2 orientacjaSrodek; //srodek znaczy tutaj poziomo i w prawo skierowane auto
	FuzzySetLinear2 orientacjaLewo;
	FuzzySetLinear2 orientacjaBardzoLewo;
	
	//zbiory rozmyte dotyczace wyjscia - decyzji o skrecie
	FuzzySetLinear2 skrecPrawo;
	FuzzySetLinear2 skrecLewo;
	//dotyczace decyzji o zwrocie
	FuzzySetLinear2 jedzNaprzod;
	FuzzySetLinear2 cofaj;
	
	ArrayList<RegulaTypu2> listaRegul;
	
	public RegulyFuzzy2(Car c, ModelOtoczenia mO) {
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
		//zwrot
		//==============================================================
		//1
		ArrayList<Punkt2D> wierzcholki18 = new ArrayList<Punkt2D>();
		wierzcholki18.add( new Punkt2D( -0.5, 0 ) );
		wierzcholki18.add( new Punkt2D( 0, 1 ) );
		FuzzySetLinear1 zwrotNaprzodTyp1 = new FuzzySetLinear1( wierzcholki18, "zwrot" );
		zwrotNaprzod = new FuzzySetLinear2(zwrotNaprzodTyp1, zwrotNaprzodTyp1);
		//2
		ArrayList<Punkt2D> wierzcholki19 = new ArrayList<Punkt2D>();
		wierzcholki19.add( new Punkt2D( -0.5, 0 ) );
		wierzcholki19.add( new Punkt2D( -1, 1 ) );
		FuzzySetLinear1 zwrotCofajTyp1 = new FuzzySetLinear1( wierzcholki19, "zwrot" );
		zwrotCofaj = new FuzzySetLinear2(zwrotCofajTyp1, zwrotCofajTyp1);
		//==============================================================
		//prawolewo
		//==============================================================
		//1
		ArrayList<Punkt2D> wierzcholki1gora = new ArrayList<Punkt2D>();
		wierzcholki1gora.add( new Punkt2D( (zeroZero.x + w -R) - w*wspolczynnikSzerokosci, 0 ) );
		wierzcholki1gora.add( new Punkt2D( (zeroZero.x + w), 1 ) );
		FuzzySetLinear1 polozenieBardzoPrawoGora = new FuzzySetLinear1( wierzcholki1gora, "x" );
		ArrayList<Punkt2D> wierzcholki1dol = new ArrayList<Punkt2D>();
		wierzcholki1dol.add( new Punkt2D( (zeroZero.x + w -R) + w*wspolczynnikSzerokosci, 0 ) );
		wierzcholki1dol.add( new Punkt2D( (zeroZero.x + w), 1 ) );
		FuzzySetLinear1 polozenieBardzoPrawoDol = new FuzzySetLinear1( wierzcholki1dol, "x" );
		polozenieBardzoPrawo = new FuzzySetLinear2(polozenieBardzoPrawoDol, polozenieBardzoPrawoGora);
		//2
		ArrayList<Punkt2D> wierzcholki2gora = new ArrayList<Punkt2D>();
		wierzcholki2gora.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ) - w*wspolczynnikSzerokosci, 0 ));
		wierzcholki2gora.add(new Punkt2D( (zeroZero.x + w -R), 1 ));
		wierzcholki2gora.add(new Punkt2D( (zeroZero.x + w) + w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozeniePrawoGora = new FuzzySetLinear1( wierzcholki2gora, "x" );
		ArrayList<Punkt2D> wierzcholki2dol = new ArrayList<Punkt2D>();
		wierzcholki2dol.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ) + w*wspolczynnikSzerokosci, 0 ));
		wierzcholki2dol.add(new Punkt2D( (zeroZero.x + w -R), 1 ));
		wierzcholki2dol.add(new Punkt2D( (zeroZero.x + w) - w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozeniePrawoDol = new FuzzySetLinear1( wierzcholki2dol, "x" );
		polozeniePrawo = new FuzzySetLinear2(polozeniePrawoDol, polozeniePrawoGora);
		//3
		ArrayList<Punkt2D> wierzcholki3gora = new ArrayList<Punkt2D>();
		wierzcholki3gora.add(new Punkt2D( zeroZero.x + r - w*wspolczynnikSzerokosci, 0 ));
		wierzcholki3gora.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ), 1 ));
		wierzcholki3gora.add(new Punkt2D( (zeroZero.x + w -R) + w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozenieSrodekGora = new FuzzySetLinear1( wierzcholki3gora, "x" );
		ArrayList<Punkt2D> wierzcholki3dol = new ArrayList<Punkt2D>();
		wierzcholki3dol.add(new Punkt2D( zeroZero.x + r + w*wspolczynnikSzerokosci, 0 ));
		wierzcholki3dol.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ), 1 ));
		wierzcholki3dol.add(new Punkt2D( (zeroZero.x + w -R) - w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozenieSrodekDol = new FuzzySetLinear1( wierzcholki3dol, "x" );
		polozenieSrodek = new FuzzySetLinear2(polozenieSrodekDol, polozenieSrodekGora);
		//4
		ArrayList<Punkt2D> wierzcholki4gora = new ArrayList<Punkt2D>();
		wierzcholki4gora.add(new Punkt2D( zeroZero.x - w*wspolczynnikSzerokosci, 0 ));
		wierzcholki4gora.add(new Punkt2D( zeroZero.x + r, 1 ));
		wierzcholki4gora.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ) + w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozenieLewoGora = new FuzzySetLinear1( wierzcholki4gora, "x" );
		ArrayList<Punkt2D> wierzcholki4dol = new ArrayList<Punkt2D>();
		wierzcholki4dol.add(new Punkt2D( zeroZero.x + w*wspolczynnikSzerokosci, 0 ));
		wierzcholki4dol.add(new Punkt2D( zeroZero.x + r, 1 ));
		wierzcholki4dol.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ) - w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozenieLewoDol = new FuzzySetLinear1( wierzcholki4dol, "x" );
		polozenieLewo = new FuzzySetLinear2(polozenieLewoDol, polozenieLewoGora);
		//5
		ArrayList<Punkt2D> wierzcholki5gora = new ArrayList<Punkt2D>();
		wierzcholki5gora.add(new Punkt2D( zeroZero.x, 1 ));
		wierzcholki5gora.add(new Punkt2D( zeroZero.x + r + w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozenieBardzoLewoGora = new FuzzySetLinear1( wierzcholki5gora, "x" );
		ArrayList<Punkt2D> wierzcholki5dol = new ArrayList<Punkt2D>();
		wierzcholki5dol.add(new Punkt2D( zeroZero.x, 1 ));
		wierzcholki5dol.add(new Punkt2D( zeroZero.x + r - w*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 polozenieBardzoLewoDol = new FuzzySetLinear1( wierzcholki5dol, "x" );
		polozenieBardzoLewo = new FuzzySetLinear2(polozenieBardzoLewoDol, polozenieBardzoLewoGora);
		//===========================================================
		//goradol
		//===========================================================
		//1
		ArrayList<Punkt2D> wierzcholki6gora = new ArrayList<Punkt2D>();
		wierzcholki6gora.add( new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()), 1 ) ); //x wymiarow samochodu to jego mniejszy bok czyli szerokosc patrz klasa Car
		wierzcholki6gora.add( new Punkt2D( srodekParkingu.y + h*wspolczynnikSzerokosci, 0 ) );
		FuzzySetLinear1 hWysokoGora = new FuzzySetLinear1( wierzcholki6gora, "y" );
		ArrayList<Punkt2D> wierzcholki6dol = new ArrayList<Punkt2D>();
		wierzcholki6dol.add( new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()), 1 ) ); //x wymiarow samochodu to jego mniejszy bok czyli szerokosc patrz klasa Car
		wierzcholki6dol.add( new Punkt2D( srodekParkingu.y - h*wspolczynnikSzerokosci, 0 ) );
		FuzzySetLinear1 hWysokoDol = new FuzzySetLinear1( wierzcholki6dol, "y" );
		hWysoko = new FuzzySetLinear2(hWysokoDol, hWysokoGora);
		//2
		ArrayList<Punkt2D> wierzcholki7gora = new ArrayList<Punkt2D>();
		wierzcholki7gora.add(new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()) - h*wspolczynnikSzerokosci, 0 ));
		wierzcholki7gora.add(new Punkt2D( srodekParkingu.y, 1 ));
		wierzcholki7gora.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()) + h*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 hSrednioGora = new FuzzySetLinear1( wierzcholki7gora, "y" );
		ArrayList<Punkt2D> wierzcholki7dol = new ArrayList<Punkt2D>();
		wierzcholki7dol.add(new Punkt2D( (zeroZero.y - h - 0.5 * c.wymiary.getX()) + h*wspolczynnikSzerokosci, 0 ));
		wierzcholki7dol.add(new Punkt2D( srodekParkingu.y, 1 ));
		wierzcholki7dol.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()) - h*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 hSrednioDol = new FuzzySetLinear1( wierzcholki7dol, "y" );
		hSrednio = new FuzzySetLinear2(hSrednioDol, hSrednioGora);
		//3
		ArrayList<Punkt2D> wierzcholki8gora = new ArrayList<Punkt2D>();
		wierzcholki8gora.add(new Punkt2D( srodekParkingu.y - h*wspolczynnikSzerokosci, 0 ));
		wierzcholki8gora.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()), 1 ));
		FuzzySetLinear1 hNiskoGora = new FuzzySetLinear1( wierzcholki8gora, "y" );
		ArrayList<Punkt2D> wierzcholki8dol = new ArrayList<Punkt2D>();
		wierzcholki8dol.add(new Punkt2D( srodekParkingu.y + h*wspolczynnikSzerokosci, 0 ));
		wierzcholki8dol.add(new Punkt2D( (zeroZero.y - 0.5 * c.wymiary.getX()), 1 ));
		FuzzySetLinear1 hNiskoDol = new FuzzySetLinear1( wierzcholki8dol, "y" );
		hNisko = new FuzzySetLinear2(hNiskoDol, hNiskoGora);
		//=============================================================
		//orientacja
		//=============================================================
		//1
		ArrayList<Punkt2D> wierzcholki9gora = new ArrayList<Punkt2D>();
		wierzcholki9gora.add( new Punkt2D( 0, 1 ) );
		wierzcholki9gora.add( new Punkt2D( Math.PI/4 + Math.PI*wspolczynnikSzerokosci, 0 ) );
		FuzzySetLinear1 orientacjaBardzoLewoGora = new FuzzySetLinear1( wierzcholki9gora, "orientacja", "orientacjaBardzoLewo" );
		ArrayList<Punkt2D> wierzcholki9dol = new ArrayList<Punkt2D>();
		wierzcholki9dol.add( new Punkt2D( 0, 1 ) );
		wierzcholki9dol.add( new Punkt2D( Math.PI/4 - Math.PI*wspolczynnikSzerokosci, 0 ) );
		FuzzySetLinear1 orientacjaBardzoLewoDol = new FuzzySetLinear1( wierzcholki9dol, "orientacja", "orientacjaBardzoLewo" );
		orientacjaBardzoLewo = new FuzzySetLinear2(orientacjaBardzoLewoDol, orientacjaBardzoLewoGora);
		//2
		ArrayList<Punkt2D> wierzcholki10gora = new ArrayList<Punkt2D>();
		wierzcholki10gora.add(new Punkt2D( 0 - Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki10gora.add(new Punkt2D( Math.PI/4, 1 ));
		wierzcholki10gora.add(new Punkt2D( Math.PI/2 + Math.PI*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 orientacjaLewoGora = new FuzzySetLinear1( wierzcholki10gora, "orientacja", "orientacjaLewo" );
		ArrayList<Punkt2D> wierzcholki10dol = new ArrayList<Punkt2D>();
		wierzcholki10dol.add(new Punkt2D( 0 + Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki10dol.add(new Punkt2D( Math.PI/4, 1 ));
		wierzcholki10dol.add(new Punkt2D( Math.PI/2 - Math.PI*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 orientacjaLewoDol = new FuzzySetLinear1( wierzcholki10dol, "orientacja", "orientacjaLewo" );
		orientacjaLewo = new FuzzySetLinear2(orientacjaLewoDol, orientacjaLewoGora);
		//3
		ArrayList<Punkt2D> wierzcholki11gora = new ArrayList<Punkt2D>();
		wierzcholki11gora.add(new Punkt2D( Math.PI/4 - Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki11gora.add(new Punkt2D( Math.PI/2, 1 ));
		wierzcholki11gora.add(new Punkt2D( Math.PI*3/4.0 + Math.PI*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 orientacjaSrodekGora = new FuzzySetLinear1( wierzcholki11gora, "orientacja", "orientacjaSrodek" );
		ArrayList<Punkt2D> wierzcholki11dol = new ArrayList<Punkt2D>();
		wierzcholki11dol.add(new Punkt2D( Math.PI/4 + Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki11dol.add(new Punkt2D( Math.PI/2, 1 ));
		wierzcholki11dol.add(new Punkt2D( Math.PI*3/4.0 - Math.PI*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 orientacjaSrodekDol = new FuzzySetLinear1( wierzcholki11dol, "orientacja", "orientacjaSrodek" );
		orientacjaSrodek = new FuzzySetLinear2(orientacjaSrodekDol, orientacjaSrodekGora);
		//4
		ArrayList<Punkt2D> wierzcholki12gora = new ArrayList<Punkt2D>();
		wierzcholki12gora.add(new Punkt2D( Math.PI/2 - Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki12gora.add(new Punkt2D( Math.PI*3/4.0, 1 ));
		wierzcholki12gora.add(new Punkt2D( Math.PI + Math.PI*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 orientacjaPrawoGora = new FuzzySetLinear1( wierzcholki12gora, "orientacja", "orientacjaPrawo" );
		ArrayList<Punkt2D> wierzcholki12dol = new ArrayList<Punkt2D>();
		wierzcholki12dol.add(new Punkt2D( Math.PI/2 + Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki12dol.add(new Punkt2D( Math.PI*3/4.0, 1 ));
		wierzcholki12dol.add(new Punkt2D( Math.PI - Math.PI*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 orientacjaPrawoDol = new FuzzySetLinear1( wierzcholki12dol, "orientacja", "orientacjaPrawo" );
		orientacjaPrawo = new FuzzySetLinear2(orientacjaPrawoDol, orientacjaPrawoGora);
		//5
		ArrayList<Punkt2D> wierzcholki13gora = new ArrayList<Punkt2D>();
		wierzcholki13gora.add(new Punkt2D( Math.PI*3/4.0 - Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki13gora.add(new Punkt2D( Math.PI, 1 ));
		FuzzySetLinear1 orientacjaBardzoPrawoGora = new FuzzySetLinear1( wierzcholki13gora,"orientacja", "orientacjaBardzoPrawo" );
		ArrayList<Punkt2D> wierzcholki13dol = new ArrayList<Punkt2D>();
		wierzcholki13dol.add(new Punkt2D( Math.PI*3/4.0 + Math.PI*wspolczynnikSzerokosci, 0 ));
		wierzcholki13dol.add(new Punkt2D( Math.PI, 1 ));
		FuzzySetLinear1 orientacjaBardzoPrawoDol = new FuzzySetLinear1( wierzcholki13dol,"orientacja", "orientacjaBardzoPrawo" );
		orientacjaBardzoPrawo = new FuzzySetLinear2(orientacjaBardzoPrawoDol, orientacjaBardzoPrawoGora);
		//Zbiory na wyjsciu - konkluzje implikacji, narazie niech beda tylko 2 prawo i lewo
		//max skret na wyjsciu to bedzie +/- Math.Pi/45
		//1
		/*zmodyfikuje te zbiory zeby byly trojkatne a nie nieskonczone:
		ArrayList<Punkt2D> wierzcholki14gora = new ArrayList<Punkt2D>();
		wierzcholki14gora.add(new Punkt2D( 0 - (Math.PI/45.0)*wspolczynnikSzerokosci, 0 ));
		wierzcholki14gora.add(new Punkt2D( Math.PI/45.0, 1 ));
		FuzzySetLinear1 skrecPrawoGora = new FuzzySetLinear1(wierzcholki14gora, "skret", "skrecPrawo" );
		ArrayList<Punkt2D> wierzcholki14dol = new ArrayList<Punkt2D>();
		wierzcholki14dol.add(new Punkt2D( 0 + (Math.PI/45.0)*wspolczynnikSzerokosci, 0 ));
		wierzcholki14dol.add(new Punkt2D( Math.PI/45.0, 1 ));
		FuzzySetLinear1 skrecPrawoDol = new FuzzySetLinear1(wierzcholki14dol, "skret", "skrecPrawo" );
		skrecPrawo = new FuzzySetLinear2(skrecPrawoDol, skrecPrawoGora);
		//2
		ArrayList<Punkt2D> wierzcholki15gora = new ArrayList<Punkt2D>();
		wierzcholki15gora.add(new Punkt2D( -Math.PI/45.0, 1 ));
		wierzcholki15gora.add(new Punkt2D( 0 + (Math.PI/45.0)*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 skrecLewoGora = new FuzzySetLinear1(wierzcholki15gora, "skret", "skrecLewo" );
		ArrayList<Punkt2D> wierzcholki15dol = new ArrayList<Punkt2D>();
		wierzcholki15dol.add(new Punkt2D( -Math.PI/45.0, 1 ));
		wierzcholki15dol.add(new Punkt2D( 0 - (Math.PI/45.0)*wspolczynnikSzerokosci, 0 ));
		FuzzySetLinear1 skrecLewoDol = new FuzzySetLinear1(wierzcholki15dol, "skret", "skrecLewo" );
		skrecLewo = new FuzzySetLinear2(skrecLewoDol, skrecLewoGora);
		*/
		double wspolczynnikSzerokosciZbiorowWyjsciowych = 0.3;
		ArrayList<Punkt2D> wierzcholki14gora = new ArrayList<Punkt2D>();
		wierzcholki14gora.add(new Punkt2D( 0 - (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		wierzcholki14gora.add(new Punkt2D( Math.PI/45.0, 1 ));
		wierzcholki14gora.add(new Punkt2D( 2.0*Math.PI/45.0 + (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		FuzzySetLinear1 skrecPrawoGora = new FuzzySetLinear1(wierzcholki14gora, "skret", "skrecPrawo" );
		ArrayList<Punkt2D> wierzcholki14dol = new ArrayList<Punkt2D>();
		wierzcholki14dol.add(new Punkt2D( 0 + (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		wierzcholki14dol.add(new Punkt2D( Math.PI/45.0, 1 ));
		wierzcholki14dol.add(new Punkt2D( 2.0*Math.PI/45.0 - (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		FuzzySetLinear1 skrecPrawoDol = new FuzzySetLinear1(wierzcholki14dol, "skret", "skrecPrawo" );
		skrecPrawo = new FuzzySetLinear2(skrecPrawoDol, skrecPrawoGora);
		//2
		ArrayList<Punkt2D> wierzcholki15gora = new ArrayList<Punkt2D>();
		wierzcholki15gora.add(new Punkt2D( - 2.0*Math.PI/45.0 - (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		wierzcholki15gora.add(new Punkt2D( -Math.PI/45.0, 1 ));
		wierzcholki15gora.add(new Punkt2D( 0 + (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		FuzzySetLinear1 skrecLewoGora = new FuzzySetLinear1(wierzcholki15gora, "skret", "skrecLewo" );
		ArrayList<Punkt2D> wierzcholki15dol = new ArrayList<Punkt2D>();
		wierzcholki15dol.add(new Punkt2D( - 2.0*Math.PI/45.0 + (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		wierzcholki15dol.add(new Punkt2D( -Math.PI/45.0, 1 ));
		wierzcholki15dol.add(new Punkt2D( 0 - (Math.PI/45.0)*wspolczynnikSzerokosciZbiorowWyjsciowych, 0 ));
		FuzzySetLinear1 skrecLewoDol = new FuzzySetLinear1(wierzcholki15dol, "skret", "skrecLewo" );
		skrecLewo = new FuzzySetLinear2(skrecLewoDol, skrecLewoGora);
		//zbiory na wyjsciu dotyczace zwrotu przod tyl, -1 cofaj, 0 i wiecej - jedz do przodu
		//1
		ArrayList<Punkt2D> wierzcholki16 = new ArrayList<Punkt2D>();
		wierzcholki16.add(new Punkt2D( -1, 0 ));
		wierzcholki16.add(new Punkt2D( 0, 1 ));
		FuzzySetLinear1 jedzNaprzodTyp1 = new FuzzySetLinear1(wierzcholki16, "zwrot", "jedzNaprzod");
		jedzNaprzod = new FuzzySetLinear2(jedzNaprzodTyp1, jedzNaprzodTyp1);
		//2
		ArrayList<Punkt2D> wierzcholki17 = new ArrayList<Punkt2D>();
		wierzcholki17.add(new Punkt2D( -1, 1 ));
		wierzcholki17.add(new Punkt2D( 0, 0 ));
		FuzzySetLinear1 cofajTyp1 = new FuzzySetLinear1(wierzcholki17, "zwrot", "cofaj");
		cofaj = new FuzzySetLinear2(cofajTyp1, cofajTyp1);
		//----------------------------------------------------------------------------------------------------------------------------------------------------

		
		//REGULY =============================================================================================================================================
		listaRegul = new ArrayList<RegulaTypu2>();
		ArrayList<FuzzySetLinear2> listaZbiorowDoPrzeslanki;
		PrzeslankaTypu2 przeslanka;
		RegulaTypu2 regula;
		/*
		// 1-wsza regula
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		//listaZbiorowDoPrzeslanki.add(hWysoko);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		// 2-ga regula jesli jestes bardzo na prawo to cofaj 
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoPrawo);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, cofaj);
		listaRegul.add(regula);
		*/
		//DLA COFANIA
		//jesli jestes po prawej i wysoko to skrec kola w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		listaZbiorowDoPrzeslanki.add(hWysoko);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jesli jestes sredniowysoko to krec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jesli jestes sredniowysoko i po lewej to krec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(polozenieLewo);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jezeli orientacja lewo to odbij w przeciwna
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(orientacjaBardzoLewo);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//DLA JAZDY DO PRZODU
		//jesli jestes posrodku i orientacjalewo to skrec kola w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		listaZbiorowDoPrzeslanki.add(zwrotNaprzod);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//osobno dla ruchu
		//jesli jestes bardzo na prawo to cofaj 
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoPrawo);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, cofaj);
		listaRegul.add(regula);
		//jesli jestes bardzo na lewo to jedz do przodu 
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoLewo);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, jedzNaprzod);
		listaRegul.add(regula);
		//jezeli jestes nisko to jedz d oprzodu
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear2>();
		listaZbiorowDoPrzeslanki.add(hNisko);
		przeslanka = new PrzeslankaTypu2(listaZbiorowDoPrzeslanki);
		regula = new RegulaTypu2(przeslanka, jedzNaprzod);
		listaRegul.add(regula);
	
	}
	
	public double[] podejmijDecyzje(Car c, boolean czyDotychczasDoPrzodu){
		polozenieSamochodu = new Point(c.polozenie.x, c.polozenie.y);
		orientacjaSamochodu = c.kierunek;
		//inicjalizacja wynikow dzialania metody
		double skretKol = 0;
		double przodTyl;
		if(czyDotychczasDoPrzodu)
			przodTyl = 0;
		else 
			przodTyl = -1;
		//ustalenie decyzji=================================
		
		//po wszystkich regulach sprawdz czy sie zapalaja
		RegulaTypu2 r;
		ArrayList<FuzzySetLinear2> listaKonkluzji = new ArrayList<FuzzySetLinear2>();
		for(int i = 0; i < listaRegul.size(); i++){
			r = listaRegul.get(i);
			if(r.czySieZapala(polozenieSamochodu.x, polozenieSamochodu.y, orientacjaSamochodu, czyDotychczasDoPrzodu)){
				listaKonkluzji.add(r.getKonkluzjaPrzycieta(polozenieSamochodu.x, polozenieSamochodu.y, orientacjaSamochodu));
			}
		}
		//teraz robie OR na koncu bede mial zbior do defuzzyfikacji
		double[] konkluzjaKoncowa = { skretKol, przodTyl };
		if(listaKonkluzji.size() > 0){
			konkluzjaKoncowa = getKonkluzjaKoncowa(listaKonkluzji, czyDotychczasDoPrzodu);
		}
		
		return konkluzjaKoncowa;
	}
	
	//do poprawienia =========================================================================================================================================
	
	private double[] getKonkluzjaKoncowa(ArrayList<FuzzySetLinear2> listaKonkluzji, boolean czyDotychczasDoPrzodu){
		FuzzySetLinear2 konkluzjaKoncowaZwrot, konkluzjaKoncowaSkret;
		FuzzySetLinear2 konkluzjaTmp;
		ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychZwrotu = new ArrayList<FuzzySetLinear2>();
		ArrayList<FuzzySetLinear2>listaKonkluzjiDotyczacychSkretu = new ArrayList<FuzzySetLinear2>();
		for(int i = 0; i < listaKonkluzji.size(); i++){
			konkluzjaTmp = listaKonkluzji.get(i);
			if(konkluzjaTmp.zmienna.equals("zwrot"))
				listaKonkluzjiDotyczacychZwrotu.add(listaKonkluzji.get(i));
			if(konkluzjaTmp.zmienna.equals("skret"))
				listaKonkluzjiDotyczacychSkretu.add(listaKonkluzji.get(i));
		}
		konkluzjaKoncowaZwrot = getKonkluzjaDotyczacaZwrotu(listaKonkluzjiDotyczacychZwrotu);
		konkluzjaKoncowaSkret = getKonkluzjaDotyczacaSkretu(listaKonkluzjiDotyczacychSkretu);
		double[] konkluzjaKoncowa;
		konkluzjaKoncowa = defuzzyfication(konkluzjaKoncowaSkret, konkluzjaKoncowaZwrot, czyDotychczasDoPrzodu);

		return konkluzjaKoncowa;
	}
	
	private FuzzySetLinear2 getKonkluzjaDotyczacaZwrotu(ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychZwrotu){
		ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychZwrotuNaprzod = new ArrayList<FuzzySetLinear2>();
		ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychZwrotuCofaj = new ArrayList<FuzzySetLinear2>();
		FuzzySetLinear2 konkluzjaKoncowaZwrotPrzod = new FuzzySetLinear2();
		FuzzySetLinear2 konkluzjaKoncowaZwrotTyl = new FuzzySetLinear2();
		FuzzySetLinear2 konkluzjaTmp;
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
		FuzzySetLinear2 konkluzjaKoncowaKontynuujObecnyRuch = new FuzzySetLinear2();
		konkluzjaKoncowaKontynuujObecnyRuch.nazwa = "kontynuujObecnyRuch";
		System.out.println("przod: "+maxPrzynaleznosc(konkluzjaKoncowaZwrotPrzod)+
							" tyl: "+maxPrzynaleznosc(konkluzjaKoncowaZwrotTyl));
		if( (maxPrzynaleznosc(konkluzjaKoncowaZwrotPrzod) < PROG_PRZYNALEZNOSCI_WYMAGANEJ_DO_ZMIANY_ZWROTU) && (maxPrzynaleznosc(konkluzjaKoncowaZwrotTyl) < PROG_PRZYNALEZNOSCI_WYMAGANEJ_DO_ZMIANY_ZWROTU) ){
			return konkluzjaKoncowaKontynuujObecnyRuch;
		}
		if(maxPrzynaleznosc(konkluzjaKoncowaZwrotPrzod) > maxPrzynaleznosc(konkluzjaKoncowaZwrotTyl))
			return konkluzjaKoncowaZwrotPrzod;
		return konkluzjaKoncowaZwrotTyl;
	}
	
	private double maxPrzynaleznosc(FuzzySetLinear2 zbior){
		double max = 0;
		for(int i = 0; i < zbior.gornaFunkcjaPrzynaleznosci.wierzcholki.size(); i++){
			if(zbior.gornaFunkcjaPrzynaleznosci.wierzcholki.get(i).y > max)
				max = zbior.gornaFunkcjaPrzynaleznosci.wierzcholki.get(i).y;
		}
		return max;
	}
	
	private FuzzySetLinear2 getKonkluzjaDotyczacaSkretu(ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychSkretu){
		ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychSkretuPrawo = new ArrayList<FuzzySetLinear2>();
		ArrayList<FuzzySetLinear2> listaKonkluzjiDotyczacychSkretuLewo = new ArrayList<FuzzySetLinear2>();
		FuzzySetLinear2 konkluzjaKoncowaSkretPrawo = new FuzzySetLinear2();
		FuzzySetLinear2 konkluzjaKoncowaSkretLewo = new FuzzySetLinear2();
		FuzzySetLinear2 konkluzjaTmp;
		for(int i = 0; i < listaKonkluzjiDotyczacychSkretu.size(); i++){
			konkluzjaTmp = listaKonkluzjiDotyczacychSkretu.get(i);
			if(konkluzjaTmp.nazwa.equals("skrecPrawo"))
				listaKonkluzjiDotyczacychSkretuPrawo.add(listaKonkluzjiDotyczacychSkretu.get(i));
			if(konkluzjaTmp.nazwa.equals("skrecLewo"))
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
		
		
		if(maxPrzynaleznosc(konkluzjaKoncowaSkretPrawo) > maxPrzynaleznosc(konkluzjaKoncowaSkretLewo)){
			return konkluzjaKoncowaSkretPrawo;
		}
		return konkluzjaKoncowaSkretLewo;
	}
	
	private double[] defuzzyfication(FuzzySetLinear2 zbiorKierunek, FuzzySetLinear2 zbiorZwrot, boolean czyDotychczasDoPrzodu){
		double zwrot = 0, skret;
		if(zbiorZwrot.nazwa.equals("kontynuujObecnyRuch")){
			if(czyDotychczasDoPrzodu)
				zwrot = 0;
			else
				zwrot = -1;
		}
		if(zbiorZwrot.nazwa.equals("jedzNaprzod")){
			zwrot = 0;
		}
		if(zbiorZwrot.nazwa.equals("cofaj")){
			zwrot = -1;
		}
		skret = skretDefuzzyfication(zbiorKierunek);
		double[] result = { skret, zwrot };
		return result;
	}
	
	private double skretDefuzzyfication(FuzzySetLinear2 zbior){
		zbior.piszDoPlikow("dolnaFunkcjaPrzynaleznosci", "gornaFunkcjaPrzynaleznosci");
		//ten return trzeba zmienic teraz dziala tak jak pryz logice typu 1
		return getSkretZMaxPrzynaleznoscia(zbior.dolnaFunkcjaPrzynaleznosci);
		//return getSkretZMaxPrzynaleznoscia(zbior.gornaFunkcjaPrzynaleznosci);
		//return ( getSkretZMaxPrzynaleznoscia(zbior.gornaFunkcjaPrzynaleznosci) + getSkretZMaxPrzynaleznoscia(zbior.gornaFunkcjaPrzynaleznosci) ) / 2.0;

		//podejrzewam ze tak to bedzie dzialalo z symetrii
		/*
		if(zbior.nazwa.equals("skrecPrawo"))
			return Math.PI/45.0;
		if(zbior.nazwa.equals("skrecLewo"))
			return Math.PI/45.0;
		return 0;
		*/
		//tu sie odbedzie cala wyprawa -redukcja typu i obliczanie centroidu przedzialu
		//roznica pojawia sie wg slajdow dopiero na tym etapie do tej pory 
		//szly 2 rownolegle systemy typu 1 (kazdy moj zbior rozmyty typu 2 sklada sie z 2 zbiorow typu 1 - gornej i dolnej fkcji przynaleznosci)
		
		
		
		
		
	}
	
	//@to nizej jest do wyrzucenia!!!!
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
	
	
	//koniec ================================================================================================================================================

}
