package com.leaf;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
public class EdgeDetector {	 
		public EdgeDetector() {                 //设置边缘检测参数
			threshold1 = 50;
			threshold2 = 230;
			setThreshold(128);
			setWidGaussianKernel(15);
		}
		public static  void process() throws EdgeDetectorException {               
			if (threshold < 0 || threshold > 255)                                 //异常处理
				throw new EdgeDetectorException("The value of the threshold is out of its valid range.");
			if (widGaussianKernel < 3 || widGaussianKernel > 40)
				throw new EdgeDetectorException("The value of the widGaussianKernel is out of its valid range.");
			width = sourceImage.getWidth(null);                                   //获取源图片的宽
			height = sourceImage.getHeight(null); 
			picsize = width * height;
			data = new int[picsize];
			magnitude = new int[picsize];
			orientation = new int[picsize];
			float f = 1.0F;
			canny_core(f, widGaussianKernel);
			thresholding_tracker(threshold1, threshold2);
			for (int i = 0; i < picsize; i++)
				if (data[i] > threshold)
					data[i] = 0xff000000;
				else
					data[i] = -1;
		      data=findcondour();
		      ArrayList<Integer> list = new ArrayList<Integer>();
			   for (int i = 0; i < picsize; i++)
			       if (data[i] !=-1)
			          list.add(i);
			int[][]result=new int[list.size()][2];
			for (int i = 0; i < list.size(); i++){
			    result[i][0]=list.get(i)/width;
			    result[i][1]=list.get(i)-(width*result[i][0]);
			}
			edge=new int[height][width];
			for (int i = 0; i <list.size(); i++) 
			{
				edge[result[i][0]][result[i][1]]=1;
			}
			data = null;
			magnitude = null;
			orientation = null;
			toHuJu();
		}
	 	
		private static void canny_core(float f, int i) {                            //canny算法
			derivative_mag = new int[picsize];
			float af4[] = new float[i];
			float af5[] = new float[i];
			float af6[] = new float[i];
			data = image2pixels(sourceImage);
			int k4 = 0;
			do {
				if (k4 >= i)
					break;
				float f1 = gaussian(k4, f);
				if (f1 <= 0.005F && k4 >= 2)
					break;
				float f2 = gaussian((float) k4 - 0.5F, f);
				float f3 = gaussian((float) k4 + 0.5F, f);
				float f4 = gaussian(k4, f * 0.5F);
				af4[k4] = (f1 + f2 + f3) / 3F / (6.283185F * f * f);
				af5[k4] = f3 - f2;
				af6[k4] = 1.6F * f4 - f1;
				k4++;
			} while (true);
			int j = k4;
			float af[] = new float[picsize];
			float af1[] = new float[picsize];
			int j1 = width - (j - 1);
			int l = width * (j - 1);
			int i1 = width * (height - (j - 1));
			for (int l4 = j - 1; l4 < j1; l4++) {
				for (int l5 = l; l5 < i1; l5 += width) {
					int k1 = l4 + l5;
					float f8 = (float) data[k1] * af4[0];
					float f10 = f8;
					int l6 = 1;
					int k7 = k1 - width;
					for (int i8 = k1 + width; l6 < j; i8 += width) {
						f8 += af4[l6] * (float) (data[k7] + data[i8]);
						f10 += af4[l6] * (float) (data[k1 - l6] + data[k1 + l6]);
						l6++;
						k7 -= width;
					}
	 
					af[k1] = f8;
					af1[k1] = f10;
				}
	 
			}
	 
			float af2[] = new float[picsize];
			for (int i5 = j - 1; i5 < j1; i5++) {
				for (int i6 = l; i6 < i1; i6 += width) {
					float f9 = 0.0F;
					int l1 = i5 + i6;
					for (int i7 = 1; i7 < j; i7++)
						f9 += af5[i7] * (af[l1 - i7] - af[l1 + i7]);
	 
					af2[l1] = f9;
				}
	 
			}
	 
			af = null;
			float af3[] = new float[picsize];
			for (int j5 = k4; j5 < width - k4; j5++) {
				for (int j6 = l; j6 < i1; j6 += width) {
					float f11 = 0.0F;
					int i2 = j5 + j6;
					int j7 = 1;
					for (int l7 = width; j7 < j; l7 += width) {
						f11 += af5[j7] * (af1[i2 - l7] - af1[i2 + l7]);
						j7++;
					}
	 
					af3[i2] = f11;
				}
	 
			}
	 
			af1 = null;
			j1 = width - j;
			l = width * j;
			i1 = width * (height - j);
			for (int k5 = j; k5 < j1; k5++) {
				for (int k6 = l; k6 < i1; k6 += width) {
					int j2 = k5 + k6;
					int k2 = j2 - width;
					int l2 = j2 + width;
					int i3 = j2 - 1;
					int j3 = j2 + 1;
					int k3 = k2 - 1;
					int l3 = k2 + 1;
					int i4 = l2 - 1;
					int j4 = l2 + 1;
					float f6 = af2[j2];
					float f7 = af3[j2];
					float f12 = hypotenuse(f6, f7);
					int k = (int) ((double) f12 * 20D);
					derivative_mag[j2] = k >= 256 ? 255 : k;
					float f13 = hypotenuse(af2[k2], af3[k2]);
					float f14 = hypotenuse(af2[l2], af3[l2]);
					float f15 = hypotenuse(af2[i3], af3[i3]);
					float f16 = hypotenuse(af2[j3], af3[j3]);
					float f18 = hypotenuse(af2[l3], af3[l3]);
					float f20 = hypotenuse(af2[j4], af3[j4]);
					float f19 = hypotenuse(af2[i4], af3[i4]);
					float f17 = hypotenuse(af2[k3], af3[k3]);
					float f5;
					if (f6 * f7 <= (float) 0
						? Math.abs(f6) >= Math.abs(f7)
						? (f5 = Math.abs(f6 * f12))
							>= Math.abs(f7 * f18 - (f6 + f7) * f16)
						&& f5
							> Math.abs(f7 * f19 - (f6 + f7) * f15) : (
								f5 = Math.abs(f7 * f12))
							>= Math.abs(f6 * f18 - (f7 + f6) * f13)
						&& f5
							> Math.abs(f6 * f19 - (f7 + f6) * f14) : Math.abs(f6)
							>= Math.abs(f7)
							? (f5 = Math.abs(f6 * f12))
								>= Math.abs(f7 * f20 + (f6 - f7) * f16)
						&& f5
							> Math.abs(f7 * f17 + (f6 - f7) * f15) : (
								f5 = Math.abs(f7 * f12))
							>= Math.abs(f6 * f20 + (f7 - f6) * f14)
						&& f5 > Math.abs(f6 * f17 + (f7 - f6) * f13)) {
						magnitude[j2] = derivative_mag[j2];
						orientation[j2] = (int) (Math.atan2(f7, f6) * (double) 40F);
					}
				}
	 
			}
	 
			derivative_mag = null;
			af2 = null;
			af3 = null;
		}
	 

		private static float hypotenuse(float f, float f1) {
			if (f == 0.0F && f1 == 0.0F)
				return 0.0F;
			else
				return (float) Math.sqrt(f * f + f1 * f1);
		}
	 
		private static float gaussian(float f, float f1) {
			return (float) Math.exp((-f * f) / ((float) 2 * f1 * f1));
		}
	 
		private static void thresholding_tracker(int i, int j) {
			for (int k = 0; k < picsize; k++)
				data[k] = 0;
	 
			for (int l = 0; l < width; l++) {
				for (int i1 = 0; i1 < height; i1++)
					if (magnitude[l + width * i1] >= i)
						follow(l, i1, j);
	 
			}
	 
		}
	 
		private static boolean follow(int i, int j, int k) {
			int j1 = i + 1;
			int k1 = i - 1;
			int l1 = j + 1;
			int i2 = j - 1;
			int j2 = i + j * width;
			if (l1 >= height)
				l1 = height - 1;
			if (i2 < 0)
				i2 = 0;
			if (j1 >= width)
				j1 = width - 1;
			if (k1 < 0)
				k1 = 0;
			if (data[j2] == 0) {
				data[j2] = magnitude[j2];
				boolean flag = false;
				int l = k1;
				do {
					if (l > j1)
						break;
					int i1 = i2;
					do {
						if (i1 > l1)
							break;
						int k2 = l + i1 * width;
						if ((i1 != j || l != i)
							&& magnitude[k2] >= k
							&& follow(l, i1, k)) {
							flag = true;
							break;
						}
						i1++;
					} while (true);
					if (!flag)
						break;
					l++;
				}
				while (true);
				return true;
			} else {
				return false;
			}
		}

		private static int[] image2pixels(Image image) {                                     //转换为像素数组
			int ai[] = new int[picsize];
			PixelGrabber pixelgrabber =
				new PixelGrabber(image, 0, 0, width, height, ai, 0, width);
			try {
				pixelgrabber.grabPixels();
			} catch (InterruptedException interruptedexception) {
				interruptedexception.printStackTrace();
			}
			boolean flag = false;
			int k1 = 0;
			do {
				if (k1 >= 16)
					break;
				int i = (ai[k1] & 0xff0000) >> 16;
				int k = (ai[k1] & 0xff00) >> 8;
				int i1 = ai[k1] & 0xff;
				if (i != k || k != i1) {
					flag = true;
					break;
				}
				k1++;
			} while (true);
			if (flag) {
				for (int l1 = 0; l1 < picsize; l1++) {
					int j = (ai[l1] & 0xff0000) >> 16;
					int l = (ai[l1] & 0xff00) >> 8;
					int j1 = ai[l1] & 0xff;
					ai[l1] =
						(int) (0.29799999999999999D * (double) j
							+ 0.58599999999999997D * (double) l
							+ 0.113D * (double) j1);
				}
	 
			} else {
				for (int i2 = 0; i2 < picsize; i2++)
					ai[i2] = ai[i2] & 0xff;
	 
			}
			return ai;
		}
	 
		public static void setSourceImage(Image image) {
			sourceImage = image;
		}
		 		
		public static void setThreshold(int i) {
			threshold = i;
		}
	 
		public static void setWidGaussianKernel(int i) {
			widGaussianKernel = i;
		}
		
		public static int[] findcondour()
		{
			int []data1 = new int[picsize];
			for(int i=0;i<picsize;i++)
				data1[i]=data[i];
			int k=0;
			int l=0;
			    for(int i=0;i<height;i++)
			    {
			    	for(int j=0;j<width;j++)
			    	{
			    		
			    		if(data1[k]!=-1)
			    			l++;
			    		if(data1[k]!=-1&&l!=1)
			    			data1[k]=-1;
			    	     	k++;		
			    	}
			    	l=0;
			    }
			     k=picsize-1;
				 l=0;
			    for(int i=0;i<height;i++)
			    {
			    	for(int j=0;j<width;j++)
			    	{
			    		
			    		if(data[k]!=-1)
			    			l++;
			    		if(data[k]!=-1&&l==1)
			    			data1[k]=0xff000000;
			    	     	k--;		
			    	}
			    	l=0;
			    }
			    l=0;
			    for(int i=0;i<width;i++)
			    {
			    	k=i;
			    	for(int j=0;j<height;j++)
			    	{
			    		
			    		if(data[k]!=-1)
			    			l++;
			    		if(data[k]!=-1&&l==1)
			    			data1[k]=0xff000000;
			    	     	k=k+width;		
			    	}
			    	l=0;
			    }
			    l=0;
			    k=picsize-1;
			    for(int i=0;i<width;i++)
			    {
			    	   k=k-i;
			    	for(int j=0;j<height;j++)
			    	{
			    		
			    		if(data[k]!=-1)
			    			  l++;
			    		if(data[k]!=-1&&l==1)
			    			data1[k]=0xff000000;
			    	     	k=k-width;		
			    	}
			        k=picsize-1;
			    	l=0;
			    }
			    return data1;
		}
		
		public static double m( int p , int q ){
			int 	x , y;
			double 	sum = 0;
			for( x = 0 ; x < height ; x++ )
				for( y = 0 ; y < width ; y++ )
					sum += Math.pow(x,p) * Math.pow(y,q) * edge[x][y];
			return sum;
		}
		
		public static double miu( int p , int q ){
			int		x , y;
			double 	x0 , y0;
			double	sum = 0;
			x0 = m(1,0) / m(0,0);
			y0 = m(0,1) / m(0,0);		
			for( x = 0 ; x < height ; x++ )
				for( y = 0 ; y < width ; y++ )
					sum += Math.pow((x-x0), p) * Math.pow((y-y0), q) * edge[x][y];
			return sum;
		}
		
		public static double eda( int p , int q ){
			int g = p+q+2;
			return miu(p,q) / Math.sqrt( Math.pow( miu(0,0), g ) );
		}
	
		public static void toHuJu(){
			double	eda20 = eda(2,0);
			double	eda02 = eda(0,2);
			double 	eda22 = eda(2,2);
			double 	eda11 = eda(1,1);
			double	eda30 = eda(3,0);
			double	eda03 = eda(0,3);
			double	eda21 = eda(2,1);
			double	eda12 = eda(1,2);
			
			huJu[1]	=	eda20 + eda02;
			huJu[2]	=	(eda20-eda02) * (eda20-eda02)	+	4 * eda11 * eda11;
			huJu[3]	=	(eda30-3*eda12) * (eda30-3*eda12)	+	(eda03-3*eda21) * (eda03-3*eda21);
			huJu[4]	=	(eda30+eda12) * (eda30+eda12)	+	(eda21+eda03) * (eda21+eda03);
			huJu[5]	= 	(eda30-3*eda12) * (eda30+eda12) * (	(eda30+eda12)*(eda30+eda12) - 3*(eda21+eda03)*(eda21+eda03)	)
								+	(3*eda21-eda03) * (eda21+eda03) * (	3*(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	);
			huJu[6]	=	(eda20-eda02) * (	(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	)	+
								4 * eda11 * (eda30+eda12) * (eda21+eda03);
			huJu[7]	=	(3*eda21-eda03) * (eda30+eda12) * (	(eda30+eda12)*(eda30+eda12) - 3*(eda21+eda03)*(eda21+eda03)	)	+
								(3*eda12-eda30) * (eda03+eda21) * (	3*(eda30+eda12)*(eda30+eda12) - (eda21+eda03)*(eda21+eda03)	);	
		//	System.out.println("轮廓七个Hu矩特征值：");
			/*for( int j = 1 ; j <= 7 ; j++ )
				System.out.print(huJu[j]+" ");
			System.out.println("");*/
		}
			
		private static int edge[][];
	    public static double[]	huJu = new double[8];
		final static float ORIENT_SCALE = 40F;
		private static int height;
		private static int width;
		private static int picsize;
		private static int data[];
		private static int derivative_mag[];
		private static int magnitude[];
		private static int orientation[];
		private static Image sourceImage;                //源图片	
		private static int threshold1;
		private static int threshold2;
		private static int threshold;
		private static int widGaussianKernel;
	
	public static class EdgeDetectorException extends Exception     //异常处理
	{
		private static final long serialVersionUID = 2891147707698396369L;
	public EdgeDetectorException()
	  {
	  }
	  public EdgeDetectorException(String s)
	  {
	    super(s);
	  }
	}
		
	public static double[] getimage(Image image)
	{
	        new EdgeDetector();   
	        setSourceImage(image);              //设置边缘处理的参数
	        setThreshold(128);
	        setWidGaussianKernel(5);
	        try {
	            process();                       //进行边缘处理
	        }
	        catch(EdgeDetectorException e) {
	            System.out.println(e.getMessage());
	        }
		return huJu;  
	}
}
