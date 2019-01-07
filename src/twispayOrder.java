package src;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.StandardCharsets;

public class twispayOrder {
    public static void main(String args[]) throws ParseException {
        JSONObject jsonOrderData = TwispaySample.getOrderData();

        // your secret key
        String secretKey = TwispaySample.getSecretKey();

        if (args.length == 2) {
            System.out.println("Arguments provided for JSON order data and secret key.");
            JSONParser jsonParser = new JSONParser();
            jsonOrderData = (JSONObject) jsonParser.parse(args[0]);
            secretKey = args[1];
        } else {
            System.out.println("No arguments provided for JSON order data and secret key, using sample values!");
        }

        System.out.println("jsonOrderData: " + jsonOrderData);
        System.out.println("secretKey: " + secretKey);

        // get the HTML form
        String htmlForm = Twispay.getHtmlOrderForm(jsonOrderData, secretKey.getBytes(StandardCharsets.UTF_8));

        System.out.println("Generated HTML form: " + htmlForm);
    }
}
