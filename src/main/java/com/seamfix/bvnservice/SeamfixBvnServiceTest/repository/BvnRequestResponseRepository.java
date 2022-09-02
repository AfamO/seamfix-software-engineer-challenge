package com.seamfix.bvnservice.SeamfixBvnServiceTest.repository;

import com.seamfix.bvnservice.SeamfixBvnServiceTest.model.BvnRequestResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BvnRequestResponseRepository extends MongoRepository<BvnRequestResponse,String> {

}
