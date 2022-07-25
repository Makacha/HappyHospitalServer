package Main;

import java.util.ArrayList;
import java.util.List;
import Entity.*;
import Map.GameMap;
import Socket.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GamePanel {
    private Connection connection = new Connection();
    public static final int originalTileSize = 16;
    public static final int scale = 2;

    public static final int tileSize = originalTileSize * scale;
    public static final int gameCol = 52;
    public static final int gameRow = 28;

    public static final int gameWidth = gameCol * tileSize;
    public static final int gameHeight = gameRow * tileSize;

    private int cntFrame = 0;
    private int slower = 0;
    private KeyboardInput keyboardInput = new KeyboardInput();
    private GameMap gameMap = new GameMap();
    private Player player;
    private Opponent opponent;
    private List<NPC> npcList = new ArrayList<>();

    public void run() {
        boolean shutDown = false;
        while (!shutDown) {
            String request = connection.getRequest();
            Command command = Connection.getCommand(request);
            String data = Connection.getData(request);
            switch (command) {
                case KEY_PRESSED:
                    keyboardInput.keyPressed(data);
                    break;
                case KEY_RELEASED:
                    keyboardInput.keyReleased(data);
                    break;
                case ADD:
                    add(data);
                    break;
                case UPDATE:
                    update();
                    break;
                case DRAW:
                    draw();
                    break;
                case LOAD_MAP:
                    load(data);
                    break;
                case SAVE_MAP:
                    save();
                    break;
                case FINISH:
                    shutDown = true;
                    break;
                default:
            }
        }
        connection.disconect();
    }

    private void add(String data) {
        if (data.equals("NPC")) {
            NPC newNPC = new NPC(this);
            npcList.add(newNPC);
        } else if (data.equals("player")) {
            player = new Player(this, keyboardInput);
        } else if (data.equals("opponent")) {
            if (opponent == null || opponent.isInDes())
                opponent = new Opponent(this);
        }
    }

    public void update() {
        cntFrame++;
        List<NPC> reachDesNPCList = new ArrayList<>();
        for (NPC npc : npcList) {
            if (npc.getReachDes() == true) {
                reachDesNPCList.add(npc);
            }
        }

        for (NPC npc : reachDesNPCList) {
            npcList.remove(npc);
        }

        for (NPC npc : npcList) {
            npc.update();
        }

        player.update();
        opponent.update();
    }

    public void draw() {
        for (NPC npc : npcList) {
            npc.draw(connection);
        }
        player.draw(connection);
        opponent.draw(connection);
        connection.sendData("slower " + slower);
        connection.sendData("end");
    }

    public void load(String data) {
        try {
            JSONObject object = new JSONObject(data);
            slower = object.getInt("slower");
            player.loadJson(object.getJSONObject("player").toString());
            opponent.loadJson(object.getJSONObject("opponent").toString());
            JSONArray npcArray = object.getJSONArray("npc");
            npcList.clear();
            for (int i = 0; i < npcArray.length(); i++) {
                NPC newNPC = new NPC(this);
                npcList.add(newNPC);
                npcList.get(i).loadJson(npcArray.getJSONObject(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        connection.sendData(this.toJson());
    }

    private String toJson() {
        StringBuilder result = new StringBuilder();
        result.append("{");
        result.append("\"slower\": " + slower + ",");
        result.append("\"player\": " + player.toJson() + ",");
        result.append("\"opponent\": " + opponent.toJson() + ",");
        result.append("\"npc\": [");
        for (int i = 0; i < npcList.size(); i++) {
            if (i > 0) result.append(",");
            result.append(npcList.get(i).toJson());
        }
        result.append("]");
        result.append("}");
        return result.toString();
    }

    public int getCntFrame() {
        return cntFrame;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public List<NPC> getNPCList() {
        return npcList;
    }

    public Opponent getOpponent() {
        return opponent;
    }

    public void incSlower() {
        slower++;
    }
}
