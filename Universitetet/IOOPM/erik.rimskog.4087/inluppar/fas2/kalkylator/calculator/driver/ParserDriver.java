package calculator.driver;

import java.io.IOException;
import calculator.parser.Parser;
import calculator.parser.SyntaxErrorException;
import calculator.symbolic.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

//TODO: JUnit
//TODO: point to where to error occured
//TODO: custom tokenizer
//TODO: eval

class ParserDriver{
    public static void main(String[] args){
        Parser p = new Parser(System.in);
        HashMap<String, Sexpr> vars = new HashMap<String, Sexpr>();
        boolean running = true;

        System.out.println("Welcome to the parser!");

        while(running){
            System.out.print("Please enter an expression: ");

            try{
                try{
                    Sexpr result = p.statement();
                    if(result == Parser.QUIT){
                        running = false;
                        break;
                    }else if(result == Parser.VARS){
                        printVars(vars);
                    }else{
                        System.out.println("result: " + result.eval(vars));
                        // System.out.println("result: " + result);
                    }
                    System.out.println("");
                }catch(SyntaxErrorException e){
                    System.out.print("Syntax Error: ");
                    System.out.println(e.getMessage());
                    System.out.println();
                    p.clear();
                }catch(EvalException ee){
                    System.out.print("Evaluation error: ");
                    System.out.println(ee.getMessage());
                    System.out.println();
                }
            }catch(IOException e){
                System.err.println("IO Exception!");
                running = false;
                break;
            }
        }
    }

    private static void printVars(HashMap<String, Sexpr> vars){
        if(vars.entrySet().isEmpty()){
            System.out.println("There aren't any variables :(");
        }else{
            boolean first = true;
            String v = "Variables: ";
            String pad = String.join("", Collections.nCopies(v.length(), " "));
            System.out.print(v);
            for(Map.Entry<String, Sexpr> kv : vars.entrySet()){
                if(first){
                    first = false;
                }else{
                    System.out.print(pad);
                }
                System.out.println(kv.getKey() + " = " + kv.getValue());
            }
        }
    }
}
