package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {



    @Autowired
    private UserDao userDao;

    @Autowired
    PasswordCryptographyProvider cryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        //check if user already exists

        UserEntity userByUserName = userDao.getUserByUserName(userEntity.getUserName());
        if(userByUserName != null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
        UserEntity userByEmail = userDao.getUserByEmail(userEntity.getEmail());

        if(userByEmail != null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }
        //encrypt password
        String password = userEntity.getPassword();
        String[] encryptedPassword = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedPassword[0]);
        userEntity.setPassword(encryptedPassword[1]);

        //create user in database
        return userDao.createUser(userEntity);
    }
}
