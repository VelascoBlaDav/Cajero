package com.mycompany.cajero;

import java.util.Scanner;

public class Lectura {
    public static String cadena(){
        //Pido por pantalla el valor
        Scanner sc = new Scanner (System.in);
        String cadena=sc.nextLine();
        //Devuelvo el valor pedido por pantalla
        return cadena;
    }

    public static int entero(){
        Scanner sc = new Scanner (System.in);
        //Pido por pantalla el valor
        String string=sc.nextLine();
        int entero=0;
        try{
            //Cambio el valor a un entero
            entero=Integer.parseInt(string);
        }catch (NumberFormatException ex){
            //Si no es un entero devuelvo error
            System.out.println(ex);
        }
        //Devuelvo el valor pedido por pantalla
        return entero;
    }
    
    public static boolean booleanoNumerico(){
        int entero=entero();
        if(entero==1){
            return true;
        }else{
            return false;
        }
    }

}
