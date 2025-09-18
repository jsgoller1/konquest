JAVA:=/opt/homebrew/opt/openjdk/bin/java
JAVAC:=/opt/homebrew/opt/openjdk/bin/javac
BIN_DIR:=bin

.PHONY: 
 
clean:
	reset
	-rm -r $(BIN_DIR)
	mkdir $(BIN_DIR)

main: clean logger 
	$(JAVAC) -cp $(BIN_DIR) src/Main.java -d $(BIN_DIR)
	$(JAVA) -cp $(BIN_DIR) Main

logger:
	$(JAVAC) src/logging/Logger.java -d $(BIN_DIR)
