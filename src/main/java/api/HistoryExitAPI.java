package api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dao.AdminDAO;
import dao.CardDAO;
import dao.HistoryExitDAO;
import global.INIT;
import model.Admin;
import model.Card;
import model.HistoryExit;
import utils.JsonCustom;

@WebServlet(urlPatterns = {"/history_exit/in","/history_exit/out"})
public class HistoryExitAPI extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        String url = req.getRequestURI();
        String host = INIT.HOST;
        if(url.equals(host+"/history_exit/in")){
            in(req,resp);
        }else if(url.equals(host+"/history_exit/out")){
            out(req,resp);
        }
    }

    private void in(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            // String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            // String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            String noteHis = null;
            String imageHis = null;
            try {noteHis = objReq.getString("his_note");} catch (Exception e){}
            try {imageHis = objReq.getString("his_image");} catch (Exception e){}
            // check connect DB
            AdminDAO adminDAO = new AdminDAO();
            if(!adminDAO.connect()){
                jsonResp.put("code",500);
                // jsonResp.put("description","Không kết nối được CSDL");
                jsonResp.put("description","Khong the ket noi CSDL");
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
            //     adminDAO.close();
            //     return;
            // }
            // check card
            CardDAO cardDAO = new CardDAO(); cardDAO.setConnection(adminDAO.getConnection());
            Card card = new Card(cardID, null, null, -1);
            int cardStatus = cardDAO.check(card);
            if(cardStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                cardDAO.close();
                return;
            }
            if(cardStatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đã được khóa");
                jsonResp.put("description","The da duoc khoa");
                writer.print(jsonResp);
                writer.close();
                cardDAO.close();
                return;
            }
            //in
            HistoryExitDAO hisDAO = new HistoryExitDAO(); hisDAO.setConnection(cardDAO.getConnection());
            HistoryExit his = new HistoryExit(cardID, imageHis, noteHis, 1, new Date());
            int inStatus = hisDAO.in(his);
            if(inStatus==2){
                jsonResp.put("code",400);
                // jsonResp.put("description","Thẻ đã được sử dụng để vào");
                jsonResp.put("description","The da duoc su dung de vao");
                writer.print(jsonResp);
                writer.close();
                hisDAO.close();
                return;
            }
            if(inStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Không vào được");
                jsonResp.put("description","Khong khoa duoc");
                writer.print(jsonResp);
                writer.close();
                hisDAO.close();
                return;
            }
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
            hisDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }

    private void out(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter writer = resp.getWriter();
        JSONObject jsonResp = new JSONObject();
        try {
            JSONObject objReq = JsonCustom.toJsonObject(req.getReader());
            System.out.println("REQUEST DATA: " + objReq.toString());
            // String userNameA = objReq.getString("a_user_name").replaceAll("\\s+", "");
            // String passWordA = objReq.getString("a_pass_word").replaceAll("\\s+", "");
            String cardID = objReq.getString("card_id").replaceAll("\\s+", "");
            String noteHis = null;
            String imageHis = null;
            try {noteHis = objReq.getString("his_note");} catch (Exception e){}
            try {imageHis = objReq.getString("his_image");} catch (Exception e){}
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
            //     adminDAO.close();
            //     return;
            // }
            // check card
            CardDAO cardDAO = new CardDAO(); cardDAO.setConnection(adminDAO.getConnection());
            Card card = new Card(cardID, null, null, -1);
            int cardStatus = cardDAO.check(card);
            if(cardStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ chưa được đăng ký");
                jsonResp.put("description","The chua duoc dang ky");
                writer.print(jsonResp);
                writer.close();
                cardDAO.close();
                return;
            }
            if(cardStatus==2){
                jsonResp.put("code",300);
                // jsonResp.put("description","Thẻ đã được khóa");
                jsonResp.put("description","The da duoc khoa");
                writer.print(jsonResp);
                writer.close();
                cardDAO.close();
                return;
            }
            //out
            HistoryExitDAO hisDAO = new HistoryExitDAO(); hisDAO.setConnection(cardDAO.getConnection());
            HistoryExit his = new HistoryExit(cardID, imageHis, noteHis, 1, new Date());
            int inStatus = hisDAO.out(his);
            if(inStatus==2){
                jsonResp.put("code",400);
                // jsonResp.put("description","Thẻ chưa sử dụng để vào");
                jsonResp.put("description","The chua su dung de vao");
                writer.print(jsonResp);
                writer.close();
                hisDAO.close();
                return;
            }
            if(inStatus==0){
                jsonResp.put("code",300);
                // jsonResp.put("description","Không ra được");
                jsonResp.put("description","Khong ra duoc");
                writer.print(jsonResp);
                writer.close();
                hisDAO.close();
                return;
            }
            jsonResp.put("code",200);
            // jsonResp.put("description","Thành công");
            jsonResp.put("description","Thanh cong");
            hisDAO.close();
        } catch (Exception e) {
            jsonResp.put("code",300);
            jsonResp.put("description",e.getMessage());
        }
        writer.print(jsonResp);
        writer.close();
    }
}
