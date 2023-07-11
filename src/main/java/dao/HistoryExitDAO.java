package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.HistoryExit;

public class HistoryExitDAO extends DAO{
    public HistoryExitDAO(){super();}
    public int in(HistoryExit his) throws SQLException{
        // check
        String select = "select * from history_exit where BINARY card_id=? order by time_io desc";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setString(1, his.getCardID());
        ResultSet res = ps.executeQuery();
        if(res.next()){
            if(res.getInt("type_io")==1){
                return 2;
            }
        }
        // insert
        connection.setAutoCommit(false);
        String insert = "insert into history_exit(card_id,time_io,type_io,image_io,note) values(?,?,?,?,?)";
        ps = connection.prepareStatement(insert);
        ps.setString(1, his.getCardID());
        ps.setString(2, his.getTimeFormat());
        ps.setInt(3, 1);
        ps.setBytes(4, his.getImageBytes());
        ps.setString(5, his.getNote());
        if(ps.executeUpdate()<=0){
            connection.rollback();
            return 0;
        }
        connection.setAutoCommit(true);
        return 1;
    }

    public int out(HistoryExit his) throws SQLException{
        // check
        String select = "select * from history_exit where BINARY card_id=? order by time_io desc";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setString(1, his.getCardID());
        ResultSet res = ps.executeQuery();
        if(res.next()){
            if(res.getInt("type_io")==0){
                return 2;
            }
        }
        // insert
        connection.setAutoCommit(false);
        String insert = "insert into history_exit(card_id,time_io,type_io,image_io,note) values(?,?,?,?,?)";
        ps = connection.prepareStatement(insert);
        ps.setString(1, his.getCardID());
        ps.setString(2, his.getTimeFormat());
        ps.setInt(3, 0);
        ps.setBytes(4, his.getImageBytes());
        ps.setString(5, his.getNote());
        if(ps.executeUpdate()<=0){
            connection.rollback();
            return 0;
        }
        connection.setAutoCommit(true);
        return 1;
    }

    public ArrayList<HistoryExit> get(String cardID) throws SQLException, ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<HistoryExit> list = new ArrayList<>();
        String select = "select * from history_exit where BINARY card_id=?";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setString(1, cardID);
        ResultSet res = ps.executeQuery();
        while(res.next()){
            Date time = format.parse(res.getString("time_io"));
            String image = res.getString("image_io");
            int type = res.getInt("type_io");
            String note = res.getString("note");
            list.add(new HistoryExit(cardID, image, note, type, time));
        }
        return list;
    }
}