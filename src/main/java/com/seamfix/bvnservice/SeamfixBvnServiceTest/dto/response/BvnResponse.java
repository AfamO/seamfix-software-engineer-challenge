package com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BvnResponse {
    private String message ="Success";
    private String basicDetail;
    private String imageDetail;
    private int code = 00;
    private String bvn;
}
