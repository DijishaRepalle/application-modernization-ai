package com.bilvantis.util;

import java.util.function.Predicate;

import static com.bilvantis.constants.CommonConstants.*;

public class Predicates {
    public static final Predicate<String> isValidPhoneNumber = num -> num.length() == TEN && num.matches(PHONE_NUMBER_REGEX);

}