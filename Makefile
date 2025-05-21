# Makefile for Java project

# Directories
SRC_DIR = src
BIN_DIR = bin
CLASS_DIR = $(BIN_DIR)/classes

# Output JAR name
JAR_NAME = app.jar

# Java compiler and options
JAVAC = javac
JAR = jar
JFLAGS = -d $(CLASS_DIR) --release 17

# All .java files under src
SOURCES := $(shell find $(SRC_DIR) -name "*.java")

# All .class files will be created in CLASS_DIR
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(CLASS_DIR)/%.class)

.PHONY: all clean run

# Default target
all: $(BIN_DIR)/$(JAR_NAME)

# Define your main class (adjust as needed)
MAIN_CLASS = src.Main
MANIFEST_FILE = manifest.txt

# Compile .java to .class
$(CLASS_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p $(dir $@)
	$(JAVAC) $(JFLAGS) $<

# Create the JAR file
# $(BIN_DIR)/$(JAR_NAME): $(CLASSES)
# 	@mkdir -p $(BIN_DIR)
# 	$(JAR) cf $@ -C $(CLASS_DIR) .

# Create the JAR file with manifest
$(BIN_DIR)/$(JAR_NAME): $(CLASSES)
	@mkdir -p $(BIN_DIR)
	echo "Main-Class: $(MAIN_CLASS)" > $(MANIFEST_FILE)
	echo "" >> $(MANIFEST_FILE)  # Manifest must end with a newline
	$(JAR) cfm $@ $(MANIFEST_FILE) -C $(CLASS_DIR) .
	rm -f $(MANIFEST_FILE)

# Run the application (optional main class)
run:
	java -jar $(BIN_DIR)/$(JAR_NAME)

run-term:
	java -jar $(BIN_DIR)/$(JAR_NAME) terminal

gui:
	$(JAVAC) $(JFLAGS) ./src/GUI/*.java
# Clean compiled classes and JAR
clean:
	rm -rf $(CLASS_DIR)
	rm -f $(BIN_DIR)/$(JAR_NAME)
