public class Test {
    public static void main(String[] args) {
        int rows = 4; //number of rows
        int columns = 4; //number of columns

        //makes a 4x4 square out of *
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }
}