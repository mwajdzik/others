```shell
docker run \
    -e POSTGRES_HOST_AUTH_METHOD=trust \
    -p 5432:5432 \
    postgres:13-alpine

docker run \
    --rm -it \
    --network=host \
    postgres:13-alpine \
    psql -U postgres -h localhost

echo -n 'error: Please reinstall universe and reboot!' | go run .
```

\d show tables

\q quit

SELECT * FROM logs;

echo -n $'Hacked!\'); DROP TABLE logs; --"' | go run .

------
ISSUE:
------

    INSERT INTO logs (time, message) VALUES ('%s', '%s');
    
    message = Hacked!'); DROP TABLE logs; --"
    
    INSERT INTO logs (time, message) VALUES ('%s', 'Hacked!'); DROP TABLE logs; --"

Use:

    INSERT INTO logs (time, message) VALUES ($1, $2);
