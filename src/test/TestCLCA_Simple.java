package test;

import java.io.IOException;

import algorithms.CLCA;

public class TestCLCA_Simple {

	private static double mst = 0.3; 

	private static double e1 = 0.00003; // not used

	private static double e2 = 0.0005; // not used

	private static int focusLeng = 3;

	private static int longestLeng = 3;

	private static int batchSize = 10; 

	private static double s = -0.315;

	private static int h = 34;

	private static String input = "lexico_test_kosarak.txt";  //T10I4D100K.txt

	private static String verifyFile = "[FP]lexico_test_kosarak.txt" ; //[FP]T10I4D100K.txt
	
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
