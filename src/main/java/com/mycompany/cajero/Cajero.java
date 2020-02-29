
package com.mycompany.cajero;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Cajero {
    public static void Main(String[] args) {
        int cuenta;       
        Socket s = null;
        
        try{
            int serverPort = 8888;
            //Creo el socket para conectarme con el servidor
            s = new Socket("localhost", serverPort); 
            DataInputStream in = new DataInputStream( s.getInputStream());
            DataOutputStream out = new DataOutputStream( s.getOutputStream());
            
            System.out.println("Escriba el numero de cuenta:\n");
            cuenta = Lectura.entero();
            int opc, cantidad, cuenta2, cantidadCuenta;
            String mensaje;
            String mensajeS;
            boolean confirmacion;

            do{
                System.out.println("\nBienvenido\n");
                System.out.println("Seleccione una opcion:\n");
                System.out.println("1) Consultar saldo:\n");
                System.out.println("2) Sacar dinero:\n");
                System.out.println("3) Ingresar dinero:\n");
                System.out.println("4) Transferencia entre cuentas:\n");
                System.out.println("5) Cambiar de cuenta:\n");
                opc = Lectura.entero();
                confirmacion = false;
                switch(opc){
                    case 1:
                        mostrarDinero(cuenta, in, out);
                        break;
                    case 2:
                        //Pido la cantidad a sacar
                        System.out.println("Escribe la cantidad a extraer:\n");
                        cantidad = Lectura.entero();
                        sacarDinero(cuenta, cantidad, in, out);
                        break;
                    case 3:
                        System.out.println("Escribe la cantidad a extraer:\n");
                        cantidad = Lectura.entero();
                        ingresarDinero(cuenta, cantidad, in, out);
                        break;
                    case 4:
                        //Pido la cantidad a ingresar 
                        cantidad = Lectura.entero();
                        //Pido la cuenta a ingresar el dinero
                        cuenta2 = Lectura.entero();
                        tranferirDinero(cuenta, cuenta2, cantidad, in, out);
                        break;
                    case 5:
                        cuenta = Lectura.entero();
                        break;
                    case 0:
                        break;
                }
            }while(opc!=0);
            //Termino el servicio del cliente
            s.close();
        }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("readline:"+e.getMessage());
        }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
    

    
    /*PASO DE MENSAJES*/
    private static void pasarDatos(int opcion,int cuenta,int total,DataOutputStream out){
        try {
            String mensaje = opcion+";"+cuenta+";"+(total);
            out.writeUTF(mensaje);
        }catch(IOException e){
            e.printStackTrace();
        }  
    }

    private static void pasarDatos(int opcion,int cuenta,DataOutputStream out){
            try {
                String mensaje = opcion+";"+cuenta+";0";
                out.writeUTF(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }  
    }

    /*BUSQUEDA DE CUENTA*/
    
    private static boolean buscarCuenta(int cuenta,DataInputStream in,DataOutputStream out){
        pasarDatos(1,cuenta,out);
        try {
            int cantidad = in.readInt();
            boolean result=in.readBoolean();
            if (result==false) {
                System.out.println("La cuenta: "+cuenta+" no existe\n");
                System.out.println("Operación cancelada/n");
                return result;
                }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    /*FUNCIONES DEL MENU*/
    
    //Mostar el saldo de la cuenta
    
    private static boolean mostrarDinero(int cuenta, DataInputStream in, DataOutputStream out){
        pasarDatos(1,cuenta,out);
        try {
            int saldo = in.readInt();
            boolean result=in.readBoolean();
            if (result==false) {
                    System.out.println("Error al consultal el saldo/n");
                    return false;
            }
            System.out.println("Saldo actual: "+saldo+"/n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Sacar dinero de la cuenta
    private static boolean sacarDinero(int cuenta, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        pasarDatos(1,cuenta,out);
        int cantidadCuenta = in.readInt();
        if((cantidadCuenta-cantidad)>0){
            pasarDatos(2, cuenta, cantidadCuenta-cantidad, out);
        }else{
            System.out.println("Saldo insuficiente, ¿desea realizar la operacion?(1/0)");
            boolean confirmacion = Lectura.booleanoNumerico();
            if(confirmacion == true){
                pasarDatos(2, cuenta, cantidadCuenta-cantidad, out);
            }else{
                System.out.println("Operacion carcelada con exito");
            }
        }
        return false;
    }
    
    //Ingresar dinero a la cuenta
    private static boolean ingresarDinero(int cuenta, int cantidad, DataInputStream in, DataOutputStream out){
        pasarDatos(1,cuenta,out);
        int cantidadCuenta = 0;
        if((cantidadCuenta+cantidad)>0){
            System.out.println("La cantidad a ingresar es esta: "+cantidad+" ¿Es correcta?(1/0)");
            boolean confirmacion = Lectura.booleanoNumerico();
            if(confirmacion == true){
                pasarDatos(3, cuenta, cantidadCuenta+cantidad, out);
            }
        }else{
            System.out.println("Imposible realizar esta operacion\nOperacion carcelada con exito");
        }
        return false;
    }
    
    //Transferir dinero a otra cuenta
    private static boolean tranferirDinero(int cuenta, int cuenta2, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
         int cantidadCuenta = in.readInt();
        if((cantidadCuenta-cantidad)>0){
            sacarDinero(cuenta, cantidad, in, out);
            ingresarDinero(cuenta2, cantidad, in, out);
        }else{
            System.out.println("Operacion invalida, saldo insuficiente");
        }
    
        return false;
    }
    
}
