package com.example.cryptochat;

public interface UserEvents {
    void UserCONNECED(long userID, String userName, int totalCount);
    void UserDISCONNECED(int totalCount);
}
