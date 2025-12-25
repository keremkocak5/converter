package com.giftandgo.converter.service;

import com.giftandgo.converter.model.IpDetails;

public interface IpRestrictable {

    boolean isIpBlocked(IpDetails ipDetails);

}
