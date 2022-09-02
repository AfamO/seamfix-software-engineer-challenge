package com.seamfix.bvnservice.SeamfixBvnServiceTest.service;

import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.request.BvnRequest;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.response.BvnResponse;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.helper.Utility;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.model.BvnRequestResponse;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.repository.BvnRequestResponseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class BvnValidationService implements ValidationService,BvnTransactionService {

    private final BvnRequestResponseRepository bvnRequestResponseRepository;
    //Mock Bvn List for testing purposes: This is also deduced from the question. I mean it;s assumed that there are already pre-existing bvn lists
    private List<String> mockSomePreStoredBvnList;

    public BvnValidationService(BvnRequestResponseRepository bvnRequestResponseRepository) {
        this.bvnRequestResponseRepository = bvnRequestResponseRepository;
    }

    @Override
    public BvnResponse validateBvn(BvnRequest bvnRequest) {
        mockSomePreStoredBvnList = Arrays.asList("01234567890","11234567891","22345678902"); //initialize with some pre-existing bvn lists.
        BvnResponse bvnResponse = new BvnResponse(null,null,null,400,"");
        if (bvnRequest.getBvn() == null || bvnRequest.getBvn().equals("")) {
            bvnResponse.setMessage("One or more of your request parameters failed validation. Please retry");
            return bvnResponse;
        }
        else if (bvnRequest.getBvn().length() < 11) {
            bvnResponse.setMessage("The searched BVN is invalid");
            bvnResponse.setCode(02);
            bvnResponse.setBvn(bvnRequest.getBvn());
            return bvnResponse;
        }
        else if (!Utility.isNumeric(bvnRequest.getBvn())) {
            bvnResponse.setMessage("The searched BVN is invalid");
            bvnResponse.setCode(400);
            bvnResponse.setBvn(bvnRequest.getBvn());
            return bvnResponse;
        }
        else if (!mockSomePreStoredBvnList.contains(bvnRequest.getBvn())){
            bvnResponse.setMessage("The searched BVN does not exist");
            bvnResponse.setCode(01);
            bvnResponse.setBvn(bvnRequest.getBvn());
            return bvnResponse;
        }
        else {
           String basicDetail  = Base64.getEncoder().encodeToString(bvnRequest.getBvn().getBytes());
           String imageDetail = new String(Base64.getDecoder().decode(basicDetail));
           bvnResponse = new BvnResponse("Success",basicDetail,imageDetail,00,bvnRequest.getBvn());
           BvnRequestResponse bvnRequestResponse = new BvnRequestResponse(bvnRequest.getBvn(),bvnRequest,bvnResponse);
           bvnRequestResponse = this.saveBvnInfo(bvnRequestResponse);
           log.info("Successfully Saved BvnInfo ::{}",bvnRequestResponse);
           return bvnResponse;

        }
    }

    @Async
    @Override
    public BvnRequestResponse saveBvnInfo(BvnRequestResponse bvnRequestResponse) {
        return bvnRequestResponseRepository.save(bvnRequestResponse);
    }
}
