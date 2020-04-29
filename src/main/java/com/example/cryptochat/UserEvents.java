package com.example.cryptochat;

public interface UserEvents {
    void UserCONNECED(final String userName, final int totalCount);
    void UserDISCONNECED(final int totalCount);
}
