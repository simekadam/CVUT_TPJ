import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
 
// class Environment {
//     static boolean isWellFormedEnvironment(ImmutableMap<String, Type> environment) {
//         if (environment.isEmpty()) return true;
//         for (final String var : environment.keySet()) {
//             if (environment.get(var).isWellFormedTypeIn(ImmutableMap.copyOf(Maps.filterEntries(environment, new Predicate<Map.Entry<String, Type>>() {
//                 @Override
//                 public boolean apply(Map.Entry<String, Type> entry) {
//                     return !entry.getKey().equals(var);
//                 }
//             })))) return true;
//         }
//         return false;
//     }
 
//     static boolean isSubtypeOf(String varName, Type t, ImmutableMap<String, Type> environment) {
//         if (new TypeVariable(varName).equalsTo(t)) return true;
//         for (String v : environment.keySet())
//             if (varName.equals(v) && (t.equalsTo(environment.get(v)) || environment.get(v).isSubtypeOf(t, environment))) return true;
//         return false;
//     }
// }
 
 
interface Type {
    boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment);
    boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment);
    Type replaceTypeVariable(String replacedName, Type replacement);
    boolean equalsTo(Type t);
}
 
 
class TopType implements Type {

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		return Environment.isWellFormedEnvironment(environment);
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		return t instanceof TopType;
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		return t instanceof TopType;
	}
    // complete this

	@Override
	public String toString() {
		return "TopType []";
	}
}
 
class TypeVariable implements Type {
    final String name;
 
    TypeVariable(String name) {
        this.name = name;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		if(environment.containsKey(this.name))
		{
			return Environment.isWellFormedEnvironment(environment);
		}
		return false;
		}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub	
		
		if(this.isWellFormedTypeIn(environment) && t.isWellFormedTypeIn(environment) && t.equalsTo(new TopType())) return true;
		if(this.equalsTo(t))
		{
			
			return true;
		}
		try{
			TypeVariable tmp = (TypeVariable) t;
			
			if(environment.containsKey(this.name) && Environment.isWellFormedEnvironment(environment)){
				return environment.get(this.name).isSubtypeOf(t, environment);
			}
			return false;
			
		}
		catch(ClassCastException ex)
		{
			return false;
		}
		
		
	
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return (this.name == replacedName) ? replacement : this;
			
		
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		
			return t instanceof TypeVariable && ((TypeVariable) t).name == this.name;
		
		
	}

	@Override
	public String toString() {
		return "TypeVariable [name=" + name + "]";
	}
 
    // complete this
}
 
class FunctionType implements Type {
    final Type domain;
    final Type range;
 
    FunctionType(Type domain, Type range) {
        this.domain = domain;
        this.range = range;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		
		return domain.isWellFormedTypeIn(environment) && range.isWellFormedTypeIn(environment);
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		if(this.isWellFormedTypeIn(environment) && t.equalsTo(new TopType())) return true;
		try
		{
			FunctionType tmp = (FunctionType) t;
			return tmp.domain.isSubtypeOf(this.domain, environment) && this.range.isSubtypeOf(tmp.range, environment);
		}
		catch(ClassCastException ex){
			return false;
		}
		
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return new FunctionType(this.domain.replaceTypeVariable(replacedName, replacement), this.range.replaceTypeVariable(replacedName, replacement));
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		return t instanceof FunctionType && (((FunctionType) t).domain.equalsTo(this.domain) && ((FunctionType) t).range.equalsTo(this.range));
	}

	@Override
	public String toString() {
		return "FunctionType [domain=" + domain + ", range=" + range + "]";
	}
 
    // complete this
	
	
}
 
class ProductType implements Type {
    final Type first;
    final Type second;
 
    ProductType(Type first, Type second) {
        this.first = first;
        this.second = second;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		return this.first.isWellFormedTypeIn(environment) && this.second.isWellFormedTypeIn(environment);
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		if(this.isWellFormedTypeIn(environment) && t.equalsTo(new TopType())) return true;
		try {
			ProductType tmp = (ProductType) t;
			return this.first.isSubtypeOf(tmp.first, environment) && this.second.isSubtypeOf(tmp.second, environment);	
		} catch (ClassCastException ex) {
			return false;
		}
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return new ProductType(this.first.replaceTypeVariable(replacedName, replacement), this.second.replaceTypeVariable(replacedName, replacement));
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		return t instanceof ProductType && (((ProductType) t).first.equalsTo(this.first) && ((ProductType) t).second.equalsTo(this.second));
	}

	@Override
	public String toString() {
		return "ProductType [first=" + first + ", second=" + second + "]";
	}
 
    // complete this
}
 
class RecordType implements Type {
    final ImmutableMap<String, Type> labelsMap;
 
    RecordType(ImmutableMap<String, Type> labelsMap) {
        this.labelsMap = labelsMap;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		for(final String s : this.labelsMap.keySet())
		{
			if(!this.labelsMap.get(s).isWellFormedTypeIn(environment)) return false;
		}
		return true;
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		if(this.isWellFormedTypeIn(environment) && t.isWellFormedTypeIn(environment) && t.equalsTo(new TopType())) return true;
		if(this.equalsTo(t)) return true;
//		if(!this.isWellFormedTypeIn(environment) || !t.isWellFormedTypeIn(environment)) return false;
		try {
			RecordType tmp = (RecordType) t;
			if(tmp.labelsMap.size() > this.labelsMap.size()) return false;
			for(final String s : this.labelsMap.keySet())
			{
				if(!tmp.labelsMap.containsKey(s) && !this.labelsMap.get(s).isWellFormedTypeIn(environment)) return false;
			}
			for(final String s : tmp.labelsMap.keySet())
			{
				if(!this.labelsMap.containsKey(s) || !this.labelsMap.get(s).isSubtypeOf(tmp.labelsMap.get(s), environment)) return false;
				
//				if(!this.labelsMap.get(s).isWellFormedTypeIn(environment)) return false;
			}
			return true;
			
		} 
		catch (ClassCastException e)
		{
			// TODO: handle exception
			return false;
		}
		
		
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		Map<String, Type> map = new HashMap<String, Type>();
		
		RecordType replacedType = new RecordType(labelsMap);
		return replacedType;
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		try
		{
			RecordType tmp = (RecordType) t;
			if(tmp.labelsMap.size() != this.labelsMap.size()) return false;
			
			for(final String s : this.labelsMap.keySet())
			{
				if(!tmp.labelsMap.containsKey(s) || !tmp.labelsMap.get(s).equalsTo(this.labelsMap.get(s))) return false;
			}
			return true;
		}
		catch(ClassCastException ex)
		{
			return false;
		}
	}

	@Override
	public String toString() {
		return "RecordType [labelsMap=" + labelsMap + "]";
	}
	
//	@Override
//	public String toString() {
//		// TODO Auto-generated method stub
//		StringBuilder sb = new StringBuilder();
//		sb.append("RecordType: ");
//		for(final String s : this.labelsMap.keySet())
//		{
//			sb.append(s+":: "+this.labelsMap.get(s)+", ");
//		}
//		return sb.toString();
//	}
 
    // complete this
}
 
class ReferenceType implements Type {
    final Type innerType;
 
    ReferenceType(Type innerType) {
        this.innerType = innerType;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		return this.innerType.isWellFormedTypeIn(environment);
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		
		if(this.isWellFormedTypeIn(environment) && t instanceof TopType) return true;
		try
		{
			ReferenceType tmp = (ReferenceType) t;
			
			return tmp.equalsTo(this);
		}
		catch(ClassCastException ex)
		{
			return false;
		}
		
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return new ReferenceType(this.innerType.replaceTypeVariable(replacedName, replacement));
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		try
		{
			
			return ((ReferenceType) t).innerType.equalsTo(this.innerType); 
		}
		catch(ClassCastException ex)
		{
			return false;
		}
	}

	@Override
	public String toString() {
		return "ReferenceType [innerType=" + innerType + "]";
	}
 
    // complete this
}
 
class RecursiveType implements Type {
    final String varName;
    final Type body;
 
    RecursiveType(String varName, Type body) {
        this.varName = varName;
        this.body = body;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		Map<String, Type> tmp = new HashMap<String, Type>(environment);
		tmp.put("pise", new TopType());
		ImmutableMap<String, Type> altEnvironment = ImmutableMap.copyOf(tmp);
		RecursiveType tmpr = new RecursiveType(this.varName,  body.replaceTypeVariable(this.varName, new TypeVariable("pise")));
		return tmpr.body.isWellFormedTypeIn(altEnvironment);
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		if(this.isWellFormedTypeIn(environment) && t.equalsTo(new TopType())) return true;
		if(this.equalsTo(t)) return true;
		if(!this.isWellFormedTypeIn(environment) || !t.isWellFormedTypeIn(environment)) return false;
		try {
			Map<String, Type> tmpmap = new HashMap<String, Type>(environment);
			tmpmap.put("pise", new TopType());
//			tmpmap.put("this_pise", new TypeVariable("pise"));
			ImmutableMap<String, Type> altEnvironment = ImmutableMap.copyOf(tmpmap);
			RecursiveType tmp = (RecursiveType) t;
//			System.out.println(altEnvironment);
//			System.out.println(tmp.body.replaceTypeVariable(tmp.varName, new TypeVariable("pise")));
//			System.out.println(this.body.replaceTypeVariable(this.varName, new TypeVariable("this_pise")));
			return this.body.replaceTypeVariable(this.varName, new TypeVariable("pise")).isSubtypeOf(tmp.body.replaceTypeVariable(tmp.varName, new TypeVariable("pise")), altEnvironment);		
		} catch (ClassCastException e) {
			// TODO: handle exception
			return false;
		}
		
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return new RecursiveType(this.varName, this.body.replaceTypeVariable(replacedName, replacement));
	}

	
	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		try
		{
			RecursiveType tmp = (RecursiveType) t;
//			System.out.println(((RecursiveType) tmp.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName))).body);
//			System.out.println(this.body);
			return ((RecursiveType) tmp.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName))).body.equalsTo(this.body) && ((RecursiveType) this.replaceTypeVariable(this.varName, new TypeVariable(tmp.varName))).body.equalsTo(tmp.body);
			
		}
		catch(ClassCastException ex)
		{
			return false;
		}
	}

	@Override
	public String toString() {
		return "RecursiveType [varName=" + varName + ", body=" + body + "]";
	}
 
    // complete this
}
 
class UniversalType implements Type {
    final String varName;
    final Type bound;
    final Type body;
 
    UniversalType(String varName, Type bound, Type body) {
        this.varName = varName;
        this.bound = bound;
        this.body = body;
    }

	@Override
	public boolean isWellFormedTypeIn(ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		Map<String, Type> tmp = new HashMap<String, Type>(environment);
		tmp.put("pise", this.bound);
		ImmutableMap<String, Type> altEnvironment = ImmutableMap.copyOf(tmp);
		UniversalType tmpu = new UniversalType("pise", this.bound,   body.replaceTypeVariable(this.varName, new TypeVariable("pise")));		
//		System.out.println(tmpu);
//		System.out.println(altEnvironment);
		return tmpu.body.isWellFormedTypeIn(altEnvironment) && tmpu.bound.isWellFormedTypeIn(altEnvironment);
	}

	@Override
	public boolean isSubtypeOf(Type t, ImmutableMap<String, Type> environment) {
		// TODO Auto-generated method stub
		if(this.isWellFormedTypeIn(environment) && t.equalsTo(new TopType())) return true;
		if(this.equalsTo(t)) return true;
		try
		{
			UniversalType second = (UniversalType) t;
			if(!second.bound.isSubtypeOf(this.bound, environment)){
				return false;
			}
			//pridam do prostredi
			Map<String, Type> tmp = new HashMap<String, Type>(environment);
			tmp.put("pise", second.bound);
//			tmp.put(second.varName, this.bound);
			ImmutableMap<String, Type> altEnvironment = ImmutableMap.copyOf(tmp);
			
			if(!this.body.replaceTypeVariable(this.varName, new TypeVariable("pise")).isSubtypeOf(second.body.replaceTypeVariable(second.varName, new TypeVariable("pise")), altEnvironment)) return false;
			
			
			
			return true;
		}
		catch(ClassCastException ex)
		{
			return false;
		}
		
	}

	@Override
	public Type replaceTypeVariable(String replacedName, Type replacement) {
		// TODO Auto-generated method stub
		return new UniversalType(replacedName, this.bound, this.body.replaceTypeVariable(replacedName, replacement));
	}

	@Override
	public boolean equalsTo(Type t) {
		// TODO Auto-generated method stub
		try
		{
			UniversalType tmp = (UniversalType) t;
			//if(tmp.varName != this.varName) return false;
//			System.out.println(tmp.body.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName)));
//			System.out.println(this.body.replaceTypeVariable(this.varName, new TypeVariable(tmp.varName)));
//			System.out.println();
//			System.out.println(((UniversalType) tmp.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName))).body.equalsTo(this.body));
//			System.out.println(((UniversalType) this.replaceTypeVariable(this.varName, new TypeVariable(tmp.varName))).body.equalsTo(tmp.body));
//			System.out.println(((UniversalType) this.replaceTypeVariable(this.varName, new TypeVariable(tmp.varName))).bound.equalsTo(tmp.bound));
//			System.out.println(((UniversalType) tmp.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName))).bound.equalsTo(this.bound));
//			
			
			return ((UniversalType) tmp.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName))).body.equalsTo(this.body) &&
					((UniversalType) this.replaceTypeVariable(this.varName, new TypeVariable(tmp.varName))).body.equalsTo(tmp.body) &&
					((UniversalType) this.replaceTypeVariable(this.varName, new TypeVariable(tmp.varName))).bound.equalsTo(tmp.bound) &&
					((UniversalType) tmp.replaceTypeVariable(tmp.varName, new TypeVariable(this.varName))).bound.equalsTo(this.bound);
		}
		catch(ClassCastException ex)
		{
			return false;
		}
	}

	@Override
	public String toString() {
		return "UniversalType [varName=" + varName + ", bound=" + bound
				+ ", body=" + body + "]";
	}
	
	
 
    // complete this
}

class Main{
	public static void main( String [ ] args )
	{
//		RecursiveType [varName=b, body=TypeVariable [name=b]].equalsTo(RecursiveType [varName=a, body=TypeVariable [name=b]])
		
//		RecursiveType t1 = new RecursiveType("b", new TypeVariable("b"));
//		RecursiveType t2 = new RecursiveType("a", new TypeVariable("b"));
//		System.out.println(t1.equalsTo(t2));
		
		
		
//		//testy od oskara
//		 RecursiveType a = new RecursiveType("b", new TypeVariable("b"));
//	       RecursiveType b = new RecursiveType("c", new TypeVariable("b"));
//	       System.out.println(a.equalsTo(b));
//
//	       a = new RecursiveType("a", new TypeVariable("c"));
//	       b = new RecursiveType("a", new TypeVariable("c"));
//	       System.out.println(a.equalsTo(b));
//
//	       a = new RecursiveType("a", new TopType());
//	       b = new RecursiveType("b", new TopType());
//	       System.out.println(a.equalsTo(b));
//
//	       a = new RecursiveType("b", new TypeVariable("a"));
//	       b = new RecursiveType("c", new TypeVariable("a"));
//	       System.out.println(a.equalsTo(b));
//
//	       a = new RecursiveType("c", new TypeVariable("a"));
//	       b = new RecursiveType("y", new TypeVariable("a"));
//	       System.out.println(a.equalsTo(b));
//
//	       a = new RecursiveType("r", new FunctionType(new TypeVariable("b"), new TypeVariable("c")));
//	       b = new RecursiveType("t", new FunctionType(new TypeVariable("b"), new TypeVariable("c")));
//	       System.out.println(a.equalsTo(b));
//	       
//	       Map<String, Type> labelsMap = new HashMap<String, Type>();
//	       labelsMap.put("x", new TopType());
//	       labelsMap.put("y", new TypeVariable("a"));
//	       labelsMap.put("z", new ReferenceType(new TopType()));
//	       ImmutableMap<String, Type> labelsMap2 = ImmutableMap.copyOf(labelsMap);
//	       a = new RecursiveType("r", new RecordType(labelsMap2));
//	       b = new RecursiveType("t", new FunctionType(new TypeVariable("b"), new TypeVariable("c")));
//	       System.out.println(a.equalsTo(b));
//		   ImmutableMap.Builder mb = new ImmutableMap.Builder<String, Type>();
//	       mb.put("a", new TopType());
//	       ImmutableMap gamma = mb.build();
//	       UniversalType u1 = new UniversalType("c", new TypeVariable("c"), new TypeVariable("c"));
//	       UniversalType u2 = new UniversalType("c", new TypeVariable("c"), new TypeVariable("c") );
//	       System.out.println(u2.isWellFormedTypeIn(gamma)); // TRUE
	       
	       //UniversalType [varName=b, bound=TopType [], body=TopType []].equalsTo(UniversalType [varName=a, bound=TypeVariable [name=a], body=TopType []])
//	       u1 = new UniversalType("b", new TopType(), new TopType());
//	       u2 = new UniversalType("a", new TypeVariable("a"), new TopType());
//	       System.out.println(u1.equalsTo(u2));
//	       u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	       u2 = new UniversalType("c", new TopType(), new TypeVariable("a"));
//	       System.out.println(u1.equalsTo(u2)); // FALSE
//
//	       u1 = new UniversalType("c", new TopType(), new TypeVariable("c"));
//	       u2 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	       System.out.println(u1.equalsTo(u2)); // TRUE
//		
//	       
//	       RecursiveType a = new RecursiveType("b", new TopType());
//	       RecursiveType b = new RecursiveType("c", new TopType());
//	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
//	       
//	       a = new RecursiveType("b", new TypeVariable("a"));
//	       b = new RecursiveType("c", new TopType());
//	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
//	       
//	       a = new RecursiveType("c", new TypeVariable("c"));
//	       b = new RecursiveType("a", new TypeVariable("a"));
//	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
//	       
//	       a = new RecursiveType("b", new TypeVariable("b"));
//	       b = new RecursiveType("b", new TopType());
//	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
//	       
//	       a = new RecursiveType("b", new TopType());
//	       b = new RecursiveType("a", new TopType());
//	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
//	       
//	       a = new RecursiveType("c", new TypeVariable("a"));
//	       b = new RecursiveType("a", new TypeVariable("a"));
//	       System.out.println(a.isSubtypeOf(b, gamma)); // FALSE
//	       
//	       a = new RecursiveType("a", new TypeVariable("a"));
//	       b = new RecursiveType("b", new TypeVariable("a"));
//	       System.out.println(a.isSubtypeOf(b, gamma)); // FALSE
	       
//	       
//	       ImmutableMap.Builder mb = new ImmutableMap.Builder<String, Type>();
//	       mb.put("a", new TopType());
//	       ImmutableMap gamma = mb.build();
//
//	       mb = new ImmutableMap.Builder<String, Type>();
//	       mb.put("a", new TopType());
//	       gamma = mb.build();
//	       
//	       
//	       System.out.println("TypeVariable");
//	       
//	       //TypeVariable [name=a].isSubtypeOf(TypeVariable [name=b], {a=TopType []}) should have returned false.
//	       TypeVariable t1 = new TypeVariable("a");
//	       TypeVariable t2 = new TypeVariable("b");
//	       System.out.println(t1.isSubtypeOf(t2, gamma));
//	       
//	       
////	       TypeVariable [name=a].isSubtypeOf(ProductType [first=TypeVariable [name=c], second=TypeVariable [name=c]], {a=TopType []}) should have returned false.
//	       ProductType pt1 = new ProductType(new TypeVariable("c"), new TypeVariable("c"));
//	       System.out.println(t1.isSubtypeOf(pt1, gamma));
//	       
//	       System.out.println("Recursive");
//	       
//	       RecursiveType r1;
//	       r1 = new RecursiveType("c", new TopType());
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // TRUE
//	      
//	       r1 = new RecursiveType("c", new TypeVariable("c"));
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // TRUE c, body: TypeVariable(c)).isWellFormedTypeIn({a=TopType()}
//
//	       r1 = new RecursiveType("c", new TypeVariable("a"));
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // TRUE
//
//	       r1 = new RecursiveType("b", new TypeVariable("b"));
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // TRUE
//
//	       r1 = new RecursiveType("a", new TypeVariable("a"));
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // TRUE
//
//	       r1 = new RecursiveType("b", new TypeVariable("c"));
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // FALSE
//
//	       r1 = new RecursiveType("c", new TypeVariable("b"));
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // FALSE
//
//	       r1 = new RecursiveType("a", new TopType());
//	       System.out.println(r1.isWellFormedTypeIn(gamma)); // TRUE
//
//	       System.out.println("Universal:");
//	       //Universal Type(a, bound: TypeVariable(b), body: TypeVariable(a)).isWellFormedTypeIn({a=TopType()})
//
//	       UniversalType ut;
//
//	       ut = new UniversalType("c", new TypeVariable("a"), new TopType());
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	       ut = new UniversalType("c", new TypeVariable("a"), new TypeVariable("c"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	       ut = new UniversalType("b", new TopType(), new TypeVariable("b"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	       ut = new UniversalType("b", new TopType(), new TypeVariable("a"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	       ut = new UniversalType("b", new TypeVariable("b"), new TopType());
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("b", new TypeVariable("b"), new TypeVariable("a"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("a", new TypeVariable("b"), new TypeVariable("a"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("c", new TypeVariable("b"), new TypeVariable("c"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("b", new TypeVariable("a"), new TypeVariable("c"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("c", new TypeVariable("b"), new TopType());
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("b", new TypeVariable("b"), new TopType());
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("b", new TypeVariable("c"), new TypeVariable("a"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	       ut = new UniversalType("c", new TypeVariable("b"), new TypeVariable("b"));
//	       System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//	       
//	       System.out.println("Universal:");
//	     //Universal Type(a, bound: TypeVariable(b), body: TypeVariable(a)).isWellFormedTypeIn({a=TopType()})
//
//	     
//
//	     ut = new UniversalType("c", new TypeVariable("a"), new TopType());
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
////	     System.out.println(gamma);
//	     ut = new UniversalType("c", new TypeVariable("a"), new TypeVariable("c"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	     ut = new UniversalType("b", new TypeVariable("b"), new TypeVariable("b"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	     ut = new UniversalType("b", new TopType(), new TypeVariable("a"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // TRUE
//
//	     ut = new UniversalType("b", new TypeVariable("b"), new TopType());
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("b", new TypeVariable("b"), new TypeVariable("a"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("a", new TypeVariable("b"), new TypeVariable("a"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("c", new TypeVariable("b"), new TypeVariable("c"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("b", new TypeVariable("a"), new TypeVariable("c"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("c", new TypeVariable("b"), new TopType());
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("b", new TypeVariable("b"), new TopType());
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("b", new TypeVariable("c"), new TypeVariable("a"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//
//	     ut = new UniversalType("c", new TypeVariable("b"), new TypeVariable("b"));
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//	     
//	     ut = new UniversalType("a", new TypeVariable("a"), new TopType());
//	     System.out.println(ut.isWellFormedTypeIn(gamma)); // FALSE
//	     
	     
	    
	       ImmutableMap.Builder mb = new ImmutableMap.Builder<String, Type>();
	       mb.put("a", new TopType());
	       ImmutableMap gamma = mb.build();

	       mb = new ImmutableMap.Builder<String, Type>();
//	       mb.put("a", new TopType());
	       mb.put("a", new TypeVariable("a"));
//	       mb.put("b", new TopType());
//	       mb.put("c", new TopType());
//	       mb.put("d", new TopType());
	       gamma = mb.build(); 
		
	       
//	     System.out.println((new TypeVariable("a")).isSubtypeOf(new TopType(), gamma));  
//		
//	     System.out.println("UniversalType:");
//	     UniversalType u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     UniversalType u2 = new UniversalType("a", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TopType(), new TopType());
//	     u2 = new UniversalType("c", new TopType(), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("a", new TypeVariable("a"), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("c", new TopType(), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TopType(), new TopType());
//	     u2 = new UniversalType("a", new TypeVariable("a"), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("c", new TopType(), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("b", new TopType(), new TypeVariable("b"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TopType(), new TypeVariable("b"));
//	     u2 = new UniversalType("b", new TopType(), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // myslim, ze FALSE
//
//	     u1 = new UniversalType("b", new TypeVariable("a"), new TopType());
//	     u2 = new UniversalType("b", new TopType(), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("b", new TypeVariable("a"), new TopType());
//	     u2 = new UniversalType("c", new TopType(), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TypeVariable("a"), new TypeVariable("a"));
//	     u2 = new UniversalType("c", new TopType(), new TopType());
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TypeVariable("a"), new TypeVariable("a"));
//	     u2 = new UniversalType("b", new TopType(), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TopType(), new TypeVariable("c"));
//	     u2 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TopType(), new TypeVariable("b"));
//	     u2 = new UniversalType("c", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TopType(), new TypeVariable("b"));
//	     u2 = new UniversalType("a", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("a"));
//	     u2 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TopType(), new TopType());
//	     u2 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("b"));
//	     u2 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("c", new TypeVariable("a"), new TypeVariable("c"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("c", new TypeVariable("a"), new TypeVariable("c"));
//	     u2 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // TRUE
//
//	     u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("b", new TopType(), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("b", new TypeVariable("a"), new TypeVariable("b"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//
//	     u1 = new UniversalType("c", new TopType(), new TypeVariable("a"));
//	     u2 = new UniversalType("b", new TopType(), new TypeVariable("b"));
//	     System.out.println(u1.isSubtypeOf(u2, gamma)); // FALSE
//	       
//	       System.out.println("easdasd");
//		     u1 = new UniversalType("b", new TopType(), new TopType());
//		       u2 = new UniversalType("a", new TypeVariable("a"), new TopType());
//		       System.out.println(u1.equalsTo(u2));
//		       u1 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//		       u2 = new UniversalType("c", new TopType(), new TypeVariable("a"));
//		       System.out.println(u1.equalsTo(u2)); // FALSE
//
//		       u1 = new UniversalType("c", new TopType(), new TypeVariable("c"));
//		       u2 = new UniversalType("a", new TopType(), new TypeVariable("a"));
//		       System.out.println(u1.equalsTo(u2)); // TRUE  
//	       TypeVariable t1 = new TypeVariable("a");
//	       TypeVariable t2 = new TypeVariable("b");
	       TopType top = new TopType();
//	       System.out.println(t1.isSubtypeOf(t2, gamma));
//	       
//	       
//	       FunctionType f1 = new FunctionType(new TypeVariable("b"), new TypeVariable("a"));
//	       FunctionType f2 = new FunctionType(new TypeVariable("a"), new TypeVariable("b"));
//	       System.out.println(f1.isSubtypeOf(f2, gamma));
//	       
////	       ProductType [first=TypeVariable [name=b], second=TypeVariable [name=a]].isSubtypeOf(ProductType [first=TypeVariable [name=b], second=TypeVariable [name=a]], {a=TopType []}) should have returned true.
//	       ProductType p1 = new ProductType(new  TypeVariable("b"), new TypeVariable("a"));
//	       ProductType p2 = new ProductType(new  TypeVariable("b"), new TypeVariable("a"));
//	       System.out.println(p1.isSubtypeOf(p2, gamma));
//	       System.out.println(p1.first.isWellFormedTypeIn(gamma));
//	       System.out.println(p1.second.isWellFormedTypeIn(gamma));
	       
	       
	       ImmutableMap<String,Type> labels1;
	       mb = new ImmutableMap.Builder<String, Type>();
	       mb.put("l2", new TopType());
	       mb.put("l1", new TopType());
	       RecordType rec1 = new RecordType(mb.build());
	       mb = new ImmutableMap.Builder<String, Type>();
	       mb.put("l2", new TopType());
	       RecordType rec2 = new RecordType(mb.build());
	       System.out.println("Recordy: " + rec1.isSubtypeOf(rec2, gamma));
	       
	       ReferenceType ref1 = new ReferenceType(new TypeVariable("c"));
	       ReferenceType ref2 = new ReferenceType(new TypeVariable("c"));
//	       System.out.println(ref1.isSubtypeOf(new TypeVariable("c"), gamma));
	       
	       RecursiveType a = new RecursiveType("a", new TopType());
	       RecursiveType b = new RecursiveType("a", new TypeVariable("a"));
//	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
	       
	       
	     
//	       
	       a = new RecursiveType("b", new TypeVariable("a"));
	       b = new RecursiveType("c", new TopType());
	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
	       
	       a = new RecursiveType("c", new TypeVariable("c"));
	       b = new RecursiveType("a", new TypeVariable("a"));
	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
	       
	       a = new RecursiveType("b", new TypeVariable("b"));
	       b = new RecursiveType("b", new TopType());
	       System.out.println(a.isSubtypeOf(b, gamma)); // TRUE
	       
	       a = new RecursiveType("b", new TopType());
	       b = new RecursiveType("c", new TopType());
	       System.out.println("Tuten: "+a.isSubtypeOf(b, gamma)); // TRUE
	       
	       a = new RecursiveType("c", new TypeVariable("a"));
	       b = new RecursiveType("a", new TypeVariable("a"));
	       System.out.println(a.isSubtypeOf(b, gamma)); // FALSE
	       
	       a = new RecursiveType("a", new TypeVariable("a"));
	       b = new RecursiveType("b", new TypeVariable("a"));
	       System.out.println(a.isSubtypeOf(b, gamma)); // FALSE
	       
	       System.out.println("Universal type:");
	       
//	       UniversalType [varName=a, bound=TopType [], body=TopType []].isSubtypeOf(TopType [], {a=TopType []}) should have returned true.
	       UniversalType u1 = new UniversalType("a", new TypeVariable("c"), new TopType());
	       UniversalType u2 = new UniversalType("b", new TypeVariable("c"), new TopType());
	       System.out.println(u1.isSubtypeOf(u2, gamma));
	       
	       System.out.println((new TypeVariable("b").isWellFormedTypeIn(gamma)));
	       
	       System.out.println("New tests");
	       mb = new ImmutableMap.Builder<String, Type>();
//	       mb.put("a", new TopType());
	       mb.put("a", new TopType());
	       mb.put("b", new TypeVariable("a"));
	       mb.put("c", new TypeVariable("b"));
	       gamma = mb.build(); 
	       TypeVariable tc = new TypeVariable("a");
	       TypeVariable tb = new TypeVariable("b");
	       System.out.println((new TypeVariable("d")).isSubtypeOf(new TopType(), gamma));
	       
	}
}	