package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.expressions.DelegateExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 03/03/13
 * Time: 20:27
 */
public class DelegateExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;

    public DelegateExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return DelegateExpr.class;
    }

    @Override
    public Query build(RALExpr expr, ConstraintResolver resolver) {
        DelegateExpr delegate = (DelegateExpr) expr;
        String pos = builder.getVarName("pos");

        return Query.start(pos + " = node:node_auto_index(position='" + delegate.getPositionConstraint().getId() + "')")
                        .where(getWhere(delegate.getDirection(), pos)).build();
    }

    private String getWhere(HierarchyDirection direction, String pos) {
        String where;
        if (direction == HierarchyDirection.DIRECT)
            where = "person-[:POSITION]->()-[:DELEGATES]->"+pos;
        else
            where = "person-[:POSITION]->()<-[:DELEGATES]-"+pos;

        return where;
    }
}
