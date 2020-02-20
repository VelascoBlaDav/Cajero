
package com.mycompany.cajero;

import static com.mycompany.cajero.Funciones.buscarCliente;
import static com.mycompany.cajero.Funciones.ingresarDinero;
import static com.mycompany.cajero.Funciones.sacarDinero;
import static com.mycompany.cajero.Funciones.transferencia;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cajero {
    public static void Main(String[] args) {
        int cuenta;
        //Pedimos lo datos de la cuenta y llamamos a la funcion buscarCliente
        //para comprobar si la cuenta existe
        Socket s = null;
    try{
        int serverPort = 8888;
        //Creo el socket para conectarme con el servidor
        s = new Socket("localhost", serverPort); 
        DataInputStream in = new DataInputStream( s.getInputStream());
        DataOutputStream out =new DataOutputStream( s.getOutputStream());
        cuenta = Lectura.entero();
        int opc, cantidad, cuenta2;
        
        System.out.println("\nBienvenido\n");
        System.out.println("Seleccione una opcion:\n");
        System.out.println("1) Consultar saldo:\n");
        System.out.println("2) Sacar dinero:\n");
        System.out.println("3) Ingresar dinero:\n");
        System.out.println("4) Transferencia entre cuentas:\n");
        System.out.println("5) Cambiar de cuenta:\n");
        opc = Lectura.entero();
        
        do{
            switch(opc){
                case 1:
                    //Envio la cuenta al cliente
                    out.writeInt(cuenta);  
                    //Recibo el mensaje del servidor
                    String mensaje = in.readUTF();
                    System.out.println(mensaje);
                    
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
                case 5:
                    cuenta = Lectura.entero();
                    break;
                case 0:
                    break;
            }
        }while(opc!=0);
        
        //Envio la cuenta al cliente
        out.writeInt(cuenta);  
        //Recibo el mensaje del servidor
        String mensaje = in.readUTF();
        System.out.println(mensaje);
        //Termino el servicio del cliente
        s.close();
    }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
    }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
    }catch (IOException e){System.out.println("readline:"+e.getMessage());
    }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
}
