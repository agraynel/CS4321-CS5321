(1) The top of the class here is "Interpreter.java."

(2) The selection pushing: I implement this part in "UnionFind.java" and "Mule.java".
    I use the union find algorithm stated in the instruction.
    I store the union find result in a Map<String, String> and Map<String, Mule>.
    The Mule is a block includes all the information required in the instruction.

    The choice of implementation for each selection operator: At first I build all
    B+ Tree based on the index_info.txt in advance. And then for each attribute in
    the selection operator, check whether this attribute appeared in the B+ Tree.
    If yes, check whether it could achieve minimum I/O. Finally, if we get an 
    attribute, use IndexScanOperator based on that attribute. If not, simply use
    the ordinary ScanOperator as the child of the Selection Operator.

    The choice of join order: I perform this part in "DP.java". When the number of 
    tables is larger than 2, this class will be used for fetching out the best join
    order. I use the buttom-up method for the dynamic programming. Initializing the
    data structures by putting single relation table in the queue. And then 
    iteratively move up for larger number of tables, calculate the intermediate I/O
    cost based on the result of the previous table sets. When the length of the table
    set is the number of total tables minus 1, compare the I/O cost and pick out the
    order that has the lowest cost and store it in the local variable.

    The choice of implementation for each join operator: During the benchmark in 
    project 3, I noticed SMJ runs much faster than BNLJ. However, SMJ could only be
    applied to all-equal join conditions. So my choice is: use SMJ when all the join
    conditions are equal, use BNLJ when SMJ cannot be used on the set of join conditions.
    For sorting, I use ExternalSort and the number of buffer pages is 100.
    For BNLJ, the number of buffer pages is 100.

(3) I test my program on windows. The file seperaters appears in:
    a. the main() method in "Interpreter.java."
    b. the String variables in "Catalog.java."
    c. the tempdir variable in "Interpreter.java."
    d. the method dump() in "BTreeBuilder.java."
    e. the constructor in "IndexScanOperator.java."
    f. the build() method in "Catalog.java."