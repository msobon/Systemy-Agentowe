CLASSPATH := target:src:lib/*:lib/drools/*:lib/drools/lib/*

compile:
	javac src/pw/*.java -d target -cp ${CLASSPATH}

test:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -cp ${CLASSPATH} pw.MyBot" "java -jar example_bots/DualBot.jar" | java -jar tools/ShowGame.jar

test2:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -cp ${CLASSPATH} pw.KohonenBot" "java -cp ${CLASSPATH} pw.MyBot" | java -jar tools/ShowGame.jar

test3:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 10000 log.txt "java -cp ${CLASSPATH} pw.RuleBot" "java -cp ${CLASSPATH} pw.MyBot" | java -jar tools/ShowGame.jar

learn:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -cp ${CLASSPATH} pw.PerceptronBot" "java -jar example_bots/BullyBot.jar" | java -jar tools/ShowGame.jar

learn2:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -cp ${CLASSPATH} pw.PerceptronBot" "java -cp ${CLASSPATH} pw.MyBot" | java -jar tools/ShowGame.jar

learn3:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -cp ${CLASSPATH} pw.PerceptronBot learn" "java -jar example_bots/BullyBot.jar" | java -jar tools/ShowGame.jar

learn4:
	java -jar tools/PlayGame.jar maps/map5.txt 10000 200 log.txt "java -cp ${CLASSPATH} pw.PerceptronBot learn" "java -cp ${CLASSPATH} pw.MyBot" | java -jar tools/ShowGame.jar
