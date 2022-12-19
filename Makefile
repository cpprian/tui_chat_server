
build: 
	javac -d bin -sourcepath src src/client/ClientImpl.java src/server/ServerImpl.java

run_server: build
	java -cp bin server.ServerImpl

run_client: build
	java -cp bin client.ClientImpl

clear:
	rm -rf bin/*

.PHONY: build run_server run_client