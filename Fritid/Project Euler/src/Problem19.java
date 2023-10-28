
public class Problem19 {
	
	public static void main(String[] args){
		new Problem19().run();
		
	}
	
	int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	int day = 0; //0=monday, 1 = tuesday, osv.
	//int date = 1;
	int month = 0; //0=january, 1 = february, osv.
	int year = 1900;
	
	public void run(){
		int total = 0;
		
		//ta oss till startdatum (1 jan 1901)
		for(int i = 0; i < 12; i++)
			nextMonth();
		
		//leta sundays
		for(int i = 0; i < 12*100; i++){
			if(day == 6){
				total++;
			}
			nextMonth();
		}
		
		System.out.println(total);
	}
	
	public void nextMonth(){
		day += months[month];
		if(month == 1 && year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)){ //feb
			day++;
		}
		day %= 7;
		
		month++;
		if(month == 12){
			year++;
		}
		month %= 12;
	}
}
