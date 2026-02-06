package com.kiosco;

import com.kiosco.model.Producto;
import com.kiosco.service.InventarioService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // instanciamos nuestro servicio (gestor de kiosco)
        InventarioService kiosco = new InventarioService();

        //instanciamos el scanner para leer.
        Scanner leer = new Scanner(System.in);

        int opcion = -1; // validacion para guardar que numero elige el usuario

        while (opcion != 0){

            System.out.println("\n--- SISTEMA DE GESTIÓN KIOSCO ---");
            System.out.println("1. Cargar Producto");
            System.out.println("2. Realizar Venta");
            System.out.println("3. Ver Inventario");
            System.out.println("4. Ver Caja y Ganancias");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leer.nextInt(); // el programa se detiene acá hasta que presionemos enter.
            leer.nextLine(); // limpiamos el buffer

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del producto: ");
                    String nombre = leer.nextLine();
                    System.out.print("Precio Costo: ");
                    double costo = leer.nextDouble();
                    System.out.print("Precio Venta: ");
                    double venta = leer.nextDouble();
                    System.out.print("Stock inicial: ");
                    int stock = leer.nextInt();

                    kiosco.agregarProducto(new Producto(1L, nombre, costo, venta, stock));
                    break;

                case 2:
                    System.out.print("¿Qué producto desea vender?: ");
                    String prodVenta = leer.nextLine();
                    System.out.print("Cantidad: ");
                    int cant = leer.nextInt();
                    kiosco.realizarVenta(prodVenta, cant);
                    break;

                case 3:
                    kiosco.mostrarInventario();
                    break;

                case 4:
                    kiosco.mostrarReporteFinanciero();
                    break;

                case 0:
                    System.out.println("Saliendo del sistema... ¡Buen trabajo hoy!");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        }

    }

}
