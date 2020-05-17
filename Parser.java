package solver;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    Map<String,String> arg;

    public Parser(String[] args) {
        this.arg=new HashMap<>();
        for(int i = 0;i <args.length;i++){
            switch(args[i]){
                case "-in":
                    arg.put("in",args[i+1]);
                    break;
                case "-out":
                    arg.put("out",args[i+1]);
            }
        }
    }

    public String get(String key){
        return arg.get(key);
    }
}
