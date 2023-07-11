package global;

import java.util.ArrayList;
import model.UserLogin;

public class INIT {
    public static final int MAX_MEMBER_LOGIN = 10;
    public static final long TIME = 2*60*60*1000;
    public static final ArrayList<UserLogin> MEMBER_LOGINS = new ArrayList<>();
    //local
    // public static final String HOST = "/smartcardapi";
    // public static final String JDBC = "jdbc:mysql://localhost:3306/smartcard";
    // public static final String USER_NAME_DB = "dattp";
    // public static final String PASS_WORD_DB = "dattp";
    // online
    public static final String HOST = "";
    public static final String JDBC = "jdbc:mysql://remotemysql.com:3306/M2tzaf1Z0W";
    public static final String USER_NAME_DB = Security.USERNAME_DB;
    public static final String PASS_WORD_DB = Security.PASSWORD_DB;
}