package com.terraforged.fm.transformer;

public enum InjectionPosition {
    BEFORE,
    AFTER,
    HEAD,
    TAIL
    ;

    public static InjectionPosition parse(String name) {
        return InjectionPosition.valueOf(name.toUpperCase());
    }
}
