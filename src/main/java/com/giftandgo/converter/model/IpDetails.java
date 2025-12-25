package com.giftandgo.converter.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

public record IpDetails(@Valid @NonNull @NotEmpty String country,
                        @Valid @NonNull @NotEmpty String isp) {
}
