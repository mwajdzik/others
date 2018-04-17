package org.amw061.algorithms.hungarian

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class HungarianTest extends Specification {

    @Subject
    def algorithm = new Hungarian()

    @Shared
    def r = new Random()

    // http://www.hungarianalgorithm.com/solve.php?c=16-17-51-36-70--79-29-22-57-66--10-36-20-63-9--42-1-34-32-69--93-79-75-86-47&random=1
    def "run the algorithm - dataset #1"() {
        given:
        def matrix = [
                [16, 17, 51, 36, 70],
                [79, 29, 22, 57, 66],
                [10, 36, 20, 63, 9],
                [42, 1, 34, 32, 69],
                [93, 79, 75, 86, 47]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInRows(matrix)

        then:
        matrix == [
                [0, 1, 35, 20, 54],
                [57, 7, 0, 35, 44],
                [1, 27, 11, 54, 0],
                [41, 0, 33, 31, 68],
                [46, 32, 28, 39, 0]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInColumns(matrix)

        then:
        matrix == [
                [0, 1, 35, 0, 54],
                [57, 7, 0, 15, 44],
                [1, 27, 11, 34, 0],
                [41, 0, 33, 11, 68],
                [46, 32, 28, 19, 0]
        ] as int[][]

        when:
        def solution1 = algorithm.findIntermediateSolution(matrix)

        then:
        !solution1.solutionFound
        solution1.matrix == [
                [0, 1, 35, 0, 55],
                [57, 7, 0, 15, 45],
                [0, 26, 10, 33, 0],
                [41, 0, 33, 11, 69],
                [45, 31, 27, 18, 0]
        ] as int[][]

        when:
        def solution2 = algorithm.findIntermediateSolution(solution1.matrix)

        then:
        solution2.solutionFound
        solution2.matrix == [
                [0, 1, 35, 0, 55],
                [57, 7, 0, 15, 45],
                [0, 26, 10, 33, 0],
                [41, 0, 33, 11, 69],
                [45, 31, 27, 18, 0]
        ] as int[][]

        when:
        def solution = algorithm.findFinalSolution(solution2.matrix)

        then:
        solution == [0:3, 1:2, 2:0, 3:1, 4:4]
    }

    // http://www.hungarianalgorithm.com/solve.php?c=23-77-65-0-38--61-20-34-0-44--0-0-0-40-33--51-28-84-1-0--73-71-82-0-5&random=1
    def "run the algorithm - dataset #2"() {
        given:
        def matrix = [
                [23, 77, 65, 0, 38],
                [61, 20, 34, 0, 44],
                [0, 0, 0, 40, 33],
                [51, 28, 84, 1, 0],
                [73, 71, 82, 0, 5]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInRows(matrix)

        then:
        matrix == [
                [23, 77, 65, 0, 38],
                [61, 20, 34, 0, 44],
                [0, 0, 0, 40, 33],
                [51, 28, 84, 1, 0],
                [73, 71, 82, 0, 5]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInColumns(matrix)

        then:
        matrix == [
                [23, 77, 65, 0, 38],
                [61, 20, 34, 0, 44],
                [0, 0, 0, 40, 33],
                [51, 28, 84, 1, 0],
                [73, 71, 82, 0, 5]
        ] as int[][]

        when:
        def solution1 = algorithm.findIntermediateSolution(matrix)

        then:
        !solution1.solutionFound
        solution1.matrix == [
                [18, 72, 60, 0, 33],
                [56, 15, 29, 0, 39],
                [0, 0, 0, 45, 33],
                [51, 28, 84, 6, 0],
                [68, 66, 77, 0, 0]
        ] as int[][]

        when:
        def solution2 = algorithm.findIntermediateSolution(solution1.matrix)

        then:
        !solution2.solutionFound
        solution2.matrix == [
                [3, 57, 45, 0, 33],
                [41, 0, 14, 0, 39],
                [0, 0, 0, 60, 48],
                [36, 13, 69, 6, 0],
                [53, 51, 62, 0, 0]
        ] as int[][]

        when:
        def solution3 = algorithm.findIntermediateSolution(solution2.matrix)

        then:
        !solution3.solutionFound
        solution3.matrix == [
                [0, 54, 42, 0, 33],
                [41, 0, 14, 3, 42],
                [0, 0, 0, 63, 51],
                [33, 10, 66, 6, 0],
                [50, 48, 59, 0, 0]
        ] as int[][]

        when:
        def solution4 = algorithm.findIntermediateSolution(solution3.matrix)

        then:
        solution4.solutionFound
        solution4.matrix == [
                [0, 54, 42, 0, 33],
                [41, 0, 14, 3, 42],
                [0, 0, 0, 63, 51],
                [33, 10, 66, 6, 0],
                [50, 48, 59, 0, 0]
        ] as int[][]

        when:
        def solution = algorithm.findFinalSolution(solution4.matrix)

        then:
        solution == [0:0, 1:1, 2:2, 3:4, 4:3]
    }

    @Unroll
    def "run the algorithm with dataset of #dim x #dim"() {
        given:
        def matrix = generateRandomMatrix(dim)

        when:
        def result = algorithm.run(matrix)

        then:
        result

        where:
        dim << [1, 5, 10, 100, 1000, 2500]
    }

    def generateRandomMatrix(int dim) {
        def matrix = new int[dim][dim]

        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex = 0; colIndex < dim; colIndex++) {
                matrix[rowIndex][colIndex] = Math.abs(r.nextInt()) % 100
            }
        }

        return matrix
    }
}
