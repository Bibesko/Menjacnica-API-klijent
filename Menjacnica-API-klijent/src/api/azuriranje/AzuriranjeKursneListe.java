package api.azuriranje;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import api.komunikacija.JsonRatesAPIKomunikacija;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import menjacnica.Valuta;

public class AzuriranjeKursneListe {

	private static final String putanjaDoFajlaKursnaLista = "data/kursnaLista.json";

	public LinkedList<Valuta> ucitajValute() throws IOException {

		FileReader reader = new FileReader(putanjaDoFajlaKursnaLista);

		Gson gson = new GsonBuilder().create();
		JsonArray valuteJson = gson.fromJson(reader, JsonArray.class);

		LinkedList<Valuta> valute = new LinkedList<Valuta>();

		for (int i = 0; i < valuteJson.size(); i++) {
			JsonObject valutaJson = (JsonObject) valuteJson.get(i);

			Valuta v = new Valuta();
			v.setKurs(valutaJson.get("kurs").getAsDouble());
			v.setNaziv(valutaJson.get("naziv").getAsString());

			valute.add(v);
		}

		return valute;

	}

	public void upisiValute(LinkedList<Valuta> valute, String datum) {

		JsonObject datumIValutaJson = new JsonObject();

		JsonArray valuteArray = new JsonArray();

		for (int i = 0; i < valute.size(); i++) {
			Valuta v = valute.get(i);

			JsonObject valutaJson = new JsonObject();

			valutaJson.addProperty("naziv", v.getNaziv());
			valutaJson.addProperty("kurs", v.getKurs());

			valuteArray.add(valutaJson);
		}

		datumIValutaJson.addProperty("datum", datum);
		datumIValutaJson.add("valute", valuteArray);

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(putanjaDoFajlaKursnaLista)));

			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			String valuteString = gson.toJson(datumIValutaJson);

			out.println(valuteString);
			out.close();
		} catch (Exception e) {
			System.out.println("Greska: " + e.getMessage());
		}

	}

	public void azurirajValute() {
		
		String[] valute = null;
		
		try {
			ucitajValute();
			
			GregorianCalendar datum = new GregorianCalendar();

			for (int i = 0; i < ucitajValute().size(); i++) {
				
				valute[i] = ucitajValute().get(i).getNaziv();
				
			}
			
			upisiValute(JsonRatesAPIKomunikacija.vratiIznosKurseva(valute), datum.getTime().toString());
			
			
		} catch (IOException e) {
			System.out.println("Greska: "+e.getMessage());
		}
		
	}

}
