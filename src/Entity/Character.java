package Entity;

import Main.GamePanel;
import Map.GameMap;
import Socket.Connection;

public abstract class Character {
    protected int x, y;
    protected int speed;
    protected int size;
    protected int state;
    protected int stun = 0;
    protected int direction = 0;
    protected GamePanel gamePanel;
    protected GameMap gameMap;

    public Character(GamePanel gamePanel) {
        this.setGamePanel(gamePanel);
        this.setGameMap(gamePanel.getGameMap());
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setStun(int stun) {
        this.stun = stun;
    }

    public int getStun() {
        return stun;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    abstract public void update();

    abstract public void draw(Connection connection);

    public GameMap getGameMap() {
        return gameMap;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

}
