package com.example.toDoAppBackend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {

    private  String provider;

    private String code;

    private  UserData userData;

    @Getter
    @Setter
    public  static  class  UserData{
        private  String id;
        private  String email;
        private  String name;
        private  String givenName;
        private  String familyName;
        private  String picture;
        private  String locale;
        private  boolean verifiedEmail;
    }
}
