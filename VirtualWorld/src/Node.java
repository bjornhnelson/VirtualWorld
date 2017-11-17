public class Node {
    private Point position;
    private Integer gScore;
    private Integer hScore;
    private Integer fScore;
    private Point previous;

    public Node(Point position, Integer startDistance, Integer heuristicDistance, Integer totalDistance, Point previous) {
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
        return fScore;
    }

    public int getFScore() {
        return hScore;
    }

    public Point getPrevious() {
        return previous;
    }

    public boolean equals(Object other) {
        if (getClass() != other.getClass() || other == null) {
            return false;
        }
        Node n1 = (Node)other;
        return position.equals(n1.getPosition()) && gScore == n1.getGScore() && hScore == n1.getHScore()
                && fScore == n1.getFScore() && previous.equals(n1.getPrevious());
    }


}
