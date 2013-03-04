package es.us.isa.cristal.parser;

import es.us.isa.cristal.model.HierarchyDirection;
import es.us.isa.cristal.model.expressions.DelegateExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 09:08
 */
public class DelegateExprTest {

    private DelegateExpr parse(String expr) {
        RALExpr ralExpr = RALParser.parse(expr);
        return (DelegateExpr) ralExpr;
    }

    @Test
    public void shouldParseDelegatesToPosition() {
        DelegateExpr expr = parse("CAN DELEGATE WORK TO POSITION THEOS Coordinator");
        Assert.assertEquals(HierarchyDirection.DIRECT, expr.getDirection());
        Assert.assertEquals("THEOS Coordinator", expr.getPositionConstraint().getId());
    }

    @Test
    public void shouldParseHaveDelegatedByPosition() {
        DelegateExpr expr = parse("CAN HAVE WORK DELEGATED BY POSITION THEOS Coordinator");
        Assert.assertEquals(HierarchyDirection.INVERSE, expr.getDirection());
        Assert.assertEquals("THEOS Coordinator", expr.getPositionConstraint().getId());
    }
}
