package service;

import exception.EmailException;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private final String smtpHost;
    private final String smtpPort;
    private final String smtpUser;
    private final String smtpPass;

    public EmailService() {
        // Read credentials from environment variables
        smtpHost = System.getenv("SMTP_HOST"); // e.g. smtp.gmail.com
        smtpPort = System.getenv("SMTP_PORT"); // e.g. 587
        smtpUser = System.getenv("SMTP_USER"); // your Gmail address
        smtpPass = System.getenv("SMTP_PASS"); // app password (not Gmail password)
    }

    public void sendEmail(String to, String subject, String body) {
        // ✅ If SMTP credentials missing → simulate email (for testing)
        if (smtpHost == null || smtpUser == null || smtpPass == null || smtpPort == null) {
            System.out.println("📩 (SIMULATED) Sending Email to: " + to);
            System.out.println("📬 Subject: " + subject);
            System.out.println("📝 Message:\n" + body);
            System.out.println("✅ Simulated email sent!\n");
            return;
        }

        try {
            // ✅ Gmail SMTP recommended configuration
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.ssl.trust", smtpHost);

            // Create session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPass);
                }
            });

            // ✅ Build email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // ✅ Send email
            Transport.send(message);
            System.out.println("📨 Email sent successfully to: " + to);

        } catch (MessagingException e) {
            throw new EmailException("❌ Failed to send email: " + e.getMessage(), e);
        }
    }

    // ✅ Optional helper to send OTP easily
    public void sendOTP(String toEmail, String otp) {
        String subject = "🔐 Your OTP Code";
        String body = "Hello!\n\nYour One-Time Password (OTP) is: " + otp +
                "\n\nPlease do not share this code with anyone.\n\nThis code expires in 5 minutes.\n\nBest Regards,\nInventory System Team";
        sendEmail(toEmail, subject, body);
    }

    public void sendVerificationSuccess(String toEmail) {
        String subject = "✅ Email Verified Successfully!";
        String body = "Congratulations! 🎉\n\nYour email has been successfully verified.\nWelcome to our inventory system.";
        sendEmail(toEmail, subject, body);
    }
}
