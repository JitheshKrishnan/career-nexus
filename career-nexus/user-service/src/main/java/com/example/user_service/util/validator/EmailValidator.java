package com.example.user_service.util.validator;

import java.util.Set;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Set<String> DISPOSABLE_DOMAINS = Set.of(
            "tempmail.com", "throwaway.email", "guerrillamail.com"
    );

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isDisposableEmail(String email) {
        if (email == null) return false;
        String domain = email.substring(email.indexOf('@') + 1);
        return DISPOSABLE_DOMAINS.contains(domain.toLowerCase());
    }
}