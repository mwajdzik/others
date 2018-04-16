package org.amw061.algorithms.hungarian;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Set;

public class Hungarian {

    public int[][] run(int[][] matrix) {
        // Find the minimum value of each row.
        // Subtract from respective rows.
        matrix = subtractMinInRows(matrix);

        // Find the minimum value of each column.
        // Subtract from respective columns.
        matrix = subtractMinInColumns(matrix);

        // Cover all zeros with a minimum number of lines
        boolean solutionFound = false;
        while (!solutionFound) {
            IntermediateSolution solution = findIntermediateSolution(matrix);
            solutionFound = solution.solutionFound;
            matrix = solution.matrix;
        }

        return matrix;
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

    IntermediateSolution findIntermediateSolution(int[][] matrix) {
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

        Set<Integer> selectedRows = Sets.newHashSet();
        Set<Integer> selectedColumns = Sets.newHashSet();

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
            return new IntermediateSolution(true, matrix);
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

        return new IntermediateSolution(false, matrix);
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
    }
}
