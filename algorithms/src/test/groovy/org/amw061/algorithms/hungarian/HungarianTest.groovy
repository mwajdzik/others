package org.amw061.algorithms.hungarian

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static org.amw061.algorithms.hungarian.Hungarian.CO
import static org.amw061.algorithms.hungarian.Hungarian.AS

// https://en.wikipedia.org/wiki/Hungarian_algorithm#Matrix_interpretation
// https://www.topcoder.com/community/data-science/data-science-tutorials/assignment-problem-and-hungarian-algorithm/

class HungarianTest extends Specification {

    @Subject
    def algorithm = new Hungarian()

    @Shared
    def r = new Random(3)

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
        def solution1 = algorithm.zeroAssignment(matrix)

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
        def solution2 = algorithm.zeroAssignment(solution1.matrix)

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
        solution == [0: 3, 1: 2, 2: 0, 3: 1, 4: 4]
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
        def solution1 = algorithm.zeroAssignment(matrix)

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
        def solution2 = algorithm.zeroAssignment(solution1.matrix)

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
        def solution3 = algorithm.zeroAssignment(solution2.matrix)

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
        def solution4 = algorithm.zeroAssignment(solution3.matrix)

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
        solution == [0: 0, 1: 1, 2: 2, 3: 4, 4: 3]
    }

    // https://www.researchgate.net/figure/Hungarian-method-step-by-step-procedure-including-the-alternating-priority-strategy_fig1_257877477
    def "run the algorithm - dataset #3"() {
        given:
        def matrix = [
                [10, 12, 20, 21],
                [10, 12, 21, 24],
                [14, 17, 28, 30],
                [16, 20, 30, 35]
        ] as int[][]
//
//        when:
//        matrix = algorithm.subtractMinInRows(matrix)
//
//        then:
//        matrix == [
//                [0, 2, 10, 11],
//                [0, 2, 11, 14],
//                [0, 3, 14, 16],
//                [0, 4, 14, 19]
//        ] as int[][]
//
//        when:
//        matrix = algorithm.subtractMinInColumns(matrix)
//
//        then:
//        matrix == [
//                [0, 0, 0, 0],
//                [0, 0, 1, 3],
//                [0, 1, 4, 5],
//                [0, 2, 4, 8]
//        ] as int[][]

//        when:
//        def solution1 = algorithm.findIntermediateSolution(matrix)
//
//        then:
//        !solution1.solutionFound
//        solution1.matrix == [
//                [1, 0, 0, 0],
//                [1, 0, 1, 3],
//                [0, 0, 3, 4],
//                [0, 1, 3, 7]
//        ] as int[][]
//
//        when:
//        def solution2 = algorithm.findIntermediateSolution(solution1.matrix)
//
//        then:
//        solution2.solutionFound
//        solution2.matrix == [
//                [1, 0, 0, 0], [1, 0, 1, 3], [0, 0, 3, 4], [0, 1, 3, 7]
//        ] as int[][]

        when:
        def solution = algorithm.run(matrix)

        then:
        solution == []
    }

    // https://www.slideshare.net/JosephKonnully/assgnmenthungarian-method
    def "run the algorithm - dataset #4"() {
        given:
        def matrix = [
                [8, 10, 17, 9],
                [3, 8, 5, 6],
                [10, 12, 11, 9],
                [6, 13, 9, 7]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInRows(matrix)

        then:
        matrix == [
                [0, 2, 9, 1],
                [0, 5, 2, 3],
                [1, 3, 2, 0],
                [0, 7, 3, 1]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInColumns(matrix)

        then:
        matrix == [
                [0, 0, 7, 1],
                [0, 3, 0, 3],
                [1, 1, 0, 0],
                [0, 5, 1, 1]
        ] as int[][]

        when:
        def solution1 = algorithm.zeroAssignment(matrix)

        then:
        solution1.solutionFound
        solution1.assignments == [0: 1, 1: 2, 2: 3, 3: 0]
        solution1.matrix == [
                [CO, AS, 7, 1],
                [CO, 3, AS, 3],
                [1, 1, CO, AS],
                [AS, 5, 1, 1]
        ] as int[][]
    }

    // https://www.slideshare.net/JosephKonnully/assgnmenthungarian-method
    def "run the algorithm - dataset #5"() {
        given:
        def matrix = [
                [6, 12, 3, 11, 15],
                [4, 2, 7, 1, 10],
                [8, 11, 10, 7, 11],
                [16, 19, 12, 23, 21],
                [9, 5, 7, 6, 10],
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInRows(matrix)

        then:
        matrix == [
                [3, 9, 0, 8, 12],
                [3, 1, 6, 0, 9],
                [1, 4, 3, 0, 4],
                [4, 7, 0, 11, 9],
                [4, 0, 2, 1, 5]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInColumns(matrix)

        then:
        matrix == [
                [2, 9, 0, 8, 8],
                [2, 1, 6, 0, 5],
                [0, 4, 3, 0, 0],
                [3, 7, 0, 11, 5],
                [3, 0, 2, 1, 1]
        ] as int[][]

        when:
        def solution1 = algorithm.zeroAssignment(matrix)

        then:
        !solution1.solutionFound
        solution1.matrix == [
                [2, 9, AS, 8, 8],
                [2, 1, 6, AS, 5],
                [AS, 4, 3, CO, CO],
                [3, 7, CO, 11, 5],
                [3, AS, 2, 1, 1]
        ] as int[][]

        when:
        def solution2 = algorithm.drawLines(solution1.matrix)

        then:
        !solution2.solutionFound
        solution2.matrix == [
                [0, 7, 0, 6, 6],
                [2, 1, 8, 0, 5],
                [0, 4, 5, 0, 0],
                [1, 5, 0, 9, 3],
                [3, 0, 4, 1, 1]
        ] as int[][]

        when:
        def solution3 = algorithm.zeroAssignment(solution2.matrix)

        then:
        solution3.solutionFound
        solution3.assignments == [0:0, 1:3, 2:4, 3:2, 4:1]
        solution3.matrix == [
                [AS, 7, CO, 6, 6],
                [2, 1, 8, AS, 5],
                [CO, 4, 5, CO, AS],
                [1, 5, AS, 9, 3],
                [3, AS, 4, 1, 1]
        ] as int[][]
    }

    // https://www.slideshare.net/JosephKonnully/assgnmenthungarian-method
    def "run the algorithm - dataset #6"() {
        given:
        def matrix = [
                [2, 3, 4, 5],
                [4, 5, 6, 7],
                [7, 8, 9, 8],
                [3, 5, 8, 4]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInRows(matrix)

        then:
        matrix == [
                [0, 1, 2, 3],
                [0, 1, 2, 3],
                [0, 1, 2, 1],
                [0, 2, 5, 1]
        ] as int[][]

        when:
        matrix = algorithm.subtractMinInColumns(matrix)

        then:
        matrix == [
                [0, 0, 0, 2],
                [0, 0, 0, 2],
                [0, 0, 0, 0],
                [0, 1, 3, 0]
        ] as int[][]

        when:
        def solution1 = algorithm.zeroAssignment(matrix)

        then:
        !solution1.solutionFound
        solution1.matrix == [
                [2147483647, 2147483647, 2147483647, 2],
                [2147483647, 2147483647, 2147483647, 2],
                [2147483647, 2147483647, 2147483647, 2147483647],
                [2147483647, 1, 3, 2147483647]
        ] as int[][]

        when:
        def solution2 = algorithm.drawLines(solution1.matrix)

        then:
        !solution2.solutionFound
        solution2.matrix == [
                [0, 7, 0, 6, 6],
                [2, 1, 8, 0, 5],
                [0, 4, 5, 0, 0],
                [1, 5, 0, 9, 3],
                [3, 0, 4, 1, 1]
        ] as int[][]

        when:
        def solution3 = algorithm.zeroAssignment(solution2.matrix)

        then:
        solution3.solutionFound
        solution3.assignments == [0:0, 1:3, 2:4, 3:2, 4:1]
        solution3.matrix == [
                [AS, 7, CO, 6, 6],
                [2, 1, 8, AS, 5],
                [CO, 4, 5, CO, AS],
                [1, 5, AS, 9, 3],
                [3, AS, 4, 1, 1]
        ] as int[][]
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
