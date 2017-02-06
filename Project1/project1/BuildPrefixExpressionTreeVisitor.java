package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * traverse a tree to create a list representing the same expression
 * in prefix form
 * 
 * @author jz699, JUNCHEN ZHAN yc2329 YI CHEN
 */
public class BuildPrefixExpressionTreeVisitor implements TreeVisitor {

	private ListNode node; // an artificial node;
	private ListNode temp; // the node that traverse the tree.
	
	public BuildPrefixExpressionTreeVisitor() {
		node = new NumberListNode(0.0);
		temp = node; // set the start point
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
	 * pre-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		UnaryMinusListNode dummy = new UnaryMinusListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
		node.getChild().accept(this);
	}

	/**
	 * Visit method for addition node;
	 * pre-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionTreeNode node) {
		AdditionListNode dummy = new AdditionListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

	/**
	 * Visit method for multiplication node;
	 * pre-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationTreeNode node) {
		MultiplicationListNode dummy = new MultiplicationListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

	/**
	 * Visit method for subtraction node;
	 * pre-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionTreeNode node) {
		SubtractionListNode dummy = new SubtractionListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

	/**
	 * Visit method for division node;
	 * pre-traverse the tree. just attach it to the tail of the list.
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionTreeNode node) {
		DivisionListNode dummy = new DivisionListNode();
		temp.setNext(dummy);
		temp = temp.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

}
