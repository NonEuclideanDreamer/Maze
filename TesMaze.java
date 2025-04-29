//******************************************************************************************************************
// TesMaze.java
// Generate Mazes on a Tesselation (only Pinwheel so far)
// author: Non-Euclidean Dreamer
//*****************************************************************************************************333


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random; 
 
import javax.imageio.ImageIO; 
 
public class TesMaze {
	static String left="left",random="random";
	static int[] cl=circ("maroon", 510, 0, 204);
	int s=3, width=25600,  
			height=14400, 
			wc=new Color(255,255,255).getRGB();//1;// 
	static int		counter=0;
	static boolean print=false;
	String name="mazo"; 
	static DecimalFormat df=new DecimalFormat("0000"); 
	Tesselation tes;
	boolean[]walls;
	boolean[][]cells;
	ArrayList<Integer>places=new ArrayList<Integer>(); 
 
	public static void main(String[] args) {
	
		for(int i=0;i<1;i++) {System.out.println("start");
		TesMaze maze=new TesMaze(false);
		maze.deepen(3);
		BufferedImage  image=maze.draw();//new BufferedImage(maze.width*maze.size,maze.height*maze.size,BufferedImage.TYPE_4BYTE_ABGR);//
		int[]maxdist=new int[maze.tes.centers.length];
	/*	for(int j=0;j<maze.tes.centers.length;j+=1)
			{
				int[]dist=maze.dist(j,maxdist);
			//	maze.drawDist(image, dist);
			}
		maze.drawDist(image, maxdist);*/ 
	int[] loc= {0,0,1};//loc,wall,dir
	for(int t=0;t<1;t++)
	{
		int[]dist=maze.dist(loc[0],maxdist);
		maze.drawDist(image, dist);
//		for(int k=0;k<10;k++)
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
		
		int k=0, j=loc[1]; if(mode==random) {Random rand=new Random();j=rand.nextInt(4);}
		int cand;
		while(k<4) {
		int or=(k*loc[2]+j+4)%4;
		cand=tes.nb[loc[0]][or];
		//print(cand);
		try {
		if(	walls[Math.abs(tes.faces[loc[0]][2*or+1])-1]){loc[2]*=-1*Math.signum(tes.faces[loc[0]][2*or+1])*Math.signum(tes.faces[cand][2*tes.nb[loc[0]][4+or]+1]);loc[1]=(tes.nb[loc[0]][4+or]+loc[2])%4; loc[0]=cand; return;}
		else k++;}
		catch(IndexOutOfBoundsException e) {k++;}
		}
	}

	public int[] dist(int x, int[] maxdist)
	{
		places.add(x);
		int[]out=new int[tes.centers.length];
		out[x]=1;
		int max=0;
		while(places.size()>0)
		{
			int i=0, k=0, j=0;
			int loc=places.get(i),cand;
			while(k<4)
			{
				int n=(k+j)%4;
				cand=tes.nb[loc][n];
				try {
					if(out[cand]>0||walls[Math.abs(tes.faces[loc][2*n+1])-1]==false) {k++;}
					else {
						out[cand]=out[loc]+1;
						max=Math.max(max, out[loc]);
						places.add(cand);
						k++;
					}
				}
				catch(IndexOutOfBoundsException ex) {k++;}
			}
			places.remove(i);
		}
		maxdist[x]=max;
		return out;
	}
	
	private BufferedImage draw() {
		return tes.draw(walls);
	}
	public void drawDist(BufferedImage image, int[] maxdist)
	{
		double factor=Math.max(0,6*64.0/width);
		for(int i=0;i<tes.centers.length;i++)
		{
			int c=cl[maxdist[i]%cl.length];
			tes.draw(i,image,c,wc);
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
	public TesMaze(boolean yes)
	{
		int x=2;
		Tesselation.width=width;
		Tesselation.height=height;
		Tesselation.name=name;
		tes=Tesselation.pinwheel(s);
		Random rand=new Random();
		int[]room=new int[tes.faces.length]; 
		int[]cells=new int[tes.faces.length];
		walls=new boolean[tes.edges.length];
		for(int i=0;i<tes.faces.length;i++)places.add(i);
		int i=rand.nextInt(places.size()),r=1,r0=1; cells[1]=1;
		int loc=places.get(i),cand;
		while(!places.isEmpty())
		{
			int k=0, j=rand.nextInt(4);
			boolean notfound=true;
			if(yes)draw();
			if(print)System.out.print(loc);
			room[loc]=r;
			while(k<4&&notfound)
			{
				int n=(k+j)%4;
				cand=tes.nb[loc][n];
				try {
					if(cells[room[cand]]==cells[r]) {k++;if(print)System.out.print(".");}//meet itself
					else if(room[cand]>0)//connect to a previously carved corridor
					{ 
					if(print)	System.out.println("meet corridor "+room[cand]+", delete wall "+(Math.abs(tes.faces[loc][2*n+1])-1));
						walls[Math.abs(tes.faces[loc][2*n+1])-1]=true;
						notfound=false; 
						connect(cells,cells[r],cells[room[cand]],r0);
						i=rand.nextInt(places.size());
						loc=places.get(i);
						if(r==r0)
						r0++;
						if(room[loc]==0) {r=r0;cells[r]=r;}
						else r=room[loc];
					}
					else {
					if(print)System.out.println("carry on"+", delete wall "+(Math.abs(tes.faces[loc][2*n+1])-1));
						walls[Math.abs(tes.faces[loc][2*n+1])-1]=true;
						notfound=false;
						i=findIndex(places,cand);
						loc=places.get(i);
					}
				}
				catch(IndexOutOfBoundsException out) {k++;if(print)System.out.print(",");}
			}
			if(notfound) {
				places.remove(i);
			if(!places.isEmpty()) {	i=rand.nextInt(places.size());	loc=places.get(i);}
			if(r==r0)
				r0++;
				if(room[loc]==0) {r=r0;cells[r]=r;}
				else r=room[loc];
			if(print)	System.out.println("corridor finished, start corridor "+r);
			}
		}
	}

	public TesMaze(Tesselation tes) {
		int x=2;
		Tesselation.width=width;
		Tesselation.height=height;
		Tesselation.name=name;
		Random rand=new Random();
		int[]room=new int[tes.faces.length]; 
		int[]cells=new int[tes.faces.length];
		walls=new boolean[tes.edges.length];
		for(int i=0;i<tes.faces.length;i++)places.add(i);
		int i=rand.nextInt(places.size()),r=1,r0=1; cells[1]=1;
		int loc=places.get(i),cand;
		while(!places.isEmpty())
		{
			int k=0, j=rand.nextInt(4);
			boolean notfound=true;
			if(print)System.out.print(loc);
			room[loc]=r;
			while(k<4&&notfound)
			{
				int n=(k+j)%4;
				cand=tes.nb[loc][n];
				try {
					if(cells[room[cand]]==cells[r]||!tes.offlimits[Math.abs(tes.faces[loc][2*n+1])-1]) {k++;if(print)System.out.print(".");}//meet itself
					else if(room[cand]>0)//connect to a previously carved corridor
					{ 
					if(print)	System.out.println("meet corridor "+room[cand]+", delete wall "+(Math.abs(tes.faces[loc][2*n+1])-1));
						walls[Math.abs(tes.faces[loc][2*n+1])-1]=true;
						notfound=false; 
						connect(cells,cells[r],cells[room[cand]],r0);
						i=rand.nextInt(places.size());
						loc=places.get(i);
						if(r==r0)
						r0++;
						if(room[loc]==0) {r=r0;cells[r]=r;}
						else r=room[loc];
					}
					else {
					if(print)System.out.println("carry on"+", delete wall "+(Math.abs(tes.faces[loc][2*n+1])-1));
						walls[Math.abs(tes.faces[loc][2*n+1])-1]=true;
						notfound=false;
						i=findIndex(places,cand);
						loc=places.get(i);
					}
				}
				catch(IndexOutOfBoundsException out) {k++;if(print)System.out.print(",");}
			}
			if(notfound) {
				places.remove(i);
			if(!places.isEmpty()) {	i=rand.nextInt(places.size());	loc=places.get(i);}
			if(r==r0)
				r0++;
				if(room[loc]==0) {r=r0;cells[r]=r;}
				else r=room[loc];
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

	private int findIndex(ArrayList<Integer> places2, int cand) {
		for(int i=0;i<places2.size();i++) { int c=places2.get(i);
		if(c==cand)
		return i;}
		System.out.println("indexfail");
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
	
	
	
	public TesMaze deepen(int steps)
	{
		tes.offlimits=walls;
		Tesselation tess=Tesselation.pinwheel(tes,steps);
		
		TesMaze maze=new TesMaze(tess);
		
		return maze;
	}

}
