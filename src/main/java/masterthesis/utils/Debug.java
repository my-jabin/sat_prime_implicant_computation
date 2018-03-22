package masterthesis.utils;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Arrays;

public class Debug {

    // print debug message?
    private static boolean PRINT = true;

//    public static void println(String message){
//        println(message,true);
//    }
//
//    public static void println(String message, boolean on){
//        if(!on || !PRINT) return;
//        System.out.println(message);
//    }
//
//    public static void println(Object o){
//        println(o.toString(),true);
//    }
//
//    public static void println(Object o, boolean on){
//        println(o.toString(),on);
//    }
//
//    public static void println(){
//        println("",true);
//    }
//
//    public static void println(String header, String message){
//        println(header);
//        println(message);
//    }
//
//    public static void println(String header, Object o){
//        println(header, o.toString());
//    }
//
//    public static void println(Object o1, Object o2){
//        System.out.println(o1.toString() + o2.toString());
//    }

    public static void println(boolean on, Object... o){
        if(!on || !PRINT) return;
        Arrays.stream(o).forEach( oo -> System.out.println(oo.toString()));
    }

}
