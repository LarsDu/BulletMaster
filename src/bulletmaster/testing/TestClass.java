package bulletmaster.testing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class TestClass {
	
	
	
	private URL base;

	public static void main(String [] args) throws Exception{
		
		try{
			//base = getCodeBase();

		}catch (Exception e){
			//TODO: handle exception
		}
		
		System.out.println("Hello World!");
		
		//Test map reader
		//TMXReader tmxRead = new TMXReader();
        //InputStream is = new BufferedInputStream(new FileInputStream("/home/ladu/Code/BulletMaster/levels/test1base64.tmx"));
		//Level map=tmxRead.readLevel(is);

		//String base = new File("").getAbsolutePath();
		//String base = new File("").getCanonicalPath();
		//String Spritedir = base+"/levels";
		//System.out.println("Basedir:"+base);
		//System.out.println("newdir:"+ Spritedir);
		
		double height = 30;
		double width = 30;
		System.out.println(Math.atan(height/width));
		
	}
	
	
}
