package operation;

import entity.TraceNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ywj on 15/5/17.
 * Format the trace to the same time tix
 *
 * input: the format is
 * taxiid timestamp lon lat id time
 * the input is in order by the id and time
 * 13331194377	20111109110113	8502.232	15611.06	1	73
 13331194377	20111109110143	8507.895	15781.51	1	103
 13331194377	20111109110211	8510.491	15785.33	1	131
 13331194377	20111109110243	8510.488	15787.88	1	163
 13331194377	20111109110313	8511.788	15789.15	1	193
 13331194377	20111109110343	8521.872	16074.51	1	223
 13331194377	20111109110415	8515.593	16454.85	1	255
 13331194377	20111109110444	8515.593	16454.85	1	284

 The output format should be

 time, taxiid, lon, lat
 */
public class FormatTrace4Sim extends AbsReadFile {

    public static String outPath = "/Users/ywj/Downloads/data/out/";

    public class Node{
        public int id;
        public int time;
        public double lon;
        public double lat;

        public Node(int id,int time, double lon, double lat)
        {
		this.id = id;
		this.time = time;
		this.lon = lon;
		this.lat = lat;
        }

        public String toString()
        {
            return String.format("%d %d %f %f\r\n", time, id, lon, lat);
        }
    }

    private Stack<Node> nodeStack = null;//对每个id重新初始化
    private int currentId = 0;

    //output
    public Stack<Node> outputNodes = new Stack<Node>();
    private int timeTix = 5;
    private int timeIdex;

    private int endTime = 7200;
    //for the current nodeStack
    //we insert the nodes
    private void insert() {
        System.out.println("inserting for node "+this.currentId);
        this.timeIdex = this.endTime;
        Node currentNode = nodeStack.pop();
        while (!nodeStack.isEmpty()) {
            Node nextNode = nodeStack.peek();
            //pop the same time node
            //make the nextNode and the current Node are of different time
            while (nextNode.time == currentNode.time) {
                nodeStack.pop();
                if(nodeStack.isEmpty())
                    return;
                nextNode = nodeStack.peek();
            }

            pushNodeToOutput(currentNode, nextNode);
            currentNode = nodeStack.pop();
        }
    }

    private void pushNodeToOutput(Node current, Node next) {
        int nextTime = next.time;
        while (this.timeIdex >= nextTime) {
            double _lon = divideByRadio(timeIdex, current.time, next.time, current.lon, next.lon);
            double _lat = divideByRadio(timeIdex, current.time, next.time, current.lat, next.lat);
            Node _node = new Node(current.id, timeIdex, _lon, _lat);
            this.outputNodes.push(_node);

            this.timeIdex -= this.timeTix;
        }

    }

    private double divideByRadio(int currentTime, int time1, int time2, double data1, double data2)
    {
        if(currentTime==time1)
            return data1;
        if(currentTime==time2)
            return data2;

        return (currentTime-time2)*(data1-data2)/(double)(time1-time2)+data2;
    }

    public void dealWithFile(File file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line;
            //读入文件
            while ((line = reader.readLine()) != null) {
                String[] s = line.split("\t");
                int id = Integer.parseInt(s[4]);
                int time = Integer.parseInt(s[5]);
                double lon = Double.parseDouble(s[2]);
                double lat = Double.parseDouble(s[3]);

                if(currentId!=id)
                {
                    //deal with current stack
                    if (currentId != 0) {
                        insert();
                        //output
                        File fileout = new File(outPath+file.getName());
                        FileWriter writer = new FileWriter(fileout,true);

                        while (!this.outputNodes.isEmpty())
                        {
                            writer.write(this.outputNodes.pop().toString());
                        }
                        writer.close();

                    }

                    //create a new stack
                    currentId = id;
                    this.nodeStack = new Stack<Node>();
                }


                //add line to the new stack
                nodeStack.push(new Node(id, time, lon, lat));
            }
            reader.close();
            fr.close();



        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        FormatTrace4Sim ft = new FormatTrace4Sim();

        try {
            String inPath = "/Users/ywj/Downloads/data/in/";
            ft.readfile(inPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
