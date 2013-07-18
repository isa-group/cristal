package es.us.isa.cristal.model.constraints;

/**
 * User: resinas
 * Date: 24/02/13
 * Time: 19:59
 */
public abstract class DataConstraint implements RuntimeConstraint {

    private String data;
    private String field;

    /**
     * Constructor de la clase
     *
     * @param data Identificador de dataobject
     * @param field Identificador de propiedad
     */
    public DataConstraint(String data, String field) {
        this.data = data;
        this.field = field;
    }

    /**
     * Devuelve la propiedad: field
     * identificador de campo de dataobject
     *
     * @return Valor de la propiedad
     */
    public String getField() {

        return this.field;
    }

    public String getData() {
        return data;
    }
}
