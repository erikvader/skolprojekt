package calculator.parser;

import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import calculator.symbolic.*;

/**
 * Parses sequences of characters from a {@link InputStream} or a
 * {@link Reader} into a {@link Sexpr} object.
 *
 */
public class Parser{

    /**
     * Singleton object for a {@link Quit} action.
     *
     */
    public static final Sexpr QUIT = new Quit();

    /**
     * Singleton object for a {@link Vars} action.
     *
     */
    public static final Sexpr VARS = new Vars();

    private StreamTokenizer st;
    private int id;

    /**
     * Creates a parser from an {@link InputStream}.
     *
     */
    public Parser(InputStream stream){
        this(new BufferedReader(new InputStreamReader(stream)));
    }

    /**
     * Creates a parser from a {@link Reader}.
     *
     */
    public Parser(Reader reader){
        st = new StreamTokenizer(reader);
        st.ordinaryChar('-');
        st.ordinaryChar('*');
        st.ordinaryChar('/');
        st.eolIsSignificant(true);
        id = 0;
    }

    /**
     * Clears/flushes the stream until end of line.
     *
     */
    public void clear() throws IOException{
        do{
            st.nextToken();
        }while(st.ttype != StreamTokenizer.TT_EOL && st.ttype != StreamTokenizer.TT_EOF);
    }

    /**
     * Parses a statement from the stream and returns it in the form
     * of a {@link Sexpr}.
     * <p>
     * A statement is a mathematical expression or a command (quit or
     * vars). For example "3+3",
     * "6*(cos(2*2)+6-2/3)*(2+2+2+2)=a=b=hejsan" or "quit". All
     * statements have to end with EOL.
     * <p>
     * If an parse error occurs, a {@link SyntaxErrorException} is
     * thrown. A call to {@link clear()} is required to flush the
     * input.
     *
     * @return the parsed statement
     */
    public Sexpr statement() throws IOException{
        id = 0;
        Sexpr ass;
        int branch = 0;
        try{
            ass = command();
        }catch(SyntaxErrorException see){
            if(id == branch+1){
                ass = assignment();
            }else{
                throw see;
            }
        }

        st.nextToken();
        if(st.ttype == StreamTokenizer.TT_EOL){
            return ass;
        }else{
            throw new SyntaxErrorException("expected EOL");
        }
    }

    Sexpr command() throws IOException{
        id++;
        st.nextToken();
        if(st.ttype == StreamTokenizer.TT_WORD){
            String word = st.sval.toLowerCase();
            if(word.equals("quit")){
                return QUIT;
            }else if(word.equals("vars")){
                return VARS;
            }else{
                st.pushBack();
                throw new SyntaxErrorException("not a command");
            }
        }else{
            st.pushBack();
            throw new SyntaxErrorException("command is not a word");
        }
    }

    Sexpr assignment() throws IOException{
        id++;
        Sexpr exp = expression();
        while(true){
            st.nextToken();
            if(st.ttype == '='){
                exp = new Assignment(exp, identifier());
            }else{
                st.pushBack();
                break;
            }
        }
        return exp;
    }

    Sexpr expression() throws IOException{
        id++;
        Sexpr sum = term();
        while(true){
            st.nextToken();
            if(st.ttype == '+'){
                sum = new Add(sum, term());
            }else if(st.ttype == '-'){
                sum = new Sub(sum, term());
            }else{
                st.pushBack();
                //exception??
                break;
            }
        }
        return sum;
    }

    Sexpr term() throws IOException{
        id++;
        Sexpr prod = factor();
        while(true){
            st.nextToken();
            if (st.ttype == '*'){
                prod = new Mult(prod, factor());
            }else if(st.ttype == '/'){
                prod = new Div(prod, factor());
            }else{
                st.pushBack();
                // return new SyntaxErrorException("Expected '*' or '/'");
                break;
            }
        }
        return prod;
    }

    Sexpr factor() throws IOException{
        id++;
        st.nextToken();
        Sexpr value;
        if(st.ttype == '('){
            value = expression();
            st.nextToken();
            if(st.ttype != ')'){
                throw new SyntaxErrorException("expected ')'");
            }
        }else if(st.ttype == StreamTokenizer.TT_NUMBER){
            st.pushBack();
            value = number();
        }else{
            st.pushBack();
            int branch = id;
            try{
                value = unary();
            }catch(SyntaxErrorException see){
                if(id == branch+1){
                    try{
                        value = identifier();
                    }catch(SyntaxErrorException see2){
                        throw new SyntaxErrorException("Expected a factor");
                    }
                }else{
                    throw see;
                }
            }
        }
        return value;
    }

    Sexpr number() throws IOException{
        id++;
        if(st.nextToken() != StreamTokenizer.TT_NUMBER){
            st.pushBack();
            throw new SyntaxErrorException("Expected number");
        }
        return new Constant(st.nval);
    }

    Sexpr identifier() throws IOException{
        id++;
        st.nextToken();

        if(st.ttype == StreamTokenizer.TT_WORD){
            String word = st.sval.toLowerCase();
            if(word.equals("quit") || word.equals("vars")){

                st.pushBack();
                throw new SyntaxErrorException(word + " can't be an identifier");
            }
            return new Variable(st.sval);
        }else{
            st.pushBack();
            throw new SyntaxErrorException("Expected a word as an identifier");
        }
    }

    Sexpr unary() throws IOException{
        id++;
        st.nextToken();
        if(st.ttype == '-'){
            return new Negation(factor());
        }else if(st.ttype == StreamTokenizer.TT_WORD){
            String word = st.sval.toLowerCase();
            if(word.equals("exp")){
                return new Exp(factor());
            }else if(word.equals("sin")){
                return new Sin(factor());
            }else if(word.equals("cos")){
                return new Cos(factor());
            }else if(word.equals("log")){
                return new Log(factor());
            }else{
                st.pushBack();
                throw new SyntaxErrorException("\"" + word + "\" is not a valid unary math operator!");
            }
        }
        st.pushBack();
        throw new SyntaxErrorException("Expected an unary math operator or a variable name!");
    }

}
