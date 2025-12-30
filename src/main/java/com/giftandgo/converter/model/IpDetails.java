package com.giftandgo.converter.model;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

public record IpDetails(@NonNull @NotEmpty String countryCode,
                        @NonNull @NotEmpty String isp) {
}
