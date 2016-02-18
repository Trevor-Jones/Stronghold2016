package diag;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import config.LogConfig;

/**
 * Logs data to a log on the roborRio
 * @author Aditya
 *
 */
public class Log{

	File f = new File(LogConfig.logLocation);
	FileWriter output;

	/**
	 * Opens new file writer
	 */
	public void logData(){
		try{
			output = new FileWriter(f);
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Logs the string to the file
	 * @param logAction
	 */
	public void log(String logAction){

		try{
			output.write(String.valueOf(System.currentTimeMillis()) + logAction + "\n");
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the file writer
	 */
	public void close(){
		try{
			output.close();
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
