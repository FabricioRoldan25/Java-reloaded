package com.kiosco.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // crea los getters y setters, toStrings y equals.
@AllArgsConstructor // hace los constructores con todos los atributos
@NoArgsConstructor // hace un constructor vacio

public class Producto implements Serializable {

    private static final long serialVersionUID = 1L; // es como la version del archivo

    private Long id;
    private String nombre;
    private double precioCosto;
    private double precioVenta;
    private int stock;

    public void stock(int cantidad) {
        this.stock += cantidad;
    }

}
