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
    
    public static String fin(int id){ //Cliente-Servidor
        return "FIN;" + id;
    }
    
    public static String fin(){ //Servidor-Cliente
        return "FIN";
    }
    
    public static String err(String mensaje){
        return "ERR;" + mensaje;
    }
    
    public static boolean isDir(String mensaje){
        return mensaje.equals("DIR");
    }

    public static boolean isFin(String mensaje) {
        return mensaje.equals("FIN");
    }
}