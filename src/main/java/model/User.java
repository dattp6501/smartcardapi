package model;

public class User {
    private int id,active;
    private long point;
    private String fullName,Username,Password;
    private Card card;
    public User() {
    }
    public User(int id, long point, String fullName, String username, String password, int active, Card card) {
        this.id = id;
        this.point = point;
        this.fullName = fullName;
        Username = username;
        Password = password;
        this.card = card;
        this.active = active;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public long getPoint() {
        return point;
    }
    public void setPoint(long point) {
        this.point = point;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public Card getCard() {
        return card;
    }
    public void setCard(Card card) {
        this.card = card;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", active=" + active + ", point=" + point + ", fullName=" + fullName + ", Username="
                + Username + ", Password=" + Password + "]";
    }
}