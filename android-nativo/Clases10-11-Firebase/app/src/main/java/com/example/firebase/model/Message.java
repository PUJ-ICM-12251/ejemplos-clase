package com.example.firebase.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    String uuid;
    String text;
    String userId;
    Date timestamp;

    public Message(String uuid, String text, String userId, Date timestamp) {
        this.uuid = uuid;
        this.text = text;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public Message() { }
}
