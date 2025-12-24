package com.giftandgo.converter.service.impl;

import com.giftandgo.converter.enums.ErrorCode;
import com.giftandgo.converter.exception.ConverterRuntimeException;
import com.giftandgo.converter.model.IpDetails;
import com.giftandgo.converter.service.IpRestrictable;
import org.springframework.stereotype.Service;

import static com.giftandgo.converter.util.Constants.RESTRICTED_ISPS;

@Service
public class RestrictIpByIsp implements IpRestrictable {

    @Override
    public void runRestrictionRule(IpDetails ipDetails) {
        if (RESTRICTED_ISPS.contains(ipDetails.isp())) {
            throw new ConverterRuntimeException(ErrorCode.RESTRICTED_ISP);
        }
    }

}
