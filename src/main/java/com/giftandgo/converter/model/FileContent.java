package com.giftandgo.converter.model;

import java.util.UUID;

public record FileContent(UUID uuid, String id, String name, String likes, String transport, Double averageSpeed,
                          Double topSpeed) {
}
