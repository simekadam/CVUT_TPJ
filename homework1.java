import java.util.Set;
 
interface Expr {
    Set<Expr> oneStepRewrite();
}
 
class Num implements Expr {
    final int value;
 
    Num(int value) {
        this.value = value;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Num num = (Num) o;
 
        if (value != num.value) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        return value;
    }

    @Override 
    Set<Expr> oneStepRewrite(){
        Set<Expr> set = Set<Expr>();
        set.add(this);
        return set;
    }
}
 
class X implements Expr {
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        return 1;
    }
    
    @Override 
    Set<Expr> oneStepRewrite(){
        Set<Expr> set = Set<Expr>();
        set.add(this);
        return set;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Addition addition = (Addition) o;
 
        if (left != null ? !left.equals(addition.left) : addition.left != null) return false;
        if (right != null ? !right.equals(addition.right) : addition.right != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
    
    @Override
    public Set<Expr> oneStepRewrite(){
        Set<Expr> s = new HashSet<Expr>();
        
            Iterator it1 = left.oneStepRewrite().iterator();
            while(it.hasNext()){
                s.add(new Addition(it.next(), right));
            }
        
        
            Iterator<Expr> it2 = right.oneStepRewrite().iterator();        
            while(it.hasNext()){
                s.add(new Addition(left, it.next()));
            }
       
        return s;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Multiplication that = (Multiplication) o;
 
        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        if (right != null ? !right.equals(that.right) : that.right != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
    
    @Override
    public Set<Expr> oneStepRewrite(){
        Set<Expr> s = new HashSet<Expr>(); 
        if(!left.getClass().getName().equals("Num") && !left.getClass().getName().equals("X"))       
        {
            Iterator<Expr> it = left.oneStepRewrite().iterator();
            while(it.hasNext()){
                s.add(new Addition(it.next(), right));
            }
        }
        else if(!right.getClass().getName().equals("Num") && !right.getClass().getName().equals("X"))
        {
            Iterator<Expr> it = right.oneStepRewrite().iterator();        
            while(it.hasNext()){
                s.add(new Addition(left, it.next()));
            }
        }
        else
        {
            s.add(new Multiplication(left, right));
        }
        return s;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Exponentiation that = (Exponentiation) o;
 
        if (exponent != null ? !exponent.equals(that.exponent) : that.exponent != null) return false;
        if (subexpr != null ? !subexpr.equals(that.subexpr) : that.subexpr != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        int result = subexpr != null ? subexpr.hashCode() : 0;
        result = 31 * result + (exponent != null ? exponent.hashCode() : 0);
        return result;
    }
    
    @Override
    public Set<Expr> oneStepRewrite(){
       Set<Expr> s = new Set<Expr>();
       Iterator<Expr> it = subexpr.oneStepRewrite().iterator();
       while(it.hasNext()){
                s.add(new Exponentiation(it.next(), exponent));
            }
        return s;
    }
}
 
class Derivative implements Expr {
    final Expr subexpr;
 
    Derivative(Expr subexpr) {
        this.subexpr = subexpr;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Derivative that = (Derivative) o;
 
        if (subexpr != null ? !subexpr.equals(that.subexpr) : that.subexpr != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() {
        return subexpr != null ? subexpr.hashCode() : 0;
    }
    
     @Override
    public Set<Expr> oneStepRewrite(){
        Set<Expr> s = new HashSet<Expr>();
        if(subexpr instanceof Num)
        s.add(new Num(0));
        if(subexpr instanceof X){}
        s.add(new Num(1));
        if(subexpr instanceof Addition)
        {
            Addition tmp = (Addition) subexpr;
            s.add(new Addition(new Derivative(tmp.left), new Derivative(tmp.right)));
        }
        if(subexpr instanceof Multiplication)
        {
            Multiplication tmp = (Multiplication) subexpr;
            s.add(new Addition(new Multiplication(new Derivative(tmp.left), tmp.right),new Multiplication(tmp.left,new Derivative(tmp.right))));
        }
        if(subexpr instanceof Exponentiation)
        {
            Exponentiation tmp = (Exponentiation) subexpr;
            s.add(new Multiplication(tmp.exponent, new Exponentiation(tmp.subexpr, new Num(tmp.exponent.value - 1))));
        }
        return s;
    }
}