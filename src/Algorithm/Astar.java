package Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import Map.GameMap;

public class Astar {
    private GameMap gameMap;
    private Node[] cell;
    private int col, row;

    public Astar(GameMap gameMap){
        this.gameMap = gameMap;
        col = gameMap.getCol();
        row = gameMap.getRow();
        cell = new Node[col*row];
        for (int j = 0; j < row; ++j) {
            for (int i = 0; i < col; ++i) {
                cell[j * col + i] = new Node();
                cell[j * col + i].setX(i);
                cell[j * col + i].setY(j);
            }
        }
        setNeighbors();
    }

    public void setNeighbors(){
        for (int j = 0; j < row; ++j){
            for (int i = 0; i < col; ++i){
                int id = j * col + i;
                int val = gameMap.getPath(id).getValue();
                if (val == 0){
                    continue;
                }

                if (j > 0){
                    int nid = (j - 1) * col + i;
                    int nval = gameMap.getPath(nid).getValue();
                    if ((val == 3 || val == 5 || val == 7 || val == 8 || val == 9 || val == 11 || val == 12)
                        && (nval == 3 || nval == 5 || nval == 6 || nval == 7 || nval == 9 || nval == 10 || nval == 13)){
                            cell[id].addBranch(cell[nid]);
                        }
                }

                if (j < row - 1){
                    int nid = (j + 1) * col + i;
                    int nval = gameMap.getPath(nid).getValue();
                    if ((val == 4 || val == 5 || val == 6 || val == 7 || val == 9 || val == 10 || val == 13)
                        && (nval == 4 || nval == 5 || nval == 7 || nval == 8 || nval == 9 || nval == 11 || nval == 12)){
                            cell[id].addBranch(cell[nid]);
                        }
                }

                if (i > 0){
                    int nid = j * col + (i - 1);
                    int nval = gameMap.getPath(nid).getValue();
                    if ((val == 2 || val == 5 || val == 6 || val == 8 || val == 9 || val == 12 || val == 13)
                        && (nval == 2 || nval == 5 || nval == 6 || nval == 7 || nval == 8 || nval == 10 || nval == 11)){
                            cell[id].addBranch(cell[nid]);
                        }
                }

                if (i < col - 1){
                    int nid = j * col + (i + 1);
                    int nval = gameMap.getPath(nid).getValue();
                    if ((val == 1 || val == 5 || val == 6 || val == 7 || val == 8 || val == 10 || val == 11)
                        && (nval == 1 || nval == 5 || nval == 6 || nval == 8 || nval == 9 || nval == 12 || nval == 13)){
                            cell[id].addBranch(cell[nid]);
                        }
                }
            }
        }
    }

    public int Manhattan(Node u, Node v) {
        return Math.abs(u.getX() - v.getX()) + Math.abs(u.getY() - v.getY());
    }

    public List<Node> findPath(int x, int y, int desx, int desy){
        for (int j = 0; j < row; ++j) {
            for (int i = 0; i < col; ++i) {
                cell[j * col + i].setF(Integer.MAX_VALUE);
                cell[j * col + i].setG(Integer.MAX_VALUE);
                cell[j * col + i].setParent(null);
            }
        }

        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        Node st = cell[y * col + x];
        Node des = cell[desy * col + desx];
        st.setG(0);
        st.setF(Manhattan(st, des));
        openList.add(st);

        while (!openList.isEmpty()) {
            Node node = openList.peek();
            if (node == des) {
                break;
            }
            for (Node nNode : node.getNeighbors()) {
                int totalWeight = node.getG() + 1;

                if (!openList.contains(nNode) && !closedList.contains(nNode)) {
                    nNode.setParent(node);
                    nNode.setG(totalWeight);
                    nNode.setF(nNode.getG() + Manhattan(nNode, des));
                    openList.add(nNode);
                } else {
                    if (totalWeight < nNode.getG()) {
                        nNode.setParent(node);
                        nNode.setG(totalWeight);
                        nNode.setF(nNode.getG() + Manhattan(nNode, des));

                        if (closedList.contains(nNode)) {
                            closedList.remove(nNode);
                            openList.add(nNode);
                        }
                    }
                }
            }

            openList.remove(node);
            closedList.add(node);
        }

        Node n = des;
        List<Node> path = new ArrayList<>();

        while (n.getParent() != null) {
            path.add(n);
            n = n.getParent();
        }
        path.add(n);
        Collections.reverse(path);
        return path;
    }
}
