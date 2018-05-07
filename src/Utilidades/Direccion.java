package Utilidades;

/**
 * Enumeración de apoyo para trabajar con las posibles direcciones.
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public enum Direccion {

    /**
     * Dirección hacia arriba (Implica disminución de Y).
     */
    ARRIBA,
    /**
     * Dirección hacia abajo (Implica aumento de Y).
     */
    ABAJO,
    /**
     * Dirección hacia la derecha (Implica aumento de X).
     */
    DER,
    /**
     * Dirección hacia la izquierda (Implica disminución de X).
     */
    IZQ,
    /**
     * Dirección indefinida (no usada).
     */
    INDEF;
}
