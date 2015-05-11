package operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import main.config;
import main.config_map_trans;

/***
 * ����ͼMif��ݣ�����txt��ʽ��
 * ����:
 * Pline 24
117.15342 40.66604
117.15368 40.66664
117.15333 40.66832
 * 
 * ���:
 * Lon lat roadid type
 * @author Yang Wenjing
 *
 */
public class TransMif2Txt extends AbsReadFile {
	private static String out_file_dir = config.mif2txt_dir;
	
	private static int road_id = 0;
	private static String type="";
	
	public void setRoadId()
	{
		road_id++;
	}

	public void setType(String name)
	{
		if(name.contains("1"))
		{
			System.out.println("**type is 1**");
			type="1";
		}
		else if(name.contains("2"))
		{
			System.out.println("**type is 2**");
			type="2";
		}else if(name.contains("3"))
		{
			System.out.println("**type is 3**");
			type="3";
		}else
		{
			System.out.println("**type is 4**");
			type="4";
		}
	}
	@Override
	public void dealWithFile(File file) {
		// TODO Auto-generated method stub
		FileReader fr;
		
		try {
			fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			File fileout = new File(out_file_dir+"//"+file.getName());
			FileWriter fw = new FileWriter(fileout);
			
			String line;
			setType(file.getName());//
			
			while((line = reader.readLine())!=null)
			{
				
				if("".equals(line.trim()))//�����������
				{
					continue;
				}
				line = line.trim();
				String []s = line.split(" ");
				if(line.contains("Pen"))//����Pen���
					continue;
				
				if(line.contains("Pline")||line.contains("Line"))//����Line��PLine��ʱ�� road id++;
				{
					//System.out.println(s[0]+"-"+s[1]);
					setRoadId();
					//System.out.println(road_id);
				}
				
				if(line.contains("Line"))//����Line�������
				{
					//System.out.println(line);
					System.out.println(String.format("%s-%s-%s-%s-%s", s[0],s[1],s[2],s[3],s[4]));
					
					fw.write(String.format("%s %s %d %s\r\n", s[1],s[2],road_id,type));
					fw.write(String.format("%s %s %d %s\r\n", s[3],s[4],road_id,type));
					continue;
				}
				
				if(s[0]=="Pline")
					continue;
				
				if(s.length==2&&s[0].contains("11"))
					fw.write(String.format("%s %s %d %s\r\n", s[0],s[1],road_id,type));
				
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String s="      pen(a,b,c)";
//		s = s.trim();
//		System.out.println(s);
		
		String dir_in="/Users/ywj/Documents/road/in";
		
		TransMif2Txt readfile = new TransMif2Txt();
		try{
			readfile.readfile(dir_in);
			
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
