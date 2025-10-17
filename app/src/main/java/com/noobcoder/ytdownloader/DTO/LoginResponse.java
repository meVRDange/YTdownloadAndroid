package com.noobcoder.ytdownloader.DTO;

public class LoginResponse {
    private String token; // assuming backend returns JWT in 'token'

    public String getToken(){ return token; }
}