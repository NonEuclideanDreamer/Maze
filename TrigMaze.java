//******************************************************************************************************************
// TrigMaze.java
// Generate Mazes on a Triangular Grid
// author: Non-Euclidean Dreamer
//*****************************************************************************************************333


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

public class TrigMaze {
	static String left="left",random="random";
	static int[] cl=Maze.circ("purple", 250, 0, 255);
	int z=20,w=1080,h=1080;
	double s=w*1.0/z,size=s;
	int width=z*2+1,
			height=(int) (h*2/Math.sqrt(3)/s-100000)+100000,
			
			wc=new Color(0,0,254).getRGB();
	static int		counter=0;
	static boolean print=false;
	String name="triggrow";
	static DecimalFormat df=new DecimalFormat("000");
	boolean[][][]walls;
	boolean[][]cells;
	ArrayList<int[]>places=new ArrayList<int[]>();

	public static void main(String[] args) {
	
		for(int i=0;i<1;i++) {System.out.println("start");
		TrigMaze maze=new TrigMaze(true);
		BufferedImage image=maze.draw();//=new BufferedImage(maze.w,maze.h,BufferedImage.TYPE_4BYTE_ABGR);// , 
		int[][]maxdist=new int[maze.width+1][maze.height+1];
/*	for(int j=0;j<maze.width;j+=1)
			for(int k=0;k<maze.height;k+=1)
			{
				//int l=k; if(j%2==1)l=maze.height-1-l;
				int[][]dist=maze.dist(j,k,maxdist);
			//	maze.drawDist(image, dist);
			}
	maze.drawDist(image, maxdist);*/
	//print(maxdist,i);
	int[]loc= {0,0,0};
	for(int t=0;t<1440*0;t++)
	{
		int[][]dist=maze.dist(loc[0],loc[1],maxdist);
		maze.drawDist(image, dist);
		for(int k=0;k<10;k++)
		maze.walk(loc,left);
		maze.print(loc);
		System.out.println();
	}
			
		}
	}
	
	private static void print(BufferedImage image, int i) {
		File file=new File("maxdist"+df.format(i)+".png");
		try 
		{
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			System.out.println("IOException");System.out.println("couldn't print");
			e.printStackTrace(); 
		}
	}

	private void walk(int[] loc, String mode) {
		
		int k=0, j=loc[2]; if(mode==random) {Random rand=new Random();j=rand.nextInt(4);}
		int[]cand=new int[2],wall;
		while(k<3) {
			wall=wall(loc,(k+j)%3);
			cand=nb(loc,(k+j)%3);
		//print(cand);
		try {
		if(	walls[wall[0]][wall[1]][wall[2]]){loc[0]=cand[0]; loc[1]=cand[1]; loc[2]=(k+j+1)%3;return;}
		else k++;}
		catch(IndexOutOfBoundsException e) {k++;}
		}
	}

	public int[][] dist(int x, int y, int[][] maxdist)
	{
		places.add(new int[] {x,y});
		int[][]out=new int[width][height];
		out[x][y]=1;
		int max=0;
		while(places.size()>0)
		{
			int i=0, k=0, j=0;
			int[]loc=places.get(i),cand=new int[2],wall;
			while(k<3)
			{
				cand=nb(loc,k);
				wall=wall(loc,k);
				try {
					if(out[cand[0]][cand[1]]>0||walls[wall[0]][wall[1]][wall[2]]==false) {k++;}
					else {
						out[cand[0]][cand[1]]=out[loc[0]][loc[1]]+1;
						max=Math.max(max, out[loc[0]][loc[1]]);
						places.add(cand.clone());
					}
				}
				catch(IndexOutOfBoundsException ex) {k++;}
			}
			places.remove(i);
		}
		maxdist[x][y]= max;
		return out;
	}
	
	private int[] wall(int[] loc, int n) {
		int k=(loc[0]+loc[1])%2;
		if(n==0)return new int[] {(loc[0]-loc[1]%2)/2,loc[1],2};
		else if(n==1)return new int[] {loc[0]/2,loc[1]-k,0};
		else return new int[] {(loc[0]-1+loc[1]%2)/2,loc[1],1};
	}

	private BufferedImage draw() {
		BufferedImage image=new BufferedImage(w,h, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<z+1;i++)
			for(int j=0;j<height;j++)
			{
				if((i<z||j%2==0)&&!walls[i][j][0])for(int k=(int) Math.max(0, size*(2*i-1+(j)%2)/2);k<Math.min(image.getWidth(), size*(2*i+1+(j)%2)/2);k++)
				{
					int y=(int) (size*(j+1)*Math.sqrt(3)/2);//System.out.println("k="+k);
					try {	image.setRGB(k, y, wc);
					image.setRGB(k, y+1, wc);
					image.setRGB(k, y-1, wc);}catch(IndexOutOfBoundsException e) {}
				}
				if(!walls[i][j][1])for(int k=0;k<(size*Math.sqrt(3)/2);k++)
				{
					double x=size*(i+0.5*(1-j%2))+k/Math.sqrt(3),
							l=-k+size*(1+j)*Math.sqrt(3)/2;
							
					for(int ex=(int)(x+1.5);ex>x-1.5;ex--)
						try {
					image.setRGB(ex,(int)l,  wc);}catch(IndexOutOfBoundsException e) {}
				}
				if(!walls[i][j][2])for(int k=0;k<(size*Math.sqrt(3)/2);k++)
				{
					double x=size*(i+0.5*(j%2))+k/Math.sqrt(3),
							l=k+size*(j)*Math.sqrt(3)/2;
							
					for(int ex=(int)(x+1.5);ex>x-1.5;ex--)
						try {
					image.setRGB(ex,(int)l,  wc);}catch(IndexOutOfBoundsException e) {}
				}
			}
		
		File file=new File(name+df.format(counter)+".png");
		try 
		{
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			System.out.println("IOException");System.out.println("couldn't print");
			e.printStackTrace(); 
		}
		counter++;
		return image;
	}
	public void drawDist(BufferedImage image, int[][]dist)
	{
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
			{
				int c=cl[dist[i][j]%cl.length],//spectrum((int) (dist[i][j]*factor));
						x=(int) (i*size/2),
						y=(int) (((j+i)%2+j)*Math.sqrt(3)*size/2),
						sign=1-(i+j)%2*2;
				for(int k=0;k<Math.sqrt(3)*size/2+1;k++) {
					int xx=(int) (k/Math.sqrt(3));
					for(int l=-xx-1;l<xx+1;l++)
						try {
					if(image.getRGB(x+l, y+k*sign)!=wc)	image.setRGB(x+l,y+k*sign,c);}catch(ArrayIndexOutOfBoundsException e) {}}
			}
		File file=new File(name+df.format(counter)+".png");
		try 
		{
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			System.out.println("IOException");System.out.println("couldn't print");
			e.printStackTrace(); 
		}
		counter++;
	}
	public TrigMaze(boolean yes)
	{
		int x=2;
		Random rand=new Random();
		int[][]room=new int[width][height];
		int[]cells=new int[width*height/x];
		walls=new boolean[z+1][height][3];
		for(int i=0;i<width;i++)for(int j=0;j<height;j++)places.add(new int[] {i,j});
		int i=rand.nextInt(places.size()),r=1,r0=1; cells[1]=1;
		int[]loc=places.get(i),cand=new int[2];
		while(!places.isEmpty())
		{
			draw();
			int k=0, j=rand.nextInt(3);
			boolean notfound=true;
			
			if(print)print(loc);
			room[loc[0]][loc[1]]=r;
			while(k<3&&notfound)
			{
				int n=(k+j)%3;
				cand=nb(loc,n);
				int[]wall=wall(loc,n);
				try { //error if romm==-1(not a real cell)
					if(cells[room[cand[0]][cand[1]]]==cells[r]) {k++;if(print)System.out.print(".");}//meet itself
					else if(room[cand[0]][cand[1]]>0)//connect to a previously carved corridor
					{
					if(print)	System.out.println("meet corridor "+room[cand[0]][cand[1]]);
					
					
					walls[wall[0]][wall[1]][wall[2]]=true;
						notfound=false;
						connect(cells,cells[r],cells[room[cand[0]][cand[1]]],r0);
						i=rand.nextInt(places.size());
						loc=places.get(i);
						if(r==r0)
						r0++;
						if(room[loc[0]][loc[1]]==0) {r=r0;cells[r]=r;}
						else r=room[loc[0]][loc[1]];
					}
					else {
					if(print)System.out.println("carry on, n="+n);
					
			
						walls[wall[0]][wall[1]][wall[2]]=true;
						notfound=false;
						i=findIndex(places,cand);
						loc=places.get(i);
					}
				}
				catch(IndexOutOfBoundsException out) {k++;}//System.out.println(out.getMessage());
			}
			if(notfound) {
				places.remove(i);
			if(!places.isEmpty()) {	i=rand.nextInt(places.size());	loc=places.get(i);}
			if(r==r0)
				r0++;
				if(room[loc[0]][loc[1]]==0) 
				{r=r0;cells[r]=r;}
				else r=room[loc[0]][loc[1]];
			if(print)	System.out.println("corridor finished, start corridor "+r);
			}
		}
	}

	private int[] nb(int[] loc, int i) {
		i=i%3;
		int k=(loc[0]+loc[1])%2*2-1	;
	if(i==0)return new int[] {loc[0]-k,loc[1]};
	else if(i==1)return new int[] {loc[0],loc[1]-k};
	else return new int[] {loc[0]+k,loc[1]};
	}

	private void connect(int[] cel, int r, int s, int r0) 
	{
		if(r==0||s==0)System.out.println("fatal error");
		int a=Math.min(r, s), b=Math.max(r,s);
		for(int i=0;i<r0+1;i++)
		{
			if(cel[i]==b) cel[i]=a;
		}	
	}

	private void print(int[] loc) {
	System.out.print("{");
	for(int i=0;i<loc.length;i++)
		System.out.print(loc[i]+", ");
	System.out.print("}");
	}

	private int findIndex(ArrayList<int[]> list, int[] cand) {
		for(int i=0;i<list.size();i++) { int[]c=list.get(i);
		if(Arrays.equals(c,cand))
		return i;}
		return 100000;
	}
	
	public static int spectrum(int n)
	{
		n=n%(6*256);
		if (n<256)
			return new Color(255,n,0).getRGB();
		n-=256;
		if (n<256)
			return new Color(255-n,255,0).getRGB();
		n-=256;
		if (n<256)
			return new Color(0,255,n).getRGB();
		n-=256;
		if (n<256)
			return new Color(0,255-n,255).getRGB();
		n-=256;
		if (n<256)
			return new Color(n,0,255).getRGB();
		n-=256;
			return new Color(255,0,255-n).getRGB();
	}
}
