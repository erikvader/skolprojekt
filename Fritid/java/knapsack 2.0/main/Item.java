package main;


public class Item {
	
	private int value = 0;
	private int weight = 0; 
	
	public Item(int totalValue, int weight){
		this.value = totalValue;
		this.weight = weight;
	}
	
	public Item(Item item){
		value = item.getValue();
		weight = item.getWeight();
	}

	@Override
	public String toString() {
		return "Item [value=" + value + ", weight=" + weight + "]";
	}



	public int getWeight() {
		return weight;
	}


	
	public void setWeight(int weight) {
		this.weight = weight;
	}


	public int getValue() {
		return value;
	}

	
	public void setValue(int value) {
		this.value = value;
	}

	
	
	
	
}
