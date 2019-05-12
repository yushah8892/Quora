package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upgrad.quora.service.exception.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(String userId,String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthTokenEntity(accessToken);
        if(userAuthTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if (userAuthTokenEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out");
        }else if(userAuthTokenEntity.getUser().getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }else{
            UserEntity userDetails = userDao.getUserDetails(userId);
            if(userDetails == null){
                throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
            }else{
                return userDao.deleteUser(userDetails);
            }

        }
    }
}
