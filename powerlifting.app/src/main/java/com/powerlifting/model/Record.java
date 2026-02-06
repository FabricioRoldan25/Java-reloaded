package com.powerlifting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor

public class Record {

    private Double sentadilla;
    private Double bancoPlano;
    private Double pesoMuerto;

    public Double calcularTotal() {

        return (sentadilla != null ? sentadilla: 0.0) +
                (bancoPlano != null ? bancoPlano: 0.0) +
                (pesoMuerto != null ? pesoMuerto: 0.0);

    }

}
