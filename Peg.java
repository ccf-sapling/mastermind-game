public class Peg {
    private String colour;

    public Peg(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public boolean equals(Object obj) {
        // if both objects point to the same reference
        if (this == obj) {
            return true;
        }
        // if obj is null or the two objects aren't of the same class
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Peg otherPeg = (Peg) obj; // cast obj to Peg
        return this.colour.equals(otherPeg.colour); // compare using String's compare method
    }
    public String toString() {
        return this.getColour();
    }
}
