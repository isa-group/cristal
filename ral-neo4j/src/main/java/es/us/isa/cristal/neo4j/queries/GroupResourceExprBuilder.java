package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.expressions.GroupResourceExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 26/02/13
 * Time: 19:54
 */
public class GroupResourceExprBuilder implements ExprBuilder {

    private Neo4jQueryBuilder builder;
    private ConstraintResolver resolver;
    
    public GroupResourceExprBuilder(Neo4jQueryBuilder builder, ConstraintResolver resolver) {
        this.builder = builder;
        this.resolver = resolver;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return GroupResourceExpr.class;
    }

    @Override
    public Query build(RALExpr expr, Object processId) {
        GroupResourceExpr group = (GroupResourceExpr) expr;
        String varName = builder.getVarName("group");
        String name = getNameFor(group.getGroupResourceType());
        String match = getMatchFor(group.getGroupResourceType(), varName);

//        StringBuilder sb = new StringBuilder();
//        sb.append("START group = node:node_auto_index("+name+"='");
//        sb.append(resolver.resolve(group.getGroupResourceConstraint()));
//        sb.append("') ");
//        sb.append(" MATCH "+match);
//        sb.append(" RETURN person.name");

        return Query.start(varName+" = node:node_auto_index("+name+"='"+ resolver.resolve(group.getGroupResourceConstraint(), processId)+"')")
                .where(match).build();

    }

    private String getMatchFor(GroupResourceType groupResourceType, String varName) {
        String match;

        if (GroupResourceType.POSITION == groupResourceType) {
            match = "person-[:POSITION]->"+varName;
        }
        else if (GroupResourceType.ROLE == groupResourceType) {
            match = "person-[:POSITION]->()-[:ROLE]->"+varName;
        } else {
            match = "person-[:POSITION]->()-[:UNIT]->"+varName;
        }


        return match;
    }

    private String getNameFor(GroupResourceType groupResourceType) {
        String result;
        if (GroupResourceType.POSITION == groupResourceType) {
            result = "position";
        }
        else if (GroupResourceType.ROLE == groupResourceType) {
            result = "role";
        } else {
            result = "unit";
        }

        return result;
    }
}
