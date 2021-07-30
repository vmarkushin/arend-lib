package org.arend.lib.meta;

import org.arend.ext.concrete.expr.ConcreteExpression;
import org.arend.ext.core.expr.*;
import org.arend.ext.error.GeneralError;
import org.arend.ext.typechecking.BaseMetaDefinition;
import org.arend.ext.typechecking.ContextData;
import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.arend.ext.ui.ArendConsole;
import org.arend.lib.StdExtension;
import org.arend.lib.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuoteMeta extends BaseMetaDefinition {
    private final StdExtension ext;

    public QuoteMeta(StdExtension ext) {
        this.ext = ext;
    }

    @Override
    public @Nullable boolean[] argumentExplicitness() {
        return new boolean[] { true };
    }

    @Override
    public int numberOfOptionalExplicitArguments() {
        return 1;
    }

    @Override
    public boolean allowExcessiveArguments() {
        return true;
    }
    
    class Data {
        ExpressionTypechecker typechecker;
    }
    
    class ToArendTermsReflector implements CoreExpressionVisitor<Data, ConcreteExpression>{
        @Override
        public ConcreteExpression visitApp(@NotNull CoreAppExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitFunCall(@NotNull CoreFunCallExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitConCall(@NotNull CoreConCallExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitDataCall(@NotNull CoreDataCallExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitFieldCall(@NotNull CoreFieldCallExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitClassCall(@NotNull CoreClassCallExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitReference(@NotNull CoreReferenceExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitInferenceReference(@NotNull CoreInferenceReferenceExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitLam(@NotNull CoreLamExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitPi(@NotNull CorePiExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitSigma(@NotNull CoreSigmaExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitUniverse(@NotNull CoreUniverseExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitError(@NotNull CoreErrorExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitTuple(@NotNull CoreTupleExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitProj(@NotNull CoreProjExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitNew(@NotNull CoreNewExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitPEval(@NotNull CorePEvalExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitLet(@NotNull CoreLetExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitCase(@NotNull CoreCaseExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitInteger(@NotNull CoreIntegerExpression expr, Data params) {
            var cons = ext.Expr.findConstructor("num");
            return ext.factory.app(ext.factory.ref(cons.getRef()), false, ext.factory.core(expr.computeTyped()));
        }

        @Override
        public ConcreteExpression visitTypeCoerce(@NotNull CoreTypeCoerceExpression expr, Data params) {
            return null;
        }

        @Override
        public ConcreteExpression visitArray(@NotNull CoreArrayExpression expr, Data params) {
            return null;
        }
    }

    @Override
    public @Nullable TypedExpression invokeMeta(@NotNull ExpressionTypechecker typechecker, @NotNull ContextData contextData) {
        ArendConsole console = ext.ui.getConsole(contextData.getMarker());

        if (contextData.getArguments().isEmpty()) {
            typechecker.getErrorReporter().report(new GeneralError(GeneralError.Level.WARNING, "Missing argument"));
        } else {
            List<? extends ConcreteExpression> args = Utils.getArgumentList(contextData.getArguments().get(0).getExpression());
            if (args.size() == 1) {
                var in = args.get(0);
                TypedExpression instance;
                var ex = typechecker.typecheck(in, null);
                if (ex != null) {
                    var outEx = ex.getExpression().accept(new ToArendTermsReflector(), new Data());
                    console.println("repr: " + outEx);
                    return typechecker.typecheck(outEx, contextData.getExpectedType());
                } else {
                    typechecker.getErrorReporter().report(new GeneralError(GeneralError.Level.WARNING, "Typecheck error"));
                }
            } else {
                typechecker.getErrorReporter().report(new GeneralError(GeneralError.Level.WARNING, "Too many args"));
            }
        }
        return typechecker.typecheck(ext.factory.withData(contextData.getMarker().getData()).number(124), contextData.getExpectedType());
    }
}
