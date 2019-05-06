package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(method = RequestMethod.GET,path = "/userprofile/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable("userId") String userId, @RequestParam("authentication") String authentication) throws AuthorizationFailedException, UserNotFoundException {

        UserEntity userDetails = userProfileService.getUserDetails(userId, authentication);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().userName(userDetails.getUserName())
                .aboutMe(userDetails.getAboutMe()).contactNumber(userDetails.getContactNumber()).country(userDetails.getCountry())
                .dob(userDetails.getDob()).firstName(userDetails.getFirstName()).lastName(userDetails.getLastName()).emailAddress(userDetails.getEmail());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
