package cs4321.project1;

import cs4321.project1.list.*;
import java.util.*;

/**
 * traverse a list representing an expression in postfix
 * form and evaluates the expression to a single number.
 * 
 * @author jz699 JUNCHEN ZHAN yc2329 YI CHEN
 */

public class EvaluatePrefixListVisitor implements ListVisitor {

	/**
	 * set a new class to handle the requirement of the second stack
	 */
	private class Mule{
		
		private char c; // the character to handle.
		private int i; // the times of the number the character has.
		
		private Mule(char c){
			this.c = c;
		}
		
	}
	
	private Stack<Double> result;
	private Stack<Mule> dummy;
	private double sign; // to track the unary minus mark.
	
	public EvaluatePrefixListVisitor() {
		result = new Stack<>();
		dummy = new Stack<>();
		sign = 1.0;
	}

	/**
	 * Method to get the result when we finished traversing the list.
	 * @return double representation of the answer
	 */
	public double getResult() {
		if(result.size()==0) return 0.0;
		// in case the list is empty
		return result.peek(); // normal return
	}

	/**
	 * Visit method for number node; push the value on the stack
	 * and move on to next node, add the field i of the top element
	 * of the stack with 1. When it reaches two, perform the algebra.
	 * and add the field i of the new top element of the stack with 1.
	 * do the same when it reaches two. Repeat this process until the 
	 * field i of the top element of the stack is not 2 anymore or
	 * when the stack of mule is empty.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(NumberListNode node) {
		result.push(node.getData()*sign);
		if(dummy.isEmpty()) return;
		dummy.peek().i++;
		while(dummy.peek().i==2){
			char ch = dummy.pop().c;
			double d1 = result.pop(), d2 = result.pop();
			switch(ch){
			    case '+' : result.push(d1+d2);
			               break;
			    case '-' : result.push(d2-d1);
			               break;
			    case '*' : result.push(d1*d2);
			               break;
			    case '/' : result.push(d2/d1);
			               break;
			}
			if(dummy.isEmpty()) break;
			dummy.peek().i++;
		}
		sign = 1.0; // set sign back to 1.0
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for addition node, push a mule with character
	 * '+' on the second stack.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionListNode node) {
		Mule mule = new Mule('+');
		dummy.push(mule);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for subtraction node, push a mule with character
	 * '-' on the second stack.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionListNode node) {
		Mule mule = new Mule('-');
		dummy.push(mule);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for multiplication node, push a mule with character
	 * '*' on the second stack.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationListNode node) {
		Mule mule = new Mule('*');
		dummy.push(mule);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}
    
	/**
	 * Visit method for division node, push a mule with character
	 * '/' on the second stack.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionListNode node) {
		Mule mule = new Mule('/');
		dummy.push(mule);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for unary minus node, set the sign to negative part
	 * for the coming number.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusListNode node) {
		sign = -sign; 
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}
}
