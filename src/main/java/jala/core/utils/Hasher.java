package jala.core.utils;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class Hasher {
    /*
      receive: User password
      returns: User password hashed with SHA256 Algorithm
    */
    public static String sha256Hash(String password){
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
