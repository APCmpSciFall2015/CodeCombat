package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Config
{
	private HashMap<String, String> properties = new HashMap<String, String>();

	public Config(File f)
	{
		load(f);
	}
	
	@Override
	public String toString()
	{
		return "" + properties;
	}
	
	public void load(File f)
	{
		try
		{
			Scanner s = new Scanner(f);
			while(s.hasNextLine())
			{
				String line = s.nextLine();
				int div = line.indexOf('=');
				properties.put(line.substring(0, div), line.substring(div + 1));
			}
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File not Found");
		}	
	}
	
	public void save(File f)
	{
		
		try
		{
			FileWriter fw = new FileWriter(f);
		
			for(Entry<String, String> entry : properties.entrySet())
			{
				fw.write("" + entry.getKey() + "=" + entry.getValue() + "\n");
			}
			fw.close();
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could write to file");
		}
	}
	
	public String get(String property)
	{
		return properties.get(property);
	}
	
	public void add(String property, String value)
	{
		properties.put(property, value);
	}
}