#!/usr/bin/env bash

function hi() {
    local NAME=$1
    echo "Hi $NAME"
    SET_BY_HI="Hi $NAME"
}

hello() {
    echo "Hello $1"
    SET_BY_HELLO="Hello $1"
}

hi "Maciek"
hello "Magda"

echo ${SET_BY_HI}
echo ${SET_BY_HELLO}


# Pipes:

FILES=`ls -1 | sort -r | head -3`

for FILE in ${FILES}
do
    echo "File: ${FILE}"
done


if [ ! -f "$1" ]
then
    echo "Provide a file parameter!"
    exit 1
fi


# Read a file

COUNT=1

# IFS - internal file separator
while IFS='' read -r LINE
do
    echo "LINE ${COUNT}: ${LINE}"
    ((COUNT++))
    sleep 0.05
done < "$1"


# cksum script_01.sh  - checksum


# watch a process

VALID=0
while [ ${VALID} -eq 0 ]
do
    read -p "Provide a PID to watch: " PID

    if [[ ( -z ${PID} ) || ( -z ${PID} ) ]]
    then
        echo "PID cannot be empty"
        continue
    elif [[ ! ${PID} =~ ^[0-9]+$ ]]
    then
        echo "PID must be a number"
        continue
    fi

    VALID=1
done


echo "Waiting for ${PID} to terminate"

STATUS=0
while [ ${STATUS} -eq 0 ]
do
    ps ${PID} > /dev/null
    STATUS=$?
done

echo "Process ${PID} terminated"