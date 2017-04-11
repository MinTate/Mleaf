package com.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.leaf.Color;
import com.leaf.EdgeDetector;
import com.leaf.Vein;

import cpu.KNN.KNN;
import cpu.KNN.TestKNN;

public class LeafService {
	BufferedImage source, temp;
	private List<Double> Data = new ArrayList<Double>();
	
	public String leaf(String picturePath) {
		
		File sourceimage = new File(picturePath);
		try {
			source = ImageIO.read(sourceimage);
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		double[][] color = Color.getColormoment(source);
		
		Vein vein = new Vein(source);
		double[] w = vein.getT();
		
		double[] edge = EdgeDetector.getimage(source);
		
		for (int i = 0; i < color.length; i++) {
			for (int j = 0; j < color[i].length; j++) {
				Data.add(color[i][j]);
			}

		}
		for (int i = 0; i < w.length; i++) {
			Data.add(w[i]);
		}
		for (int j = 1; j < edge.length; j++) {
			Data.add(edge[j]);
		}

		TestKNN success = new TestKNN();
		String datafile ="F:\\MyEclipse 10\\Mleaf\\cqudata\\datafile.txt";
		try {
			List<List<Double>> datas = new ArrayList<List<Double>>();
			success.read(datas, datafile);
			KNN knn = new KNN();
			return readdescription(Math.round(Float.parseFloat((knn.knn(datas, Data, 3)))));
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	public String readdescription(int num) throws IOException {
        
		String description=" ";
		File dir = new File("F:\\MyEclipse 10\\Mleaf\\cqudata\\leaves description");
		if (!dir.exists() || dir.isFile()) {
			System.out.println("路径有问题！");
			System.exit(0);
		}
		String[] l = dir.list();
		File leafdir = new File(dir, l[num-1]);
		BufferedReader br = new BufferedReader(new FileReader(leafdir));// 构造一个BufferedReader类来读取文件
		String s ="";
		while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
			description=description+s+"|";
		}
		br.close();
		
		
		return description;
	}
}
