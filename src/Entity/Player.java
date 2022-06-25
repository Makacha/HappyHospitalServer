package Entity;

import Main.GamePanel;
import Main.KeyboardInput;
import Socket.Connection;

public class Player extends Character {
    private KeyboardInput keyboardInput;
    private MoveChecker moveChecker;
    private int lastKey = 0;

    public Player(GamePanel gamePanel, KeyboardInput keyboardInput) {
        super(gamePanel);
        this.keyboardInput = keyboardInput;        
        this.setX(0);
        this.setY(GamePanel.screenHeight / 2 - GamePanel.tileSize);
        this.setSpeed(1);
        this.setSize(GamePanel.originalTileSize);
        moveChecker = new MoveChecker(this);
    }

    public boolean validMove() {
        int x = this.getX();
        int y = this.getY();
        int size = this.getSize();
        if (y < 0 || y + size > GamePanel.screenHeight) {
            return false;
        }
        if (x < 0 || x + size > GamePanel.screenWidth) {
            return false;
        }
        if (moveChecker.buildingCollisionCheck() == false) {
            return false;
        }
        if (moveChecker.pathDirectionCheck() == false) {
            return false;
        }
        return true;
    }

    public void update() {
        int cntFrame = gamePanel.getCntFrame();
        int[] states = {8, 10, 9, 5, 7};
        if (this.getStun() > 0 || (keyboardInput.getKeyW() == false && keyboardInput.getKeyS() == false
                && keyboardInput.getKeyA() == false && keyboardInput.getKeyD() == false)) {
            state = states[lastKey];
        }

        if (this.getStun() > 0) {
            this.setStun(this.getStun() - 1);
            return;
        }

        if (keyboardInput.getKeyW() == true) {
            this.setY(this.getY() - this.getSpeed());
            lastKey = 1;
            this.setDirection(1);
            if (cntFrame % 30 < 15) {
                state = 3;
            } else {
                state = 4;
            }
            if (validMove() == false) {
                this.setY(this.getY() + this.getSpeed());
                this.setDirection(0);
            }
        }

        if (keyboardInput.getKeyS() == true) {
            this.setY(this.getY() + this.getSpeed());
            lastKey = 2;
            this.setDirection(2);
            if (cntFrame % 30 < 15) {
                state = 1;
            } else {
                state = 2;
            }
            if (validMove() == false) {
                this.setY(this.getY() - this.getSpeed());
                this.setDirection(0);
            }
        }

        if (keyboardInput.getKeyA() == true) {
            this.setX(this.getX() - this.getSpeed());
            lastKey = 3;
            this.setDirection(3);
            if (cntFrame % 30 < 15) {
                state = 5;
            } else {
                state = 6;
            }
            if (validMove() == false) {
                this.setX(this.getX() + this.getSpeed());
                this.setDirection(0);
            }
        }

        if (keyboardInput.getKeyD() == true) {
            this.setX(this.getX() + this.getSpeed());
            lastKey = 4;
            this.setDirection(4);
            if (cntFrame % 30 < 15) {
                state = 7;
            } else {
                state = 8;
            }
            if (validMove() == false) {
                this.setX(this.getX() - this.getSpeed());
                this.setDirection(0);
            }
        }

        if (moveChecker.npcCollisonCheck() == false) {
            this.setStun(60 * 3);
        }
    }

    public void draw(Connection connection) {
        connection.sendData("player " + state + " " 
        + this.getX() + " " + this.getY() + " "
        + this.getSize() + " " + this.getSize());
    }


}
