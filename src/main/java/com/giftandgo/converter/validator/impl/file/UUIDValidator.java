package com.giftandgo.converter.validator.impl.file;

import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class UUIDValidator implements BiPredicate<String[], Integer> {

    public static final UUIDValidator INSTANCE = new UUIDValidator();

    private UUIDValidator() {
    }

    @Override
    public boolean test(String[] content, Integer associatedColumn) {
            try {
                UUID.fromString(content[associatedColumn]);
                return true;
            } catch (Exception exception) {
                return false;
            }
        }
}
