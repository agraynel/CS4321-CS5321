package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.*;

public class EvaluateTreeVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleLeafNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new AdditionTreeNode(n1, n2);
		TreeNode n4 = new AdditionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testMultiplicationNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new MultiplicationTreeNode(n1, n2);
		TreeNode n4 = new MultiplicationTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(2.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}

	@Test
	public void testStudentSettings1() {
		TreeNode n1 = new LeafTreeNode(6.0);
		TreeNode n2 = new LeafTreeNode(3.0);
		TreeNode n3 = new LeafTreeNode(1.0);
		TreeNode n4 = new AdditionTreeNode(n2,n3);
		TreeNode n5 = new UnaryMinusTreeNode(n1);
		TreeNode n14 = new UnaryMinusTreeNode(n5);
		TreeNode n6 = new MultiplicationTreeNode(n14,n4);
		TreeNode n7 = new LeafTreeNode(3.0);
		TreeNode n8 = new LeafTreeNode(2.0);
		TreeNode n9 = new LeafTreeNode(4.0);
		TreeNode n10 = new UnaryMinusTreeNode(n9);
		TreeNode n11 = new SubtractionTreeNode(n8,n10);
		TreeNode n12 = new SubtractionTreeNode(n7,n11);
		TreeNode n13 = new DivisionTreeNode(n6,n12);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n13.accept(v1);
		assertEquals(-8.0, v1.getResult(), DELTA);
		EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n6.accept(v2);
		assertEquals(24.0, v2.getResult(), DELTA);
		EvaluateTreeVisitor v3 = new EvaluateTreeVisitor();
		n12.accept(v3);
		assertEquals(-3.0, v3.getResult(), DELTA);
	}
	
	@Test
	public void testStudentSettings2() {
		TreeNode n1 = new LeafTreeNode(6.5);
		TreeNode n2 = new LeafTreeNode(3.1);
		TreeNode n3 = new LeafTreeNode(2.9);
		TreeNode n4 = new UnaryMinusTreeNode(n2);
		TreeNode n5 = new UnaryMinusTreeNode(n3);
		TreeNode n6 = new AdditionTreeNode(n4,n5);
		TreeNode n7 = new MultiplicationTreeNode(n1,n6);
		TreeNode n8 = new LeafTreeNode(7.5);
		TreeNode n9 = new LeafTreeNode(3.0);
		TreeNode n10 = new UnaryMinusTreeNode(n8);
		TreeNode n11 = new DivisionTreeNode(n10,n9);
		TreeNode n12 = new AdditionTreeNode(n7,n11);
		TreeNode n13 = new LeafTreeNode(24.4);
		TreeNode n14 = new LeafTreeNode(20.4);
		TreeNode n15 = new UnaryMinusTreeNode(n13);
		TreeNode n16 = new UnaryMinusTreeNode(n14);
		TreeNode n17 = new SubtractionTreeNode(n15,n16);
		TreeNode n18 = new MultiplicationTreeNode(n12,n17);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n18.accept(v1);
		assertEquals(166.0, v1.getResult(), DELTA);
		EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n12.accept(v2);
		assertEquals(-41.5, v2.getResult(), DELTA);
	}
}
