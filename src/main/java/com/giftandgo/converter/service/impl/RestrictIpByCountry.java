package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.IpRestrictable;
import org.springframework.stereotype.Service;

import static com.giftandgo.converter.util.Constants.RESTRICTED_COUNTRIES;

@Service
public class RestrictIpByCountry implements IpRestrictable {

    @Override
    public void runRestrictionRule(IpDetails ipDetails) {
        if (RESTRICTED_COUNTRIES.contains(ipDetails.country())) {
            throw new ConverterRuntimeException(ErrorCode.RESTRICTED_COUNTRY);
        }
    }

}
