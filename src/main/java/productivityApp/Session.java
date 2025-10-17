package productivityApp;

public class Session {
    private static User currentUser;
    public static void set(User u){ currentUser = u; }
    public static User get(){ return currentUser; }
    public static boolean isLoggedIn(){ return currentUser != null; }
}
