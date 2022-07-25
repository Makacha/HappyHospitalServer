package Entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        if (pos >= path.size() - 1) {
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

    public String toJson() {
        StringBuilder result = new StringBuilder();
        result.append("{");
        result.append("\"x\": " + x + ",");
        result.append("\"y\": " + y + ",");
        result.append("\"speed\": " + speed + ",");
        result.append("\"size\": " + size + ",");
        result.append("\"state\": " + state + ",");
        result.append("\"stun\": " + stun + ",");
        result.append("\"direction\": " + direction + ",");
        result.append("\"pos\": " + pos + ",");
        result.append("\"desCol\": " + desCol + ",");
        result.append("\"desRow\": " + desRow + ",");
        result.append("\"reachDes\": " + reachDes + ",");
        result.append("\"path\": " + "[");
        for (int i = 0; i < path.size(); i++) {
            if (i > 0)
                result.append(",");
            result.append("{");
            result.append("\"x\": " + path.get(i).getX() + ",");
            result.append("\"y\": " + path.get(i).getY());
            result.append("}");
        }
        result.append("]");
        result.append("}");
        return result.toString();
    }

    public void loadJson(String json) {
        try {
            JSONObject object = new JSONObject(json);
            x = object.getInt("x");
            y = object.getInt("y");
            speed = object.getInt("speed");
            size = object.getInt("size");
            state = object.getInt("state");
            stun = object.getInt("stun");
            direction = object.getInt("direction");
            pos = object.getInt("pos");
            desCol = object.getInt("desCol");
            desRow = object.getInt("desRow");
            reachDes = object.getBoolean("reachDes");
            path = new ArrayList<>();
            JSONArray pathJson = object.getJSONArray("path");
            for (int i = 0; i < pathJson.length(); i++) {
                Node tmp = new Node();
                tmp.setX(pathJson.getJSONObject(i).getInt("x"));
                tmp.setY(pathJson.getJSONObject(i).getInt("y"));
                path.add(tmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
