package com.hiperusera.fichajes.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@JmixEntity
@Table(name = "FICH_TIPO_TIENDA")
@Entity(name = "fich_TipoTienda")
public class TipoTienda {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "TIENDA")
    private String tienda;

    @Column(name = "NOMBRE_TIENDA")
    private String nombreTienda;

    @Column(name = "TIPO_TIENDA")
    private String tipoTienda;

    public TiendasEnum getTipoTienda() {
        return tipoTienda == null ? null : TiendasEnum.fromId(tipoTienda);
    }

    public void setTipoTienda(TiendasEnum tipoTienda) {
        this.tipoTienda = tipoTienda == null ? null : tipoTienda.getId();
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}