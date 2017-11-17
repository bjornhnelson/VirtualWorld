import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy  {

    public List<Point> computePath(Point start, Point end,
                                          Predicate<Point> canPassThrough,
                                          BiPredicate<Point, Point> withinReach,
                                          Function<Point, Stream<Point>> potentialNeighbors) {
        LinkedList<Node> openList = new LinkedList<Node>();
        LinkedList<Node> closedList = new LinkedList<Node>();

        Node current = new Node(start, 0, heuristicDistance(start, end), heuristicDistance(start, end), null);
        openList.add(current);

        while (openList.size() > 0) {

            Node minF = null;
            for (Node n : openList)  {
                if (minF == null)
                    minF = n;
                else
                    if (n.getFScore() < minF.getFScore())
                        minF = n;
            }
            current = minF;

            if (withinReach.test(current.getPosition(), end))
                return reconstructPath(closedList, current.getPosition());

            openList.remove(current);
            closedList.add(current);

            List<Point> adjacentPoints = potentialNeighbors.apply(current.getPosition()).filter(canPassThrough).collect(Collectors.toList());
            for (Point p : adjacentPoints) {

                for (Node n : closedList)
                    if (n.getPosition() == p)
                        continue;

                boolean addToList = true;
                for (Node n : openList) {
                    if (n.getPosition() == p)
                        addToList = false;
                }

                if (!addToList)
                    continue;
                else {
                    Integer tentativeGScore = current.getGScore() + heuristicDistance(current.getPosition(), p);
                    if (tentativeGScore >= heuristicDistance(start, p))
                        continue;
                    Point a = p;
                    Integer b = tentativeGScore;
                    Integer c = heuristicDistance(p, end);
                    Integer d = b + c;
                    Point e = current.getPosition();

                    Node addedNode = new Node(a, b, c, d, e);

                    openList.add(addedNode);
                }
            }

        }
        return null;
    }

    public static int heuristicDistance(Point p1, Point p2) {
        int distance = Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
        return distance;
    }

    public List<Point> reconstructPath(LinkedList<Node> closedList, Point current) {
        List<Point> result = new LinkedList<>();
        result.add(0, current);
        boolean continueRun = true;
        while (continueRun) {
            for (Node n : closedList) {
                if (n.getPosition().equals(current))
                    current = n.getPrevious();
                    if (current == null) {
                        continueRun = false;
                    }
                    else {
                        result.add(0, current);
                    }
            }
        }
        return result;
    }

}
