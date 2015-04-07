package entity;

import java.util.ArrayList;
import java.util.List;

public class AdjRelationShip {
	public int id;//�Լ���id
	public int color=0;//����Լ�����ɫ�� 1 2 3 4
	public List<AdjRelationShip> neighbors= new ArrayList<AdjRelationShip>();
	
	public AdjRelationShip(int id)
	{
		this.id = id;
	}
	
	public String toString()
	{
		return String.format("%d %d", id,color);
	}
	
	
	public void setColor()
	{
		int []colorArr={-1,0,0,0,0,0,0,0,0,0,0};
		for(AdjRelationShip a:this.neighbors)
		{
			colorArr[a.color]=1;	
		}
		
		int i=1;
		while(i<=8)
		{
			if(colorArr[i]==0)
			{
				
				this.color=i;
				System.out.println(this.id+" "+this.color);
				break;
			}
			i++;
		}
	}

}
