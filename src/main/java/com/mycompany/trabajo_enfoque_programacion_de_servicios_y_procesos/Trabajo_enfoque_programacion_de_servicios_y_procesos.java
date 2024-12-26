/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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
 * Clase que inicializa y ejecuta los hilos (clientes) para el uso de los recursos (coches)
 * @author sebastiancamposrojas
 */
public class Trabajo_enfoque_programacion_de_servicios_y_procesos {
    
    /**
     * Establezco variable estática con el valor de 4
     * (siendo 4 los coches disponibles en el concesionario).
     */
    private static final int COCHES = 4;
    
    /**
     * Establezco variable estática con el valor de 9
     * (siendo 9 los clientes que están solicitando probar coches).
     */
    private static final int CLIENTES = 9;

    
    /**
     * Método principal a ejecutar, este es el método que buscará Java para empezar la ejecución de una clase.
     * @param args 
     */
    public static void main(String[] args) {
        
        /**
         * Establezco el componente 'Semaphore' con el valor de la variable estática 'COCHES' 
         * así poder distribuir de manera eficiente el uso de los coches con los respectivos clientes simultaneamente.
         */
        Semaphore semaforo = new Semaphore(COCHES);
        
        /**
         * Establezco el componente 'AtomicBoolean' para garantizar el acceso 
         * y modificación de los mismos recursos de manera simultanea.
         */
        AtomicBoolean[] coches_disponibles = new AtomicBoolean[COCHES];
        
        /**
         * Inicializo los coches disponibles con AtomicBoolean, 
         * asignandoles valores de los coches a la variable 'AtomicBoolean[]'.
         */
        for (int i = 0; i < COCHES; i++) {
            coches_disponibles[i] = new AtomicBoolean(true);
        }
        
        /**
         * Genero un bucle referente a clientes, de 1 al valor de la variable estática 'CLIENTES'
         * para definir el paso de todos los clientes a probar los coches.
         */
        for (int x = 1; x <= CLIENTES; x++) {
            
            /**
             * Establezco una instancia de ClienteConcesionario, indicando el cliente, semaforo y coches_disponibles previamente asignados
             * para luego este objeto ejecute método start(), dando inicio a los hilos.
             */
            ClienteConcesionario cliente = new ClienteConcesionario("Cliente_" + x, semaforo, coches_disponibles);
            cliente.start();  
        }
    }
}
