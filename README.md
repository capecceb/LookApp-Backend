# LookApp-Backend

Se sube una versión inicial con un login
Ejemplo

curl -X POST "http://localhost:8080/login" -d'{"username":"pedro","password":"pedro"}' -H "Content-Type: application/json"
{"login":"ok"}

curl -X POST "http://localhost:8080/login" -d'{"username":"pedro","password":"pedro2"}' -H "Content-Type: application/json"
{"login":"ok"}

Listar usuarios
curl "http://localhost:8080/users"

Listar un usuario
curl "http://localhost:8080/users/1"

Para Agregar un usuario
curl -X POST "http://localhost:8080/users" -d'{"username":"Pedro2","password":null,"roles":[]}' -H "Content-Type: application/json"
