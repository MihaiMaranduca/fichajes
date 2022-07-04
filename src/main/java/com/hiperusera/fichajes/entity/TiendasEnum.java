package com.hiperusera.fichajes.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum TiendasEnum implements EnumClass<String> {

    SUPER("SUPER"),
    CASH("CASH"),
    TUCASH("TUCASH"),
    PERFUMERIA("PERFUMERIA");

    private String id;

    TiendasEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TiendasEnum fromId(String id) {
        for (TiendasEnum at : TiendasEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}