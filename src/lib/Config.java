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

/**
 * Stores Configuration Options for use in the CodeCombat game
 * @author Robert
 * @version 0.1.
 */
public class Config
{

	/** Stores the configuration properties */
	private HashMap<String, String> properties = new HashMap<String, String>();

	/**
	 * No-Argument Config Constructor. Instantiates a blank config class.
	 */
	public Config()
	{
	}

	/**
	 * 1-Argument Config Constructor. Instantiates a config class that saves to
	 * a given file.
	 * @param f the file to save the information to
	 */
	public Config(File f)
	{
		load(f);
	}

	// Overridden methods
	// -------------------------------

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "" + properties;
	}

	// Functional methods
	// ---------------------------------------------

	/**
	 * Loads the configuration options from the file.
	 * @param f the file to load from
	 */
	public void load(File f)
	{
		try
		{
			Scanner s = new Scanner(f);
			while (s.hasNextLine())
			{
				String line = s.nextLine();
				int div = line.indexOf('=');
				if(div > 0)
					properties.put(line.substring(0, div), line.substring(div + 1));
			}
			System.out.println("loaded config data from file: " + f);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("File not Found");
		}
	}

	/**
	 * Saves the configuration properties to a file.
	 * @param f the file to save it to
	 */
	public void save(File f)
	{
		try
		{
			FileWriter fw = new FileWriter(f);

			// put entries in order

			// write to file
			for (String s : toSortedList())
				fw.write(s);
			fw.close();
			System.out.println("wrote config data to file: " + f);
		}
		catch (IOException e)
		{
			System.err.println("ERROR: Could write to file");
		}
	}

	/**
	 * Gets the data for a property.
	 * @param property the property that the user wants the data from
	 * @return the data contained in the property
	 */
	public String get(String property)
	{
		return properties.get(property);
	}

	/**
	 * Sets the data for a given property.
	 * @param property the property that the user wants the option from
	 * @param value the value to set the option to
	 */
	public void set(String property, String value)
	{
		properties.put(property, value);
	}

	/**
	 * Sorts all configuration options and values into a list of string objects.
	 * @return the list containing the configuration option
	 */
	public List<String> toSortedList()
	{
		LinkedList<String> entries = new LinkedList<String>();
		for (Entry<String, String> entry : properties.entrySet())
			entries.add(entry.getKey() + "=" + entry.getValue() + "\n");
		Collections.sort(entries);
		return entries;
	}

	/**
	 * Sorts all configuration options to a list of String objects.
	 * @return the list containing the configuration options
	 */
	public List<String> keysToSortedList()
	{
		LinkedList<String> keys = new LinkedList<String>();
		for (Entry<String, String> entry : properties.entrySet())
			keys.add(entry.getKey());
		Collections.sort(keys);
		return keys;
	}
}