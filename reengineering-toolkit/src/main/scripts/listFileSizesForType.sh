#!/bin/bash

# Takes two inputs, first is the target directory 
# second is the file pattern (e.g. *.java)

for file in `find $1 -type f -name $2`
do
	total=$(wc -l < $file)
	echo "$file,$total"
done 
