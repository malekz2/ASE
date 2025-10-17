package app;

import gradebook.models.User;

public class SecurityContext {
    private static User currentUser;

    public static void set(User u) { currentUser = u; }
    public static User get() { return currentUser; }
    public static boolean isLoggedIn() { return currentUser != null; }
    public static void clear() { currentUser = null; }
}
