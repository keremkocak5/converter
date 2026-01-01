package com.giftandgo.converter.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

public record TransportOutcomeContent(@Valid @NonNull @NotEmpty String name,
                                      @Valid @NonNull @NotEmpty String transport,
                                      double topSpeed) {
}
