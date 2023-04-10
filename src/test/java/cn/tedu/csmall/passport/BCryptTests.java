package cn.tedu.csmall.passport;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTests {
    @Test
    public void testEncode(){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String password="123456";

        for (int i = 0; i < 100; i++) {
            String encodePassword=encoder.encode(password);
            System.out.println(encodePassword);
        }
//        $2a$10$sFV3jCWHfTxaaDZovzsjweaRQ4IWxeHPxBIMoAZ71W238xKqSXgEC
//        $2a$10$GUwc8UsY6OAZB9nIC4Mh6.Fy24QPOV69w.SjqezVR4vaaA4XrWRS.
//        $2a$10$7Yt9iwAH1g.ZltyyIOQ5/.pmlqYrFpIi/hyjYovtTUzY/jvPwPRKC
//        $2a$10$gP5STtoTzuIUiemi.iTLk.lgJ2Ux0bazrUJeMMFBU52Pm9sMdFttO
//        $2a$10$Sk9b3ABhSAoaQJk3RIgFVOKjWAq.X6gQxTTh0QxV52xq1CDfEsQeW
//        $2a$10$Io0u7IONhCYlZoAbWqV2JO2nNWB3k6EU5pOBsJtes3H1I8YE4Jdge
//        $2a$10$ArMlktarNoV/7XYhNjNw7umFx0O0SxEtKPotmfCRfFQuCDJpItjFC
    }

    @Test
    public void testMatches(){
        String rawPassword = "123456";
        String encodedPassword = "$2a$10$UjZEWuPg6jz6B74J0qbsEOM81EMXQPDs3s4mLogzjW1wsBwCRhngy";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
