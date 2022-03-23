package sg.edu.nus.iss.workshop17.Model;

import jakarta.json.JsonObject;


public class Currency {
    private String alpha3;
    private String currencyId;
    private String currencyName;
    private String currencySymbol;
    private String id;
    private String name;
   
    public String getAlpha3() {
        return alpha3;
    }
    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }
    public String getCurrencyId() {
        return currencyId;
    }
    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }
    public String getCurrencyName() {
        return currencyName;
    }
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static Currency createCurrency(JsonObject jsonObject){
        Currency c = new Currency();
        c.setAlpha3(jsonObject.getString("alpha3"));
        c.setCurrencyId(jsonObject.getString("currencyId"));
        c.setCurrencyName(jsonObject.getString("currencyName"));
        c.setCurrencySymbol(jsonObject.getString("currencySymbol"));
        c.setId(jsonObject.getString("id"));
        c.setName(jsonObject.getString("name"));
      
        return c;
    
    
    }

    @Override
    public String toString() {
        return "[currencyName=" + currencyName + ", currencySymbol=" + currencySymbol + ", name=" + name + "]";
    }

}