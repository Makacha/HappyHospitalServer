package Main;

public class KeyboardInput {
    private boolean keyW, keyS, keyA, keyD;

    public void keyPressed(String inp) {
        if (inp.equals("W")) {
            keyW = true;
        } else if (inp.equals("S")) {
            keyS = true;
        }
        if (inp.equals("A")) {
            keyA = true;
        } else if (inp.equals("D")) {
            keyD = true;
        }
    }

    public void keyReleased(String inp) {

        if (inp.equals("W")) {
            keyW = false;
        }
        if (inp.equals("A")) {
            keyA = false;
        }
        if (inp.equals("S")) {
            keyS = false;
        }
        if (inp.equals("D")) {
            keyD = false;
        }
    }

    public boolean getKeyW() {
        return this.keyW;
    }

    public boolean getKeyA() {
        return this.keyA;
    }

    public boolean getKeyS() {
        return this.keyS;
    }

    public boolean getKeyD() {
        return this.keyD;
    }

}
