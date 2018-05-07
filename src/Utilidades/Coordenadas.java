package Utilidades;

/**
 * Clase de ayuda para operar con coordenadas (valores X e Y de una posición del
 * tablero).
 *
 * @author Iván Chicano Capelo, Daniel Diz Molinero, David Muñoz Alonso
 */
public class Coordenadas {

    private int x, y;

    /**
     * Construye un objeto coordenadas a partir de los valores X e Y.
     *
     * @param x
     * @param y
     */
    public Coordenadas(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve la coordenada X.
     *
     * @return X.
     */
    public int getX() {
        return x;
    }

    /**
     * Devuelve la coordenada Y.
     *
     * @return Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Suma el parámetro a la coordenada X actual.
     *
     * @param x Valor a sumar.
     */
    public void aumentarX(int x) {
        this.x += x;
    }

    /**
     * Suma el parámetro a la coordenada X actual.
     *
     * @param y Valor a sumar.
     */
    public void aumentarY(int y) {
        this.y += y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coordenadas other = (Coordenadas) obj;
        if (this.x != other.getX()) {
            return false;
        }
        if (this.y != other.getY()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.x;
        hash = 83 * hash + this.y;
        return hash;
    }
}
