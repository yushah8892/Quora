package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(String userName,String password) throws AuthenticationFailedException {

        UserEntity userEntity = userDao.getUserByUserName(userName);
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001","This username does not exist");
        }

        String encryptedPass = cryptographyProvider.encrypt(password,userEntity.getSalt());

        if(encryptedPass.equals(userEntity.getPassword()) == false){
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }else{
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPass);
            UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
            userAuthToken.setUser(userEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);
            userAuthToken.setUuid(userEntity.getUuid());
            userDao.createAuthToken(userAuthToken);
            return userAuthToken;
        }


    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity getUserAuthToken(String authToken) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(authToken);
        if(userAuthTokenEntity == null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }else{
            userAuthTokenEntity.setLogoutAt(ZonedDateTime.now());
            userDao.updateAuthToken(userAuthTokenEntity);
            return userAuthTokenEntity;
        }
    }
}
