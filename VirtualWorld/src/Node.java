public class Node {
    private Point position;
    private int gScore;
    private int hScore;
    private int fScore;
    private Point previous;

    public Node(Point position, int gScore, int hScore, int fScore, Point previous) {
        this.position = position;
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = fScore;
        this.previous = previous;
    }

    public Point getPosition() {
        return position;
    }

    public int getGScore() { return gScore; }

    public int getHScore() {
        return hScore;
    }

    public int getFScore() {
        return fScore;
    }

    public Point getPrevious() {
        return previous;
    }

    public boolean equals(Object other) {
        if (getClass() != other.getClass())
            return false;
        if (other == null)
            return false;
        Node n1 = (Node)other;
        return position.equals(n1.getPosition()) && gScore == n1.getGScore() && hScore == n1.getHScore()
                && fScore == n1.getFScore() && previous.equals(n1.getPrevious());
    }

    public String toString() {
        return "Node: " + position + ", " + gScore + ", " + hScore + ", " + fScore + ", " + previous;
    }

    // compareTo method

}
