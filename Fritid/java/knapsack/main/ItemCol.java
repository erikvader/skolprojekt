package main;

import java.util.ArrayList;


public class ItemCol {
	
	private int totalValue = 0;
	private ArrayList<Item> items;

	@Override
	public String toString() {
		return "ItemCol [totalValue=" + totalValue + ", items=" + items + "]";
	}

	public ItemCol(int totalValue, ArrayList<Item> items){
		this.totalValue = totalValue;
		this.items = new ArrayList<Item>(items);
	}
	
	public ItemCol(ItemCol itemCol){
		this(itemCol.getTotalValue(), itemCol.getItems());
	}

	public void add(Item item){
		items.add(item);
		totalValue += item.getValue();
	}
	
	public int getTotalWeight(){
		int finalA = 0;
		for(Item x : items){
			finalA += x.getWeight();
		}
		return finalA;
	}

	
	public int getTotalValue() {
		return totalValue;
	}

	
	public void setTotalValue(int totalValue) {
		this.totalValue = totalValue;
	}

	
	public ArrayList<Item> getItems() {
		return items;
	}

	
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
}
