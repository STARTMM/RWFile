/**
 * 
 */
package operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.*;

import main.config;

import entity.Cell;
import entity.Queue;

/**
 * @author Yang Wenjing
 * 
 * ���ù�ȱ����ķ�ʽ
 *
 */
public class ReadMatrixFile2 extends AbsReadFile {
	public static List<Cell> cellList = new ArrayList<Cell>();//���ڶ�ȡcell
	public static String out_file_path = config.output_file_dir;
	public static HashMap<String, Cell> cellsMap=new HashMap<String, Cell>();
	public static Queue<Cell> queue = new Queue<Cell>();//��ʼ������
	public static int threshold = config.threshold;
	public static int regionScale = config.regionScale;
	
	public static int scaleCount = 0;
	
	public static int size = 120;
	public static String getKey(int x, int y) {
		String key = String.format("%d_%d", x,y);
		return key;
	}

	/* (non-Javadoc)
	 * @see operation.AbsReadFile#dealWithFile(java.io.File)
	 */
	@Override
	public void dealWithFile(File file) {
		// ���崦����
		FileReader fr;
		
		try {
			fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while((line = reader.readLine())!=null)
			{
				if(line=="")
					continue;
				
				String[]arrl = line.split("\t");
				int x = Integer.parseInt(arrl[0]);
				int y = Integer.parseInt(arrl[1]);
				int num = Integer.parseInt(arrl[2]);
				
				Cell c = new Cell(x,y,num);
				//if(pass(c))
				//{
				String key = getKey(x,y);
				cellsMap.put(key, c);
				cellList.add(c);
				//}
				
				
			}
			reader.close();
			fr.close();
			
			/**
			 * ��������Ϊ0�ĵ�
			 */
			for(int i=0;i<100;i++)
			{
				for(int j=0;j<100;j++)
				{
					String key = getKey(i,j);
					if(!cellsMap.containsKey(key))
					{
						cellsMap.put(key, new Cell(i,j,0));
					}
				}
			}
			/**
			 * ����ʶ���㷨
			 */
			cluster();
			
			/**
			 * ��ӡ������ļ�
			 */
			printArr(cellList,file);

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void printArr(List<Cell> cellList2, File file) throws IOException {
		// TODO Auto-generated method stub
		File fileout = new File(out_file_path+"\\"+file.getName());
		System.out.println("����ڵ��ļ�"+file.getName());
		FileWriter fw = new FileWriter(fileout);
		String key;
		for(int i=0;i<100;i++)
		{
			for(int j=0;j<100;j++)
			{
				key = getKey(i,j);
				if(cellsMap.containsKey(key))
				{
					Cell c = cellsMap.get(key);
					fw.write(c.toString()+"\r\n");
				}
				else{
					fw.write(i+" "+j+" 0 0"+"\r\n\r\n");
				}
			}
		}
		fw.close();
		
	}

	private boolean pass(Cell c) {
		// TODO Auto-generated method stub
		return c.num>=threshold?true:false;
	}

	/**
	 * ����ʶ��ĺ����㷨
	 */
	public void cluster() {
		Collections.sort(cellList);
		
		String key;
		for(Cell c:cellList)
		{
			if(!c.isUsed())
			{
				Cell.addClusterCounter();
//				if(Cell.clusterCount<=0)
//					break;
				c.setCluster();
				
				
					scaleCount=0;
					queue.enqueue(c);
					scaleCount++;
					while(!queue.isEmpty())
					{
						
						Cell pcell = queue.dequeue();
						pcell.setUsed();
						if(pass(c))
						{
							enqueueNext(pcell.x, pcell.y-1);
							enqueueNext(pcell.x-1,pcell.y);
							enqueueNext(pcell.x,pcell.y+1);
							enqueueNext(pcell.x+1,pcell.y);
							enqueueNext(pcell.x-1,pcell.y-1);
							enqueueNext(pcell.x-1,pcell.y+1);
							enqueueNext(pcell.x+1,pcell.y-1);
							enqueueNext(pcell.x+1,pcell.y+1);
						}
						else{
							enqueueNext_for_parse(pcell.x, pcell.y-1);
							enqueueNext_for_parse(pcell.x-1,pcell.y);
							enqueueNext_for_parse(pcell.x,pcell.y+1);
							enqueueNext_for_parse(pcell.x+1,pcell.y);
							enqueueNext_for_parse(pcell.x-1,pcell.y-1);
							enqueueNext_for_parse(pcell.x-1,pcell.y+1);
							enqueueNext_for_parse(pcell.x+1,pcell.y-1);
							enqueueNext_for_parse(pcell.x+1,pcell.y+1);
						}
				}
				
			}
		}
	}

	/**
	 * @param c
	 * @param x
	 * @param y
	 */
	public void enqueueNext(int x, int y) {
		if(scaleCount>regionScale)
			return;
		String key;
		key = getKey(x,y);
		
		if(cellsMap.containsKey(key)&&pass(cellsMap.get(key)))
		{
			Cell next=cellsMap.get(key);
			if(!next.isUsed())
			{
				next.setCluster();
				scaleCount++;
				queue.enqueue(next);
			}
		}
	}
	
	/**
	 * @param c
	 * @param x
	 * @param y
	 */
	public void enqueueNext_for_parse(int x, int y) {
		if(scaleCount>regionScale)
			return;
		String key;
		key = getKey(x,y);
		
		if(cellsMap.containsKey(key)&&!pass(cellsMap.get(key)))
		{
			Cell next=cellsMap.get(key);
			if(!next.isUsed())
			{
				next.setCluster();
				scaleCount++;
				queue.enqueue(next);
			}
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadMatrixFile2 readMatrixFile = new ReadMatrixFile2();
		try{
			readMatrixFile.readfile(config.txt_file_dir);
			
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
