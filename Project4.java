import java.net.*;
import java.io.*;
import net.sf.json.*;
import org.apache.commons.lang.exception.*;

public class Project4 {

	public static void main(String[] args) throws Exception {
		String link = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&minMagnitude=2.0&maxRows=20&username=HaynerS";
		String JSonString = readURL(link);
		JSONObject x = JSONObject.fromObject(JSonString);
		JSONArray earthquakeData = (JSONArray)(x.getJSONArray("earthquakes"));
		System.out.println(earthquakeData);
		System.out.println();
		for (int i=0;i<earthquakeData.size(); i++) {
			JSONObject name = (JSONObject)(earthquakeData.remove(i));
			System.out.println("Record " + (i + 1));
			System.out.println("-----------");
			System.out.println("datetime:" + name.get("datetime"));
			System.out.println("depth:" + name.get("depth"));
			System.out.println("lng:" + name.get("lng"));
			System.out.println("src:" + name.get("src"));
			System.out.println("eqid:" + name.get("eqid"));
			System.out.println("magnitude:" + name.get("magnitude"));
			System.out.println("lat:" + name.get("lat"));
			System.out.println();
		}
	}
	
	public static String readURL(String webservice) throws Exception {
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
}


