package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Card;
import model.User;

public class CardDAO extends DAO{
    public CardDAO() {
    }
    
    public boolean add(Card card) throws SQLException{
        String insert = "INSERT INTO card(id,user_id,active,note) VALUES(?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, card.getId());
        ps.setInt(2, card.getUser().getId());
        ps.setInt(3, card.getActive());
        ps.setString(4, card.getNote());
        return ps.executeUpdate()>=1;
    }

    public int check(Card card) throws SQLException{
        /**
         * 0: chua tao
         * 1: da tao
         * 2: tam khoa
         */
        String select = "SELECT * FROM card where BINARY id=?";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setString(1, card.getId());
        ResultSet res = ps.executeQuery();
        if(!res.next()){
            return 0;
        }
        card.setActive(res.getInt("active"));
        card.setNote(res.getString("note"));
        User user = new User(res.getInt("user_id"), -1, "", "", "",-1, card);
        card.setUser(user);
        if(card.getActive()==0){
            return 2;
        }
        return 1;
    }

    public int checkCard(User user) throws SQLException{
        /**
         * 0: chua tao
         * 1: da tao
         * 2: tam khoa
         */
        String select = "select * from card where user_id=?";
        PreparedStatement ps = connection.prepareStatement(select);
        ps.setInt(1, user.getId());
        ResultSet res = ps.executeQuery();
        if(!res.next()){
            return 0;
        }
        String cardID = res.getString("id");
        String note = res.getString("note");
        int active = res.getInt("active");
        if(user.getCard()==null){
            user.setCard(new Card());
        }
        Card card = user.getCard();
        card.setId(cardID);
        card.setNote(note);
        card.setActive(active);
        card.setUser(user);
        if(card.getActive()==0){
            return 2;
        }
        return 1;
    }

    public int lock(Card card) throws SQLException{
        /**
         * 0: khong khoa duoc
         * 1: thanh cong
         * 2: the dang duoc khoa
         * 3: the chua duoc dang ki
         */
        int cardStatus = check(card);
        if(cardStatus==0){// chua dang ki
            return 3;
        }
        if(cardStatus==2){// dang khoa
            return 2;
        }
        connection.setAutoCommit(false);
        //lock card
        String lock = "UPDATE card SET active=0 WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(lock);
        ps.setString(1, card.getId());
        if(ps.executeUpdate()<=0){// khong khoa duoc
            connection.rollback();
            return 0;
        }
        lock = "UPDATE users SET active=0 WHERE id=?";
        ps = connection.prepareStatement(lock);
        ps.setInt(1, card.getUser().getId());
        if(ps.executeUpdate()<=0){//khong khoa duoc
            connection.rollback();
            return 0;
        }
        connection.setAutoCommit(true);
        return 1;
    }

    public int unlock(Card card) throws SQLException{
        /**
         * 0: khong mo khoa duoc
         * 1: thanh cong
         * 2: the dang duoc mo khoa
         * 3: the chua duoc dang ki
         */
        int cardStatus = check(card);
        if(cardStatus==1){// dang mo khoa
            return 2;
        }
        if(cardStatus==0){// chua dang ki
            return 3;
        }
        connection.setAutoCommit(false);
        //lock card
        String lock = "UPDATE card SET active=1 WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(lock);
        ps.setString(1, card.getId());
        if(ps.executeUpdate()<=0){// khong khoa duoc
            connection.rollback();
            return 0;
        }
        lock = "UPDATE users SET active=1 WHERE id=?";
        ps = connection.prepareStatement(lock);
        ps.setInt(1, card.getUser().getId());
        if(ps.executeUpdate()<=0){//khong khoa duoc
            connection.rollback();
            return 0;
        }
        connection.setAutoCommit(true);
        return 1;
    }
}