package filter;

import java.util.Date;

import global.INIT;
import model.User;
import model.UserLogin;

public class SessionF {
    public static int checkSession(UserLogin user){
        //0 chua dang nhap
        //1 thanh cong
        //2 session het thoi gian
        int index = INIT.MEMBER_LOGINS.indexOf(user);
        if(index<0){
            return 0;
        }

        UserLogin u = INIT.MEMBER_LOGINS.get(index);
        checkUser(u.getUser());
        user.setUser(u.getUser());
        user.setTime(u.getTime());
        long time_current = new Date().getTime();
        if(time_current<user.getTime()){
            return 1;
        }
        INIT.MEMBER_LOGINS.remove(user);
        return 2;
    }

    public static boolean checkUser(User user){
        


        return true;
    }
}
