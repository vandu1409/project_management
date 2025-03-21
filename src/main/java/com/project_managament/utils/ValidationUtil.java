package com.project_managament.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requirePositive(int number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requirePositive(long number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateColor(String color, String message) {
        if (!color.matches("^#[0-9A-Fa-f]{6}$")) throw new IllegalArgumentException(message);
    }

    public static void validateComment(String content, int userId, int taskId) {
        requireNonEmpty(content, "Comment content cannot be empty!");
        requirePositive(userId, "Invalid user ID!");
        requirePositive(taskId, "Invalid task ID!");
    }

    public static void validateEmail(String email) {
        requireNonEmpty(email, "Email cannot be empty!");
        if (!EMAIL_PATTERN.matcher(email).matches()) throw new IllegalArgumentException("Invalid email format!");
    }

    public static void validatePhone(String phone) {
        requireNonEmpty(phone, "Phone number cannot be empty!");
        if (!PHONE_PATTERN.matcher(phone).matches()) throw new IllegalArgumentException("Invalid phone number format!");
    }

    public static void validateUserProfile(String name, String phone) {
        requireNonEmpty(name, "Name cannot be empty!");
        validatePhone(phone);
    }


    public static void validatePassword(String password) {
        requireNonEmpty(password, "Mật khẩu không được để trống!");
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự, gồm chữ và số!");
        }
    }

    public static void validateUser(String email, String password) {
        validateEmail(email);
        validatePassword(password);
    }

}
