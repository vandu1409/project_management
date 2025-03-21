package com.project_managament.services.impl;

import com.project_managament.services.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    private final String host;
    private final String port;
    private final String username;
    private final String password;

    public EmailServiceImpl() {
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            this.host = (String) env.lookup("mail.host");
            this.port = (String) env.lookup("mail.port");
            this.username = (String) env.lookup("mail.username");
            this.password = (String) env.lookup("mail.password");
        } catch (NamingException e) {
            throw new RuntimeException("Lỗi khi tải cấu hình email từ context.xml", e);
        }
    }

    @Override
    public void sendEmail(String to, String subject, String htmlContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi gửi email");
        }
    }
}
