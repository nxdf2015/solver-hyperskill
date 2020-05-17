package solver;

import javax.naming.CompositeName;
import java.io.*;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LinearSystems {

    private   File file;
    private double[][] equations;
    private int numberEquation;

    public LinearSystems(File file) {
        this.file=file;
        readFile();
    }


    public LinearSystems(String str){
        String[] lines = str.split("\n");

         equations = new double[lines.length][];
         numberEquation=equations.length;
        for(int i = 0;i < equations.length;i++ ){
            equations[i] = lineToEquation(lines[i]);
        }
    }



    private void readFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
             numberEquation= Integer.parseInt(reader.readLine());
            equations = new double[numberEquation][];

             String line;
             int i = 0;
             while((line = reader.readLine()) != null){
                 equations[i++]=lineToEquation(line);
             }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }


    public void saveResult(File f){
        double[] result = getResult();

        //format number
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat decimal = new DecimalFormat("####.#####");

        try(PrintWriter writer  = new PrintWriter(f)){
            System.out.println("save result");
            for(int i=0;i < numberEquation;i++){
                writer.println(decimal.format(result[i]));
            }
        }
        catch ( IOException e){
            System.out.println(e);
        }
    }

    private double[] lineToEquation(String line) {
        String[] ls = line.split(" ");
        double[] coeff = new double[ls.length];
        for (int i = 0; i < ls.length; i++) {
            coeff[i] = Double.parseDouble(ls[i]);
        }
        return  coeff;
    }

    public double getCoeff(int row, int col){
        return equations[row][col];
    }



    public  int findPivot(int column,int startRow){
        int row = startRow;
       while(row < numberEquation){
           if (getCoeff(row,column) != 0){
               return row;
           }
           row++;
       }
       return -1;
    }



    public void solve(){
            int col=0;
            for(int row  = 0 ; row < numberEquation;row++){
                int rowPivot=findPivot(col,row);
                if(rowPivot == -1){
                    col++;
                    continue;
                }
                if (row != rowPivot){
                    swapLine(row,rowPivot);

                   System.out.printf("%d <-> %d\n",row,rowPivot);

                }

                double coeff=getCoeff(row,col);
                for(int i = row+1;i <numberEquation; i++ ) {

                    equations[i] = Row.reduceLine(equations[i], equations[row], -getCoeff(i, col) / coeff);
                    System.out.printf("%d <- %d + %d * %f\n",i,i,row, - getCoeff(i,col)/ coeff);

                }

             col++;
            }
    }

    public double[]    getResult(){

         double[] result = new double[numberEquation];
         for(int i=numberEquation-1 ; i >=0; i--){
             double value = 0;
             for (int col=i+1 ; col < numberEquation ; col++){
                value += getCoeff(i,col) * result[col];
             }
             result[i] = (getCoeff(i,numberEquation ) - value)/ getCoeff(i,i);


         }
         return result ;
    }

    public void swapLine(int row, int rowPivot) {
        double[] temp = equations[row];
        equations[row] = equations[rowPivot];
        equations[rowPivot] = temp;
    }

    public void addLine(int row,int rowPivot){
        equations[row] = Row.add(equations[row],equations[rowPivot]);
    }

    public void addLine(int row,int rowPivot, double mult){
        equations[row] = Row.reduceLine(equations[row],equations[rowPivot],mult);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");
        for(double[] equation : equations){

                for(double coeff:equation){
                    s.append(coeff+" ");
                }
                s.append(System.lineSeparator());
        }
        return s.toString();

    }
}
