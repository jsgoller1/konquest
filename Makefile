# JAVA:=/opt/homebrew/opt/openjdk/bin/java
# JAVAC:=/opt/homebrew/opt/openjdk/bin/javac

JAVA:=java
JAVAC:=javac
SRC_DIR=src
BIN_DIR:=bin

# NOTE (joshua): Had my own makefile until Claude suggested this once as deps got hairy. 
# Find all .java files
# SOURCES := $(shell find $(SRC_DIR) -name "*.java" -print)

ifeq ($(OS),Windows_NT)
    SOURCES := $(shell dir /s /b $(SRC_DIR)\*.java)
else
    SOURCES := $(shell find $(SRC_DIR) -name "*.java" -print)
endif

.PHONY: clean run build

all: build run-debug

# Ensure bin directory exists
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Compile everything in one shot
build: $(BIN_DIR)
	$(JAVAC) -cp $(SRC_DIR) -d $(BIN_DIR) $(SOURCES)

run-debug: build
	$(JAVA) -ea -cp $(BIN_DIR) Main --log-level debug --mute-sound

run-prod: build
	$(JAVA) -cp $(BIN_DIR) Main --log-level info

clean:
	reset
	-rm -rf $(BIN_DIR)

# This works only on MacOS
count-lines: 
	@echo "Project LoC (Java only)"
	@find . -name "*.java" | xargs wc -l | tail -1 | awk '{print $1}'