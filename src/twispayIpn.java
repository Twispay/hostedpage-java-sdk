package src;

import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;

public class twispayIpn {
    public static void main(String args[]) {
        // normally you get the encrypted data from the HTTP request (POST/GET) in the `opensslResult` parameter
        String encryptedIpnResponse = TwispaySample.getEncryptedIpnResponse();

        // your secret key
        String secretKey = TwispaySample.getSecretKey();

        if (args.length == 2) {
            System.out.println("Arguments provided for encrypted IPN response and secret key.");
            encryptedIpnResponse = args[0];
            secretKey = args[1];
        } else {
            System.out.println("No arguments provided for encrypted IPN response and secret key, using sample values!");
        }

        System.out.println("encryptedIpnResponse: " + encryptedIpnResponse);
        System.out.println("secretKey: " + secretKey);

        // get the JSON IPN response
        JSONObject jsonResponse = Twispay.decryptIpnResponse(encryptedIpnResponse, secretKey.getBytes(StandardCharsets.UTF_8));

        System.out.println("Decrypted IPN response: " + jsonResponse);
    }
}
