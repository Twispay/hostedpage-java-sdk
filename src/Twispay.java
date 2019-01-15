import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import javax.crypto.Mac;

/**
 * The Twispay class implements methods to get the value
 * of `jsonRequest` and `checksum` that need to be sent by POST
 * when making a Twispay order and to decrypt the Twispay IPN response.
 */
public class Twispay {

    /**
     * Get the `jsonRequest` parameter (order parameters as JSON and base64 encoded).
     *
     * @param JSONObject jsonOrderData The order parameters.
     *
     * @return String
     */
    public static String getBase64JsonRequest(JSONObject jsonOrderData) {
        return new String(Base64.getEncoder().encode(jsonOrderData.toString().getBytes()));
    }

    /**
     * Get the `checksum` parameter (the checksum computed over the `jsonRequest` and base64 encoded).
     *
     * @param JSONObject jsonOrderData The order parameters.
     * @param byte[] secretKey The secret key (from Twispay).
     *
     * @return String
     *
     * @throws RuntimeException
     */
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

    /**
     * Decrypt the IPN response from Twispay.
     *
     * @param String encryptedIpnResponse
     * @param byte[] secretKey The secret key (from Twispay).
     *
     * @return JSONObject
     *
     * @throws RuntimeException
     */
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
