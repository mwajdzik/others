package org.amw061.algorithms.hungarian

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

// https://en.wikipedia.org/wiki/Hungarian_algorithm#Matrix_interpretation
class HungarianAlgorithmTest extends Specification {

    @Shared
    def r = new Random(3)

    @Unroll
    def "run the algorithm, case: #caseNum"() {
        given:
        def algorithm = new HungarianAlgorithm(matrix as double[][])

        when:
        def result = algorithm.execute()

        then:
        result == expected

        where:
        caseNum | matrix                 || expected
        1       | [[1, 2], [2, 1]]       || [0: 0, 1: 1]
        2       | [[1, 1], [2, 1]]       || [0: 0, 1: 1]
        3       | [[1, 1], [1, 2]]       || [0: 1, 1: 0]
        4       | [[1, 1], [1, 1]]       || [0: 0, 1: 1]

        // http://www.hungarianalgorithm.com/solve.php?c=16-17-51-36-70--79-29-22-57-66--10-36-20-63-9--42-1-34-32-69--93-79-75-86-47&random=1
        5       | [[16, 17, 51, 36, 70],
                   [79, 29, 22, 57, 66],
                   [10, 36, 20, 63, 9],
                   [42, 1, 34, 32, 69],
                   [93, 79, 75, 86, 47]] || [0: 3, 1: 2, 2: 0, 3: 1, 4: 4]

        // http://www.hungarianalgorithm.com/solve.php?c=23-77-65-0-38--61-20-34-0-44--0-0-0-40-33--51-28-84-1-0--73-71-82-0-5&random=1
        6       | [[23, 77, 65, 0, 38],
                   [61, 20, 34, 0, 44],
                   [0, 0, 0, 40, 33],
                   [51, 28, 84, 1, 0],
                   [73, 71, 82, 0, 5]]   || [0: 0, 1: 1, 2: 2, 3: 4, 4: 3]

        // https://www.researchgate.net/figure/Hungarian-method-step-by-step-procedure-including-the-alternating-priority-strategy_fig1_257877477
        7       | [[10, 12, 20, 21],
                   [10, 12, 21, 24],
                   [14, 17, 28, 30],
                   [16, 20, 30, 35]]     || [0: 3, 1: 2, 2: 1, 3: 0]

        // https://www.slideshare.net/JosephKonnully/assgnmenthungarian-method
        8       | [[8, 10, 17, 9],
                   [3, 8, 5, 6],
                   [10, 12, 11, 9],
                   [6, 13, 9, 7]]        || [0: 1, 1: 2, 2: 3, 3: 0]

        9       | [[6, 12, 3, 11, 15],
                   [4, 2, 7, 1, 10],
                   [8, 11, 10, 7, 11],
                   [16, 19, 12, 23, 21],
                   [9, 5, 7, 6, 10]]     || [0: 0, 1: 3, 2: 4, 3: 2, 4: 1]

        10      | [[2, 3, 4, 5],
                   [4, 5, 6, 7],
                   [7, 8, 9, 8],
                   [3, 5, 8, 4]]         || [0: 0, 1: 1, 2: 2, 3: 3]
    }

    @Unroll
    def "run the algorithm, huge dataset of #dim x #dim, random values"() {
        given:
        def matrix = generateRandomMatrix(dim)

        when:

        def algorithm = new HungarianAlgorithm(matrix)
        def result = algorithm.execute()

        then:
        result.size() == dim

        where:
        dim << [1, 5, 10, 100, 1000, 2500]
    }


    @Unroll
    def "run the algorithm, huge dataset of #dim x #dim, all zeros"() {
        given:
        def matrix = generateMatrixWithZeros(dim)

        when:

        def algorithm = new HungarianAlgorithm(matrix)
        def result = algorithm.execute()

        then:
        result.size() == dim

        where:
        dim << [1, 5, 10, 100, 1000, 2500]
    }

    def generateRandomMatrix(int dim) {
        def matrix = new double[dim][dim]

        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex = 0; colIndex < dim; colIndex++) {
                matrix[rowIndex][colIndex] = Math.abs(r.nextInt()) % 100
            }
        }

        return matrix
    }

    def generateMatrixWithZeros(int dim) {
        def matrix = new double[dim][dim]

        for (int rowIndex = 0; rowIndex < dim; rowIndex++) {
            for (int colIndex = 0; colIndex < dim; colIndex++) {
                matrix[rowIndex][colIndex] = 0
            }
        }

        return matrix
    }
}
