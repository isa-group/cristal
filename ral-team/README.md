RAL Team
========

This project implements the mapping from RALTeam expression to DL Concept Descriptions expressed in the OWL
Manchester Syntax.

To do so, it implements RALTeam in an internal DSL way in Java. Therefore, RALTeam expressions must be written using
Java syntax as follows:

```Java
RALTeamCore r;
RALTeamMember m;

String map1 = r.and(r.ofType("type1"), r.createdBy("p1"));
String map2 = m.whoseMembersInclude(m.onlyPeople(), m.who("HAS ROLE PhdStudent"));
String map3 = m.whoseMembersIncludePlayingRoleType(m.atLeast(1), m.who("HAS POSITION Analyst"), "developer");
```

The result of these expressions is directly the mapping to DL concept expressions. Therefore the output of the following expression:

```
System.out.println(map3);
```

is

```
teams:Team and teams-example:developer min 1 (organization:occupies some ({teams-example:Analyst}))
```

More examples can be found at [src/test/java](src/test/java).

In its current version, it supports all expressions from RALTeam Core, RALTeam Hierarchy, RALTeam
Data and RALTeam Member. See http://www.isa.us.es/cristal for more details about their syntax.