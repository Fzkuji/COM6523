#!/bin/bash

#INSERT YOUR TARGET CSV FILE BELOW
DATASTORE="../$1.csv"

#INSERT THE ROOT DIRECTORY OF THE REPO YOU ARE ANALYSING BELOW
REPO_DIR="$1"


cd $REPO_DIR

# Check out the current head from the git repository
# git checkout master

echo "Timestamp, Message, Committer, Added, Removed, File" > ${DATASTORE}

# Obtain an array of hash-codes for the previous versions.

VERSIONS=($(git log --no-merges --pretty=format:"%h" ))

# Iterate through the array of versions, and for each version do

for i in ${VERSIONS[@]}; do

    #Obtain the source code for the current version i by checking it out.
    #ONLY IF YOU NEED TO ACCESS CODE - WASTES A LOT OF TIME OTHERWISE
    #git checkout -f $i

    #Obtain the date that version i was committed (via git show)
	DATE=$(git show -s --format='%ct' $i)

    #Obtain the commit message for the current version (also via git show)
    MESSAGE=$(git show -s --format='%B' $i | tr '\r\n,' ' ')

    #Obtain the author ID for the current version (also via git show)
    AUTHOR=$(git show -s --format='%an' $i)

    #Obtain individual file change data
    FILE_CHANGES=$(git show --numstat --no-renames --format="%n" $i)

    arr=()
    while read -r line; do
        check=$(echo "$line" | tr -d " \t\n\r")
        if [ "$check" != "" ]; then
            REPLACE_WITH_COMMA=${line//	/,}
            arr+=("$REPLACE_WITH_COMMA")
        fi
    done <<< "${FILE_CHANGES}"

    for k in ${arr[*]}; do
        echo $k
        echo "\"$DATE\", \"$MESSAGE\", \"$AUTHOR\", "${k} >> ${DATASTORE}
    done

done
