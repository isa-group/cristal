package es.us.isa.cristal.neo4j.queries;

import es.us.isa.cristal.model.expressions.PersonExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.resolver.ConstraintResolver;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 16:48
 */
public class PersonExprBuilder implements ExprBuilder {
    @Override
    public Class<? extends RALExpr> getExprType() {
        return PersonExpr.class;
    }

    @Override
    public Query build(RALExpr expr, ConstraintResolver resolver, Object processId) {
        PersonExpr p = (PersonExpr) expr;

//        StringBuilder sb = new StringBuilder();


//        sb.append("START person = node:node_auto_index(name='");
//        sb.append(resolver.resolve(p.getPersonConstraint()));
//        sb.append("') RETURN person.name");

        return Query.start().where("person.name='" + resolver.resolve(p.getPersonConstraint()) + "'").build();

    }
}
