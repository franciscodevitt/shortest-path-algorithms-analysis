package org.ayed.gta;

import java.util.Scanner;

public class Partida {

    private Garaje garaje;
    private Menu menu;

    public static final int AGREGAR_VEHICULO = 1;
    public static final int MOSTRAR_VEHICULOS = 2;
    public static final int ELIMINAR_VEHICULO = 3;
    public static final int MEJORAR_GARAJE = 4;
    public static final int AGREGAR_CREDITOS = 5;
    public static final int MOSTRAR_VALOR = 6;
    public static final int MOSTAR_COSTO = 7;
    public static final int EXPORTAR_GARAJE = 8;
    public static final int IMPORTAR_GARAJE = 9;
    public static final int SALIR = 0;

    /**
     * cosntructor de la partida
     */
    public Partida() {
        this.garaje = new Garaje();
        this.menu = new Menu();
    }
    /**
     *incia partida
     * llama al menu principal
     */

    public void iniciarPartida() {
        int opcion;
        do {
            opcion = this.menu.menuPrincipal();
            try{

                switch (opcion) {
                    case AGREGAR_VEHICULO:
                        Vehiculo vehiculo = nuevoVehiculo();
                        garaje.agregarVehiculo(vehiculo);
                        break;
                    case MOSTRAR_VEHICULOS:
                        garaje.listarVehiculos();
                        break;
                    case ELIMINAR_VEHICULO:
                        String nombre = ingresarNombre();
                        garaje.eliminarVehiculo(nombre);
                        break;
                    case MEJORAR_GARAJE:
                        garaje.mejorarGaraje();
                        break;
                    case AGREGAR_CREDITOS:
                        Integer creditos = ingresarCreditos();
                        garaje.agregarCreditos(creditos);
                        break;
                    case MOSTRAR_VALOR:
                        System.out.println("El valor total del garaje es: $" + garaje.obtenerValorTotal() + ".");;
                        break;
                    case MOSTAR_COSTO:
                        System.out.println("Costo de mantenimieto del garaje: $" + garaje.obtenerCostoMantenimiento() + ".");;
                        break;
                    case EXPORTAR_GARAJE:
                        garaje.exportarGaraje();
                        break;
                    case IMPORTAR_GARAJE:
                        garaje.importarGaraje();
                        break;
                    case SALIR:
                        break;
                    default:
                        System.out.println("Ingrese una opcion valida");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            menu.pausar();
        } while (opcion != 0);
    }

    /**
     * solicita los datos para un nuevo vehiculo
     */

    public Vehiculo nuevoVehiculo(){

        String nombre = ingresarNombre();
        TipoVehiculo tipoVehiculo = TipoVehiculo.valueOf(ingresarTipoVehiculo());
        int precio = ingresarPrecio();
        int capacidadGasolina = ingresarCapacidadGasolina();

        Vehiculo vehiculo = new Moto(nombre, 12, precio, capacidadGasolina);
        return vehiculo;
    }
    /**
     * @return nombre del vehiculo
     */

    public String ingresarNombre(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del vehículo: ");
        return  scanner.nextLine();
    }
    /**
     * @return tipo de vehiculo
     */
    public  String ingresarTipoVehiculo(){
        Scanner scanner = new Scanner(System.in);
        String tipo;
        do{
            System.out.print("Ingrese el tipo de vehiculo AUTO/MOTO: ");
            tipo = scanner.nextLine().toUpperCase();
        }while (!tipo.equals("AUTO") && !tipo.equals("MOTO"));
        return tipo;
    }
    /**
     * @return precio del vehiculo
     */
    public  int ingresarPrecio(){
        Scanner scanner = new Scanner(System.in);
        String linea;
        do{
            System.out.print("Ingrese el precio del vehículo: ");
            linea = scanner.nextLine();
        }while (!esValido(linea));
        return Integer.parseInt(linea);

    }
    /**
     * @return capacidad de gasolina
     */

    public  int ingresarCapacidadGasolina(){
        Scanner scanner = new Scanner(System.in);
        String linea;
        do{
            System.out.print("Ingrese la capacidad de gasolina del vehiculo: ");
            linea = scanner.nextLine();
        }while (!esValido(linea));
        return Integer.parseInt(linea);

    }
    /**
     * @return creditos ingresados
     */

    public  int ingresarCreditos(){
        Scanner scanner = new Scanner(System.in);
        String linea;
        do{
            System.out.print("Ingrese la capacidad de gasolina del vehiculo: ");
            linea = scanner.nextLine();
        }while (!esValido(linea));
        return Integer.parseInt(linea);
    }
    /**
     * @return valides de un numero ingresado
     */
    public boolean esValido(String numeroString){
        boolean valido = false;
        try {
            int numero = Integer.parseInt(numeroString);
            if (numero > 0){
                valido = true;
            } else {
                System.out.println("El numero no puede ser negativo.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un número válido.");
        }
        return valido;
    }

}
