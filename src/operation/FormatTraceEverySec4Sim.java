package operation;

import java.io.*;
import java.util.Stack;

/**
 * Created by ywj on 15/5/17.
 * Format the trace to the same time tix
 *
 * input: the format is
 * taxiid timestamp lon lat id time
 * the input is in order by the id and time
 75 1 8502.609533 15622.423333
 80 1 8503.553367 15650.831667
 85 1 8504.4972 15679.24
 90 1 8505.441033 15707.648333
 95 1 8506.384867 15736.056667
 100 1 8507.3287 15764.465
 105 1 8508.080429 15781.782857
 110 1 8508.544 15782.465
 115 1 8509.007571 15783.147143
 120 1 8509.471143 15783.829286
 The output format should be

 time, taxiid, lon, lat
 */
public class FormatTraceEverySec4Sim extends AbsReadFile {

    public static String outPath = "/Users/ywj/Documents/START_paper/traces/out/fullinsert/";

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
        Node currentNode = null;
        Node beforeCurrent = null;
        while (this.timeIdex>=0)
        {
            while (!nodeStack.isEmpty()) {
                beforeCurrent = currentNode;
                currentNode = nodeStack.pop();
                if (currentNode.time == this.timeIdex) {
                    this.outputNodes.push(currentNode);
                    this.timeIdex -= this.timeTix;
                }
            }
            //当nodeStack为空
            pushNodeToOutput(beforeCurrent,currentNode);
            beforeCurrent = currentNode;
            currentNode = this.outputNodes.peek();
            this.timeIdex -= this.timeTix;

        }

    }

    private void pushNodeToOutput(Node current, Node next) {
        int nextTime = next.time;
        double _lon = divideByRadio(timeIdex, current.time, next.time, current.lon, next.lon);
        double _lat = divideByRadio(timeIdex, current.time, next.time, current.lat, next.lat);
        Node _node = new Node(current.id, timeIdex, _lon, _lat);
        this.outputNodes.push(_node);


    }

    private double divideByRadio(int currentTime, int time1, int time2, double data1, double data2)
    {
        if(currentTime==time1)
            return data1;
        if(currentTime==time2)
            return data2;

        return (currentTime-time2)*(data1-data2)/(double)(time1-time2)+data2;
    }

    private static  int seed = 0;
    public void dealWithFile(File file) {
        FileReader fr;
        try {
            fr = new FileReader(file);
            seed = 0;
            BufferedReader reader = new BufferedReader(fr);
            String line;
            //读入文件
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(" ");
                int id = Integer.parseInt(s[1]);
                int time = Integer.parseInt(s[0]);
                double lon = Double.parseDouble(s[2]);
                double lat = Double.parseDouble(s[3]);

                if(currentId!=id)
                {
                    //deal with current stack
                    seed++;
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
                nodeStack.push(new Node(seed, time, lon, lat));
            }
            reader.close();
            fr.close();



        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        FormatTraceEverySec4Sim ft = new FormatTraceEverySec4Sim();

        try {
            String inPath = "/Users/ywj/Documents/START_paper/traces/out/insert/";
            ft.readfile(inPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
