package cs4321.project1;

import cs4321.project1.tree.DivisionTreeNode;
import cs4321.project1.tree.LeafTreeNode;
import cs4321.project1.tree.SubtractionTreeNode;
import cs4321.project1.tree.AdditionTreeNode;
import cs4321.project1.tree.MultiplicationTreeNode;
import cs4321.project1.tree.UnaryMinusTreeNode;
import java.util.Stack;

/**
 * Traverse a tree to evaluate the corresponding expression
 * to a single number.
 * 
 * @author jz699 JUNCHEN ZHAN yc2329 YI CHEN
 */

public class EvaluateTreeVisitor implements TreeVisitor {

	private Stack<Double> stack; 
	
	public EvaluateTreeVisitor() {
		stack = new Stack<>();
	}

	/**
	 * Method to get the result when we finished traversing the tree.
	 * @return double representation of the answer.
	 */
	public double getResult() {
		if(stack.size()==0) return 0.0; 
		// in case when the tree is empty.
		return stack.peek(); // normal case.
	}

	/**
	 * Visit method for leaf node; just push the value on the stack.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(LeafTreeNode node) {
		stack.push(node.getData());
	}

	/**
	 * Visit method for unary minus node; post-traverse the tree
	 *  set the top of the data on the stack to its negative form.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		node.getChild().accept(this);
		stack.push(stack.pop()*(-1.0));
	}

	/**
	 * Visit method for addition node, post-traverse the tree
	 * pop two elements out of the stack
	 * and perform an addition, push the result back.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d1+d2);
	}

	/**
	 * Visit method for addition node, post-traverse the tree
	 * pop two elements out of the stack
	 * and perform a multiplication, push the result back.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d1*d2);
	}

	/**
	 * Visit method for subtraction node, post-traverse the tree
	 * pop two elements out of the stack
	 * and perform a subtraction, push the result back.
	 * notice the latter element is the one to be subtracted.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d2-d1);
	}

	/**
	 * Visit method for division node, post-traverse the tree
	 * pop two elements out of the stack
	 * and perform a division, push the result back.
	 * notice the latter element is the one to be divided.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double d1 = stack.pop(), d2 = stack.pop();
		stack.push(d2/d1);
	}
}
