package Map;

import java.io.*;

import Main.GamePanel;

public class GameMap {
    private Tile path[];
    private Tile building[];
    private int row, col;

    public GameMap() {
        row = GamePanel.gameRow;
        col = GamePanel.gameCol;
        path = new Tile[col * row];
        building = new Tile[col * row];
        setPath();
        setBuilding();
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setBuilding() {
        try {
            InputStream is = new FileInputStream("res/Map/building.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (int j = 0; j < row; ++j) {
                String line = br.readLine();
                String num[] = line.split(" ");
                for (int i = 0; i < col; ++i) {
                    building[j * col + i] = new Tile();
                    building[j * col + i].setValue(Integer.parseInt(num[i]));
                    if (building[j * col + i].getValue() != 0) {
                        building[j * col + i].setBlock(true);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public Tile getBuilding(int id) {
        return building[id];
    }

    public void setPath() {
        try {
            InputStream is = new FileInputStream("res/Map/path.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (int j = 0; j < row; ++j) {
                String line = br.readLine();
                String num[] = line.split(" ");
                for (int i = 0; i < col; ++i) {
                    path[j * col + i] = new Tile();
                    path[j * col + i].setValue(Integer.parseInt(num[i]));
                }
            }
            br.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public Tile getPath(int id) {
        return path[id];
    }
}
