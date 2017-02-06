package cs4321.project1;

import cs4321.project1.list.DivisionListNode;
import cs4321.project1.list.SubtractionListNode;
import cs4321.project1.list.NumberListNode;
import cs4321.project1.list.AdditionListNode;
import cs4321.project1.list.MultiplicationListNode;
import cs4321.project1.list.UnaryMinusListNode;
import java.util.Stack;

/**
 * traverse a list representing an expression in postfix
 * form, and evaluates the expression to a single number.
 * 
 * @author jz699 JUNCHEN ZHAN yc2329 YI CHEN
 */
public class EvaluatePostfixListVisitor implements ListVisitor {

	private Stack<Double> stack; // use a stack to handle the problem
	
	public EvaluatePostfixListVisitor() {
		stack = new Stack<>();
	}

	/**
	 * Method to get the result when we finished traversing the list.
	 * @return double representation of the answer
	 */
	public double getResult() {
		if(stack.size()==0) return 0.0;
		// in case the stack is empty.
		return stack.peek(); // normal return.
	}

	/**
	 * Visit method for number node; push the value on the stack
	 * and move on to next node
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(NumberListNode node) {
		stack.push(node.getData());
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for addition node, pop two elements out of the stack
	 * and perform an addition, push the result back.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionListNode node) {
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d1+d2);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for subtraction node, pop two elements out of the stack
	 * and perform a subtraction, push the result back.
	 * notice the latter element is the one to be subtracted.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionListNode node) {
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d2-d1);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for multiplication node, pop two elements out of the stack
	 * and perform a multiplication, push the result back.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationListNode node) {
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d1*d2);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for division node, pop two elements out of the stack
	 * and perform a division, push the result back.
	 * notice the latter element is the one to be divided.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionListNode node) {
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d2/d1);
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

	/**
	 * Visit method for unary minus node; set the top of the data
	 * on the stack to its negative form
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusListNode node) {
		stack.push(stack.pop()*(-1.0));
		if(node.getNext()!=null)
		    node.getNext().accept(this);
	}

}
