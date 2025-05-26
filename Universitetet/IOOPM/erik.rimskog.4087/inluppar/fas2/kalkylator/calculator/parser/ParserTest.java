package calculator.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import calculator.parser.Parser;
import calculator.symbolic.*;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ParserTest {

    private Parser createParser(String s){
        return new Parser(new ByteArrayInputStream(s.getBytes()));
    }
    
    @Test
    public void testNumber() {
        Parser p;
        Sexpr expected;
        try{
            p = createParser("1");
            expected = new Constant(1);
            assertEquals(p.number(), expected);

            p = createParser("0");
            expected = new Constant(0);
            assertEquals(p.number(), expected);

            boolean error = false;
            p = createParser("asd");
            try{
                p.number();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);

            p = createParser("0.1");
            expected = new Constant(0.1);
            assertEquals(p.number(), expected);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void testIdentifier() {
        Parser p;
        Sexpr expected;
        try{
            p = createParser("a");
            expected = new Variable("a");
            assertEquals(p.identifier(), expected);

            p = createParser("A");
            expected = new Variable("A");
            assertEquals(p.identifier(), expected);

            p = createParser("hejsan");
            expected = new Variable("hejsan");
            assertEquals(p.identifier(), expected);

            boolean error = false;
            p = createParser("2");
            try{
                p.identifier();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);

            p = createParser("he2a");
            expected = new Variable("he2a");
            assertEquals(p.identifier(), expected);

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void testUnary() {
        Parser p;
        Sexpr expected;
        try{
            p = createParser("-2");
            expected = new Negation(new Constant(2));
            assertEquals(p.unary(), expected);

            p = createParser("Cos(2)");
            expected = new Cos(new Constant(2));
            assertEquals(p.unary(), expected);

            p = createParser("Cos 2");
            expected = new Cos(new Constant(2));
            assertEquals(p.unary(), expected);

            boolean error = false;
            p = createParser("hej");
            try{
                p.unary();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void testFactor() {
        Parser p;
        Sexpr expected;
        try{
            p = createParser("(3+3)");
            expected = new Add(new Constant(3), new Constant(3));
            assertEquals(p.factor(), expected);

            p = createParser("3");
            expected = new Constant(3);
            assertEquals(p.factor(), expected);

            p = createParser("a");
            expected = new Variable("a");
            assertEquals(p.factor(), expected);

            p = createParser("sin(2)");
            expected = new Sin(new Constant(2));
            assertEquals(p.factor(), expected);

            boolean error = false;
            p = createParser("(sin(2)");
            try{
                p.factor();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);

            error = false;
            p = createParser("+");
            try{
                p.factor();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void testTerm() {
        Parser p;
        Sexpr expected;
        try{

            p = createParser("3*3");
            expected = new Mult(new Constant(3), new Constant(3));
            assertEquals(p.term(), expected);

            p = createParser("(1+2)*3");
            expected = new Mult(new Add(new Constant(1), new Constant(2)), new Constant(3));
            assertEquals(p.term(), expected);

            p = createParser("3*3*3*3");
            expected = new Mult(new Mult(new Mult(new Constant(3), new Constant(3)), new Constant(3)), new Constant(3));
            assertEquals(p.term(), expected);

            p = createParser("3/3*2");
            expected = new Mult(new Div(new Constant(3), new Constant(3)), new Constant(2));
            assertEquals(p.term(), expected);

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void testExpression() {
        Parser p;
        Sexpr expected;
        try{

            p = createParser("3+3");
            expected = new Add(new Constant(3), new Constant(3));
            assertEquals(p.expression(), expected);

            p = createParser("(1*2)+3");
            expected = new Add(new Mult(new Constant(1), new Constant(2)), new Constant(3));
            assertEquals(p.expression(), expected);

            p = createParser("3+3+3+3");
            expected = new Add(new Add(new Add(new Constant(3), new Constant(3)), new Constant(3)), new Constant(3));
            assertEquals(p.expression(), expected);

            p = createParser("3-3+2");
            expected = new Add(new Sub(new Constant(3), new Constant(3)), new Constant(2));
            assertEquals(p.expression(), expected);

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void testAssignment() {
        Parser p;
        Sexpr expected;
        try{

            p = createParser("3+3=a");
            expected = new Assignment(new Add(new Constant(3), new Constant(3)), new Variable("a"));
            assertEquals(p.assignment(), expected);

            p = createParser("3+3=a=b");
            expected = new Assignment(new Assignment(new Add(new Constant(3), new Constant(3)), new Variable("a")), new Variable("b"));
            assertEquals(p.assignment(), expected);

            boolean error = false;
            p = createParser("a=2");
            try{
                p.assignment();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
   
    @Test
    public void testCommand() {
        Parser p;
        Sexpr expected;
        try{

            p = createParser("quit");
            expected = Parser.QUIT;
            assertTrue(p.command() == expected);

            p = createParser("QuIt");
            expected = Parser.QUIT;
            assertTrue(p.command() == expected);

            boolean error = false;
            p = createParser("a");
            try{
                p.command();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
   
    @Test
    public void testStatement() {
        Parser p;
        Sexpr expected;
        try{

            p = createParser("3+(2*4)\n");
            expected = new Add(new Constant(3), new Mult(new Constant(2), new Constant(4)));
            assertEquals(p.statement(), expected);

            p = createParser("Sin(2+Cos(3*4))=b=a\n");
            expected = new Assignment(new Assignment(new Sin(new Add(new Constant(2), new Cos(new Mult(new Constant(3), new Constant(4))))), new Variable("b")), new Variable("a"));
            assertEquals(p.statement(), expected);

            // p = createParser("a");
            // expected = new Variable("a");
            // assertEquals(p.statement(), expected);

            // p = createParser("sin(2)");
            // expected = new Sin(new Constant(2));
            // assertEquals(p.statement(), expected);

            boolean error = false;
            p = createParser("(sin(2))");
            try{
                p.statement();
            }catch(SyntaxErrorException see){
                error = true;
            }
            assertTrue(error);

            // error = false;
            // p = createParser("+");
            // try{
            //     p.statement();
            // }catch(SyntaxErrorException see){
            //     error = true;
            // }
            // assertTrue(error);
        
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
   


}
