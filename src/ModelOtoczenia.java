import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class ModelOtoczenia {
	BufferedImage mapaParkingu;
	//dane miejsca parkingowego
	//na razie zakladam ze jest tylko jedno miejsce parkingowe na mapie
	Point greenFieldStart;
	Point greenFieldEnd;
	Point centrumMiejscaParkingowego;
	//Czujnik czujnik;
	int dlugoscMiejscaParkingowego, wysokoscMiejscaParkingowego;
	//int xSamochodu, ySamochodu;
	//double orientacjaSamochodu;
	int x0, y0;
	int xSrodkaParkingu, ySrodkaParkingu;
	
	public ModelOtoczenia(BufferedImage map){
		mapaParkingu = map;
		greenFieldStart = findGreenFieldStart();
		greenFieldEnd = findGreenFieldEnd();
		if((greenFieldStart.x==greenFieldEnd.x)||(greenFieldStart.y==greenFieldEnd.y)){
			System.out.println("Nie ma miejsca parkingowego!");
		}
		dlugoscMiejscaParkingowego = Math.abs(greenFieldEnd.x - greenFieldStart.x);
		wysokoscMiejscaParkingowego = Math.abs(greenFieldStart.y - greenFieldEnd.y);
		x0 = greenFieldStart.x;
		y0 = greenFieldEnd.y;
		int x = greenFieldStart.x+(greenFieldEnd.x - greenFieldStart.x)/2;
		xSrodkaParkingu = x;
		int y = greenFieldStart.y+(greenFieldEnd.y - greenFieldStart.y)/2;
		ySrodkaParkingu = y;
		//System.out.println(greenFieldStart.toString()+"  "+greenFieldEnd.toString());
		centrumMiejscaParkingowego = new Point(x, y);
		//System.out.println(centrumMiejscaParkingowego.toString());		

	}
	
	public Point getGreenFieldStart(){
		return greenFieldStart;
	}
	
	public Point getGreenFieldEnd(){
		return greenFieldStart;
	}

	//zalozenie ze jest tylko jedno miejsce parkingowe!!!!
	//zielony - g>225
	//czerwony r>230
	public Point findGreenFieldStart(){
		for(int x=0; x<mapaParkingu.getWidth(); x++){
			for(int y=0; y<mapaParkingu.getHeight(); y++){
				int color_int = mapaParkingu.getRGB(x, y);
				Color kolor = new Color(color_int);
				int g = kolor.getGreen();
				int r = kolor.getRed();
				int b = kolor.getBlue();
				if((g==230)&&(r==168)&&(b==29))
					return new Point(x, y);
			}
		}
		return new Point(0, 0);
	}
	public Point findGreenFieldEnd(){
		int ret_x=0, ret_y=0;
		for(int x=0; x<mapaParkingu.getWidth(); x++){
			for(int y=0; y<mapaParkingu.getHeight(); y++){
				int color_int = mapaParkingu.getRGB(x, y);
				Color kolor = new Color(color_int);
				int g = kolor.getGreen();
				int r = kolor.getRed();
				int b = kolor.getBlue();
				if((g==230)&&(r==168)&&(b==29)){
					ret_x = x;
					ret_y = y;
				}	
			}
		}
		return new Point(ret_x, ret_y);
	}
	
	public void drawUkladOdniesienia(Graphics2D g2d){
		g2d.setColor(Color.RED);
		g2d.fillOval(centrumMiejscaParkingowego.x, centrumMiejscaParkingowego.y, 4, 4);
		g2d.setColor(Color.BLACK);
		g2d.fillOval(x0, y0, 4, 4);
		g2d.fillOval(x0+dlugoscMiejscaParkingowego, y0, 4, 4);
	}
	
	public void restart(){
		;
	}

}


