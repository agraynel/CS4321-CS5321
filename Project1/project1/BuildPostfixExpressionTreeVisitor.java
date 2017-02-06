package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * traverse a tree to create a list representing the same 
 * expression in postfix form.
 * 
 * @author jz699 JUNCHEN ZHAN yc2329 YI CHEN
 */
public class BuildPostfixExpressionTreeVisitor implements TreeVisitor {

	private ListNode node; // an artificial node;
	private ListNode temp; // the node that traverse the tree.
	
	public BuildPostfixExpressionTreeVisitor() {
		node = new NumberListNode(0.0);
		temp = node; // start point
	}

	/**
	 * Method to get the head of the listNode.
	 * @return the head of the linked list.
	 */
	public ListNode getResult() {
		return node.getNext();
	}

	/**
	 * Visit method for leaf node;
	 * just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(LeafTreeNode node) {
		NumberListNode dummy = new NumberListNode(node.getData());
		temp.setNext(dummy);
		temp = temp.getNext();
	}

	/**
	 * Visit method for unary minus node;
	 * post-traverse the tree.  just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		node.getChild().accept(this);
		UnaryMinusListNode dummy = new UnaryMinusListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
	}

	/**
	 * Visit method for addition node; 
	 * post-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		AdditionListNode dummy = new AdditionListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
	}

	/**
	 * Visit method for multiplication node; 
	 * post-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		MultiplicationListNode dummy = new MultiplicationListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
	}

	/**
	 * Visit method for subtraction node; 
	 * post-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		SubtractionListNode dummy = new SubtractionListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
	}

	/**
	 * Visit method for division node; 
	 * post-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionTreeNode node) {
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		DivisionListNode dummy = new DivisionListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
	}

}
