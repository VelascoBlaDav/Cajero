
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
            cuenta = Lectura.entero();
            int opc, cantidad, cuenta2, cantidadCuenta;
            String mensaje;
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
                        //Envio la cuenta al cliente
                        out.writeInt(cuenta);  
                        //Recibo el mensaje del servidor
                        mensaje = in.readUTF();
                        System.out.println(mensaje);

                        break;
                    case 2:
                        //Pido la cantidad a sacar
                        cantidad = Lectura.entero();
                        //envio la cuenta
                        out.writeInt(cuenta);
                        mensaje = in.readUTF();
                        cantidadCuenta = Integer.parseInt(mensaje);
                        if((cantidadCuenta-cantidad)>0){
                            out.writeInt(cantidadCuenta-cantidad);
                        }else{
                            System.out.println("Saldo insuficiente, ¿desea realizar la operacion?(1/0)");
                            confirmacion = Lectura.booleanoNumerico();
                            if(confirmacion == true){
                                out.writeInt(cantidadCuenta-cantidad);
                            }else{
                                System.out.println("Operacion carcelada con exito");
                            }
                        }
                        break;
                    case 3:

                        do{
                            //Pido la cantidad a ingresar 
                            cantidad = Lectura.entero();
                            //envio la cuenta
                            out.writeInt(cuenta);
                            mensaje = in.readUTF();
                            cantidadCuenta = Integer.parseInt(mensaje);
                            System.out.println("La cantidad a ingresar es esta: "+cantidad+" ¿Es correcta?(1/0)");
                            confirmacion = Lectura.booleanoNumerico();
                            if(confirmacion == true){
                                out.writeInt(cantidadCuenta+cantidad);
                            }
                        }while(confirmacion != true);
                        break;
                    case 4:
                        //Pido la cantidad a ingresar 
                        cantidad = Lectura.entero();
                        //Pido la cuenta a ingresar el dinero
                        cuenta2 = Lectura.entero();
                        //envio la cuenta
                        out.writeInt(cuenta);
                        mensaje = in.readUTF();
                        cantidadCuenta = Integer.parseInt(mensaje);
                        if((cantidadCuenta-cantidad)>0){
                            out.write(cuenta2);
                            out.write(cantidad);
                            mensaje = in.readUTF();
                            System.out.println(mensaje);
                        }else{
                            System.out.println("Operacion invalida, saldo insuficiente");
                        }
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
}
