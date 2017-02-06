package cs4321.project1;

import cs4321.project1.tree.*;

/**
 * Class for a parser that can parse a string and produce an expression tree. To
 * keep the code simple, this does no input checking whatsoever so it only works
 * on correct input.
 * 
 * An expression is one or more terms separated by + or - signs. A term is one
 * or more factors separated by * or / signs. A factor is an expression in
 * parentheses (), a factor with a unary - before it, or a number.
 * 
 * @author Lucja Kot
 * @author jz699 JUNCHEN ZHAN yc2329 YI CHEN
 */
public class Parser {

	private String[] tokens;
	private int currentToken; // pointer to next input token to be processed

	/**
	 * @precondition input represents a valid expression with all tokens
	 *               separated by spaces, e.g. "3.0 - ( 1.0 + 2.0 ) / - 5.0. All
	 *               tokens must be either numbers that parse to Double, or one
	 *               of the symbols +, -, /, *, ( or ), and all parentheses must
	 *               be matched and properly nested.
	 */
	public Parser(String input) {
		this.tokens = input.split("\\s+");
		currentToken = 0;
	}

	/**
	 * Parse the input and build the expression tree
	 * 
	 * @return the (root node of) the resulting tree
	 */
	public TreeNode parse() {
		return expression();
	}

	/**
	 * Parse the remaining input as far as needed to get the next factor
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode factor() {
		TreeNode result = null;
		// if the token is "(", it means this is the start of (E).
		if(currentToken<tokens.length&&tokens[currentToken].equals("(")){
			currentToken++;
		    return expression();
		}
		// if the token is "-", it means this is an unary minus factor.
		else if(currentToken<tokens.length&&tokens[currentToken].equals("-")){
			currentToken++;
			TreeNode dummy = new UnaryMinusTreeNode(factor());
		    return dummy;
		}
		// by default, the token is a double in String form.
		else if(currentToken<tokens.length){
		    double dummy = Double.parseDouble(tokens[currentToken]);
		    result = new LeafTreeNode(dummy);
		    currentToken++;
		}
		return result;
	}

	/**
	 * Parse the remaining input as far as needed to get the next term
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode term() {
		TreeNode result = factor(), temp = null;
		// jump out when we meet the end of the array or when the 
        // current token is ")".
		while(currentToken<tokens.length&&!tokens[currentToken].equals(")")
             &&(tokens[currentToken].equals("*")||tokens[currentToken].equals("/"))){
        	    int dummy = currentToken; // use a temporary integer to save the index
        	    currentToken++; // move forward the index
        	    TreeNode result2 = factor();
        	    // define the type of the root TreeNode.
        	    if(tokens[dummy].equals("*")) 
        		    temp = new MultiplicationTreeNode(result,result2);
        	    else temp = new DivisionTreeNode(result,result2);
        	    result = temp; // set the root to the return TreeNode.
        }
		return result;
		
	}

	/**
	 * Parse the remaining input as far as needed to get the next expression
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode expression() {
        TreeNode result = term(), temp = null;
        // jump out when we meet the end of the array or when the 
        // current token is ")".
        while(currentToken<tokens.length&&!tokens[currentToken].equals(")")
            &&(tokens[currentToken].equals("+")||tokens[currentToken].equals("-"))){
        	    int dummy = currentToken; // use a temporary integer to save the index
        	    currentToken++; // move forward the index
        	    TreeNode result2 = term();
        	    // define the type of the root TreeNode.
        	    if(tokens[dummy].equals("+")) 
        		    temp = new AdditionTreeNode(result,result2);
        	    else temp = new SubtractionTreeNode(result,result2);
        	    result = temp; // set the root to the return TreeNode.
        }
        // when the current token is ")", it means this is the end of (E).
        // therefore, we should move the pointer forward.
        if(currentToken<tokens.length&&tokens[currentToken].equals(")"))
			currentToken++;
		return result;
	}
	
}
