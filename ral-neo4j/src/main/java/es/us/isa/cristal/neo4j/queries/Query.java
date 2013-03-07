package es.us.isa.cristal.neo4j.queries;

import com.google.common.base.Joiner;

/**
 * User: resinas
 * Date: 27/02/13
 * Time: 11:14
 */
public class Query {
    private String start;
    private String where;
    private String match;
    private String with;

    public static class QueryBuilder {
        private String start = null;
        private String where = null;
        private String match = null;
        private String with = null;

        public QueryBuilder start(String... start) {
            this.start = Joiner.on(",").join(start);
            return this;
        }

        public QueryBuilder where(String... where) {
            this.where = Joiner.on(",").join(where);
            return this;
        }

        public QueryBuilder match(String... match) {
            this.match = Joiner.on(",").skipNulls().join(match);
            return this;
        }

        public QueryBuilder with(String... with) {
            this.with = Joiner.on(",").skipNulls().join(with);
            return this;
        }

        public Query build() {
            return new Query(start, match, with, where);
        }
    }

    public static QueryBuilder start(String... start) {
        return (new QueryBuilder()).start(start);
    }

    public Query(String start, String match, String with, String where) {
        this.start = start;
        this.where = where;
        this.match = match;
        this.with = with;
    }

    public String getStart() {
        return start;
    }

    public String getWhere() {
        return where;
    }

    public String getMatch() {
        return match;
    }

    public String getWith() {
        return with;
    }
}
