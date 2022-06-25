package Entity;

import java.util.List;

import Algorithm.*;
import Main.GamePanel;
import Socket.Connection;

public class Opponent extends Character {
    private MoveChecker moveChecker;
    private Astar astar;
    private int pos = 0;
    private List<Node> path;

    public Opponent(GamePanel gamePanel) {
        super(gamePanel);
        this.setX(0);
        this.setY(GamePanel.screenHeight / 2 + GamePanel.originalTileSize / 2);
        this.setSpeed(1);
        this.setSize(GamePanel.originalTileSize);
        moveChecker = new MoveChecker(this);
        astar = new Astar(gamePanel.getGameMap());
        int colId = this.getX() / GamePanel.tileSize;
        int rowId = this.getY() / GamePanel.tileSize;
        path = astar.findPath(colId, rowId, GamePanel.screenCol - 1, rowId);

    }

    public boolean Inside(Node u) {
        if (this.getX() != u.getX() * GamePanel.tileSize + GamePanel.originalTileSize / 2
                || this.getY() != u.getY() * GamePanel.tileSize
                        + GamePanel.originalTileSize / 2) {
            return false;
        }
        return true;
    }

    public void update() {
        if (pos == path.size() - 1) {
            return;
        }

        if (this.getStun() > 0) {
            this.setStun(this.getStun() - 1);
            return;
        }

        Node c1 = path.get(pos);
        Node c2 = path.get(pos + 1);
        int cntFrame = gamePanel.getCntFrame();
        if (c1.getX() == c2.getX()) {
            if (c1.getY() < c2.getY()) {
                this.setY(this.getY() + this.getSpeed());
                if (cntFrame % 30 < 15) {
                    state = 1;
                } else {
                    state = 2;
                }
            } else if (c1.getY() > c2.getY()) {
                this.setY(this.getY() - this.getSpeed());
                if (cntFrame % 30 < 15) {
                    state = 3;
                } else {
                    state = 4;
                }
            }
        } else {
            if (c1.getX() < c2.getX()) {
                this.setX(this.getX() + this.getSpeed());
                if (cntFrame % 30 < 15) {
                    state = 7;
                } else {
                    state = 8;
                }
            } else if (c1.getX() > c2.getX()) {
                this.setX(this.getX() - this.getSpeed());
                if (cntFrame % 30 < 15) {
                    state = 5;
                } else {
                    state = 6;
                }
            }
        }

        if (moveChecker.npcCollisonCheck() == false) {
            this.setStun(60 * 3);
        }

        if (Inside(c2)) {
            ++pos;
        }
    }

    public void draw(Connection connection) {
        connection.sendData("opponent " + state + " " 
        + this.getX() + " " + this.getY() + " "
        + this.getSize() + " " + this.getSize());
    }

}
