package es.us.isa.cristal.model.constraints;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 20:02
 */
public abstract class ActivityConstraint implements Constraint {

    private String activityName;

    /**
     * Constructor de la clase
     *
     * @param id Identificador de la restricción
     */
    public ActivityConstraint(String id) {

        this.activityName = id;
    }

    /**
     * Devuelve la propiedad: activityName
     * identificador de la restricción. Su significado depende de la subclase
     *
     * @return Valor de la propiedad
     */
    public String getActivityName() {

        return this.activityName;
    }
}
