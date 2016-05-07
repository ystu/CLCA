package summary;

import itemset_array_integers_with_count.Itemset;

public class Summary extends Itemset {

	private double delta;

	public Summary(Itemset itemset, double delta){
		
		this.itemset = itemset.getItems();
		this.support = itemset.getAbsoluteSupport();
		this.delta = delta;
	}
	
	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

}
