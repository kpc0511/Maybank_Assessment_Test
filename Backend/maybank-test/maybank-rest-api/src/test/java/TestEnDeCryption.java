
import com.alibaba.fastjson2.JSONObject;
import com.maybank.platform.services.util.AES256Utils;
import com.maybank.platform.services.util.QueryStringParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestEnDeCryption {
    private String salt = "q1KJ2kt7c3bRM9n3s5wQpQ==";
    private String secretKey = "8Sw1doXBThPB4G2n38DisSGu79VdWD8g";

    @Test
    public void testEncryptionDecryption() {
        String originalText = "HelloWorld";

        // Encrypt the original text
        String encryptedText = AES256Utils.encrypt(originalText, secretKey);
        System.out.println("testEncryptionDecryption::encryptedText:"+encryptedText);
        // Decrypt the encrypted text
        String decryptedText = AES256Utils.decrypt(encryptedText, secretKey);
        System.out.println("testEncryptionDecryption::decryptedText:"+decryptedText);
        // Verify that the decrypted text matches the original text
        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testEncryptionDecryptionWithEmptyString() {
        String originalText = "";

        // Encrypt the original text
        String encryptedText = AES256Utils.encrypt(originalText, secretKey);

        // Decrypt the encrypted text
        String decryptedText = AES256Utils.decrypt(encryptedText, secretKey);

        // Verify that the decrypted text matches the original text
        assertEquals(originalText, decryptedText);
    }

    @Test
    public void testEncryptionDecryptionWithSpecialCharacters() {
        String originalText = "Hello@World#123";

        // Encrypt the original text
        String encryptedText = AES256Utils.encrypt(originalText, secretKey);

        // Decrypt the encrypted text
        String decryptedText = AES256Utils.decrypt(encryptedText, secretKey);

        // Verify that the decrypted text matches the original text
        assertEquals(originalText, decryptedText);
    }
    @Test
    public void testEncryptionDecryptionWithRealData() {
        String originalText = "username=moduser1&password=12345678";

        // Encrypt the original text
        String encryptedText = AES256Utils.encrypt(originalText, secretKey);
        System.out.println("testEncryptionDecryptionWithRealData::encryptedText:"+encryptedText);
        // Decrypt the encrypted text
        String decryptedText = AES256Utils.decrypt(encryptedText, secretKey);
        System.out.println("testEncryptionDecryptionWithRealData::decryptedText:"+decryptedText);
        // Verify that the decrypted text matches the original text
        assertEquals(originalText, decryptedText);

        JSONObject jsonObject = QueryStringParser.parse(originalText);

        // Get username and password from the JSON object
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        assertEquals("moduser1", username);
        assertEquals("12345678", password);
    }

    @Test
    public void testDeCryption() {
        String data = "ZFK6QXbaBNd36mQxNHMyZRXQYQ3+AgwK3eauT38nd/UGDJKs68vViGkUAL2lYW7p";
        String decryptedText = AES256Utils.decrypt(data, secretKey);
        System.out.println("testDeCryption::decryptedText:"+decryptedText);
    }
}
