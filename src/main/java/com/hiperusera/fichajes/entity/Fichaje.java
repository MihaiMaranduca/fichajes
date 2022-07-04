package com.hiperusera.fichajes.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@JmixEntity
@Table(name = "FICH_FICHAJE")
@Entity(name = "fich_Fichaje")
public class Fichaje {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;


    @Column(name = "CENTRO")
    private String centro;

    @Column(name = "MARCA_TIEMPO")
    private LocalDateTime marcaTiempo;

    @Column(name = "NUMERO_TARJETA")
    private String numeroTarjeta;

    @Column(name = "NUMERO_EMPLEADO", nullable = false)
    @NotNull
    private String numeroEmpleado;

    @Column(name = "NOMBRE_EMPLEADO")
    private String nombreEmpleado;

    @Column(name = "APELLIDO_EMPLEADO")
    private String apellidoEmpleado;

    @Column(name = "TIPO_FICHAJE")
    private String tipoFichaje;

    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(name = "ENVIADO")
    private Boolean enviado;

    @Column(name = "REVISADO")
    private Boolean revisado;

    @Column(name = "FECHA")
    private LocalDate fecha;


    public Boolean getRevisado() {
        return revisado;
    }

    public void setRevisado(Boolean revisado) {
        this.revisado = revisado;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public LocalDate getFecha() {
        return fecha;
    }


    public void setFecha(LocalDate fecha){
        this.fecha = fecha;

    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public LocalDateTime getMarcaTiempo() {
        return marcaTiempo;
    }

    public void setMarcaTiempo(LocalDateTime marcaTiempo) {
        this.marcaTiempo = marcaTiempo;
    }

    public TipoFichaje getTipoFichaje() {
        return tipoFichaje == null ? null : TipoFichaje.fromId(tipoFichaje);
    }

    public void setTipoFichaje(TipoFichaje tipoFichaje) {
        this.tipoFichaje = tipoFichaje == null ? null : tipoFichaje.getId();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.marcaTiempo = dateTime;
    }

    public LocalDateTime getDateTime() {
        return marcaTiempo;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }


    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    @DependsOnProperties({"nombreEmpleado", "apellidoEmpleado"})
    public String getInstanceName() {
        return String.format("%s %s", nombreEmpleado, apellidoEmpleado);
    }


}