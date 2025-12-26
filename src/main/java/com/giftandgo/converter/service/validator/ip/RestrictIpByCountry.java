package com.giftandgo.converter.service.validator.ip;

import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.IpRestrictable;
import org.springframework.stereotype.Service;

import static com.giftandgo.converter.util.Constants.RESTRICTED_COUNTRIES;

@Service
public class RestrictIpByCountry implements IpRestrictable {

    @Override
    public boolean isRestricted(IpDetails ipDetails) {
        return RESTRICTED_COUNTRIES.contains(ipDetails.country());
    }

}
