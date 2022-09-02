package com.seamfix.bvnservice.SeamfixBvnServiceTest.service;

import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.request.BvnRequest;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.response.BvnResponse;

public interface ValidationService {

    public BvnResponse validateBvn(BvnRequest bvnRequest);
}
