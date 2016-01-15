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
	double PROG_PRZYNALEZNOSCI_WYMAGANEJ_DO_ZMIANY_ZWROTU = 0.01;
	
	//moje zbiory rozmyte
	//zwrot
	FuzzySetLinear1 zwrotNaprzod;
	FuzzySetLinear1 zwrotCofaj;
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
		//zwrot
		//==============================================================
		//1
		ArrayList<Punkt2D> wierzcholki18 = new ArrayList<Punkt2D>();
		wierzcholki18.add( new Punkt2D( -0.5, 0 ) );
		wierzcholki18.add( new Punkt2D( 0, 1 ) );
		zwrotNaprzod = new FuzzySetLinear1( wierzcholki18, "zwrot" );
		//2
		ArrayList<Punkt2D> wierzcholki19 = new ArrayList<Punkt2D>();
		wierzcholki19.add( new Punkt2D( -0.5, 0 ) );
		wierzcholki19.add( new Punkt2D( -1, 1 ) );
		zwrotCofaj = new FuzzySetLinear1( wierzcholki19, "zwrot" );
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
		wierzcholki2.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ), 0 ));
		wierzcholki2.add(new Punkt2D( (zeroZero.x + w -R), 1 ));
		wierzcholki2.add(new Punkt2D( (zeroZero.x + w), 0 ));
		polozeniePrawo = new FuzzySetLinear1( wierzcholki2, "x" );
		//3
		ArrayList<Punkt2D> wierzcholki3 = new ArrayList<Punkt2D>();
		wierzcholki3.add(new Punkt2D( zeroZero.x + r, 0 ));
		wierzcholki3.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ), 1 ));
		wierzcholki3.add(new Punkt2D( (zeroZero.x + w -R), 0 ));
		polozenieSrodek = new FuzzySetLinear1( wierzcholki3, "x" );
		//4
		ArrayList<Punkt2D> wierzcholki4 = new ArrayList<Punkt2D>();
		wierzcholki4.add(new Punkt2D( zeroZero.x, 0 ));
		wierzcholki4.add(new Punkt2D( zeroZero.x + r, 1 ));
		wierzcholki4.add(new Punkt2D( ( zeroZero.x + 0.5*w - (c.odleglosc_od_przodu_do_osi - c.wymiary.getY()/2) ), 0 ));
		polozenieLewo = new FuzzySetLinear1( wierzcholki4, "x" );		
		//5
		ArrayList<Punkt2D> wierzcholki5 = new ArrayList<Punkt2D>();
		wierzcholki5.add(new Punkt2D( zeroZero.x, 1 ));
		wierzcholki5.add(new Punkt2D( zeroZero.x + r, 0 ));
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
		orientacjaBardzoLewo = new FuzzySetLinear1( wierzcholki9, "orientacja", "orientacjaBardzoLewo" );
		//2
		ArrayList<Punkt2D> wierzcholki10 = new ArrayList<Punkt2D>();
		wierzcholki10.add(new Punkt2D( 0, 0 ));
		wierzcholki10.add(new Punkt2D( Math.PI/4, 1 ));
		wierzcholki10.add(new Punkt2D( Math.PI/2, 0 ));
		orientacjaLewo = new FuzzySetLinear1( wierzcholki10, "orientacja", "orientacjaLewo" );
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
		orientacjaPrawo = new FuzzySetLinear1( wierzcholki12, "orientacja", "orientacjaPrawo" );
		//5
		ArrayList<Punkt2D> wierzcholki13 = new ArrayList<Punkt2D>();
		wierzcholki13.add(new Punkt2D( Math.PI*3/4.0, 0 ));
		wierzcholki13.add(new Punkt2D( Math.PI, 1 ));
		orientacjaBardzoPrawo = new FuzzySetLinear1( wierzcholki13,"orientacja", "orientacjaBardzoPrawo" );
		//Zbiory na wyjsciu - konkluzje implikacji, narazie niech beda tylko 2 prawo i lewo
		//max skret na wyjsciu to bedzie +/- Math.Pi/45
		//zamieniam zeby bylo tak jak pry rozmytych typu 2 tzn zbior prawo nie idzie od zera do plus infinity tylko od zera przez max w punkcie
		//pi/45 i maleje do 2pi/45
		//1
		final double MAX_SKRET = Math.PI/90.0;
		ArrayList<Punkt2D> wierzcholki14 = new ArrayList<Punkt2D>();
		wierzcholki14.add(new Punkt2D( 0, 0 ));
		wierzcholki14.add(new Punkt2D( MAX_SKRET, 1 ));
		wierzcholki14.add(new Punkt2D( 2.0*MAX_SKRET, 0 )); //dopisane
		skrecPrawo = new FuzzySetLinear1(wierzcholki14, "skret", "skrecPrawo" );
		//2
		ArrayList<Punkt2D> wierzcholki15 = new ArrayList<Punkt2D>();
		wierzcholki15.add(new Punkt2D( -2.0*MAX_SKRET, 0 )); //dopisane
		wierzcholki15.add(new Punkt2D( -MAX_SKRET, 1 ));
		wierzcholki15.add(new Punkt2D( 0, 0 ));
		skrecLewo = new FuzzySetLinear1(wierzcholki15, "skret", "skrecLewo" );
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
		ArrayList<FuzzySetLinear1> listaZbiorowDoPrzeslanki;
		Przeslanka przeslanka;
		Regula regula;
		/*
		//osobno dla skretu
		//jezeli jestes wyosko to skrec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hWysoko);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jezeli orientacja na prawo to skrec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(orientacjaPrawo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jezeli orientacja bardzo na lewo to skrec w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(orientacjaBardzoLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		*/
		/*
		//jezeli orientacja na prawo to skrec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(orientacjaPrawo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jezeli jestes bardzo na prawo i orientacja lewo to skrec w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoPrawo);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jezeli jestes na prawo i srednio i orientacja lewo to skrec w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jezeli srednio i orientacja lewo to skrec w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		*/
		//============================================================================================================
		//DLA COFANIA
		//jesli jestes po prawej i wysoko to skrec kola w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		listaZbiorowDoPrzeslanki.add(hWysoko);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jesli jestes sredniowysoko to krec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jesli jestes sredniowysoko i po lewej to krec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(polozenieLewo);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jezeli orientacja lewo to odbij w przeciwna
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(orientacjaBardzoLewo);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		/*
		//jesli jestes na prawo i orientacja prosto to skrec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		listaZbiorowDoPrzeslanki.add(orientacjaSrodek);
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		*/
		//DLA JAZDY DO PRZODU
		//jesli jestes posrodku i orientacjalewo to skrec kola w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		listaZbiorowDoPrzeslanki.add(zwrotNaprzod);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		/*
		//jesli jestes sredniowysoko to krec w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		listaZbiorowDoPrzeslanki.add(zwrotNaprzod);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jesli jestes sredniowysoko i po lewej to krec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(polozenieLewo);
		listaZbiorowDoPrzeslanki.add(zwrotNaprzod);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jesli jestes na prawo i orientacja prosto to skrec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		listaZbiorowDoPrzeslanki.add(orientacjaSrodek);
		listaZbiorowDoPrzeslanki.add(zwrotNaprzod);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		*/
		//=======================================================================================================================
		//osobno dla ruchu
		//jesli jestes bardzo na prawo to cofaj 
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoPrawo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, cofaj);
		listaRegul.add(regula);
		//jesli jestes bardzo na lewo to jedz do przodu 
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, jedzNaprzod);
		listaRegul.add(regula);
		//jezeli jestes nisko to jedz d oprzodu
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hNisko);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, jedzNaprzod);
		listaRegul.add(regula);
		/*
		//test nowych skretow i zbiorow naprzod cofaj
		//jezeli cofasz to skrec w prawo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(zwrotNaprzod);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jezeli jedziesz naprzod to skrec w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(zwrotCofaj);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		*/
		
		
		//jezeli jestes nisko to skres kola w prawo
		//listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		//listaZbiorowDoPrzeslanki.add(hNisko);
		//przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		//regula = new Regula(przeslanka, skrecPrawo);
		//listaRegul.add(regula);
		/*
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieBardzoPrawo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecPrawo);
		listaRegul.add(regula);
		//jesli jestes na prawo to cofaj i skrec kola w prawo
		//listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		//listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		//przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		//regula = new Regula(przeslanka, cofaj);
		//listaRegul.add(regula);
		//listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		//listaZbiorowDoPrzeslanki.add(polozeniePrawo);
		//przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		//regula = new Regula(przeslanka, skrecLewo);
		//listaRegul.add(regula);
		//jesli jestes na srodku to cofaj i skrec kola w lewo
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, cofaj);
		listaRegul.add(regula);
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(polozenieSrodek);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		//jezeli wysokosc srednio to odbijaj
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, skrecLewo);
		listaRegul.add(regula);
		*/
		/*
		//jezeli jestes srednio wysoko i orientacja bardzo lewo to jedz do przodu
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hSrednio);
		listaZbiorowDoPrzeslanki.add(orientacjaBardzoLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, jedzNaprzod);
		listaRegul.add(regula);
		//jezeli jestes nisko i orientacja lewo to jedz do przodu
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(hNisko);
		listaZbiorowDoPrzeslanki.add(orientacjaLewo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, jedzNaprzod);
		listaRegul.add(regula);
		//jezeli orientacja prawo to cofaj
		listaZbiorowDoPrzeslanki = new ArrayList<FuzzySetLinear1>();
		listaZbiorowDoPrzeslanki.add(orientacjaPrawo);
		przeslanka = new Przeslanka(listaZbiorowDoPrzeslanki);
		regula = new Regula(przeslanka, cofaj);
		listaRegul.add(regula);
		*/
		//----------------------------------------------------------------------------------------------------------------------------------------------------
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
		Regula r;
		ArrayList<FuzzySetLinear1> listaKonkluzji = new ArrayList<FuzzySetLinear1>();
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
		//System.out.println("skret kol = "+konkluzjaKoncowa[0]+"  przodtyl = "+konkluzjaKoncowa[1]);
		//for(int i = 1; i < 2; i++)
			//System.out.println("Regula "+i+" czy sie zapala : "+listaRegul.get(i).czySieZapala(c.polozenie.x, c.polozenie.y, c.kierunek)+
				//"  kierunek = "+c.kierunek+"  zmienna = "+listaRegul.get(i).przeslanka.zbiory.get(0).zmienna);
		//koniec ustalania decyzji =========================
		/*double[] result = new double[2];
		result[0] = skretKol;
		result[1] = przodTyl;
		return result;*/
		//Odwracam skret jesli auto jedzie do przodu
		//konkluzjaKoncowa = odwrocSkretGdyJedzieszDoPrzodu(konkluzjaKoncowa);
		
		return konkluzjaKoncowa;
	}
	
/*	private double[] odwrocSkretGdyJedzieszDoPrzodu(double[] konkluzja){
		double[] konkluzjaKoncowa = new double[2];
		konkluzjaKoncowa[0] = konkluzja[0];
		if(konkluzja[0] == 0)
			konkluzjaKoncowa[1] = -konkluzja[1];
		else 
			konkluzjaKoncowa[1] = konkluzja[1];
		return konkluzjaKoncowa;
	}
*/	
	private double[] getKonkluzjaKoncowa(ArrayList<FuzzySetLinear1> listaKonkluzji, boolean czyDotychczasDoPrzodu){
		FuzzySetLinear1 konkluzjaKoncowaZwrot, konkluzjaKoncowaSkret;
		FuzzySetLinear1 konkluzjaTmp;
		ArrayList<FuzzySetLinear1> listaKonkluzjiDotyczacychZwrotu = new ArrayList<FuzzySetLinear1>();
		ArrayList<FuzzySetLinear1>listaKonkluzjiDotyczacychSkretu = new ArrayList<FuzzySetLinear1>();
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
		FuzzySetLinear1 konkluzjaKoncowaKontynuujObecnyRuch = new FuzzySetLinear1();
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
	
	private double[] defuzzyfication(FuzzySetLinear1 zbiorKierunek, FuzzySetLinear1 zbiorZwrot, boolean czyDotychczasDoPrzodu){
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
		zbiorKierunek.piszDoPliku("regulyTypu1Skret");
		if(zbiorKierunek.nazwa.equals(""))
			skret = 0;
		else
			skret = skretCentreOfGravityDefuzzyfication(zbiorKierunek);
		double[] result = { skret, zwrot };
		return result;
	}
	
	private double skretCentreOfGravityDefuzzyfication(FuzzySetLinear1 zbior){
		double[] punktyDyskretyzacji = getPunktyDyskretyzacji(zbior);
		double licznik = 0;
		double mianownik = 0;
		double srodekCiezkosci = 0;
		double x;
		double przynaleznosc;
		for(int i = 0; i < punktyDyskretyzacji.length; i++){
			x = punktyDyskretyzacji[i];
			przynaleznosc = zbior.getPrzynaleznosc(x);
			licznik += x * przynaleznosc;
			mianownik += przynaleznosc;
		}
		srodekCiezkosci = licznik / mianownik;
		return srodekCiezkosci;
	}
	
	private double[] getPunktyDyskretyzacji(FuzzySetLinear1 zbior){
		int liczbaPuktow = 100;
		double[] punkty = new double[liczbaPuktow];
		double min = zbior.wierzcholki.get(0).x;
		double max = zbior.wierzcholki.get(zbior.wierzcholki.size() - 1).x;
		if(max < min){
			double tmp = max;
			max = min;
			min = tmp;
		}
		double interval = ( max - min ) / liczbaPuktow;
		for(int i = 0; i < 100; i++){
			punkty[i] = min + interval * i;
		}
		return punkty;
	}
	
	/*
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
	*/

}


