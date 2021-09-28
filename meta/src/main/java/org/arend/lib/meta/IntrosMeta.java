package org.arend.lib.meta;

import org.arend.ext.concrete.ConcreteFactory;
import org.arend.ext.concrete.ConcreteParameter;
import org.arend.ext.concrete.ConcreteSourceNode;
import org.arend.ext.concrete.expr.ConcreteExpression;
import org.arend.ext.core.context.CoreParameter;
import org.arend.ext.core.expr.CoreExpression;
import org.arend.ext.core.expr.CorePiExpression;
import org.arend.ext.error.TypecheckingError;
import org.arend.ext.typechecking.BaseMetaDefinition;
import org.arend.ext.typechecking.ContextData;
import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.arend.ext.ui.ArendConsole;
import org.arend.lib.StdExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IntrosMeta extends BaseMetaDefinition {
    private final StdExtension ext;

    public IntrosMeta(StdExtension ext) {this.ext = ext; }

    @Override
    public @Nullable boolean[] argumentExplicitness() {
        return new boolean[] { true };
    }

    @Override
    public boolean allowExcessiveArguments() {
        return false;
    }

    @Override
    public @Nullable TypedExpression invokeMeta(@NotNull ExpressionTypechecker typechecker, @NotNull ContextData contextData) {
        ConcreteSourceNode marker = contextData.getMarker();
        CoreExpression originalGoalType = contextData.getExpectedType();
        if (originalGoalType instanceof CorePiExpression) {
            List<CoreParameter> piArgs = new ArrayList<>(1);
            CoreExpression piTarget = originalGoalType.getPiParameters(piArgs);

            List<ConcreteParameter> refs = new ArrayList<>(1);
            ConcreteFactory factory = ext.factory.withData(marker);
            for (int i = 0; i < piArgs.size(); i++) { refs.add(factory.param(factory.local(piArgs.get(i).getBinding().getName()))); }
            ConcreteExpression resultLam = factory.lam(refs, contextData.getArguments().get(0).getExpression());

            return typechecker.typecheck(resultLam, originalGoalType);
        }

        typechecker.getErrorReporter().report(new TypecheckingError("Expected a pi type", contextData.getMarker()));
        return null;
    }
}
