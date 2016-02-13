package diag;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import config.LogConfig;

public class Log
{

	File f = new File(LogConfig.logLocation);
	FileWriter output;

	public void logData()
	{
		try
		{
			output = new FileWriter(f);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void log(String logAction){

		try
		{
			output.write(String.valueOf(System.currentTimeMillis()) + logAction + "\n");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public void close(){
		try
		{
			output.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
