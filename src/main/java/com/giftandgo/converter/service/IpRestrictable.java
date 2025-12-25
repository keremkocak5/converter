package com.giftandgo.converter.service;

import com.giftandgo.converter.model.IpDetails;

public interface IpRestrictable {

    boolean isRestricted(IpDetails ipDetails);

}
