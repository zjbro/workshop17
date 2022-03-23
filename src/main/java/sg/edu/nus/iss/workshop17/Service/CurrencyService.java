package sg.edu.nus.iss.workshop17.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.workshop17.Model.Currency;


@Service
public class CurrencyService {
    
    private static final String URL = "https://free.currconv.com/api/v7";

    @Value("${currency.api}")
    private String apiKey;
    //set CURRENCY_API = " "

    private boolean hasKey;

    @PostConstruct
    private void init(){
        hasKey = null != apiKey;
        System.out.println(">>>>>API key set: " + hasKey);     
    }

    public String createUrl(){
        String newUrl = UriComponentsBuilder.fromUriString(URL)
            .path("/countries")
            .queryParam("apiKey", apiKey)
            .toUriString();
        System.out.println(">>>>>>>>>URL: " + newUrl);
        return newUrl;
    }

    public ResponseEntity<String> getResponseEntity(String url){
        RestTemplate rTemplate = new RestTemplate();
        ResponseEntity<String> resp = rTemplate.getForEntity(url, String.class);
        return resp;
    }

    public List<JsonObject> createJsonObjList(){
        ResponseEntity<String> resp = getResponseEntity(createUrl());
        JsonObject result = null;
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
            JsonReader r = Json.createReader(is);
            result = r.readObject();         
        } catch (Exception ex) {
            System.err.printf(">>>>>>Error: %s\n", ex.getMessage());
            ex.printStackTrace();
        }

        JsonObject currencies = result.getJsonObject("results");
        Set<String> keySet = currencies.keySet();
        List<JsonObject> oList = new ArrayList<>();

        for(String k: keySet){
            oList.add(currencies.getJsonObject(k));
        }
        return oList;
    }

    public JsonObject createJsonObj(ResponseEntity<String> resp){
        JsonObject exchangeRate = null;
        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())){
            JsonReader r = Json.createReader(is);
            exchangeRate = r.readObject();         
        } catch (Exception ex) {
            System.err.printf(">>>>>>Error: %s\n", ex.getMessage());
            ex.printStackTrace();
        }
        return exchangeRate;
    }

    public List<Currency> createCurrencyList(List<JsonObject> jsonObjectList){
        List<Currency> currencyList =  jsonObjectList
            .stream()
            .map(o -> Currency.createCurrency(o))
            .toList();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>JSONOBJECTLIST FIRST: " + jsonObjectList.get(1));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>CURRENCYLIST FIRST: " + currencyList.get(1));
        return currencyList;
    }

    public String convertUrl(String s1, String s2){
        String convertUrl = UriComponentsBuilder.fromUriString(URL)
            .path("/convert")
            .queryParam("q", s1+"_"+s2)
            .queryParam("compact", "ultra")
            .queryParam("apiKey", apiKey)
            .toUriString();
        System.out.println(">>>>>>>>>URL: " + convertUrl);
        return convertUrl;
    }


    public Double getConvertionRates(String s1, String s2, Double amount) {
        RequestEntity<Void> req = RequestEntity.get(convertUrl(s1, s2)).build();
        RestTemplate rTemplate = new RestTemplate();

        ResponseEntity<String> respo = rTemplate.exchange(req, String.class);
        
        Map<String,Double> result = getResult(respo, s1, s2);
        Double convertionRates= result.get(s1+"_"+s2);
        return convertionRates;
    }

    public Map<String, Double> getResult(ResponseEntity<String> respo, String s1, String s2) {
        JsonObject resultObj = null;

        try(InputStream file = new ByteArrayInputStream(respo.getBody().getBytes())){
            JsonReader reader = Json.createReader(file);
            resultObj = reader.readObject();
        } catch (IOException ex){
            //handle error ex
        }

        Map<String, Double> result = new HashMap<>();
        result.put(s1+"_"+s2, resultObj.getJsonNumber(s1+"_"+s2).doubleValue());


        return result;
    }


    




    


}