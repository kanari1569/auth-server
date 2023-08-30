package com.mpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.fasterxml.jackson.databind.ObjectMapper;

@AllArgsConstructor
@Builder
@Getter
public class UserResponse {
    private String status;
    private String token;
    private String email;
    private String platform;

    public String toJson(){
        String userResponseJson;
        try {
            userResponseJson = new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            // TODO: handle exception
            userResponseJson = "";
        }
        return userResponseJson;
    }
}
