//******************************************************************************************************************
// Tesselation.java
// Generate Tesselation to create Grid, so far only Pinwheel
// author: Non-Euclidean Dreamer
//*****************************************************************************************************333


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

public class Tesselation 
{
	static int width=1080,height=1080, color=new Color(255,255,255).getRGB(),counter=0;
	static String name="pinwheel";
	static DecimalFormat df=new DecimalFormat("0000");
	double[][]corners,centers;
	int[][]		edges, 
			faces,
			nb;
	boolean[]offlimits;
	int[]ft,et;
	
	public static void main(String[] args)
	{
		Tesselation pinwheel=pinwheel(2);
		
		pinwheel.draw();
	}
	
	
	public Tesselation(double[][]c,double[][]cent,int[][]e, int[][]f)
	{
		corners=c;
		centers=cent;
		edges=e;
		faces=f;
	}
	
	public static Tesselation pinwheel(int depth)
	{
		if(depth==0) { double size=Math.min(width*0.249,height*0.49);double[]center= {width/2,height/2};
			Tesselation out= new Tesselation(new double[][] {{center[0]-2*size,center[1]-size},{center[0],center[1]-size},{center[0]+2*size,center[1]-size},{center[0]-2*size,center[1]+size},{center[0],center[1]+size},{center[0]+2*size,center[1]+size}},new double[][] {center,center},
					new int[][] {{0,1},{1,2},{2,3},{3,0},{3,4},{4,5},{5,2}},new int[][]{{0,1,1,2,2,3,3,4},{5,-6,4,-5,3,-3,2,-7}});
			out.et=new int[] {0,0,1,0,0,0,0};
			out.nb=new int[][] {{-1,-1,1,-1,0,0,2,0},{-1,-1,0,-1,0,0,2,0}};
			return out;
		}
		
		else {
			Tesselation prev=pinwheel(depth-1);
		//	prev.draw();
			int l=VG.sum(prev.et);
			double[][]cent=new double[prev.centers.length*5][2],c=new double[prev.corners.length+l*4+2*prev.centers.length][2];
			int[][]e=new int[prev.et.length+4*l+6*prev.centers.length][5], f=new int[cent.length][8],nb=new int[cent.length][8];
			int[]edgetype=new int[e.length],cumedge=new int[prev.et.length];
			
			for(int i=0;i<prev.corners.length;i++)
			{
				c[i]=prev.corners[i];
			}
			int j=0;
			for(int i=0;i<prev.edges.length;i++)
			{
				if(prev.et[i]==1)
				{
					c[prev.corners.length+4*j]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.2);
					c[prev.corners.length+4*j+1]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.4);
					c[prev.corners.length+4*j+2]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.6);
					c[prev.corners.length+4*j+3]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.8);
					
					cumedge[i]=j;
					
					e[4*j+i][0]=prev.edges[i][0];
					e[4*j+i][1]=prev.corners.length+4*j;
					e[4*j+i+1][0]=prev.corners.length+4*j;
					e[4*j+i+1][1]=prev.corners.length+4*j+1;
					e[4*j+i+2][0]=prev.corners.length+4*j+1;
					e[4*j+i+2][1]=prev.corners.length+4*j+2;
					e[4*j+i+3][0]=prev.corners.length+4*j+2;
					e[4*j+i+3][1]=prev.corners.length+4*j+3;
					e[4*j+i+4][0]=prev.corners.length+4*j+3;
					e[4*j+i+4][1]=prev.edges[i][1];
					for(int k=0;k<5;k++)edgetype[4*j+i+k]=0;
					
					j++;
				}
				else
				{
					cumedge[i]=j;
					e[4*j+i]=prev.edges[i];
					edgetype[4*j+i]=1;
				}
			}
			
			for(int i=0;i<prev.centers.length;i++)
			{
				
				//	for(int j1=0;j1<4;j1++)VG.print(prev.corners[prev.faces[i][2*j1]]);
				int latecorner=prev.corners.length+4*cumedge[ (Math.abs(prev.faces[i][5]))-1]+((int)Math.signum(prev.faces[i][5])+1)/2*3,
						edgecorner=prev.corners.length+4*cumedge[(int) (Math.abs(prev.faces[i][5]))-1]+2-((int)Math.signum(prev.faces[i][5])+1)/2;
		if(i==0) {VG.print(prev.faces[i]);
			VG.print(prev.edges[Math.abs( prev.faces[i][5])-1]);	//	
			VG.print(c[latecorner]);VG.print(c[edgecorner]);
		}
			
				//System.out.println(i+","+l+","+latecorner+","+prev.corners.length);
				c[prev.corners.length+4*l+2*i]=VG.interpolate(c[prev.faces[i][0]], c[latecorner], 0.5);
				c[prev.corners.length+4*l+2*i+1]=VG.interpolate(c[prev.faces[i][2]], c[prev.corners.length+4*l+2*i], 0.5);
				
				e[prev.et.length+4*l+6*i][0]=prev.faces[i][0];
				e[prev.et.length+4*l+6*i][1]=prev.corners.length+4*l+2*i;
				edgetype[prev.et.length+4*l+6*i]=0;
				
				e[prev.et.length+4*l+6*i+1][0]=prev.corners.length+4*l+2*i;
				e[prev.et.length+4*l+6*i+1][1]=latecorner;
				edgetype[prev.et.length+4*l+6*i+1]=0;
				
				e[prev.et.length+4*l+6*i+2][0]=prev.corners.length+4*l+2*i;
				e[prev.et.length+4*l+6*i+2][1]=prev.corners.length+4*l+2*i+1;
				edgetype[prev.et.length+4*l+6*i+2]=0;
				
				e[prev.et.length+4*l+6*i+3][0]=prev.corners.length+4*l+2*i+1;
				e[prev.et.length+4*l+6*i+3][1]=prev.faces[i][2];
				edgetype[prev.et.length+4*l+6*i+3]=0;
				
				e[prev.et.length+4*l+6*i+4][0]=latecorner;
				e[prev.et.length+4*l+6*i+4][1]=prev.faces[i][2];
				edgetype[prev.et.length+4*l+6*i+4]=1;
				
				e[prev.et.length+4*l+6*i+5][0]=prev.faces[i][2];
				e[prev.et.length+4*l+6*i+5][1]=edgecorner;
				edgetype[prev.et.length+4*l+6*i+5]=0;
				
				cent[5*i]=VG.interpolate(prev.corners[prev.faces[i][0]],prev.corners[prev.faces[i][6]],0.5);
				cent[5*i+1]=VG.interpolate(prev.corners[prev.faces[i][0]],prev.corners[prev.faces[i][2]],0.5);
				cent[5*i+2]=VG.interpolate(prev.corners[prev.faces[i][2]], c[latecorner], 0.5);
				cent[5*i+3]=cent[5*i+2];
				cent[5*i+4]=VG.interpolate(prev.corners[prev.faces[i][4]],prev.corners[prev.faces[i][2]],0.5);
				
				
				
				f[5*i][4]=prev.faces[i][0];
				f[5*i][1]=-1-(prev.et.length+4*l+6*i+1);
				f[5*i][2]=prev.corners.length+4*l+2*i;
				f[5*i][3]=-(prev.et.length+4*l+6*i+1);
				f[5*i][0]=latecorner;
				int a=Math.abs(prev.faces[i][7])-1,b=(int) Math.signum(prev.faces[i][7]);
				f[5*i][5]= ((4*cumedge[ a]+a+1)*-b);
				f[5*i][6]=prev.faces[i][6]; 
				a=Math.abs(prev.faces[i][5])-1;b=(int) Math.signum(prev.faces[i][5]);
				f[5*i][7]=((4*cumedge[ a]+2*(1+b)+a+1)*-b);
				nb[5*i][0]=5*i+2;
				nb[5*i][4]=3;
				nb[5*i][1]=5*i+1;
				nb[5*i][5]=3;
				if(prev.nb[i][3]==-1)nb[5*i][2]=-1;
				else{nb[5*i][2]=5*prev.nb[i][3]+(-10*prev.nb[i][7]*prev.nb[i][7]+28*prev.nb[i][7]+6)/6;
				nb[5*i][6]=2;}
				if(prev.nb[i][2]==-1)
				{ 
					nb[5*i][3]=-1;
					nb[5*i+3][0]=-1;
					nb[5*i+3][1]=-1;
					nb[5*i+4][0]=-1;
					nb[5*i+4][1]=-1;
				}
				else if(Math.signum(prev.faces[i][5])==Math.signum(prev.faces[prev.nb[i][2]][5]))
				{
					nb[5*i][3]=5*prev.nb[i][2];
					nb[5*i+3][0]=5*prev.nb[i][2]+3;
					nb[5*i+3][1]=5*prev.nb[i][2]+3;
					nb[5*i+4][0]=5*prev.nb[i][2]+4; 
					nb[5*i+4][1]=5*prev.nb[i][2]+4;
					nb[5*i][7]=3;
					nb[5*i+3][4]=0;
					nb[5*i+3][5]=1;
					nb[5*i+4][4]=0;
					nb[5*i+4][5]=1;
				}
				else
				{
					nb[5*i][3]=5*prev.nb[i][2]+4;
					nb[5*i+3][0]=5*prev.nb[i][2]+3;
					nb[5*i+3][1]=5*prev.nb[i][2]+4;
					nb[5*i+4][0]=5*prev.nb[i][2]+3;
					nb[5*i+4][1]=5*prev.nb[i][2];
					nb[5*i][7]=1;
					nb[5*i+3][4]=0;
					nb[5*i+3][5]=0;
					nb[5*i+4][4]=1;
					nb[5*i+4][5]=3;
				}
						
				f[5*i+1][0]=prev.corners.length+4*l+2*i;
				f[5*i+1][2]=prev.corners.length+4*l+2*i+1;
				f[5*i+1][4]=prev.faces[i][2];
				f[5*i+1][6]=prev.faces[i][0];
				f[5*i+1][1]=prev.et.length+4*l+6*i+2+1;
				f[5*i+1][3]=prev.et.length+4*l+6*i+3+1;
				a=Math.abs(prev.faces[i][1])-1;b=(int) Math.signum(prev.faces[i][1]);
				f[5*i+1][5]=(4*cumedge[a]+a+1)*-b;
				f[5*i+1][7]=prev.et.length+4*l+6*i+1;
				nb[5*i+1][0]=5*i+2;
				nb[5*i+1][4]=0;
				nb[5*i+1][1]=5*i+2;
				nb[5*i+1][5]=1;
				if(prev.nb[i][0]==-1)nb[5*i+1][2]=-1;
				else
				{
					nb[5*i+1][2]=5*prev.nb[i][0]+(-10*prev.nb[i][4]*prev.nb[i][4]+28*prev.nb[i][4]+6)/6;
					nb[5*i+1][6]=2;
				}
				nb[5*i+1][3]=5*i;
				nb[5*i+1][7]=1;
				
				
				f[5*i+2][0]=prev.corners.length+4*l+2*i;
				f[5*i+2][2]=prev.corners.length+4*l+2*i+1;
				f[5*i+2][4]=prev.faces[i][2];
				f[5*i+2][6]=latecorner;
				f[5*i+2][1]=prev.et.length+4*l+6*i+2+1;
				f[5*i+2][3]=prev.et.length+4*l+6*i+3+1;
				f[5*i+2][5]=-(prev.et.length+4*l+6*i+4+1);
				f[5*i+2][7]=-(prev.et.length+4*l+6*i+1+1);
				nb[5*i+2][0]=5*i+1;
				nb[5*i+2][4]=0;
				nb[5*i+2][1]=5*i+1;
				nb[5*i+2][5]=1;
				nb[5*i+2][2]=5*i+3;
				nb[5*i+2][6]=2;
				nb[5*i+2][3]=5*i;
				nb[5*i+2][7]=0;
				
				f[5*i+3][0]=edgecorner;
				f[5*i+3][2]=(edgecorner+latecorner)/2;
				f[5*i+3][6]=prev.faces[i][2];
				f[5*i+3][4]=latecorner;
				a=Math.abs(prev.faces[i][5])-1;b=(int) Math.signum(prev.faces[i][5]);
				f[5*i+3][1]=(4*cumedge[a]+a+2+1)*b;
				f[5*i+3][3]=(4*cumedge[a]+a+2+b+1)*b;
				f[5*i+3][5]=prev.et.length+4*l+6*i+4+1;
				f[5*i+3][7]=prev.et.length+4*l+6*i+5+1;
				nb[5*i+3][2]=5*i+2;
				nb[5*i+3][6]=2;
				nb[5*i+3][3]=5*i+4;
				nb[5*i+3][7]=3;
				
				f[5*i+4][0]=edgecorner;
				f[5*i+4][2]=(3*edgecorner-latecorner)/2;
				f[5*i+4][4]=prev.faces[i][4];
				f[5*i+4][6]=prev.faces[i][2]; 
				f[5*i+4][1]=(4*cumedge[a]+a+2-b+1)*-b; 
				f[5*i+4][3]=(4*cumedge[a]+a+2-2*b+1)*-b;
				a=Math.abs(prev.faces[i][3])-1;b=(int) Math.signum(prev.faces[i][3]);
				f[5*i+4][5]=(4*cumedge[a]+a+1)*-b;
				f[5*i+4][7]=prev.et.length+4*l+6*i+5+1;
				if(prev.nb[i][1]==-1)nb[5*i+4][2]=-1;
				else {
				nb[5*i+4][2]=5*prev.nb[i][1]+(-10*prev.nb[i][5]*prev.nb[i][5]+28*prev.nb[i][5]+6)/6;
				nb[5*i+4][6]=2;
				} 
				nb[5*i+4][3]=5*i+3;
				nb[5*i+4][7]=3;
			}
			
			
			Tesselation next=new Tesselation(c,cent, e, f);
			next.et=edgetype;
			next.nb=nb;for(int i=0;i<nb.length;i++)VG.print(nb[i]);
			return next;
		}
		
	}
	
	/*public static Tesselation Penrose(int depth)
	{
		if (depth==0) return new Tesselation(new double[] {},new double[] {0,0}, new int[][] { {0,1},{1,2},{2,3},{3,0}},new int[][] {{0,1,2,3}});
		
		else
		{
			Tesselation last=Penrose(depth-1);
			
			
		}
	}*/
	
	BufferedImage draw() {
		BufferedImage image=new BufferedImage(width,height, BufferedImage.TYPE_4BYTE_ABGR);
		double thikness=1.5;
		for(int i=0;i<edges.length;i++)
		{
				double x=corners[edges[i][0]][0],
						y=corners[edges[i][0]][1],
						delx=corners[edges[i][1]][0]-x,
						dely=corners[edges[i][1]][1]-y,
						norm=Math.sqrt(delx*delx+dely*dely),
						boundx=dely/norm*thikness,
						boundy=-delx/norm*thikness;
				
				for(int n=0;n<norm;n++)
				{
					try{image.setRGB((int)(x+delx*n/norm), (int)(y+dely*n/norm), color);}
					catch(ArrayIndexOutOfBoundsException e) {
						System.out.println(e.getMessage()+"," +(int)(x+delx*n/norm)+"," +(int)(y+dely*n/norm));
					}
				}
		}
		
		File file=new File(name+df.format(counter)+".png");
		try 
		{
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			//System.out.println("IOException");System.out.println("couldn't print");
			e.printStackTrace(); 
		}
		counter++;
		return image;
	}
	
	BufferedImage draw(boolean[]walls) {
		BufferedImage image=new BufferedImage(width,height, BufferedImage.TYPE_4BYTE_ABGR);
		double thikness=1.5;
		for(int i=0;i<edges.length;i++)if(!walls[i])
		{
				double x=corners[edges[i][0]][0],
						y=corners[edges[i][0]][1],
						delx=corners[edges[i][1]][0]-x,
						dely=corners[edges[i][1]][1]-y,
						norm=Math.sqrt(delx*delx+dely*dely);
				
				for(int n=0;n<norm;n++)
				{
					try{image.setRGB((int)(x+delx*n/norm), (int)(y+dely*n/norm), color);}
					catch(ArrayIndexOutOfBoundsException e) {
				//		System.out.println(e.getMessage()+"," +(int)(x+delx*n/norm)+"," +(int)(y+dely*n/norm));
					}
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


	public void draw(int i, BufferedImage image, int c, int wc) 
	{
		double[]loc=corners[faces[i][0]],a=VG.subtract(corners[faces[i][4]],loc),b=VG.subtract(corners[faces[i][6]],loc);
		double anorm=VG.norm(a)*1.4, bnorm=VG.norm(b)*1.4;
		for(int k=0;k<anorm;k++)
		{
			double bound=bnorm*(anorm-k)/anorm;
			for(int l=0;l<bound+1;l++)
			{
				int x=(int) (loc[0]+k*a[0]/anorm+l*b[0]/bnorm),
						y=(int)Math.round (loc[1]+k*a[1]/anorm+l*b[1]/bnorm);
			//	System.out.println(image.getRGB(x, y));
				if(image.getRGB(x, y)!=color)image.setRGB(x, y, c);
			}
		}
	}


	public static Tesselation pinwheel(Tesselation prev, int steps) 
	{
		
		if(steps==0)return prev;
		
		//	prev.draw();
			int l=VG.sum(prev.et);
			double[][]cent=new double[prev.centers.length*5][2],c=new double[prev.corners.length+l*4+2*prev.centers.length][2];
			int[][]e=new int[prev.et.length+4*l+6*prev.centers.length][5], f=new int[cent.length][8],nb=new int[cent.length][8];
			int[]edgetype=new int[e.length],cumedge=new int[prev.et.length];
			boolean[]offlimits=new boolean[e.length];
			
			for(int i=0;i<prev.corners.length;i++)
			{
				c[i]=prev.corners[i];
			}
			int j=0;
			for(int i=0;i<prev.edges.length;i++)
			{
				if(prev.et[i]==1)
				{
					c[prev.corners.length+4*j]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.2);
					c[prev.corners.length+4*j+1]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.4);
					c[prev.corners.length+4*j+2]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.6);
					c[prev.corners.length+4*j+3]=VG.interpolate(prev.corners[prev.edges[i][0]], prev.corners[prev.edges[i][1]], 0.8);
					
					cumedge[i]=j;
					
					e[4*j+i][0]=prev.edges[i][0];
					e[4*j+i][1]=prev.corners.length+4*j;
					e[4*j+i+1][0]=prev.corners.length+4*j;
					e[4*j+i+1][1]=prev.corners.length+4*j+1;
					e[4*j+i+2][0]=prev.corners.length+4*j+1;
					e[4*j+i+2][1]=prev.corners.length+4*j+2;
					e[4*j+i+3][0]=prev.corners.length+4*j+2;
					e[4*j+i+3][1]=prev.corners.length+4*j+3;
					e[4*j+i+4][0]=prev.corners.length+4*j+3;
					e[4*j+i+4][1]=prev.edges[i][1];
					for(int k=0;k<5;k++) {
						edgetype[4*j+i+k]=0;
						offlimits[4*j+i+k]=prev.offlimits[i];
					}
					
					j++;
				}
				else
				{
					cumedge[i]=j;
					e[4*j+i]=prev.edges[i];
					edgetype[4*j+i]=1;
					offlimits[4*j+i]=prev.offlimits[i];
				}
			}
			
			for(int i=0;i<prev.centers.length;i++)
			{
				
				//	for(int j1=0;j1<4;j1++)VG.print(prev.corners[prev.faces[i][2*j1]]);
				int latecorner=prev.corners.length+4*cumedge[ (Math.abs(prev.faces[i][5]))-1]+((int)Math.signum(prev.faces[i][5])+1)/2*3,
						edgecorner=prev.corners.length+4*cumedge[(int) (Math.abs(prev.faces[i][5]))-1]+2-((int)Math.signum(prev.faces[i][5])+1)/2;
		if(i==0) {VG.print(prev.faces[i]);
			VG.print(prev.edges[Math.abs( prev.faces[i][5])-1]);	//	
			VG.print(c[latecorner]);VG.print(c[edgecorner]);
		}
			
				//System.out.println(i+","+l+","+latecorner+","+prev.corners.length);
				c[prev.corners.length+4*l+2*i]=VG.interpolate(c[prev.faces[i][0]], c[latecorner], 0.5);
				c[prev.corners.length+4*l+2*i+1]=VG.interpolate(c[prev.faces[i][2]], c[prev.corners.length+4*l+2*i], 0.5);
				
				e[prev.et.length+4*l+6*i][0]=prev.faces[i][0];
				e[prev.et.length+4*l+6*i][1]=prev.corners.length+4*l+2*i;
				edgetype[prev.et.length+4*l+6*i]=0;
				offlimits[prev.et.length+4*l+6*i]=true;
				
				e[prev.et.length+4*l+6*i+1][0]=prev.corners.length+4*l+2*i;
				e[prev.et.length+4*l+6*i+1][1]=latecorner;
				edgetype[prev.et.length+4*l+6*i+1]=0;

				offlimits[prev.et.length+4*l+6*i+1]=true;
				
				e[prev.et.length+4*l+6*i+2][0]=prev.corners.length+4*l+2*i;
				e[prev.et.length+4*l+6*i+2][1]=prev.corners.length+4*l+2*i+1;
				edgetype[prev.et.length+4*l+6*i+2]=0;
				offlimits[prev.et.length+4*l+6*i+2]=true;
				
				e[prev.et.length+4*l+6*i+3][0]=prev.corners.length+4*l+2*i+1;
				e[prev.et.length+4*l+6*i+3][1]=prev.faces[i][2];
				edgetype[prev.et.length+4*l+6*i+3]=0;
				offlimits[prev.et.length+4*l+6*i+3]=true;
				
				e[prev.et.length+4*l+6*i+4][0]=latecorner;
				e[prev.et.length+4*l+6*i+4][1]=prev.faces[i][2];
				edgetype[prev.et.length+4*l+6*i+4]=1;
				offlimits[prev.et.length+4*l+6*i+4]=true;
				
				e[prev.et.length+4*l+6*i+5][0]=prev.faces[i][2];
				e[prev.et.length+4*l+6*i+5][1]=edgecorner;
				edgetype[prev.et.length+4*l+6*i+5]=0;
				offlimits[prev.et.length+4*l+6*i+5]=true;
				
				cent[5*i]=VG.interpolate(prev.corners[prev.faces[i][0]],prev.corners[prev.faces[i][6]],0.5);
				cent[5*i+1]=VG.interpolate(prev.corners[prev.faces[i][0]],prev.corners[prev.faces[i][2]],0.5);
				cent[5*i+2]=VG.interpolate(prev.corners[prev.faces[i][2]], c[latecorner], 0.5);
				cent[5*i+3]=cent[5*i+2];
				cent[5*i+4]=VG.interpolate(prev.corners[prev.faces[i][4]],prev.corners[prev.faces[i][2]],0.5);
				
				
				
				f[5*i][4]=prev.faces[i][0];
				f[5*i][1]=-1-(prev.et.length+4*l+6*i+1);
				f[5*i][2]=prev.corners.length+4*l+2*i;
				f[5*i][3]=-(prev.et.length+4*l+6*i+1);
				f[5*i][0]=latecorner;
				int a=Math.abs(prev.faces[i][7])-1,b=(int) Math.signum(prev.faces[i][7]);
				f[5*i][5]= ((4*cumedge[ a]+a+1)*-b);
				f[5*i][6]=prev.faces[i][6]; 
				a=Math.abs(prev.faces[i][5])-1;b=(int) Math.signum(prev.faces[i][5]);
				f[5*i][7]=((4*cumedge[ a]+2*(1+b)+a+1)*-b);
				nb[5*i][0]=5*i+2;
				nb[5*i][4]=3;
				nb[5*i][1]=5*i+1;
				nb[5*i][5]=3;
				if(prev.nb[i][3]==-1)nb[5*i][2]=-1;
				else{nb[5*i][2]=5*prev.nb[i][3]+(-10*prev.nb[i][7]*prev.nb[i][7]+28*prev.nb[i][7]+6)/6;
				nb[5*i][6]=2;}
				if(prev.nb[i][2]==-1)
				{ 
					nb[5*i][3]=-1;
					nb[5*i+3][0]=-1;
					nb[5*i+3][1]=-1;
					nb[5*i+4][0]=-1;
					nb[5*i+4][1]=-1;
				}
				else if(Math.signum(prev.faces[i][5])==Math.signum(prev.faces[prev.nb[i][2]][5]))
				{
					nb[5*i][3]=5*prev.nb[i][2];
					nb[5*i+3][0]=5*prev.nb[i][2]+3;
					nb[5*i+3][1]=5*prev.nb[i][2]+3;
					nb[5*i+4][0]=5*prev.nb[i][2]+4; 
					nb[5*i+4][1]=5*prev.nb[i][2]+4;
					nb[5*i][7]=3;
					nb[5*i+3][4]=0;
					nb[5*i+3][5]=1;
					nb[5*i+4][4]=0;
					nb[5*i+4][5]=1;
				}
				else
				{
					nb[5*i][3]=5*prev.nb[i][2]+4;
					nb[5*i+3][0]=5*prev.nb[i][2]+3;
					nb[5*i+3][1]=5*prev.nb[i][2]+4;
					nb[5*i+4][0]=5*prev.nb[i][2]+3;
					nb[5*i+4][1]=5*prev.nb[i][2];
					nb[5*i][7]=1;
					nb[5*i+3][4]=0;
					nb[5*i+3][5]=0;
					nb[5*i+4][4]=1;
					nb[5*i+4][5]=3;
				}
						
				f[5*i+1][0]=prev.corners.length+4*l+2*i;
				f[5*i+1][2]=prev.corners.length+4*l+2*i+1;
				f[5*i+1][4]=prev.faces[i][2];
				f[5*i+1][6]=prev.faces[i][0];
				f[5*i+1][1]=prev.et.length+4*l+6*i+2+1;
				f[5*i+1][3]=prev.et.length+4*l+6*i+3+1;
				a=Math.abs(prev.faces[i][1])-1;b=(int) Math.signum(prev.faces[i][1]);
				f[5*i+1][5]=(4*cumedge[a]+a+1)*-b;
				f[5*i+1][7]=prev.et.length+4*l+6*i+1;
				nb[5*i+1][0]=5*i+2;
				nb[5*i+1][4]=0;
				nb[5*i+1][1]=5*i+2;
				nb[5*i+1][5]=1;
				if(prev.nb[i][0]==-1)nb[5*i+1][2]=-1;
				else
				{
					nb[5*i+1][2]=5*prev.nb[i][0]+(-10*prev.nb[i][4]*prev.nb[i][4]+28*prev.nb[i][4]+6)/6;
					nb[5*i+1][6]=2;
				}
				nb[5*i+1][3]=5*i;
				nb[5*i+1][7]=1;
				
				
				f[5*i+2][0]=prev.corners.length+4*l+2*i;
				f[5*i+2][2]=prev.corners.length+4*l+2*i+1;
				f[5*i+2][4]=prev.faces[i][2];
				f[5*i+2][6]=latecorner;
				f[5*i+2][1]=prev.et.length+4*l+6*i+2+1;
				f[5*i+2][3]=prev.et.length+4*l+6*i+3+1;
				f[5*i+2][5]=-(prev.et.length+4*l+6*i+4+1);
				f[5*i+2][7]=-(prev.et.length+4*l+6*i+1+1);
				nb[5*i+2][0]=5*i+1;
				nb[5*i+2][4]=0;
				nb[5*i+2][1]=5*i+1;
				nb[5*i+2][5]=1;
				nb[5*i+2][2]=5*i+3;
				nb[5*i+2][6]=2;
				nb[5*i+2][3]=5*i;
				nb[5*i+2][7]=0;
				
				f[5*i+3][0]=edgecorner;
				f[5*i+3][2]=(edgecorner+latecorner)/2;
				f[5*i+3][6]=prev.faces[i][2];
				f[5*i+3][4]=latecorner;
				a=Math.abs(prev.faces[i][5])-1;b=(int) Math.signum(prev.faces[i][5]);
				f[5*i+3][1]=(4*cumedge[a]+a+2+1)*b;
				f[5*i+3][3]=(4*cumedge[a]+a+2+b+1)*b;
				f[5*i+3][5]=prev.et.length+4*l+6*i+4+1;
				f[5*i+3][7]=prev.et.length+4*l+6*i+5+1;
				nb[5*i+3][2]=5*i+2;
				nb[5*i+3][6]=2;
				nb[5*i+3][3]=5*i+4;
				nb[5*i+3][7]=3;
				
				f[5*i+4][0]=edgecorner;
				f[5*i+4][2]=(3*edgecorner-latecorner)/2;
				f[5*i+4][4]=prev.faces[i][4];
				f[5*i+4][6]=prev.faces[i][2]; 
				f[5*i+4][1]=(4*cumedge[a]+a+2-b+1)*-b; 
				f[5*i+4][3]=(4*cumedge[a]+a+2-2*b+1)*-b;
				a=Math.abs(prev.faces[i][3])-1;b=(int) Math.signum(prev.faces[i][3]);
				f[5*i+4][5]=(4*cumedge[a]+a+1)*-b;
				f[5*i+4][7]=prev.et.length+4*l+6*i+5+1;
				if(prev.nb[i][1]==-1)nb[5*i+4][2]=-1;
				else {
				nb[5*i+4][2]=5*prev.nb[i][1]+(-10*prev.nb[i][5]*prev.nb[i][5]+28*prev.nb[i][5]+6)/6;
				nb[5*i+4][6]=2;
				} 
				nb[5*i+4][3]=5*i+3;
				nb[5*i+4][7]=3;
			}
			
			
			Tesselation next=new Tesselation(c,cent, e, f);
			next.et=edgetype;
			next.nb=nb;for(int i=0;i<nb.length;i++)VG.print(nb[i]);
			next.offlimits=offlimits;
			return pinwheel(next,steps-1);
		
	}
}
