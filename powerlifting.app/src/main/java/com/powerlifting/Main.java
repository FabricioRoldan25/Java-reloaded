package com.powerlifting;

import com.powerlifting.model.Record;
import com.powerlifting.model.Atleta;

public class Main {

    public static void main(String[] args) {
        //registro de fuerza
        Record marcasActuales = new Record(90.0, 75.5, 155.0);
        //creamos al atleta usando composicion
        Atleta Fabricio = new Atleta(1l, "Fabricio", 65.4, 22, "M", marcasActuales);

        System.out.println("--- Reporte de Powerlifting ---");
        System.out.println("Atleta: " + Fabricio.getName());
        System.out.println("Categoria: " + Fabricio.obtenerCategoria());
        System.out.println("Total: " + Fabricio.getMejoresMarcas().calcularTotal() + " kg");

    }

}