package advent;

import java.io.IOException;

public class Day23 
{
	public static void main(String[] args) throws IOException
	{
		String input = FileUtility.textFileToString("input/23.txt");
		
		// Part one
		FileUtility.stringToTextFile(Long.toString(runProgram(input, 0)), "output/23A.txt");

		// Part two
		FileUtility.stringToTextFile(Long.toString(runProgram(input, 1)), "output/23B.txt");
	}
	
	private static enum Instruction
	{
		HLF("hlf", 0, -1), 
		TPL("tpl", 0, -1),
		INC("inc", 0, -1),
		JMP("jmp", -1, 0),
		JIE("jie", 0, 1),
		JIO("jio", 0, 1);
		
		private String name;
		
		// These are the indexes (within an argument array) where the given
		// argument would be found. -1 indicates not present.
		private int registerPos;
		private int offsetPos;
		
		private Instruction(String name, int registerPos, int offsetPos)
		{
			this.name = name;
			this.registerPos = registerPos;
			this.offsetPos = offsetPos;
		}
		
		public static Statement parse(String line)
		{
			String[] statement = line.split(",?\\s+");
			
			// Attempt to match the statement with an instruction
			Instruction instruction = null;
			for (Instruction potential : Instruction.values())
				if (potential.name.equals(statement[0]))
					instruction = potential;
			
			if (instruction == null)
				throw new IllegalArgumentException();
			
			int register = -1, offset = 1;
			
			if (instruction.registerPos >= 0)
				register =  ("a".equals(statement[1 + instruction.registerPos])) ? 0 : 1;
			
			if (instruction.offsetPos >= 0)
				offset = Integer.parseInt(statement[1 + instruction.offsetPos]);

			return new Statement(instruction, register, offset);
		}
	}
	
	private static class Statement
	{
		public final Instruction instruction;
		public final int register;
		public final int offset;
		
		public Statement(Instruction instruction, int register, int offset)
		{
			this.instruction = instruction;
			this.register = register;
			this.offset = offset;
		}
	}
	
	private static long runProgram(String input, int initialA)
	{
		// Parse the program into Statements
		String[] lines = input.split("\r\n|\r|\n");
		Statement[] program = new Statement[lines.length];
		for (int i = 0; i < lines.length; ++i)
			program[i] = Instruction.parse(lines[i]);
		
		// Run the program
		int index = 0;
		long[] registers = { initialA, 0 };
		while (index < lines.length) {
			// Compute the instruction
			Statement current = program[index];
			switch (current.instruction) {
			case HLF:
				registers[current.register] /= 2;
				index++;
				break;
			case TPL:
				registers[current.register] *= 3;
				index++;
				break;
			case INC:
				registers[current.register] += 1;
				index++;
				break;
			case JMP:
				index += current.offset;
				break;
			case JIE:
				if (registers[current.register] % 2 == 0)
					index += current.offset;
				else
					index++;
				break;
			case JIO:
				if (registers[current.register] == 1)
					index += current.offset;
				else
					index++;
				break;
			}
		}
		
		return registers[1];
	}

}
