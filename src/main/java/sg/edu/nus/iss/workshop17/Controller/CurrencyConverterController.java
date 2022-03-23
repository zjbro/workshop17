package sg.edu.nus.iss.workshop17.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.JsonObject;
import sg.edu.nus.iss.workshop17.Service.CurrencyService;
import sg.edu.nus.iss.workshop17.Model.Currency;

@Controller
@RequestMapping
public class CurrencyConverterController {

    @Autowired
    public CurrencyService cService;

    @GetMapping
    public String generateInfo(Model model){
        List<JsonObject> jsonObjectList = cService.createJsonObjList();
        List<Currency> currencyList = cService.createCurrencyList(jsonObjectList);
        model.addAttribute("currencyList", currencyList);
        return "index";
    }
    
    @GetMapping("/convert")
    public String convertCurrency(
        @RequestParam("from") String from,
        @RequestParam("to") String to,
        @RequestParam("amount") Double amount,
        Model model){
            String s1 = from;
            String s2 = to;
            Double exchangeRate = cService.getConvertionRates(s1, s2, amount);
            Double finalAmount = amount*exchangeRate;
            model.addAttribute("finalAmount", finalAmount);
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            model.addAttribute("amount", amount);

        return "results";
    }
}
