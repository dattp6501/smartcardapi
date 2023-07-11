package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryExit {
    private String cardID,image,note;
    private int type;
    private Date time;
    public HistoryExit() {
    }
    public HistoryExit(String cardID, String image, String note, int type, Date time) {
        this.cardID = cardID;
        this.image = image;
        this.note = note;
        this.type = type;
        this.time = time;
    }
    public String getCardID() {
        return cardID;
    }
    public void setCardID(String cardID) {
        this.cardID = cardID;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public byte[] getImageBytes(){
        return null;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public Date getTime() {
        return time;
    }
    public String getTimeFormat(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }
    public void setTime(Date time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "HistoryExit [cardID=" + cardID + ", image=" + image + ", note=" + note + ", type=" + type + ", time="
                + getTimeFormat() + "]";
    }
}