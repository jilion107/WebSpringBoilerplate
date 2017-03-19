package com.xmnjm.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jilion.chen on 3/18/2017.
 */
@Component
public class JsonWebToken {
    @Autowired
    private Audience audienceEntity;

    public Object getAccessToken(String userName, String password) {
        try {
            if(userName == null || password == null) {
                return null;
            }

            //拼装accessToken
            String accessToken = JwtHelper.createJWT(userName, audienceEntity.getName(),
                    audienceEntity.getExpiresSecond() * 1000, audienceEntity.getBase64Secret());

            //返回accessToken
            AccessToken accessTokenEntity = new AccessToken();
            accessTokenEntity.setAccessToken(accessToken);
            accessTokenEntity.setExpiresIn(audienceEntity.getExpiresSecond());
            accessTokenEntity.setTokenType("restful");
            return accessTokenEntity;

        }
        catch(Exception ex) {
            return null;
        }
    }
}
