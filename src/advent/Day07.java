package advent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/07.txt");
		
		// Part one
		String result = Integer.toString(solveCircuit(input).get("a"));
		FileUtility.stringToTextFile(result, "output/07A.txt");
		
		/* Part two */
		
		// Assign b the value of a from the previous computation
		// Do this either be changing an existing assignment or inserting a new one
		Matcher matcher = Pattern.compile("\\d+ -> b").matcher(input);
		if (matcher.find())
			input = matcher.replaceFirst(result + " -> b");
		else
			input = result + " -> b\n" + input;

		// Solve for 'a' with the new input in mind
		FileUtility.stringToTextFile(Integer.toString(solveCircuit(input).get("a")), "output/07B.txt");
	}
	
	private static Map<String, Integer> solveCircuit(String input)
	{
		String[] lines = input.split("\r\n|\r|\n");
		
		// Parse all statements
		List<Statement> statements = new LinkedList<Statement>();
		for (String line : lines)
			statements.add(parseStatement(line));
		
		// Find initially solvable statments (direct assignment from constants)
		Map<String, Integer> knowns = new HashMap<String, Integer>(lines.length);
		Iterator<Statement> itr = statements.iterator();
		while (itr.hasNext()) {
			Statement statement = itr.next();
			if (statement.operation == Operation.NONE && statement.getTerm(0) instanceof Constant) {
				knowns.put(statement.result.getName(),
						((Constant) statement.getTerm(0)).getValue());
				itr.remove();
			}
		}
		
		while (!statements.isEmpty()) {
			itr = statements.iterator();
			while (itr.hasNext()) {
				Statement statement = itr.next();
				
				// If the statement could be solved successfully, remove it
				if (solve(knowns, statement))
					itr.remove();
			}
		}
		
		return knowns;
	}
	
	private static boolean solve(Map<String, Integer> knowns, Statement statement)
	{
		int[] termValues = new int[statement.terms()];
		
		// Attempt to get int values for all terms - return false if it fails
		for (int i = 0; i < statement.terms(); ++i) {
			Term term = statement.getTerm(i);
			
			if (term instanceof Constant) {
				termValues[i] = ((Constant) term).getValue();
			} else {
				Variable var = (Variable) term;
				if (knowns.containsKey(var.getName()))
					termValues[i] = knowns.get(var.getName());
				else
					return false;
			}
		}
		
		// Compute the result, add it to the knowns, and indicate success
		knowns.put(statement.result.getName(), statement.operation.compute(termValues));
		return true;
	}
	
	private static Statement parseStatement(String statement)
	{
		String[] tokens = statement.split("\\s+");
		
		// If the statement has only 3 tokens, it must be a straight assignment
		if (tokens.length == 3)
			return new Statement(tokens[2], tokens[0]);
		// If first term starts w/ uppercase letter, it's a unary op statement
		if (Character.isUpperCase(tokens[0].charAt(0)))
			return new Statement(tokens[0], tokens[3], tokens[1]);
		// Otherwise, must be a binary op statement
		else
			return new Statement(tokens[1], tokens[4], tokens[0], tokens[2]);
	}
	
	private static class Statement
	{
		private final List<Term> terms;
		public final Operation operation;
		public final Variable result;
		
		public Statement(String operation, String result, String ... givenTerms)
		{
			terms = new ArrayList<Term>(givenTerms.length);
			for (String term : givenTerms) {
				if (Character.isAlphabetic(term.charAt(0)))
					terms.add(new Variable(term));
				else
					terms.add(new Constant(Integer.parseInt(term)));
			}
			
			this.operation = Operation.parseOp(operation);
			this.result = new Variable(result);
		}
		
		public Statement(String result, String term)
		{
			this.operation = Operation.NONE;
			this.result = new Variable(result);
			
			terms = new ArrayList<Term>(1);
			if (Character.isAlphabetic(term.charAt(0)))
				terms.add(new Variable(term));
			else
				terms.add(new Constant(Integer.parseInt(term)));
		}
		
		public Term getTerm(int index)
		{
			return terms.get(index);
		}
		
		public int terms()
		{
			return terms.size();
		}
	}
	
	private static abstract class Term
	{
		
	}
	
	private static class Variable extends Term
	{
		private final String name;
		
		public Variable(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
	}
	
	private static class Constant extends Term
	{
		private final int value;
		
		public Constant(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}
	
	private static enum Operation 
	{
		AND, OR, NOT(1), LSHIFT, RSHIFT, NONE(1);
		
		private final int operands;
		
		private Operation()
		{
			operands = 2;
		}
		
		private Operation(int operands)
		{
			this.operands = operands;
		}
		
		public int compute(int ... terms)
		{
			if (terms.length != operands)
				throw new IllegalArgumentException("Incorrect number of operands");
			
			switch (this) {
			case AND:		return terms[0] & terms[1];
			case OR:		return terms[0] | terms[1];
			case NOT:		return (~terms[0]) & 0xFFFF;
			case LSHIFT:	return (terms[0] << terms[1]) & 0xFFFF;
			case RSHIFT:	return (terms[0] >> terms[1]) & 0xFFFF;
			case NONE:		return terms[0];
			default:		throw new UnsupportedOperationException();
			}
		}
		
		public static Operation parseOp(String op)
		{
			switch (op) {
			case "AND":		return AND;
			case "OR":		return OR;
			case "NOT":		return NOT;
			case "LSHIFT":	return LSHIFT;
			case "RSHIFT":	return RSHIFT;
			default:		throw new UnsupportedOperationException();
			}
		}
	}
}
