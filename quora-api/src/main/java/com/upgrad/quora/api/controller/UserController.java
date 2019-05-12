package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;
@RestController
public class UserController {
    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    private AuthenticationService authentication;

    @RequestMapping(method = RequestMethod.POST,path = "/user/signup",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setDob(signupUserRequest.getDob());
        userEntity.setRole("nonadmin");
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setSalt("1234abc");
        userEntity.setUserName(signupUserRequest.getUserName());

        UserEntity createdUser = signupBusinessService.signup(userEntity);

        final SignupUserResponse userResponse = new SignupUserResponse().id(createdUser.getUuid()).status("REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);


    }

    @RequestMapping(method = RequestMethod.POST,path = "/user/signin",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> login(final String authorization) throws AuthenticationFailedException {
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");


        final UserAuthTokenEntity userAuthTokenEntity = authentication.authenticate(decodedArray[0], decodedArray[1]);
        UserEntity user = userAuthTokenEntity.getUser();
        SigninResponse signinResponse = new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthTokenEntity.getAccessToken());

        return new ResponseEntity<SigninResponse>(signinResponse,headers, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST,path="/user/signout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(String authorization) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthToken = authentication.getUserAuthToken(authorization);

        SignoutResponse signoutResponse = new SignoutResponse().id(userAuthToken.getUuid()).message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.ACCEPTED);


    }
}
