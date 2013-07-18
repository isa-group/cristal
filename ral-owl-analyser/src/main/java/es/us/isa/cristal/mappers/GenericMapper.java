package es.us.isa.cristal.mappers;

/**
 * User: resinas
 * Date: 02/07/13
 * Time: 18:02
 */
public interface GenericMapper<Source, Target> {
    public Class<? extends Source> getExprType();
    public Target map(Source expr, Object pid);

}
