package advent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day04
{
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException
	{
		String input = FileUtility.textFileToString("input/04.txt");
		
		// Part one
		FileUtility.stringToTextFile(Integer.toString(smallestKey(input, 5)), "output/04A.txt");
		
		// Part two
		FileUtility.stringToTextFile(Integer.toString(smallestKey(input, 6)), "output/04B.txt");
	}
	
	private static int smallestKey(String input, int leadingZeros) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest digest = MessageDigest.getInstance("MD5");
		
		int i = 1;
		while (true) {
			// Get hash of secret key + current number
			byte[] hash = digest.digest((input + Integer.toString(i)).getBytes("UTF-8"));
			
			// Test for five leading hexadecimal zeros
			boolean hasLeadingZeros = true;
			for (int j = 0; j < leadingZeros; ++j) {
				// Determine whether we need upper or lower half (hex digit) of byte
				byte shiftBy =  (byte) ((j % 2 == 0) ? 4 : 0);

				// Acquire bits corresponding to just the hex digit in question
				if (((hash[j / 2] >> shiftBy) & 0xFF) != 0)
					hasLeadingZeros = false;
			}
			
			if (hasLeadingZeros)
				return i;
			
			i++;
		}
	}
}
