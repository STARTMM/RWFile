package operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import main.config_map_trans;

import entity.Node;
import entity.Segment;

public class TransLonLat2MeterReadFile extends AbsReadFile {
	private static String out_file_name = config_map_trans.wkt_out_path;
	public static File fileout = new File(out_file_name);
	public static FileWriter fw = null;
	
	/**
	 * set @Longitude_0 = 116.1987630+(116.5577685-116.1987630)*0.1
set @Latitude_0 = 39.76115800+(40.02637700-39.76115800)*0.1
set @Longitude_max =116.1987630+(116.5577685-116.1987630)*0.9
set @Latitude_max = 39.76115800+(40.02637700-39.76115800)*0.9
set @Earth_equation_r = 6378137.0
set @Earth_polar_r = 6356725.0
SELECT Top 10000 ((Lon - @Longitude_0) * PI() / 180.0)*(Cos(Lat * PI() / 180.0))*(@Earth_polar_r + (@Earth_equation_r - @Earth_polar_r) * (90 - Lat) / 90), 
(@Earth_polar_r + (@Earth_equation_r - @Earth_polar_r) * (90 - Lat) / 90)*((Lat - @Latitude_0) * PI() / 180.0) 
FROM VTD20110307 WHERE Event = 4 and Lon> @Longitude_0 and Lon<@Longitude_max and Lat>@Latitude_0 and Lat<@Latitude_max
	 * @param longtitude
	 * @param latitude
	 * @return
	 */

	/**
	 *
	 1�����ȣ� 116.37826575-(116.5577685-116.1987630)*(12000/30721)=116.2380338

	 2��ά�ȣ�39.8937675-(40.02637700-39.76115800)*(12000/29524)=39.7859695
	 */
//
//	public static double latitude_0 = 39.76115800+(40.02637700-39.76115800)*0.1;
//	public static double longtitude_0 = 116.1987630+(116.5577685-116.1987630)*0.1;
	public static double latitude_0 = 39.7859695;
	public static double longtitude_0 = 116.2380338;
//	public static double longtitude_max = 116.1987630+(116.5577685-116.1987630)*0.9;
//	public static double latitude_max = 39.76115800+(40.02637700-39.76115800)*0.9;
//
	public static double earth_equation_r =6378137.0;
	public static double earth_polar_r = 6356725.0;
	
	double getLongtitude(double longtitude, double latitude){
		double lon = ((longtitude-longtitude_0)*Math.PI/180.0)*(Math.cos(latitude*Math.PI/180.0))*(earth_polar_r+(earth_equation_r-earth_polar_r)*(90-latitude)/90);
		return lon;
		
	}
	public static double getLatitude(double longtitude, double latitude){
		double lat = (earth_polar_r+(earth_equation_r-earth_polar_r)*(90-latitude)/90)*(latitude-latitude_0)*Math.PI/180.0;
		
		return lat;
	}
	
	
	

	@Override
	public void dealWithFile(File file) {
		// TODO Auto-generated method stub
		FileReader fr;
		try {
			fr = new FileReader(file);
			fw = new FileWriter(fileout);
			BufferedReader reader = new BufferedReader(fr);
			String line;
			
			while((line = reader.readLine())!=null)
			{
//				String line2 = reader.readLine();
//				String line3 = reader.readLine();
				
				//???????
//				String[]arrl = line.split(" ");
				//???????
//				String[]arrl2 = line2.split(" ");

				String[] arrl = line.split(" ");
				if(arrl.length!=4)
					continue;
//				if(arrl2.length!=2)
//					continue;
				
				double lon1,lon2,lat1,lat2;
				lon1 = getLongtitude(Double.parseDouble(arrl[0]), Double.parseDouble(arrl[1]));
				lat1 = getLatitude(Double.parseDouble(arrl[0]), Double.parseDouble(arrl[1]));
				
//				lon2 = getLongtitude(Double.parseDouble(arrl2[0]),Double.parseDouble(arrl2[1]));
//				lat2 = getLatitude(Double.parseDouble(arrl2[0]),Double.parseDouble(arrl2[1]));
				
				
				
				if(lon1>=24000||lat1>=24000||lon1<0||lat1<0)
				{
//					System.out.println("��������");
					
				}
				else{
					if(Integer.parseInt(arrl[3])==2)
						System.out.println(arrl[0]+"-"+arrl[1]+"-"+arrl[2]+"-"+arrl[3]);
//					System.out.println(lat2+" "+lat2);
				
					fw.write(lon1+" "+lat1+" "+arrl[2]+" "+arrl[3]+"\r\n");
//					fw.write(lon2+" "+lat2+"\r\n");
					fw.write("\r\n");
				}
				
				
			}
			fw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}


}
