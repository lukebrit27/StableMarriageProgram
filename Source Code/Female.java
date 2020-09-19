// Luke Britton June 2017
// This is a sub class of the Person class
import java.util.*;
public class Female extends Person
{
	
	
	public Female(int personNo, int listLength)
	{
		super(personNo, listLength);
		isMale = false;
	}
	
	public Male getSpouse(Marriages M)
	{
		return M.getWomansSpouse(this.getPersonNo());
	}
	
	
	// check if female is equal to this female
	public boolean isEqualTo(Female w)
	{
		if (this.getPersonNo() == w.getPersonNo())	
			return true;
			
		return false;
	}
	
}