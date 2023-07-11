package api;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import dao.AdminDAO;
import dao.CardDAO;
import global.INIT;
import model.Admin;
import model.Card;
import utils.JsonCustom;

@WebServlet(urlPatterns = {"/card/lock_card","/card/unlock_card","/test"})
public class CardAPI extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String url = req.getRequestURI();
        String host = INIT.HOST;
        if(url.equals(host+"/card/lock_card")){
            lockCard(req,resp);
        }else if(url.equals(host+"/card/unlock_card")){
            unlockCard(req,resp);
        }else if(url.equals(host+"/test")){
            test(req,resp);
        }
    }

    private void unlockCard(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        JSONObject objReq = null;
        try {
            objReq = JsonCustom.toJsonObject(req.getReader());
        } catch (Exception e) {
            jsonResp.put("code",300);
            // jsonResp.put("description","Không có dữ liệu gửi đi");
            jsonResp.put("description","Khong co du lieu gui di");
            writer.print(jsonResp);
            writer.close();
            return;
        }
        try {
            System.out.println("REQUEST DATA: " + objReq.toString());
            // String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            // String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            // check connect DB
            AdminDAO adminDAO = new AdminDAO();
            if(!adminDAO.connect()){
                jsonResp.put("code",500);
                // jsonResp.put("description","Không kết nối được CSDL");
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // check permission
            // Admin admin = new Admin(-1, "", userNameA, passWordA);
            // if(!adminDAO.check(admin)){
            //     jsonResp.put("code",700);
            //     jsonResp.put("description","không dủ quyền");
            //     writer.print(jsonResp);
            //     writer.close();
            //     return;
            // }
            // lock card
            Card card = new Card(cardID, null, "", -1);
            CardDAO cardDAO = new CardDAO(); cardDAO.setConnection(adminDAO.getConnection());
            int cardtatus = cardDAO.unlock(card);
            cardDAO.close();
            if(cardtatus==3){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // String = "{\"a_user_name\": \"Admin.dattp\",
            //     "a_pass_word": "Admin$123",
            //     "card_id": "A33945AD"
            // }";
            if(cardtatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đã được mở khóa");
                jsonResp.put("description","The da duoc mo");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            if(cardtatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Không mở khóa được");
                jsonResp.put("description","Khong mo khoa duoc");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void lockCard(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        JSONObject objReq = null;
        try {
            objReq = JsonCustom.toJsonObject(req.getReader());
        } catch (Exception e) {
            jsonResp.put("code",300);
            // jsonResp.put("description","Không có dữ liệu gửi đi");
            jsonResp.put("description","Khong co du lieu gui di");
            writer.print(jsonResp);
            writer.close();
            return;
        }
        try {
            System.out.println("REQUEST DATA: " + objReq.toString());
            // String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            // String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            // check connect DB
            AdminDAO adminDAO = new AdminDAO();
            if(!adminDAO.connect()){
                jsonResp.put("code",500);
                // jsonResp.put("description","Không kết nối được CSDL");
                jsonResp.put("description","Khong ket noi duoc CSDL");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            // check permission
            // Admin admin = new Admin(-1, "", userNameA, passWordA);
            // if(!adminDAO.check(admin)){
            //     jsonResp.put("code",700);
            //     jsonResp.put("description","không dủ quyền");
            //     writer.print(jsonResp);
            //     writer.close();
            //     return;
            // }
            // lock card
            Card card = new Card(cardID, null, "", -1);
            CardDAO cardDAO = new CardDAO(); cardDAO.setConnection(adminDAO.getConnection());
            int cardtatus = cardDAO.lock(card); 
            cardDAO.close();
            if(cardtatus==3){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            if(cardtatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đã được khóa");
                jsonResp.put("description","The da duoc khoa");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            if(cardtatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Không khóa được");
                jsonResp.put("description","Khong khoa duoc");
                writer.print(jsonResp);
                writer.close();
                return;
            }
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String url = req.getRequestURI();
        String host = INIT.HOST;
        if(url.equals(host+"/test")){
            test(req,resp);
        }
    }
    private void test(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            jsonResp.put("code",200);
            jsonResp.put("description","Ok chưa em");
            jsonResp.put("data request", objReq);
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }
}