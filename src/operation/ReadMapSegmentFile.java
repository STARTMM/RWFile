package operation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import main.config_map;

import entity.Neighbor;
import entity.Node;
import entity.Segment;

public class ReadMapSegmentFile extends ReadSegmentFile {
	private List<Integer> usedNodeArr = new ArrayList<Integer>();
	private List<Integer> usedSegments = new ArrayList<Integer>();
	private static String out_file_name = config_map.wkt_out_path;
	private static int layer = 0;
	public static File fileout = new File(out_file_name);
	public static FileWriter fw = null;
	public void findSubConnectMap()
	{
			try {
				fw = new FileWriter(fileout);
				Iterator iter = SegmentFactory.neighbors.entrySet().iterator();
				
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Neighbor nb = SegmentFactory.neighbors.get(entry.getKey());
					if(usedNodeArr.contains(nb.node.id))
						continue;
					traverse(nb);                                                                                                 
					break;
				}
				
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
	private void traverse(Neighbor nb) {
		// TODO 遍历neighbor
		layer++;
		System.out.print("�����ڵ㣺"+nb.node.id+"=========="+"�������"+layer);
		if(layer>1000)
			return;
		usedNodeArr.add(nb.node.id);
		for(Node n : nb.nodes)
		{
			Segment s = SegmentFactory.getSegment(nb.node, n);
			if(usedSegments.contains(s.id))
				continue;
			else{
				usedSegments.add(s.id);
				try {
					fwrite(fw,s);
					System.out.println("��������һ��");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.print("�����ˣ�traverse!");
					e.printStackTrace();
				}
				traverse(SegmentFactory.neighbors.get(n.id));
				layer--;
			}
		}
	}
	private void fwrite(FileWriter fw, Segment s) throws IOException {
		try {
			fw.write("LINESTRING ("+s.node1.toString()+", "+s.node2.toString()+")\r\n\r\n");
			System.out.println("��ӡ��Segment"+s.id);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print("�����ˣ�fwrite!");
			fw.close();
		}
		
	
	}

}
