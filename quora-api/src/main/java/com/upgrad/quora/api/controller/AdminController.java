package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.DELETE,path = "/admin/user/{userId}",produces =MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") String userId,@RequestParam("authorization") String accessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userEntity = adminService.deleteUser(userId,accessToken);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(userEntity.getUuid()).status("USER SUCCESSFULLY DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.ACCEPTED);

    }
}
