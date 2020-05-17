package solver;

public class Row {

    public static double[] multiply(double[] equation  , double m){
        double[] result = new double[equation.length];
        for(int i =0 ; i < equation.length;i++){
            result[i] = m * equation[i];
        }
        return result;
    }
    public static double[] add(double[] left, double[] right){
        double[] result = new double[left.length];
        for(int i = 0 ; i <left.length; i++){
            result[i] = left[i] + right[i];
        }
        return result;
    }


    public static double[]  reduceLine(double[] equation, double[] equation1, double value) {

         return add(equation,multiply(equation1,value));
         
    }
}
