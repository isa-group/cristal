package es.us.isa.cristal.neo4j;

import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.expressions.GroupResourceExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 26/02/13
 * Time: 19:54
 */
public class GroupResourceExprBuilder implements QueryBuilder {
    @Override
    public Class<? extends RALExpr> getExprType() {
        return GroupResourceExpr.class;
    }

    @Override
    public String build(RALExpr expr, ConstraintResolver resolver) {
        GroupResourceExpr group = (GroupResourceExpr) expr;
        String name = getNameFor(group.getGroupResourceType());
        String match = getMatchFor(group.getGroupResourceType());

        StringBuilder sb = new StringBuilder();
        sb.append("START group = node:node_auto_index("+name+"='");
        sb.append(resolver.resolve(group.getGroupResourceConstraint()));
        sb.append("') ");
        sb.append(" MATCH "+match);
        sb.append(" RETURN person.name");

        return sb.toString();

    }

    private String getMatchFor(GroupResourceType groupResourceType) {
        String match;

        if (GroupResourceType.POSITION == groupResourceType) {
            match = "person-[:POSITION]->group";
        }
        else if (GroupResourceType.ROLE == groupResourceType) {
            match = "person-[:POSITION]->()-[:ROLE]->group";
        } else {
            match = "person-[:POSITION]->()-[:UNIT]->group";
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
