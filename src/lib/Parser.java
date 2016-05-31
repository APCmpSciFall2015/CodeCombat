package lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Parser
{
	// the parser will do constants my way
	public static List<List<Float>> parse2DImmutableFloatArray(String s)
	{
		List<List<Float>> output = new ArrayList<List<Float>>();
		Scanner sc = new Scanner(s.substring(1, s.length() - 1)).useDelimiter("[\\{\\}]");
		while(sc.hasNext())
		{
			String pair = sc.next();
			int div = pair.indexOf(',');
			if(div > 0)
			{
				output.add(Collections.unmodifiableList(new ArrayList<Float>(Arrays.asList(
						new Float[]
							{
								Float.parseFloat(pair.substring(0, div)),
								Float.parseFloat(pair.substring(div+1, pair.length()))
							}
						))));
			}
		}
		return Collections.unmodifiableList(output);
	}
}
