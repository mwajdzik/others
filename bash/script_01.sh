#!/usr/bin/env bash

# $1, ...
# $@ all params

NAME=Maciek
echo "My name is $NAME!"

PARAM_1=$1
PARAM_2=$2
echo "Your parameters are $PARAM_1 and $PARAM_2"

SYSTEM=`uname`
echo "System name is $SYSTEM"

echo "PATH is: $PATH"
echo "TERM is: $TERM"

if [ -z "${EDITOR}" ]
then
    echo "EDITOR is not set!"
fi

# HOME, PATH, HOSTNAME, SHELL, USER, TERM

# for comparing numbers: -eq, -ne, -lt, -gt, -le, -ge

if [ "${SYSTEM}" = "Darwin" ]
then
    echo "OSX!"
elif [ ${SYSTEM} = "Linux" ]
then
    echo "Linux!"
else
    echo "Something else!"
fi


COUNT=0

while [ ${COUNT} -lt 5 ]
do
    echo "COUNT = ${COUNT}"
    ((COUNT++))
done

# break, continue

for PARAM in "$@"
do
    echo "Param: ${PARAM}"
done


# EXIT code can be read from $?
exit 0
