package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.model.expressions.ReportExpr;

/**
 * User: resinas
 * Date: 03/03/13
 * Time: 20:27
 */
public class ReportExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;

    public ReportExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return ReportExpr.class;
    }

    @Override
    public Query build(RALExpr expr, Object processId) {
        ReportExpr report = (ReportExpr) expr;
        String pos = builder.getVarName("pos");

        return Query.start(pos + " = node:node_auto_index(position='" + report.getPositionConstraint().getId() + "')")
                        .where(getWhere(report.getDirection(), report.getDirectly(), pos)).build();
    }

    private String getWhere(HierarchyDirection direction, boolean directly, String pos) {
        String reports = ":REPORTS";
        String where;

        if (! directly)
            reports += "*";

        if (direction == HierarchyDirection.DIRECT)
            where = "person-[:POSITION]->()-["+reports+"]->"+pos;
        else
            where = "person-[:POSITION]->()<-["+reports+"]-"+pos;

        return where;
    }
}
