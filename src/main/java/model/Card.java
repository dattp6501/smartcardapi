package model;

public class Card {
    private String id,note;
    private int active;
    private User user;
    public Card(String id, User user, String note, int active) {
        this.id = id;
        this.note = note;
        this.active = active;
        this.user = user;
    }
    public Card() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Card [id=" + id + ", note=" + note + ", active=" + active + ", user=" + user.toString() + "]";
    }
    
}