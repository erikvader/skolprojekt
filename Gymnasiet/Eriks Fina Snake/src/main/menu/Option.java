package main.menu;


public class Option{
	private String name = "hej";
	private MenyuAction al;
	
	public Option(String n, MenyuAction al){
		name = n;
		this.al = al;
	}
	
	public void click(){
		if(al != null)
			al.action();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String s){
		name = s;
	}
	
}
