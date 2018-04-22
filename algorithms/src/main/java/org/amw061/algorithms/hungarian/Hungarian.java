package org.amw061.algorithms.hungarian;

import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

@SuppressWarnings("Duplicates")
@Slf4j
public class Hungarian {

    public final static int CO = Integer.MIN_VALUE;
    public final static int AS = Integer.MAX_VALUE;

    public HashMap<Integer, Integer> run(int[][] matrix) throws IOException {
        log.trace("Find the minimum value of each row. Subtract from respective rows.");
        matrix = subtractMinInRows(matrix);

        log.trace("Find the minimum value of each column. Subtract from respective columns.");
        matrix = subtractMinInColumns(matrix);

        log.trace("Cover all zeros with a minimum number of lines.");
        int iteration = 0;
        boolean solutionFound = false;

//        while (!solutionFound) {
//            log.trace("Looking for the solution - iteration #{}", iteration++);
//            IntermediateSolution solution = zeroAssignment(matrix);
//            solutionFound = solution.solutionFound;
//            matrix = solution.matrix;
//
//            if (iteration == 100) {
//                try (BufferedWriter writer = newBufferedWriter(get("invalid.txt"))) {
//                    writer.write(Arrays.deepToString(matrix));
//                    throw new RuntimeException("No solution found after " + iteration + " iterations");
//                }
//            }
//        }

        log.trace("Solution found!");
        return findFinalSolution(matrix);
    }

    int[][] subtractMinInRows(int[][] matrix) {
        for (int[] row : matrix) {
            int min = Ints.min(row);

            for (int i = 0; i < row.length; i++) {
                row[i] -= min;
            }
        }

        return matrix;
    }

    int[][] subtractMinInColumns(int[][] matrix) {
        int dim = matrix.length;

        for (int colIndex = 0; colIndex < dim; colIndex++) {
            int min = Integer.MAX_VALUE;

            for (int[] row : matrix) {
                int val = row[colIndex];
                min = min > val ? val : min;
            }

            for (int i = 0; i < dim; i++) {
                matrix[i][colIndex] -= min;
            }
        }

        return matrix;
    }

    IntermediateSolution zeroAssignment(int[][] matrix) {
        int dim = matrix.length;
        Map<Integer, Integer> assignments = newHashMap();

        log.trace("Go through each row, select a 0 if it is the only 0 in row, strike out if there are other 0s in same column.");
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            int numberOfZerosInRow = 0;
            int foundIndex = -1;

            for (int colIndex = 0; colIndex < dim; colIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    numberOfZerosInRow++;
                    foundIndex = colIndex;
                }
            }

            if (numberOfZerosInRow == 1) {
                assignments.put(rowIndex, foundIndex);
                matrix[rowIndex][foundIndex] = AS;

                for (int i = 0; i < dim; i++) {
                    if (matrix[i][foundIndex] == 0) {
                        matrix[i][foundIndex] = CO;
                    }
                }
            }
        }

        log.trace("Go through each column, select a 0 if it is the only 0 in column, strike out if there are other 0s in same row.");
        for (int colIndex = 0; colIndex < dim; colIndex++) {
            int numberOfZerosInColumn = 0;
            int foundIndex = -1;

            for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    numberOfZerosInColumn++;
                    foundIndex = rowIndex;
                }
            }

            if (numberOfZerosInColumn == 1) {
                assignments.put(foundIndex, colIndex);
                matrix[foundIndex][colIndex] = AS;

                for (int i = 0; i < dim; i++) {
                    if (matrix[foundIndex][i] == 0) {
                        matrix[foundIndex][i] = CO;
                    }
                }
            }
        }

        log.trace("Mark the rest with AS.");
        for (int colIndex = 0; colIndex < dim; colIndex++) {
            for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    assignments.put(rowIndex, colIndex);
                    matrix[rowIndex][colIndex] = AS;
                }
            }
        }

        log.trace("At this point no 0 in the matrix, only AS or CO");

        boolean solutionFound = assignments.size() == dim;
        return new IntermediateSolution(solutionFound, matrix, assignments);
    }

    IntermediateSolution drawLines(int[][] matrix) {
        int dim = matrix.length;

        Set<Integer> markedRows = newHashSet();
        Set<Integer> markedColumns = newHashSet();

        Set<Integer> newlyMarkedRows = newHashSet();
        Set<Integer> newlyMarkedColumns = newHashSet();

        // Mark all rows having no assignments.
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            boolean noAssignment = true;

            for (int colIndex = 0; colIndex < dim; colIndex++) {
                if (matrix[rowIndex][colIndex] == AS) {
                    noAssignment = false;
                }
            }

            if (noAssignment) {
                markedRows.add(rowIndex);
                newlyMarkedRows.add(rowIndex);
            }
        }

        // Mark all (unmarked) columns having zeros in newly marked row(s).
        for (int colIndex = 0; colIndex < dim; colIndex++) {
            for (int rowIndex : newlyMarkedRows) {
                if (matrix[rowIndex][colIndex] == CO) {
                    markedColumns.add(colIndex);
                    newlyMarkedColumns.add(colIndex);
                }
            }
        }

        // Mark all rows having assignments in newly marked columns.
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex : newlyMarkedColumns) {
                if (matrix[rowIndex][colIndex] == AS) {
                    markedRows.add(rowIndex);
                }
            }
        }

        newlyMarkedRows.clear();
        newlyMarkedColumns.clear();

        // ---

        Set<Integer> columnLines = markedColumns;
        Set<Integer> rowLines = newHashSet();

        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            if (!markedRows.contains(rowIndex)) {
                rowLines.add(rowIndex);
            }
        }

        // ---

        // result found
        if (rowLines.size() + columnLines.size() == dim) {
            return new IntermediateSolution(true, matrix, null);
        }

        // find the smallest uncovered number
        int smallestUncoveredNumber = Integer.MAX_VALUE;
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            if (!rowLines.contains(rowIndex)) {
                for (int colIndex = 0; colIndex < dim; colIndex++) {
                    if (!columnLines.contains(colIndex)) {
                        if (matrix[rowIndex][colIndex] < smallestUncoveredNumber) {
                            smallestUncoveredNumber = matrix[rowIndex][colIndex];
                        }
                    }
                }
            }
        }

        // remove AS and CO marks
        for (int colIndex = 0; colIndex < dim; colIndex++) {
            for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
                if (matrix[rowIndex][colIndex] == AS || matrix[rowIndex][colIndex] == CO) {
                    matrix[rowIndex][colIndex] = 0;
                }
            }
        }

        // subtract this number from all uncovered elements and
        // add it to all elements that are covered twice
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex = 0; colIndex < dim; colIndex++) {
                if (!rowLines.contains(rowIndex) && !columnLines.contains(colIndex)) {
                    matrix[rowIndex][colIndex] -= smallestUncoveredNumber;
                }

                if (rowLines.contains(rowIndex) && columnLines.contains(colIndex)) {
                    matrix[rowIndex][colIndex] += smallestUncoveredNumber;
                }
            }
        }

        return new IntermediateSolution(false, matrix, null);
    }

    IntermediateSolution findIntermediateSolutionGreedy(int[][] matrix) {
        int dim = matrix.length;

        int[] zerosInRows = new int[dim];
        int[] zerosInColumns = new int[dim];

        Arrays.fill(zerosInRows, 0);
        Arrays.fill(zerosInColumns, 0);

        // find count of zeros in all rows and all columns
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex = 0; colIndex < dim; colIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    zerosInRows[rowIndex]++;
                    zerosInColumns[colIndex]++;
                }
            }
        }

        Set<Integer> selectedRows = newHashSet();
        Set<Integer> selectedColumns = newHashSet();

        while (!(allZeros(zerosInRows) && allZeros(zerosInColumns))) {
            // find max of rows and columns
            int indexWithMaxZerosInColumns = getIndexWithMaxValue(zerosInColumns);
            int indexWithMaxZerosInRows = getIndexWithMaxValue(zerosInRows);

            if (zerosInColumns[indexWithMaxZerosInColumns] > zerosInRows[indexWithMaxZerosInRows]) {
                // column has a maximum value - subtract zeros from corresponding rows
                selectedColumns.add(indexWithMaxZerosInColumns);
                zerosInColumns[indexWithMaxZerosInColumns] = 0;

                for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
                    if (matrix[rowIndex][indexWithMaxZerosInColumns] == 0 && !selectedRows.contains(rowIndex)) {
                        zerosInRows[rowIndex]--;
                    }
                }
            } else {
                // row has a maximum value - subtract zeros from corresponding columns
                zerosInRows[indexWithMaxZerosInRows] = 0;
                selectedRows.add(indexWithMaxZerosInRows);

                for (int colIndex = 0; colIndex < dim; colIndex++) {
                    if (matrix[indexWithMaxZerosInRows][colIndex] == 0 && !selectedColumns.contains(colIndex)) {
                        zerosInColumns[colIndex]--;
                    }
                }
            }
        }

        // result found
        if (selectedRows.size() + selectedColumns.size() == dim) {
            return new IntermediateSolution(true, matrix, null);
        }

        // find the smallest uncovered number
        int smallestUncoveredNumber = Integer.MAX_VALUE;
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            if (!selectedRows.contains(rowIndex)) {
                for (int colIndex = 0; colIndex < dim; colIndex++) {
                    if (!selectedColumns.contains(colIndex)) {
                        if (matrix[rowIndex][colIndex] < smallestUncoveredNumber) {
                            smallestUncoveredNumber = matrix[rowIndex][colIndex];
                        }
                    }
                }
            }
        }

        // subtract this number from all uncovered elements and add it to all elements that are covered twice
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex = 0; colIndex < dim; colIndex++) {
                if (!selectedRows.contains(rowIndex) && !selectedColumns.contains(colIndex)) {
                    matrix[rowIndex][colIndex] -= smallestUncoveredNumber;
                }

                if (selectedRows.contains(rowIndex) && selectedColumns.contains(colIndex)) {
                    matrix[rowIndex][colIndex] += smallestUncoveredNumber;
                }
            }
        }

        return new IntermediateSolution(false, matrix, null);
    }

    HashMap<Integer, Integer> findFinalSolution(int[][] matrix) {
        int dim = matrix.length;
        HashMap<Integer, Integer> result = newHashMap();

        // go through each row, select a 0 if it is the only 0 in row, strike out if there are other 0s in same column
        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            int numberOfZerosInRow = 0;
            int index = -1;

            for (int colIndex = 0; colIndex < dim; colIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    numberOfZerosInRow++;
                    index = colIndex;
                }
            }

            if (numberOfZerosInRow == 1) {
                result.put(rowIndex, index);
                matrix[rowIndex][index] = Integer.MAX_VALUE;

                for (int i = 0; i < dim; i++) {
                    if (matrix[i][index] == 0) {
                        matrix[i][index] = Integer.MIN_VALUE;
                    }
                }
            }
        }

        // go through each column, select a 0 if it is the only 0 in column, strike out if there are other 0s in same row
        for (int colIndex = 0; colIndex < dim; colIndex++) {
            int numberOfZerosInColumn = 0;
            int index = -1;

            for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    numberOfZerosInColumn++;
                    index = rowIndex;
                }
            }

            if (numberOfZerosInColumn == 1) {
                result.put(index, colIndex);
                matrix[index][colIndex] = Integer.MAX_VALUE;

                for (int i = 0; i < dim; i++) {
                    if (matrix[index][i] == 0) {
                        matrix[index][i] = Integer.MIN_VALUE;
                    }
                }
            }
        }

        for (int colIndex = 0; colIndex < dim; colIndex++) {
            for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
                if (matrix[rowIndex][colIndex] == 0) {
                    result.put(rowIndex, colIndex);
                    matrix[rowIndex][colIndex] = Integer.MAX_VALUE;
                }
            }
        }

        return result;
    }

    private int getIndexWithMaxValue(int[] zeros) {
        int index = 0;

        for (int i = 0; i < zeros.length; i++) {
            if (zeros[i] > zeros[index]) {
                index = i;
            }
        }

        return index;
    }

    private boolean allZeros(int[] zeros) {
        for (int value : zeros) {
            if (value != 0) {
                return false;
            }
        }

        return true;
    }

    @Data
    @AllArgsConstructor
    private class IntermediateSolution {
        private boolean solutionFound;
        private int[][] matrix;
        private Map<Integer, Integer> assignments;
    }
}
