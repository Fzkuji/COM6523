chmod +x listFiles.sh
chmod +x listFileSizes.sh
chmod +x repMining.sh
./listFiles.sh joda-time/src/main '*.java' > javaFiles.csv
./listFileSizes.sh joda-time/src/main '*.java' > javaFileSizes.csv
./repMining.sh joda-time > repCommits.csv