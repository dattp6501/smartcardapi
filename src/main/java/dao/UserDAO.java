package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Card;
import model.User;

public class UserDAO extends DAO{
    public UserDAO() {
    }
    
    public int add(User user) throws SQLException{
        /**
         * 0: khong them duoc
         * 1: them thanh cong
         * 2: da duoc dang ki
         */
        // check card
        CardDAO cardDAO = new CardDAO();
        cardDAO.setConnection(connection);
        int cardStatus = cardDAO.check(user.getCard());
        if(cardStatus!=0){// da dang ki
            return 2;
        }
        //add user
        connection.setAutoCommit(false);
        String insert = "INSERT INTO users(full_name,user_name,pass_word,point,active) VALUES(?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getFullName());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setLong(4, user.getPoint());
        ps.setInt(5, user.getActive());
        ps.executeUpdate();
        ResultSet res = ps.getGeneratedKeys();
        if(!res.next()){// khong them duoc
            connection.rollback();
            res.close();
            return 0;
        }
        user.setId(res.getInt(1));
        res.close();
        //add card
        Card card = user.getCard();
        if(!cardDAO.add(card)){
            connection.rollback();
            return 0;
        }
        connection.setAutoCommit(true);
        return 1;//thanh cong
    }

    public int check(Card card) throws SQLException{
        /*
         * 0: chua dang ki
         * 1: da dang ki
         * 2: tam khoa
         */
        // check card
        CardDAO cardDAO = new CardDAO();
        cardDAO.setConnection(connection);
        int cardStatus = cardDAO.check(card);
        if(cardStatus!=1){// tam khoa hoac chua dang ki
            return cardStatus;
        }
        // check user
        User user = card.getUser();
        user.setCard(card);
        check(user);
        return 1;
    }
    public int check(User user) throws SQLException{
        /*
         * 0: chua dang ki
         * 1: da dang ki va dang hoat dong
         * 2: tam khoa
         */
        String select = "SELECT * FROM users WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setInt(1, user.getId());
        ResultSet res = ps.executeQuery();
        if(!res.next()){// chua dang ki
            res.close();
            return 0;
        }
        user.setFullName(res.getString("full_name"));
        user.setUsername(res.getString("user_name"));
        user.setPassword(res.getString("pass_word"));
        user.setPoint(res.getLong("point"));
        user.setActive(res.getInt("active"));
        return user.getActive()==1?1:2;
    }

    public boolean login(User user) throws SQLException{
        boolean ok = false;
        String select = "select * from users where BINARY user_name=? and binary pass_word=?";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ResultSet res = ps.executeQuery();
        if(res.next()){
            user.setId(res.getInt("id"));
            user.setFullName(res.getString("full_name"));
            user.setPoint(res.getInt("point"));
            user.setActive(res.getInt("active"));
            ok = true;
        }
        CardDAO cardDAO = new CardDAO(); cardDAO.setConnection(connection);
        int cardStatus = cardDAO.checkCard(user);
        if(cardStatus == 0 || cardStatus == 2){
            ok = false;
        }
        res.close();
        return ok;
    }

    public boolean addPoint(String cardID, int point) throws SQLException{
        Card card = new Card(cardID, new User(), "", -1);
        check(card);
        boolean ok = false;
        String update = "update users set point=? where id=?";
        PreparedStatement ps = connection.prepareStatement(update);
        ps.setLong(1, card.getUser().getPoint()+point);
        ps.setInt(2, card.getUser().getId());
        ok = ps.executeUpdate()>0;
        return ok;
    }

    public int subPoint(String cardID, int point) throws SQLException{
        /**
         * 0: khong thanh cong
         * 1: thanh cong
         * 2: tru qua diem
         */
        Card card = new Card(cardID, new User(), "", -1);
        check(card);
        if(card.getUser().getPoint()<point){
            return 2;
        }
        String update = "update users set point=? where id=?";
        PreparedStatement ps = connection.prepareStatement(update);
        ps.setLong(1, card.getUser().getPoint()-point);
        ps.setInt(2, card.getUser().getId());
        if(ps.executeUpdate()<=0){
            return 0;
        }
        return 1;
    }

    public boolean updateProfile(User user) throws SQLException{
        String update = "UPDATE users set full_name=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(update);
        ps.setString(1, user.getFullName());
        ps.setInt(2, user.getId());
        return ps.executeUpdate()>0;
    }

    public boolean updateAccount(User user) throws SQLException{
        String update = "UPDATE users set user_name=?,pass_word=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(update);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setInt(3, user.getId());
        return ps.executeUpdate()>0;
    }
}