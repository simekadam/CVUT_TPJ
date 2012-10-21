import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

interface Expr {
    Set<Expr> oneStepRewrite();
    Expr derive();
}

class Main {
    public static void main(String[] args) {

        System.out.println(new Derivative(new Num(42)));
        System.out.println(new Derivative(new X()));
        System.out.println(new Derivative(new Addition(new X(), new X())));
        System.out.println(new Derivative(new Multiplication(new X(), new X())));
        System.out.println(new Derivative(new Exponentiation(new X(),
                new Num(2))));

    }
}

class Num implements Expr {
    final int value;

    Num(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Num num = (Num) o;

        if (value != num.value)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public Set<Expr> oneStepRewrite() {
        Set<Expr> set = new HashSet<Expr>();
        set.add(this);
        return set;
    }

    @Override
    public Expr derive() {

        return new Num(0);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.value + "";
    }
}

class X implements Expr {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public Set<Expr> oneStepRewrite() {
        Set<Expr> set = new HashSet<Expr>();
        set.add(this);
        return set;
    }

    @Override
    public Expr derive() {

        return new Num(1);
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "X";
    }
}

class Addition implements Expr {
    final Expr left, right;

    Addition(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Addition addition = (Addition) o;

        if (left != null ? !left.equals(addition.left) : addition.left != null)
            return false;
        if (right != null ? !right.equals(addition.right)
                : addition.right != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public Set<Expr> oneStepRewrite() {
        // TODO Auto-generated method stub
        Set<Expr> set = new HashSet<Expr>();
            Expr expr;
            Iterator<Expr> it = left.oneStepRewrite().iterator();
            while (it.hasNext()) {
                expr = it.next();
                set.add(new Addition(expr, right));
            }
            it = right.oneStepRewrite().iterator();
            while (it.hasNext()) {
                expr = it.next();
                set.add(new Addition(left, expr));
            }
        

        return set;
    }

    @Override
    public Expr derive() {
        Expr derived = new Addition(new Derivative(left), new Derivative(right));
        return derived;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "(" + this.left.toString() + "+" + this.right.toString() + ")";
    }
}

class Multiplication implements Expr {
    final Expr left, right;

    Multiplication(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Multiplication that = (Multiplication) o;

        if (left != null ? !left.equals(that.left) : that.left != null)
            return false;
        if (right != null ? !right.equals(that.right) : that.right != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public Set<Expr> oneStepRewrite() {
        // TODO Auto-generated method stub
        Set<Expr> set = new HashSet<Expr>();
        
            Iterator<Expr> it = left.oneStepRewrite().iterator();
            Expr expr;
            while (it.hasNext()) {
                expr = it.next();
                set.add(new Multiplication(expr, right));
            }
            it = right.oneStepRewrite().iterator();
            while (it.hasNext()) {
                expr = it.next();
                set.add(new Multiplication(left, expr));
            }
        
        

        return set;
    }

    @Override
    public Expr derive() {
        Expr derived = new Addition(new Multiplication(left,
                new Derivative(right)), new Multiplication(new Derivative(left), right));
        return derived;
    }

    @Override
    public String toString() {
        return "(" + this.left.toString() + "*" + this.right.toString() + ")";
    }
}

class Exponentiation implements Expr {
    final Expr subexpr;
    final Num exponent;

    Exponentiation(Expr subexpr, Num exponent) {
        this.subexpr = subexpr;
        this.exponent = exponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Exponentiation that = (Exponentiation) o;

        if (exponent != null ? !exponent.equals(that.exponent)
                : that.exponent != null)
            return false;
        if (subexpr != null ? !subexpr.equals(that.subexpr)
                : that.subexpr != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subexpr != null ? subexpr.hashCode() : 0;
        result = 31 * result + (exponent != null ? exponent.hashCode() : 0);
        return result;
    }

    @Override
    public Set<Expr> oneStepRewrite() {
        // TODO Auto-generated method stub
        Set<Expr> set = new HashSet<Expr>();
        
            Iterator<Expr> it = subexpr.oneStepRewrite().iterator();
            while (it.hasNext()) {
                Expr expr = it.next();
                set.add(new Exponentiation(expr, exponent));
            }
        
        return set;
    }

    @Override
    public Expr derive() {
        Expr derived = new Multiplication(exponent, new Exponentiation(subexpr,
                new Num(exponent.value - 1)));
        return derived;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        Set<Expr> set = this.subexpr.oneStepRewrite();

        StringBuilder stb = new StringBuilder();
        Iterator<Expr> iterator = set.iterator();

        while (iterator.hasNext()) {
            Expr ex = iterator.next();
            // System.out.println(ex+" "+set.size()+" "+this.subexpr.getClass().getName());
            stb.append("(" + ex + "^" + this.exponent + ")");
        }

        return stb.toString();
    }
}

class Derivative implements Expr {
    final Expr subexpr;

    Derivative(Expr subexpr) {
        this.subexpr = subexpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Derivative that = (Derivative) o;

        if (subexpr != null ? !subexpr.equals(that.subexpr)
                : that.subexpr != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return subexpr != null ? subexpr.hashCode() : 0;
    }

    @Override
    public Set<Expr> oneStepRewrite() {
        // TODO Auto-generated method stub
        Set<Expr> set = new HashSet<Expr>();
        set.add(subexpr.derive());

        return set;
    }

    @Override
    public Expr derive() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        Set<Expr> set = this.oneStepRewrite();

        StringBuilder stb = new StringBuilder();
        Iterator<Expr> iterator = set.iterator();

        while (iterator.hasNext()) {

            stb.append(iterator.next());
        }

        return stb.toString();
    }
}