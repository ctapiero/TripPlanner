
package a06;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

/**
 * Represents an Airline Object that gets all available airlines from openflights.org
 * 
 * @author Cristian Tapiero + Kevin Pineda + Tommy Xa
 * @param Airline class allows methods and files to read in from resource files.
 */
public class Airline implements Comparable<Airline> {
	private ST<Integer, String> airlineNameST;
	private ST<Integer, String> airlineCountryST;
	private String name;
	private String country;
	private int id;
	
	/**
	 * @param airlines
	 */
	public Airline(int airlineID) {
		readAirlineName();
		readAirlineCountry();
		name = airlineNameST.get(airlineID);
		country = airlineCountryST.get(airlineID);
		id = airlineID;
	}

	/**
	 * @param Reads in airline names from CSV.
	 */
	private void readAirlineName() {
		String fileName = "src/a06/Resources/airlines.csv";
		In in = new In(fileName);
		String line;
		airlineNameST = new ST<>();
		
		while (in.hasNextLine()) {
			line = in.readLine();
			String[] tokens = line.split(",");
			try {
				int airlineID = Integer.parseInt(tokens[0]);
				String name = tokens[1];
				airlineNameST.put(airlineID, name);
				
			} catch (NumberFormatException ex) {
				System.err.println("Problem reading in " + "\"" + line + "\"");
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @param Reads in airline names from CSV.
	 */
	public void readAirlineCountry() {
		String fileName = "src/a06/Resources/airlines.csv";
		In in = new In(fileName);
		String line;
		airlineCountryST = new ST<>();
		
		while (in.hasNextLine()) {
			line = in.readLine();
			String[] tokens = line.split(",");
			try {
				int airlineID = Integer.parseInt(tokens[0]);
				String country = tokens[2];
				airlineCountryST.put(airlineID, country);
				
			} catch (NumberFormatException ex) {
				System.err.println("Problem reading in " + "\"" + line + "\"");
				ex.printStackTrace();
			}
		}
	}
		
	/**
	 * @return the airlineID
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the airlineID
	 */
	public String getCountry() {
		return this.country;
	}

	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return String.format("%s,%s", name.replaceAll("\"", ""),country.replaceAll("\"", ""));
	}

	@Override
	public int compareTo(Airline o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//test class
	public static void main(String[] args) {
		Airline myairLine = new Airline(321);
		StdOut.println(myairLine.toString());

	}	
}
