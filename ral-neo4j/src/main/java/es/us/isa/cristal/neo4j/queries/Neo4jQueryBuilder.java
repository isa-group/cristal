package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.BPEngine;
import es.us.isa.cristal.resolver.ConstraintResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 16:36
 */
public class Neo4jQueryBuilder {

    private Map<Class<? extends RALExpr>, ExprBuilder> builderMap;
    private int varCounter = 0;
    private BPEngine bpEngine;

    public Neo4jQueryBuilder(BPEngine bpEngine) {
        builderMap = new HashMap<Class<? extends RALExpr>, ExprBuilder>();
        this.bpEngine = bpEngine;

        addQueryBuilder(new PersonExprBuilder());
        addQueryBuilder(new GroupResourceExprBuilder(this));
        addQueryBuilder(new NegativeExprBuilder(this));
        addQueryBuilder(new AndExprBuilder(this));
        addQueryBuilder(new OrExprBuilder(this));
        addQueryBuilder(new CapabilityExprBuilder());
        addQueryBuilder(new CommonalityExprBuilder(this));
        addQueryBuilder(new DelegateExprBuilder(this));
        addQueryBuilder(new ReportExprBuilder(this));
        addQueryBuilder(new IsAssignmentExprBuilder(this));

    }

    public String build(RALExpr expr, Object pid) {
        ConstraintResolver resolver = new ConstraintResolver(bpEngine, pid);
        Query q = buildQuery(expr, resolver);
        StringBuilder sb = new StringBuilder("START person=node:node_auto_index('name:*')");
        if (q.getStart() != null && ! q.getStart().isEmpty())
            sb.append(", ").append(q.getStart());
        if (q.getMatch() != null && ! q.getMatch().isEmpty())
            sb.append(" MATCH ").append(q.getMatch());
        if (q.getWith() != null && ! q.getWith().isEmpty())
            sb.append(" WITH person,").append(q.getWith());
        sb.append(" WHERE ").append(q.getWhere());
        sb.append(" RETURN DISTINCT person.name");

        return sb.toString();
    }

    public String getColumnName() {
        return "person.name";
    }


    protected BPEngine getBpEngine() {
        return bpEngine;
    }

    protected String getVarName(String name) {
        return name + varCounter++;
    }

    private void addQueryBuilder(ExprBuilder b) {
        builderMap.put(b.getExprType(), b);
    }


    protected Query buildQuery(RALExpr expr, ConstraintResolver resolver) {
        ExprBuilder builder = builderMap.get(expr.getClass());
        if (builder == null) {
            throw new RuntimeException("RAL Expression not supported: " + expr.getClass());
        }

        return builder.build(expr, resolver);
    }
}
