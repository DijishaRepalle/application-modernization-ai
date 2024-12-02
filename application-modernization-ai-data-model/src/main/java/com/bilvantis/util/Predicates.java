package com.bilvantis.util;

import java.util.function.Predicate;

public class Predicates {
    public static final Predicate<String> isValidPhoneNumber = num -> num.length() == 10 && num.matches("^[6-9][0-9]+$");

}
