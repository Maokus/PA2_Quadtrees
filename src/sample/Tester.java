package sample;

import java.util.Arrays;

public class Tester {
    public static void main(String[] args) {
        /*int[][] values = new int[][]{{6,4,7,4},
                                     {5,6,3,8},
                                     {9,4,2,4},
                                     {9,1,3,4}};*/
        int[][] values = new int[][]{{1,2,1,4,6},
                                     {5,6,7,8,9},
                                     {9,10,11,12,6},
                                     {13,14,15,16,0},
                                    {6,9,69,420,6}};
        QuadNode node = constructQuadTree(values, 5, 5);
        QuadNode min = node.getRectMin(new BoundingBox(new Double[][]{{0.0,0.0},{4.0,4.0}}));
        System.out.println(min);
    }

    public static void printRectData(Object[] s){
        System.out.print(String.format("Min is: %d, ", s[0]));
        double[] s1 = (double[]) s[1];
        System.out.println(Arrays.toString(s1));
        System.out.println();
    }

    public static QuadNode constructQuadTree(int[][] values, double fuck, double fucky){
        QuadNode node = new QuadNode(new Double[][]{{0.0, 0.0}, {fuck, fucky}});

        node.genChildren();

        for (int i = 0; i < 4; ++i) {
            QuadNode[] children = node.getChildren();
            children[i].genChildren();
        }

        for (double i = 0; i < fuck; ++i){
            for (double j = 0; j < fucky; ++j){
                node.updateQuadNode(j + 0.5, i + 0.5, values[(int) i][(int) j], false);
            }
        }

        return node;
    }
}
