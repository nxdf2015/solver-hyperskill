package solver;

import javafx.geometry.Pos;

import java.util.Optional;

public class Position  {
    public Integer col;
    public Integer row;

    public static Position of(int row,int col){
        return new Position(row,col);
    }

    public static Position empty(){
        return new Position();
    }

    public Position() {
    }

    private Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public boolean isEmpty() {
        return col == null && row == null;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getRow() {
        return row;
    }
}
