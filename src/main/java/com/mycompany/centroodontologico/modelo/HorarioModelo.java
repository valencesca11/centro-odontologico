package com.mycompany.centroodontologico.modelo;

import java.sql.Time;

public class HorarioModelo {

    private String dia;
    private Time horaInicio;
    private Time horaFin;

    public HorarioModelo() {}

    public HorarioModelo(String dia, Time horaInicio, Time horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return dia + ": " + horaInicio + " - " + horaFin;
    }
}
