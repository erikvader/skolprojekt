package game.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class FileHandler {
	
	public static void writeFile(File f, HashMap<String, String> data) throws IOException{
		/*
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
		oos.writeObject(data);
		oos.close();
		*/
		
		PrintWriter pw = new PrintWriter(f);
		Iterator<String> iter = data.keySet().iterator();
		String key = "";
		while(iter.hasNext()){
			key = iter.next();
			pw.println(key+"="+data.get(key));
		}
		pw.close();
		
	}
	
	public static void loadFile(File f, HashMap<String, String> data) throws IOException{
		/*
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
        data = (HashMap<String, String>) ois.readObject();
        ois.close();
        */
		
		data.clear();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String input = "";
		int index;
		while((input = br.readLine()) != null){
			index = input.indexOf("=");
			data.put(input.substring(0, index), input.substring(index+1, input.length()));
		}
		br.close();
		
	}
}
