ARGS = $(filter-out $@,$(MAKECMDGOALS))
MAKEFLAGS += --silent

build: 
	javac *.java

run_rmi: build
	rmiregistry 7

run_server: build
	java ServerImpl

run_client: build
	java Client $(ARGS)

clear:
	rm -rf *.class

.PHONY: build run_rmi run_server run_client