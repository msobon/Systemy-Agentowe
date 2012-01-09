set lang=en_US
set LC_ALL=C
set LC_NUMERIC=C

java -Duser.country=EN -jar tools/PlayGame.jar maps/map5.txt 1000 1000 log.txt "java -Duser.country=EN -cp target MyBot" "java -Duser.country=EN -jar example_bots/DualBot.jar" | java -Duser.country=EN -jar tools/ShowGame.jar
