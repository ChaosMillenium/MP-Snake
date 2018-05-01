package Utilidades;

public class ConstructorMensajes {

    public static String idc(int id) {
        return "IDC;" + id;
    }

    public static String tab(int filas, int columnas) {
        return "TAB;" + filas + ";" + columnas;
    }

    public static String tsp(int tamaño) {
        return "TSP;" + tamaño;
    }

    public static String coi(int[] coordenadas, int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("COI;").append(id).append(";");
        for (int i = 0; i < coordenadas.length - 1; i++) {
            sb.append(coordenadas[i]).append(";");
        }
        sb.append(coordenadas[coordenadas.length - 1]);
        return sb.toString();
    }

    public static String dir(String direccion) {
        return "DIR;" + direccion;
    }

    public static String fin(int id) { //Cliente-Servidor
        return "FIN;" + id;
    }

    public static String fin() { //Servidor-Cliente
        return "FIN";
    }

    public static String err(String mensaje) {
        return "ERR;" + mensaje;
    }

    public static String elj(int id, int[] coordenadas) {
        StringBuilder sb = new StringBuilder();
        sb.append("ELJ;").append(id).append(";");
        for (int i = 0; i < coordenadas.length - 1; i++) {
            sb.append(coordenadas[i]).append(";");
        }
        sb.append(coordenadas[coordenadas.length - 1]);
        return sb.toString();
    }

    public static String mov(int id, int cabezaX, int cabezaY, int colaX, int colaY) {
        return "MOV;" + id + ";" + cabezaX + ";" + cabezaY + ";" + colaX + ";" + colaY;
    }

    public static String pts(int id, int puntos) {
        return "PTS;" + id + ";" + puntos;
    }

    public static String tsr(int x, int y) {
        return "TSR;" + x + ";" + y;
    }

    public static String man(boolean manual) {
        int manualInt;
        if (manual) {
            manualInt = 1;
        } else {
            manualInt = 0;
        }
        return "MAN;" + manualInt;
    }

    public static boolean isDir(String mensaje) {
        return mensaje.equals("DIR");
    }

    public static boolean isFin(String mensaje) {
        return mensaje.equals("FIN");
    }

    public static boolean isMan(String mensaje) {
        return mensaje.equals("MAN");
    }
}
