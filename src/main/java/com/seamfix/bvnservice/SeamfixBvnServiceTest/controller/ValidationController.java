package com.seamfix.bvnservice.SeamfixBvnServiceTest.controller;

import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.request.BvnRequest;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.response.BvnResponse;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.helper.Constants;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.APP_PATH)
@EnableAsync
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }


    @PostMapping("")
    public ResponseEntity<BvnResponse> createBank(@Validated @RequestBody BvnRequest bvnRequest){
        return new ResponseEntity<>(validationService.validateBvn(bvnRequest), HttpStatus.CREATED);
    }
}
