import util.optionsparser.SortMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        SortMode sortMode1 = SortMode.ASCEND;
        SortMode sortMode2 = SortMode.DESCEND;
        Integer prev = 1;
        Integer next = 2;

        Comparator<Integer> comparator1 = sortMode1 == SortMode.ASCEND ? (o1, o2) -> o2 - o1 : Comparator.comparingInt(o -> o);
        Comparator<Integer> comparator2 = sortMode2 == SortMode.ASCEND ? (o1, o2) -> o2 - o1 : Comparator.comparingInt(o -> o);
        System.out.println(comparator1.compare(prev, next));
        System.out.println(comparator2.compare(next, prev));
    }
}
