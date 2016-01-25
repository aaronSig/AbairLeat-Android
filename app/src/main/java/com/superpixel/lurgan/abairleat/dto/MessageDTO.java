package com.superpixel.lurgan.abairleat.dto;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Martin on 1/25/16.
 */
public class MessageDTO extends FirebaseDTO {
    private String author;
    private Date date;
    private String text;

    public MessageDTO() {}

    public MessageDTO(String author, Date date, String text) {
        this.author = author;
        this.date = date;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "author='" + author + '\'' +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("author", getAuthor());
        map.put("date", formatDate(getDate()));
        map.put("text", getText());

        return map;
    }
}
