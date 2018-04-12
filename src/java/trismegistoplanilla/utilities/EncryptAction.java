package trismegistoplanilla.utilities;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class EncryptAction {

  public static String secretKey = "";

  public static String Encriptar(String texto, String secretKey) {
    String base64EncryptedString = "";

    try {

      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
      byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

      SecretKey key = new SecretKeySpec(keyBytes, "DESede");
      Cipher cipher = Cipher.getInstance("DESede");
      cipher.init(Cipher.ENCRYPT_MODE, key);

      byte[] plainTextBytes = texto.getBytes("utf-8");
      byte[] buf = cipher.doFinal(plainTextBytes);
      byte[] base64Bytes = Base64.encodeBase64(buf);
      base64EncryptedString = new String(base64Bytes);

      if (base64EncryptedString.contains("+")) {
        if (base64EncryptedString.contains("+")) {
          System.out.println("tiene +");
          char busqueda = '+';
          int n = 0;
          for (char c : base64EncryptedString.toCharArray()) {
            if (c == busqueda) {
              base64EncryptedString = base64EncryptedString.replace("+", "-");
              n++;
            }
          }
        }
      }

    } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
      System.out.println("Error encriptación: " + ex.getMessage());
    }
    return base64EncryptedString;
  }

  public static String Desencriptar(String textoEncriptado, String secretKey) throws Exception {
    String base64EncryptedString = "";

    try {
      byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
      byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
      SecretKey key = new SecretKeySpec(keyBytes, "DESede");

      Cipher decipher = Cipher.getInstance("DESede");
      decipher.init(Cipher.DECRYPT_MODE, key);

      byte[] plainText = decipher.doFinal(message);

      base64EncryptedString = new String(plainText, "UTF-8");

    } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
      System.out.println("Error desencriptación: " + ex.getMessage());
    }
    return base64EncryptedString;
  }

//    public static void main(String[] args) throws Exception {
//        System.out.println("encript: " + Encriptar("1", "TR1SM3G1ST0-ID-PL4N1LL4"));
//        System.out.println("encript: " + Encriptar("420", "key"));
//        System.out.println("encript: " + Encriptar("codigo_persona", "key2"));
//        System.out.println("encript: " + Encriptar("$_alonso", "key3"));
//        System.out.println("*****************************************");
//        System.out.println("decript: " + Desencriptar("GRu+q9kNerU=", "TR1SM3G1ST0-ID-PL4N1LL4"));
//        System.out.println("decript: " + Desencriptar("RE4NjC-GSUc=", "key"));
//        System.out.println("decript: " + Desencriptar("MiVveElYrWs=", "key2"));
//        System.out.println("decript: " + Desencriptar("J0vS3gUoAXo=", "key3"));
//    }

}
