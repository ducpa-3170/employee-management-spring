package com.example.employee_management.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Utility class để sinh JWT Secret Key
 *
 * Chạy class này để sinh một secret key mới:
 * - Cách 1: Run main method trực tiếp trong IDE
 * - Cách 2: Chạy lệnh: mvn exec:java -Dexec.mainClass="com.example.employee_management.utils.JwtSecretGenerator"
 *
 * Copy secret key được sinh ra và paste vào application.yml (jwt.secret)
 */
public class JwtSecretGenerator {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("JWT SECRET KEY GENERATOR");
        System.out.println("=".repeat(80));

        // Sinh secret key cho HS256 (256-bit)
        SecretKey keyHS256 = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64KeyHS256 = Base64.getEncoder().encodeToString(keyHS256.getEncoded());

        // Sinh secret key cho HS512 (512-bit) - RECOMMENDED
        SecretKey keyHS512 = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64KeyHS512 = Base64.getEncoder().encodeToString(keyHS512.getEncoded());

        System.out.println("\n📌 HS256 Secret Key (256-bit):");
        System.out.println(base64KeyHS256);
        System.out.println("\nKey length: " + keyHS256.getEncoded().length * 8 + " bits");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("\n📌 HS512 Secret Key (512-bit) - RECOMMENDED:");
        System.out.println(base64KeyHS512);
        System.out.println("\nKey length: " + keyHS512.getEncoded().length * 8 + " bits");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("\n✅ HOW TO USE:");
        System.out.println("1. Copy một trong hai secret key ở trên");
        System.out.println("2. Mở file: src/main/resources/application.yml");
        System.out.println("3. Thay thế giá trị của 'jwt.secret' bằng key vừa copy");
        System.out.println("4. Hoặc tốt hơn: Lưu vào biến môi trường hoặc file .env");
        System.out.println("\nVí dụ trong application.yml:");
        System.out.println("jwt:");
        System.out.println("  secret: " + base64KeyHS512);
        System.out.println("  expiration: 86400");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("\n🔐 SECURITY BEST PRACTICES:");
        System.out.println("1. KHÔNG commit secret key vào Git repository");
        System.out.println("2. Sử dụng biến môi trường trong production");
        System.out.println("3. Mỗi environment (dev, staging, prod) nên có secret key riêng");
        System.out.println("4. Thay đổi secret key định kỳ");
        System.out.println("\n" + "=".repeat(80));

        System.out.println("\n💡 ALTERNATIVE: Sử dụng với Environment Variable");
        System.out.println("\nTrong application.yml:");
        System.out.println("jwt:");
        System.out.println("  secret: ${JWT_SECRET:defaultSecretForDevelopment}");
        System.out.println("  expiration: ${JWT_EXPIRATION:86400}");

        System.out.println("\nKhi chạy ứng dụng:");
        System.out.println("export JWT_SECRET=\"" + base64KeyHS512 + "\"");
        System.out.println("export JWT_EXPIRATION=86400");
        System.out.println("mvn spring-boot:run");

        System.out.println("\n" + "=".repeat(80));
        System.out.println();
    }

    /**
     * Sinh secret key và trả về dưới dạng Base64 string
     * @param algorithm Algorithm để sinh key (HS256, HS384, HS512)
     * @return Base64 encoded secret key
     */
    public static String generateSecretKey(SignatureAlgorithm algorithm) {
        SecretKey key = Keys.secretKeyFor(algorithm);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Sinh secret key HS512 (recommended)
     * @return Base64 encoded HS512 secret key
     */
    public static String generateHS512SecretKey() {
        return generateSecretKey(SignatureAlgorithm.HS512);
    }

    /**
     * Sinh secret key HS256
     * @return Base64 encoded HS256 secret key
     */
    public static String generateHS256SecretKey() {
        return generateSecretKey(SignatureAlgorithm.HS256);
    }
}
