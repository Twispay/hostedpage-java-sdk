# Twispay sample code for Java

Sample code for generating a HTML form for a Twispay order:

```java
// sample data contains order parameters
HashMap<String, Object> sampleData = new HashMap<>();
sampleData.put("siteId", 1);

HashMap<String, Object> customerData = new HashMap<>();
customerData.put("identifier", "external-user-id");
customerData.put("firstName", "John");
customerData.put("lastName", "Doe");
customerData.put("country", "US");
customerData.put("state", "NY");
customerData.put("city", "New York");
customerData.put("address", "1st Street");
customerData.put("zipCode", "11222");
customerData.put("phone", "0012120000000");
customerData.put("email", "john.doe@test.com");

sampleData.put("customer", customerData);

HashMap<String, Object> orderData = new HashMap<>();
orderData.put("orderId", "external-order-id");
orderData.put("type", "purchase");
orderData.put("amount", 2194.99);
orderData.put("currency", "USD");
orderData.put("description", "1 year subscription on site");

sampleData.put("order", orderData);

sampleData.put("cardTransactionMode", "authAndCapture");
sampleData.put("invoiceEmail", "john.doe@test.com");
sampleData.put("backUrl", "http://google.com");

// get the data as JSON
JSONObject jsonOrderData = new JSONObject();
jsonOrderData.putAll(sampleData);

// your secret key
String secretKey = "cd07b3c95dc9a0c8e9318b29bdc13b03";

// TRUE for Twispay live site, otherwise Twispay stage will be used
boolean twispayLive = false;

// get the HTML form
String base64JsonRequest = Twispay.getBase64JsonRequest(jsonOrderData);
String base64Checksum = Twispay.getBase64Checksum(jsonOrderData, secretKey.getBytes(StandardCharsets.UTF_8));
String hostName = twispayLive ? "secure.twispay.com" : "secure-stage.twispay.com";
String htmlForm = "<form action=\"https://" + hostName + "\" method=\"post\" accept-charset=\"UTF-8\">\n"
    + "<input type=\"hidden\" name=\"jsonRequest\" value=\"" + base64JsonRequest + "\">\n"
    + "<input type=\"hidden\" name=\"checksum\" value=\"" + base64Checksum + "\">\n"
    + "<input type=\"submit\" value=\"Pay\">\n"
    + "</form>";
```

Sample code for decrypting the Twispay IPN response:

```java
// normally you get the encrypted data from the HTTP request (POST/GET) in the `opensslResult` parameter
String encryptedIpnResponse = "oUrO8wW0IXK1yj9F8RYbHw==,Hrw4AkEt+DBALL4P9gNDyBxkvnjh3wxlgAdqe1jVffEGrwpEpCKc3eYjR4l+mi9dCxPuvXRceVgqd7ypn9aXGLXejxClumv4l2Ym2djbpsi2PFRWyWXHoJar+NX8aLU/yCYdHUoNtvoZRA2RI13IUCLZZ1znlQdyEL9NXQTEAxrbZe7a4vmYbUDBosAiIfApGLGMWQG/OF+ebukvLeZGajzUbhbp69k8/UD03dT8NBDMSos5XayJNnEibM2unImh6tcOek5prenHQOqkIv7TeGfC3HQDxUgXH2Rw8j+7Kyu/p72AYTCvXrJOoAVJ00KKDXTi4xu7+a5VJwP/tpdLz5jeoIfivzgxPP9I/o72OhSrdAZcxPQ5YjbyS22IXhz7G1MkHX0ItytWRqKyfXjq+58LS2ovlQu3eYhoftfBjsq3xisdjqTld9V+DL97qCcWzHo7hscMLO7/5nrXsGiSY16PZ6tUtqe9lI4ErvC+71iH+i44NijMTXMt9uX01V/4Wqlz8m5sDE4Nl0uM31eV2M1MvLKyV1tntj78WREX/mpuqclD8wWO+weglzqfyaF/";

// your secret key
String secretKey = "cd07b3c95dc9a0c8e9318b29bdc13b03";

// get the IPN response
JSONObject jsonResponse = Twispay.decryptIpnResponse(encryptedIpnResponse, secretKey.getBytes(StandardCharsets.UTF_8));
```

Run the sample code from the command line with Apache Ant:

- execute `ant order` to generate and output the HTML form for a Twispay order;
- execute `ant ipn` to decrypt and output the received data from a IPN call.
