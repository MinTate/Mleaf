package com.leaf;

import java.awt.image.BufferedImage;


public class Color {

    public static double[][] getColormoment(BufferedImage img) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        double[][] colormoment = new double[3][3];   
        int r, g, b; 
        int white=0;
        double ur=0,ug=0,ub=0;
        double	or=0,og=0,ob=0;
        double	sr=0,sg=0,sb=0;
        int pix[] = new int[w*h];   
        pix = img.getRGB(0, 0, w, h, pix, 0, w);  
        for(int i=0; i<w*h; i++) {  
            r = pix[i]>>16 & 0xff;  
            g = pix[i]>>8 & 0xff;  
            b = pix[i] & 0xff;  
            if(r==255&&g==255&&b==255){
            	white++;continue;
            }
            ur+=r;
            ug+=g;
            ub+=b;
        }  
        ur=ur/(w*h-white);
        ug=ug/(w*h-white);
        ub=ub/(w*h-white);
        for(int i=0; i<w*h; i++) {  
            r = pix[i]>>16 & 0xff;  
            g = pix[i]>>8 & 0xff;  
            b = pix[i] & 0xff;  
            if(r==255&&g==255&&b==255){
            	continue;
            }
            or+=(r-ur)*(r-ur);
            og+=(g-ug)*(g-ug);
            ob+=(b-ub)*(r-ub);
            sr+=(r-ur)*(r-ur)*(r-ur);
            sg+=(g-ug)*(g-ug)*(g-ug);
            sb+=(b-ub)*(r-ub)*(r-ur);
        }  
        or=Math.sqrt(or/(w*h-white));
        og=Math.sqrt(og/(w*h-white));
        ob=Math.sqrt(ob/(w*h-white));
        sr=sr/(w*h-white);
        sg=sg/(w*h-white);
        sb=sb/(w*h-white);
        if (sr>0) {
			sr=StrictMath.pow(sr, 1.0f/3);
		}
        else sr=-StrictMath.pow(-sr, 1.0f/3);
        if (sg>0) {
			sg=StrictMath.pow(sg, 1.0f/3);
		}
        else sg=-StrictMath.pow(-sg, 1.0f/3);
        if (sb>0) {
			sb=StrictMath.pow(sb, 1.0f/3);
		}
        else sb=-StrictMath.pow(-sb, 1.0f/3);
        colormoment[0][0]=ur;
        colormoment[0][1]=or;
        colormoment[0][2]=sr;
        colormoment[1][0]=ug;
        colormoment[1][1]=og;
        colormoment[1][2]=sg;
        colormoment[2][0]=ub;
        colormoment[2][1]=ob;
        colormoment[2][2]=sb;
        return  colormoment;  
    }  
}
