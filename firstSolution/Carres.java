import java.util.ArrayList;
import java.util.HashMap;

public class Carres
{
    public static int digitCount = 0;
    public static final char EMPTY = '0';

    public static void main(String[] args)
    {
        ArrayList<ArrayList<String>> squares = getInput(args[0]);


                                // Algorithm start //
        // Each square <String> has a list of 4 coordinates <ArrayList<String>>
        // ["x11, y11", "x12, y12", "x13, y13", "x14, y14"]
        HashMap<String, ArrayList<String>> visitedSquaresCoords = new HashMap<>();
        HashMap<String, Integer> squaresSizes = new HashMap<>();

        for (int i = 0; i < squares.size(); i++)
        {
            ArrayList<String> row = squares.get(i);

            for (int j = 0; j < row.size(); j++)
            {
                String cell = row.get(j);
                addAllSquaresCoords(cell, i, j, visitedSquaresCoords, squares, squaresSizes);
            }
        }

        System.out.println(visitedSquaresCoords);
        System.out.println(squaresSizes);
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

    public static void addAllSquaresCoords(String cell,
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