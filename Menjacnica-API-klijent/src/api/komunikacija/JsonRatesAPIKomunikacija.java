package api.komunikacija;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import menjacnica.*;

public class JsonRatesAPIKomunikacija {
	
	private static final String appKey = "jr-ba8999934fc5a7ab64a4872fb4ed9af7";
	private static final String jsonRatesURL = "http://jsonrates.com/get/";

	
	private String sendGet(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		
		boolean endReading = false;
		String response = "";
		
		while (!endReading) {
			String s = in.readLine();
			
			if (s != null) {
				response += s;
			} else {
				endReading = true;
			}
		}
		in.close();
 
		return response.toString();
	}
		
	public LinkedList<Valuta> vratiIznosKurseva (String[] valute) {
	
		LinkedList<Valuta> listaValute = new LinkedList<Valuta>();
		String url = null;
		
		for (int i = 0; i < valute.length; i++) {
			
			 url = jsonRatesURL + "?" +
					"from=" + valute[i] +
					"&to=RSD" +
					"&apiKey=" + appKey;
			
		
		Valuta nova = new Valuta();
		
			 
		try {
			String result = sendGet(url);
			
			Gson gson = new GsonBuilder().create();
			JsonObject jsonResult = gson.fromJson(result, JsonObject.class);
			
			nova.setKurs(Double.parseDouble(jsonResult.get("rate").getAsString()));
			nova.setNaziv(valute[i]);
						
			listaValute.add(nova);
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {	
			e.printStackTrace();
		}
		
	}
			return listaValute;		
	}
			
}
