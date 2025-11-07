package com.example.user_service.util.validator;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPER_CASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    public static boolean validatePassword(String password) {
        return password != null
                && hasMinimumLength(password)
                && hasUpperCase(password)
                && hasLowerCase(password)
                && hasDigit(password)
                && hasSpecialChar(password);
    }

    public static boolean hasMinimumLength(String password) {
        return password.length() >= MIN_LENGTH;
    }

    public static boolean hasUpperCase(String password) {
        return UPPER_CASE.matcher(password).find();
    }

    public static boolean hasLowerCase(String password) {
        return LOWER_CASE.matcher(password).find();
    }

    public static boolean hasDigit(String password) {
        return DIGIT.matcher(password).find();
    }

    public static boolean hasSpecialChar(String password) {
        return SPECIAL_CHAR.matcher(password).find();
    }
}