package org.arend.lib.meta;

import org.arend.ext.error.TypecheckingError;
import org.arend.ext.typechecking.BaseMetaDefinition;
import org.arend.ext.typechecking.ContextData;
import org.arend.ext.typechecking.ExpressionTypechecker;
import org.arend.ext.typechecking.TypedExpression;
import org.arend.lib.StdExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FailMeta extends BaseMetaDefinition {
    private final StdExtension ext;

    public FailMeta(StdExtension ext) { this.ext = ext; }

    @Override
    public @Nullable TypedExpression invokeMeta(@NotNull ExpressionTypechecker typechecker, @NotNull ContextData contextData) {
        typechecker.getErrorReporter().report(new TypecheckingError("Meta failure", contextData.getReferenceExpression()));
        return null;
    }
}
