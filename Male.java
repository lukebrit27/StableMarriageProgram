// Luke Britton June 2017
// This is a sub class of the Person class
public class Male extends Person
{
	Male (int personNo, int listLength)
	{
		super(personNo, listLength);
		isMale = true;
	}
	
	public Female getSpouse(Marriages M)
	{
		return M.getMansSpouse(this.getPersonNo());
	}
	
	
	// check if male is equal to this male
	public boolean isEqualTo(Male m)
	{
		if (this.getPersonNo() == m.getPersonNo())	
			return true;
			
		return false;
	}
}