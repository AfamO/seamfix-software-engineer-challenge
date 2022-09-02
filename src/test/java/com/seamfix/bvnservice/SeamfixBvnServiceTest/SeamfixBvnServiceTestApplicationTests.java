package com.seamfix.bvnservice.SeamfixBvnServiceTest;

import com.google.gson.Gson;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.algorithm.FraudSuspiciousNotifications;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.request.BvnRequest;
import com.seamfix.bvnservice.SeamfixBvnServiceTest.dto.response.BvnResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SeamfixBvnServiceTestApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	private String bvn; //intentionally null
	private HttpEntity<BvnRequest> httpEntity;
	private HttpHeaders headers;

	//Assuming the webservice exists here
	private  static String WEBSERVICEBASEURL = "http://localhost:";

	@Test
	void contextLoads() {
	}

	@BeforeAll
	public static void setUpRequestParams() {
		// we need to create a new headers instance
		HttpHeaders headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		int port = 8080;
		WEBSERVICEBASEURL = "http://localhost:"+ port + "/bvn-service/svalidate/wrapper";

	}

	@Test
	public void validFraudDetectionNotifications()  {
		List<Integer> expenses = Arrays.asList(2,3,4,2,3,6,8,4,5);
		int n = 9;
		int d = 5;
		FraudSuspiciousNotifications fraudSuspiciousNotifications = new FraudSuspiciousNotifications();
		Assertions.assertEquals(2, fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
	}

	@Test
	public void zeroPriorDayFraudDetectionNotifications()  {
		List<Integer> expenses = Arrays.asList(2,3,4,2,3,6,8,4,5);
		int n = 9;
		int d = 0;
		FraudSuspiciousNotifications fraudSuspiciousNotifications = new FraudSuspiciousNotifications();
		Assertions.assertEquals(0, fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
	}
	@Test
	public void differentExpensesFraudDetectionNotifications()  {
		List<Integer> expenses = Arrays.asList(2,3,4,5,9,8,3);
		int n = 7;
		int d = 4;
		FraudSuspiciousNotifications fraudSuspiciousNotifications = new FraudSuspiciousNotifications();
		Assertions.assertEquals(2, fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
	}

	@Test
	public void sameNandDFraudDetectionNotifications()  {
		List<Integer> expenses = Arrays.asList(2,3,4,5,9,8,3);
		int n = 7;
		int d = 7;
		FraudSuspiciousNotifications fraudSuspiciousNotifications = new FraudSuspiciousNotifications();
		Assertions.assertEquals(0, fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
	}


	@Test
	public void validBvnInRequestPayload() throws Exception {
		bvn = "11234567891";
		// Get Bvn Request Object
		BvnRequest bvnRequest = new BvnRequest(bvn);
		// build the request
		httpEntity = new HttpEntity<>(bvnRequest, headers);
		ResponseEntity<BvnResponse> responseEntity = testRestTemplate.postForEntity(WEBSERVICEBASEURL , httpEntity, BvnResponse.class);
		if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.CREATED) {
			BvnResponse bvnResponse = responseEntity.getBody();
			Assertions.assertEquals("Success", bvnResponse.getMessage());
			Assertions.assertEquals(00, bvnResponse.getCode());
			Assertions.assertEquals(bvnRequest.getBvn(), bvnResponse.getBvn());
			Assertions.assertNotNull(bvnResponse.getImageDetail());
			Assertions.assertNotNull(bvnResponse.getBasicDetail());
			Assertions.assertFalse("".equals(bvnResponse.getImageDetail()));
			Assertions.assertFalse("".equals(bvnResponse.getBasicDetail()));
		}

	}

	@Test
	public void emptyBvnInRequestPayload() throws Exception {
		bvn = "";
		// Get Bvn Request Object
		BvnRequest bvnRequest = new BvnRequest(bvn);
		// build the request
		httpEntity = new HttpEntity<>(bvnRequest, headers);
		ResponseEntity<BvnResponse> responseEntity = testRestTemplate.postForEntity(WEBSERVICEBASEURL, httpEntity, BvnResponse.class);
		if (responseEntity != null) {
			BvnResponse bvnResponse = responseEntity.getBody();
			Assertions.assertEquals("One or more of your request parameters failed validation. Please retry", bvnResponse.getMessage());
			Assertions.assertEquals(400, bvnResponse.getCode());
		}

	}

	@Test
	public void invalidBvnInRequestPayload() throws Exception {
		bvn = "09876543210";
		// Get Bvn Request Object
		BvnRequest bvnRequest = new BvnRequest(bvn);
		// build the request
		httpEntity = new HttpEntity<>(bvnRequest, headers);
		ResponseEntity<BvnResponse> responseEntity = testRestTemplate.postForEntity(WEBSERVICEBASEURL, httpEntity, BvnResponse.class);
		if (responseEntity != null) {
			BvnResponse bvnResponse = responseEntity.getBody();
			Assertions.assertEquals("The searched BVN does not exist", bvnResponse.getMessage());
			Assertions.assertEquals(01, bvnResponse.getCode());
			Assertions.assertEquals(bvn,bvnResponse.getBvn());
		}

	}

	@Test
	public void invalidBvnLessThan11Digits() throws Exception {
		bvn = "0987654321";
		// Get Bvn Request Object
		BvnRequest bvnRequest = new BvnRequest(bvn);
		// build the request
		httpEntity = new HttpEntity<>(bvnRequest, headers);
		ResponseEntity<BvnResponse> responseEntity = testRestTemplate.postForEntity(WEBSERVICEBASEURL, httpEntity, BvnResponse.class);
		if (responseEntity != null) {
			BvnResponse bvnResponse = responseEntity.getBody();
			Assertions.assertEquals("The searched BVN is invalid", bvnResponse.getMessage());
			Assertions.assertEquals(02, bvnResponse.getCode());
			Assertions.assertEquals(bvnRequest.getBvn(),bvnResponse.getBvn());
		}

	}

	@Test
	public void invalidBvnContainsNonDigits() throws Exception {
		bvn = "098n65432Mk"; // invalid bvn test case: it contains non digits-letters
		// Get Bvn Request Object
		BvnRequest bvnRequest = new BvnRequest(bvn);
		// build the request
		httpEntity = new HttpEntity<>(bvnRequest, headers);
		ResponseEntity<BvnResponse> responseEntity = testRestTemplate.postForEntity(WEBSERVICEBASEURL, httpEntity, BvnResponse.class);
		if (responseEntity != null) {
			BvnResponse bvnResponse = responseEntity.getBody();
			Assertions.assertEquals("The searched BVN is invalid", bvnResponse.getMessage());
			Assertions.assertEquals(400, bvnResponse.getCode());
			Assertions.assertEquals(bvnRequest.getBvn(),bvnResponse.getBvn());
		}

	}

	@Test
	public void validBvnValidBasic64EncodedString() throws Exception {
		bvn = "01234567890";  //valid Bvn test case, but tests for valid basic 64 encoded bvn string
		// Get Bvn Request Object
		BvnRequest bvnRequest = new BvnRequest(bvn);
		// build the request
		httpEntity = new HttpEntity<>(bvnRequest, headers);
		ResponseEntity<BvnResponse> responseEntity = testRestTemplate.postForEntity(WEBSERVICEBASEURL, httpEntity, BvnResponse.class);
		if (responseEntity != null) {
			BvnResponse bvnResponse = responseEntity.getBody();
			log.info("Bvn bvnResponse with Basic Detail =={}",bvnResponse);
			Assertions.assertEquals(Base64.getEncoder().encodeToString(bvnRequest.getBvn().getBytes()),bvnResponse.getBasicDetail());
		}

	}
}
