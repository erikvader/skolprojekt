package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	int folders, pictures;
	String desktopPath;
	
	public void run(){
		try {
			SettingsReader sr = new SettingsReader(getClass().getResourceAsStream("/settings.txt"));
			folders = Integer.parseInt(sr.get("folders"));
			pictures = Integer.parseInt(sr.get("pictures"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		desktopPath = System.getProperty("user.home")+"\\Desktop";
		
		for(int i = 0; i < folders; i++){
			String folder = "/Cat Pictures "+(i+1);
			File dir = new File(desktopPath, folder);
			if(!dir.exists()){
				dir.mkdir();
			}
			for(int j = 0; j < pictures; j++){
				try {
					ExportResource("/cat.jpg", folder, "/cat"+Integer.toString(j+1)+".jpg");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public String ExportResource(String resourceName, String folder, String target) throws Exception {
	        InputStream stream = null;
	        OutputStream resStreamOut = null;
	        String jarFolder;
	        try {
	            stream = Main.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
	            if(stream == null) {
	                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
	            }

	            int readBytes;
	            byte[] buffer = new byte[4096];
	            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().toURI().getPath().replace('\\', '/');
	            //System.out.println(desktopPath + folder + resourceName);
	            System.out.println(jarFolder);
	            resStreamOut = new FileOutputStream(desktopPath + folder + target);
	            while ((readBytes = stream.read(buffer)) > 0) {
	                resStreamOut.write(buffer, 0, readBytes);
	            }
	        } catch (Exception ex) {
	            throw ex;
	        } finally {
	            stream.close();
	            resStreamOut.close();
	        }

	        return jarFolder + resourceName;
	    }

}
