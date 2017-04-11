package cpu.KNN;

	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.util.ArrayList;
	import java.util.List;
	// KNN算法测试类
public class TestKNN 
{
	/**
	* 从数据文件中读取数据
	* @param datas 存储数据的集合对象
	* @param path 数据文件的路径
	*/
	public void read(List<List<Double>> datas, String path)
	{
	   try 
	   {
	      BufferedReader br = new BufferedReader(new FileReader(new File(path)));
	      String reader = br.readLine();
	      while (reader != null)
	      {
	        String t[] = reader.split("\\*");
	        ArrayList<Double> list = new ArrayList<Double>();
	        for (int i = 0; i < t.length; i++)
	        {
	          list.add(Double.parseDouble(t[i]));	          
	        }
	        datas.add(list);
	        reader = br.readLine();
	      }
	   }
	   catch (Exception e) 
	   {
	      e.printStackTrace();
	   }
	}
	/**
	* 程序执行入口
	* @param args
	*/
//	public static void main(String[] args) 
//	{
//	    TestKNN t = new TestKNN();
//	    String datafile = new File("").getAbsolutePath() + File.separator + "cqudata\\datafile.txt";
//	    try 
//	    {
//	        List<List<Double>> datas = new ArrayList<List<Double>>();
//	        t.read(datas, datafile);
//	        KNN knn = new KNN();
//	            List<Double> test = "神速的返回函数";
//	            System.out.print("测试元组: ");
//	            for (int j = 0; j < test.size(); j++)
//	            {
//	              System.out.print(test.get(j) + " ");
//	            }
//	            System.out.print("类别为: ");
//	            System.out.println(Math.round(Float.parseFloat((knn.knn(datas, test, 3)))));
//	     } 
//	    catch (Exception e) 
//	    {
//	      e.printStackTrace();
//	    }
//	}
//	

}
