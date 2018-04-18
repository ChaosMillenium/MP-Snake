# MP-Snake

Mensajes adicionales:

TAB;60;60: Tamaño tablero (2 argumentos: filas, columnas) Servidor -> Cliente<br /><br />

COI;1;0;0;1;1;2;2;3;3: Coordenadas iniciales de la serpiente (1 argumento: ID jugador+ 2 argumentos * Tamaño serpiente: Coordenadas de cada casilla que ocupa la serpiente inicialmente) Servidor -> Cliente<br />
Al conectarse un usuario, se envía un mensaje por jugador activo con esta cabecera las coordenadas de las serpientes actuales en juego.<br /><br />

ELJ;1: Informa de que un jugador indicado ha sido eliminado del juego (1 argumento: ID jugador) Servidor -> Cliente<br /><br />
TSR;10;10 Nuevo tesoro (2 argumentos: Coordenadas X e Y) Servidor -> Cliente<br />
Al conectarse un usuario, se envía un mensaje con esta cabecera las coordenadas de los tesoros en juego.
<br /><br />
