package solver;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

//    static Scanner scanner = new Scanner(System.in);
//    public static  Supplier<List<Double>> getValues = () ->  Arrays.stream(scanner.nextLine().split(" "))
//                           .map(Double::parseDouble)
//                           .collect(Collectors.toList());
//
   public static void main(String[] args) {
             Parser parser = new Parser(args);
             String in = parser.get("in");
             String out = parser.get("out");
             LinearSystemsComplex  linearSystems = new LinearSystemsComplex(new File(            in));
        System.out.println(linearSystems);
            linearSystems.solve();
           System.out.println(linearSystems);
           linearSystems.save(new File(        out));


   }
}
