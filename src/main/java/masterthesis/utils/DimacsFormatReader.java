package masterthesis.utils;

import masterthesis.base.ApplicationContext;

import java.io.*;
import java.util.ArrayList;

public class DimacsFormatReader {

    private final File file;

    public DimacsFormatReader(String filename) {
        this(new File(filename));
    }

    public DimacsFormatReader(File file) {
        this.file = file;
    }

    public static void read(String fileName){
        read(new File(fileName));
    }

    public static void read(File file){
        if(file == null)
            return;
        try {
            ApplicationContext ac = ApplicationContext.getInstance();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()){
                final String line = reader.readLine();
                if (line.startsWith("p cnf"))
                    break;
            }
            String[] splites;
            while (reader.ready()){
                splites = reader.readLine().split("\\s+");
                if(splites.length >= 2){
                    final ArrayList<Integer> cnfContent = new ArrayList<>();
                    for(int i = 0 ; i < splites.length - 1; i++){
                        if(!splites[i].isEmpty()){
                            Integer parsedLiteral = Integer.parseInt(splites[i]);
                            cnfContent.add(parsedLiteral);
                        }
                    }
                    ac.addCNFContent(cnfContent);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void readCNF(final ApplicationContext ac ){
//        if(this.file == null)
//            return;
//        try {
//            //ac.setFileAbsoluteName(this.file.getAbsolutePath());
//            BufferedReader reader = new BufferedReader(new FileReader(this.file));
//            while (reader.ready()){
//                final String line = reader.readLine();
//                if (line.startsWith("p cnf"))
//                    break;
//            }
//            String[] splites;
//            while (reader.ready()){
//                splites = reader.readLine().split("\\s+");
//                if(splites.length >= 2){
//                    final ArrayList<Integer> cnfContent = new ArrayList<>();
//                    for(int i = 0 ; i < splites.length - 1; i++){
//                        if(!splites[i].isEmpty()){
//                            Integer parsedLiteral = Integer.parseInt(splites[i]);
//                            cnfContent.add(parsedLiteral);
//                        }
//                    }
//                    ac.addCNFContent(cnfContent);
//                }
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//    }
}
