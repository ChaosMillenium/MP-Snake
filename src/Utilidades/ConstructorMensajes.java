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
        for (int i = 0; i < coordenadas.length-1; i++){
            sb.append(coordenadas[i]).append(";");
        }
        sb.append(coordenadas[coordenadas.length-1]);
        return sb.toString();
    }
    
    public static String dir(String direccion,int id){
        return "DIR;" + id + ";" + direccion;
    }
}