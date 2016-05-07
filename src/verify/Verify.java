package verify;

import itemset_array_integers_with_count.Itemset;
import itemset_array_integers_with_count.Itemsets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import summary.Summary;
import algorithms.Apriori;
import algorithms.CLCA;


public class Verify {

	private double fp = 0;

	private double tp = 0;

	private double totalAccurate = 0;

	private double recall;

	private double precision;

	private double f_score = 0.0;

	private ArrayList<int[]> accurate;

	public void evaluate(Itemsets appro, String accurateFile) throws IOException{
		List<List<Itemset>> levels = appro.getLevels();
		List<Itemset> level;
		int result = -1;
		//step1. read accurateFile and store in Itemsets
		List<int[]> accurate = getAccuratePattern(accurateFile);		
		totalAccurate = accurate.size();
		
		// step2. read each pattern in appro and compare accurate, count the total patterns, true positive and false positive
		for(int k=1 ; k <= levels.size() - 1 ; k++){
			// get each level's Itemset
			for(Itemset approItemset : levels.get(k)){
				// use CLCA's function to check the answer
				result = CLCA.indexOfDataInSummary(accurate, approItemset.itemset); 
				if(result != -1){
					tp++;
				}else{
					fp++;
				}
			}
		}
		
		//step3. evalute recall, precision by fp, tp and totalAccurate
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits( 3 );    //小數後3位
		
		System.out.println("******verify**********");
		System.out.println("true positive = " + (int)(tp));
		System.out.println("false positive = " + (int)fp);
		System.out.println("total accurate = " + (int)totalAccurate);
		System.out.println("");
		
		recall = tp / totalAccurate;
		precision =  tp / (fp + tp);
		f_score = (2 * recall * precision) / (recall + precision);
		System.out.println("********accuracy**********");
		System.out.println("recall = " + nf.format(recall));
		System.out.println("precision = " + nf.format(precision));
		System.out.println("f_score = " + nf.format(f_score));
		
	}
	
	/* get the accurate answer from file */
	public List<int[]> getAccuratePattern(String accurateFile) throws IOException {
		ArrayList<int[]> accurateList = new ArrayList<int[]>();
		BufferedReader reader = new BufferedReader(new FileReader(accurateFile));
		String line;
		while((line = reader.readLine()) != null){
			// split the line according to spaces
			String[] lineSplited = line.split(" ");
			// create an array of int to store the items in this transaction
			int acc[] = new int[lineSplited.length];
			for(int i=0; i<lineSplited.length; i++){
				// transform this item from a string to an integer
				Integer item = Integer.parseInt(lineSplited[i]);
				// store the item in the memory representation of the database
				acc[i] = item;
			}
			// add acc into accurateList
			accurateList.add(acc);
		}
		return accurateList;
		
	}

}
