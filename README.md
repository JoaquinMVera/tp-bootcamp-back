# Tp Bootcamp-Back

Proyecto hecho para el bootcamp de Allaria+ 2023
Lo hicimos en Scala, usando el framework Play!

## Requerimientos para correr el proyecto local
- Conectar la vpn de Daruma (Nexus). Por temas de configuracion del SBT en nuestras maquinas, nuestras dependencias siempre le pegaban a los repos de daruma. Puede ser que no haya problema al compilar en otro equipo, pero no lo probe
- Correr [este script de SQL](./app/accesData/sql/DatabaseInit.sql), teniendo una base postgreSQL configurada, y configurar adecuadamente el  [application.conf](./conf/application.conf) con tu perfil, esquema y usuario de SQL.
- Pararse sobre el directorio donde hayas clonado este repo, y hacer un `sbt clean compile run`. Luego, probar en POSTMAN en http://localhost:9000/

## Descripcion de los endpoints

### Venue

```scala
POST /venues (Carga una Venue)

En el body, se espera el siguiente JSON :
{
	"name": String,
	"address": String,
	"capacity": Int
	
}

No aceptaremos nombres ni direcciones vacias. Tampoco capacidades nulas o negativas.

Luego, devolveremos en un JSON la Venue creada en la DB
```

```scala
GET /Venues (Lista todas las Venues)

Devolveremos en un JSON las Venues con las que contamos en la db

```

### Shows

```scala
POST /shows (Carga un show, con sus performances y sus zonas)

En el body, se espera el siguiente JSON:

{
		"name": String
		"category": String
		"venueId": LONG
		"performances": DATE [
			{//YYYY-MM-DD}
		],
		"zones" : [
			{"name": String,"amount":Int,"price":BigDecimal}
		]
	}
}

No aceptaremos nombres vacio, categorias que no esten en la lista {musical,concert,theater}, performances y zonas vacias (o invalidas, usando el mismo criterio)
Luego, devolveremos un JSON con el show creado en la db
```

```scala
GET /shows (Devuelve todos los shows)

Devolveremos un JSON con todos los shows con los que contamos en la DB

```

```scala
GET /shows/[show_id]

Devuelve un JSON con el show correspondiente, junto a sus performances y sus zonas

```

```scala
PUT /shows/[show_id] (cambia el estado de las performances)

En el body,se espera un JSON con este formato:
{
	"performanceId": Long
	"state": Boolean
}

devolveremos un JSON con la performance, donde contara con su estado actualizado

```

### Users

```scala
POST /users (da de alta un usuario)

En el body, se espera un JSON con este formato:
{
	"name": String
	"email": String
}

Luego, devolveremos un JSON con el usuario creado en la db
```

``` scala
GET /users

Devolveremos un JSON con todos los usuarios de la db

```

``` scala
GET /users/[user_id]

Devolveremos un JSON con el usuario indicado de la db

```

### Bookings

``` scala
POST /bookings (Crea un booking,comprar tickets)

En el body, esperamos un JSON con el siguiente formato:

{
	"userId": Long
	"zoneId": Long
	"performanceId": Long
	"ticketAmount": Int
}

Esta accion solo se llevara a cabo si hay entradas disponibles y si el usuario tiene el balance para comprar las entradas que quiere

```

``` scala
GET /bookings?user=[UserId]

Devolveremos todos los bookings del usuario indicado, en formato JSON

```

