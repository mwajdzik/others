class HelloWorld {

    static void main(String[] args) {
        def msg = "Hello world!"

        println msg.toUpperCase()
        println msg.getClass()

        def johnDoe = new Person()
        johnDoe.setFirstName("John")
        johnDoe.setLastName("Doe")
        johnDoe.setAge(23)

        def persons = [
                johnDoe,
                new Person(firstName: "Mary", lastName: "Hill", age: 30)
        ]

        for (person in persons) {
            println person.getFullName()
            println person.toString()
        }

        persons.each {
            println it.getFullName()
        }

        persons.eachWithIndex { Person p, int i ->
            println i + ": " + p.getFullName()
        }

        println persons.find { it.lastName == 'Hill' }
        println persons.collect { it.age > 18 }
        println persons.sort { it.age }

        // https://groovy-lang.org/gdk.html

        println johnDoe.getFullName().dropRight(2)

        Closure johnDoeToString = { println it.toString() }
        johnDoeToString(johnDoe)
        johnDoeToString.call(johnDoe)

        // file
        def file = new File("resources/application.properties")
        println file.getText("UTF-8")

        file.eachLine { line, no ->
            println no + ": " + line
        }
    }
}
