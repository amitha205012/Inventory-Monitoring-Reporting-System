package service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {
    private final Map<String, String> otpMap = new HashMap<>();
    private final Random rnd = new Random();

    public String generateOTP(String email) {
        String otp = String.format("%06d", rnd.nextInt(1_000_000));
        otpMap.put(email, otp);
        return otp;
    }

    public boolean validateOTP(String email, String otp) {
        String actual = otpMap.get(email);
        return actual != null && actual.equals(otp);
    }

    public void clearOTP(String email) { otpMap.remove(email); }
}
