main-java = calculator/driver/ParserDriver.java
main-class = calculator.driver.ParserDriver
src = .
bin = bin/
test-java = calculator/parser/ParserTest.java
test-class = calculator.parser.ParserTest
junit = junit-4.12.jar:hamcrest-core-1.3.jar

.PHONY: all
all: $(main-class)

.PHONY: test
test: $(main-class)
	javac -cp $(src):$(junit) -d $(bin) $(test-java)
	java -cp $(bin):$(junit) org.junit.runner.JUnitCore $(test-class)

.PHONY: $(main-class)
$(main-class): $(bin)
	javac -Xlint -cp $(src) -d $(bin) $(main-java)

$(bin):
	mkdir $@

.PHONY: run
run: $(main-class)
	java -cp $(bin) $(main-class)

.PHONY: clean
clean:
	-rm -rf $(bin)
	-find . -name "*~" | xargs -n 1 -d '\n' rm -f

