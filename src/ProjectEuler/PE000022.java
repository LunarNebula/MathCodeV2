package ProjectEuler;

import DataSet.BST;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class PE000022 {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("" + // "C:\\Users\\bdane\\IdeaProjects\\MathCodeV2\\" +
                "src\\ProjectEuler\\PE000022_names.txt");
        Scanner useless = new Scanner(file);
        String[] line = useless.nextLine().split(",");
        for(int i = 0; i < line.length; i++) {
            line[i] = line[i].substring(1, line[i].length() - 1);
        }
        List<String> list = new BST<>(line).getOrderedList();
        int score = 0, index = 0;
        for(String name : list) {
            int subScore = 0;
            for(char c : name.toCharArray()) {
                subScore += c - 'A' + 1;
            }
            index++;
            score += subScore * index;
        }
        System.out.println("Score: " + score);
    }
}
