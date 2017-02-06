(1) The top of the class here is "Interpreter.java."

(2)   For the Sort Merge Join, whenever there is a set of tuples available
    for join, I call the anchor() method to take down the position of the
    file channel and buffer page. When there are no tuples that could 
    satisfy the join conditions in the inner tuple, call the moveback()
    method to set the position of the file channel and buffer page back
    to those two parameters documented by anchor() method.
      For handling the DISTINCT. I use the operator that is either 
    SortOperator or EXSortOperator and assign it to the field in 
    DuplicateEliminationOperator. Also I create a field of Tuple to take
    down the temporary tuple. In the getNextTuple(), I call the nextTuple()
    from the two sort operators and check whether it equals with the 
    temporary tuple. If not, set the temporary tuple to the new tuple and 
    return the new tuple. When callint reset(), set the temporary tuple to
    null and reset the sort operators.
      In this way, I can keep the DISTINCT in unbound state.

(3) I test my program on windows. The file seperaters appears in:
    a. the main() method in "Interpreter.java."
    b. the String variables in "Catalog.java."
    c. the "Parser.java."
    d. the tempdir variable in "Interpreter.java."