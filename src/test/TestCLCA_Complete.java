package test;

import java.io.IOException;

import algorithms.CLCA;

public class TestCLCA_Complete {

	private static double mst = 0.001; // 0.1% is standard
	
	private static double e1 = 0.00003; // not used

	private static double e2 = 0.0005; // not used

	private static int focusLeng = 100;

	private static int longestLeng = 10; 

	private static int batchSize = 1000;

	private static double s = -0.315;

	private static int h = 34;

	private static String input = "T10I4D100K.txt";  

	private static String verifyFile = "[FP]T10I4D100K.txt" ;
	
	private static String output;

	public static void main(String [] arg) throws IOException{
		//create a instance
		CLCA clca= new CLCA(mst, input, output, verifyFile, e1, e2, focusLeng, longestLeng, batchSize, s, h);
		//run
		clca.runCLCA();
		
		
	}
	public String fileToPath(String filename) {
		return null;
	}

}
