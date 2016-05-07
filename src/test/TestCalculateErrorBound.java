package test;

import algorithms.CLCA;

public class TestCalculateErrorBound {

	private static double mst = 0.001;

	private static double e1 = 0.00003;

	private static double e2 = 0.0005;

	private static int focusLeng = 4;

	private static int longestLeng = 10;

	private static int batchSize = 100;

	private static double s = -0.315;

	private static int h = 34;

	private static String input = "contextPasquier99.txt";

	private static String output;

	public static void main(String [] arg){
		
		//	create a instance
		CLCA clca= new CLCA(mst, input, output, null, e1, e2, focusLeng, longestLeng, batchSize, s, h);
		//run
		double error[] = clca.calculateErrorBound(longestLeng, s, h);
		System.out.println("error bound :");
		for(double d : error){
			System.out.println(d);
		}
		double cutoff[] = clca.calculateCutoffBound(error);
		System.out.println("cutoff bound :");
		for(double d : cutoff){
			System.out.println(d);
		}
		
	}


}
