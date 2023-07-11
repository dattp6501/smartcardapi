package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Admin;

public class AdminDAO extends DAO{
    public AdminDAO(){
        super();
    }

    public boolean check(Admin admin) throws SQLException{
        boolean ok = false;
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM admin WHERE active=1 and BINARY user_name=? and BINARY pass_word=?");
        ps.setString(1, admin.getUserName());
        ps.setString(2, admin.getPassword());
        ResultSet res = ps.executeQuery();
        if(res.next()){
            ok = true;
        }
        res.close();
        return ok;
    }
}