package parkowanie;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JButton;


public class Kierowca implements ActionListener {
	String typSterowania = "ostre";
	ModulDecyzyjny modulDecyzyjny;
	Car samochod;
	ModelOtoczenia modelOtoczenia;
	double MAX_SKRET_KOL;
	boolean czyJedziesz;
	//double[][] odlegloscKierunekOdPrzeszkody;
	
	BufferedImage mapaParkingu;
	Random random = new Random();
	int liczbaKrokow; 
	int sleepTime = 100;
	Thread thread;
	
	public Kierowca(Car c, ModelOtoczenia modelOt){
		czyJedziesz = false;
		samochod = c;
		modelOtoczenia = modelOt;
		mapaParkingu = c.mapa_parkingu;
		MAX_SKRET_KOL = c.MAX_SKRET_KOL;
		//System.out.println(mapaParkingu.toString());
		liczbaKrokow = 0;
		modulDecyzyjny = new ModulDecyzyjny(samochod, modelOtoczenia);
		thread = new Thread();
		
	}
	
	public void restart(BufferedImage map){
		czyJedziesz = false;
		mapaParkingu = map;
		liczbaKrokow = 0;
		samochod.restart(map);
		modelOtoczenia.restart();
		modulDecyzyjny.restart(map);
		thread.stop();
	}
	
	private void startDriveSharp(){
		while(!taskIsComplete()){
			try {
				Thread.sleep(100);
				decyzjaOSkrecieIZwrocieRegulyOstre(samochod, !samochod.czyWstecznyBieg);
				samochod.printPolozenieToFile();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	private void startDriveFuzzy1(){
		while(!taskIsComplete()){
			//System.out.println("jestem fuzzy");
			try {
				Thread.sleep(100);
				decyzjaOSkrecieIZwrocieFuzzy1(samochod, !samochod.czyWstecznyBieg);
				samochod.printPolozenieToFile();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	private void startDriveFuzzy2(){
		while(!taskIsComplete()){
			try {
				Thread.sleep(100);
				decyzjaOSkrecieIZwrocieFuzzy2(samochod, !samochod.czyWstecznyBieg);
				samochod.printPolozenieToFile();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		if( (button.getText()=="Ostre") && (!czyJedziesz) ){
			czyJedziesz = true;
			thread = new Thread() {
				public void run() {
		            startDriveSharp();
				}
			};
			thread.start();
		} else if( (button.getText()=="Fuzzy1") && (!czyJedziesz) ){
			czyJedziesz = true;
			thread = new Thread() {
				public void run() {
		            startDriveFuzzy1();
				}
			};
			thread.start();
		} else if( (button.getText()=="Fuzzy2") && (!czyJedziesz) ){
			czyJedziesz = true;
			thread = new Thread() {
				public void run() {
		            startDriveFuzzy2();
				}
			};
			thread.start();
		} 
		if( (button.getText()=="Stop") && (czyJedziesz) ){
			czyJedziesz = false;
			thread.stop();
		}
		 
	}
	
	public boolean taskIsComplete() throws IndexOutOfBoundsException{
		Point rog;
		int licznik = 0;
		for(int i=0; i<4; i++){
			rog = new Point(samochod.widok.xpoints[i], samochod.widok.ypoints[i]);
			//czy wszystkie rogi sa na zielonym
			int color_int = mapaParkingu.getRGB(rog.x, rog.y);
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
		if((licznik == 4) && ( (Math.abs(samochod.kierunek-Math.PI/2.0)<Math.PI/180.0) || (Math.abs(samochod.kierunek-3.0*Math.PI/2.0)<Math.PI/180.0) )){
			//System.out.println("weszlo task is complete");
			return true;
		}
		return false;
	}

	public void decyzjaOSkrecieIZwrocieRegulyOstre(Car c, boolean dotychczasDoPrzodu){		
		double[] decyzja = modulDecyzyjny.decyzjaRegulyOstre(c, !c.czyWstecznyBieg);
		c.krok(decyzja);
		//decyzja = kierowca.decyzjaOSkrecieIZwrocie(this, !czyWstecznyBieg);
	}
	
	public void decyzjaOSkrecieIZwrocieFuzzy1(Car c, boolean dotychczasDoPrzodu){		
		double[] decyzja = modulDecyzyjny.decyzjaFuzzy1(c, !c.czyWstecznyBieg);
		c.krok(decyzja);
		//decyzja = kierowca.decyzjaOSkrecieIZwrocie(this, !czyWstecznyBieg);
	}
	
	public void decyzjaOSkrecieIZwrocieFuzzy2(Car c, boolean dotychczasDoPrzodu){		
		double[] decyzja = modulDecyzyjny.decyzjaFuzzy2(c, !c.czyWstecznyBieg);
		c.krok(decyzja);
		//decyzja = kierowca.decyzjaOSkrecieIZwrocie(this, !czyWstecznyBieg);
	}

	/*
	//lepiej bedzie to przeniesc do modul wnioskujacy (zmienic nazwe decydent) a nazwe tej klasy zmienic na kierowca
	public double decyzjaOSkrecie(){
		int lewaSciana=0, prawaSciana=0, dolnaSciana = mapaParkingu.getHeight();
		boolean poprzedniCzerwony = false;
		double roznicaKierunkow = samochod.kierunek- Math.PI/2;
		int color_int = mapaParkingu.getRGB(20,mapaParkingu.getHeight()-50);
		Color kolor = new Color(color_int);
		int r = kolor.getRed();
		int g = kolor.getGreen();
		double result = 0;
		//int b = kolor.getBlue();
		if((r>230)&&(g<100)){ // to po && zeby nie bralo bieli za czerwony
			poprzedniCzerwony = true;
		}
		for(int x=21; x<mapaParkingu.getWidth(); x++){
			color_int = mapaParkingu.getRGB(x,mapaParkingu.getHeight()-50);
			kolor = new Color(color_int);
			r = kolor.getRed();
			g = kolor.getGreen();
			//int b = kolor.getBlue();
			if((r>230)&&(g<100)){ // to po && zeby nie bralo bieli za czerwony
				if(!poprzedniCzerwony)
					prawaSciana = x;
				poprzedniCzerwony = true;
			} else {
				if(poprzedniCzerwony)
					lewaSciana = x;
				poprzedniCzerwony = false;
			}
		}
		//System.out.println("=========================================="+ prawaSciana+" "+ lewaSciana+" "+ dolnaSciana);
		
		//rozwazam prawy bok auta
		for(int i=0; i<4; i++){
			//jezeli odleglosc auta od tylnej sciany jest mniejsza niz costam to costam
			if((samochod.widok.xpoints[i]-lewaSciana)<20){
				czyWstecznyBieg = false;
				//System.out.println("=============================raz");
			}
			if((dolnaSciana - samochod.widok.ypoints[i])<20){
				if(czyWstecznyBieg){
					czyWstecznyBieg = false;
					result += 0;
				}
				else{
					czyWstecznyBieg = true;
					result += -Math.PI/90;
				}
				//System.out.println("=============================dwa");
			}
			if((prawaSciana - samochod.widok.xpoints[i])<20){
				czyWstecznyBieg = true;
				//System.out.println("=============================trzy");
			}
		}		
		//wyswietlam w katach ale licze w radianach
		//System.out.println((roznicaKierunkow*180)/Math.PI);
		double roznicaPolozenia = samochod.polozenie.getY()-(mapaParkingu.getHeight()-200);//drugi skladnik roznicy powinien byc srodkiem miejsca parkingowego!!! info o tym jest w decydencie tym bardziej trzeba przeniesc te funkcje tam
		if((Math.abs(roznicaPolozenia)>10)&&(Math.abs(roznicaKierunkow)<Math.PI/4)){
			//System.out.println("No ale tu powinno wejsc zawsze");
			if(roznicaPolozenia<0){ //roznica ujemna - auto na lewo od celu
				//System.out.println(" jestem na lewo Roznica polozenia = "+roznicaPolozenia);
				if(!czyWstecznyBieg)
					result += -Math.PI/90;
				else
					result += Math.PI/90;
			}
			else {
				System.out.println("Jestem na prawo od celu, roznica polozenia = "+roznicaPolozenia);
				if(!czyWstecznyBieg)
					result += Math.PI/90;
				else
					result += -Math.PI/90;
			}
		}
		//System.out.println(Math.abs(roznicaKierunkow));
		if(Math.abs(roznicaKierunkow) > 30*Math.PI/90){
			System.out.println("Korekcja kierunku");
			if(roznicaKierunkow>0) //roznica dodatnia auto skrecone za bardzo w prawo
				if(!czyWstecznyBieg)
					result += Math.PI/90;
				else
					result += Math.PI/90;
			else 
				if(!czyWstecznyBieg)
					result += -Math.PI/90;
				else
					result += -Math.PI/90;
		}
		else
			result += 0;
		
		return result;
	}
	*/

	
	


}
