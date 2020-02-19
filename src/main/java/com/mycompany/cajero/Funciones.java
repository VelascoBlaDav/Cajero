
package com.mycompany.cajero;

import java.util.*;

public class Funciones {
    
    public static void menu(int cuenta){
        int opc, cantidad, cuenta2;
        
        System.out.println("\nBienvenido\n");
        System.out.println("Seleccione una opcion:\n");
        System.out.println("1) Consultar saldo:\n");
        System.out.println("2) Sacar dinero:\n");
        System.out.println("3) Ingresar dinero:\n");
        System.out.println("4) Transferencia entre cuentas:\n");
        opc = Lectura.entero();
        
        do{
            switch(opc){
                case 1:
                    //Solo llamada al servidor
                    consultaSaldo(cuenta);
                    break;
                case 2:
                    cantidad = Lectura.entero();
                    sacarDinero(cuenta, cantidad);
                    break;
                case 3:
                    cantidad = Lectura.entero();
                    ingresarDinero(cuenta, cantidad);
                    break;
                case 4:
                    cantidad = Lectura.entero();
                    cuenta2 = Lectura.entero();
                    cuenta2 = buscarCliente(cuenta2);
                    transferencia(cuenta, cantidad, cuenta2);
                    break;
                case 0:
                    break;
            }
        }while(opc!=0);
    
    }
    
    //Funciones
    private static boolean consultaSaldo(int cuenta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    public static boolean sacarDinero(int cuenta, int cantidad){
        //Si se desea retirar de la cuenta una cantidad que dejaría la cuenta 
        //en números rojos, se informará al usuario y se solicitará 
        //confirmación
        return true;
    }
    
    public static boolean ingresarDinero(int cuenta, int cantidad){
        return true;
    }
    
    public static boolean transferencia(int cuenta, int cantidad, int cuenta2){
        //No se permitirán transferencias que dejen en números 
        //rojos la cuenta de origen. 
        //Se deberá informar al usuario de esta situación.
        return true;
    }

    public static int buscarCliente(int cuenta){
        //si el cliente existe en el fichero devuelve el numero de cuenta del fichero
        return cuenta;
    }


}

