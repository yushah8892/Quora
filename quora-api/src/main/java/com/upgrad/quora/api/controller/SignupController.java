package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class SignupController {
    private final Logger log = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private SignupBusinessService signupBusinessService;

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
      log.debug("userName:" + userEntity.getUserName());

        UserEntity createdUser = signupBusinessService.signup(userEntity);

        final SignupUserResponse userResponse = new SignupUserResponse().id(createdUser.getUuid()).status("REGISTERED");

        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);


    }

}
