package service;

import DAO.UserDAO;
import model.User;
import exception.InvalidInputException;

import java.util.Scanner;

public class UserService {
    private final UserDAO userDAO;
    private final EmailService emailService;
    private final OTPService otpService;
    private final Scanner sc = new Scanner(System.in);

    public UserService(UserDAO userDAO, EmailService emailService, OTPService otpService) {
        this.userDAO = userDAO;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    public void register(String role) {
        try {
            System.out.println("\n📝 --- Register New User (" + role + ") ---");
            System.out.print("Enter name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter email: ");
            String email = sc.nextLine().trim().toLowerCase();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty())
                throw new InvalidInputException("All fields are required.");

            if (userDAO.getUserByEmail(email) != null) {
                System.out.println("⚠️ Email already registered. Try login!");
                return;
            }

            User user = new User(0, name, email, pass, false, role);
            userDAO.addUser(user);

            boolean verified = false;
            int attempts = 0;

            while (!verified && attempts < 3) {
                String otp = otpService.generateOTP(email);
                try {
                    emailService.sendEmail(email, "Your OTP for Inventory App", "Your OTP is: " + otp);
                } catch (Exception e) {
                    System.out.println("❌ Failed to send OTP. Check your email setup.");
                    return;
                }

                System.out.print("Enter OTP sent to your email: ");
                String entered = sc.nextLine().trim();

                if (otpService.validateOTP(email, entered)) {
                    user.setVerified(true);
                    userDAO.updateUser(user);
                    otpService.clearOTP(email);
                    System.out.println("🎉 Email verified successfully! You may login now.");
                    verified = true;
                } else {
                    System.out.println("❌ Invalid OTP. Try again.");
                    attempts++;
                }
            }

            if (!verified) {
                System.out.println("⚠️ Verification failed after 3 attempts. Try register later.");
            }

        } catch (InvalidInputException ex) {
            System.out.println("⚠️ " + ex.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Registration error: " + e.getMessage());
        }
    }

    public User login() {
        try {
            System.out.println("\n🔐 --- User Login ---");
            System.out.print("Enter email: ");
            String email = sc.nextLine().trim().toLowerCase();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();

            User u = userDAO.getUserByEmail(email);
            if (u == null) {
                System.out.println("❌ No account with this email.");
                return null;
            }
            if (!u.getPassword().equals(pass)) {
                System.out.println("❌ Incorrect password.");
                return null;
            }

            if (!u.isVerified()) {
                System.out.println("⚠️ Email not verified. Sending OTP again...");
                int attempts = 0;
                boolean verified = false;

                while (!verified && attempts < 3) {
                    String otp = otpService.generateOTP(email);
                    emailService.sendEmail(email, "Verify your account (login)", "Your OTP: " + otp);

                    System.out.print("Enter OTP: ");
                    String entered = sc.nextLine().trim();

                    if (otpService.validateOTP(email, entered)) {
                        u.setVerified(true);
                        userDAO.updateUser(u);
                        otpService.clearOTP(email);
                        System.out.println("🎉 Verified — logged in.");
                        verified = true;
                        return u;
                    } else {
                        System.out.println("❌ OTP invalid. Try again.");
                        attempts++;
                    }
                }

                if (!verified) {
                    System.out.println("❌ Verification failed. Contact admin.");
                    return null;
                }
            }

            System.out.println("✅ Welcome back, " + u.getName() + "!");
            return u;

        } catch (Exception e) {
            System.out.println("⚠️ Login failed: " + e.getMessage());
            return null;
        }
    }
}
