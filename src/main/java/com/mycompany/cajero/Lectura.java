
package com.mycompany.cajero;

import java.util.Scanner;



public class Lectura {
    public static String cadena(){
        Scanner sc = new Scanner (System.in);
        String cadena=sc.nextLine();
        return cadena;
    }

    public static int entero(){
        Scanner sc = new Scanner (System.in);
        String string=sc.nextLine();
        int entero=0;
        try {
            entero=Integer.parseInt(string);
        } 
        catch (NumberFormatException ex) 
        {
            System.out.println(ex);
        }
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
