package trismegistoplanilla.utilities;

import java.util.Random;

public class GenerateCodeVerification {

    private static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String randomString(int len) {
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

}
