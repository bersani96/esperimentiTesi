#!/bin/bash
# Scenario:
# vengono effettuate N chiamate REST consecutive al servizio A.

N=50 # Numero di chiamate da effettuare

echo "Eseguo $N chiamate REST consecutive al servizio A"

for (( i=0; i<$N; i++ ))
do
    echo "Chiamata $((i+1))"
    ./curl-client.sh http://localhost:8080/a/prosegui " "
    echo ""
done

echo "Fatto"