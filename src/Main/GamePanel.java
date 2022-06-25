package Main;

import java.util.ArrayList;
import java.util.List;
import Entity.*;
import Map.GameMap;
import Socket.*;

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
        connection.sendData("end");
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
}
