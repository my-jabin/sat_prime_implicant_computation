package masterthesis.utils;

import com.sun.org.apache.xpath.internal.SourceTree;
import masterthesis.base.Literal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    public static void printWeight(Map<Literal, Integer> weight) {
        Set set = weight.entrySet();
        Iterator iterator = set.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            sb.append(entry.getKey()).append("(").append(entry.getValue()).append("), ");
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        Debug.println(true, sb.toString());
    }

}
