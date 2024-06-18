package com.forero.infrastructure;

public class contraseña {

    public static boolean isValidPassword(String password) {
        // Check length
        if (password.length() < 7 || password.length() > 31) {
            return false;
        }

        // Check case
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
        }
        if (!hasUppercase || !hasLowercase) {
            return false;
        }

        // Check special characters
        boolean hasSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (isSpecialCharacter(c)) {
                hasSpecialCharacter = true;
                break;
            }
        }
        if (!hasSpecialCharacter) {
            return false;
        }

        // Check for spaces
        if (password.contains(" ")) {
            return false;
        }

        return true;
    }

    private static boolean isSpecialCharacter(char c) {
        String specialChars = "~!@#$%^&*()_-+={}[]|;:<>,.?/'";
        return specialChars.contains(Character.toString(c));
    }

    public static void main(String[] args) {
        final String password = "Pa$$w0rd123";
        boolean isValid = isValidPassword(password);
        System.out.println("Is password '" + password + "' valid? " + isValid);
    }
}
