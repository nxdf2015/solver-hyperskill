package solver;


import java.io.File;

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
