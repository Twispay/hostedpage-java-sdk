package src;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TwispaySample {
    public static JSONObject getOrderData() {
        // sample data contains all available parameters
        // depending on order type, not all parameters are required/needed
        // you need to replace `siteId` etc. with valid data
        HashMap<String, Object> sampleData = new HashMap<>();
        sampleData.put("siteId", 1);

        HashMap<String, Object> customerData = new HashMap<>();
        customerData.put("identifier", "identifier");
        customerData.put("firstName", "John");
        customerData.put("lastName", "Doe");
        customerData.put("country", "US");
        customerData.put("state", "NY");
        customerData.put("city", "New York");
        customerData.put("address", "1st Street");
        customerData.put("zipCode", "11222");
        customerData.put("phone", "0012120000000");
        customerData.put("email", "john.doe@test.com");

        List<String> customerTags = new ArrayList<>();
        customerTags.add("customer_tag_1");
        customerTags.add("customer_tag_2");
        customerData.put("tags", customerTags);

        sampleData.put("customer", customerData);

        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", "external-order-id");
        orderData.put("type", "recurring");
        orderData.put("amount", 2194.99);
        orderData.put("currency", "USD");

        List<HashMap> itemsList = new ArrayList<>();

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

        orderData.put("intervalType", "month");
        orderData.put("intervalValue", 1);
        orderData.put("trialAmount", 1);
        orderData.put("firstBillDate", "2020-10-02T12:00:00+00:00");

        HashMap<String, Object> level3Airline = new HashMap<>();
        orderData.put("ticketNumber", "8V32EU");
        orderData.put("passengerName", "John Doe");
        orderData.put("flightNumber", "SQ619");
        orderData.put("departureDate", "2020-02-05T14:13:00+02:00");
        orderData.put("departureAirportCode", "KIX");
        orderData.put("arrivalAirportCode", "OTP");
        orderData.put("carrierCode", "American Airlines");
        orderData.put("travelAgencyCode", "19NOV05");
        orderData.put("travelAgencyName", "Elite Travel");
        orderData.put("level3Airline", level3Airline);

        sampleData.put("order", orderData);

        sampleData.put("cardTransactionMode", "authAndCapture");
        sampleData.put("cardId", 1);
        sampleData.put("invoiceEmail", "john.doe@test.com");
        sampleData.put("backUrl", "http://google.com");

        HashMap<String, String> customData = new HashMap<>();
        customData.put("key1", "value");
        customData.put("key2", "value");
        sampleData.put("customData", customData);

        // get the data as JSON text
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(sampleData);

        return jsonObject;
    }

    public static String getEncryptedIpnResponse() {
        return "oUrO8wW0IXK1yj9F8RYbHw==,Hrw4AkEt+DBALL4P9gNDyBxkvnjh3wxlgAdqe1jVffEGrwpEpCKc3eYjR4l+mi9dCxPuvXRceVgqd7ypn9aXGLXejxClumv4l2Ym2djbpsi2PFRWyWXHoJar+NX8aLU/yCYdHUoNtvoZRA2RI13IUCLZZ1znlQdyEL9NXQTEAxrbZe7a4vmYbUDBosAiIfApGLGMWQG/OF+ebukvLeZGajzUbhbp69k8/UD03dT8NBDMSos5XayJNnEibM2unImh6tcOek5prenHQOqkIv7TeGfC3HQDxUgXH2Rw8j+7Kyu/p72AYTCvXrJOoAVJ00KKDXTi4xu7+a5VJwP/tpdLz5jeoIfivzgxPP9I/o72OhSrdAZcxPQ5YjbyS22IXhz7G1MkHX0ItytWRqKyfXjq+58LS2ovlQu3eYhoftfBjsq3xisdjqTld9V+DL97qCcWzHo7hscMLO7/5nrXsGiSY16PZ6tUtqe9lI4ErvC+71iH+i44NijMTXMt9uX01V/4Wqlz8m5sDE4Nl0uM31eV2M1MvLKyV1tntj78WREX/mpuqclD8wWO+weglzqfyaF/";
    }

    public static String getSecretKey() {
        return "cd07b3c95dc9a0c8e9318b29bdc13b03";
    }
}
