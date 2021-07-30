
package a06;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;

/**
 * Uses classes AirlineRoutes, AutocompleteGUI and MapPanel to create an GUI that
 * plots geographical data based on Airport Objects.
 * @author Cristian Tapiero + Kevin Pineda + Tommy Xa
 *
 */
public class TripPlannerGUI extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static String file = "src/a06/Resources/airportcityTerm.txt";
	private static AutocompleteGUI gui = new AutocompleteGUI(file, 2);
	private static AutocompleteGUI.AutocompletePanel departurePanel = gui.new AutocompletePanel(file);
	private static AutocompleteGUI.AutocompletePanel destinationPanel = gui.new AutocompletePanel(file);
	private JLabel lblNewLabel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JTextArea textArea;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private String departure;
	private String destination;
	private final MapPanel map = new MapPanel();;
	private JPanel panel;
	private Airline sourceAirline;
	private int sizeOfStops;
	private double totalDistance;
	private String totalPrice;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TripPlannerGUI frame = new TripPlannerGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates the frame.
	 */
	public TripPlannerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(80, 80, 1100, 600);
		getContentPane().setLayout(new BorderLayout());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//add title
		createTitle();
		createSearchBoxes();
		
		JButton btnNewButton = createSearchButton();
		
		contentPane.setLayout(null);
		contentPane.add(panel_2);
		contentPane.add(btnNewButton);
		contentPane.add(lblNewLabel);
		contentPane.add(panel_1);
		contentPane.add(map);
		
		textArea = new JTextArea();
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setBounds(604, 128, 290, 422);
		contentPane.add(textArea);
		
			lblNewLabel_1 = new JLabel("Departure:");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblNewLabel_1.setBounds(33, 37, 400, 68);
			contentPane.add(lblNewLabel_1);
		
			lblNewLabel_2 = new JLabel("Destination:");
			lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
			lblNewLabel_2.setBounds(477, 37, 124, 68);
			contentPane.add(lblNewLabel_2);
		
			lblNewLabel_3 = new JLabel("New label");
			lblNewLabel_3.setBackground(Color.BLACK);
			lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\Cristian Tapiero\\OneDrive\\Documentos\\CSIS2420\\FinalProject\\Resources\\airPlaneLogo.png"));
			lblNewLabel_3.setBounds(915, 144, 61, 49);
			contentPane.add(lblNewLabel_3);
		
			panel = map;
			panel.setBounds(21, 128, 573, 422);
			contentPane.add(panel);
		
		
	}

	/**
	 * Plots the coordinates delimited by the Airport objects.
	 */
	private void plotCoordinates() {
		
		for(int i = 0; i < AirlineRoutes.airportPaths.size();i++) {
			if(i == AirlineRoutes.airportPaths.size()-1) {
				break;
			}
			map.addSegment(new Segment(
					new Point(AirlineRoutes.airportPaths.get(i).getLatitude(),AirlineRoutes.airportPaths.get(i).getLongitude()),
					new Point(AirlineRoutes.airportPaths.get(i+1).getLatitude(),AirlineRoutes.airportPaths.get(i+1).getLongitude()), Color.BLUE));
			
			map.addPOI(new POI(AirlineRoutes.airportPaths.get(i).getLatitude(),AirlineRoutes.airportPaths.get(i).getLongitude(),AirlineRoutes.airportPaths.get(i).getName()));
		
		}
	}

	

	/**
	 * Creates a button that interacts with GUI
	 * @return
	 */
	private JButton createSearchButton() {
		JButton btnNewButton = new JButton("Search");
		btnNewButton.setBounds(894, 65, 95, 49);
		btnNewButton.setFont(new Font("Lucida Bright", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				departure = departurePanel.getSelectedText();
				destination = destinationPanel.getSelectedText();
				textArea.setText(String.format( "Destination: %s \n" + "Departure: %s",
						departure, destination));
				try {
					performSearch();
					plotCoordinates();
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return btnNewButton;
	}

	/**
	 * Creates SearchBoxes
	 */
	private void createSearchBoxes() {
		panel_1 = departurePanel;
		panel_1.setBounds(100, 65, 300, 68);
		
		panel_2 = destinationPanel;
		panel_2.setBounds(550, 65, 300, 68);
	}

	/**
	 * Creates Title
	 */
	private void createTitle() {
		lblNewLabel = new JLabel("TRIP PLANNER");
		lblNewLabel.setBounds(10, 11, 398, 49);
		lblNewLabel.setFont(new Font("Magneto", Font.BOLD, 40));
	}
/**
 * Calls methods from AirlineRoutes to find the shortest route and linked data.
 * @throws ParseException
 */
	public void performSearch() throws ParseException {
		

		AirlineRoutes.routeChecker(departure, destination);
		sourceAirline = new Airline(AirlineRoutes.getRandomAirline(departure));
		
		sizeOfStops = AirlineRoutes.getNumberOfStops();
		totalDistance = AirlineRoutes.totalDistance(AirlineRoutes.airportPaths);
		totalPrice = AirlineRoutes.ticketPrice(totalDistance);

		textArea.setText(
				"\nYour airline is: " + sourceAirline.getName() + "	\n\n" + "Number of Stops: " + sizeOfStops + "\n\nPrice: " + totalPrice + " per ticket" 
						+ "\n\nYour trips distance is: " + AirlineRoutes.totalDistance(AirlineRoutes.getAirportPaths()) + " miles\n\n" 
						+ "Total duration of your flight: " + AirlineRoutes.calculateTime(AirlineRoutes.totalDistance(AirlineRoutes.getAirportPaths())) + "\n");
		
		for(Airport airports: AirlineRoutes.layovers(destination, departure)) {
			textArea.append(airports.getName() +", " + airports.getCountry() + "\n");
		}
	}
	
}
