package com.giftandgo.converter.service;

import com.giftandgo.converter.model.IpDetails;

import java.util.Optional;

public interface IpTraceable {

    Optional<IpDetails> getIpDetails(String ip);

}
