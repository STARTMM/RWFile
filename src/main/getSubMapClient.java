package main;

import java.io.FileNotFoundException;
import java.io.IOException;

import operation.ReadMapSegmentFile;
import operation.SegmentFactory;

public class getSubMapClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadMapSegmentFile readfile = new ReadMapSegmentFile();
		try {
			readfile.readfile(config_map.txt_file_dir);
			System.out.println("��ȡ������ͳ��"+readfile.segments.size());
			System.out.println("��ȡ������ͳ��"+SegmentFactory.neighbors.size());
			
			readfile.findSubConnectMap();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
