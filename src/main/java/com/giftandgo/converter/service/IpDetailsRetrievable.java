package com.giftandgo.converter.service;

import com.giftandgo.converter.model.IpDetails;
import org.springframework.retry.annotation.Retryable;

import java.util.Optional;

public interface IpDetailsRetrievable {

    Optional<IpDetails> getIpDetails(String ip);

}
