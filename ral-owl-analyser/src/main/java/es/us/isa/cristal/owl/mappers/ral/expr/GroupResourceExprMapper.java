package es.us.isa.cristal.owl.mappers.ral.expr;

import es.us.isa.cristal.model.GroupResourceType;
import es.us.isa.cristal.model.expressions.GroupResourceExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import es.us.isa.cristal.owl.Definitions;
import es.us.isa.cristal.owl.mappers.ral.ExprMapper;
import es.us.isa.cristal.owl.mappers.ral.OwlConstraintMapper;

/**
 * User: resinas
 * Date: 03/07/13
 * Time: 10:30
 */
public class GroupResourceExprMapper implements ExprMapper {
    private OwlConstraintMapper mapper;

    public GroupResourceExprMapper(OwlConstraintMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Class<? extends RALExpr> getExprType() {
        return GroupResourceExpr.class;
    }

    @Override
    public String map(RALExpr expr, Object pid) {
        GroupResourceExpr e = (GroupResourceExpr) expr;
        GroupResourceType type = e.getGroupResourceType();
        String query;
        String mapc = mapper.map(e.getGroupResourceConstraint(), pid);

        if (type == GroupResourceType.POSITION)
            query = Definitions.OCCUPIES + " some (" + mapc + ")";
        else if (type == GroupResourceType.UNIT) {
            query = Definitions.OCCUPIES + " some (" + Definitions.ISMEMBEROF + " some (" + mapc + "))";
        }
        else if (type == GroupResourceType.ROLE) {
            query = Definitions.OCCUPIES + " some (" + Definitions.PARTICIPATESIN + " some (" + mapc + "))";
        } else {
            throw new RuntimeException("Not valid type in group resource expression");
        }

        return query;
    }
}
