import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FuzzySetLinear1 {
	ArrayList<Punkt2D> wierzcholki;
	
	public FuzzySetLinear1(ArrayList<Punkt2D> wierzcholki){
		this.wierzcholki = wierzcholki;
		sortujWierzcholki();
	}
	
	//public ZbiotRozmyty or(ZbiorRozmytyset){
		
	//}
	public void sortujWierzcholki(){
		// wierzcholki musza byc uporzadkowane wzgledem x
		double minX;
		int minIndex;
		ArrayList<Punkt2D> sorted = new ArrayList<Punkt2D>();
		while (wierzcholki.size()>1){
			minIndex = 0;
			minX = wierzcholki.get(0).x;
			for(int i=0; i<wierzcholki.size(); i++){
				if(wierzcholki.get(i).x < minX){
					minX = wierzcholki.get(i).x;
					minIndex = i;
				}
			}
			//System.out.println("minindex="+minIndex+" punkt spod minindex = "+wierzcholki.get(minIndex).x+", "+wierzcholki.get(minIndex).y);
			sorted.add(new Punkt2D(wierzcholki.get(minIndex)));
			wierzcholki.remove(minIndex);
		}
		sorted.add(wierzcholki.get(0));
		wierzcholki = sorted;
	}
	
	public ArrayList<Punkt2D> sortujPunktyPrzeciec(ArrayList<Punkt2D> punktyPrzeciec){
		// punkty musza byc uporzadkowane wzgledem x, tak samo jak sortujWierzcholki
		double minX;
		int minIndex;
		ArrayList<Punkt2D> sorted = new ArrayList<Punkt2D>();
		while (punktyPrzeciec.size()>1){
			minIndex = 0;
			minX = punktyPrzeciec.get(0).x;
			for(int i=0; i<punktyPrzeciec.size(); i++){
				if(punktyPrzeciec.get(i).x < minX){
					minX = punktyPrzeciec.get(i).x;
					minIndex = i;
				}
			}
			//System.out.println("minindex="+minIndex+" punkt spod minindex = "+wierzcholki.get(minIndex).x+", "+wierzcholki.get(minIndex).y);
			sorted.add(new Punkt2D(punktyPrzeciec.get(minIndex)));
			punktyPrzeciec.remove(minIndex);
		}
		sorted.add(punktyPrzeciec.get(0));
		return sorted;
	}
	
	public double getPrzynaleznosc(double x){
		//przejdz po wierzcholkach i wybierz takie 2 miedzy ktorymi znajduje sie x
		//wierzcholki powinny byc posortowane wzgledem wartosci x (y to stopien przynaleznosci)
		//czy x mniejsze niz x pierwszego wierzcholka
		if(x<=wierzcholki.get(0).x)
			return wierzcholki.get(0).y;
		for(int i=0; i<wierzcholki.size()-1; i++){
			if( (wierzcholki.get(i).x<x) && (x<=wierzcholki.get(i+1).x) ){
				return getValue(wierzcholki.get(i), wierzcholki.get(i+1), x);
			}
		}
		return wierzcholki.get(wierzcholki.size()-1).y;
	}
	
	public double getValue(Punkt2D p1, Punkt2D p2, double x){
		//zwraca wartosc funkcji liniowej utworzonej na podstawie 2 punktow p1 i p2 w punkcie x
		double dy = p1.y-p2.y;
		double dx = p1.x-p2.x;
		// a , b - wspolczynniki funkcji liniowej
		double a = dy/dx;
		double b = p1.y - a*p1.x;
		
		return (a*x+b);
	}
	
	
	public FuzzySetLinear1 and(FuzzySetLinear1 set){
		//and to bedzie minumim
		ArrayList<Punkt2D> wierzcholkiNowegoZbioru = new ArrayList<Punkt2D>();
		ArrayList<Punkt2D> punktyPrzeciec = new ArrayList<Punkt2D>();
		Punkt2D[] paraPunktow1 = new Punkt2D[2];
		Punkt2D[] paraPunktow2 = new Punkt2D[2];
		Punkt2D punktPrzeciecia;
		for(int i=0; i<wierzcholki.size()-1; i++){
			for(int j=0; j<set.wierzcholki.size()-1; j++){
				paraPunktow1[0] = wierzcholki.get(i);
				paraPunktow1[1] = wierzcholki.get(i+1);
				paraPunktow2[0] = set.wierzcholki.get(j);
				paraPunktow2[1] = set.wierzcholki.get(j+1);
				punktPrzeciecia = getPunktPrzeciecia(paraPunktow1, paraPunktow2);
				if(punktPrzeciecia.y >= 0){
					punktyPrzeciec.add(new Punkt2D(punktPrzeciecia));
				}
			}
		}
		punktyPrzeciec = sortujPunktyPrzeciec(punktyPrzeciec);
		//teraz majac wszystkie punkty przeciec lece po nich i sprawdzam ktore z dotychczasowych wierzcholkow sa ponizej i je dodaje
		//pamietaj o poczatku i koncu
		double x;
		double y;
		for(int i=0; i<punktyPrzeciec.size()-1; i++){
			for(int j=0; j<wierzcholki.size(); j++){
				x = wierzcholki.get(j).x;
				if( (punktyPrzeciec.get(i).x<=x) && (x<=punktyPrzeciec.get(i+1).x) ){
					y = getValue(punktyPrzeciec.get(i), punktyPrzeciec.get(i+1), x);
					if( wierzcholki.get(j).y < y ) 
						wierzcholkiNowegoZbioru.add(new Punkt2D(wierzcholki.get(j)));
				}
			}
		}
		//to co wyzej dla drugiego zbioru: set
		for(int i=0; i<punktyPrzeciec.size()-1; i++){
			for(int j=0; j<set.wierzcholki.size(); j++){
				x = set.wierzcholki.get(j).x;
				if( (punktyPrzeciec.get(i).x<=x) && (x<=punktyPrzeciec.get(i+1).x) ){
					y = getValue(punktyPrzeciec.get(i), punktyPrzeciec.get(i+1), x);
					if( set.wierzcholki.get(j).y < y ) 
						wierzcholkiNowegoZbioru.add(new Punkt2D(set.wierzcholki.get(j)));
				}
			}
		}
		for(int i=0; i<punktyPrzeciec.size(); i++){
			wierzcholkiNowegoZbioru.add(new Punkt2D(punktyPrzeciec.get(i)));
		}
		
		//poczatek i koniec
		//first
		wierzcholkiNowegoZbioru.add(new Punkt2D( Math.min(wierzcholki.get(0).x, set.wierzcholki.get(0).x) , 0 ));
		//last
		wierzcholkiNowegoZbioru.add(new Punkt2D( Math.max(wierzcholki.get(wierzcholki.size()-1).x, set.wierzcholki.get(set.wierzcholki.size()-1).x) , 0 ));
		//koniec poczatku i konca
		
		return new FuzzySetLinear1(wierzcholkiNowegoZbioru);
	}
	
	public void przytnij(double y){
		ArrayList<Punkt2D> wierzcholkiNowegoZbioru = new ArrayList<Punkt2D>();
		ArrayList<Punkt2D> punktyPrzeciec = new ArrayList<Punkt2D>();
		Punkt2D[] odcinekTnacy = new Punkt2D[2];
		odcinekTnacy[0] = new Punkt2D(-Double.MAX_VALUE, y);
		odcinekTnacy[1] = new Punkt2D(Double.MAX_VALUE, y);
		Punkt2D[] odcinekWykresu = new Punkt2D[2];
		Punkt2D punktPrzeciecia;
		for(int i=0; i<wierzcholki.size()-1; i++){
			odcinekWykresu[0] = wierzcholki.get(i);
			odcinekWykresu[1] = wierzcholki.get(i+1);
			punktPrzeciecia = getPunktPrzeciecia(odcinekWykresu, odcinekTnacy);
			if(punktPrzeciecia.y >= 0){
				punktyPrzeciec.add(new Punkt2D(punktPrzeciecia));
			}
		}
		punktyPrzeciec = sortujPunktyPrzeciec(punktyPrzeciec);
		//teraz majac wszystkie punkty przeciec lece po nich i sprawdzam ktore z dotychczasowych wierzcholkow sa ponizej i je dodaje
		//pamietaj o poczatku i koncu
		for(int j=0; j<wierzcholki.size(); j++){
			if( wierzcholki.get(j).y <= y ) 
				wierzcholkiNowegoZbioru.add(new Punkt2D(wierzcholki.get(j)));
		}
		for(int i=0; i<punktyPrzeciec.size(); i++){
			wierzcholkiNowegoZbioru.add(new Punkt2D(punktyPrzeciec.get(i)));
		}
		wierzcholki = wierzcholkiNowegoZbioru;
		sortujWierzcholki();
	}
	
	public FuzzySetLinear1 negacja(){
		ArrayList<Punkt2D> wierzcholkiNowegoZbioru = new ArrayList<Punkt2D>();
		for(int i=0; i<wierzcholki.size(); i++){
			wierzcholkiNowegoZbioru.add( new Punkt2D( wierzcholki.get(i).x, (1-wierzcholki.get(i).y ) ) );
		}
		return new FuzzySetLinear1(wierzcholkiNowegoZbioru);
	}
	
	public FuzzySetLinear1 or(FuzzySetLinear1 set){
		//and to bedzie minumim
		ArrayList<Punkt2D> wierzcholkiNowegoZbioru = new ArrayList<Punkt2D>();
		ArrayList<Punkt2D> punktyPrzeciec = new ArrayList<Punkt2D>();
		Punkt2D[] paraPunktow1 = new Punkt2D[2];
		Punkt2D[] paraPunktow2 = new Punkt2D[2];
		Punkt2D punktPrzeciecia;
		for(int i=0; i<wierzcholki.size()-1; i++){
			for(int j=0; j<set.wierzcholki.size()-1; j++){
				paraPunktow1[0] = wierzcholki.get(i);
				paraPunktow1[1] = wierzcholki.get(i+1);
				paraPunktow2[0] = set.wierzcholki.get(j);
				paraPunktow2[1] = set.wierzcholki.get(j+1);
				punktPrzeciecia = getPunktPrzeciecia(paraPunktow1, paraPunktow2);
				if(punktPrzeciecia.y >= 0){
					punktyPrzeciec.add(new Punkt2D(punktPrzeciecia));
				}
			}
		}
		punktyPrzeciec = sortujPunktyPrzeciec(punktyPrzeciec);
		//teraz majac wszystkie punkty przeciec lece po nich i sprawdzam ktore z dotychczasowych wierzcholkow sa powyzej i je dodaje
		//pamietaj o poczatku i koncu
		double x;
		double y;
		for(int i=0; i<punktyPrzeciec.size()-1; i++){
			for(int j=0; j<wierzcholki.size(); j++){
				x = wierzcholki.get(j).x;
				if( (punktyPrzeciec.get(i).x<=x) && (x<=punktyPrzeciec.get(i+1).x) ){
					y = getValue(punktyPrzeciec.get(i), punktyPrzeciec.get(i+1), x);
					if( wierzcholki.get(j).y > y ) 
						wierzcholkiNowegoZbioru.add(new Punkt2D(wierzcholki.get(j)));
				}
			}
		}
		//to co wyzej dla drugiego zbioru: set
		for(int i=0; i<punktyPrzeciec.size()-1; i++){
			for(int j=0; j<set.wierzcholki.size(); j++){
				x = set.wierzcholki.get(j).x;
				if( (punktyPrzeciec.get(i).x<=x) && (x<=punktyPrzeciec.get(i+1).x) ){
					y = getValue(punktyPrzeciec.get(i), punktyPrzeciec.get(i+1), x);
					if( set.wierzcholki.get(j).y > y ) 
						wierzcholkiNowegoZbioru.add(new Punkt2D(set.wierzcholki.get(j)));
				}
			}
		}
		for(int i=0; i<punktyPrzeciec.size(); i++){
			wierzcholkiNowegoZbioru.add(new Punkt2D(punktyPrzeciec.get(i)));
		}
		
		//poczatek i koniec
		//first
		//jezeli znormalizowana szerokoscia rozstepu odleglosc miedzy wierzcholkami jest mniejsza niz epsilon to uznaje ze to te nsam x
		//i skoro to jest or to biore z nich max (ten wierzcholek ktory ma wieksza wartosc y - przynaleznosci)
		if ( Math.abs( (wierzcholki.get(0).x - set.wierzcholki.get(0).x)/
					   (wierzcholki.get(0).x - wierzcholki.get(wierzcholki.size()-1).x) ) < 0.01 ){
			if(wierzcholki.get(0).y > set.wierzcholki.get(0).y)
				wierzcholkiNowegoZbioru.add(wierzcholki.get(0));
			else
				wierzcholkiNowegoZbioru.add(set.wierzcholki.get(0));
		} else {
			if (wierzcholki.get(0).x > set.wierzcholki.get(0).x)
				wierzcholkiNowegoZbioru.add(set.wierzcholki.get(0));
			else
				wierzcholkiNowegoZbioru.add(wierzcholki.get(0));
		}
		//last
		if ( Math.abs( (wierzcholki.get(wierzcholki.size()-1).x - set.wierzcholki.get(set.wierzcholki.size()-1).x)/
				   (wierzcholki.get(0).x - wierzcholki.get(wierzcholki.size()-1).x) ) < 0.01 ){
			if(wierzcholki.get(wierzcholki.size()-1).y > set.wierzcholki.get(set.wierzcholki.size()-1).y)
				wierzcholkiNowegoZbioru.add(wierzcholki.get(wierzcholki.size()-1));
			else
				wierzcholkiNowegoZbioru.add(set.wierzcholki.get(set.wierzcholki.size()-1));
		} else {
			if (wierzcholki.get(wierzcholki.size()-1).x > set.wierzcholki.get(set.wierzcholki.size()-1).x)
				wierzcholkiNowegoZbioru.add(wierzcholki.get(wierzcholki.size()-1));
			else
				wierzcholkiNowegoZbioru.add(set.wierzcholki.get(set.wierzcholki.size()-1));
		}
		//koniec poczatku i konca
		
		return new FuzzySetLinear1(wierzcholkiNowegoZbioru);
	}
	
	
	public Punkt2D getPunktPrzeciecia(Punkt2D[] para1, Punkt2D[] para2){
		//pary punktow para1 i para2 definiuja mi odcinki
		Punkt2D niePrzecinajaSie = new Punkt2D(0, -1);
		
		double dy1 = (para1[0].y-para1[1].y);
		double dx1 = (para1[0].x-para1[1].x);
		// a , b - wspolczynniki funkcji liniowej
		if(dx1 == 0)
			return niePrzecinajaSie;
		double a1 = dy1/dx1;
		double b1 = para1[0].y - a1*para1[0].x;
		
		double dy2 = (para2[0].y-para2[1].y);
		double dx2 = (para2[0].x-para2[1].x);
		// a , b - wspolczynniki funkcji liniowej
		if(dx2 == 0)
			return niePrzecinajaSie;
		double a2 = dy2/dx2;
		double b2 = para2[0].y - a2*para2[0].x;
		if( (a2 - a1) == 0 )
			return niePrzecinajaSie;
		//w obliczenia w zeszycie ale liczy sie tak podobnie jak wspolczynniki a i b z ta tylko roznica ze w mianowniku jest odwrotnie (dostawiony minus)
		double x = (b1 - b2)/(a2 - a1);
		// a skoro y=a1x+b1 to
		double y = a1*x + b1;
		
	//teraz pozostaje pytanie czy punkt przeciecia prostych zawierajacych te odcinki - bo to policzylem, nalezy do obu tych odcinkow czyli czy rzeczywiscie sie przecinaja
		if( ( x > Math.max(para1[0].x, para1[1].x) ) || ( x < Math.min(para1[0].x, para1[1].x)  ) ||
			( x > Math.max(para2[0].x, para2[1].x) ) || ( x < Math.min(para2[0].x, para2[1].x)  )   ){
			return niePrzecinajaSie;
		}
		return new Punkt2D(x,  y);
		
	}
	
	public void pisz(){
		System.out.println("Zbiór rozmyty:");
		for(int i=0; i<wierzcholki.size(); i++){
			System.out.println(wierzcholki.get(i).x +" "+ wierzcholki.get(i).y);
		}
		System.out.println(" ");
	}
	
	public void piszDoPliku() {
		File plik = new File("/home/tomek/workspace/obliczeniaMiekkie/OperacjeNaZbiorachRozmytych/plots/zbiorRozmyty.dat");
		try {
			PrintWriter writer = new PrintWriter(plik);
			writer.write("# x y"+"\n");
			for(int i=0; i<wierzcholki.size(); i++){
				writer.write(wierzcholki.get(i).x +" "+ wierzcholki.get(i).y+"\n");
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void piszDoPliku(String nazwa) {
		File plik = new File("/home/tomek/workspace/obliczeniaMiekkie/OperacjeNaZbiorachRozmytych/plots/"+nazwa+".dat");
		try {
			PrintWriter writer = new PrintWriter(plik);
			writer.write("# x y"+"\n");
			for(int i=0; i<wierzcholki.size(); i++){
				writer.write(wierzcholki.get(i).x +" "+ wierzcholki.get(i).y+"\n");
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
