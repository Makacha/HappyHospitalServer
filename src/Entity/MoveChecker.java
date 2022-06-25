package Entity;

import java.util.List;

import Main.GamePanel;

public class MoveChecker {
    Character character;

    public MoveChecker (Character character){
        this.character = character;
    }
    
    public boolean buildingCollisionCheck() {
        int x = character.getX() + character.getSize() / 2;
        int y1 = character.getY();
        int y2 = character.getY() + character.getSize();

        int tileSize = GamePanel.tileSize;
        int col = GamePanel.screenCol;

        int colID = x / tileSize;
        int rowID1 = y1 / tileSize;
        int rowID2 = y2 / tileSize;

        if (character.getGameMap().getBuilding(rowID1 * col + colID).getBlock() == true) {
            return false;
        }

        if (character.getGameMap().getBuilding(rowID2 * col + colID).getBlock() == true) {
            return false;
        }

        return true;
    }

    public boolean pathDirectionCheck() {
        int x = character.getX() + character.getSize() / 2;
        int y = character.getY() + character.getSize();

        int tileSize = GamePanel.tileSize;
        int col = GamePanel.screenCol;

        int colID = x / tileSize;
        int rowID = y / tileSize;
        int direction = character.getDirection();

        if (direction == 0 ){
            return true;
        }

        if (direction == 1 && character.getGameMap().getPath(rowID * col + colID).getValue() == 4){
            return false;
        }

        if (direction == 2 && character.getGameMap().getPath(rowID * col + colID).getValue() == 3){
            return false;
        }

        if (direction == 3 && character.getGameMap().getPath(rowID * col + colID).getValue() == 1){
            return false;
        }

        if (direction == 4 && character.getGameMap().getPath(rowID * col + colID).getValue() == 2){
            return false;
        }

        return true;
    }

    public boolean npcCollisonCheck(){
        List<NPC> npcList = character.getGamePanel().getNPCList();

        int leftX = character.getX();
        int upY = character.getY();
        int rightX = leftX + character.getSize();
        int downY = upY + character.getSize();

        for (NPC npc : npcList){
            int npcLeftX = npc.getX();
            int npcRightX = npcLeftX + npc.getSize();
            int npcUpY = npc.getY();
            int npcDownY = npcUpY + npc.getSize();

            if (((npcLeftX < leftX && leftX < npcRightX) || (leftX < npcLeftX && npcLeftX < rightX))
                && ((npcUpY < downY && downY < npcDownY) || ( upY < npcDownY && npcDownY < downY))){
                    return false;
            }
        }

        return true;
    }
}
