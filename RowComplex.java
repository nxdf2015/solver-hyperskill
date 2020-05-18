package solver;

public class RowComplex {
    public static C[] multiply(C[] equation  , C m){
        C[] result = new C[equation.length];
        for(int i =0 ; i < equation.length;i++){
            result[i] = m.product(equation[i]);
        }
        return result;
    }
    public static C[] add(C[] left, C[] right){
        C[] result = new C[left.length];
        for(int i = 0 ; i <left.length; i++){
            result[i] = left[i].add(right[i]);
        }
        return result;
    }


    public static C[]  reduceLine(C[] equation, C[] equation1, C value) {

        return add(equation,multiply(equation1,value));

    }
}
