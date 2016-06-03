package lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class Config
{
	private HashMap<String, String> properties = new HashMap<String, String>();

	public Config(){}
	
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
			System.out.println("loaded config data from file: " + f);
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
			
			// put entries in order
			
			
			
			// write to file
			for(String s : toSortedList())
				fw.write(s);
			fw.close();
			System.out.println("wrote config data to file: " + f);
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
	
	public void set(String property, String value)
	{
		properties.put(property, value);
	}
	
	public List<String> toSortedList()
	{
		LinkedList<String> entries = new LinkedList<String>();
		for(Entry<String, String> entry : properties.entrySet())
			entries.add(entry.getKey() + "=" + entry.getValue() + "\n");
		Collections.sort(entries);
		return entries;
	}
	
	public List<String> keysToSortedList()
	{
		LinkedList<String> keys = new LinkedList<String>();
		for(Entry<String, String> entry : properties.entrySet())
			keys.add(entry.getKey());
		Collections.sort(keys);
		return keys;
	}
}