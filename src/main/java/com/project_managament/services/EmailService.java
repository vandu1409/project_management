package com.project_managament.services;

import com.project_managament.models.Email;

public interface EmailService {
     void sendEmail(String to, String subject, String htmlContent);
}
