package ai_project_final;

import java.sql.*;

public class Check_depFacts {
    String[] reqd_facts = new String[10];
    int m=0;
    
    public boolean getTruthValue(String[] FA, String[] IA, int rule_index){
        boolean value =false;
        Connection conn;  
        PreparedStatement pst;  
        ResultSet rs,rs1;
        String url = "jdbc:mysql://localhost:3306/";  
        String dbName = "ai_project";  
        String driver = "com.mysql.jdbc.Driver";  
        String userName = "root";  
        String password = "";
        try{
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            String query = "SELECT FactLHS FROM rulelhs WHERE RuleIndex=" + rule_index + ";" ; 
            //System.out.println("Rule No. : "+rule_index);
            pst = conn.prepareStatement(query);
            rs= pst.executeQuery();
           // System.out.println("Facts in LHS for rule " +rule_index+" : ");
           /* int i=0;
            while(rs.next()){
                System.out.println(rs.getInt(1));
                i++;
            }
            * 
            */
            
            
            
            while(rs.next()){
                  //System.out.println(rs.getInt(1));
                  String query1 = "SELECT Facts FROM factsinfo WHERE FactIndex=" + rs.getInt(1) + ";" ; 
                  pst = conn.prepareStatement(query1);
                  rs1= pst.executeQuery();
                  rs1.next();
                  String fact = rs1.getString(1).trim();
                  //System.out.println("fact : "+fact);
                  boolean tv1 = ExistInArray(fact,FA);
                  //System.out.println("tv1 : "+tv1);
                  boolean tv2=false;
                  if(!tv1){
                      tv2 = ExistInArray(fact,IA);
                     // System.out.println("tv2 : "+tv2);
                  }
                  if(tv1 | tv2){
                      value = true;
                  }
                  else{
                      reqd_facts[m++] = fact; 
                      value=false;
                      return value;
                  }
            }
            conn.close();
        }
        
        catch(Exception e){
        }
        //System.out.println("Final Value : "+value);
        return value;
    }
    
    public boolean ExistInArray(String fact, String[] array){
       // System.out.println("fact in EIA : "+fact);
       /* for(int l=0;l<array.length;l++){
                System.out.println("array in EIA : "+array[l]);
            }
            * 
            */
        for(int i=0;i<array.length;i++){
            if(fact.equals(array[i])){
                return true;
            }
        }
        return false;
    }
        
 public String[] getReqdFacts(){
       return reqd_facts;
   }
}
