CLASSPATH := target:lib/*:lib/drools/*:lib/drools/lib/*

compile:
	javac src/*.java -d target -cp lib/neuroph-2.5.1.jar:lib/drools/drools-api-5.1.1.jar:src

test:
	echo ${CLASSPATH}
	java -Duser.country=EN -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -Duser.country=EN MyBot" "java -Duser.country=EN -jar example_bots/DualBot.jar" | java -Duser.country=EN -jar tools/ShowGame.jar
