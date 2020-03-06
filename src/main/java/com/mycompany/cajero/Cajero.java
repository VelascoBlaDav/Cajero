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
        
        //Hago un try-catch-finally para el control de excepciones
        try{
            int serverPort = 8888;
            //Creo el socket para conectarme con el servidor en el puerto añadido anteriormente
            s = new Socket("localhost", serverPort); 
            //Genero el DataInput y el DataOutput con el socket que cree antes 
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            
            //Pido el numero de cuenta y compruebo que existe el numero antes de mostrar el menu simulando un logueo
            System.out.println("\nBienvenido al cajero\n");
            System.out.println("Escriba el numero de cuenta:\n");
            cuenta = Lectura.entero();
            buscarCuenta(cuenta, in, out);
            int opc, cantidad, cuenta2;
            //Inicio el bucle para el menu
            do{
                System.out.println("Seleccione una opcion:\n");
                System.out.println("1) Consultar saldo:\n");
                System.out.println("2) Sacar dinero:\n");
                System.out.println("3) Ingresar dinero:\n");
                System.out.println("4) Transferencia entre cuentas:\n");
                System.out.println("5) Cambiar de cuenta:\n");
                System.out.println("0) Salir:\n");
                opc = Lectura.entero();
                switch(opc){
                    case 1:
                        //Llamo a la funcion mostrarDinero para consultar el saldo disponible
                        mostrarDinero(cuenta, in, out);
                        break;
                    case 2:
                        //Pido la cantidad a sacar
                        System.out.println("Escribe la cantidad a extraer:\n");
                        cantidad = Lectura.entero();
                        //Llamo a la funcion sacarDinero
                        sacarDinero(cuenta, cantidad, in, out);
                        break;
                    case 3:
                        //Pido la cantidad a sacar
                        System.out.println("Escribe la cantidad a extraer:\n");
                        cantidad = Lectura.entero();
                        //Llamo a la funcion ingresarDinero
                        ingresarDinero(cuenta, cantidad, in, out);
                        break;
                    case 4:
                        //Pido la cantidad a ingresar 
                        cantidad = Lectura.entero();
                        //Pido la cuenta a la que ingresar el dinero
                        cuenta2 = Lectura.entero();
                        //Llamo a la funcion transferirDinero
                        tranferirDinero(cuenta, cuenta2, cantidad, in, out);
                        break;
                    case 5:
                        //Pido el numero de cuenta y compruebo que existe
                        cuenta = Lectura.entero();
                        buscarCuenta(cuenta, in, out);
                        break;
                    case 0:
                        System.out.println("\nMuchas gracias\nQue tenga un buen dia\n");
                        pasarDatos(opc, cuenta, out);
                        //Al ser la opc = 0 se sale del programa
                        break;
                }
            }while(opc!=0);
            //Termino el servicio del cliente y cierro el shocket
            s.close();
        }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("readline:"+e.getMessage());
        }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
        
    /*PASO DE MENSAJES*/
    private static void pasarDatos(int opcion,int cuenta,int total,DataOutputStream out) throws IOException{

        //Genero una cadena con los datos a enviar
        String mensaje = opcion+";"+cuenta+";"+(total);
        //Envio la cadena
        out.writeUTF(mensaje);
    }

    private static void pasarDatos(int opcion,int cuenta,DataOutputStream out) throws IOException{
        //Genero una cadena con los datos a enviar
        String mensaje = opcion+";"+cuenta;
        //Envio la cadena
        out.writeUTF(mensaje);
    }
    
    /*BUSQUEDA DE CUENTA*/

    private static boolean buscarCuenta(int cuenta,DataInputStream in,DataOutputStream out) throws IOException{
        pasarDatos(1,cuenta,out);

        boolean result=in.readBoolean();
        //Si la cuenta existe
        if (result==true) {
            System.out.println("Bienvenido "+cuenta+"\n");
            return true;
            
        }else{
            //Sino muestro un mensaje de error
            System.out.println("La cuenta: "+cuenta+" no existe\n");
            System.out.println("Operación cancelada/n");
            return false;
        }
    }
    
    /*BLOQUEO DE CUENTA*/
    
    private static void bloquearCuenta(int cuenta,DataOutputStream out) throws IOException{
        pasarDatos(3,cuenta,out);
    }
    /*DESBLOQUEO DE CUENTA*/
    
    private static void desbloquearCuenta(int cuenta,DataOutputStream out) throws IOException{
        pasarDatos(4,cuenta,out);
    }

    /*FUNCIONES DEL MENU*/
    
    //Mostar el saldo de la cuenta
    
    private static boolean mostrarDinero(int cuenta, DataInputStream in, DataOutputStream out) throws IOException{
        //Llamo a la funcion pasarDatos
        pasarDatos(1,cuenta,out);
        //Guardo los datos recibidos en variables
        String data = in.readUTF();
        String[] datos =data.split(";");
        int saldo = Integer.parseInt(datos[0]);
        boolean result = Boolean.valueOf(datos[1]);
        //Hago la comprobacion de que el saldo se ha podido comprobar correctamente
        if (result==true){
           //Si se ha podido comprobar muestro el saldo y devuelvo verdadero
            System.out.println("Saldo actual: "+saldo+"/n");
            return true;
        }else{
            //Si no se ha podido comprobar mando un mensaje de error y devuelvo falso
            System.out.println("Error al consultal el saldo/n");
            return false;
        }
    }
    
    //Sacar dinero de la cuenta
    private static boolean sacarDinero(int cuenta, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        //Llamo a la funcion pasarDatos
        pasarDatos(1,cuenta,out);
        //Guardo el dato recibido en cantidadCuenta
        String data = in.readUTF();
        String[] datos =data.split(";");
        int cantidadCuenta = Integer.parseInt(datos[0]);
        boolean result = Boolean.valueOf(datos[1]);
        //Compruebo que la cantidad total es mayor que 0
        if((cantidadCuenta-cantidad)>0){
            bloquearCuenta(cuenta, out);
            //Si es mayor que 0 llamo a la funcion pasarDatos y envio los datos al servidor
            pasarDatos(2, cuenta, cantidadCuenta-cantidad, out);
            desbloquearCuenta(cuenta, out);
            return true;
        }else{
            //Si es menor que 0 muestro un mensaje de error y pregunto si quiere continuar con la operacion
            System.out.println("Saldo insuficiente, ¿desea realizar la operacion?(1/0)");
            boolean confirmacion = Lectura.booleanoNumerico();
            if(confirmacion == true){
                //Si hay confirmacion paso los datos
                bloquearCuenta(cuenta, out);
                pasarDatos(2, cuenta, cantidadCuenta-cantidad, out);
                desbloquearCuenta(cuenta, out);
                return true;
            }else{
                //Sino muestro un mensaje
                System.out.println("Operacion carcelada con exito");
                return false;
            }
        }
    }
    
    //Ingresar dinero a la cuenta
    private static boolean ingresarDinero(int cuenta, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        //Llamo a la funcion pasarDatos
        pasarDatos(1,cuenta,out);
        //Guardo el dato recibido en cantidadCuenta
        String data = in.readUTF();
        String[] datos =data.split(";");
        int cantidadCuenta = Integer.parseInt(datos[0]);
        boolean result = Boolean.valueOf(datos[1]);
        //Si el total es mayor que cero 
        if((cantidadCuenta+cantidad)>0){
            //Pido una confirmacion
            System.out.println("La cantidad a ingresar es esta: "+cantidad+" ¿Es correcta?(1/0)");
            boolean confirmacion = Lectura.booleanoNumerico();
            //Si se confirma
            if(confirmacion == true){
                bloquearCuenta(cuenta, out);
                //Llamo a la funcion pasarDatos
                pasarDatos(3, cuenta, cantidadCuenta+cantidad, out);
                desbloquearCuenta(cuenta, out);
                return true;
            }else{
                //Sino se confirmo devuelvo un mensaje de error
                System.out.println("Operacion carcelada\n");
                return false;
            }
        }else{
            //Sino muestro un mensaje de error
            System.out.println("Imposible realizar esta operacion\nOperacion carcelada con exito");
            return false;
        }        
    }
    
    //Transferir dinero a otra cuenta
    private static boolean tranferirDinero(int cuenta, int cuenta2, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        //Guardo el dato recibido en cantidadCuenta
        String data = in.readUTF();
        String[] datos =data.split(";");
        int cantidadCuenta = Integer.parseInt(datos[0]);
        boolean result = Boolean.valueOf(datos[1]);
        //Si la cantidad total es mayor que 0
        if((cantidadCuenta-cantidad)>0){
            //Saco el dinero de la primera cuenta y lo ingreso en la segunda cuenta
            sacarDinero(cuenta, cantidad, in, out);
            ingresarDinero(cuenta2, cantidad, in, out);
            return true;
        }else{
            //Devuelvo un mensaje de error
            System.out.println("Operacion invalida, saldo insuficiente");
            return false;
        }
    }
    
}

