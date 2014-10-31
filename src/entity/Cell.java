package entity;

public class Cell implements Comparable<Cell>{
	public int x;
	public int y;
	public int num;
	public int cluster=-1;//�������ڵ������ţ� Ϊ-1ʱΪû�б�š�
	private static int clusterCount = 0;
	public static void addClusterCounter()
	{
		clusterCount++;
	}
	
	public void setCluster()
	{
		this.cluster=clusterCount;
	}
	
	public Cell(int x, int y, int num)
	{
		this.x = x;
		this.y = y;
		this.num = num;
	}
	
	public int compareTo(Cell c)
	{
		if(this.num==c.num)
			return 0;
		
		return this.num>c.num?1:-1;
			
	}
	
	public String toString()
	{
		return String.format("%d %d %d %d", this.x, this.y, this.num, this.cluster*100);
	}

	

}
