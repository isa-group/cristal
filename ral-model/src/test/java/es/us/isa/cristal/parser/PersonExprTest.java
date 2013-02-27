package es.us.isa.cristal.parser;

import es.us.isa.cristal.model.constraints.PersonInDataFieldConstraint;
import es.us.isa.cristal.model.constraints.ThisPersonConstraint;
import es.us.isa.cristal.model.expressions.PersonExpr;
import es.us.isa.cristal.model.expressions.RALExpr;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 13:42
 */
public class PersonExprTest {

    private PersonExpr parse(String exprStr) {
        RALExpr expr = RALParser.parse(exprStr);
        return (PersonExpr) expr;
    }

    @Test
    public void shouldParsePersonExpr() {
        PersonExpr personExpr = parse("IS Pepe");
        ThisPersonConstraint c = (ThisPersonConstraint) personExpr.getPersonConstraint();

        Assert.assertEquals("Pepe", c.getId());
    }

    @Test
    public void shouldParsePersonDataExpr() {
        PersonExpr expr = parse("IS PERSON IN DATA FIELD data.field");
        PersonInDataFieldConstraint c = (PersonInDataFieldConstraint) expr.getPersonConstraint();

        Assert.assertEquals("data", c.getData());
        Assert.assertEquals("field", c.getField());
    }
    }
