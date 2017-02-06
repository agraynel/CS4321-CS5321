package cs4321.project1;

import cs4321.project1.list.*;

/**
 * traverses a list representing an expression in either
 * prefix or postfix form, and creates a String that 
 * represents the expression.
 * 
 * @author jz699 JUNCHEN ZHAN yc2329 YI CHEN
 */

public class PrintListVisitor implements ListVisitor {

	private String result;  // return string representation.
	
	public PrintListVisitor() {
		result = "";
	}

	/**
	 * Method to get the finished string representation when visitor is done
	 * 
	 * @return string representation of the visited tree
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Visit method for number node
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(NumberListNode node) {
		result += node.getData();
		if(node.getNext()!=null){
			result += " ";
			node.getNext().accept(this);
		}
	}

	/**
	 * Visit method for addition node 
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionListNode node) {
		result += "+";
		if(node.getNext()!=null){
			result += " ";
			node.getNext().accept(this);
		}
	}

	/**
	 * Visit method for subtraction node 
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionListNode node) {
        result += "-";
        if(node.getNext()!=null){
			result += " ";
			node.getNext().accept(this);
		}
	}

	/**
	 * Visit method for multiplication node 
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationListNode node) {
		result += "*";
		if(node.getNext()!=null){
			result += " ";
			node.getNext().accept(this);
		}
	}

	/**
	 * Visit method for division node
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionListNode node) {
		result += "/";
		if(node.getNext()!=null){
			result += " ";
			node.getNext().accept(this);
		}
	}

	/**
	 * Visit method for unary minus node
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusListNode node) {
		result += "~";
		if(node.getNext()!=null){
			result += " ";
			node.getNext().accept(this);
		}
	}

}
