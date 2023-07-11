package api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import dao.AdminDAO;
import dao.CardDAO;
import dao.HistoryExitDAO;
import dao.UserDAO;
import filter.SessionF;
import global.INIT;
import model.Admin;
import model.Card;
import model.HistoryExit;
import model.User;
import model.UserLogin;
import utils.JsonCustom;

@WebServlet(urlPatterns = {"/user/add_user","/user/get_user","/user/get_history_exit","/user/login","/admin/get_history_exit","/user/logout","/user/add_point","/user/sub_point","/user/profile","/user/update_profile"})
public class UserAPI extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String url = req.getRequestURI();
        String host = INIT.HOST;
        System.out.println(url);
        System.out.println(host);
        if(url.equals(host+"/user/add_user")){
            addUser(req,resp);
        }else if(url.equals(host+"/user/get_user")){
            getUser(req,resp);
        }else if(url.equals(host+"/user/get_history_exit")){
            getHistoryExit(req,resp);
        }else if(url.equals(host+"/user/login")){
            Login(req,resp);
        }else if(url.equals(host+"/admin/get_history_exit")){
            getHistoryExitAdmin(req,resp);
        }else if(url.equals(host+"/user/logout")){
            Logout(req, resp);
        }else if(url.equals(host+"/user/add_point")){
            addPoint(req, resp);
        }else if(url.equals(host+"/user/sub_point")){
            subPoint(req, resp);
        }else if(url.equals(host+"/user/profile")){
            getProfile(req, resp);;
        }else if(url.equals(host+"/user/update_profile")){
            updateProfile(req, resp);
        }
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            String cardNote = "";
            int cardActive = 1;
            String fullNameU = objReq.getString("full_name");
            String userNameU = objReq.getString("user_name").replaceAll("\\s+", " ");
            String passWordU = objReq.getString("pass_word").replaceAll("\\s+", " ");
            long pointU = 0;
            int activeU = 1;
            try{pointU = objReq.getLong("point");}catch(Exception e){}
            try{cardNote = objReq.getString("card_note");}catch(Exception e){}
            try{cardActive = objReq.getInt("card_active");}catch(Exception e){}
            // check connect DB
            AdminDAO adminDAO = new AdminDAO();
            if(!adminDAO.connect()){
                jsonResp.put("code",500);
                // jsonResp.put("description","Không kết nối được CSDL");
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // check permission
            Admin admin = new Admin(-1, "", userNameA, passWordA);
            if(!adminDAO.check(admin)){
                jsonResp.put("code",700);
                // jsonResp.put("description","không dủ quyền");
                jsonResp.put("description","Khong du quyen");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // add user
            Card card = new Card(cardID, null, cardNote, cardActive);
            User user = new User(-1, pointU, fullNameU, userNameU, passWordU, activeU, card);
            card.setUser(user);
            UserDAO userDAO = new UserDAO(); userDAO.setConnection(adminDAO.getConnection());
            int userStatus = userDAO.add(user);
            userDAO.close();
            if(userStatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đã được đăng ký");
                jsonResp.put("description","The da duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            if(userStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Không thêm được");
                jsonResp.put("description","Khong them duoc");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
            adminDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void getUser(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            // check connect DB
            AdminDAO adminDAO = new AdminDAO();
            if(!adminDAO.connect()){
                jsonResp.put("code",500);
                // jsonResp.put("description","Không kết nối được CSDL");
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // check permission
            Admin admin = new Admin(-1, "", userNameA, passWordA);
            if(!adminDAO.check(admin)){
                jsonResp.put("code",700);
                // jsonResp.put("description","không dủ quyền");
                jsonResp.put("description","Khong du quyen");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // check user
            Card card = new Card(cardID, new User(), "", -1);
            UserDAO userDAO = new UserDAO();
            userDAO.setConnection(adminDAO.getConnection());
            int userStatus = userDAO.check(card);
            userDAO.close();
            if(userStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            if(userStatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đang tạm khóa");
                jsonResp.put("description","The dang tam khoa");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            JSONObject result = new JSONObject();
            result.put("user_id", card.getUser().getId());
            result.put("full_name", card.getUser().getFullName());
            result.put("user_name", card.getUser().getUsername());
            result.put("point", card.getUser().getPoint());
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
            jsonResp.put("result", result);
            adminDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void getProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String session = objReq.getString("session");
            // check session
            UserLogin user = new UserLogin(new User(), session, -1);
            int sessionStatus = SessionF.checkSession(user);
            if(sessionStatus==0){
                jsonResp.put("code",300);
                jsonResp.put("description","Người dùng chưa đăng nhập");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            if(sessionStatus==2){
                jsonResp.put("code",700);
                jsonResp.put("description","Phiên đăng nhập của bạn đã kết thúc");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // check connect DB
            UserDAO userDAO = new UserDAO();
            if(!userDAO.connect()){
                jsonResp.put("code",500);
                jsonResp.put("description","Không kết nối được CSDL");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            //profile
            Card card = user.getUser().getCard();
            JSONObject result = new JSONObject();
            result.put("user_id", card.getUser().getId());
            result.put("full_name", card.getUser().getFullName());
            result.put("user_name", card.getUser().getUsername());
            result.put("point", card.getUser().getPoint());
            //card
            JSONObject CardJson = new JSONObject();
            CardJson.put("card_id", card.getId());
            CardJson.put("card_note",card.getNote());
            CardJson.put("card_active", card.getActive());
            result.put("card", CardJson);
            //
            jsonResp.put("code",200);
            jsonResp.put("description","Thành công");
            jsonResp.put("result", result);
            userDAO.close();
            
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String session = objReq.getString("session");
            JSONObject UserJson = objReq.getJSONObject("user");
            String fullName = UserJson.getString("full_name");
            // check session
            UserLogin user = new UserLogin(new User(), session, -1);
            int sessionStatus = SessionF.checkSession(user);
            if(sessionStatus==0){
                jsonResp.put("code",300);
                jsonResp.put("description","Người dùng chưa đăng nhập");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            if(sessionStatus==2){
                jsonResp.put("code",700);
                jsonResp.put("description","Phiên đăng nhập của bạn đã kết thúc");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // check connect DB
            UserDAO userDAO = new UserDAO();
            if(!userDAO.connect()){
                jsonResp.put("code",500);
                jsonResp.put("description","Không kết nối được CSDL");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            //profile
            User newUser = new User();
            newUser.setId(user.getUser().getId());
            newUser.setFullName(fullName);
            if(!userDAO.updateProfile(newUser)){
                jsonResp.put("code",300);
                jsonResp.put("description","Cập nhật không thành công");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            jsonResp.put("code",200);
            jsonResp.put("description","Thành công");
            userDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void getHistoryExit(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String session = objReq.getString("session");
            // check session
            UserLogin user = new UserLogin(new User(), session, -1);
            int sessionStatus = SessionF.checkSession(user);
            if(sessionStatus==0){
                jsonResp.put("code",300);
                jsonResp.put("description","Người dùng chưa đăng nhập");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            if(sessionStatus==2){
                jsonResp.put("code",700);
                jsonResp.put("description","Phiên đăng nhập của bạn đã kết thúc");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // check connect DB
            HistoryExitDAO hisDAO = new HistoryExitDAO();
            if(!hisDAO.connect()){
                jsonResp.put("code",500);
                jsonResp.put("description","Không kết nối được CSDL");
                writer.print(jsonResp);
                writer.close();
                hisDAO.close();
                return;
            }
            // get history exit
            ArrayList<HistoryExit> listHis = hisDAO.get(user.getUser().getCard().getId());
            JSONObject result = new JSONObject();
            JSONArray listJson = new JSONArray();
            for(HistoryExit his: listHis){
                JSONObject hisJson = new JSONObject();
                hisJson.put("card_id", his.getCardID());
                hisJson.put("time", his.getTimeFormat());
                hisJson.put("image", his.getImage()==null?"":his.getImage());
                hisJson.put("type", his.getType());
                hisJson.put("note", his.getNote()==null?"":his.getNote());
                listJson.put(hisJson);
            }
            result.put("list", listJson);
            jsonResp.put("code",200);
            jsonResp.put("description","Thành công");
            jsonResp.put("result", result);
            hisDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
            e.printStackTrace();
        }
        writer.print(jsonResp);
        writer.close();
    }


    private void getHistoryExitAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            int userID = objReq.getInt("user_id");
            // check connect DB
            AdminDAO adminDAO = new AdminDAO();
            if(!adminDAO.connect()){
                jsonResp.put("code",500);
                // jsonResp.put("description","Không kết nối được CSDL");
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // check permission
            Admin admin = new Admin(-1, "", userNameA, passWordA);
            if(!adminDAO.check(admin)){
                jsonResp.put("code",700);
                // jsonResp.put("description","không dủ quyền");
                jsonResp.put("description","Khong du quyen");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // check user
            User user = new User();
            user.setId(userID);
            UserDAO userDAO = new UserDAO(); userDAO.setConnection(adminDAO.getConnection());
            int userStatus = userDAO.check(user);
            if(userStatus==0){
                jsonResp.put("code",700);
                // jsonResp.put("description","Người dùng chưa đăng kí tài khoản");
                jsonResp.put("description","Nguoi dung chua dang ky tai khoan");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            if(userStatus==2){
                jsonResp.put("code",700);
                // jsonResp.put("description","Tài khoản đang tạm khóa");
                jsonResp.put("description","Tai khoan dang tam khoa");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            //check card
            user.setCard(new Card());
            CardDAO cardDAO = new CardDAO(); cardDAO.setConnection(adminDAO.getConnection());
            int cardStatus = cardDAO.checkCard(user);
            if(cardStatus==0){
                jsonResp.put("code",700);
                // jsonResp.put("description","Người dùng chưa đang ký thẻ");
                jsonResp.put("description","Nguoi dung chua dang ky the");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            if(cardStatus==2){
                jsonResp.put("code",700);
                // jsonResp.put("description","Thẻ đang tạm khóa");
                jsonResp.put("description","The dang tam khoa");
                writer.print(jsonResp);
                writer.close();
                adminDAO.close();
                return;
            }
            // get history exit
            HistoryExitDAO hisDAO = new HistoryExitDAO(); hisDAO.setConnection(adminDAO.getConnection());
            ArrayList<HistoryExit> listHis = hisDAO.get(user.getCard().getId());
            JSONObject result = new JSONObject();
            JSONArray listJson = new JSONArray();
            for(HistoryExit his: listHis){
                JSONObject hisJson = new JSONObject();
                hisJson.put("card_id", his.getCardID());
                hisJson.put("time", his.getTimeFormat());
                hisJson.put("image", his.getImage()==null?"":his.getImage());
                hisJson.put("type", his.getType());
                hisJson.put("note", his.getNote()==null?"":his.getNote());
                listJson.put(hisJson);
            }
            result.put("list", listJson);
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
            jsonResp.put("result", result);
            adminDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void addPoint(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            int point = objReq.getInt("point");
            //connect
            UserDAO userDAO = new UserDAO();
            if(!userDAO.connect()){
                jsonResp.put("code",500);
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            // check card
            Card card = new Card(cardID, new User(), "", -1);
            int userStatus = userDAO.check(card);
            if(userStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            if(userStatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đang tạm khóa");
                jsonResp.put("description","The dang tam khoa");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            if(!userDAO.addPoint(cardID, point)){
                jsonResp.put("code",300);
                jsonResp.put("description","Khong them duoc diem");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            jsonResp.put("code",200);
            jsonResp.put("description","Thanh cong");
            userDAO.close();
            
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void subPoint(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            int point = objReq.getInt("point");
            //
            UserDAO userDAO = new UserDAO();
            if(!userDAO.connect()){
                jsonResp.put("code",500);
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            // check card
            Card card = new Card(cardID, new User(), "", -1);
            int userStatus = userDAO.check(card);
            if(userStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            if(userStatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đang tạm khóa");
                jsonResp.put("description","The dang tam khoa");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            //point
            int pointStatus = userDAO.subPoint(cardID, point);
            if(pointStatus==0){
                jsonResp.put("code",300);
                jsonResp.put("description","Khong them duoc diem");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            if(pointStatus==0){
                jsonResp.put("code",400);
                jsonResp.put("description","Diem tru lon hon diem hien co");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            jsonResp.put("code",200);
            jsonResp.put("description","Thanh cong");
            userDAO.close();
            
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void Login(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String userName = objReq.getString("user_name").replaceAll("\\s+", "");
            String passWord = objReq.getString("pass_word").replaceAll("\\s+", "");
            // check connect DB
            UserDAO userDAO = new UserDAO();
            if(!userDAO.connect()){
                jsonResp.put("code",500);
                jsonResp.put("description","Không kết nối được CSDL");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // check login
            User user = new User();
            user.setUsername(userName);
            user.setPassword(passWord);
            if(!userDAO.login(user)){
                jsonResp.put("code",500);
                jsonResp.put("description","Tài khoản hoặc mật khẩu không đúng");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            if(user.getActive()<=0){
                jsonResp.put("code",300);
                jsonResp.put("description","Tài khoản đang tạm khóa");
                writer.print(jsonResp);
                writer.close();
                userDAO.close();
                return;
            }
            UserLogin newUser = new UserLogin(user, generateSession(), new Date().getTime()+INIT.TIME);
            INIT.MEMBER_LOGINS.add(newUser);
            jsonResp.put("code",200);
            jsonResp.put("description","Thành công");
            jsonResp.put("session",newUser.getSession());
            userDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }
    private String generateSession(){
        UUID session = UUID.randomUUID();
        return session.toString();
    }

    private void Logout(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            String session = objReq.getString("session");
            // check session
            UserLogin user = new UserLogin(new User(), session, -1);
            int sessionStatus = SessionF.checkSession(user);
            if(sessionStatus==0){
                jsonResp.put("code",300);
                jsonResp.put("description","Người dùng chưa đăng nhập");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            INIT.MEMBER_LOGINS.remove(user);
            jsonResp.put("code",200);
            jsonResp.put("description","Thành công");
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }
}