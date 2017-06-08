package stanhebben.zenscript.type.casting;

import stanhebben.zenscript.compiler.IEnvironmentMethod;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.value.IAny;

/**
 * @author Stan
 */
public class CastingRuleAny implements ICastingRule {

    private final ZenType toType;

    public CastingRuleAny(ZenType toType) {
        this.toType = toType;
    }

    @Override
    public void compile(IEnvironmentMethod method) {
        method.getOutput().constant(toType);
        method.getOutput().invokeInterface(IAny.class, "as", Class.class, Object.class);
    }

    @Override
    public ZenType getInputType() {
        return ZenType.ANY;
    }

    @Override
    public ZenType getResultingType() {
        return toType;
    }
}
