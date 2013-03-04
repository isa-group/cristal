package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.CommonalityAmount;
import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.expressions.CommonalityExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 26/02/13
 * Time: 19:54
 */
public class CommonalityExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;

    public CommonalityExprBuilder(Neo4jQueryBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return CommonalityExpr.class;
    }

    @Override
    public Query build(RALExpr expr, ConstraintResolver resolver) {
        CommonalityExpr commonalityExpr = (CommonalityExpr) expr;
        String p = builder.getVarName("p");
        String g = builder.getVarName("g");

        Query.QueryBuilder queryBuilder =
                Query.start(p + " = node:node_auto_index(name='" + resolver.resolve(commonalityExpr.getPersonConstraint()) + "')")
                        .match(getPatternFor(commonalityExpr.getGroupResourceType(), p, g));

        if(commonalityExpr.getAmount() == CommonalityAmount.SOME)
            buildForSome(queryBuilder, commonalityExpr.getGroupResourceType(), p, g);
        else
            buildForAll(queryBuilder, commonalityExpr.getGroupResourceType(), g);

        return queryBuilder.build();
    }

    private void buildForAll(Query.QueryBuilder queryBuilder, GroupResourceType groupResourceType, String g) {
        String coll = builder.getVarName("coll");
        queryBuilder.with("collect(" + g + ") AS " + coll).where("ALL(el in " + coll + " WHERE " + getPatternFor(groupResourceType, "person", "el") + ")");
    }

    private void buildForSome(Query.QueryBuilder queryBuilder, GroupResourceType type, String p, String g) {
        queryBuilder.where(getPatternFor(type, "person", g));
    }

    private String getPatternFor(GroupResourceType groupResourceType, String p, String g) {
        String match;

        if (GroupResourceType.POSITION == groupResourceType) {
            match = p+"-[:POSITION]->"+g;
        }
        else if (GroupResourceType.ROLE == groupResourceType) {
            match = p+"-[:POSITION]->()-[:ROLE]->"+g;
        } else {
            match = p+"-[:POSITION]->()-[:UNIT]->"+g;
        }

        return match;
    }

}
