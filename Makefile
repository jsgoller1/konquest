JAVA:=/opt/homebrew/opt/openjdk/bin/java
JAVAC:=/opt/homebrew/opt/openjdk/bin/javac
SRC_DIR=src/
BIN_DIR:=bin/

# NOTE (joshua): Had my own makefile until Claude suggested this once as deps got hairy. 
# Find all .java files
SOURCES := $(shell find $(SRC_DIR) -name "*.java" -print)

.PHONY: clean run build

all: build run

# Ensure bin directory exists
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Compile everything in one shot
build: $(BIN_DIR)
	$(JAVAC) -cp $(SRC_DIR) -d $(BIN_DIR) $(SOURCES)

run: build
	$(JAVA) -cp $(BIN_DIR) Main

clean:
	reset
	-rm -rf $(BIN_DIR)