// Luke Britton June 2017
public class PreferenceList
{

	private Person[] list;
	
	public PreferenceList(Person[] l)
	{
		list = l;
	}
	
	
	public int getListPosition(Person p)
	{
		int pos = -1;
		int listLength = list.length;
		int personNo = p.getPersonNo();
		
		for (int i = 0; i < listLength; i++)
		{
			if (personNo == list[i].getPersonNo())
			{
				pos = i;
				break;
			}
			
			
		}
		
		return pos;
	}
	
}