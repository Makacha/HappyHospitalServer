package Entity;

import java.util.List;

import Algorithm.*;
import Main.GamePanel;
import Socket.Connection;

public class NPC extends Character {

    private Astar astar;
    private int pos = 0;
    private List<Node> path;
    private int desCol, desRow;
    private boolean reachDes = false;

    public NPC(GamePanel gamePanel) {
        super(gamePanel);
        this.setSpeed(1);
        this.setSize(GamePanel.originalTileSize);
        int randX, randY;
        while (true) {
            randX = (int) (Math.random() * GamePanel.gameCol);
            randY = (int) (Math.random() * GamePanel.gameRow);
            if (this.getGameMap().getPath(randY * GamePanel.gameCol + randX).getValue() != 0) {
                break;
            }
        }
        this.setX(randX * GamePanel.tileSize + GamePanel.originalTileSize / 2);
        this.setY(randY * GamePanel.tileSize + GamePanel.originalTileSize / 2);

        while (true) {
            randX = (int) (Math.random() * GamePanel.gameCol);
            randY = (int) (Math.random() * GamePanel.gameRow);
            if (this.getGameMap().getPath(randY * GamePanel.gameCol + randX).getValue() != 0) {
                break;
            }
        }
        desCol = randX;
        desRow = randY;

        astar = new Astar(gamePanel.getGameMap());
        int colId = this.getX() / GamePanel.tileSize;
        int rowId = this.getY() / GamePanel.tileSize;
        path = astar.findPath(colId, rowId, desCol, desRow);
    }

    public boolean Inside(Node u) {
        if (this.getX() != u.getX() * GamePanel.tileSize + GamePanel.originalTileSize / 2
                || this.getY() != u.getY() * GamePanel.tileSize
                        + GamePanel.originalTileSize / 2) {
            return false;
        }
        return true;
    }

    public boolean getReachDes() {
        return reachDes;
    }

    public void update() {
        if (pos == path.size() - 1) {
            reachDes = true;
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

        if (Inside(c2)) {
            ++pos;
        }
    }

    public void draw(Connection connection) {
        connection.sendData("NPC " + state + " "
                + this.getX() + " " + this.getY() + " "
                + this.getSize());
    }
}
