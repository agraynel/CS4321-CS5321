package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.TreeNode;

public class ParserTest {

	/*
	 * This class depends on the correct functioning of PrintTreeVisitor(), which is provided for you.
	 */
			
	@Test
	public void testSingleNumber() {
		Parser p1 = new Parser("1.0");
		TreeNode parseResult1 = p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("1.0", v1.getResult());

	}
	
	@Test
	public void testUnaryMinusSimple() {
		Parser p1 = new Parser("- 1.0");
		TreeNode parseResult1 = p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("(-1.0)", v1.getResult());

	}
	
	@Test
	public void testUnaryMinusComplex() {
		Parser p1 = new Parser("- - 1.0");
		TreeNode parseResult1 =  p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("(-(-1.0))", v1.getResult());

	}
	
	@Test
	public void testStudentSettings1() {
		Parser p1 = new Parser("( ( ( ( 3.0 + 2.0 + 3.0 ) ) "
				+ "/ ( 2.0 - ( ( - - ( - 1.0 ) ) + 3.0 * 6.0 / 1.2 ) ) ) )");
		TreeNode parseResult1 =  p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("(((3.0+2.0)+3.0)/(2.0-((-(-(-1.0)))+((3.0*6.0)/1.2))))",
				v1.getResult());
	}

	@Test
	public void testStudentSettinggs2() {
		Parser p1 = new Parser("( 6.5 * ( 3.1 + 2.9 ) + ( - 7.5 ) / 3.0 ) "
				+ "* ( - 24.4 - - 20.4 )");
		TreeNode parseResult1 =  p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("(((6.5*(3.1+2.9))+((-7.5)/3.0))*((-24.4)-(-20.4)))", 
				v1.getResult());
	}
}
