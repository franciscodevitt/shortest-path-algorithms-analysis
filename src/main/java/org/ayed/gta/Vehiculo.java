package org.ayed.gta;

public abstract class Vehiculo {

    protected String nombre;
    protected TipoVehiculo tipo;
    protected int precio;
    protected int capacidadGasolina;   // capacidad máxima del tanque
    protected int gasolinaActual;      // litros actuales
    protected int kilometraje;         // en km
    protected int velocidadMaxima;     // en km/h

    public static final int PRECIO_RUEDA_MOTO = 30;
    public static final int PRECIO_RUEDA_AUTO = 50;
    public static final int PRECIO_LITRO = 1;

    // constructor base
    protected Vehiculo(String nombre, TipoVehiculo tipo, int precio, int capacidadGasolina, int velocidadMaxima) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("el nombre del vehiculo no puede ser vacio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("el tipo no puede ser nulo");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("el precio tiene que ser mayor a 0");
        }
        if (capacidadGasolina < 0) {
            throw new IllegalArgumentException("la capacidad de gasolina no puede ser negativa");
        }
        if (velocidadMaxima < 0) {
            throw new IllegalArgumentException("la velocidad maxima no puede ser negativa");
        }

        this.nombre = nombre.trim();
        this.tipo = tipo;
        this.precio = precio;
        this.capacidadGasolina = capacidadGasolina;
        this.gasolinaActual = 0;   // arrancamos en 0 por defecto
        this.kilometraje = 0;
        this.velocidadMaxima = velocidadMaxima;
    }

    // getters básicos
    public String getNombre() { return nombre; }
    public TipoVehiculo getTipo() { return tipo; }
    public int getPrecio() { return precio; }
    public int getCapacidadGasolina() { return capacidadGasolina; }
    public int getGasolinaActual() { return gasolinaActual; }
    public int getKilometraje() { return kilometraje; }
    public int getVelocidadMaxima() { return velocidadMaxima; }

    // para compatibilidad con el garaje
    public String obtenerNombreVehiculo() {
        return this.nombre;
    }

    // cada subclase define cuántas ruedas tiene
    public abstract int ruedas();

    // costo por ruedas (por defecto autos/motos)
    protected int costoRuedas() {
        if (tipo == TipoVehiculo.AUTO) {
            return PRECIO_RUEDA_AUTO * ruedas();
        } else if (tipo == TipoVehiculo.MOTO) {
            return PRECIO_RUEDA_MOTO * ruedas();
        }
        // para EXOTICO se sobreescribe en la subclase
        return 0;
    }

    // costo por kilometraje: por defecto 0, se sobreescribe en subclases
    protected int costoPorKilometraje() {
        return 0;
    }

    // calcula el costo de mantenimiento del vehiculo
    public int obtenerCostoPorVehiculo() {
        int costoBaseRuedas = costoRuedas();
        int costoGasolina = PRECIO_LITRO * capacidadGasolina; // podés ajustar a gasolinaActual si quieren
        return costoBaseRuedas + costoGasolina + costoPorKilometraje();
    }

    // devuelve el precio del vehiculo
    public int obtenerPrecioPorVehiculo() {
        return this.precio;
    }

    // devuelve una version corta para mostrar en consola
    public String obtenerVehiculo() {
        return tipo + ": " + nombre + " (Precio: $" + precio + ", Gasolina: " + gasolinaActual + "/" + capacidadGasolina + "L, Km: " + kilometraje + ")";
    }

    // devuelve una linea lista para exportar al csv
    // formato: nombre,precio,tipo,ruedas,capacidadGasolina,gasolinaActual,kilometraje
    public String obtenerVehiculoParaExportar() {
        return nombre + "," + precio + "," + tipo + "," + ruedas() + "," + capacidadGasolina + "," + gasolinaActual + "," + kilometraje;
    }

    // cargar una cantidad específica de combustible, devuelve litros realmente cargados
    public int cargarCombustible(int litros) {
        if (litros < 0) {
            throw new IllegalArgumentException("no se pueden cargar litros negativos");
        }
        int espacioDisponible = capacidadGasolina - gasolinaActual;
        int litrosCargados = Math.min(litros, espacioDisponible);
        gasolinaActual += litrosCargados;
        return litrosCargados;
    }

    // cargar hasta el máximo, devuelve litros cargados
    public int cargarAlMaximo() {
        int espacioDisponible = capacidadGasolina - gasolinaActual;
        gasolinaActual = capacidadGasolina;
        return espacioDisponible;
    }
    

    // suma kilometros (para que el juego principal lo use en las misiones)
    public void sumarKilometros(int km) {
        if (km < 0) {
            throw new IllegalArgumentException("no se pueden restar kilometros");
        }
        this.kilometraje += km;
    }

    // consume gasolina del vehículo
    public boolean consumirGasolina(int litros) {
        if (litros < 0) {
            throw new IllegalArgumentException("no se pueden consumir litros negativos");
        }
        if (gasolinaActual >= litros) {
            gasolinaActual -= litros;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Vehiculo{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", precio=" + precio +
                ", capacidadGasolina=" + capacidadGasolina +
                ", gasolinaActual=" + gasolinaActual +
                ", kilometraje=" + kilometraje +
                ", velocidadMaxima=" + velocidadMaxima +
                ", costoDiario=" + obtenerCostoPorVehiculo() +
                '}';
    }
}
