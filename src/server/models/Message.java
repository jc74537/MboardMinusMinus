package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Date;
public class Message {
    public static ArrayList<Message> messages = new ArrayList<Message>();
    private int id;
    private String messageText;
    private String author;
    private String date;

    public Message(int id, String messageText, String date, String author) {
        this.id = id;
        this.messageText = messageText;
        this.author = author;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "ID: " + id +
                " Author: " + author +
                " Message: " + messageText +
                " Date: " + date +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void delMessage() {
        this.messageText = "[Deleted]";
    }
    public static int nextID() {
        int nextID = 0;
        for (Message m: messages){
            if (m.getId() > nextID) { nextID = m.getId();}
        }
        return nextID + 1;
    }
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("id", getId());
        j.put("text", getMessageText());
        j.put("postDate", getDate().toString());
        j.put("author", getAuthor());
        return j;
    }

}
