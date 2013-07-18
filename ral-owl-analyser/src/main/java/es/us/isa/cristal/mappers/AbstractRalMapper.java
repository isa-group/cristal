package es.us.isa.cristal.mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 17:41
 */
public abstract class AbstractRalMapper<Source, Target, Mapper extends GenericMapper<Source, Target>> {
    private Map<Class<? extends Source>, Mapper> builderMap;
    private int varCounter = 0;

    public AbstractRalMapper() {
        builderMap = new HashMap<Class<? extends Source>, Mapper>();
    }

    public Target map(Source expr, Object pid) {
        return buildMap(expr, pid);
    }

    protected String getVarName(String name) {
        return name + varCounter++;
    }

    protected void addMapper(Mapper b) {
        builderMap.put(b.getExprType(), b);
    }


    protected Target buildMap(Source expr, Object pid) {
        Mapper builder = builderMap.get(expr.getClass());

        if (builder == null) {
            builder = trySuperclasses(expr);
            if (builder == null) {
                builder = tryInterfaces(expr);
                if (builder == null) {
                    throw new RuntimeException("RAL Expression not supported: " + expr.getClass());
                }
            }
        }

        return builder.map(expr, pid);
    }

    private Mapper trySuperclasses(Source expr) {
        Mapper builder = null;
        Class<?> clazz = expr.getClass().getSuperclass();

        while (clazz != null) {
            builder = builderMap.get(clazz);
            if (builder != null)
                break;

            clazz = clazz.getSuperclass();
        }

        return builder;
    }

    private Mapper tryInterfaces(Source expr) {
        Mapper builder = null;
        Class<?>[] classes = expr.getClass().getInterfaces();
        for (Class<?> c: classes) {
            builder = builderMap.get(c);
            if (builder != null)
                break;
        }

        return builder;
    }

}
