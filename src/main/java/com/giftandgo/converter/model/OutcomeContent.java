package com.giftandgo.converter.model;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

public record OutcomeContent(@NonNull @NotEmpty String name,
                             String transport,
                             double topSpeed) {
}
