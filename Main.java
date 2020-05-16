package solver;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] n = scanner.nextLine().split(" ");
        double x = Double.parseDouble(n[0]);
        double  y = Double.parseDouble(n[1]);
        System.out.println(y/x);
    }
}
