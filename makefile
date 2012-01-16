CLASSPATH := target:src:lib/*:lib/drools/*:lib/drools/lib/*

compile:
	javac src/pw/*.java -d target -cp ${CLASSPATH}

test:
	java -Duser.country=EN -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -Duser.country=EN -cp ${CLASSPATH} pw.MyBot" "java -Duser.country=EN -jar example_bots/DualBot.jar" | java -Duser.country=EN -jar tools/ShowGame.jar
