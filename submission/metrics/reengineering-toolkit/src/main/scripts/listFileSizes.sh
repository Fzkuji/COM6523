#!/bin/bash

# Takes a single input, which is the target directory 
# within which to list all files (excluding directories)

for file in `find $1 -type f`
do
	total=$(wc -l < $file)
	echo "$file,$total"
done 
