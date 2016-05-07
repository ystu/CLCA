package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;



import algorithms.CLCA;
import algorithms.CustomizedApriori;
import itemset_array_integers_with_count.Itemsets;

/**
 * Example of how to use APRIORI
 *  algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class TestCA {
	private static double mst = 0.5;

	private static double e1 = 0.2;

	private static double e2 = 0.2;

	private static int focusLeng = 4;

	private static int longestLeng = 10;

	private static int batchSize = 10;

	private static double s = -0.56;

	private static double h = 19.5;

	private static String input = "lexico_test_kosarak.txt";

	private static String output;

	public static void main(String [] arg) throws IOException{

		
		String output = null;
		// Note : we here set the output file path to null
		// because we want that the algorithm save the 
		// result in memory for this example.
		
		
		
//		// Applying the Apriori algorithm
		CLCA clca= new CLCA(mst, input, output, null, e1, e2, focusLeng, longestLeng, batchSize, s, h);
		CustomizedApriori ca = new CustomizedApriori();

		double[] error = clca.calculateErrorBound(longestLeng, s, h);
		for(double d : error){
			System.out.println(d);
		}
		ArrayList<int[]> originalData;
		Itemsets candPattern;
		
		while(clca.isDataAvail()){	
			originalData = clca.getBatchSizeData(input, batchSize);
			clca.setTotalTrans(clca.getTotalTrans() + originalData.size());
			candPattern = ca.runCustomizedApriori(error, originalData, longestLeng);
			candPattern.printItemsets(1);//ca.getDatabaseSize());
			
		}
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = TestCA.class.getResource(filename);
		System.out.println(java.net.URLDecoder.decode(url.getPath(),"UTF-8"));
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
