package algorithms;

import itemset_array_integers_with_count.Itemset;
import itemset_array_integers_with_count.Itemsets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import summary.Summaries;
import summary.Summary;
import test.TestGetBatch;
import tools.MemoryLogger;
import verify.Verify;


public class CLCA {

	private double mst;

	private String verifyFile;

	private String input;
	
	private boolean isDataAvail = true;
	private int previousPosition = 0;
	private int readLineCounter = 0;

	private double e1;

	private double e2;

	private int focusLeng;

	private int longestLeng;

	private int totalTrans = 0;

	private int batchSize;

	private double s;

	private double h;

	private double[] error;

	private double[] cutoff;
	
	private ArrayList<int[]> originalData;
	
	private Itemsets candPattern;

	private Summaries streamSummary;

	private Itemsets frequentPattern;
	
	long startTime;
	long endTime;
	long startCATime;
	long endCATime;
	long startUpdateTime;
	long endUpdateTime;
	long startRemoveTime;
	long endRemoveTime;
	
	

	public CLCA(double mst,String input, String output, String verifyFile, double e1, double e2, 
			int focusLeng, int longestLeng, int batchSize, double s, double h2){
		this.mst = mst;
		this.input = input;
		this.verifyFile = verifyFile;
		this.e1 = e1;
		this.e2 = e2;
		this.focusLeng = focusLeng;
		this.longestLeng = longestLeng;
		this.batchSize = batchSize;
		this.s = s;
		this.h = h2;
		
	}
	
	public void printError(){
		for(double d : error){
			System.out.println(d);
		}
	}
	public void runCLCA() throws IOException {
		CustomizedApriori ca = new CustomizedApriori();
		Verify verify = new Verify();
		//create a new summary
		streamSummary = new Summaries("stream summary");
		//calculate error bound
		error = calculateErrorBound(longestLeng, s, h);
//		printError();
		//use error bound to calculate cutoff bound
		cutoff = calculateCutoffBound(error);
		
		//setup start time
		startTime = System.currentTimeMillis();
		// reset the utility for checking the memory usage
		MemoryLogger.getInstance().reset();
		
		// the flag isDataAvail's initial value is true.
		while(isDataAvail){
			//get batch size data and store in array
			originalData = getBatchSizeData(input, batchSize);
			
			// total transactions
			totalTrans += originalData.size(); 
			startCATime = System.currentTimeMillis();
			// get candidate pattern from a batch
			candPattern = ca.runCustomizedApriori(error, originalData, longestLeng);
			candPattern.printItemsets(1);//ca.getDatabaseSize());
			endCATime = System.currentTimeMillis();
			
//			startUpdateTime = System.currentTimeMillis();
//			//update stream summary
//			System.out.println("before remove");
//			streamSummary = updateStreamSummary(candPattern, streamSummary, error, cutoff, totalTrans);
//			streamSummary.printSummaries(1);//ca.getDatabaseSize());
//			endUpdateTime = System.currentTimeMillis();
//			
//			//we check the memory usage
//			MemoryLogger.getInstance().checkMemory();
//			
//			startRemoveTime = System.currentTimeMillis();
//			//remove insignificant data
////			System.out.println("total trans : " + totalTrans);
//			System.out.println("after remove");
//			streamSummary = removeInsigData(streamSummary, error, totalTrans);
//			streamSummary.printSummaries(1);//ca.getDatabaseSize());
//			endRemoveTime = System.currentTimeMillis();
			
			//we check the memory usage
			MemoryLogger.getInstance().checkMemory();
		}
//		/* find frequent pattern from summaries */
//		frequentPattern = miningFP(streamSummary, mst, error, totalTrans);
//		frequentPattern.printItemsets(1);
//		
//		/* evaluate the result */
//		verify.evaluate(frequentPattern, verifyFile);
//		
//		/* print performance */
		endTime = System.currentTimeMillis();
		printStatus();
		
		
	}
	
	/* calculate error bound */
	public double[] calculateErrorBound(int longestLeng, double s, double h2) {
		double[] error = new double[longestLeng+1];
		
		for(int i = 1; i <= longestLeng; i++){
//			error[i] = (1/(1+Math.exp(s*(i-h2))));
//			error[i] = 0.1*mst;
			error[i] = 0.0001;
		}

		return error;
	}
	
	/* calculate cutoff bound */
	public double[] calculateCutoffBound(double[] error2) {
		// TODO Auto-generated method stub
		double[] cutoff = new double[error2.length];
		
		for(int i=0; i < error2.length; i++){
			cutoff[i] = error2[i] * batchSize;
		}
		return cutoff;
	}
	
	/* get batch size data */
	public ArrayList<int[]> getBatchSizeData(String input, int size) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(input));
		String line;
		isDataAvail = false;
		// it need to remove all data in ArrayList in order to ues in next batch
		originalData = new ArrayList<int[]>();
		readLineCounter = 0;
		
		while((line = reader.readLine()) != null){
			readLineCounter++;
						
			// if the line is a comment, is empty or is a kind of metadata
			if (line.isEmpty() == true || line.charAt(0) == '#'
					|| line.charAt(0) == '%' || line.charAt(0) == '@') {
				continue;
			}
			// skip the data has read before
			if(readLineCounter <=  previousPosition){
				continue;
			}
			
			
			// split every element
			String[] lineSplited = line.split(" ");
			// create an array to store items
			int[] transactions = new int[lineSplited.length];
			
			// for each item in this transaction
			for (int i=0; i < lineSplited.length; i++){
				// transform from a string to integer
				Integer item = Integer.parseInt(lineSplited[i]);
				transactions[i] = item;
			}
			
			// add transactions in arrayList
			originalData.add(transactions);
			
			// if it has read batch size data, stop reading
			if (readLineCounter - previousPosition >= batchSize) {
				previousPosition = readLineCounter; // record last time position
				// whether there is a transaction in next line
				if(reader.readLine() != null)
					isDataAvail = true; // it still has data in dataset need to execute
				break;
			}
		}
			
		return originalData;
	}
	
	/* after get candiate itemset, update summary */
	public Summaries updateStreamSummary(Itemsets candidate, Summaries summaries, double[] error, double[] cutoff, int totalTrans) {
		List<List<Itemset>> clevels = candidate.getLevels();
		List<List<Summary>> slevels = summaries.getLevels();
		List<Summary> sList = null;
		List<int[]> sIntList = null;
		Summary summary;
		int k;
		int position;
		double delta = 0;
		
		// k is mean length of itemset, each time process different length
		for (k = 1; k <= clevels.size() - 1; k++) { 
			// if summary's level didn't have level k, it need to be add
			if (slevels.size() <= k) {
				slevels.add(new ArrayList<Summary>());
			}
			// get List<Summary> from stream summaries
			sList = slevels.get(k);
			
			// In order to know which data is exist in summaries,
			// we transform the sList's every element to int[] 
			// but we should avoid there is no data in sList
			if(sList != null){
				sIntList = new ArrayList<int[]>();
				// put the summary's int[] is benefit for candidate itemset to compare
				for(Summary s : sList){
					sIntList.add(s.getItems());  //sIntList is List<int[]>
				}
			}

			//accroding to stream summaries, udpate or add
			for (Itemset itemset : clevels.get(k)) { // all the length k candidate
				//check whether candiate exit in summaries
				if((position = indexOfDataInSummary(sIntList, itemset.getItems())) != -1) { // exist! 
					// use position to get the Summary and update it's support
					sList.get(position).setAbsoluteSupport(sList.get(position).getAbsoluteSupport() + itemset.getAbsoluteSupport());
					
				}else{ // not exist!
					// add new itemset in summaries
					delta = error[k] * totalTrans - cutoff[k];
					summary = new Summary(itemset, delta);
					summaries.addSummary(summary, k);
				}
			}			
		}			
		return summaries;
	}

	/* check if the candidate data exist in summary. If exist, return it's index. otherwise, if not exist, return -1 */
	public static int indexOfDataInSummary(List<int[]> summary, int[] candidate){
		int indexOfList = -1;
		for(int[] datas : summary){
			indexOfList++;
			// first, whether length is different
			if(datas.length == candidate.length){
				//second, check every element
				for(int i=0; i<datas.length; i++){
					if(datas[i] != candidate[i])
						break;
					if(i == datas.length - 1){ // if every element is the same, return true
						return indexOfList;
					}
				}
				
			}			
		}
		return -1; // not exist!
	}
	
	/* remove insignificant Summary from Summaries */
	public Summaries removeInsigData(Summaries summaries, double[] error, int totalTrans) {
		List<List<Summary>> levels = summaries.getLevels();
		List<Summary> level;
		List<Summary> markedToRemove = new ArrayList<Summary>();
		// start remove data from length one, then two ...
		for( int k=1; k <= levels.size() - 1; k++){
			markedToRemove.clear(); // clear list
			level = levels.get(k); // get level k List<Summary>
			for(Summary s : level){
				// f(X) + delta <= Error[k] * totalTrans ==> if satisfy, put the Summary into List
				if( s.getAbsoluteSupport() + s.getDelta() <= error[k] * totalTrans)
					markedToRemove.add(s);
			}
			for(Summary rs : markedToRemove){
				level.remove(rs);
			}
		}
		
		return summaries;
	}

	/* mine frequent pattern from summary and store in ArrayList */
	public Itemsets miningFP(Summaries summaries, double mst, double[] error, int totalTrans) {
		List<List<Summary>> levels = summaries.getLevels();
		Itemsets frequentPattern = new Itemsets("approximate frequent pattern");
		Itemset itemset;
		
		// find every pattern in summaries which satisfy threshold
		for( int k=0; k<= levels.size() - 1; k++){
			for( Summary s : levels.get(k)){
				// if( f(X) >= (mst - Error[|X|]) * totalTrans )
				if( s.getAbsoluteSupport() >= (mst - error[k]) * totalTrans){
					//add this pattern to Itemsets
					itemset = new Itemset(s.itemset); //we just need the pattern, ignore support
					frequentPattern.addItemset(itemset, k);  // add pattern to Itemsets
				}
			}
		}
		return frequentPattern;
	}

	public void printStatus() {
		System.out.println("");
		System.out.println("******pattern count*******");
		System.out.println("candidate count : " + candPattern.getItemsetsCount());
		System.out.println("summary count : " + streamSummary.getSummariesCount());
		System.out.println("");
		System.out.println("******performance**********");
		System.out.println("total time = " + (endTime - startTime)/1000.0 + " s");
		System.out.println("Maximum memory usage : " + MemoryLogger.getInstance().getMaxMemory() + " MB");
		System.out.println("");
		System.out.println("******each procedure run time*******");
		System.out.println("CA run time : " + (endCATime - startCATime)/1000.0);
		System.out.println("update run time : " + (endUpdateTime - startUpdateTime)/1000.0);
		System.out.println("remove run time : "  + (endRemoveTime - startRemoveTime)/1000.0);
		
	}
	public double[] getError() {
		return error;
	}

	public void setError(double[] error) {
		this.error = error;
	}

	public double[] getCutoff() {
		return cutoff;
	}
	public void setCutoff(double[] cutoff) {
		this.cutoff = cutoff;
	}
	public boolean isDataAvail() {
		return isDataAvail;
	}
	public void setDataAvail(boolean isDataAvail) {
		this.isDataAvail = isDataAvail;
	}
	public int getTotalTrans() {
		return totalTrans;
	}
	public void setTotalTrans(int totalTrans) {
		this.totalTrans = totalTrans;
	}

}
