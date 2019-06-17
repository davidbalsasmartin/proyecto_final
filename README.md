# GYMADE

## Introducción al proyecto.
Proyecto desarrollado en Spring y Angular, con el objetivo de mejorar la rutina deportiva, haciendola más simple para el usuario final.


## Herramientas y tecnologías utilizadas.
- Back:
Spring boot 2.x
Jwt
Spring Security
Java Mail

- Front:
Angular 7.x
NGX Bootstrap
Angular Material
NG2Charts

- BD:
Postgresql
PgAdmin

## Explicación del proceso de desarrollo.
Para realizar el desarrollo de la aplicación se ha dividido en 3 fases, una por cada capa:
* Back
Primero se ha realizado el el back con Spring-Boot 2.x, incluyendo Spring Security, con encriptación de claves, y sirviendome de Json Web Token también para su seguridad.
Se ha hecho uso también de Java Mail y más...
Este se ha dividido en capas: Controller, Service y Repository

* Front
Después se ha realizado el front en Angular 7, conectando con la seguridad, haciendo uso de dos interceptors, uno para los errores,
que captura el error o en caso de ser crítico, hace logout automatático.
Y otro para la implementación del token en las llamadas.
Se ha introducido Angula material con NG2Charts para estilizar la página, sobretodo los bordes y las 'animaciones', aunque se ha retocado, siendo el estilo propio.

* Base de Datos
Como BD se ha utilizado Postgresql, haciendo uso de PGAdmin4 para poder visualizarlo en un entorno gráfico, más amigable.


## Demostración de funcionamiento de la aplicación.

https://gymadeback.herokuapp.com/

https://gymadefront.herokuapp.com/
