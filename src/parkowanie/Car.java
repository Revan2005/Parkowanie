package parkowanie;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.swing.JButton;


public class Car {
	public final Point polozeniePoczatkowe = new Point(900, 330);
	final double kierunekPoczatkowy = Math.PI*0.5;
	final double MAX_SKRET_KOL = Math.PI/30.0; //max skret to 45stopni - to niekoniecznie stopnia skretu kola nie uwzgledniam do konca mechaniki pojazdu
	//stosunek dlugosc szerokosc  i odleglosc od tylu auta do osi tylnych kol ( os obrotu samochodu) taka jak we fiacie punto
	double rozmiarAuta = 1.0;
	public final Point wymiary = new Point((int)(130*rozmiarAuta), (int)(300*rozmiarAuta)); //zawsze pierwszy wymiar ma byc mniejszy!!!!
	public final int odleglosc_od_tylu_do_osi = (int)(45*rozmiarAuta);
	public final int odleglosc_od_przodu_do_osi = wymiary.y-odleglosc_od_tylu_do_osi; //y- dlugosc auta (wiekszy wymiar)
	//Graphics2D g2d;
	
	BufferedImage mapa_parkingu;
	MyPanel panel;
	public Point polozenie;// poprzednie_polozenie;//przez polozenie rozumiem tylko wspolrzedne punktu srodka osi tylnych kol nie uwzgledniam u kierunku
	int promienSwiatel = 5;
	int licznik_skretu_w_lewo = 0, licznik_skretu_w_prawo = 0; //liczy ile stopni samochod skrecil bez ruchu w przod lub w tyl
	final int MAX_SKRET_BEZ_RUCHU_PRZOD_TYL = 5/2; //1 krok to 2 stopnie katowe (Pi/90) licznik nad dwojka to kat w stopniach
	//powyzej ktorego nie mozna skrecic stojac w miejscu (to nie jest skret kol auta za duzo z tym by bylo roboty to 
	//nie musi byc symulacja samochodu) 
	//double predkosc;
	public double kierunek; //0 znaczy jade w gore ekranu, pi/2 w prawo pi w dol itd
	int liczbaKolizji;
	//Kierowca kierowca;
	public double promienDlugi;
	public double promienKrotki;
	double alfa;
	double beta;
	double gamma;
	int x[] = new int[4];
	int y[] = new int[4];
	public Polygon widok; // widok to 4 rogi na ich podstawie rysuje auto
	public Polygon punkty_na_obwodzie_probkujace_kolizje;
	double skretKol; //wartosci od -1 do 1
	boolean czyWstecznyBieg = false;
	//boolean czyWstecznyBieg;
	Thread thread;
	
	public Car(MyPanel _panel){
		panel = _panel;
		mapa_parkingu = panel.parking_map;
		polozenie = polozeniePoczatkowe;
		
		//predkosc = 0;
		kierunek = kierunekPoczatkowy; //0 znaczy jade w gore ekranu, pi/2 w prawo pi w dol itd
		liczbaKolizji = 0;
		//mapa_parkingu = panel.parking_map; to nie powinno byc potrzebne
		
		//wielkosci potrzebne do obliczania polozenia punktow na obwodzie
		promienDlugi = Math.sqrt((wymiary.x*wymiary.x*0.25)+odleglosc_od_przodu_do_osi*odleglosc_od_przodu_do_osi); //*0.25 - 0.2^2 bo ma byc polowa szerokosci uwzgledniona tutaj
		promienKrotki = Math.sqrt(wymiary.getX()*wymiary.getX()*0.25+odleglosc_od_tylu_do_osi*odleglosc_od_tylu_do_osi);
		
		alfa = Math.atan((wymiary.getX()/2.0)/odleglosc_od_przodu_do_osi)*2;
		gamma = Math.atan((wymiary.getX()/2.0)/odleglosc_od_tylu_do_osi)*2;
		beta = (2*Math.PI-alfa-gamma)*0.5;
		
		aktualizujPolozeniePunktowNaObwodzie();
		//kierowca = new Kierowca(this);
		//start();
	}
	
	
	//public void setMap(BufferedImage map){
		//mapa_parkingu = map;
	//}
	
	/*
	public void setStartPosition(){
		polozenie = polozeniePoczatkowe;
		kierunek = Math.PI*0.5;
		zmianaPolozenia(polozenie, kierunek);
		//panel.repaint();
	}
	*/
	
	public void zmianaPolozenia(Point _polozenie, double _kierunek) {
		polozenie = _polozenie;
		kierunek = _kierunek;
		aktualizujPolozeniePunktowNaObwodzie();
	}
	
	public void aktualizujPolozeniePunktowNaObwodzie(){
		double prawyPrzedniRogX = polozenie.getX()+Math.sin(kierunek+alfa*0.5)*promienDlugi;//*0.5;//tu wszedzie i ponizej przed promienDlugi nie pozinno byc *0.5??
		double prawyPrzedniRogY = polozenie.getY()-Math.cos(kierunek+alfa*0.5)*promienDlugi;//*0.5;
		x[0] = (int)prawyPrzedniRogX;
		y[0] = (int)prawyPrzedniRogY;
		//System.out.println(alfa);
		//System.out.println(Math.sin(alfa/2)*promienDlugi);
		
		double prawyTylnyRogX = polozenie.getX()+Math.sin(kierunek+(alfa*0.5)+beta)*promienKrotki;//*0.5;
		double prawyTylnyRogY = polozenie.getY()-Math.cos(kierunek+(alfa*0.5)+beta)*promienKrotki;//*0.5;
		x[1] = (int)prawyTylnyRogX;
		y[1] = (int)prawyTylnyRogY;
		
		double lewyTylnyRogX = polozenie.getX()+Math.sin(kierunek+(alfa*0.5)+beta+gamma)*promienKrotki;//*0.5;
		double lewyTylnyRogY = polozenie.getY()-Math.cos(kierunek+(alfa*0.5)+beta+gamma)*promienKrotki;//*0.5;
		x[2] = (int)lewyTylnyRogX;
		y[2] = (int)lewyTylnyRogY;
		
		double lewyPrzedniRogX = polozenie.getX()+Math.sin(kierunek-alfa*0.5)*promienDlugi;//*0.5;
		//System.out.println(Math.sin(kierunek+alfa*0.5+beta+gamma+beta)*promienDlugi);
		double lewyPrzedniRogY = polozenie.getY()-Math.cos(kierunek-alfa*0.5)*promienDlugi;//*0.5;
		//System.out.println(Math.cos(kierunek+alfa*0.5+beta+gamma+beta)*promienDlugi);
		
		x[3] = (int)lewyPrzedniRogX;
		y[3] = (int)lewyPrzedniRogY;
		//System.out.println(kierunek+" "+alfa+" "+beta+" "+dlugoscPolowyPrzekatnej);
		//System.out.println(x[0]+" "+y[0]+" "+x[1]+" "+y[1]+" "+x[2]+" "+y[2]+" "+x[3]+" "+y[3]);
		widok = new Polygon(x, y, 4);
		 
		//na podstawie powyzszego mozna policzyc polozenia puntkow na obwodzie do probkowania kolizji
		//punkty w polygonie widok ida tak: prawy gorny rog i dalej zgodnie z ruchem wskazowek zegara
		//-(zakladajac ze auto stoi przodem do gory ekranu)
		int x[] = new int[16]; // wyznacze 16 punktow na obwodzie - po 3 na przod/tyl i po 5 na boki - starczy raczej
		int y[] = new int[16];
				
		Point rogPG = new Point(widok.xpoints[0], widok.ypoints[0]);
		Point rogPD = new Point(widok.xpoints[1], widok.ypoints[1]);
		Point rogLD = new Point(widok.xpoints[2], widok.ypoints[2]);
		Point rogLG = new Point(widok.xpoints[3], widok.ypoints[3]);
		int x1, y1, x2, y2;
		//prawy bok
		x1 = rogPG.x;
		x2 = rogPD.x;
		y1 = rogPG.y;
		y2 = rogPD.y;
		for(int i=0; i<5; i++){
			x[i] = x1 - (i+1)*(x1-x2)/6;
			y[i] = y1 - (i+1)*(y1-y2)/6;
		}
		//tylny zderzak
		x1 = rogPD.x;
		x2 = rogLD.x;
		y1 = rogPD.y;
		y2 = rogLD.y;
		for(int i=0; i<3; i++){
			x[i+5] = x1 - (i+1)*(x1-x2)/4;
			y[i+5] = y1 - (i+1)*(y1-y2)/4;
		}
		//lewy bok
		x1 = rogLD.x;
		x2 = rogLG.x;
		y1 = rogLD.y;
		y2 = rogLG.y;
		for(int i=0; i<5; i++){
			x[i+8] = x1 - (i+1)*(x1-x2)/6;
			y[i+8] = y1 - (i+1)*(y1-y2)/6;
		}
		//przedni zderzak
		x1 = rogLG.x;
		x2 = rogPG.x;
		y1 = rogLG.y;
		y2 = rogPG.y;
		for(int i=0; i<3; i++){
			x[i+13] = x1 - (i+1)*(x1-x2)/4;
			y[i+13] = y1 - (i+1)*(y1-y2)/4;
		}
		punkty_na_obwodzie_probkujace_kolizje = new Polygon(x, y, 16);
	}
	
	
	
	public boolean collision(){
		//na razie sprawdzam tylko czy nie zahaczylem ktoryms rogiem
		Point rog;
		//Polygon widok = widok_z_gory.widok;
		for(int i=0; i<4; i++){
			rog = new Point(widok.xpoints[i], widok.ypoints[i]);
			//czy nie wyjechal za mape
			if((rog.x<=0)||(rog.y<=0)||(rog.x>=mapa_parkingu.getWidth())||(rog.y>=mapa_parkingu.getHeight())){
				liczbaKolizji++;
				return true;
			}
			//czy nie wjechal na czerwone
			int color_int = mapa_parkingu.getRGB(rog.x, rog.y);
			Color kolor = new Color(color_int);
			int r = kolor.getRed();
			int g = kolor.getGreen();
			//int b = kolor.getBlue();
			if((r>230)&&(g<100)){ // to po && zeby nie bralo bieli za czerwony
				//System.out.println("red="+r+" green= "+g);
				liczbaKolizji++;
				return true;
			}
		}
		//troche za duzo kodu nieladnie to wyglada ale nie bede kombinowal 
		// : to samo co wyzej dla punktow na obwodzie
		//Polygon obwod = widok_z_gory.punkty_na_obwodzie_probkujace_kolizje;
		Polygon obwod = punkty_na_obwodzie_probkujace_kolizje;
		Point p;
		for(int i=0; i<16; i++){
			p = new Point(obwod.xpoints[i], obwod.ypoints[i]);
			//czy nie wyjechal za mape
			//niepotrzebne bo sprawdzone juz rogami pojazdu punkt na obwodzie nie wyjedzie jesli nie wyhechal zaden rog pojazdu
			//if((p.x<=0)||(p.y<=0)||(p.x>=mapa_parkingu.getWidth())||(p.y>=mapa_parkingu.getHeight())){
			//	return true;
			//}
			//czy nie wjechal na czerwone
			int color_int = mapa_parkingu.getRGB(p.x, p.y);
			Color kolor = new Color(color_int);
			int r = kolor.getRed();
			int g = kolor.getGreen();
			//int b = kolor.getBlue();
			if((r>230)&&(g<100)){  //to po && zeby nie bralo bieli za czerwony
				liczbaKolizji++;
				//System.out.println("red="+r+" green= "+g);
				return true;
			}
		}
		return false;
	}
	
	public void move(char c){
		if(c=='d')
			skrec(Math.PI/90); //1 krok to 2 stopnie katowe
		if(c=='a')
			skrec(-Math.PI/90);
		if(c=='w')
			goForward(10);
		if(c=='s')
			goBack(10);
		if(collision()){
			//System.out.println("collision!!!!");
			undoMove(c);
		}
		if(taskIsComplete()){
			System.out.println("zielony!!!!");;
		}
		//panel.revalidate();
		panel.repaint();
	}
	
	public boolean taskIsComplete() throws IndexOutOfBoundsException{
		Point rog;
		int licznik = 0;
		for(int i=0; i<4; i++){
			rog = new Point(widok.xpoints[i], widok.ypoints[i]);
			//czy wszystkie rogi sa na zielonym
			int color_int = mapa_parkingu.getRGB(rog.x, rog.y);
			Color kolor = new Color(color_int);
			int r = kolor.getRed();
			int g = kolor.getGreen();
			//int b = kolor.getBlue();
			//System.out.println(r+" "+g+" "+" "+b);
			if((g>225)&&(r<200)){ //red mniejsze od 100 zeby nie bralo bieli za zielony!!!!
				licznik++;
			}
		}
		//jak cale auto jest na zielonym i jest ulozone w miare poziomo (+-1stopni katowych)
		if((licznik == 4) && ( (Math.abs(kierunek-Math.PI/2.0)<Math.PI/180.0) || (Math.abs(kierunek-3.0*Math.PI/2.0)<Math.PI/180.0) )){
			//System.out.println("weszlo task is complete");
			return true;
		}
		return false;
	}
	
	
	public void undoMove(char c){
		if(c=='d')
			skrec(-Math.PI/90); //1 krok to 2 stopnie katowe
		if(c=='a')
			skrec(Math.PI/90);
		if(c=='w')
			goBack(10);
		if(c=='s')
			goForward(10);
	}
	
	public void skrec(double angle){
		kierunek+=angle;
		while(kierunek > Math.PI*2)
			kierunek -= Math.PI*2;
		while(kierunek < 0)
			kierunek += Math.PI*2;
		zmianaPolozenia(polozenie, kierunek);
		panel.repaint();
	}
	

	public void goForward(double distance){
		double noweX = polozenie.getX()+Math.sin(kierunek)*distance;
		double noweY = polozenie.getY()-Math.cos(kierunek)*distance;//minus bo os oy jest skierowana w dol
		//poprzednie_polozenie = new Point(polozenie.x, polozenie.y);//dotychczasowe polozenie
		polozenie = new Point((int)Math.round(noweX), (int)Math.round(noweY));
		//zmienilo sie polozenie przod/tyl wiec zeruje liczniki skretu
		licznik_skretu_w_lewo = 0;
		licznik_skretu_w_prawo =0;
		zmianaPolozenia(polozenie, kierunek);
		//panel.repaint();
	}
	
	public void goBack(double distance){
		//tu zamiana tylko znakow +/-
		double noweX = polozenie.getX()-Math.sin(kierunek)*distance;
		double noweY = polozenie.getY()+Math.cos(kierunek)*distance;
		polozenie = new Point((int)Math.round(noweX), (int)Math.round(noweY));
		//zmienilo sie polozenie przod/tyl wiec zeruje liczniki skretu
		licznik_skretu_w_lewo = 0;
		licznik_skretu_w_prawo = 0;
		zmianaPolozenia(polozenie, kierunek);
		//panel.repaint();
	}
	
	public void drawWidok(Graphics2D g2d) {
		g2d.setColor(Color.BLUE);
		g2d.fillPolygon(widok);
		g2d.setColor(Color.YELLOW);
		g2d.fillOval(widok.xpoints[0]-promienSwiatel, widok.ypoints[0]-promienSwiatel, promienSwiatel*2, promienSwiatel*2);
		g2d.fillOval(widok.xpoints[3]-promienSwiatel, widok.ypoints[3]-promienSwiatel, promienSwiatel*2, promienSwiatel*2);
		g2d.setColor(Color.RED);
		g2d.fillOval(widok.xpoints[1]-promienSwiatel, widok.ypoints[1]-promienSwiatel, promienSwiatel*2, promienSwiatel*2);
		g2d.fillOval(widok.xpoints[2]-promienSwiatel, widok.ypoints[2]-promienSwiatel, promienSwiatel*2, promienSwiatel*2);
		g2d.fillOval(polozenie.x, polozenie.y, 2, 2);
		
		for(int i=0; i<16; i++){
			g2d.fillOval(punkty_na_obwodzie_probkujace_kolizje.xpoints[i]-2, punkty_na_obwodzie_probkujace_kolizje.ypoints[i]-2, 4, 4);
		}
	}
	
	public void restart(BufferedImage map){
		thread.stop();
		mapa_parkingu = map;
		kierunek = Math.PI*0.5; //0 znaczy jade w gore ekranu, pi/2 w prawo pi w dol itd
		polozenie = polozeniePoczatkowe;
		liczbaKolizji = 0;
		aktualizujPolozeniePunktowNaObwodzie();
		//kierowca.restart(map);
	}
	/*
	public void start(){
		thread = new Thread() {
			public void run() {
	            startDrive();
			}
		};
		thread.start();
	}*/
	

	
	public void krok(double[] decyzja){
		skretKol = decyzja[0];
		double zwrot = decyzja[1];
		if( zwrot == 0 ){
			czyWstecznyBieg = false;
		} else
			czyWstecznyBieg = true;
		
		
		if (Math.abs(skretKol)>MAX_SKRET_KOL){
			if(skretKol<0)
				skretKol = -MAX_SKRET_KOL;
			else
				skretKol = MAX_SKRET_KOL;
		}
		//System.out.println("Wykonuje krok");
		if(czyWstecznyBieg){
			move('s');
			skrec(-skretKol);
			if(collision()){
				System.out.println("collision!!!!");
				skrec(skretKol);
				move('w');
			}
		} else {
			move('w');
			skrec(skretKol);
			if(collision()){
				System.out.println("collision!!!!");
				skrec(-skretKol);
				move('s');
			}
		}
	}

	public void printPolozenieToFile(){
		try {
			FileWriter writer = new FileWriter("./trajektorie/trajektoria.txt", true);
			writer.write("polozenie: " + this.polozenie + ";      xpointsRogow: " + Arrays.toString(this.widok.xpoints) +
							";         ypointsRogow: " + Arrays.toString(this.widok.ypoints) + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
