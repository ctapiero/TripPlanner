
package a06;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.SymbolGraph;

/**
 * Represents a program that finds the best route for a flight and gets the
 * designated information for a full flight.
 * 
 * @author Cristian Tapiero + kevin Pineda + Tommy Xa
 *
 */
public class AirlineRoutes {
	private static final double airplaneSpeed = 500; // mph
	static ArrayList<Airport> airportPaths = new ArrayList<>();
	private static Airline airline;
	private static Queue<String> layovers = new Queue<>();

	/**
	 * Builds a Symbol Graph from file and uses BFS to provide the shortest path for
	 * the airplane flight.
	 * 
	 * @param departure   - String airport departure
	 * @param destination - String airport destination
	 */
	public static void routeChecker(String departure, String destination) {
		String routesFile = "src/a06/Resources/newroutes.txt";
		SymbolGraph routes = new SymbolGraph(routesFile, "	");
		Graph graph = routes.graph();

		if (!routes.contains(departure)) {
			StdOut.println(departure + " is not an airport that is available");
		} else {
			int a = routes.indexOf(departure);
			int b = routes.indexOf(destination);
			BreadthFirstPaths bfs = new BreadthFirstPaths(graph, a);
			if (bfs.hasPathTo(b)) {
				for (int x : bfs.pathTo(b)) {
					layovers.enqueue(routes.nameOf(x));
					airportPaths.add(new Airport(routes.nameOf(x)));
				}
			}
		}
	}

	/**
	 * Provides the edges of a graph and represents the layovers of the airplane
	 * path.
	 * 
	 * @param departure   - String airport departure
	 * @param destination - String airport destination
	 * @return - ArrayList of Object Airport
	 */
	public static ArrayList<Airport> layovers(String departure, String destination) {
		routeChecker(destination, departure);
		layovers.dequeue();
		ArrayList<Airport> layoverAirports = new ArrayList<>();

		for (int i = 0; i < layovers.size() - 2; i++) {
			if (layovers.size() == 1) {
				break;
			}
			layoverAirports.add(new Airport(layovers.dequeue()));
		}
		return layoverAirports;
	}

	/**
	 * Method that calculates the distances between two different latitudes and
	 * longitudes. Provides the distance in miles. Helper method for
	 * totalDistance().
	 * 
	 * @param lat1 - double latitude of Airport
	 * @param lon1 - double longitude of Airport
	 * @param lat2 - double latitude of Airport
	 * @param lon2 - double longitude of Airport
	 * @return - A distance in miles
	 */
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		} else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
					+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			return (dist);
		}
	}

	/**
	 * Provides a total distance from all airports that are in the flight path.
	 * 
	 * 
	 * @param airportPaths - ArrayList of Object of Airports representing the paths.
	 * @return - the total distance between airports
	 */
	public static int totalDistance(ArrayList<Airport> airportPaths) {
		Queue<Double> dist = new Queue<>();
		double total = 0.0;
		for (int i = 0; i < airportPaths.size(); i++) {
			if (i == airportPaths.size() - 1) {
				break;
			} else {
				dist.enqueue(distance(airportPaths.get(i).getLatitude(), airportPaths.get(i).getLongitude(),
						airportPaths.get(i + 1).getLatitude(), airportPaths.get(i + 1).getLongitude()));
			}
		}
		while (!dist.isEmpty()) {
			total += dist.dequeue();
		}
		return (int) total;
	}

	/**
	 * Calculates the time from the entire flight including layovers using d = rt.
	 * Creates an abritatry time differential to act as a layover.
	 * 
	 * 
	 * @param distance - total distance using method totalDistance
	 * @return - a string of total time and giving hours.
	 * @throws ParseException
	 */
	public static String calculateTime(double distance) throws ParseException {
		double totalDistance = totalDistance(airportPaths);
		double time = totalDistance / airplaneSpeed;
		short layoverTimeDifferential = (short) (1 + getNumberOfStops());
		time = layoverTimeDifferential + time;
		return (int) time + " hours";
	}

	/**
	 * Gets the initial departure Airport from the file. Uses a hashmap to store all
	 * the airline that an airport can connect to.
	 * 
	 * @param s - the Airport
	 * @return - Gives all the airlines that can go to the airport
	 */
	private static Set<Integer> getSourceAirline(String s) {
		Map<String, Set<Integer>> sourceAirlines = new HashMap<>();
		String fileName = "src/a06/Resources/routes.csv";
		In in = new In(fileName);
		String line;

		while (in.hasNextLine()) {
			line = in.readLine();
			String[] tokens = line.split(",");
			try {
				int airlineID = Integer.parseInt(tokens[0]);
				String airport = tokens[1];
				Set<Integer> airlineIDList = sourceAirlines.get(airport);
				if (airlineIDList == null)
					sourceAirlines.put(airport, airlineIDList = new HashSet<Integer>());
				airlineIDList.add(airlineID);

			} catch (NumberFormatException ex) {
				System.err.println("Problem reading in " + "\"" + line + "\"");
				ex.printStackTrace();
			}
		}
		return sourceAirlines.get(s);
	}

	/**
	 * Gets a random airline to simulate different airlines being available for the
	 * trip.
	 * 
	 * @param departure - departure Airport
	 * @return - a String airline
	 */
	public static int getRandomAirline(String departure) {
		Set<Integer> sourceAirlines = getSourceAirline(departure);
		ArrayList<Integer> randomSource = new ArrayList<>();
		for (Integer x : sourceAirlines) {
			randomSource.add(x);
		}
		int newAirline = randomSource.get(StdRandom.uniform(randomSource.size()));
		return newAirline;
	}

	/**
	 * Simulates different ticket prices dependent on the total distance of the
	 * flight.
	 * 
	 * 
	 * @param distance - total distance a flight has
	 * @return - A string representing the ticket price
	 */
	public static String ticketPrice(double distance) {
		int ticketPrice = 0;
		if (distance < 100) {
			// 50 - 100
			ticketPrice = 50 + (int) (Math.random() * ((100 - 50) + 1));
		} else if (distance > 100 && distance <= 1000) {
			// between 100-500
			ticketPrice = 100 + (int) (Math.random() * ((500 - 100) + 1));
		} else if (distance > 1000 && distance <= 2000) {
			// 500-800
			ticketPrice = 500 + (int) (Math.random() * ((800 - 500) + 1));
		} else if (distance > 2000 && distance <= 4000) {
			// 800 - 1200c
			ticketPrice = 2500 + (int) (Math.random() * ((4000 - 2500) + 1));
		}
		return "$" + ticketPrice;
	}

	/**
	 * Gets the number of stops a flight has. Calculates depending on the edges by
	 * -2 the size.
	 * 
	 * @return - Int representing number of stops
	 */
	public static int getNumberOfStops() {
		return airportPaths.size() - 2;
	}

	/**
	 * Gets the paths from the flights. Uses the ArrayList to provide the flights
	 * 
	 * @return - returns the ArrayList of airport paths
	 */
	public static ArrayList<Airport> getAirportPaths() {
		return airportPaths;
	}

	// TESTING -*********************************-
	/**
	 * Testing of methods
	 * @param args
	 */
	public static void main(String[] args) {

		// Start of test using user input
		StdOut.print("Departure: ");
		StdOut.println();
		String departure = StdIn.readLine();
		StdOut.print("Destination: ");
		String destination = StdIn.readLine();
		StdOut.print("Country: ");

		// Airline Set Test
		System.out.println("All airlines in source airport: " + getSourceAirline(departure));
		System.out.println();


		// Random Airline Test
		System.out.println("Choosin airline for departure: " + getRandomAirline(departure));
		System.out.println();

		// Airline Object Test
		airline = new Airline(getRandomAirline(departure));
		System.out.println("Finding all available methods: " + airline.getCountry() + ", " + airline.getName());
		System.out.println();

		// route test
		routeChecker(departure, destination);
		System.out.println();
		System.out.println("test toString" + airportPaths.toString());
		System.out.println();

		// layover test
		System.out.println();
		System.out.println("layovers: ");
		layovers(destination, departure).forEach(airport -> System.out.println(airport.getName()));
		System.out.println();
		System.out.println();

		// size test
		int size = airportPaths.size() - 1;
		int sizeOfStops = airportPaths.size() - 2;
		System.out.println("up to index " + size);
		System.out.println("number of stops: " + sizeOfStops);
		System.out.println();

		// distance test
		// need to adjust per the amount of edges
		System.out.println("distance is: " + totalDistance(airportPaths));
//		double edge1 = distance(airportPaths.get(0).getLatitude(), airportPaths.get(0).getLongitude(),
//				airportPaths.get(1).getLatitude(), airportPaths.get(1).getLongitude());
//		double edge2 = distance(airportPaths.get(1).getLatitude(), airportPaths.get(1).getLongitude(),
//				airportPaths.get(2).getLatitude(), airportPaths.get(2).getLongitude());
//		double edge3 = distance(airportPaths.get(2).getLatitude(), airportPaths.get(2).getLongitude(),
//				airportPaths.get(3).getLatitude(), airportPaths.get(3).getLongitude());
//		System.out.println(edge1 + " + " + edge2 + " + " + edge3 + " = " + (edge1 + edge2 + edge3));
		System.out.println();

		// time test
		try {
			System.out.println("time is: " + calculateTime(totalDistance(airportPaths)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();

		// getname test
		StdOut.print("names of airports: ");
		for (Airport airport : airportPaths) {
			StdOut.print(airport.getName() + ", ");
		}
		StdOut.println();

	}

}
