package parkowanie;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Czujnik {
	Car car;
	BufferedImage mapa;
	int[][] mapaBinarnie;
	
	public Czujnik(Car c, BufferedImage map){
		car = c;
		mapa = map;
		mapaBinarnie = new int[mapa.getWidth()][mapa.getHeight()];
		binaryzujMape();
		//showBinaryMap();
	}
	
	public InfoZCzujnika getDistanceAndDirectionToClosestObstacle(){
		Polygon punktyNaObwidzie = car.punkty_na_obwodzie_probkujace_kolizje;
		int[] rogiX = car.widok.xpoints;
		int[] rogiY = car.widok.ypoints;
		double distanceMin = Double.MAX_VALUE, distance;
		String kierunek = "";
		int minx=0, miny=0, minxnaaucie=0, minynaaucie=0, minPoint=-1, minRog=-1;
		for(int x=0; x<mapaBinarnie.length; x++){
			for(int y=0; y<mapaBinarnie[0].length; y++){
				if(mapaBinarnie[x][y] == 1){
					for(int i=0; i<punktyNaObwidzie.npoints; i++){
						distance = Point.distance(x, y, punktyNaObwidzie.xpoints[i], punktyNaObwidzie.ypoints[i]);
						if(distance < distanceMin){
							distanceMin = distance;
							minx = x;
							miny = y;
							minxnaaucie = punktyNaObwidzie.xpoints[i];
							minynaaucie = punktyNaObwidzie.ypoints[i];
							minPoint = i;
						}
					}
					for(int i=0; i<rogiX.length; i++){
						distance = Point.distance(x, y, rogiX[i], rogiY[i]);
						if(distance < distanceMin){
							distanceMin = distance;
							minx = x;
							miny = y;
							minxnaaucie = rogiX[i];
							minynaaucie = rogiY[i];
							minRog = i;
						}
					}
				}
			}
		}
		//ktory punkt jest najblizej przeszkody
		if(minRog != -1){
			switch(minRog){
				case 0:
					kierunek = "prawyPrzedniRog";
					break;
				case 1:
					kierunek = "prawyTylnyRog";
					break;
				case 2:
					kierunek = "lewyTylnyRog";
					break;
				case 3:
					kierunek = "lewyPrzedniRog";
					break;
				default:
					break;
			}
		} else {
			if(minPoint<5)
				kierunek = "prawyBok";
			else
				if(minPoint<8)
					kierunek = "tyl";
				else
					if(minPoint<13)
						kierunek = "lewyBok";
					else
						kierunek = "Przod";
		}
		
		//kierunek wektora majacego poczatek w aucie i koniec w przeszkodzie
		//0 - gora pi/2 prawo itd zgodnie z ruchem wsk zefgara
		double direction;
		double dx = Math.abs((double)(minxnaaucie - minx));
		double dy = Math.abs((double)(minynaaucie - miny));
		if(minxnaaucie>minx){
			if (minynaaucie>miny){
				//"moja" 4 cwiartka
				direction = Math.atan(dy/dx)+Math.PI+Math.PI/2.0;
			} else {
				//moja 3 cwiartka
				direction = Math.atan(dx/dy)+Math.PI;
			}
		} else {
			if (minynaaucie>miny){
				//moja 1 cwiartka
				direction = Math.atan(dx/dy);
			} else {
				//moja 2 cwiartka
				direction = Math.atan(dy/dx)+Math.PI/2.0;
			}
		}
		double[] minDistDir = new double[2];
		minDistDir[0] = distanceMin;
		minDistDir[1] = direction;
		
		//System.out.println("xa = "+minxnaaucie+" ya = "+minynaaucie+" minx = "+minx+" miny = "+miny+
		//		"dx = "+dx+" dy = "+dy
		//		+"dystans do najblizszej przeszkody = "+distanceMin+", kierunek = "+direction*180/Math.PI);

		return new InfoZCzujnika(kierunek, distanceMin);
	}
	
	public void binaryzujMape(){
		int color_int;
		Color kolor;
		int r;
		int g;
		for(int x=0; x<mapa.getWidth(); x++){
			for(int y=0; y<mapa.getHeight(); y++){
				color_int = mapa.getRGB(x,y);
				kolor = new Color(color_int);
				r = kolor.getRed();
				g = kolor.getGreen();
				if((r>230)&&(g<100))
					mapaBinarnie[x][y] = 1;
				else
					mapaBinarnie[x][y] = 0;
			}
		}
	}
	
	
	public void showBinaryMap(){
		for(int i=0; i<mapaBinarnie.length; i++){
			for(int j=0; j<mapaBinarnie[0].length; j++){
				System.out.print(mapaBinarnie[i][j]);
			}
			System.out.println();
		}
	}

}
