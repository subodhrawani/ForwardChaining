package ai_project_final;

import java.io.IOException;  
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewRules {
	
	public NewRules(){
		
	}
	
	// to return the no of facts in the given rule
	private int no_of_facts(String rule){
		String[] words = rule.split("\\s");
		int result = 1;  //no of facts in the rule
		for(int j=1; j<words.length; j++){
			if(words[j].equals("AND") || words[j].equals("OR") || words[j].equals("THEN")){
				result++;
			}
		}
		//System.out.println(result);
		return result;
	}
	
	public String EnterRule(String str){
		Connection conn = null;  
	    PreparedStatement pst = null, pst2 =null, pst3=null;  
	    ResultSet rs = null, rs1 = null,rs0=null,rs3=null;
	    String url = "jdbc:mysql://localhost:3306/";  
	    String dbName = "AI_Project";  
	    String driver = "com.mysql.jdbc.Driver";  
	    String userName = "root";  
	    String password = "";
	    int RuleIndex, DerivedFact;
	    
	    try {
			Class.forName(driver).newInstance();
			conn = DriverManager  
	                .getConnection(url + dbName, userName, password);
			String query = "SELECT * from RuleInfo where Rule=?";
			pst = conn.prepareStatement(query);
			pst.setString(1, str);
			rs= pst.executeQuery();
			
			if(rs.next()){
				// If the given rule is in table already
				return "Rule already exists in the Knowledge Database";
			}
			else{
				
				query = "INSERT into RuleInfo (Rule) VALUES (\"" + str + "\");";
				pst = conn.prepareStatement(query);
				pst.execute();
				
				query = "SELECT * from RuleInfo where Rule=?";
				pst = conn.prepareStatement(query);
				pst.setString(1, str);
				rs= pst.executeQuery();
				
				rs.next();
				RuleIndex = rs.getInt(2);
				
			}
			
			int k=0;
		    int lhs=0;  //no of facts in the LHS
		    int n = no_of_facts(str);
		    String[] facts = new String[n];
		    java.util.Arrays.fill(facts ,"");
		    String[] words = str.split("\\s");
		    
		    for(int i =1 ; i<words.length; i++){
		    	if(words[i].equals("THEN")){
		    		// To count no. of facts in the LHS
		    		lhs = k;
		    	}
		    	if(words[i].equals("AND") || words[i].equals("OR") || words[i].equals("THEN")){
		    		facts[k].trim();
		    		k++;
		    		
		    	}
		    	else{
		    		facts[k] = facts[k] + words[i] + " ";
		    	}
		    }
		   //System.out.println(lhs);
		   
		   
		    // For each facts in the LHS part
		    for(int i=0; i<=lhs; i++){
		    	int FactIndex;
		    	query = "SELECT * from factsinfo WHERE facts=\"" + facts[i] + "\";";
		    	//System.out.print(facts[i]);
		    	pst = conn.prepareStatement(query);
		    	rs=pst.executeQuery();
		    	
		    	if(rs.next()){
		    		FactIndex = rs.getInt(2);
		    		String BorD = rs.getString(3);	    		
		    		String query1 = "INSERT into factsLHS (FactIndex,FactInLHS) VALUES (" + FactIndex + "," + RuleIndex + ");";
		    		pst = conn.prepareStatement(query1);
					pst.execute();
		    		if(BorD.equals("D")){
		    			//System.out.println("Into it");
		    			String query2 = "SELECT * FROM FactsRHS where FactIndex=" + FactIndex + ";";
		    			pst = conn.prepareStatement(query2);
				    	rs1=pst.executeQuery();
				    	while(rs1.next()){
				    		int RuleToEdit;
				    		RuleToEdit = rs1.getInt(2);
				    		query2 = "INSERT INTO Ruleleadingto (RuleIndex, RuleTo) VALUES (" + RuleToEdit + "," + RuleIndex + ");";
				    		pst = conn.prepareStatement(query2);
							pst.execute();
				    	}
		    		}
		    		
		    		//update KB-Table-2 row of ri: update column ‘Dependent Facts’ with fj;
					query = "INSERT into RuleLHS (RuleIndex, FactLHS) VALUES (" + RuleIndex + "," + FactIndex + ");";
					pst2 = conn.prepareStatement(query);
					pst2.execute();
		    	}
		    	else{
		    		//enter fj into KB-Table-1: give an Index and update the 
		    		//column ‘List of  Rules Where Fact Appeared in LHS’ 
		    		//		with ri; 
		    		query = "INSERT into factsinfo (Facts,BasicorDerived) VALUES (\"" + facts[i] + "\",'B');";
		    		pst2 = conn.prepareStatement(query);
					pst2.execute();
					query = "SELECT * from factsinfo WHERE Facts=\"" + facts[i] + "\";";
					pst2 = conn.prepareStatement(query);
					rs1 = pst2.executeQuery();
					rs1.next();
					
					int factind = rs1.getInt(2);
					//System.out.println(factind);
					query = "INSERT into factsLHS (FactIndex, FactinLHS) VALUES (" + factind + "," + RuleIndex + ");";
					pst2 = conn.prepareStatement(query);
					pst2.execute();
					//update KB-Table-2 row of ri: update column ‘Dependent Facts’ with fj;
					query = "INSERT into RuleLHS (RuleIndex, FactLHS) VALUES (" + RuleIndex + "," + factind + ");";
					pst2 = conn.prepareStatement(query);
					pst2.execute();
					
					
				
		    	}
		    	
		    	
		    }
		    
		    //System.out.println("No of FActs are" + n);
		    //Now for the facts in th RHS
		    for(int i = lhs+1 ; i<n;i++){
		    	String query3 = "SELECT * from factsinfo WHERE Facts=\"" + facts[i] + "\";";
		    	pst2 = conn.prepareStatement(query3);
				rs3 = pst2.executeQuery();
				
				if(rs3.next()){
					//System.out.println("aigudiugadiu" + rs.getInt(2));
					int Factind = rs3.getInt(2);
					query3 = "SELECT * from factsLHS where FactIndex =" + Factind + ";";
					pst2 = conn.prepareStatement(query3);
					rs1 = pst2.executeQuery();
					while(rs1.next()){
						int RuleToEdit = rs1.getInt(2);
						//System.out.println(RuleToEdit);
						String query0 = "INSERT into Rulecomingfrom (RuleIndex, Rulefrom) VALUES (" + RuleToEdit + "," + RuleIndex + ");";
						pst2 = conn.prepareStatement(query0);
						pst2.execute();
						query0 = "INSERT into Ruleleadingto (RuleIndex, Ruleto) VALUES (" + RuleIndex +"," + RuleToEdit + ");";
						pst2 = conn.prepareStatement(query0);
						pst2.execute();
						
					}
				}
				else{
					query3 = "INSERT into factsinfo (Facts) VALUES (\"" + facts[i] + "\");";
					pst3 = conn.prepareStatement(query3);
					//System.out.println("Check");
					pst3.execute();
				}
				
				query3 = "UPDATE factsinfo SET BasicorDerived=\"D\" WHERE Facts=\"" + facts[i] + "\";";
				pst3 = conn.prepareStatement(query3);
				pst3.execute();
				query3 = "SELECT * from factsinfo WHERE Facts=\"" + facts[i] + "\";";
				pst3 = conn.prepareStatement(query3);
				rs0 = pst3.executeQuery();
				rs0.next();
				int FactIndice = rs0.getInt(2);
				query3 = "INSERT into factsRHS VALUES (" + FactIndice + "," + RuleIndex + ");";
				pst3 = conn.prepareStatement(query3);
				pst3.execute();
				query3 = "UPDATE RuleInfo SET DerivedFact=" + FactIndice + " WHERE RuleIndex=" + RuleIndex + ";";			
				pst3 = conn.prepareStatement(query3);
				pst3.execute();
				DerivedFact = FactIndice;
		    }
		    
		    query = "SELECT * from RuleLHS WHERE RuleIndex=" + RuleIndex + ";";
		    pst = conn.prepareStatement(query);
		    rs = pst.executeQuery();
		    String Formula = "";
		    while(rs.next()){
		    	Formula = Formula + rs.getInt(2) + ".";
		    	
		    }
		    Formula = Formula.substring(0, Formula.length()-1);
		    query = "Select DerivedFact from RuleInfo WHERE RuleIndex=" + RuleIndex + ";";
		    pst = conn.prepareStatement(query);
		    rs = pst.executeQuery();
		    rs.next();
		    DerivedFact=rs.getInt(1);
		    query= "SELECT * FROM FactDer WHERE FactIndex=" + DerivedFact + ";";
		    pst = conn.prepareStatement(query);
		    rs = pst.executeQuery();
		    while(rs.next()){
		    	String temp = "DELETE from factder WHERE DerivedFrom='" + rs.getString(2) + "';";
		    	pst2 = conn.prepareStatement(temp);
		    	pst2.execute();
		    	Formula = rs.getString(2) + "+" + Formula;
		    }
		    query = "INSERT into FactDer VALUES (" + DerivedFact + ",'" + Formula + "');";
		    pst = conn.prepareStatement(query);
		    pst.execute();
		    
		    
		    return null;
		    
			
			
			
			
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	    // First store the rule in the database
	    
	    
	    
	    
	    
		return null;
		
	}

	public void DerFact() {
            Connection conn = null;  
	    PreparedStatement pst = null, pst2 =null;  
	    ResultSet rs = null, rs1 = null;
	    String url = "jdbc:mysql://localhost:3306/";  
	    String dbName = "AI_Project";  
	    String driver = "com.mysql.jdbc.Driver";  
	    String userName = "root";  
	    String password = "";
		
	    try {
	    	Class.forName(driver).newInstance();
			conn = DriverManager  
	                .getConnection(url + dbName, userName, password);
			String query = "SELECT * from factsRHS ORDER by FactIndex";
			pst = conn.prepareStatement(query);
			rs = pst.executeQuery();
		    int temp = 0;
		    boolean flag =false;
		    String Formula = "";
		    while(rs.next()){
		    	
		    	int FactDerived = rs.getInt(1); // Fact in the RHS
		    	int FactinRule =rs.getInt(2); // Rule in which the fact appearedinthe RHS
		    	if(temp == FactDerived){
		    		Formula = Formula.substring(0, Formula.length()-1);
		    		Formula = Formula + "+";
		    	}
		    	else{
		    		if(flag){
		    			Formula = Formula.substring(0, Formula.length()-1);
		    			String TempQuery = "INSERT into FactDer VALUES (" + FactDerived + ",'" + Formula + "');";
			    		pst2 = conn.prepareStatement(TempQuery);
			    		pst2.execute();
			    		Formula = "";
		    		}
		    	}
		    	String newQuery = "SELECT * from ruleLHS WHERE RuleIndex=" + FactinRule + ";";
		    	pst2 = conn.prepareStatement(newQuery);
		    	rs1 = pst2.executeQuery();
		    	while(rs1.next()){
		    		int FactDependsUpon = rs.getInt(2);
		    		Formula = Formula + FactDependsUpon + ".";
		    		
		    	}
		    	temp = FactDerived;
		    	flag = true;
		    	
		    }
		    Formula = Formula.substring(0, Formula.length()-1);
		    String TempQuery = "INSERT into FactDer VALUES (" + temp + ",'" + Formula + "');";
			pst2 = conn.prepareStatement(TempQuery);
			pst2.execute();

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    	    
		// TODO Auto-generated method stub
		
	}

}
