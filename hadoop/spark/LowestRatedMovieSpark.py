from pyspark import SparkConf, SparkContext


# This function just creates a Python "dictionary" we can later
# use to convert movie ID's to movie names while printing out the final results.
def loadMovieNames():
    movieNames = {}

    with open("ml-100k/u.item") as f:
        for line in f:
            fields = line.split('|')
            movieNames[int(fields[0])] = fields[1]

    return movieNames


# Take each line of u.data and convert it to (movieID, (rating, 1.0))
# This way we can then add up all the ratings for each movie, and
# the total number of ratings for each movie (which lets us compute the average)
def parseInput(line):
    fields = line.split()
    return (int(fields[1]), (float(fields[2]), 1.0))


if __name__ == "__main__":
    # The main script - create our SparkContext
    conf = SparkConf().setAppName("WorstMovies")
    sc = SparkContext(conf=conf)

    # Load up the raw u.data file
    lines = sc.textFile("hdfs:///user/maria_dev/ml-100k/u.data")

    # Convert to (movieID, (rating, 1.0))
    movieRatings = lines.map(parseInput)

    # Reduce to (movieID, (sumOfRatings, totalRatings))
    ratingTotalsAndCount = movieRatings.reduceByKey(lambda m1, m2: (m1[0] + m2[0], m1[1] + m2[1]))

    # Filter out movies rated 10 or fewer times
    popularTotalsAndCount = ratingTotalsAndCount.filter(lambda x: x[1][1] > 10)

    # Map to (rating, averageRating)
    averageRatings = popularTotalsAndCount.mapValues(lambda totalAndCount: totalAndCount[0] / totalAndCount[1])

    # Sort by average rating
    sortedMovies = averageRatings.sortBy(lambda x: x[1])

    # Take the top 10 results
    results = sortedMovies.take(10)

    # Load up our movie ID -> movie name lookup table
    movieNames = loadMovieNames()

    # Print them out:
    for result in results:
        print(movieNames[result[0]], result[1])
