package com.mpt.authservice.domain;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user")
@Entity
public class User {
    @Id
    @Column(name = "userID")
    private String userID;
    @Column(name = "email")
    private String email;
    @Column(name = "platform")
    private String platform;

    @Builder
    public User(String userID, String email, String platform ) {
        this.userID = userID;
        this.email = email;
        this.platform = platform;
    }

    public Map<String,Object> getHashMap(){
        Map<String,Object> claims = new HashMap<>();

        claims.put("userID",userID);
        claims.put("email",email);
        claims.put("platform", platform);

        return claims;
    }
}
