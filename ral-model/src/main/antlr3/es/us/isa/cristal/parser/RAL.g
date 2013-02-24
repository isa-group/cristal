grammar RAL;

@lexer::header {
package es.us.isa.cristal.parser;
}

@header {
package es.us.isa.cristal.parser;

import es.us.isa.cristal.model.expressions.*;
import es.us.isa.cristal.model.constraints.*;
import es.us.isa.cristal.model.*;
}
 
@members {
    
    public static RALExpr parse(String ralExpr) {
    
        try {

            RALLexer lex = new RALLexer(new ANTLRStringStream( ralExpr ));
            CommonTokenStream tokens = new CommonTokenStream(lex);
     
            RALParser parser = new RALParser(tokens);

            return parser.expression();
            
        } catch (RecognitionException e)  {
            throw new RuntimeException(e);
        }
    }

}
 
/*--------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------*/

/*expressions [String processId] :
  ( taskName ':' e=expression ';' {ExpressionSet.getExpressions(processId).put($taskName.text, $e.value); System.out.println($taskName.text +":"+ $expression.text+';');} )+ ;
*/

expression returns [RALExpr value] :
  e=exprAnd {$value = $e.value;} 
  ( 'OR' e=exprAnd { $value = new OrExpr($value, $e.value);} )* ;

exprAnd returns [RALExpr value] :
  e=exprBase {$value = $e.value;} 
  ( 'AND' ( 'IF POSSIBLE' e=exprBase { $value = new IfPossibleExpr($value, $e.value);}
		    | e=exprBase { $value = new AndExpr($value, $e.value);} ) )* ;

exprBase returns [RALExpr value] :
  e1=exprSimple {$value = $e1.value;}
  | '(' e2=expression ')' {$value = $e2.value;}
  | 'NOT' e1=exprBase {$value = new NegativeExpr($e1.value);} ;

exprSimple returns [RALExpr value] :
  'IS' ( e1=personConstraint {$value = new PersonExpr($e1.value);}
         | 'ASSIGNMENT IN ACTIVITY' activityName {$value = new IsAssignmentExpr($activityName.text); }
         | depth 'REPORTED BY' e2=positionConstraint {$value = new IsReportedByExpr($depth.value, $e2.value);} )
  | 'HAS' ( groupResourceType e3=groupResourceConstraint {$value = new GroupResourceExpr($groupResourceType.value, $e3.value);}
  			| 'CAPABILITY' capabilityConstraint {$value = new CapabilityExpr($capabilityConstraint.text);} )
  | 'SHARES' amount groupResourceType 'WITH' e1=personConstraint {$value = new CommonalityExpr(CommonalityAmount.valueOf($amount.text), $groupResourceType.value, $e1.value);}
  | 'REPORTS TO' e2=positionConstraint depth {$value = new ReportsToExpr($depth.value, $e2.value);}
  | 'CAN' ( 'DELEGATE WORK TO' e2=positionConstraint {$value = new DelegateToExpr($e2.value);}
  			| 'HAVE WORK DELEGATED BY' e2=positionConstraint {$value = new HaveDelegatedExpr($e2.value);} ) ;

personConstraint returns [PersonConstraint value] : 
  personName {$value = new ThisPersonConstraint($personName.text);}
  | 'PERSON IN DATA FIELD' dataobject '.' fieldName {$value = new PersonInDataFieldConstraint($dataobject.text, $fieldName.text);} 
  | 'PERSON WHO DID ACTIVITY' activityName  {$value = new PersonWhoDidActivityConstraint($activityName.text);};

groupResourceConstraint returns [GroupResourceConstraint value] : 
  groupResourceName {$value = new ThisGroupResourceConstraint($groupResourceName.text);} 
  | 'IN DATA FIELD' dataobject '.' fieldName {$value = new GroupResourceInDataFieldConstraint($dataobject.text, $fieldName.text);} ;

positionConstraint returns [PositionConstraint value] : 
  'POSITION' ( namePosition {$value = new ThisPositionConstraint($namePosition.text);} 
               | 'OF' e=personConstraint {$value = new PositionOfConstraint($e.value);} ) ;

groupResourceType returns [GroupResourceType value] :
  'POSITION' {$value = GroupResourceType.POSITION;}
  | 'ROLE'   {$value = GroupResourceType.ROLE;}
  | 'UNIT'   {$value = GroupResourceType.UNIT;} ;

amount : 
  'SOME' 
  | 'ALL' ;

depth returns [boolean value]:
  ('DIRECTLY')? {$value = ! "".equals($text);} ;

capabilityConstraint : ID;
/*  capabilityName 
  | capabilityRestriction ;*/

taskName : ID ;

personName : ID ;

activityName : ID ;

dataobject : ID ;

fieldName : ID ;

namePosition : ID ;

groupResourceName : ID ;

/*capabilityName : ID ;

capabilityRestriction : ID ;*/

/*--------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------*/
 
ID : LETTER ( LETTER | '0'..'9' | '-' | '_' )* ;

fragment LETTER  : ( 'a'..'z' | 'A'..'Z' ) ;

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+    { $channel = HIDDEN; } ;


