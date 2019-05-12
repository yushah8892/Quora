package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.upgrad.quora.service.business.*;

@RestController
public class SignoutController
{

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST,path="/user/signout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse>  signout(final String authorization) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthToken = authenticationService.getUserAuthToken(authorization);

        SignoutResponse signoutResponse = new SignoutResponse().id(userAuthToken.getUuid()).message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.ACCEPTED);


    }
}
