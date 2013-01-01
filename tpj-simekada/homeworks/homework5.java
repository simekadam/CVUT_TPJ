import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

class FJClass {
    final String name;
    final String superclassName;
    final ImmutableList<String> fieldNames;
    final ImmutableSet<FJMethod> methods;

    FJClass(String name, String superclassName, ImmutableList<String> fieldNames, ImmutableSet<FJMethod> methods) {
        this.name = name;
        this.superclassName = superclassName;
        this.fieldNames = fieldNames;
        this.methods = methods;
    }
}

class FJMethod {
    final String name;
    final String returnType;
    final ImmutableList<String> parameterNames;
    final FJExpression body;

    FJMethod(String name, String returnType, ImmutableList<String> parameterNames, FJExpression body) {
        this.name = name;
        this.returnType = returnType;
        this.parameterNames = parameterNames;
        this.body = body;
    }
}

interface FJExpression {

}

class FJVarAccess implements FJExpression {
    final String name;

    FJVarAccess(String name) {
        this.name = name;
    }
}

class FJFieldAccess implements FJExpression {
    final FJExpression receiver;
    final String fieldName;

    FJFieldAccess(FJExpression receiver, String fieldName) {
        this.receiver = receiver;
        this.fieldName = fieldName;
    }
}

class FJInvocation implements FJExpression {
    final FJExpression receiver;
    final String methodName;
    final ImmutableList<FJExpression> parameters;

    FJInvocation(FJExpression receiver, String methodName, ImmutableList<FJExpression> parameters) {
        this.receiver = receiver;
        this.methodName = methodName;
        this.parameters = parameters;
    }
}

class FJInstantiation implements FJExpression {
    final String className;
    final ImmutableList<FJExpression> parameters;

    FJInstantiation(String className, ImmutableList<FJExpression> parameters) {
        this.className = className;
        this.parameters = parameters;
    }
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
}

interface ExtFJStatement {

}

class ExtFJAssignment implements ExtFJStatement {
    final String varName;
    final ExtFJExpression expr;

    ExtFJAssignment(String varName, ExtFJExpression expr) {
        this.varName = varName;
        this.expr = expr;
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
}

class ExtFJWhileLoop implements ExtFJStatement {
    final ExtFJExpression condition;
    final ExtFJStatement body;

    ExtFJWhileLoop(ExtFJExpression condition, ExtFJStatement body) {
        this.condition = condition;
        this.body = body;
    }
}

class ExtFJBlock implements ExtFJStatement {
    final ImmutableList<ExtFJStatement> statements;

    ExtFJBlock(ImmutableList<ExtFJStatement> statements) {
        this.statements = statements;
    }
}

interface ExtFJExpression {

}

class ExtFJVarAccess implements ExtFJExpression {
    final String name;

    ExtFJVarAccess(String name) {
        this.name = name;
    }
}

class ExtFJFieldAccess implements ExtFJExpression {
    final ExtFJExpression receiver;
    final String fieldName;

    ExtFJFieldAccess(ExtFJExpression receiver, String fieldName) {
        this.receiver = receiver;
        this.fieldName = fieldName;
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
}

class ExtFJInstantiation implements ExtFJExpression {
    final String className;
    final ImmutableList<ExtFJExpression> parameters;

    ExtFJInstantiation(String className, ImmutableList<ExtFJExpression> parameters) {
        this.className = className;
        this.parameters = parameters;
    }
}

class ExtFJNumber implements ExtFJExpression {
    final int value;

    ExtFJNumber(int value) {
        this.value = value;
    }
}

class ExtFJAddition implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJAddition(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }
}

class ExtFJSubtraction implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJSubtraction(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }
}

class ExtFJBoolean implements ExtFJExpression {
    final boolean value;

    ExtFJBoolean(boolean value) {
        this.value = value;
    }
}

class ExtFJNegation implements ExtFJExpression {
    final ExtFJExpression expr;

    ExtFJNegation(ExtFJExpression expr) {
        this.expr = expr;
    }
}

class ExtFJConjunction implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJConjunction(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
    }
}

class ExtFJDisjunction implements ExtFJExpression {
    final ExtFJExpression left, right;

    ExtFJDisjunction(ExtFJExpression left, ExtFJExpression right) {
        this.left = left;
        this.right = right;
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
}

class ExtFJNumericEquals implements ExtFJExpression {
    final ExtFJExpression first, second;

    ExtFJNumericEquals(ExtFJExpression first, ExtFJExpression second) {
        this.first = first;
        this.second = second;
    }
}