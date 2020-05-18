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
    private int numberCol;
    private int[] cols;

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


             String line;
             int i = 0;
             String[] sizes = reader.readLine().split(" ");
            numberEquation= Integer.parseInt(sizes[1]);
            numberCol= Integer.parseInt(sizes[0]);
            cols=new int[numberCol];

            for(int j = 0;j <cols.length;j++){
                cols[j]=j;
            }
            equations = new double[numberEquation][];

            while((line = reader.readLine()) != null){
                 equations[i++]=lineToEquation(line);
             }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }


    private void saveResult(File f,int lastRow){
        double[] result = getResult(lastRow);

        result = reorder(result);

        //format number
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat decimal = new DecimalFormat("####.#####");

        try(PrintWriter writer  = new PrintWriter(f)){
            System.out.println("save result");
            for(int i=0;i < numberCol;i++){
                writer.println(decimal.format(result[i]));
            }
        }
        catch ( IOException e){
            System.out.println(e);
        }
    }

    private double[] reorder(double[] result) {
        double[] temp = new double[result.length];
        for(int i = 0;i <cols.length;i++){
            temp[cols[i]]=result[i];
        }
        return temp;

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



    public  int findRowPivot(int column,int startRow){
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
                int rowPivot=findRowPivot(col,row);
                if(rowPivot == -1){
                   int colPivot =findColPivot(col,row);
                   if (colPivot != -1){
                       swapCol(colPivot,col);
                       System.out.printf("col %d <-> %d\n",col,colPivot);
                   }
                   else{
                       Position position = findPivotBottom(row,col);

                       if (position.isEmpty()){
                           break;
                       }
                       else {
                           swapCol(col , position.getCol());
                           swapLine(row, position.getRow());
                           System.out.printf("row %d <-> %d\n  | col %d <-> %d ",row,position.getCol(),col,position.getCol());

                       }
                   }

                }
                else if (row != rowPivot){
                    swapLine(row,rowPivot);

                   System.out.printf("row %d <-> %d\n",row,rowPivot);

                }

                double coeff=getCoeff(row,col);
                for(int i = row+1;i <numberEquation; i++ ) {
                    double m = - getCoeff(i, col) / coeff;
                    equations[i] = Row.reduceLine(equations[i], equations[row], m);
                    System.out.printf("%d <- %d + %d * %f\n",i,i,row, m);

                }

             col++;
            }
    }

    private Position findPivotBottom(int row, int col) {
        for(int i = row+1;i < numberEquation;i++){
            for(int j =col+1;j<numberCol;j++){
                if (getCoeff(i,j) != 0){
                    return Position.of(i,j);
                }
            }
        }
        return Position.empty();
    }

    private void swapCol(int colPivot, int col) {
        for(int row=0; row < numberEquation;row++){
            double t = getCoeff(row,col);
            setCol(row,col, getCoeff(row,colPivot));
            setCol(row,colPivot,t);
        }
        int i =  cols[col];
        cols[col]=cols[colPivot];
        cols[colPivot]=i;

    }

    private void setCol(int row, int col , double t) {
        equations[row][col] = t;
    }

    private int findColPivot(int col, int row) {
        for(int i=col+1;i< numberCol;i++){
            if (getCoeff(row,col) != 0 ){
                return col;
            }
        }
        return -1;
    }

    public void save(File file){
        int row = findNonZeroRow();

        if (nonExistSolution(row)){
            saveString("No solutions",file);
        }
        else if (row < numberCol -1){
            saveString("Infinitely many solutions",file);
        }
        else {

           saveResult(file,row);
        }
    }

    private void saveString(String message, File file) {
        try(PrintWriter writer = new PrintWriter((file))){
            writer.println(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean nonExistSolution(int row) {

        if (isZero(equations[row],numberCol) && getCoeff(row,numberCol) != 0){
            return true;
        }
        else{
            return false;
        }
    }

    private int findNonZeroRow() {
        int row = numberEquation-1;
        while( row >= 0){
            if (!isZero(equations[row])){
                return row;
            }
            row--;
        }
        return -1;
    }

    private boolean isZero(double[] equation) {
       int i = 0;

       while(i <numberCol+1){
           if (equation[i] != 0){
               return false;
           }
           i++;
       }
       return true;
    }

    private boolean isZero(double[] equation,int last) {
        int i = 0;
        while(i <last){
            if (equation[i] != 0){
                return false;
            }
            i++;
        }
        return true;
    }

    public double[]    getResult(int lastRow){


         double[] result = new double[numberCol];
         for(int i=lastRow ; i >=0; i--){
             double value = 0;
             for (int col=i; col < numberCol ; col++){
                value += getCoeff(i,col) * result[col];
             }
             result[i] = (getCoeff(i,numberCol ) - value)/ getCoeff(i,i);


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
