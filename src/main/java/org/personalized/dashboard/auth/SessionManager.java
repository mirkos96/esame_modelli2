package org.personalized.dashboard.auth;

/**
 * Created by sudan on 28/5/15.
 */
public class SessionManager {

    private static ThreadLocal<String> userId = new ThreadLocal<String>();

    public static String getUserIdFromSession() {
        return userId.get();
    }

    public static void setUserId(String extractedUserId) {
        userId.set(extractedUserId);
    }
}
