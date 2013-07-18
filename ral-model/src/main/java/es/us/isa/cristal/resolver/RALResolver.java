package es.us.isa.cristal.resolver;

import es.us.isa.cristal.model.expressions.RALExpr;

import java.util.Collection;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 20:50
 */
public interface RALResolver {
    public Collection<String> resolve(RALExpr expr, Object pid);
}
