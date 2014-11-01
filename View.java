import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class View extends JFrame{
	private final int FRAMEWIDTH= 600;
	private final int FRAMEHEIGHT= 600;
	private String[] states = {"United States","Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut",
			"Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky",
			"Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri",
			"Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina",
			"North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota",
			"Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};

	private String[][] ACIOcodes = {{"KIAD","KDCA","KBWI"},{"KMGM"},{"PAJN"},{"KIWA","KPHX"},{"KLIT","KLRF"},{"KMHR","KSAC","KSMF"},
			{"KAPA","KBJC","KDEN"},{"KHFD"},{"KDOV"},{"KTLH","KDLZ"},{"KFTY","KMGE","KATL","KPDK"},{"PHNL"},{"KBOI"},{},
			{"KIND","KEYE","KMQJ","KTYQ"},{"KDSM"},{"KFOE","KTOP"},{"KFFT"},{"KBTR"},{"KAUG"},{},{"KBOS","KMHT"},{"KLAN"},
			{"KMSP"},{"KHKS","KJAN"},{"KJEF"},{"KHLN"},{"KLNK"},{"KCXP"},{"KCON"},{"KTTN"},{"KSAF"},{"KALB"},{"KRDU"},{"KBIS"},
			{"KCMH","KLCK","KOSU","KTZR"},{"KOKC","KPWA","KRCE","KTIK"},{"KSLE"},{"KCXY","KMDT"},{"KPVD"},{"KCAE","KCUB"},{"KPIR"},
			{"KBNA"},{"KAUS","KEDC"},{"KSLC"},{"KMPV"},{"KOFP","KFCI","KRIC"},{"KOLM"},{"KCRW"},{"KMSN"},{"KCYS"}};

	private String[] stateCapitals = {"Washington, DC","Montgomery","Juneau","Phoenix","Little Rock","Sacramento","Denver","Hartford",
			"Dover","Tallahassee","Atlanta","Honolulu","Boise","Springfield","Indianapolis","Des Moines","Topeka",
			"Frankfort","Baton Rouge","Augusta","Annapolis","Boston","Lansing","Saint Paul","Jackson","Jefferson City",
			"Helena","Lincoln","Carson City","Concord","Trenton","Santa Fe","Albany","Raleigh","Bismarck","Columbus",
			"Oklahoma City","Salem","Harrisburg","Providence","Columbia","Pierre","Nashville","Austin","Salt Lake City",
			"Montpelier","Richmond","Olympia","Charleston","Madison","Cheyenne"};

	private double[] north = {49.388611};
	private double[] south = {24.544245};
	private double[] east = {-66.954811};
	private double[] west = {-124.733253};

	private JComboBox<String> statesDropDown = new JComboBox<String>(states);
	private JPanel control = new JPanel(new FlowLayout());
	private JPanel display = new JPanel(null);
	private JLabel locationDisplay = new JLabel();
	private JLabel capitalDisplay = new JLabel();
	private JButton testButton = new JButton("Display");
	private ImageIcon[] flags = new ImageIcon[51];
	private JLabel flagPic = new JLabel();
	private JTextArea liveData = new JTextArea();
	private JScrollPane outputLiveData = new JScrollPane(liveData);

	public View(){
		super.setLayout(null);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setTitle("Live Data");

		testButton.addActionListener(new displayListener());
		statesDropDown.setEditable(false);
		control.add(statesDropDown);
		control.add(testButton);

		setLocations();

		display.add(flagPic);
		display.add(locationDisplay);
		display.add(capitalDisplay);
		display.add(outputLiveData);

		super.add(control);
		super.add(display);

		super.setSize(FRAMEWIDTH, FRAMEHEIGHT);
		super.setVisible(true);
		super.setResizable(false);
		super.setLocationRelativeTo(null);

		//load images
		for(int x = 0; x < states.length; x++){
			flags[x] = new ImageIcon("Images/" + states[x] + ".png");
		}
	}

	private void setLocations(){
		control.setLocation(0,0);
		control.setSize(FRAMEWIDTH, 50);
		display.setLocation(0,control.getHeight());
		display.setSize(FRAMEWIDTH, FRAMEHEIGHT - control.getHeight());

		flagPic.setLocation(10,10);
		flagPic.setSize(200,103);
		locationDisplay.setLocation(flagPic.getX() + flagPic.getWidth() + 20,flagPic.getY());
		locationDisplay.setSize(100, 20);
		capitalDisplay.setLocation(locationDisplay.getX(),locationDisplay.getY() + locationDisplay.getHeight() + 20);
		capitalDisplay.setSize(150, 20);
		outputLiveData.setLocation(flagPic.getX(),flagPic.getY() + flagPic.getHeight() + 20);
		outputLiveData.setSize(FRAMEWIDTH - 30,370);

		liveData.setEditable(false);
	}

	private String buildPanel(int num){
		String output = "";
		liveData.setText(output);
		locationDisplay.setText((String)statesDropDown.getSelectedItem());
		flagPic.setIcon(flags[num]);
		capitalDisplay.setText("Capital: " + stateCapitals[num]);

		output += getWeatherData(num);

		if(num == 0)
			output += getEarthquakeData(num);

		return output;
	}

	private String getWeatherData(int num){
		String output = stateCapitals[num] + " Airport Weather Data" + "\n\n";


		for(int y = 0; y < ACIOcodes[num].length; y++){
			try{
				String link ="http://api.geonames.org/weatherIcaoJSON?ICAO=" + ACIOcodes[num][y] + "&formatted=true&username=HaynerS";
				String JSonString = readURL(link);
				JSONObject x = JSONObject.fromObject(JSonString);
				JSONObject weatherData =(JSONObject)(x.get("weatherObservation"));

				String data = (String)weatherData.get("stationName");

				for(int z = 0; z < data.length(); z++){
					if(data.charAt(z) == ','){
						data = data.substring(z + 2, data.length());
						z = data.length();
					}
				}
				output += "\t" + data + "\n";
				output += "\tTemperature is " + weatherData.get("temperature") + 
						" degrees (Celsius) with" + weatherData.get("clouds") + "\n\n";
			}catch(Exception e){
				output += "\tNo weather data available for " + ACIOcodes[num][y] + "\n\n";
			}
		}
		if(ACIOcodes[num].length == 0){
			output += "\tNo airports in or near capital city\n\n";
		}

		return output;
	}

	private String getEarthquakeData(int num){
		String output = "5 Worst Earthquakes for " + states[num] + "\n\n";
		try{
			String link = "http://api.geonames.org/earthquakesJSON?north=" + north[num] + "&south=" + south[num] 
					+ "&east=" + east[num] + "&west=" + west[num] + "&minMagnitude=2.0&maxRows=5&username=HaynerS";
			String JSonString = readURL(link);
			JSONObject x = JSONObject.fromObject(JSonString);
			JSONArray earthquakeData = (JSONArray)(x.getJSONArray("earthquakes"));

			for (int i=0;i<earthquakeData.size(); i++) {
				JSONObject name = (JSONObject)(earthquakeData.get(i));
				output += "\tLatetime: " + name.get("datetime") + "\n";
				output += "\tLongitude: " + name.get("lng") + "\tLatitude: " + name.get("lat") + "\n";
				output += "\tMagnitude: " + name.get("magnitude") + "\n\n";
			}
		}catch(Exception e){
			output += "\tNo earthquake data available\n\n";
		}
		return output;
	}

	private static String readURL(String webservice) throws Exception {
		URL oracle = new URL(webservice);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						oracle.openStream()));

		String inputLine;
		String result = "";

		while ((inputLine = in.readLine()) != null)
			result = result + inputLine;

		in.close();
		return result;
	}

	private class displayListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			//System.out.println(statesDropDown.getSelectedIndex() + " " + statesDropDown.getSelectedItem());
			//buildPanel(statesDropDown.getSelectedIndex());

			liveData.setText(buildPanel(statesDropDown.getSelectedIndex()));
		}
	}
}
