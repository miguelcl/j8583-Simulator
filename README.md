j8583-Simulator
===============

Generacion de Mensajes de prueba ISO-8583 sobre TCP


Modo de uso
------------------------------

java -jar j8583-Simulator.jar
usage: 2 [-d <arg>] -defF [-delay <arg>] [-h] -host <arg> [-m <arg>] -port
       <arg> [-r <arg>] [-s <arg>] [-t <arg>] [-tout <arg>] [-v]
 -d,--dStart <arg>        Thread start delay frecuency.
 -defF                    Path to definition file path.
 -delay <arg>             Delay between thread launches.
 -h,--help                Print help for this application.
 -host <arg>              Ip of the server to make the connection.
 -m,--messageFrec <arg>   Thread message frecuency.
 -port <arg>              Port in which the server attempt for connection.
 -r,--Recieve <arg>       Message type to get on response , example 210.
 -s,--Send <arg>          Message type to send, example 100.
 -t,--threads <arg>       Number of threads.
 -tout,--timeout <arg>    Connection timeout.
 -v,--verbose             Enable verbose mode.


