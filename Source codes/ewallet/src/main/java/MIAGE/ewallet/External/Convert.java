/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MIAGE.ewallet.External;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Sanjeewa Kulathunga
 */
public class Convert implements ExternalAPI{
    @Override
    public double getEURUSD(){
        try{
            URL url = new URL("https://api.exchangerate.host/convert?from=EUR&to=USD");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            }else{
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                JSONParser parse = new JSONParser();
                JSONObject Respond = (JSONObject) parse.parse(inline);
                JSONObject CRate = new JSONObject();
                CRate=(JSONObject)Respond.get("info");
                return Double.valueOf(String.valueOf(CRate.get("rate")));
            }
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public double getUSDEUR() {
        try{
            URL url = new URL("https://api.exchangerate.host/convert?from=USD&to=EUR");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            }else{
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                JSONParser parse = new JSONParser();
                JSONObject Respond = (JSONObject) parse.parse(inline);
                JSONObject CRate = new JSONObject();
                CRate=(JSONObject)Respond.get("info");
                return Double.valueOf(String.valueOf(CRate.get("rate")));
            }
        }catch(Exception e){
            return 0;
        }
    }

}
