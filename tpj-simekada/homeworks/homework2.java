import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

enum Type {
    NUMBER, BOOLEAN, NONE
}

interface Expr {
    Type getType(Map<String, Type> gamma);
}

class Num implements Expr {
    final int value;

    Num(int value) {
        this.value = value;
        System.out.println(this.toString());
    }

    @Override
    public Type getType(Map<String, Type> gamma) {
        // if(gamma.containsValue(Type.NUMBER))
        // {
        return Type.NUMBER;
        // }
        // else
        // {
        // return Type.NONE;
        // }

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[Num](" + this.value + ")";
    }

}

class Bool implements Expr {
    final boolean value;

    Bool(boolean value) {
        this.value = value;
        System.out.println(this.toString());
    }

    @Override
    public Type getType(Map<String, Type> gamma) {
        // if(gamma.containsValue(Type.BOOLEAN))
        // {
        return Type.BOOLEAN;
        // }
        // else
        // {
        // return Type.NONE;
        // }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[Bool](" + this.value + ")";
    }
}

class Triangle implements Expr {
    final Expr subexpr;

    Triangle(Expr subexpr) {
        this.subexpr = subexpr;
        System.out.println(this.toString());
    }

    @Override
    public Type getType(Map<String, Type> gamma) {
        try {
            return subexpr.getType(gamma).equals(Type.NUMBER) ? Type.NUMBER
                    : Type.NONE;
        } catch (Exception e) {
            return Type.NONE;
        }

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[Triangle](" + this.subexpr + ")";
    }

}

class Nand implements Expr {
    final Expr e1, e2;

    Nand(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
        System.out.println(this.toString());
    }

    @Override
    public Type getType(Map<String, Type> gamma) {

        return (e1.getType(gamma).equals(Type.BOOLEAN) && e2.getType(gamma)
                .equals(Type.BOOLEAN)) ? Type.BOOLEAN : Type.NONE;

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[Nand](" + this.e1 + ", " + this.e2 + ")";
    }
}

class Ternary implements Expr {
    final Expr e1, e2, e3;

    Ternary(Expr e1, Expr e2, Expr e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        System.out.println(this.toString());
    }

    @Override
    public Type getType(Map<String, Type> gamma) {
        return (e1.getType(gamma).equals(Type.BOOLEAN)) ? ((e2.getType(gamma)
                .equals(Type.NONE)) ? Type.NONE : (e2.getType(gamma).equals(
                e3.getType(gamma)) ? e2.getType(gamma) : Type.NONE))
                : Type.NONE;
    }

    @Override
    public String toString() {
        return "[Ternary](" + this.e1 + ", " + this.e2 + ", " + this.e3 + ")";
    }
}

class Var implements Expr {
    final String name;

    Var(String name) {
        this.name = name;
        System.out.println(this.toString());
    }

    @Override
    public Type getType(Map<String, Type> gamma) {
        return (gamma.containsKey(name)) ? gamma.get(name) : Type.NONE;

    }

    @Override
    public String toString() {
        return "[Var](" + this.name + ")";
    }
}

interface Program {
    boolean isWellTyped(Map<String, Type> gamma);
}

class Assignment implements Program {
    final String varName;
    final Expr subexpr;

    Assignment(String varName, Expr subexpr) {
        this.varName = varName;
        this.subexpr = subexpr;
        System.out.println(this.toString());
    }

    @Override
    public boolean isWellTyped(Map<String, Type> gamma) {

        if (!gamma.containsKey(varName)) {
            try {
                return subexpr.getType(gamma).equals(Type.NONE) ? false : true;
            } catch (NullPointerException ex) {
                return false;
            }
        } else {
            try {
                return subexpr.getType(gamma).equals(gamma.get(varName)) ? true
                        : false;
            } catch (NullPointerException ex) {
                return false;
            }
        }

    }

    @Override
    public String toString() {

        return "[Assignment](" + this.varName + ", " + this.subexpr + ")";
    }

}

class CompoundProgram implements Program {
    final Assignment assignment;
    final Program program;

    CompoundProgram(Assignment assignment, Program program) {
        this.assignment = assignment;
        this.program = program;
        System.out.println(this.toString());
    }

    @Override
    public boolean isWellTyped(Map<String, Type> gamma) {
        if(assignment.isWellTyped(gamma))
        {
            HashMap<String, Type> gamma2 = new HashMap<String, Type>(gamma);
            gamma2.put(assignment.varName, assignment.subexpr.getType(gamma));
            ImmutableMap<String, Type> imgamma = ImmutableMap.copyOf(gamma2);           
            return program.isWellTyped(imgamma);
        }
        return false;
        
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub

        return "[Program](" + this.assignment + ", " + this.program + ")";
    }

}

public class homework2 {

    /**
     * @param arg
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // Var x = new Var(null);
        Ternary ter = new Ternary(new Bool(true), new Num(1), new Num(3));
        Map<String, Type> gamma = new HashMap<String, Type>();
        // gamma.put("y", Type.BOOLEAN);
        gamma.put("p", Type.BOOLEAN);
        // gamma.put("x", Type.NUMBER);
        // gamma.put("z", Type.NUMBER);
        // gamma.put("o", Type.NUMBER);
        // System.out.println(x.getType(gamma));
        // System.out.println(ter.getType(gamma));
        Triangle tr = new Triangle(null);
        tr.getType(gamma);
        CompoundProgram cmd = new CompoundProgram(new Assignment("s", new Num(2)), new Assignment("test", new Num(2)));
//      [Program]([Assignment](x, [Bool](true)), [Assignment](x, [Bool](false)))
        CompoundProgram program = new CompoundProgram(new Assignment("x", new Bool(true)), new Assignment("x", new Bool(false)));
        System.out.println(program.isWellTyped(gamma));

    }

}
