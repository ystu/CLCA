package summary;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Summaries {
	
	private List<List<Summary>> levels = new ArrayList<List<Summary>>();
	private String name;  
	private int SummariesCount = 0;
	private int numBatch = 0;
	
	public Summaries(String name) {
		this.name = name;
		levels.add(new ArrayList<Summary>());
	}
	
	public void printSummaries(int nbObject) {
		System.out.println(" ------- " + name + " -------");
		System.out.println("the number of batch : " + ++numBatch);//(++numBatch+1)/2);
		int patternCount = 0;
		int levelCount = 0;
		// for each level (a level is a set of itemsets having the same number of items)
		for (List<Summary> level : levels) {
			// print how many items are contained in this level
			System.out.println("  L" + levelCount + " ");
			// for each itemset
			for (Summary summary : level) {
				Arrays.sort(summary.getItems());
				// print the itemset
				System.out.print("  pattern " + patternCount + ":  ");
				summary.print();
				// print the support of this itemset
				System.out.print("support :  " + summary.getRelativeSupportAsString(nbObject));
				patternCount++;
				System.out.println("  delta : " + summary.getDelta());
				System.out.println("");
			}
			levelCount++;
		}
		System.out.println(" --------------------------------");
	}
	
	public void addSummary(Summary summary, int k) {
		while (levels.size() <= k) {
			levels.add(new ArrayList<Summary>());
		}
		levels.get(k).add(summary);
		SummariesCount++;
	}

	public List<List<Summary>> getLevels() {
		return levels;
	}

	public void setLevels(List<List<Summary>> levels) {
		this.levels = levels;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSummariesCount() {
		return SummariesCount;
	}

	public void setSummariesCount(int summariesCount) {
		SummariesCount = summariesCount;
	}

	public int getNumBatch() {
		return numBatch;
	}

	public void setNumBatch(int numBatch) {
		this.numBatch = numBatch;
	}
	
}
