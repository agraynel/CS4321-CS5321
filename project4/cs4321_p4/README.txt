(1) The top of the class here is "Interpreter.java."

(2) Here is the logic for my index scan operator:
    a. I set my lowkey and highkey in the constructor.
    b. I handle the difference between clustered vs. unclustered indexes
       in the getNextTuple() method for the IndexScanOperator.
    c. I create a file channel to the B+ Tree file. use the first index
       of the tree to locate the root. Search the tree by using the 
       locations of address and keys in different pages. At last, when we
       find the starting point, pick the position out to set the starting 
       point of the file channel and the index out to set the starting 
       point of the page. In this process, no nodes of the B+ Tree are
       deserialized.

(3) In my Physical Plan Builder, I seperate the selection conditions by 
    using "AND". After that I call the setExpression method for each tuple.
    In that method, I check whether this method is a join condition or a 
    selection. If it is the latter, I will pick out the column to see 
    whether it shows up in the B+ Tree index and whether the relationship 
    is not "!=" and whether the other part is a number. If it satisfies all
    the needs, the number will be converted to lowkey and highkey. If not, add
    it back to the selection expression tree.

(4) I test my program on windows. The file seperaters appears in:
    a. the main() method in "Interpreter.java."
    b. the String variables in "Catalog.java."
    c. the "Parser.java."
    d. the tempdir variable in "Interpreter.java."