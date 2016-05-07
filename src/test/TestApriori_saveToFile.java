package test;

import itemset_array_integers_with_count.Itemsets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import algorithms.Apriori;


/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class TestApriori_saveToFile {

		private static String input_path = "D:\\Data\\FPdataset\\";
		private static String output_path = "D:\\Data\\FPdataset\\verify\\";

		public static void main(String[] arg) throws FileNotFoundException, IOException {
			double minsup = 0.01; // means a minsup of 2 transaction (we used a relative support)
			String dataset = "retail";  //kosarak_test_lexico  retail  simple
			String input = input_path + dataset + ".txt"; 
			String output = output_path + dataset +"_mst=" + minsup + ".txt";
			int longestLength = 100;
		
		// Applying the Apriori algorithm
		Apriori apriori = new Apriori();
		Itemsets result = apriori.runAlgorithm(minsup, input, output, longestLength);
		apriori.printStats();
	}
	

}
