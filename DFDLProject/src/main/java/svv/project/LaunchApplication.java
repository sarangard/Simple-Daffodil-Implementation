package svv.project;

/** 
 * Calls the SchemaValidator method from main. 
 * @author SaiManjula
 * @author Sarang
 */

public class LaunchApplication 
{

	public static void main(String[] args) throws Exception 
	{
		SchemaValidator sv = new SchemaValidator();
		
		sv.Execute();
	}

}
