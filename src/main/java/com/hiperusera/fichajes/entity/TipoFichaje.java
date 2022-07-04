package com.hiperusera.fichajes.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum TipoFichaje implements EnumClass<String> {

    ENTRADA("ENTRADA"),
    INICIO_PAUSA("INICIO PAUSA"),
    FIN_PAUSA("FIN PAUSA"),
    SALIDA("SALIDA"),
    SALIDA_JUSTIFICADA("SALIDA JUSTIFICADA"),
    ENTRADA_JUSTIFICADA("ENTRADA JUSTIFICADA"),
    SALIDA_SIN_JUSTIFICAR("SALIDA SIN JUSTIFICAR"),
    ENTRADA_SIN_JUSTIFICAR("ENTRADA SIN JUSTIFICAR");

    private String id;

    TipoFichaje(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TipoFichaje fromId(String id) {
        for (TipoFichaje at : TipoFichaje.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}