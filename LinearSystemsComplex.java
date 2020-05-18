package solver;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class LinearSystemsComplex {
    private File file;
    private C[][] equations;
    private int numberEquation;
    private int numberCol;
    private int[] cols;

    public LinearSystemsComplex(File file) {
        this.file=file;
        readFile();
    }


    public LinearSystemsComplex(String str){
        String[] lines = str.split("\n");

        equations = new C[lines.length][];
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
            equations = new C[numberEquation][];

            while((line = reader.readLine()) != null){
                equations[i++]=lineToEquation(line);
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }


    private void saveResult(File f,int lastRow){
        C[] result = getResult(lastRow);

        result = reorder(result);


//        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
//        formatSymbols.setDecimalSeparator('.');
//        DecimalFormat decimal = new DecimalFormat("####.#####");

        try(PrintWriter writer  = new PrintWriter(f)){
            System.out.println("save result");
            for(int i=0;i < numberCol;i++){
                writer.println(result[i].toString());
            }
        }
        catch ( IOException e){
            System.out.println(e);
        }
    }

    private C[] reorder(C[] result) {
        C[] temp = new C[result.length];
        for(int i = 0;i <cols.length;i++){
            temp[cols[i]]=result[i];
        }
        return temp;

    }

    public C[] lineToEquation(String line) {
        String[] ls = line.split(" ");
        C[] coeff = new C[ls.length];
        for (int i = 0; i < ls.length; i++) {
            coeff[i] = C.parse(ls[i]);
        }
        return  coeff;
    }

    public C getCoeff(int row, int col){
        return equations[row][col];
    }



    public  int findRowPivot(int column,int startRow){
        int row = startRow;
        while(row < numberEquation){

            if (!getCoeff(row,column).isZero()){
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

            C coeff=getCoeff(row,col);
            for(int i = row+1;i <numberEquation; i++ ) {
                C m = (getCoeff(i, col).diviseBy( coeff)).changeSign();
                equations[i] = RowComplex.reduceLine(equations[i], equations[row], m);
                System.out.printf("%d <- %d + %d * %s\n",i,i,row, m.toString());

            }

            col++;
        }
    }

    private Position findPivotBottom(int row, int col) {
        for(int i = row+1;i < numberEquation;i++){
            for(int j =col+1;j<numberCol;j++){
                if (!getCoeff(i,j).isZero()){
                    return Position.of(i,j);
                }
            }
        }
        return Position.empty();
    }

    private void swapCol(int colPivot, int col) {
        for(int row=0; row < numberEquation;row++){
            C t = getCoeff(row,col);
            setCol(row,col, getCoeff(row,colPivot));
            setCol(row,colPivot,t);
        }
        int i =  cols[col];
        cols[col]=cols[colPivot];
        cols[colPivot]=i;

    }

    private void setCol(int row, int col , C t) {
        equations[row][col] = t;
    }

    private int findColPivot(int col, int row) {
        for(int i=col+1;i< numberCol;i++){
            if (!getCoeff(row,col).isZero() ){
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

        if (isZero(equations[row],numberCol) && !getCoeff(row,numberCol).isZero()){
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

    private boolean isZero(C[]  equation) {
        int i = 0;

        while(i <numberCol+1){
            if (!equation[i].isZero()){
                return false;
            }
            i++;
        }
        return true;
    }

    private boolean isZero(C[] equation,int last) {
        int i = 0;
        while(i <last){
            if (!equation[i].isZero()){
                return false;
            }
            i++;
        }
        return true;
    }

    public C[]    getResult(int lastRow){


        C[] result = new C[numberCol];
        Arrays.fill(result,C.ZERO);
        for(int i=lastRow ; i >=0; i--){

            C value = C.ZERO;

            for (int col=i; col < numberCol ; col++){

               C  t = getCoeff(i,col).product(result[col]);
                value = value.add(t);
            }
            C t = getCoeff(i,numberCol ).substract(value);
            result[i] = t.diviseBy(getCoeff(i,i));


        }
        return result ;
    }

    public void swapLine(int row, int rowPivot) {
        C[] temp = equations[row];
        equations[row] = equations[rowPivot];
        equations[rowPivot] = temp;
    }

    public void addLine(int row,int rowPivot){
        equations[row] = RowComplex.add(equations[row],equations[rowPivot]);
    }

    public void addLine(int row,int rowPivot, C mult){
        equations[row] = RowComplex.reduceLine(equations[row],equations[rowPivot],mult);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("");
        for(C[] equation : equations){

            for(C coeff:equation){
                s.append(coeff.toString()+" ");
            }
            s.append(System.lineSeparator());
        }
        return s.toString();

    }
}
