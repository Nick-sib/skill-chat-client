package com.example.cryptochat.common;

import com.google.gson.Gson;

class Protocol {
    final static int USER_STATUS = 1;
    final static int MESSAGE = 2;
    final static int USER_NAME = 3;

    static class Message {
        private final static int GROUP_CHAT = 1;
        private long sender;
        private long receiver = GROUP_CHAT;
        private String encodedText;

        Message (String encodedText) {
            this.encodedText = encodedText;
        }

        long getSender() {
            return sender;
        }

        /*public Message setSender(long sender) {
            this.sender = sender;
            return this;
        }

        public long getReceiver() {
            return receiver;
        }

        public Message setReceiver(long receiver) {
            this.receiver = receiver;
            return this;
        }*/

        String getEncodedText() {
            return encodedText;
        }

        /*public Message setEncodedText(String encodedText) {
            this.encodedText = encodedText;
            return this;
        }*/
    }

    static class User {
        private long id;
        private String name;

        public User () {}

        long getId() {
            return id;
        }

        /*public User setId(long id) {
            this.id = id;
            return this;
        }*/

        String getName() {
            return name;
        }

        /*public User setName(String name) {
            this.name = name;
            return this;
        }*/
    }

    static class UserStatus {
        private User user;
        private boolean connected;

        User getUser() {
            return user;
        }

        /*public UserStatus setUser(User user) {
            this.user = user;
            return this;
        }*/

        boolean isConnected() {
            return connected;
        }

        /*public UserStatus setConnected(boolean connected) {
            this.connected = connected;
            return this;
        }*/
    }

    static class UserName {
        private String name;

        UserName(String name) {
            this.name = name;
        }

        /*public String getName() {
            return name;
        }

        public UserName setName(String name) {
            this.name = name;
            return this;
        }*/
    }

    static int getType(String json) {
        if (json == null || json.length() == 0) {
            return -1;
        }
        return Integer.parseInt(json.substring(0,1));
    }

    static UserStatus unpackStatus(String json) {
        Gson g = new Gson();
        //Log.d("myLOG", "unpackStatus: " + json);
        return g.fromJson(json.substring(1), UserStatus.class);
    }

    static Message unpackMessage(String json) {
        Gson g = new Gson();
        return g.fromJson(json.substring(1), Message.class);
    }

    static String packMessage(Message m) {
        Gson g = new Gson();
        return MESSAGE + g.toJson(m);
    }

    public static String packName(UserName n) {
        Gson g = new Gson();
        return USER_NAME + g.toJson(n);
    }
}
