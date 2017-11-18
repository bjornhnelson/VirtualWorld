import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        Map<Point, Node> openList = new HashMap<>();
        Map<Point, Node> closedList = new HashMap<>();

        Node current = new Node(start, 0, start.heuristicDistance(end), start.heuristicDistance(end), null);
        openList.put(current.getPosition(), current);

        while (openList.size() > 0) {

            if (withinReach.test(current.getPosition(), end)) {
                List<Point> result = reconstructPath(closedList, current);
                return result;
            }

            List<Point> adjacentPoints = potentialNeighbors.apply(current.getPosition()).filter(canPassThrough).collect(Collectors.toList());
            for (Point p : adjacentPoints) {

                if (closedList.containsKey(p))
                    continue;

                Point a = p;
                int b = current.getGScore() + 1;
                if (openList.containsKey(p)) {
                    if (openList.get(p).getGScore() < b)
                        b = openList.get(p).getGScore();
                }
                int c = p.heuristicDistance(end);
                int d = b + c;
                Point e = current.getPosition();
                openList.put(a, new Node(a, b, c, d, e));
            }

            openList.remove(current.getPosition());
            closedList.put(current.getPosition(), current);

            Node min = null;
            for (Node n : openList.values()) {
                if (min == null)
                    min = n;
                if (n.getFScore() < min.getFScore())
                    min = n;
            }
            current = min;
        }
        List<Point> emptyList = new LinkedList<>();
        return emptyList;
    }

    private static List<Point> reconstructPath(Map<Point, Node> closedList, Node current) {
        List<Point> result = new LinkedList<>();

        boolean test = true;
        while (test) {
            if (current.getGScore() == 0) {
                test = false;
                continue;
            }
            result.add(0, current.getPosition());
            current = closedList.get(current.getPrevious());
        }
        return result;
    }

}
