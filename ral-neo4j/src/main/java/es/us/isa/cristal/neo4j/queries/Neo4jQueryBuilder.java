package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.BPEngine;
import es.us.isa.cristal.model.expressions.RALExpr;
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
    private ConstraintResolver resolver;
    
    
    public Neo4jQueryBuilder(BPEngine bpEngine, ConstraintResolver resolver) {
        builderMap = new HashMap<Class<? extends RALExpr>, ExprBuilder>();
        this.bpEngine = bpEngine;
        this.resolver = resolver;
        addQueryBuilder(new PersonExprBuilder(resolver));
        addQueryBuilder(new GroupResourceExprBuilder(this, resolver));
        addQueryBuilder(new NegativeExprBuilder(this));
        addQueryBuilder(new AndExprBuilder(this));
        addQueryBuilder(new OrExprBuilder(this));
        addQueryBuilder(new CapabilityExprBuilder());
        addQueryBuilder(new CommonalityExprBuilder(this, resolver));
        addQueryBuilder(new DelegateExprBuilder(this));
        addQueryBuilder(new ReportExprBuilder(this));
        addQueryBuilder(new IsAssignmentExprBuilder(this));

    }

    public String build(RALExpr expr, Object pid) {
        
        Query q = buildQuery(expr, pid);
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

// quitar el pid del constraint resolver, que el constraint reolver recibael pid en elmetodo resolve
// pasar a cada xxxxExprbuilder el constraint resolver en el constructor.
//se quita el constraint resolver de losmetodos buildquery.
    
    protected Query buildQuery(RALExpr expr, Object pid) {
        ExprBuilder builder = builderMap.get(expr.getClass());
        if (builder == null) {
            throw new RuntimeException("RAL Expression not supported: " + expr.getClass());
        }
        
        return builder.build(expr, pid);
    }
}
