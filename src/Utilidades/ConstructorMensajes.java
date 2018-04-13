package Utilidades;

public class ConstructorMensajes{
    public static String idc(int id){
        return "IDC;"+id;
    }
    public static String tab(int filas,int columnas){
        return "TAB;"+filas+";"+columnas;
    }
    public static String tsp(int tamaño){
        return "TSP;"+tamaño;
    }
    public static String coi(int[] coordenadas){
        StringBuilder sb = new StringBuilder();
        sb.append("COI;");
        for (int coordenada : coordenadas){
            sb.append(coordenada).append(";");
        }
        return sb.toString();
    }
}