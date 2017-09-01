package com.tanawutmee.Chat.utility;

/**
 * Created by Tanawut.mee on 16-Mar-17.
 */
public class Utility {
    public static boolean isMessageValidated(String message) {
        return !(message == null || message.isEmpty());
    }

    public static boolean isUsernameAndPasswordEmpty(String username, String password) {
        return username.isEmpty() || password.isEmpty();
    }

    public static boolean isUsernameAndPasswordLessThan6Charactor(String username, String password) {
        return username.length() < 6 || password.length() < 6;
    }

    public static boolean isPasswordAndConfirmPasswordValidated(String password, String confirmPassword) {
        return !password.equals(confirmPassword);
    }
}
