package Algorithm;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
    private int x,y;
    private Node parent = null;
    private List<Node> neighbors;
    private int f = Integer.MAX_VALUE;
    private int g = Integer.MAX_VALUE;

    public Node(){
        this.neighbors = new ArrayList<>();
    }

    @Override
    public int compareTo(Node n) {
          return Integer.compare(this.f, n.f);
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getF() {
        return f;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getG() {
        return g;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
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

    public void addBranch(Node node){
        neighbors.add(node);
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }
}

