package com.example.p3di.core;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity(tableName = "tarea")
public class Tarea implements Serializable {
    @PrimaryKey
    @NonNull
    private String tareaId;
    @ColumnInfo(name = "titulo")
    private String titulo;
    @ColumnInfo(name = "fecha")
    private long fechaLimite;
    @ColumnInfo(name = "favorito")
    private boolean favorito;
    @ColumnInfo(name = "completado")
    private boolean completado;
    @ColumnInfo(name= "selected")
    private boolean selected;


    public Tarea(String titulo, long fechaLimite, boolean favorito, boolean completado, boolean selected) {
        this.tareaId = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.fechaLimite = fechaLimite;
        this.favorito = favorito;
        this.completado = completado;
        this.selected = selected;
    }

    public String getTareaId() {
        return tareaId;
    }

    public void setTareaId(String tareaId) {
        this.tareaId = tareaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(long fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
