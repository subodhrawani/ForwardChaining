package ai_project_final;

/*
 * Usage:
  BackwardChaining bc=new BackwardChaining();
  Queue<String> FiredFacts=bc.getFacts(GOAL);// 
  boolean GOALVALIDITY=bc.validate();//returns the validity of goal
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JOptionPane;

public class BackwardChaining {

	private boolean flag=false;
    static Stack<String> FactStack = new Stack<String>();
    static Map<String,String> FactDetails=new HashMap<String,String>();
    static Queue<String> TrueFacts = new LinkedList<String>();

	private Connection conn = null;
	DisplayTree dt = new DisplayTree();
	public BackwardChaining() {
		String url = "jdbc:mysql://localhost:3306/";  
		String dbName = "ai_project";  
		String driver = "com.mysql.jdbc.Driver";  
		String userName = "root";  
		String password = "";	
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = DriverManager  
					.getConnection(url + dbName, userName, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	   public static void main(String[] args)
	{
		System.out.println("--Started--");
		BackwardChaining bc=new BackwardChaining();
		bc.getFacts("43");
		/*
		for(Object object : bc.TrueFacts) {
			//System.out.print("suman");
		    String element = (String) object;
		    System.out.print(element);
		}*/
		//System.out.println("Given goal is "+bc.flag);
	}
	   
       
	public Stack<String> getFacts(String getindex) {
		//String getindex=getFactIndex(goal);
		//System.out.print(getindex);
		FactStack.add(getindex);
		FactDetails.put(getindex, getFactName(getindex));
		dt.initiateTree();
		developFactStack(getindex);
		return FactStack;
	}
	public boolean validate(){
		return flag;
	}
	
	private void developFactStack(String fact){//Stores derivation in stack
		String query=null;
		try {
			query= "Select * from factder where FactIndex='"+fact+"'";
			ResultSet result=queryDatabase(query);
			if(result.next()) {
				String derivedFrom=result.getString("DerivedFrom");
				String dependentfactarray[]=derivedFrom.split("[+]");
				flag=false;

				for(String dfa: dependentfactarray) {
					
					//System.out.println(fact+"derieving-->"+dfa);
					if(flag==true)
						break;
					String dependentfact[]=dfa.split("[.]");
					FactStack.add("(");
					int i=0;
					for(String df: dependentfact) {
						i++;
						FactStack.add(df);
						
						FactDetails.put(df, getFactName(df));
						
						/*
						System.out.println("++++++++++++++++++++++");
						for(Object object : FactStack) {
						    String element = (String) object;
						    System.out.print(element);
						}
						System.out.println("\n++++++++++++++++++++++");*/
						
						
						
						developFactStack(df);
						if(flag==false) {
							while(i--!=0) {
								FactStack.pop();
							}
							FactStack.pop();
							break;
						}
							
					}
					if(flag!=false)
					{
						FactStack.add(")");
						TrueFacts.add(fact);
					}

					
				}
			}
			else{
				//System.out.println(fact);
				
				boolean stackpresent=false;
				for(Object object : TrueFacts) {
				    String element = (String) object;
				    if(element.compareTo(fact)==0) {
				    	stackpresent=true;
				    	flag=true;
				    	break;
				    }
				}
				if(stackpresent==false){
					flag=(new PromptWindow()).promptWindow(fact,getFactName(fact));
					if(flag==true)
						TrueFacts.add(fact);
				}
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getFactName(String factindex){
		String query="Select * from factsinfo where FactIndex=' "+factindex+"'";
		ResultSet result;
		try {
			result = queryDatabase(query);
			if(result.next()) {
				return result.getString("Facts");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	private String BASIC(String factindex){
		String query="Select * from factsinfo where FactIndex=' "+factindex+"'";
		ResultSet result;
		try {
			result = queryDatabase(query);
			if(result.next()){
				//System.out.println(result.getString("BasicorDerived"));
				return result.getString(3);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private String getFactIndex(String fact){
		String query="Select * from factsinfo where Facts='"+fact+"'";
		//System.out.println(query);

		ResultSet result;
		try {
			result = queryDatabase(query);
			while(result.next()){
				String index=result.getString(2);
				//System.out.println(index);

				return index;
				//return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	private ResultSet queryDatabase(String query) throws SQLException
	{
		PreparedStatement pst = null;
		pst = this.conn.prepareStatement(query);
		return (pst.executeQuery());
	}
}

class PromptWindow {
	protected boolean promptWindow(String fact, String factname) {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		
		int dialogResult=JOptionPane.showConfirmDialog (null, fact+": \""+factname+"\" - True/False ?","Backward Chaining Fact Validation",dialogButton);

        if(dialogResult == JOptionPane.YES_OPTION){ //The ISSUE is here
        	return true;
        }
        return false;
	}
}
