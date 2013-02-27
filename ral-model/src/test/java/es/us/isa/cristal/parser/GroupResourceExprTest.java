package es.us.isa.cristal.parser;

import es.us.isa.cristal.model.constraints.DataConstraint;
import es.us.isa.cristal.model.constraints.IdConstraint;
import es.us.isa.cristal.model.expressions.GroupResourceExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 09:08
 */
public class GroupResourceExprTest {

    private GroupResourceExpr parse(String expr) {
        RALExpr ralExpr = RALParser.parse(expr);
        return (GroupResourceExpr) ralExpr;
    }

    @Test
    public void shouldParsePosition() {
        GroupResourceExpr expr = parse("HAS POSITION Coordinator");
        IdConstraint c = (IdConstraint) expr.getGroupResourceConstraint();

        Assert.assertEquals("Coordinator", c.getId());
    }

    @Test
    public void shouldParsePositionWithWhitespaces() {
        GroupResourceExpr expr = parse("HAS POSITION THEOS Coordinator");
        IdConstraint c = (IdConstraint) expr.getGroupResourceConstraint();

        Assert.assertEquals("THEOS Coordinator", c.getId());
    }

    @Test
    public void shouldParsePositionsInDataObjects() {
        GroupResourceExpr expr = parse("HAS POSITION IN DATA FIELD data.field");
        DataConstraint c = (DataConstraint) expr.getGroupResourceConstraint();

        Assert.assertEquals("data", c.getData());
        Assert.assertEquals("field", c.getField());
    }
}
