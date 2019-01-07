package src;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.Mac;

public class Twispay {

    public static String getBase64JsonRequest(JSONObject jsonOrderData) {
        return new String(Base64.getEncoder().encode(jsonOrderData.toString().getBytes()));
    }

    public static String getBase64Checksum(JSONObject jsonOrderData, byte[] secretKey) throws RuntimeException {
        byte[] hmacSha512;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey, "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(signingKey);
            hmacSha512 = mac.doFinal(jsonOrderData.toString().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new String(Base64.getEncoder().encode(hmacSha512));
    }

    public static String getHtmlOrderForm(JSONObject jsonOrderData, byte[] secretKey, boolean twispayLive) throws RuntimeException {
        String base64JsonRequest = Twispay.getBase64JsonRequest(jsonOrderData);
        String base64Checksum = Twispay.getBase64Checksum(jsonOrderData, secretKey);
        String hostName = twispayLive ? "secure.twispay.com" : "secure-stage.twispay.com";
        return "<form action=\"https://" + hostName + "\" method=\"post\" accept-charset=\"UTF-8\">\n"
                + "<input type=\"hidden\" name=\"jsonRequest\" value=\"" + base64JsonRequest + "\">\n"
                + "<input type=\"hidden\" name=\"checksum\" value=\"" + base64Checksum + "\">\n"
                + "<input type=\"submit\" value=\"Pay\">\n"
                + "</form>";
    }

    public static String getHtmlOrderForm(JSONObject jsonOrderData, byte[] secretKey) throws RuntimeException {
        return getHtmlOrderForm(jsonOrderData, secretKey, false);
    }

    public static JSONObject decryptIpnResponse(String encryptedIpnResponse, byte[] secretKey) throws RuntimeException {
        // get the IV and the encrypted data
        String[] encryptedParts = encryptedIpnResponse.split(",", 2);
        byte[] iv = Base64.getDecoder().decode(encryptedParts[0]);
        byte[] encryptedData = Base64.getDecoder().decode(encryptedParts[1]);

        // decrypt the encrypted data
        byte[] decryptedIpnResponse;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            decryptedIpnResponse = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // JSON decode decrypted data
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) jsonParser.parse(new String(decryptedIpnResponse));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jsonResponse;
    }
}
