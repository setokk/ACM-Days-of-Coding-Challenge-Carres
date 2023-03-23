import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carres
{
    public static int digitCount = 0;
    public static final char EMPTY = '0';

    public static void main(String[] args)
    {

        // Get input into a 2D array
        var squares = getInput(args[0]);

        /* Each square <String> has a list of 4 coordinates <ArrayList<String>>
         ["x11, y11", "x12, y12", "x13, y13", "x14, y14"]
        - visitedSquaresCoords = the squares we have visited
        - squaresSizes = the corresponding sizes of those squares */
        var visitedSquaresCoords = new HashMap<String, ArrayList<String>>();
        var squaresSizes = new HashMap<String, Integer>();


        /* Loop though the squares and get the squares with their corresponding sizes */
        for (int i = 0; i < squares.size(); i++)
        {
            var row = squares.get(i);
            for (int j = 0; j < row.size(); j++)
            {
                String cell = row.get(j);
                getSquaresFromCell(cell, i, j, visitedSquaresCoords, squares, squaresSizes);
            }
        }

        // Sort based on square sizes (get the 3 highest)
        List<String> maxSquares = squaresSizes
                        .entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(3).map(Map.Entry::getKey)
                        .toList();

        // Get the three squares' coordinates and find the min
        int[] maxSquaresCoords = new int[24];
        int i = 0;
        for (String key : maxSquares)
        {
            var coordinates = visitedSquaresCoords.get(key);
            for (String coordinate : coordinates)
            {
                String[] splitted = coordinate.split(",");
                maxSquaresCoords[i] = Integer.parseInt(splitted[0]); // x coordinate (even number index)
                maxSquaresCoords[i+1] = Integer.parseInt(splitted[1]); // y coordinate (odd number index)
                i = i + 2;
            }
        }

        // Find min coordinates by calculating the distance between the three points
        var minCoordinates = new StringBuilder("None");
        double minDistance = 1000000;
        for (int j = 0; j < 8; j = j + 2) // x1, y1 index
        {
            for (int k = 8; k < 16; k = k + 2) // x2, y2 index
            {
                for (int l = 16; l < 24; l = l + 2) // x3, y3 index
                {
                    // Difference between coordinates of the first and second square
                    double x1_x2_diff = Math.pow((maxSquaresCoords[j] - maxSquaresCoords[k]), 2);
                    double y1_y2_diff = Math.pow((maxSquaresCoords[j+1] - maxSquaresCoords[k+1]), 2);
                    double D1 = Math.sqrt(x1_x2_diff + y1_y2_diff);

                    // Difference between coordinates of the first and third square
                    double x1_x3_diff = Math.pow((maxSquaresCoords[j] - maxSquaresCoords[l]), 2);
                    double y1_y3_diff = Math.pow((maxSquaresCoords[j+1] - maxSquaresCoords[l+1]), 2);
                    double D2 = Math.sqrt(x1_x3_diff + y1_y3_diff);

                    // Difference between coordinates of the second and third square
                    double x2_x3_diff = Math.pow((maxSquaresCoords[k] - maxSquaresCoords[l]), 2);
                    double y2_y3_diff = Math.pow((maxSquaresCoords[k+1] - maxSquaresCoords[l+1]), 2);
                    double D3 = Math.sqrt(x2_x3_diff + y2_y3_diff);

                    // Total distance
                    double D = (D1 + D2 + D3) / 3.0;
                    if (D <= minDistance)
                    {
                        minDistance = D;

                        // Delete previous min
                        minCoordinates.delete(0, minCoordinates.length());
                        minCoordinates.append("[(" + maxSquaresCoords[j] + "," + maxSquaresCoords[j+1] + "), " +
                                              "(" + maxSquaresCoords[k] + "," + maxSquaresCoords[k+1] + "), " +
                                              "(" + maxSquaresCoords[l] + "," + maxSquaresCoords[l+1] + ")]");
                    }
                }
            }
        }

        System.out.println("Closest Coordinates of Max Squares: " + minCoordinates);
        System.out.println("Distance: " + minDistance);
    }

    public static ArrayList<String> deepClone(ArrayList<String> list)
    {
        return new ArrayList<>(list);
    }

    public static void addCellTo(ArrayList<String> row, StringBuilder cell)
    {
        row.add(cell.toString());
        cell.delete(0, cell.length());
    }

    public static ArrayList<ArrayList<String>> getInput(String input)
    {
        ArrayList<ArrayList<String>> squares = new ArrayList<>();
        ArrayList<String> row = new ArrayList<>();
        StringBuilder cell = new StringBuilder();

        // Get each character one by one
        for (int i = 1; i < input.length(); i++)
        {
            if (input.charAt(i) == ',')  // Add cell
            {
                addCellTo(row, cell);
            }
            else if (input.charAt(i) == ' ' ||
                     input.charAt(i) == ']') // Next row or final row
            {
                addCellTo(row, cell);

                squares.add(deepClone(row));
                row.clear();
            }
            else // We have a digit
            {
                cell.append(input.charAt(i));
            }
        }

        return squares;
    }

    public static char getNextDigit(String cell)
    {
        char digit = cell.charAt(digitCount);
        digitCount++;

        return digit;
    }

    public static boolean hasMoreDigits(String cell)
    {
        if (digitCount < cell.length())
            return true;
        else
        {
            // We've gone through all the digits
            digitCount = 0;
            return false;
        }
    }

    public static boolean isNotVisited(char digit,
                                        HashMap<String, ArrayList<String>>
                                                visitedSquaresCoords)
    {
        return (!visitedSquaresCoords.containsKey(String.valueOf(digit)));
    }


    public static int getSquareSize(char digit,
                                  int i,
                                  int j,
                                  ArrayList<ArrayList<String>> squares)
    {
        int initial_index = j;
        while (j + 1 < squares.get(i).size())
        {
            j++;

            if (squares.get(i).get(j).contains(String.valueOf(digit)))
            {
                return Math.abs(initial_index - j) + 1; // Size
            }
        }

        return 0;
    }

    public static void getSquaresFromCell(String cell,
                                          int i,
                                          int j,
                                          HashMap<String, ArrayList<String>> visitedSquaresCoords,
                                          ArrayList<ArrayList<String>> squares,
                                          HashMap<String, Integer> squaresSizes)
    {
            char digit;
            while (hasMoreDigits(cell))
            {
                digit = getNextDigit(cell);
                if (digit != EMPTY &&
                        isNotVisited(digit, visitedSquaresCoords))
                {
                    String upLeftCoord = "";
                    String downLeftCoord = "";
                    String upRightCoord = "";
                    String downRightCoord = "";

                    // Find square size by moving right from where we found the digit
                    int squareSize = getSquareSize(digit, i, j, squares);

                    // Calculate coords for all 4 directions
                    // Left coords
                    upLeftCoord = i + "," + j;
                    downLeftCoord = (i + squareSize - 1) + "," + j;

                    // Right coords
                    upRightCoord = i + "," + (j + squareSize - 1);
                    downRightCoord = (i + squareSize - 1) + "," + (j + squareSize - 1);


                    // Create coordinates list and add them to the hashmap with the square id digit
                    ArrayList<String> coords = new ArrayList<>(4);
                    coords.add(upLeftCoord);
                    coords.add(downLeftCoord);
                    coords.add(upRightCoord);
                    coords.add(downRightCoord);

                    visitedSquaresCoords.put(String.valueOf(digit), coords);
                    squaresSizes.put(String.valueOf(digit), squareSize);
                }
            }
    }
}
