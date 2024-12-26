/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.trabajo_enfoque_programacion_de_servicios_y_procesos;

/**
 * Importación de clases:
 * Establezco la importación del componente 'Semaphore' el cual ayuda a gestionar el acceso a recursos y gestionando la sincronización entre varios hilos
 * 
 * Establezco la importación del componente 'AtomicBoolean' el cual permite garantizar el trabajo simultaneo cuando varios hilos intentan acceder al mismo recurso 
 * y estos no produzcan condiciones de carrera.
 */
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clase que representa el cliente que está en el concesionario esperando probar un coche, 
 * está clase la extiendo de 'Thread' para trabajar con hilos y así realizar el proceso de pruebas de coches de manera eficiente.
 * @author sebastiancamposrojas
 */
public class ClienteConcesionario extends Thread {
    private String nombre_cliente;
    private Semaphore semaforo;
    private AtomicBoolean[] coches_disponibles;

    /**
     * Constructor para inicializar atributos de la clase ClienteConcesionario.
     * @param nombre_cliente
     * @param semaforo
     * @param coches_disponibles 
     */
    public ClienteConcesionario(String nombre_cliente, Semaphore semaforo, AtomicBoolean[] coches_disponibles) {
        this.nombre_cliente = nombre_cliente;
        this.semaforo = semaforo;
        this.coches_disponibles = coches_disponibles;
    }
    
    /**
     * Sobreescribo el método run() que es obtenido desde la clase heredada 'Thread' para comenzar a ejecutar hilos 
     * y realizar el proceso de lectura de los recursos 'coches' y asignarlos a los clientes.
     */
    @Override
    public void run() {
        /**
         * Establezco un controlador de errores try/catch en caso de producirse una interrupción de algún hilo.
         */
        try {
            /**
             * Establezco el cierre de la sección crítica mientras el cliente comienza a probar el coche.
             */
            semaforo.acquire();
            
            /**
             * Establezco el tiempo para simular lo que demoraría un cliente en probar un coche.
             */
            int tiempo_prueba = tiempoPrueba();
            
            /**
             * Asigno el primero coche disponible existente.
             */
            int numero_coche = asignarCoche();
            
            /**
             * Se entrega mensaje indicando que el cliente esta probando el coche
             * sumo + 1 a la variable numero_coche porque los arrays comienzan en el elemento 0 y para no mostrar un valor 0 que viene retornado desde el método asignarCoche(), le sumo 1.
             */
            System.out.println(this.nombre_cliente + " ... probando vehículo ... " + (numero_coche + 1));

            /**
             * Se establece un valor de dormido al hilo 
             * simulando ser el tiempo el que están probando el coche.
             */
            Thread.sleep(tiempo_prueba);

            /**
             * Se entrega mensaje que ha dejado de probar el coche y da paso al siguiente cliente
             * sumo + 1 a la variable numero_coche porque los arrays comienzan en el elemento 0 y para no mostrar un valor 0 que viene retornado desde el método asignarCoche(), le sumo 1.
             */
            System.out.println(this.nombre_cliente + " ... terminó de probar el vehículo ... " + (numero_coche + 1));
            
            /**
             * Libero el coche que ya dejo de probar el cliente.
             */
            liberarCoche(numero_coche);
            
        /**
         * Establezco la captura de la excepción de interrupción de algún hilo, 
         * en caso que el flujo sea interrumpido por mientras se estaba esperando.
         */
        } catch (InterruptedException e) {
            e.printStackTrace();
            
        /**
         * Utilizo finally porque me aseguro que el flujo del programa pasará por aquí si o si, 
         * si este entra por el try o por el catch, de está manera libero y abro la sección critica.
         */
        } finally {
            /**
             * Establezco la apertura de la sección critica para que otro cliente pueda probar el coche.
             */
            semaforo.release();
        }
    }
    
    /**
     * Método para accionar la asignación de coches a los clientes que desean probar,
     * utilizando el metodo compareAndSet() de la variable AtomicBoolean 'coches_disponibles' 
     * que nos permite tomar el coche y marcarlo como en uso
     * establezco el encapsulado del método a 'private', dado que será accedido solo desde está clase en donde está definido: 'ClienteConcesionario'.
     * @return int
     * @throws IllegalStateException 
     */
    private int asignarCoche() throws IllegalStateException{
        /**
         * Realizo busqueda de algún vehículo disponible.
         */
        for (int i = 0; i < coches_disponibles.length; i++) {
            /**
             * Establezco el coche como ocupado con el método compareAndSet()
             * esté método me permite seleccionar un recurso especifico y cambiar su condición,
             * en este caso concreto, el coche que itera en el bucle y está con valor 'true' será seleccionado y se seteará a 'false'
             * para que este coche no sea tomado por otro cliente hasta que le indiquemos que está disponible nuevamente.
             */
            if (coches_disponibles[i].compareAndSet(true, false)) { 
                return i;
            }
        }
        
        /**
         * Establezco una excepción, en caso que no hayan coches_disponibles.
         */
        throw new IllegalStateException("Por el momento no tenemos coches disponibles.");
    }
    
    /**
     * Método para accionar la liberación del coche del asignado en el parámetro 'coche' entrante del método
     * seteando la variable array con el elemento del 'coche' como verdadero usando el metodo set() de AtomicBoolean
     * establezco el encapsulado del método a 'private', dado que será accedido solo desde está clase en donde está definido: 'ClienteConcesionario'.
     * @param coche 
     */
    private void liberarCoche(int coche) {
        /**
         * Establezco como disponible el coche que entra por parámetro de esté método.
         */
        coches_disponibles[coche].set(true); 
    }
    
    /**
     * Método para establecer un tiempo aleatorio entre 1 segundo a 5 segundos 
     * para simular el tiempo que demoraría un cliente probando un coche en el concesionario
     * establezco el encapsulado del método a 'private', dado que será accedido solo desde está clase en donde está definido: 'ClienteConcesionario'.
     * @return int
     */
    private int tiempoPrueba(){
        /**
         * Establezco tiempo a probar el coche por cada cliente.
         */
        return (int) (Math.random() * 5000) + 1000;
    }
}
