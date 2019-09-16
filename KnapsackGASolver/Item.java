/**
 * This class represents an item, which will be put in the backpack.
 * Each item has its own cost and weight. Both are integers, because
 * I wanted to keep it as simple as possible, but they easily can be
 * converted into double-values.
 * 
 * @author Dimitar Kermedchiev
 */
package solution;

public class Item {

	private int weight;
	private int cost;
	
	Item(int cost, int weight){
		this.cost = cost;
		this.weight = weight;
	}

	public int getCost() {
		return cost;
	}
	
	public int getWeight() {
		return weight;
	}
}
