package com.seamfix.bvnservice.SeamfixBvnServiceTest.model;

import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.request.BvnRequest;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.response.BvnResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("bvninfo")
public class BvnRequestResponse {
    @Id
    private String id;
    private BvnRequest bvnRequest;
    private BvnResponse bvnResponse;
}
