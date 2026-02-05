package com.powerlifting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Atleta {

    private Long id;
    private String name;
    private Double pesoCorporal;
    private Integer edad;
    private String sexo; //(M / D)

    private Record mejoresMarcas;

    public String obtenerCategoria(){

        if (pesoCorporal == null) return "Sin peso";
        if (pesoCorporal <= 59) return "-59kg";
        if (pesoCorporal <= 66) return "-66kg";
        if (pesoCorporal <= 74) return "-74kg";
        if (pesoCorporal <= 83) return "-83kg";
        if (pesoCorporal <= 93) return "-93kg";

        return "+93kg";

    }

}
