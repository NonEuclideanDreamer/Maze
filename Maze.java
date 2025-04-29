//******************************************************************************************************************
// Maze.java
// Generate Mazes on a Square Grid
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

public class Maze {
	static String left="left",random="random";
	static int[] cl=circ("petrol", 500, 0, 50);
	int s=40, width=1080/s,
			height=1080/s,
			size=s,
			wc=new Color(128,0,255).getRGB();//1;//
	static int		counter=0;
	static boolean print=false;
	String name="triggrow";
	static DecimalFormat df=new DecimalFormat("000");
	boolean[][][]walls;
	boolean[][]cells;
	ArrayList<int[]>places=new ArrayList<int[]>();

	public static void main(String[] args) {
	
		for(int i=0;i<1;i++) {System.out.println("start");
		Maze maze=new Maze(true);
		BufferedImage maxdist=new BufferedImage(maze.width,maze.height,BufferedImage.TYPE_4BYTE_ABGR), image=maze.draw();//new BufferedImage(maze.width*maze.size,maze.height*maze.size,BufferedImage.TYPE_4BYTE_ABGR);//
	/*for(int j=0;j<maze.width;j+=1)
			for(int k=0;k<maze.height;k+=1)
			{
				int l=k; if(j%2==1)l=maze.height-1-l;
				int[][]dist=maze.dist(j,l,maxdist);
			//	maze.drawDist(image, dist);
			}*/
	//print(maxdist,i);
	int[]loc= {0,0,0};
	for(int t=0;t<00;t++)
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
		int[]cand=new int[2];
		while(k<4) {
		int or=(k+j)%2,dir=(k+j)/2%2;
		cand[or]=(int) (loc[or]+Math.pow(-1, dir));
		cand[1-or]=loc[1-or];
		//print(cand);
		try {
		if(	walls[loc[0]-(1-or)*dir][loc[1]-or*dir][1-or]){loc[0]=cand[0]; loc[1]=cand[1]; loc[2]=(k+j+3)%4;return;}
		else k++;}
		catch(IndexOutOfBoundsException e) {k++;}
		}
	}

	public int[][] dist(int x, int y, BufferedImage maxdist)
	{
		places.add(new int[] {x,y});
		int[][]out=new int[width][height];
		out[x][y]=1;
		int max=0;
		while(places.size()>0)
		{
			int i=0, k=0, j=0;
			int[]loc=places.get(i),cand=new int[2];
			while(k<4)
			{
				int or=(k+j)%2,dir=(k+j)/2%2;
				cand[or]=(int) (loc[or]+Math.pow(-1, dir));
				cand[1-or]=loc[1-or];
				try {
					if(out[cand[0]][cand[1]]>0||walls[loc[0]-(1-or)*dir][loc[1]-or*dir][1-or]==false) {k++;}
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
		maxdist.setRGB(x, y, spectrum(max));
		return out;
	}
	
	private BufferedImage draw() {
		BufferedImage image=new BufferedImage(width*size,height*size, BufferedImage.TYPE_4BYTE_ABGR);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
			{
				if(j<height-1&&!walls[i][j][0])for(int k=size*i;k<size*(i+1);k++)
				{
					image.setRGB(k, size*(j+1), wc);
					image.setRGB(k, size*(j+1)+1, wc);
					image.setRGB(k, size*(j+1)-1, wc);
				}
				if(i<width-1&&!walls[i][j][1])for(int k=size*j;k<size*(j+1);k++)
				{
					image.setRGB(size*(i+1),k,  wc);
					image.setRGB( size*(i+1)+1,k, wc);
					image.setRGB(size*(i+1)-1, k, wc);
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
		double factor=Math.max(0,6*64.0/width);
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
			{
				int c=cl[dist[i][j]%cl.length];//spectrum((int) (dist[i][j]*factor));
				for(int k=i*size;k<(i+1)*size;k++)
					for(int l=j*size;l<(j+1)*size;l++)
					if(image.getRGB(k, l)!=wc)	image.setRGB(k,l,c);
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
	public Maze(boolean yes)
	{
		int x=2;
		Random rand=new Random();
		int[][]room=new int[width][height];
		int[]cells=new int[width*height/x];
		walls=new boolean[width][height][2];
		for(int i=0;i<width;i++)for(int j=0;j<height;j++)places.add(new int[] {i,j});
		int i=rand.nextInt(places.size()),r=1,r0=1; cells[1]=1;
		int[]loc=places.get(i),cand=new int[2];
		while(!places.isEmpty())
		{
			int k=0, j=rand.nextInt(4);
			boolean notfound=true;
			if(yes)draw();
			if(print)print(loc);
			room[loc[0]][loc[1]]=r;
			while(k<4&&notfound)
			{
				int or=(k+j)%2,dir=(k+j)/2%2;
				cand[or]=(int) (loc[or]+Math.pow(-1, dir));
				cand[1-or]=loc[1-or];
				try {
					if(cells[room[cand[0]][cand[1]]]==cells[r]) {k++;if(print)System.out.print(".");}//meet itself
					else if(room[cand[0]][cand[1]]>0)//connect to a previously carved corridor
					{
					if(print)	System.out.println("meet corridor "+room[cand[0]][cand[1]]);
						walls[loc[0]-(1-or)*dir][loc[1]-or*dir][1-or]=true;
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
					if(print)System.out.println("carry on");
						walls[loc[0]-(1-or)*dir][loc[1]-or*dir][1-or]=true;
						notfound=false;
						i=findIndex(places,cand);
						loc=places.get(i);
					}
				}
				catch(IndexOutOfBoundsException out) {k++;}
			}
			if(notfound) {
				places.remove(i);
			if(!places.isEmpty()) {	i=rand.nextInt(places.size());	loc=places.get(i);}
			if(r==r0)
				r0++;
				if(room[loc[0]][loc[1]]==0) {r=r0;cells[r]=r;}
				else r=room[loc[0]][loc[1]];
			if(print)	System.out.println("corridor finished, start corridor "+r);
			}
		}
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

	public Maze()
	{
		walls=new boolean[width][height][2];
		cells=new boolean[width][height];
		Random rand=new Random();
		int x=rand.nextInt(width),
				y=rand.nextInt(height);
		places.add(new int[] {x,y});
		cells[x][y]=true;
		while(places.size()>0)
		{
			int i=(int)Math.pow(rand.nextDouble(Math.pow(places.size(),50)),1.0/80), k=0, j=rand.nextInt(4);
			boolean notfound=true;
			int[]loc=places.get(i),cand=new int[2];
			while(k<4&&notfound)
			{
				int or=(k+j)%2,dir=(k+j)/2%2;
				cand[or]=(int) (loc[or]+Math.pow(-1, dir));
				cand[1-or]=loc[1-or];
				try {
					if(cells[cand[0]][cand[1]]) {k++;}
					else {
						cells[cand[0]][cand[1]]=true;
						walls[loc[0]-(1-or)*dir][loc[1]-or*dir][1-or]=true;
						notfound=false;
						places.add(cand.clone());
					}
				}
				catch(IndexOutOfBoundsException out) {k++;}
			}
			if(notfound)places.remove(i);
		}
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
	
	static int[] circ(String string, int res, int offset, int height)
	{	
		int[]out=new int[res];
		if(string.equals("navy")||string.equals("lime")||string.equals("mint")||string.equals("maroon"))//height between -255&255
		{ 
			int i=0,j=2,k=1,sign=1;
			if(string.equals("navy")) {System.out.println("navy");i=2;j=0;k=1;sign=-1;}
			if(string.equals("mint")) {System.out.println("mint");i=2;j=0;k=1;sign=1;}
			else if(string.equals("maroon")) {System.out.println("maroon");i=0;j=2;k=1;sign=-1;}
			for(int c=0;c<res;c++)
			{ 
				
				double[]raw=new double[3];
				raw[i]=Math.cos(c*2*Math.PI/res+Math.PI*offset/510);
				raw[j]=Math.sin(c*2*Math.PI/res+Math.PI*offset/510)+height/128.0;
				raw[k]=sign*(Math.sin(c*2*Math.PI/res+Math.PI*offset/510)-height/128.0);
				double max=Math.max(Math.abs(raw[i]),Math.max(Math.abs(raw[j]), Math.abs(raw[k])));
				
					out[c]=new Color((int)(128+127.999*raw[0]/max),(int)(128+127.999*raw[1]/max),(int)(128+127.999*raw[2]/max)).getRGB();
				//System.out.print("{"+out[c][0]+","+out[c][1]+","+out[c][2]+"},");

			}	
			
		}
		else if(string.equals("oliv")||string.equals("petrol")||string.equals("purple"))//face
		{
			int i=2,j=0,k=1,step=4*255/res,loc=offset,stage=0,sign=1,bound=0;
			if(string.equals("oliv")){ i=0;j=1;k=2;}
			else if(string.equals("petrol")) {i=1;j=2;k=0;} 
			for(int c=0;c<res;c++)
			{
				int[]cl=new int[3];
				cl[i]=loc;
				cl[j]=bound;
				cl[k]=height;
				out[c]=new Color(cl[0],cl[1],cl[2]).getRGB();
				//System.out.print("{"+out[c][0]+","+out[c][1]+","+out[c][2]+"},");
				loc+=sign*step;
				if(loc>255||loc<0)
				{
					loc=(Math.abs(loc))%255;
					stage++;
					int temp=i;
					i=j;
					j=temp;
					if(stage>1)loc=255-loc;
					if(stage%2==1) {bound=255-bound;}
					else if(stage==2) {sign=-1;}
				}
			}
		}
		return out;
	}
}
