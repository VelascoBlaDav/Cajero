package com.mycompany.cajero;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cajero {
    public static void main(String[] args) {
        int cuenta = 0;       
        Socket s = null;
        
        //Hago un try-catch-finally para el control de excepciones
        try{
            int serverPort = 8888;
            //Creo el socket para conectarme con el servidor en el puerto añadido anteriormente
            s = new Socket("localhost", serverPort); 
            //Genero el DataInput y el DataOutput con el socket que cree antes 
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            int opc;
            Boolean ok=false;
            
            System.out.println("Esperando respuesta del servidor...\n");
            do{
                //Pido el numero de cuenta y compruebo que existe el numero antes de mostrar el menu simulando un logueo
                System.out.println("\nBienvenido al cajero\n");
                while(ok!=true){
                    System.out.println("Escriba el numero de cuenta:\n");
                    cuenta = Lectura.entero();
                    ok = buscarCuenta(cuenta, in, out);
                    comprobarBoolean(ok);
                }
                int cantidad, cuenta2;
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
                            ok = mostrarDinero(cuenta, in, out);
                            comprobarBoolean(ok);
                            break;
                        case 2:
                            //Pido la cantidad a sacar
                            System.out.println("Escribe la cantidad a extraer:\n");
                            cantidad = Lectura.entero();
                            //Llamo a la funcion sacarDinero
                            ok = sacarDinero(cuenta, cantidad, in, out);
                            comprobarBoolean(ok);
                            break;
                        case 3:
                            //Pido la cantidad a ingresar
                            System.out.println("Escribe la cantidad a ingresar:\n");
                            cantidad = Lectura.entero();
                            //Llamo a la funcion ingresarDinero
                            ok = ingresarDinero(cuenta, cantidad, in, out);
                            comprobarBoolean(ok);
                            break;
                        case 4:
                            //Pido la cantidad a ingresar 
                            System.out.println("Escribe la cantidad a ingresar:\n");
                            cantidad = Lectura.entero();
                            //Pido la cuenta a la que ingresar el dinero
                            System.out.println("Escribe la cuenta a ingresar el dinero:\n");
                            cuenta2 = Lectura.entero();
                            //Llamo a la funcion transferirDinero
                            ok = tranferirDinero(cuenta, cuenta2, cantidad, in, out);
                            comprobarBoolean(ok);
                            break;
                        case 5:
                            //Pido el numero de cuenta y compruebo que existe
                            System.out.println("Escriba el numero de cuenta:\n");
                            cuenta2 = cuenta;
                            cuenta = Lectura.entero();
                            if(cuenta==cuenta2){
                                System.out.println("Error: Has añadido la misma cuenta\n");
                                System.out.println("¿Quieres cambiar de cuenta?(1/0)\n");
                                boolean comprobacion = Lectura.booleanoNumerico();
                                if(comprobacion==true){
                                    ok = buscarCuenta(cuenta, in, out);
                                while(ok==false){
                                    System.out.println("Escriba el numero de cuenta:\n");
                                    cuenta = Lectura.entero();
                                    ok = buscarCuenta(cuenta, in, out);
                                    }
                                }
                            }else{
                                ok = buscarCuenta(cuenta, in, out);
                                while(ok==false){
                                    System.out.println("Escriba el numero de cuenta:\n");
                                    cuenta = Lectura.entero();
                                    ok = buscarCuenta(cuenta, in, out);
                                }
                            }
                            break;
                        case 0:
                            System.out.println("\nMuchas gracias\nQue tenga un buen dia\n");
                            pasarDatos(opc, cuenta, out);
                            borrarBuffer(in);
                            //Al ser la opc = 0 se sale del programa
                            break;
                    }
                }while(opc!=0);
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
    
    /*FUNCION DE BUFFER*/
    private static void borrarBuffer(DataInputStream in) throws IOException{
        String data = in.readUTF();
    }
    
    /*BUSQUEDA DE CUENTA*/

    private static boolean buscarCuenta(int cuenta,DataInputStream in,DataOutputStream out) throws IOException{
        pasarDatos(1,cuenta,out);

        String data = in.readUTF();
        String[] datos =data.split(";");
        int result = Integer.parseInt(datos[0]);
        System.out.println(result);
        //Si la cuenta existe
        if (result==0) {
            System.out.println("Bienvenido "+cuenta+"\n");
            return true;
            
        }else{
            return false;
        }
    }
    
    /*BLOQUEO DE CUENTA*/
    
    private static boolean bloquearCuenta(int cuenta, DataInputStream in, DataOutputStream out) throws IOException{
        pasarDatos(3,cuenta,out);
        String data = in.readUTF();
        String[] datos =data.split(";");
        int result = Integer.parseInt(datos[0]);
        if(result == 1){
            System.out.println("Error del servidor\n");
            return false;
        }
        return true;
    }
    /*DESBLOQUEO DE CUENTA*/
    
    private static boolean desbloquearCuenta(int cuenta,DataInputStream in, DataOutputStream out) throws IOException{
        pasarDatos(4,cuenta,out);
        String data = in.readUTF();
        String[] datos =data.split(";");
        int result = Integer.parseInt(datos[0]);
        if(result == 1){
            System.out.println("Error del servidor\n");
            return false;
        }else{
            String buffer = in.readUTF();
            String[] buffers =buffer.split(";");
            int result2 = Integer.parseInt(buffers[0]);
            if(result2 == 1){
                System.out.println("Error del servidor\n");
                return false;
            }
        return true;
        }
    }

    /*FUNCIONES DEL MENU*/
    
    //Mostar el saldo de la cuenta
    
    private static boolean mostrarDinero(int cuenta, DataInputStream in, DataOutputStream out) throws IOException{
        //Llamo a la funcion pasarDatos
        pasarDatos(1,cuenta,out);
        //Guardo los datos recibidos en variables
        String data = in.readUTF();
        String[] datos =data.split(";");
        int result = Integer.parseInt(datos[0]);
        int saldo = Integer.parseInt(datos[1]);
        //Hago la comprobacion de que el saldo se ha podido comprobar correctamente
        if (result == 0){
           //Si se ha podido comprobar muestro el saldo y devuelvo verdadero
            System.out.println("Saldo actual: "+saldo+"\n");
            return true;
        }else{
            //Si no se ha podido comprobar mando un mensaje de error y devuelvo falso
            System.out.println("Error del servidor\n");
            System.out.println("Error al consultal el saldo\n");
            return false;
        }
    }
    
    //Sacar dinero de la cuenta
    private static boolean sacarDinero(int cuenta, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        Boolean ok, ok2;
        //Llamo a la funcion pasarDatos
        pasarDatos(1,cuenta,out);
        //Guardo el dato recibido en cantidadCuenta
        String data = in.readUTF();
        String[] datos =data.split(";");
        int result = Integer.parseInt(datos[0]);
        int cantidadCuenta = Integer.parseInt(datos[1]);
        if(result != 0){
            System.out.println("Error del servidor\n");
            return false;
        }else{
            //Compruebo que la cantidad total es mayor que 0
            if((cantidadCuenta-cantidad)>0){ //Si es mayor que 0 llamo a la funcion pasarDatos y envio los datos al servidor
                ok = bloquearCuenta(cuenta, in, out);
                if(ok==true){
                   pasarDatos(2, cuenta, cantidadCuenta-cantidad, out);
                    //borrarBuffer(in);
                   ok2 = desbloquearCuenta(cuenta, in, out);
                   if(ok2!=true){
                       System.out.println("Error en el servidor.\n");
                   }
                   return ok2;
                }
            }else{
                //Si es menor que 0 muestro un mensaje de error y pregunto si quiere continuar con la operacion
                System.out.println("Saldo insuficiente, ¿desea realizar la operacion?(1/0)");
                boolean confirmacion = Lectura.booleanoNumerico();
                if(confirmacion == true){
                    //Si hay confirmacion paso los datos
                    ok = bloquearCuenta(cuenta, in, out);
                    if(ok==true){
                        pasarDatos(2, cuenta, cantidadCuenta-cantidad, out);
                        //borrarBuffer(in);
                        ok2 = desbloquearCuenta(cuenta, in, out);
                        if(ok2!=true){
                            desbloquearCuenta(cuenta,in,out);
                            System.out.println("Error en el servidor.\n");
                        }
                   return ok2;
                }else{
                    //Sino muestro un mensaje
                    System.out.println("Operacion carcelada con exito");
                    return false;
                }
            }
        }
    }
    return false;
}
    
    //Ingresar dinero a la cuenta
    private static boolean ingresarDinero(int cuenta, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        boolean ok,ok2;
        //Llamo a la funcion pasarDatos
        pasarDatos(1,cuenta,out);
        //Guardo el dato recibido en cantidadCuenta
        String data = in.readUTF();
        String[] datos =data.split(";");
        int result = Integer.parseInt(datos[0]);
        int cantidadCuenta = Integer.parseInt(datos[1]);
        if(result != 0){
            System.out.println("Error del servidor\n");
            return false;
        }else{
            //Si el total es mayor que cero 
            if((cantidadCuenta+cantidad)>0){
                //Pido una confirmacion
                System.out.println("La cantidad a ingresar es esta: "+cantidad+" ¿Es correcta?(1/0)");
                boolean confirmacion = Lectura.booleanoNumerico();
                //Si se confirma
                if(confirmacion == true){
                    ok = bloquearCuenta(cuenta,in,out);
                    if(ok==true){
                        //Llamo a la funcion pasarDatos
                        pasarDatos(2, cuenta, cantidad+cantidadCuenta, out);
                        //borrarBuffer(in);
                        ok2 = desbloquearCuenta(cuenta,in,out);
                        
                        if(ok2!=true){
                             System.out.print("Error al realizar la operacion.\n");
                        }
                        return ok2;
                    }else{
                        System.out.print("Error al realizar la operacion.\n");
                        desbloquearCuenta(cuenta,in,out);
                        return ok;
                    }
                }else{
                    //Sino se confirmo devuelvo un mensaje de error
                    System.out.println("Operacion carcelada\n");
                    return confirmacion;
                }
            }else{
                //Sino muestro un mensaje de error
                System.out.println("Imposible realizar esta operacion\nOperacion carcelada con exito");
                return false;
            }
        }
    }
    
    //Transferir dinero a otra cuenta
    private static boolean tranferirDinero(int cuenta, int cuenta2, int cantidad, DataInputStream in, DataOutputStream out) throws IOException{
        //Guardo el dato recibido en cantidadCuenta
        boolean ok, ok2;
        pasarDatos(1,cuenta,out);
        String data = in.readUTF();
        String[] datos =data.split(";");
        int cantidadCuenta = Integer.parseInt(datos[1]);
        int result = Integer.parseInt(datos[0]);
        if(result != 0){
            System.out.println("Error del servidor\n");
            return false;
        }else{
            //Si la cantidad total es mayor que 0
            if((cantidadCuenta-cantidad)>0){
                System.out.println("OK\n");
                //Saco el dinero de la primera cuenta y lo ingreso en la segunda cuenta
                
                ok = sacarDinero(cuenta, cantidad, in, out);
                mostrarDinero(cuenta, in, out);
                if(ok == true){
                    ok2 = ingresarDinero(cuenta2, cantidad, in, out);
                    if(ok2 != true){
                        System.out.println("Error, no se ha podido llevar a cabo la operacion de ingreso\n");
                    }
                    return ok2;
                }else{
                    System.out.println("Error, no se ha podido llevar a cabo la operacion de extraccion\n");
                    return ok;
                }
            }else{
                //Devuelvo un mensaje de error
                System.out.println("Operacion invalida, saldo insuficiente");
                return false;
            }
        }
    }
    private static void comprobarBoolean(Boolean ok){
        if(ok==true){
            System.out.println("Operacion realizada con exito.\n");
        }else{
            System.out.println("Fallo en la operacion.\n");
        }
    }
}


