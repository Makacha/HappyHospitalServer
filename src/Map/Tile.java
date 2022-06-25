package Map;

public class Tile {
    private boolean block = false;
    private int value = 0;

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean getBlock() {
        return block;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
