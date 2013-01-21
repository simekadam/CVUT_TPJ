import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.*;

interface ExtFJStatement {
    public FJExpression translate();

    public FJExpression translate(FJExpression state);
}

interface ExtFJExpression {

    public FJExpression translate();

    public FJExpression translate(FJExpression state);

}

class ExtFJClass {
    final String name;
    final String superclassName;
    final ImmutableList<String> fieldNames;
    final ImmutableSet<ExtFJMethod> methods;

    ExtFJClass(String name, String superclassName, ImmutableList<String> fieldNames, ImmutableSet<ExtFJMethod> methods) {
        this.name = name;
        this.superclassName = superclassName;
        this.fieldNames = fieldNames;
        this.methods = methods;
    }

    public FJClass translate() {
//        if(!homework5.varNamesMap.containsKey("this")){
            homework5.varNamesMap.put("this", homework5.lastInt++);
//        }
        System.out.println(this);
        FJExpression firstState = new FJInstantiation("FirstState", ImmutableList.<FJExpression>of( new ExtFJNumber(homework5.varNamesMap.get("this")).translate(), new FJVarAccess("this")));


        FJExpression thisState = firstState;
//        for(String field : fieldNames)
//        {
//
//
//            {
//                if(homework5.varNamesMap.containsKey(field))
//                {
//
//                }
//                else
//                {
//                    homework5.varNamesMap.put(field, homework5.lastInt++);
//
//                }
//                thisState = new FJInstantiation(
//                        "State",
//                        ImmutableList.<FJExpression>of(
//                                firstState,
//                                new ExtFJNumber(homework5.varNamesMap.get(field)).translate(),
//                                new FJFieldAccess(new FJVarAccess("this"), field)
//
//                        )
//                        );
//                homework5.exprMap.put(new FJFieldAccess(new FJVarAccess("this"), field), field);
//
//            }
//        }
        homework5.exprMap.put(firstState, "this");
//        FJExpression state = new FJInstantiation(
//                "State",
//                ImmutableList.<FJExpression>of(
//                        firstState,
//                        new ExtFJNumber(homework5.varNamesMap.get("this")).translate(),
//                        thisState
//                ));
//        homework5.exprMap.put(state, "this");
        ImmutableSet.Builder<FJMethod> builder = new ImmutableSet.Builder<FJMethod>();

        for (ExtFJMethod extFJMethod : methods) {
            builder.add(extFJMethod.translate(firstState));
        }
//        builder.add(new FJMethod("apply", "FJClass", ImmutableList.<String>of("state"), new FJVarAccess("this")));

        ImmutableList.Builder<String> listBuilder = new ImmutableList.Builder<String>();
        for (String fieldName : fieldNames) {
            listBuilder.add(fieldName);
        }


        return new FJClass(this.name, this.superclassName, listBuilder.build(), builder.build());
    }

    @Override
    public String toString() {
        return "ExtFJClass{" + '\n' +
                "name=" + name + '\n' +
                ", superclassName=" + superclassName + '\n' +
                ", fieldNames=" + fieldNames + '\n' +
                ", methods=" + methods + '\n' +
                '}';
    }
}

class ExtFJMethod {
    final String name;
    final String returnType;
    final ImmutableList<String> parameterNames;
    final ImmutableList<ExtFJStatement> statements;
    final ExtFJExpression returnedExpression;

    ExtFJMethod(String name, String returnType, ImmutableList<String> parameterNames, ImmutableList<ExtFJStatement> statements, ExtFJExpression returnedExpression) {
        this.name = name;
        this.returnType = returnType;
        this.parameterNames = parameterNames;
        this.statements = statements;
        this.returnedExpression = returnedExpression;
    }

    @Override
    public String toString() {
        return "ExtFJMethod{" + '\n' +
                "name='" + name + '\'' + '\n' +
                ", returnType='" + returnType + '\'' + '\n' +
                ", parameterNames=" + parameterNames + '\n' +
                ", statements=" + statements + '\n' +
                ", returnedExpression=" + returnedExpression + '\n' +
                '}';
    }

    public FJMethod translate() {
        return null;
    }

    public FJMethod translate(FJExpression state) {
        ImmutableList.Builder<FJExpression> builder = new ImmutableList.Builder<FJExpression>();

//        state = new FJInstantiation("State", ImmutableList.<FJExpression>of(new ExtFJNumber(homework5.varNamesMap.get("this")).translate(), new ExtFJNumber(homework5.varNamesMap.get("this")).translate(), new FJVarAccess("this")));
        boolean first = true;
        for (String param : parameterNames) {

            {
//               if(!homework5.varNamesMap.containsKey(param))
               {
                   homework5.varNamesMap.put(param, homework5.lastInt++);
               }

                    homework5.exprMap.put(new ExtFJVarAccess(param).translate(), param);

                state = new FJInstantiation(
                        "State",
                        ImmutableList.<FJExpression>of(
                                state,
                                new ExtFJNumber(homework5.varNamesMap.get(param)).translate(),
                                new FJVarAccess(param)
                        )
                );
            }
        }

        FJExpression nextState = null;
        FJExpression fjExpression = null;
        FJExpression initial = state;
        int i = 0;
        for (ExtFJStatement extFJStatement : statements) {
            fjExpression = extFJStatement.translate(state);
            state = new FJInvocation(fjExpression, "apply", ImmutableList.<FJExpression>of(state));

        }
        FJExpression returned;
//
            returned = returnedExpression.translate(state);
        while(returned instanceof FJInstantiation || returned instanceof FJVarAccess)
        {
            returned = new FJInvocation(returned, "evaluate", ImmutableList.<FJExpression>of(state));
        }



//
        return new FJMethod(name, returnType, parameterNames, returned);
    }
}

class ExtFJAssignment implements ExtFJStatement {
    final String varName;
    final ExtFJExpression expr;

    ExtFJAssignment(String varName, ExtFJExpression expr) {
        this.varName = varName;
        this.expr = expr;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> assigmentMethods = new ImmutableSet.Builder<FJMethod>();
        assigmentMethods.add(
                new FJMethod(
                        "apply",
                        "State",
                        ImmutableList.<String>of("state"),
                        new FJInvocation(
                                new FJVarAccess("state"),
                                "put",
                                ImmutableList.<FJExpression>of(
                                        new FJFieldAccess(new FJVarAccess("this"), "name"),
                                        new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "value"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state")))
                                )
                        )
                )
        );
        homework5.addToSet(new FJClass("Statement", "Object", ImmutableList.<String>of(), ImmutableSet.<FJMethod>of(new FJMethod(
                "apply",
                "State",
                ImmutableList.<String>of("state"),
                new FJVarAccess("state")
        )
        )));
        homework5.addToSet(new FJClass("Assignment", "Statement", ImmutableList.<String>of("name", "value"), assigmentMethods.build()));
    }

    @Override
    public String toString() {
        return "ExtFJAssignment{" + '\n' +
                "varName='" + varName + '\'' + '\n' +
                ", expr=" + expr + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
          return null;

    }

    @Override
    public FJExpression translate(FJExpression state) {

        if(!homework5.varNamesMap.containsKey(varName)){
            homework5.varNamesMap.put(varName, homework5.lastInt++);
        }
//        homework5.exprMap.put(expr.translate(state), varName);


        FJInstantiation inst = new FJInstantiation("Assignment", ImmutableList.<FJExpression>of(
                new ExtFJNumber(homework5.varNamesMap.get(varName)).translate(),
                expr.translate(state)
        ));
//        return new FJInvocation(state, "put", ImmutableList.<FJExpression>of(new ExtFJNumber(homework5.varNamesMap.get(varName)).translate(), expr.translate(state)));
        return inst;
    }
}

class ExtFJIfStatement implements ExtFJStatement {
    final ExtFJExpression condition;
    final ExtFJStatement thenCase;
    final ExtFJStatement elseCase;

    ExtFJIfStatement(ExtFJExpression condition, ExtFJStatement thenCase, ExtFJStatement elseCase) {
        this.condition = condition;
        this.thenCase = thenCase;
        this.elseCase = elseCase;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> ifMethods = new ImmutableSet.Builder<FJMethod>();

        ifMethods.add(
                new FJMethod(
                    "apply",
                    "State",
                    ImmutableList.<String>of("state"),
                    new FJInvocation(
                            new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "condition"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))),
                            "ternary",
                            ImmutableList.<FJExpression>of(
                                    new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "then"), "apply", ImmutableList.<FJExpression>of(new FJVarAccess("state"))),
                                    new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "else"), "apply", ImmutableList.<FJExpression>of(new FJVarAccess("state")))
                            )
                            )
                )
        );

//        ifMethods.add(
//                new FJMethod(
//                        "apply",
//                        "State",
//                        ImmutableList.<String>of("state"),
//                        new FJInvocation(
//                                new FJInstantiation(
//                                "Ternary",
//                                ImmutableList.<FJExpression>of(
//                                        new FJFieldAccess(new FJVarAccess("this"), "condition"),
//                                        new FJFieldAccess(new FJVarAccess("this"), "then"),
//                                        new FJFieldAccess(new FJVarAccess("this"), "else")
//                                )
//                        ),
//                        "apply",
//                        ImmutableList.<FJExpression>of(new FJVarAccess("state"))
//                        )
//                )
//        );


        homework5.addToSet(new FJClass("Statement", "Object", ImmutableList.<String>of(), ImmutableSet.<FJMethod>of(new FJMethod(
                "apply",
                "State",
                ImmutableList.<String>of("state"),
                new FJVarAccess("state")
        )
        )));
        homework5.addToSet(
                new FJClass(
                        "If",
                        "Statement",
                        ImmutableList.<String>of("condition", "then", "else"),
                        ifMethods.build()
                )
        );
    }

    @Override
    public String toString() {
        return "ExtFJIfStatement{" +
                "condition=" + condition + '\n' +
                ", thenCase=" + thenCase + '\n' +
                ", elseCase=" + elseCase + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FJExpression translate(FJExpression state) {
        return new FJInstantiation("If", ImmutableList.<FJExpression>of(condition.translate(state), thenCase.translate(state), elseCase.translate(state)));
    }
}

class ExtFJWhileLoop implements ExtFJStatement {
    final ExtFJExpression condition;
    final ExtFJStatement body;

    ExtFJWhileLoop(ExtFJExpression condition, ExtFJStatement body) {
        this.condition = condition;
        this.body = body;
    }

    public static void initClasses() {

        ImmutableSet.Builder<FJMethod> whileMethods = new ImmutableSet.Builder<FJMethod>();
//        whileMethods.add(new FJMethod(
//                "apply",
//                "State",
//                ImmutableList.<String>of("state"),
//                new FJInvocation(
//                        new FJInstantiation(
//                                "Ternary",
//                                ImmutableList.<FJExpression>of(
//                                       new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "condition"), "apply", ImmutableList.<FJExpression>of(new FJVarAccess("state"))),
//                                        new FJInvocation(
//                                                new FJVarAccess("this"),
//                                                "apply",
//                                                ImmutableList.<FJExpression>of(
//                                                        new FJInvocation(
//                                                                new FJFieldAccess(new FJVarAccess("this"), "body"),
//                                                                "apply",
//                                                                ImmutableList.<FJExpression>of(new FJVarAccess("state"))
//                                                        )
//                                                )
//
//
//
//                                        ),
//                                        new FJVarAccess("state")
//                        )
//                ),
//                        "apply",
//                        ImmutableList.<FJExpression>of(new FJVarAccess("state"))
//        )));
//        whileMethods.add(new FJMethod(
//                "apply",
//                "State",
//                ImmutableList.<String>of("state"),
//                new FJInvocation(
//                        new FJFieldAccess(new FJVarAccess("this"), "condition"),
//                        "ternary",
//                        ImmutableList.<FJExpression>of(
//                                new FJInvocation(new FJVarAccess("this"), "apply", ImmutableList.<FJExpression>of(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "body"), "apply", ImmutableList.<FJExpression>of(new FJVarAccess("state"))))),
//                                new FJVarAccess("state")
//                        )
//
//                )
//        ));
        FJInvocation whileCondition = new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "condition"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state")));
        FJExpression whileFirst = new FJInvocation(new FJVarAccess("this"), "apply", new ImmutableList.Builder<FJExpression>().add(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "body"), "apply", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("state")).build())).build());
        FJExpression whileSecond = new FJVarAccess("state");
        FJInvocation whileTernary = new FJInvocation(whileCondition, "ternary",  new ImmutableList.Builder<FJExpression>().add(whileFirst).add(whileSecond).build());

        whileMethods.add(new FJMethod("apply", "State", new ImmutableList.Builder<String>().add("state").build(), whileTernary));
        homework5.addToSet(new FJClass("Statement", "Object", ImmutableList.<String>of(), ImmutableSet.<FJMethod>of(new FJMethod(
                "apply",
                "State",
                ImmutableList.<String>of("state"),
                new FJVarAccess("state")
        )
        )));

        homework5.addToSet(new FJClass(
                "While",
                "Statement",
                ImmutableList.<String>of("condition", "body"),
                whileMethods.build()
        ));

    }

    @Override
    public String toString() {
        return "ExtFJWhileLoop{" + '\n' +
                "condition=" + condition + '\n' +
                ", body=" + body + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FJExpression translate(FJExpression state) {
        return new FJInstantiation("While", ImmutableList.<FJExpression>of(condition.translate(state), body.translate(state)));
    }

}

class ExtFJBlock implements ExtFJStatement {
    final ImmutableList<ExtFJStatement> statements;

    ExtFJBlock(ImmutableList<ExtFJStatement> statements) {
        this.statements = statements;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> blockMethods = new ImmutableSet.Builder<FJMethod>();
        blockMethods.add(
                new FJMethod(
                        "apply",
                        "State",
                        ImmutableList.<String>of("state"),
                        new FJInvocation(
                                new FJFieldAccess(new FJVarAccess("this"), "second"),
                                "apply",
                                ImmutableList.<FJExpression>of(
                                        new FJInvocation(
                                                new FJFieldAccess(new FJVarAccess("this"), "first"),
                                                "apply",
                                                ImmutableList.<FJExpression>of(
                                                        new FJVarAccess("state")
                                                )
                                        )
                                )
                        )
                )
        );
        homework5.addToSet(new FJClass("Statement", "Object", ImmutableList.<String>of(), ImmutableSet.<FJMethod>of(new FJMethod(
                "apply",
                "State",
                ImmutableList.<String>of("state"),
                new FJVarAccess("state")
        )
        )));
        homework5.addToSet(
                new FJClass("Block", "Statement", ImmutableList.<String>of("first", "second"), blockMethods.build())
        );

    }

    @Override
    public String toString() {
        return "ExtFJBlock{" +
                "statements=" + statements +
                '}';
    }

    @Override
    public FJExpression translate() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FJExpression translate(FJExpression state) {
        ImmutableList<ExtFJStatement> list = statements;
        FJExpression instantiation = list.get(0).translate(state);
//        state = new FJInvocation(instantiation, "apply", ImmutableList.<FJExpression>of(state));
        boolean flag = true;
        for (ExtFJStatement extFJStatement : list) {
            if (flag) {
                flag = false;
                continue;
            }
            instantiation = new FJInstantiation("Block", ImmutableList.<FJExpression>of(instantiation,extFJStatement.translate(state)));
//            state = new FJInvocation(instantiation, "apply", ImmutableList.<FJExpression>of(state));
        }
        return instantiation;


    }
}

class ExtFJVarAccess implements ExtFJExpression {
    final String name;

    ExtFJVarAccess(String name) {
        this.name = name;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> varAccessMethods = new ImmutableSet.Builder<FJMethod>();
        varAccessMethods.add(new FJMethod(
                "evaluate",
                "Object",
                ImmutableList.<String>of("state"),
                new FJInvocation(
                        new FJVarAccess("state"),
                        "get",
                        ImmutableList.<FJExpression>of(
                                new FJFieldAccess(new FJVarAccess("this"), "name")
                        ))
        ));


//        varAccessMethods.add(new FJMethod(
//                "apply",
//                "Object",
//                ImmutableList.<String>of("state"),
//                new FJInvocation(
//                        new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "name"), "numericEquals", ImmutableList.<FJExpression>of(
//                                new FJFieldAccess(new FJVarAccess("state"), "name")
//                        )
//                        ),
//                        "ternary",
//                        ImmutableList.<FJExpression>of(
//                                new FJFieldAccess(new FJVarAccess("state"), "value"),
//                                new FJInvocation(
//                                        new FJVarAccess("this"),
//                                        "apply",
//                                        ImmutableList.<FJExpression>of(new FJFieldAccess(new FJVarAccess("state"), "previousState"))
//
//                                )
//                        )
//                )
//        ));

        homework5.addToSet(new FJClass(
                "VarAccess",
                "Object",
                ImmutableList.<String>of("name"),
                varAccessMethods.build()
        ));

    }

    @Override
    public String toString() {
        return "ExtFJVarAccess{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public FJExpression translate() {
        return new FJVarAccess(this.name);
    }

    @Override
    public FJExpression translate(FJExpression state) {
//        return new FJInvocation(state, "get", ImmutableList.<FJExpression>of(new ExtFJNumber(homework5.varNamesMap.get(name)).translate()));
        return new FJInstantiation("VarAccess", ImmutableList.<FJExpression>of(new ExtFJNumber(homework5.varNamesMap.get(name)).translate()));
    }
}

class ExtFJFieldAccess implements ExtFJExpression {
    final ExtFJExpression receiver;
    final String fieldName;

    ExtFJFieldAccess(ExtFJExpression receiver, String fieldName) {
        this.receiver = receiver;
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "ExtFJFieldAccess{" + '\n' +
                "receiver=" + receiver + '\n' +
                ", fieldName='" + fieldName + '\'' + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        return new FJFieldAccess(receiver.translate(), fieldName);
    }

    @Override
    public FJExpression translate(FJExpression state) {

        return new FJFieldAccess(receiver.translate(state), fieldName);
//            FJExpression inst = new FJInvocation(new ExtFJVarAccess(((ExtFJVarAccess)receiver).name).translate(state),"evaluate", ImmutableList.<FJExpression>of(state));
//            return new FJInvocation(inst,"get", ImmutableList.<FJExpression>of(new ExtFJNumber(homework5.varNamesMap.get(fieldName)).translate(state)));
    }
}

class ExtFJInvocation implements ExtFJExpression {
    final ExtFJExpression receiver;
    final String methodName;
    final ImmutableList<ExtFJExpression> parameters;

    ExtFJInvocation(ExtFJExpression receiver, String methodName, ImmutableList<ExtFJExpression> parameters) {
        this.receiver = receiver;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ExtFJInvocation{" + '\n' +
                "receiver=" + receiver + '\n' +
                ", methodName='" + methodName + '\'' + '\n' +
                ", parameters=" + parameters + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        ImmutableList.Builder<FJExpression> builder = new ImmutableList.Builder<FJExpression>();
        for (ExtFJExpression expr : parameters) {
            builder.add(expr.translate());
        }
        return new FJInvocation(receiver.translate(), methodName, builder.build());
    }

    @Override
    public FJExpression translate(FJExpression state) {
        ImmutableList.Builder<FJExpression> builder = new ImmutableList.Builder<FJExpression>();
        for (ExtFJExpression expr : parameters) {
            builder.add(expr.translate(state));
        }
        FJInvocation fjInvocation = new FJInvocation(receiver.translate(state), "evaluate", ImmutableList.<FJExpression>of(state));
        return new  FJInstantiation("Invocation", ImmutableList.<FJExpression>of(new FJInvocation(fjInvocation, methodName, builder.build())));
    }

    public static void initClasses()
    {
        ImmutableSet.Builder<FJMethod> instantiationMethods = new ImmutableSet.Builder<FJMethod>();

        instantiationMethods.add(new FJMethod(
                "evaluate",
                "FJExpression",
                ImmutableList.<String>of("state"),
                new FJFieldAccess(new FJVarAccess("this"), "invocation")
        ));


        homework5.addToSet(new FJClass("Invocation", "Object", ImmutableList.<String>of("invocation"), instantiationMethods.build()));

    }
}

class ExtFJInstantiation implements ExtFJExpression {
    final String className;
    final ImmutableList<ExtFJExpression> parameters;

    ExtFJInstantiation(String className, ImmutableList<ExtFJExpression> parameters) {
        this.className = className;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ExtFJInstantiation{" + '\n' +
                "className='" + className + '\'' + '\n' +
                ", parameters=" + parameters + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        ImmutableList.Builder<FJExpression> builder = new ImmutableList.Builder<FJExpression>();
        for (ExtFJExpression expr : parameters) {
            builder.add(expr.translate());
        }
        return new FJInstantiation(className, builder.build());
    }

    @Override
    public FJExpression translate(FJExpression state) {
        ImmutableList.Builder<FJExpression> builder = new ImmutableList.Builder<FJExpression>();
        for (ExtFJExpression expr : parameters) {
            builder.add(expr.translate(state));
        }
        FJInstantiation instance =  new FJInstantiation(className, builder.build());
        return new FJInstantiation("Instantiation", ImmutableList.<FJExpression>of(instance));
    }

    static void initClasses()
    {
        ImmutableSet.Builder<FJMethod> instantiationMethods = new ImmutableSet.Builder<FJMethod>();

        instantiationMethods.add(new FJMethod(
                "evaluate",
                "FJExpression",
                ImmutableList.<String>of("state"),
                new FJFieldAccess(new FJVarAccess("this"), "instantiation")
        ));


        homework5.addToSet(new FJClass("Instantiation", "Object", ImmutableList.<String>of("instantiation"), instantiationMethods.build()));
    }
}

class ExtFJNumber implements ExtFJExpression {
    final int value;

    ExtFJNumber(int value) {
        this.value = value;
    }

    static void initClasses() {
        ImmutableSet.Builder<FJMethod> methodsNatural = new ImmutableSet.Builder<FJMethod>();
        ImmutableSet.Builder<FJMethod> methodsZero = new ImmutableSet.Builder<FJMethod>();
        ImmutableSet.Builder<FJMethod> methodsSucc = new ImmutableSet.Builder<FJMethod>();

        ImmutableList.Builder<String> paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");
        methodsNatural.add(new FJMethod("plus", "Natural", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "plus", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));
        methodsNatural.add(new FJMethod("minus", "Natural", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "minus", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));
        methodsNatural.add(new FJMethod("inverseMinus", "Natural", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "inverseMinus", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));
        methodsNatural.add(new FJMethod("isZero", "Boolean", new ImmutableList.Builder<String>().build(), new FJInvocation(new FJVarAccess("this"), "isZero", new ImmutableList.Builder<FJExpression>().build())));
        methodsNatural.add(new FJMethod("numericEquals", "Boolean", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "numericEquals", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));
        methodsNatural.add(new FJMethod("evaluate", "Natural", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "apply", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));

        //ZERO
        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");

        ImmutableList<FJExpression> expressions;
        ImmutableList.Builder<FJExpression> expressionBuilder = new ImmutableList.Builder<FJExpression>();

        expressionBuilder.add(new FJVarAccess("second"));

        methodsZero.add(new FJMethod("plus", "Natural", paramsOfMethods.build(), new FJVarAccess("second")));
        methodsZero.add(new FJMethod("minus", "Natural", paramsOfMethods.build(), new FJVarAccess("this")));
        methodsZero.add(new FJMethod("inverseMinus", "Natural", paramsOfMethods.build(), new FJVarAccess("second")));
        methodsZero.add(new FJMethod("isZero", "Boolean", new ImmutableList.Builder<String>().build(), new FJInstantiation("True", new ImmutableList.Builder<FJExpression>().build())));

        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");

        methodsZero.add(new FJMethod("numericEquals", "Boolean", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("second"), "isZero", new ImmutableList.Builder<FJExpression>().build())));
        methodsZero.add(new FJMethod("evaluate", "Zero", paramsOfMethods.build(), new FJVarAccess("this")));


        //SUCC
        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");

//        ImmutableList<FJExpression> expressions;
        expressionBuilder = new ImmutableList.Builder<FJExpression>();

        expressionBuilder.add(new FJVarAccess("second"));

        FJInstantiation succ = new FJInstantiation("Succ", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build());
        FJInvocation fjInvocation = new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "pred"), "plus", new ImmutableList.Builder<FJExpression>().add(succ).build());

        methodsSucc.add(new FJMethod("plus", "Natural", paramsOfMethods.build(), fjInvocation));

        FJInvocation fjInvocationInverseMinus = new FJInvocation(new FJVarAccess("second"), "inverseMinus", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("this")).build());

        methodsSucc.add(new FJMethod("minus", "Natural", paramsOfMethods.build(), fjInvocationInverseMinus));

        FJInvocation fjInvocationMinus = new FJInvocation(new FJFieldAccess(new FJVarAccess("second"), "pred"), "minus", new ImmutableList.Builder<FJExpression>().add(new FJFieldAccess(new FJVarAccess("this"), "pred")).build());

        methodsSucc.add(new FJMethod("inverseMinus", "Natural", paramsOfMethods.build(), fjInvocationMinus));

        methodsSucc.add(new FJMethod("isZero", "Boolean", new ImmutableList.Builder<String>().build(), new FJInstantiation("False", new ImmutableList.Builder<FJExpression>().build())));

        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");

        FJInvocation inner = new FJInvocation(new FJVarAccess("this"), "minus", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build());
        FJInvocation inner2 = new FJInvocation(new FJVarAccess("second"), "minus", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("this")).build());
        FJInvocation inner3 = new FJInvocation(inner, "isZero", new ImmutableList.Builder<FJExpression>().build());
        FJInvocation inner4 = new FJInvocation(inner2, "isZero", new ImmutableList.Builder<FJExpression>().build());

        methodsSucc.add(new FJMethod("numericEquals", "Boolean", paramsOfMethods.build(), new FJInvocation(inner3, "and", new ImmutableList.Builder<FJExpression>().add(inner4).build())));
        methodsSucc.add(new FJMethod("evaluate", "Succ", paramsOfMethods.build(), new FJVarAccess("this")));


        homework5.addToSet(new FJClass("Natural", "Object", new ImmutableList.Builder<String>().build(), methodsNatural.build()));
        homework5.addToSet(new FJClass("Zero", "Natural", new ImmutableList.Builder<String>().build(), methodsZero.build()));
        homework5.addToSet(new FJClass("Succ", "Natural", new ImmutableList.Builder<String>().add("pred").build(), methodsSucc.build()));
    }

    @Override
    public String toString() {
        return "ExtFJNumber{" +
                "value=" + value +
                '}';
    }

    @Override
    public FJExpression translate() {


        ImmutableList<FJExpression> params = new ImmutableList.Builder<FJExpression>().build();
        FJExpression current = new FJInstantiation("Zero", params);

        for (int i = 0; i < value; i++) {
            params = new ImmutableList.Builder<FJExpression>().add(current).build();
            current = new FJInstantiation("Succ", params);
        }

        return current;
    }

    @Override
    public FJExpression translate(FJExpression state) {
        return this.translate();
    }


}

class ExtFJAddition implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJAddition(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> additionMethods = new ImmutableSet.Builder<FJMethod>();

        additionMethods.add(new FJMethod(
                "evaluate",
                "Natural",
                ImmutableList.<String>of("state"),
                new FJInvocation(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "left"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), "plus", ImmutableList.<FJExpression>of(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "right"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))))
        )));


        homework5.addToSet(new FJClass(
                "Addition",
                "Object",
                ImmutableList.<String>of("left", "right"),
                additionMethods.build()
        ));
    }

    @Override
    public FJExpression translate() {
        FJExpression left = this.left.translate();
        FJExpression right = this.right.translate();
        return new FJInvocation(left, "plus", new ImmutableList.Builder<FJExpression>().add(right).build());

    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression left = this.left.translate(state);
        FJExpression right = this.right.translate(state);
//        return new FJInvocation(left, "plus", new ImmutableList.Builder<FJExpression>().add(right).build());
        return new FJInstantiation("Addition", ImmutableList.<FJExpression>of(left, right));
    }
}

class ExtFJSubtraction implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJSubtraction(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> subtractionMethods = new ImmutableSet.Builder<FJMethod>();

        subtractionMethods.add(new FJMethod(
                "evaluate",
                "Natural",
                ImmutableList.<String>of("state"),
                new FJInvocation(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "left"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), "minus", ImmutableList.<FJExpression>of(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "right"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))))
        )));


        homework5.addToSet(new FJClass(
                "Subtraction",
                "Object",
                ImmutableList.<String>of("left", "right"),
                subtractionMethods.build()
        ));
    }

    @Override
    public String toString() {
        return "ExtFJSubtraction{" + '\n' +
                "left=" + left + '\n' +
                ", right=" + right + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        FJExpression left = this.left.translate();
        FJExpression right = this.right.translate();
        return new FJInvocation(left, "minus", new ImmutableList.Builder<FJExpression>().add(right).build());

    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression left = this.left.translate(state);
        FJExpression right = this.right.translate(state);
        return new FJInstantiation("Subtraction", ImmutableList.<FJExpression>of(left, right));
    }
}

class ExtFJBoolean implements ExtFJExpression {
    final boolean value;

    ExtFJBoolean(boolean value) {
        this.value = value;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> methodsBoolean = new ImmutableSet.Builder<FJMethod>();
        ImmutableSet.Builder<FJMethod> methodsTrue = new ImmutableSet.Builder<FJMethod>();
        ImmutableSet.Builder<FJMethod> methodsFalse = new ImmutableSet.Builder<FJMethod>();

        ImmutableList.Builder<String> paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");
        methodsBoolean.add(new FJMethod("and", "Boolean", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "and", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));
        methodsBoolean.add(new FJMethod("or", "Boolean", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "or", new ImmutableList.Builder<FJExpression>().add(new FJVarAccess("second")).build())));
        methodsBoolean.add(new FJMethod("evaluate", "Boolean", paramsOfMethods.build(), new FJVarAccess("this")));

        paramsOfMethods = new ImmutableList.Builder<String>();
        methodsBoolean.add(new FJMethod("not", "Boolean", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "not", new ImmutableList.Builder<FJExpression>().build())));

        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("left");
        paramsOfMethods.add("right");
        ImmutableList.Builder<FJExpression> paramsOfTernary = new ImmutableList.Builder<FJExpression>();
        paramsOfTernary.add(new FJVarAccess("left"));
        paramsOfTernary.add(new FJVarAccess("right"));
        methodsBoolean.add(new FJMethod("ternary", "FJExpression", paramsOfMethods.build(), new FJInvocation(new FJVarAccess("this"), "ternary", paramsOfTernary.build())));

        //TRUE
        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");

        ImmutableList<FJExpression> expressions;
        ImmutableList.Builder<FJExpression> expressionBuilder = new ImmutableList.Builder<FJExpression>();

        expressionBuilder.add(new FJVarAccess("second"));

        methodsTrue.add(new FJMethod("and", "Boolean", paramsOfMethods.build(), new FJVarAccess("second")));
        methodsTrue.add(new FJMethod("or", "Boolean", paramsOfMethods.build(), new FJVarAccess("this")));
        methodsTrue.add(new FJMethod("evaluate", "Boolean", paramsOfMethods.build(), new FJVarAccess("this")));

        paramsOfMethods = new ImmutableList.Builder<String>();
        methodsTrue.add(new FJMethod("not", "Boolean", paramsOfMethods.build(), new FJInstantiation("False", new ImmutableList.Builder<FJExpression>().build())));

        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("left");
        paramsOfMethods.add("right");
        methodsTrue.add(new FJMethod("ternary", "FJExpression", paramsOfMethods.build(), new FJVarAccess("left")));


        //FALSE
        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("second");

//        ImmutableList<FJExpression> expressions;
        expressionBuilder = new ImmutableList.Builder<FJExpression>();

        expressionBuilder.add(new FJVarAccess("second"));

        methodsFalse.add(new FJMethod("and", "Boolean", paramsOfMethods.build(), new FJVarAccess("this")));
        methodsFalse.add(new FJMethod("or", "Boolean", paramsOfMethods.build(), new FJVarAccess("second")));
        methodsFalse.add(new FJMethod("evaluate", "Boolean", paramsOfMethods.build(), new FJVarAccess("this")));

        paramsOfMethods = new ImmutableList.Builder<String>();
        methodsFalse.add(new FJMethod("not", "Boolean", paramsOfMethods.build(), new FJInstantiation("True", new ImmutableList.Builder<FJExpression>().build())));

        paramsOfMethods = new ImmutableList.Builder<String>();
        paramsOfMethods.add("left");
        paramsOfMethods.add("right");
        methodsFalse.add(new FJMethod("ternary", "FJExpression", paramsOfMethods.build(), new FJVarAccess("right")));


        homework5.addToSet(new FJClass("True", "Boolean", new ImmutableList.Builder<String>().build(), methodsTrue.build()));
        homework5.addToSet(new FJClass("False", "Boolean", new ImmutableList.Builder<String>().build(), methodsFalse.build()));
        homework5.addToSet(new FJClass("Boolean", "Object", new ImmutableList.Builder<String>().build(), methodsBoolean.build()));
    }

    @Override
    public String toString() {
        return "ExtFJBoolean{" +
                "value=" + value +
                '}';
    }

    @Override
    public FJExpression translate() {
        ImmutableList<FJExpression> params = new ImmutableList.Builder<FJExpression>().build();


        return this.value ? new FJInstantiation("True", params) : new FJInstantiation("False", params);
    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJInstantiation returned;
        if(value) returned = new FJInstantiation("True", ImmutableList.<FJExpression>of());
        else returned = new FJInstantiation("False", ImmutableList.<FJExpression>of());
        return returned;
    }
}

class ExtFJNegation implements ExtFJExpression {
    final ExtFJExpression expr;

    ExtFJNegation(ExtFJExpression expr) {
        this.expr = expr;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> negationMethods = new ImmutableSet.Builder<FJMethod>();

        negationMethods.add(new FJMethod(
                "evaluate",
                "Boolean",
                ImmutableList.<String>of("state"),
                new FJInvocation(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "param"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), "not", ImmutableList.<FJExpression>of())
        ));


        homework5.addToSet(new FJClass(
                "Negation",
                "Object",
                ImmutableList.<String>of("param"),
                negationMethods.build()
        ));
    }

    @Override
    public String toString() {
        return "ExtFJNegation{" +
                "expr=" + expr +
                '}';
    }

    @Override
    public FJExpression translate() {
//        FJExpression param = this.expr.translate();
//        return new FJInvocation(param, "not", new ImmutableList.Builder<FJExpression>().build());
        return null;
    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression param = this.expr.translate(state);
//        return new FJInvocation(param, "not", new ImmutableList.Builder<FJExpression>().build());
        return new FJInstantiation("Negation", ImmutableList.<FJExpression>of(param));
    }
}

class ExtFJConjunction implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJConjunction(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> conjunctionMethods = new ImmutableSet.Builder<FJMethod>();

        FJInvocation invocation1 = new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "left"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state")));
        FJInvocation invocation2 = new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "right"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state")));

        conjunctionMethods.add(new FJMethod(
                "evaluate",
                "Boolean",
                ImmutableList.<String>of("state"),
                new FJInvocation(invocation1, "and", ImmutableList.<FJExpression>of(invocation2))
        ));


        homework5.addToSet(new FJClass(
                "Conjunction",
                "Object",
                ImmutableList.<String>of("left", "right"),
                conjunctionMethods.build()
        ));
    }

    @Override
    public String toString() {
        return "ExtFJConjunction{" + '\n' +
                "left=" + left + '\n' +
                ", right=" + right + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        FJExpression left = this.left.translate();
        FJExpression right = this.right.translate();
        return new FJInvocation(left, "and", new ImmutableList.Builder<FJExpression>().add(right).build());
    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression left = this.left.translate(state);
        FJExpression right = this.right.translate(state);
//        return new FJInvocation(left, "and", new ImmutableList.Builder<FJExpression>().add(right).build());
        return new FJInstantiation("Conjunction", ImmutableList.<FJExpression>of(left, right));
    }

}

class ExtFJDisjunction implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJDisjunction(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> disjunctionMethods = new ImmutableSet.Builder<FJMethod>();

        disjunctionMethods.add(new FJMethod(
                "evaluate",
                "Boolean",
                ImmutableList.<String>of("state"),
                new FJInvocation(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "left"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), "or", ImmutableList.<FJExpression>of(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "right"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))))
        )));


        homework5.addToSet(new FJClass(
                "Disjunction",
                "Object",
                ImmutableList.<String>of("left", "right"),
                disjunctionMethods.build()
        ));
    }

    @Override
    public String toString() {
        return "ExtFJDisjunction{" + '\n' +
                "left=" + left + '\n' +
                ", right=" + right + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        FJExpression left = this.left.translate();
        FJExpression right = this.right.translate();
        return new FJInvocation(left, "or", new ImmutableList.Builder<FJExpression>().add(right).build());
    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression left = this.left.translate(state);
        FJExpression right = this.right.translate(state);
//        return new FJInvocation(left, "or", new ImmutableList.Builder<FJExpression>().add(right).build());
        return new FJInstantiation("Disjunction", ImmutableList.<FJExpression>of(left, right));
    }
}

class ExtFJTernary implements ExtFJExpression {
    final ExtFJExpression condition;
    final ExtFJExpression thenCase, elseCase;

    ExtFJTernary(ExtFJExpression condition, ExtFJExpression thenCase, ExtFJExpression elseCase) {
        this.condition = condition;
        this.thenCase = thenCase;
        this.elseCase = elseCase;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> ternaryMethods = new ImmutableSet.Builder<FJMethod>();

        ternaryMethods.add(new FJMethod(
                "evaluate",
                "State",
                ImmutableList.<String>of("state"),
                new FJInvocation(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "condition"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), "ternary", ImmutableList.<FJExpression>of(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "then"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "else"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state")))))


        ));


        homework5.addToSet(new FJClass(
                "Ternary",
                "Object",
                ImmutableList.<String>of("condition", "then", "else"),
                ternaryMethods.build()
        ));
    }

    @Override
    public String toString() {
        return "ExtFJTernary{" + '\n' +
                "condition=" + condition + '\n' +
                ", thenCase=" + thenCase + '\n' +
                ", elseCase=" + elseCase + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        FJExpression left = thenCase.translate();
        FJExpression right = elseCase.translate();
        ImmutableList.Builder<FJExpression> builder = new ImmutableList.Builder<FJExpression>();
        builder.add(left);
        builder.add(right);
        return new FJInvocation(condition.translate(), "ternary", builder.build());
    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression left = thenCase.translate(state);
        FJExpression right = elseCase.translate(state);

//        return new FJInvocation(condition.translate(state), "ternary", builder.build());
        return new FJInstantiation("Ternary", ImmutableList.<FJExpression>of(condition.translate(state),left, right));
    }
}

class ExtFJNumericEquals implements ExtFJExpression {
    final ExtFJExpression first, second;

    ExtFJNumericEquals(ExtFJExpression first, ExtFJExpression second) {
        this.first = first;
        this.second = second;
    }

    public static void initClasses() {
        ImmutableSet.Builder<FJMethod> equalsMethods = new ImmutableSet.Builder<FJMethod>();

        equalsMethods.add(new FJMethod(
                "evaluate",
                "Boolean",
                ImmutableList.<String>of("state"),
                new FJInvocation(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "left"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state"))), "numericEquals", ImmutableList.<FJExpression>of(new FJInvocation(new FJFieldAccess(new FJVarAccess("this"), "right"), "evaluate", ImmutableList.<FJExpression>of(new FJVarAccess("state")))))
        ));


        homework5.addToSet(new FJClass(
                "NumericEquals",
                "Object",
                ImmutableList.<String>of("left", "right"),
                equalsMethods.build()
        ));
    }

    @Override
    public String toString() {
        return "ExtFJNumericEquals{" + '\n' +
                "first=" + first + '\n' +
                ", second=" + second + '\n' +
                '}';
    }

    @Override
    public FJExpression translate() {
        FJExpression left = this.first.translate();
        FJExpression right = this.second.translate();
        return new FJInvocation(left, "numericEquals", new ImmutableList.Builder<FJExpression>().add(right).build());
    }

    @Override
    public FJExpression translate(FJExpression state) {
        FJExpression left = this.first.translate(state);
        FJExpression right = this.second.translate(state);
//        return new FJInvocation(left, "numericEquals", new ImmutableList.Builder<FJExpression>().add(right).build());
        return new FJInstantiation("NumericEquals", ImmutableList.<FJExpression>of(left, right));
    }
}

class homework5 {

    static Set<FJClass> returnedSet;
    static int lastInt = 0;
    static HashMap<String, Integer> varNamesMap = new HashMap<String, Integer>();
    static HashMap<FJExpression, String> exprMap = new HashMap<FJExpression, String>();

    static Set<FJClass> translate(Set<ExtFJClass> classes) {

        returnedSet = new HashSet<FJClass>();
        ExtFJNumber.initClasses();
        ExtFJBoolean.initClasses();
        ExtFJAssignment.initClasses();
        ExtFJBlock.initClasses();
        ExtFJIfStatement.initClasses();
        ExtFJWhileLoop.initClasses();
        ExtFJVarAccess.initClasses();
        ExtFJAddition.initClasses();
        ExtFJConjunction.initClasses();
        ExtFJDisjunction.initClasses();
        ExtFJNegation.initClasses();
        ExtFJSubtraction.initClasses();
        ExtFJTernary.initClasses();
        ExtFJNumericEquals.initClasses();
        ExtFJInstantiation.initClasses();
        ExtFJInvocation.initClasses();
        //inicializace stavu
        ImmutableSet.Builder<FJMethod> stateMethods = new ImmutableSet.Builder<FJMethod>();
        stateMethods.add(
                new FJMethod("put",
                        "State",
                        ImmutableList.<String>of("name", "value"),
                        new FJInstantiation(
                                "State",
                                ImmutableList.<FJExpression>of(
                                        new FJVarAccess("this"),
                                        new FJVarAccess("name"),
                                        new FJVarAccess("value")
                                )
                        )));
        stateMethods.add(
                new FJMethod("get",
                        "FJExpression",
                        ImmutableList.<String>of("name"),

                        new FJInvocation(
                                new FJInvocation(new FJVarAccess("name"), "numericEquals", ImmutableList.<FJExpression>of(
                                        new FJFieldAccess(new FJVarAccess("this"), "name")
                                )
                                ),
                                "ternary",
                                ImmutableList.<FJExpression>of(
                                        new FJFieldAccess(new FJVarAccess("this"), "value"),
                                        new FJInvocation(
                                                new FJFieldAccess(new FJVarAccess("this"), "previousState"),
                                                "get",
                                                ImmutableList.<FJExpression>of(new FJVarAccess("name"))

                                        )
                                )
                        )
                )
        );


//        stateMethods.add(
//                new FJMethod("get",
//                        "FJExpression",
//                        ImmutableList.<String>of("name"),
//
//                        new FJInvocation(
//                                new FJInstantiation(
//                                        "Ternary",
//                                        ImmutableList.<FJExpression>of(
//                                             new FJInvocation(
//                                                     new FJInstantiation("NumericEquals", ImmutableList.<FJExpression>of(
//                                                             new FJVarAccess("name"),
//                                                             new FJFieldAccess(new FJVarAccess("this"), "name")
//                                                     )),
//                                                     "apply",
//                                                     ImmutableList.<FJExpression>of(new FJVarAccess("this"))
//                                             ),
//                                                new FJFieldAccess(new FJVarAccess("this"), "value"),
//                                                new FJInvocation(
//                                                        new FJFieldAccess(new FJVarAccess("this"), "previousState"),
//                                                        "get",
//                                                        ImmutableList.<FJExpression>of(new FJVarAccess("name"))
//
//                                                )
//                                        )
//                                ),
//                                "apply",
//                                ImmutableList.<FJExpression>of(
//                                        new FJVarAccess("this")
//                                )
//                        )
//                )
//        );
//        stateMethods.add(new FJMethod(
//                "apply",
//                ImmutableList.<String>of("state"),
//                new FJVarAccess("state")
//        ));

        addToSet(new FJClass("State", "Object", new ImmutableList.Builder<String>().add("previousState").add("name").add("value").build(), stateMethods.build()));

        stateMethods = new ImmutableSet.Builder<FJMethod>();
        stateMethods.add(
                new FJMethod("put",
                        "State",
                        ImmutableList.<String>of("name", "value"),
                        new FJInstantiation(
                                "State",
                                ImmutableList.<FJExpression>of(
                                        new FJVarAccess("this"),
                                        new FJVarAccess("name"),
                                        new FJVarAccess("value")
                                )
                        )));
        stateMethods.add(
                new FJMethod("get",
                        "FJExpression",
                        ImmutableList.<String>of("name"),

                        new FJInvocation(
                                new FJInvocation(new FJVarAccess("name"), "numericEquals", ImmutableList.<FJExpression>of(
                                        new FJFieldAccess(new FJVarAccess("this"), "name")
                                )
                                ),
                                "ternary",
                                ImmutableList.<FJExpression>of(
                                        new FJFieldAccess(new FJVarAccess("this"), "value"),
                                        new FJInstantiation("Boolean", ImmutableList.<FJExpression>of())
                                )
                        )
                )
        );

        addToSet(new FJClass("FirstState", "Object", new ImmutableList.Builder<String>().add("name").add("value").build(), stateMethods.build()));
        addToSet(new FJClass("Null", "Object", ImmutableList.<String>of(), ImmutableSet.<FJMethod>of()));


        for (ExtFJClass extclass : classes) {
            returnedSet.add(extclass.translate());
        }

        return returnedSet;
    }

    static void addToSet(FJClass addedClass) {

        returnedSet.add(addedClass);

    }

    public static void main(String[] args) {
        ExtFJStatement statement = new ExtFJAssignment("a", new ExtFJNumber(0));
        ExtFJStatement statement2 = new ExtFJAssignment("a", new ExtFJAddition(new ExtFJVarAccess("a"), new ExtFJVarAccess("p")));
        ExtFJStatement statement3 = new ExtFJAssignment("p", new ExtFJSubtraction(new ExtFJVarAccess("p"), new ExtFJNumber(1)));
        ExtFJStatement block = new ExtFJBlock(new ImmutableList.Builder<ExtFJStatement>().add(statement2).add(statement3).build());
        ExtFJAssignment assignment2 = new ExtFJAssignment("p", new ExtFJNumber(0));
        ExtFJExpression condition = new ExtFJNegation(new ExtFJNumericEquals(new ExtFJVarAccess("p"), new ExtFJNumber(0)));
        ExtFJStatement whileLoop = new ExtFJWhileLoop(condition, block);
        ExtFJMethod f = new ExtFJMethod("f", "Object", new ImmutableList.Builder<String>().add("p").build(), new ImmutableList.Builder<ExtFJStatement>().add(statement).add(whileLoop).build(), new ExtFJTernary(new ExtFJNumericEquals(new ExtFJVarAccess("a"),new ExtFJNumber(0)), new ExtFJInstantiation("X", new ImmutableList.Builder<ExtFJExpression>().build()), new ExtFJInstantiation("Y", new ImmutableList.Builder<ExtFJExpression>().build())));
        ExtFJMethod g = new ExtFJMethod("g", "Object", new ImmutableList.Builder<String>().build(), new ImmutableList.Builder<ExtFJStatement>().build(),new ExtFJInvocation(new ExtFJVarAccess("this"), "f", new ImmutableList.Builder<ExtFJExpression>().add(new ExtFJNumber(1)).build()));
        ExtFJClass foo = new ExtFJClass("Foo", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().add(f).add(g).build());
        ExtFJClass x = new ExtFJClass("X", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());
        ExtFJClass y = new ExtFJClass("Y", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());


//        ExtFJStatement statement = new ExtFJAssignment("a", new ExtFJNumber(0));
//        ExtFJStatement statement2 = new ExtFJAssignment("a", new ExtFJAddition(new ExtFJVarAccess("a"), new ExtFJVarAccess("p")));
//        ExtFJStatement statement3 = new ExtFJAssignment("p", new ExtFJSubtraction(new ExtFJVarAccess("p"), new ExtFJNumber(1)));
//        ExtFJStatement block = new ExtFJBlock(new ImmutableList.Builder<ExtFJStatement>().add(statement3).build());
//        ExtFJAssignment assignment2 = new ExtFJAssignment("p", new ExtFJNumber(0));
//        ExtFJExpression condition =  new ExtFJNegation(new ExtFJNumericEquals(new ExtFJVarAccess("p"), new ExtFJNumber(0)));
//        ExtFJStatement whileLoop = new ExtFJWhileLoop(condition, block);
//        ExtFJMethod f = new ExtFJMethod("f", "Object", new ImmutableList.Builder<String>().add("p").build(), new ImmutableList.Builder<ExtFJStatement>().add(whileLoop).build(), new ExtFJVarAccess("p"));
//        ExtFJMethod g = new ExtFJMethod("g", "Object", new ImmutableList.Builder<String>().build(), new ImmutableList.Builder<ExtFJStatement>().build(), new ExtFJInvocation(new ExtFJVarAccess("this"), "f", new ImmutableList.Builder<ExtFJExpression>().add(new ExtFJNumber(10)).build()));
//        ExtFJClass foo = new ExtFJClass("Foo", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().add(f).add(g).build());
//        ExtFJClass x = new ExtFJClass("X", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());
//        ExtFJClass y = new ExtFJClass("Y", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());


        ImmutableSet.Builder<ExtFJClass> extClasses = new ImmutableSet.Builder<ExtFJClass>();
        extClasses.add(foo);
        extClasses.add(x);
        extClasses.add(y);
        ImmutableSet<FJClass> fjClasses = ImmutableSet.copyOf(translate(extClasses.build()));
        FJInstantiation fJInstantiationFoo = new FJInstantiation("Foo", ImmutableList.<FJExpression>of());

        System.out.println("REDUCE TO VALUE:");
        System.out.println(new FJInvocation(fJInstantiationFoo, "g", new ImmutableList.Builder<FJExpression>().build()).reduceToValue(fjClasses));
    }


//    public static void main(String[] args) {
//
////        ExtFJClass foo = new ExtFJClass("Foo", "Object", new ImmutableList.Builder<String>().add("testField").build(), new ImmutableSet.Builder<ExtFJMethod>().add(
////                new ExtFJMethod(
////                        "f",
////                        "Object",
////                        new ImmutableList.Builder<String>().add("test").build(),
////                        new ImmutableList.Builder<ExtFJStatement>().add(
////                                new ExtFJAssignment("d", new ExtFJBoolean(false)),
////                                new ExtFJAssignment("f", new ExtFJNumericEquals(new ExtFJBoolean(false), new ExtFJVarAccess("c")))
////
////
////
////
////                        ).build(),
////                        new ExtFJVarAccess("f")
////                )).build()
////        );
//        ExtFJClass foo = new ExtFJClass("Foo", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().add(
//                new ExtFJMethod(
//                        "g",
//                        "Object",
//                        ImmutableList.<String>of(),
//                        ImmutableList.<ExtFJStatement>of(
//
//
//                        ),
//                        new ExtFJTernary(new ExtFJBoolean(true), new ExtFJInstantiation("X", ImmutableList.<ExtFJExpression>of()), new ExtFJInstantiation("Y", ImmutableList.<ExtFJExpression>of()))))
////
//
//                                .build()
//                );
//        ExtFJClass x = new ExtFJClass("X", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());
//        ExtFJClass y = new ExtFJClass("Y", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());
//
//
//        ImmutableSet.Builder<ExtFJClass> origClasses = new ImmutableSet.Builder<ExtFJClass>();
//        origClasses.add(foo);
//        origClasses.add(x);
//        origClasses.add(y);
//        ImmutableSet<FJClass> table = ImmutableSet.copyOf(translate(origClasses.build()));
//        FJInstantiation inst = new FJInstantiation("Foo", ImmutableList.<FJExpression>of(new ExtFJNumber(1).translate()));
//        System.out.println(inst);
//        System.out.println("REDUCE TO VALUE:");
////
//        FJInvocation test = new FJInvocation(inst, "g", new ImmutableList.Builder<FJExpression>().build());
//        System.out.println(test);
//        Object[] array = homework5.returnedSet.toArray();
//
////        HashSet<FJClass> list = (HashSet<FJClass>) translate(ImmutableSet.<ExtFJClass>of(foo));
////
////        for (FJClass l : list)
////        {
////            for(FJMethod method : l.methods)
////            {
////                if(method.name.equals("g"))
////                {
////                    System.out.println(method);
////                    System.out.println("_________________________________________");
////                    method.body.reduceToValue(table);
////                }
////
////            }
////        }
//
////        int xxx = 1;
//        System.out.println(test.reduceToValue(table));
//    }


//    public static void main(String[] args) {
//        ExtFJStatement statement = new ExtFJAssignment("a", new ExtFJNumber(0));
//        ExtFJStatement statement2 = new ExtFJAssignment("a", new ExtFJAddition(new ExtFJVarAccess("a"), new ExtFJVarAccess("p")));
//        ExtFJStatement statement3 = new ExtFJAssignment("p", new ExtFJSubtraction(new ExtFJVarAccess("p"), new ExtFJNumber(1)));
//        ExtFJStatement block = new ExtFJBlock(new ImmutableList.Builder<ExtFJStatement>().add(statement2).add(statement3).build());
//        ExtFJAssignment assignment2 = new ExtFJAssignment("p", new ExtFJNumber(0));
//        ExtFJExpression condition = new ExtFJNegation(new ExtFJNumericEquals(new ExtFJVarAccess("p"), new ExtFJNumber(5)));
//        ExtFJStatement whileLoop = new ExtFJWhileLoop(condition, block);
//        ExtFJMethod f = new ExtFJMethod("f", "Object", new ImmutableList.Builder<String>().add("p").build(), new ImmutableList.Builder<ExtFJStatement>().add(statement).add(whileLoop).build(), new ExtFJVarAccess("a"));
//        ExtFJMethod g = new ExtFJMethod("g", "Object", new ImmutableList.Builder<String>().build(), new ImmutableList.Builder<ExtFJStatement>().build(),new ExtFJInvocation(new ExtFJVarAccess("this"), "f", new ImmutableList.Builder<ExtFJExpression>().add(new ExtFJNumber(10)).build()));
//        ExtFJClass foo = new ExtFJClass("Foo", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().add(f).add(g).build());
//        ExtFJClass x = new ExtFJClass("X", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());
//        ExtFJClass y = new ExtFJClass("Y", "Object", new ImmutableList.Builder<String>().build(), new ImmutableSet.Builder<ExtFJMethod>().build());
//
//        ImmutableSet.Builder<ExtFJClass> extClasses = new ImmutableSet.Builder<ExtFJClass>();
//        extClasses.add(foo);
//        extClasses.add(x);
//        extClasses.add(y);
//        ImmutableSet<FJClass> fjClasses = ImmutableSet.copyOf(translate(extClasses.build()));
//        FJInstantiation fJInstantiationFoo = new FJInstantiation("Foo", ImmutableList.<FJExpression>of());
//
//        System.out.println("REDUCE TO VALUE:");
//        System.out.println(new FJInvocation(fJInstantiationFoo, "g", new ImmutableList.Builder<FJExpression>().build()).reduceToValue(fjClasses));
//    }

}