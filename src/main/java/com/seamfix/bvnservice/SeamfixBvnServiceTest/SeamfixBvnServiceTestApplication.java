package com.seamfix.bvnservice.SeamfixBvnServiceTest;

import com.seamfix.bvnservice.SeamfixBvnServiceTest.algorithm.FraudSuspiciousNotifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableMongoRepositories
public class SeamfixBvnServiceTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeamfixBvnServiceTestApplication.class, args);
		/** I am ASSUMING the necessary commandline READ AND scanners operations and  have been done to read
		 * the expenses, n and d variables as stated in the number 3 question.
		 * These tests have also been asserted in test module under spring test package of this application.
		 */
		List<Integer> expenses = Arrays.asList(2,3,4,2,3,6,8,4,5);
		int n = 9;
		int d = 5;
		FraudSuspiciousNotifications fraudSuspiciousNotifications = new FraudSuspiciousNotifications();
		log.info("Number of Fraud Notifications=={}", fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));

		d = 0;
		log.info("Number of Fraud Notifications=={}", fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
		expenses = Arrays.asList(2,3,4,5,9,8,3);
		n = 7;
		d = 4;
		log.info("Number of Fraud Notifications=={}", fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
		// n and d is the same number
		n = 7;
		d = 7;
		log.info("Number of Fraud Notifications=={}", fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));
		expenses = Arrays.asList();
		n = 0;
		d = 8;
		log.info("Number of Fraud Notifications=={}", fraudSuspiciousNotifications.countDetectedAndSentFraudSuspiciousNotifications(expenses,n,d));

	}

}
