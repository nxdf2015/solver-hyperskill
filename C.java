package solver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class C   {
    public static  C ZERO = new C(0,0);
    public double x,y;

    public C(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static C parse(String s) {
        Pattern real = Pattern.compile("^([+-]?\\d+\\.?\\d*)");
        Pattern img = Pattern.compile("([+-]?\\d*\\.?\\d*i)$");

        Matcher realMatcher = real.matcher(s);
        Matcher imgMatcher = img.matcher(s);
        double x = 0, y = 0;
        if (imgMatcher.matches()){
            x= 0;
        }
        else if (realMatcher.find()) {
            x = Double.parseDouble(realMatcher.group(1));
        }

        imgMatcher = img.matcher(s);
        if (imgMatcher.find()) {
            String im = imgMatcher.group(1);
            System.out.println(im);
            if (im.equals("+i") || im.equals("i")) {
                y = 1;
            } else if (im.equals("-i")) {
                y = -1;
            } else {
                y = Double.parseDouble(im.substring(0, im.length() - 1));
            }

        }

        return new C(x, y);
    }


    
    public C changeSign() {
       return new C(-x,-y);
    }

    
    public boolean isZero() {
       return x== 0 && y ==0;
    }

    
    public C add(C z) {
        return new C(x + z.x , y + z.y);
    }

    public C substract(C z){
        return new C(x - z.x , y - z.y);
    }

    
    public C product(C z) {
        return new C(x * z.x - y * z.y , x* z.y + y * z.x  );
    }

    public double module(){
        return x*x + y*y;
    }

    public C diviseBy(C z) {
        double m = z.module();
        return   new C(x/ m, y/m).product(z.conj());
    }

    public C conj(){
        return new C(x,-y);
    }
    
    public int compareTo(C number) {
        return 0;
    }

    @Override
    public String toString() {

        if (Math.abs(x)!=0 && Math.abs(y)!=0){
            String sign = y < 0 ? "":"+";
            return String.format("%f%s%fi", x, sign ,y);
        }
        if (Math.abs(y)== 0){
            return String.format("%f",x);

        }
        else if (Math.abs(x)== 0 ){
            return String.format("%fi",y);
        }
        else {
            return "0";
        }
    }
}
