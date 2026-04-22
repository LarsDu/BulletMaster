SRC_DIR = src
BIN_DIR = bin
MAIN    = bulletmaster.game.Game
ENTRY   = $(SRC_DIR)/bulletmaster/game/Game.java

.PHONY: all run clean

all: $(BIN_DIR)
	javac -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(ENTRY)

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

run: all
	java -cp $(BIN_DIR) $(MAIN)

clean:
	rm -rf $(BIN_DIR)
