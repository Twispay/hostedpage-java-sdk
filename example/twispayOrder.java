import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.charset.StandardCharsets;

/**
 * Example code for generating a HTML form to be posted to Twispay.
 */
public class twispayOrder {
    public static void main(String args[]) throws ParseException {
        // sample data contains all available parameters
        // depending on order type, not all parameters are required/needed
        // you need to replace `siteId` etc. with valid data
        // do not send empty values for optional parameters
        HashMap<String, Object> sampleData = new HashMap<>();
        sampleData.put("siteId", 1); // mandatory

        HashMap<String, Object> customerData = new HashMap<>();
        customerData.put("identifier", "external-user-id");
        customerData.put("firstName", "John"); // conditional (if required by the bank/mid)
        customerData.put("lastName", "Doe"); // conditional (if required by the bank/mid)
        customerData.put("country", "US"); // conditional (if required by the bank/mid)
        customerData.put("state", "NY");
        customerData.put("city", "New York"); // conditional (if required by the bank/mid)
        customerData.put("address", "1st Street"); // conditional (if required by the bank/mid)
        customerData.put("zipCode", "11222"); // conditional (if required by the bank/mid)
        customerData.put("phone", "0012120000000"); // conditional (if required by the bank/mid)
        customerData.put("email", "john.doe@test.com"); // mandatory

        List<String> customerTags = new ArrayList<>();
        customerTags.add("customer_tag_1");
        customerTags.add("customer_tag_2");
        customerData.put("tags", customerTags);

        sampleData.put("customer", customerData); // mandatory

        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", "external-order-id"); // mandatory
        orderData.put("type", "recurring"); // mandatory; one of: purchase, recurring, managed
        orderData.put("amount", 2194.99); // mandatory
        orderData.put("currency", "USD"); // mandatory

        // use description or items; for airlines or tourism the items is mandatory
        orderData.put("description", "product or service description");

        List<HashMap> itemsList = new ArrayList<>(); // an array of item object; add any number of items on the cart

        HashMap<String, Object> itemData = new HashMap<>();
        itemData.put("item", "1 year subscription on site");
        itemData.put("unitPrice", 34.99);
        itemData.put("units", 1);
        itemData.put("type", "digital");
        itemData.put("code", "xyz");
        itemData.put("vatPercent", 19);
        itemData.put("itemDescription", "1 year subscription on site");
        itemsList.add(itemData);

        itemData = new HashMap<>();
        itemData.put("item", "200 tokens");
        itemData.put("unitPrice", 10.75);
        itemData.put("units", 200);
        itemData.put("type", "digital");
        itemData.put("code", "abc");
        itemData.put("vatPercent", 19);
        itemData.put("itemDescription", "200 tokens");
        itemsList.add(itemData);

        itemData = new HashMap<>();
        itemData.put("item", "discount");
        itemData.put("unitPrice", 10);
        itemData.put("units", 1);
        itemData.put("type", "digital");
        itemData.put("code", "fgh");
        itemData.put("vatPercent", 19);
        itemData.put("itemDescription", "discount");
        itemsList.add(itemData);

        orderData.put("items", itemsList);

        List<String> orderTags = new ArrayList<>();
        orderTags.add("tag_1");
        orderTags.add("tag_2");
        orderData.put("tags", orderTags);

        orderData.put("intervalType", "month"); // conditional (if order.type = recurring)
        orderData.put("intervalValue", 1); // conditional (if order.type = recurring)
        orderData.put("trialAmount", 1); // conditional (if order.type = recurring and you want smaller payment for trial)
        orderData.put("firstBillDate", "2020-10-02T12:00:00+00:00"); // conditional (if order.type = recurring)

        // next fields are mandatory if your business is airlines or tourism
        // send one of level3Airline, level3Tourism objects along with level3Type to match what you send
        orderData.put("level3Type", "airline");

        HashMap<String, Object> level3Airline = new HashMap<>();
        level3Airline.put("ticketNumber", "8V32EU");
        level3Airline.put("passengerName", "John Doe");
        level3Airline.put("flightNumber", "SQ619");
        level3Airline.put("departureDate", "2020-02-05T14:13:00+02:00");
        level3Airline.put("departureAirportCode", "KIX");
        level3Airline.put("arrivalAirportCode", "OTP");
        level3Airline.put("carrierCode", "American Airlines");
        level3Airline.put("travelAgencyCode", "19NOV05");
        level3Airline.put("travelAgencyName", "Elite Travel");
        orderData.put("level3Airline", level3Airline);

        HashMap<String, Object> level3Tourism = new HashMap<>();
        level3Tourism.put("tourNumber", "8V32EU");
        level3Tourism.put("travellerName", "John Doe");
        level3Tourism.put("flightNumber", "SQ619");
        level3Tourism.put("departureDate", "2020-02-05T14:13:00+02:00");
        level3Tourism.put("returnDate", "2020-02-06T14:13:00+02:00");
        level3Tourism.put("travelAgencyCode", "19NOV05");
        level3Tourism.put("travelAgencyName", "Elite Travel");
        orderData.put("level3Tourism", level3Tourism);

        sampleData.put("order", orderData);

        sampleData.put("cardTransactionMode", "authAndCapture"); // mandatory; one of: auth, authAndCapture
        sampleData.put("cardId", 1); // optional; use it if you want to suggest customer to use one of his previous saved cards
        sampleData.put("invoiceEmail", "john.doe@test.com"); // optional; if you need different email address than of the customer's where he will receive the payment confirmation
        sampleData.put("backUrl", "http://google.com"); // optional

        HashMap<String, String> customData = new HashMap<>();
        customData.put("key1", "value");
        customData.put("key2", "value");
        sampleData.put("customData", customData); // optional; any number of custom fields that you want to pass to twispay and get back on the transaction response

        // get the data as JSON
        JSONObject jsonOrderData = new JSONObject();
        jsonOrderData.putAll(sampleData);

        // your secret key
        String secretKey = "cd07b3c95dc9a0c8e9318b29bdc13b03";

        System.out.println("Sample jsonOrderData: " + jsonOrderData);
        System.out.println("Sample secretKey: " + secretKey);

        // TRUE for Twispay live site, otherwise Twispay stage will be used
        boolean twispayLive = false;

        // use base64JsonRequest and base64Checksum to generate a form
        String base64JsonRequest = Twispay.getBase64JsonRequest(jsonOrderData);
        String base64Checksum = Twispay.getBase64Checksum(jsonOrderData, secretKey.getBytes(StandardCharsets.UTF_8));
        String hostName = twispayLive ? "secure.twispay.com" : "secure-stage.twispay.com";
        String htmlForm = "<form action=\"https://" + hostName + "\" method=\"post\" accept-charset=\"UTF-8\">\n"
                + "<input type=\"hidden\" name=\"jsonRequest\" value=\"" + base64JsonRequest + "\">\n"
                + "<input type=\"hidden\" name=\"checksum\" value=\"" + base64Checksum + "\">\n"
                + "<input type=\"submit\" value=\"Pay\">\n"
                + "</form>";

        System.out.println("Generated HTML form: " + htmlForm);
    }
}
