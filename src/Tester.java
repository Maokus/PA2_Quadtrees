import java.util.Arrays;

public class Tester {
    public static void main(String[] args) {
        /*int[][] values = new int[][]{{6,4,7,4},
                                     {5,6,3,8},
                                     {9,4,2,4},
                                     {9,1,3,4}};*/
        int[][] values = new int[][]{{1,2,1,4},
                                     {5,6,7,8},
                                     {9,10,11,12},
                                     {13,14,15,16}};
        QuadNode node = constructQuadTree(values);

        System.out.println(node);

    }

    public static void printRectData(Object[] s){
        System.out.print(String.format("Min is: %d, ", s[0]));
        double[] s1 = (double[]) s[1];
        System.out.println(Arrays.toString(s1));
        System.out.println();
    }

    public static QuadNode constructQuadTree(int[][] values){
        QuadNode node = new QuadNode(new Double[][]{{0.0, 0.0}, {4.0, 4.0}});

        node.genChildren();

        for (int i = 0; i < 4; ++i) {
            QuadNode[] children = node.getChildren();
            children[i].genChildren();
        }

        for (double i = 0; i < 4; ++i){
            for (double j = 0; j < 4; ++j){
                node.updateQuadNode(j + 0.5, i + 0.5, values[(int) i][(int) j], false);
            }
        }

        return node;
    }
}
