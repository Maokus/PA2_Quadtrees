package sample;

import java.util.Arrays;

public class Tester {
    public static void main(String[] args) {
        /*int[][] values = new int[][]{{6,4,7,4},
                                     {5,6,3,8},
                                     {9,4,2,4},
                                     {9,1,3,4}};*/
        int[][] values = new int[][]{{10,2,10,4},
                                     {5,6,7,8},
                                     {9,1,11,12},
                                     {13,14,15,16},};
        int[][] gaming = new int[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                gaming[i][j] = 8*i + j;
            }
        }
        int[][] twobtwo = new int[][]{{1,2},{3,4}};
        QuadNode node = QuadNode.constructQuadNode(values);
        QuadNode min = node.getRectMin(new BoundingBox(new Double[][]{{1.0,1.0},{4.0,4.0}}));
//        QuadNode min = node.getRectMin(node.getBoundingBox());
//        System.out.println(node);
//        System.out.println(round(10));
        System.out.println(min);
    }

    public static int round(int n){
        int m = 0x8000;
        n = n - 1;
        while((m & n) == 0){
            m >>= 1;
        }
        return m << 1;
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

        //Generate values
        for (int i = 0; i < 4; ++i) {
            QuadNode[] children = node.getChildren();
            children[i].genChildren();
        }

        //Populate the children
        for (double i = 0; i < 4; ++i){
            for (double j = 0; j < 4; ++j){
                node.updateQuadNode(j + 0.5, i + 0.5, values[(int) i][(int) j], false);
            }
        }

        return node;
    }
}
