import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KeyFingerprint {
    public static void main(String[] args) throws Exception {
        for (String file : args) {
            String pem = Files.readString(Path.of(file))
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(pem);
            PublicKey key = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(keyBytes));

            byte[] hash = MessageDigest.getInstance("SHA-256")
                .digest(key.getEncoded());

            System.out.println(
                file + " : " +
                HexFormat.of().formatHex(hash)
            );
        }
    }
}
