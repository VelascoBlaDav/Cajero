
package com.mycompany.cajero;

import java.io.*;
import java.net.*;

public class Conection {
        public static void Main(String[] args) {
        int cuenta;
        //Pedimos lo datos de la cuenta y llamamos a la funcion buscarCliente
        //para comprobar si la cuenta existe
        cuenta = Lectura.entero();
        cuenta = Funciones.buscarCliente(cuenta);
        Funciones.menu(cuenta);
        Socket s = null;
    try{
        int serverPort = 8888;
        s = new Socket("localhost", serverPort);    
        DataInputStream in = new DataInputStream( s.getInputStream());
        DataOutputStream out =new DataOutputStream( s.getOutputStream());
        out.writeInt(cuenta);  
    }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
    }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
    }catch (IOException e){System.out.println("readline:"+e.getMessage());
    }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
}
