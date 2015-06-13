package ai_project_final;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class frwd_chaining implements Closeable{
    
    String[] Facts_array;
    
    public frwd_chaining(String[] FA)throws SQLException{
        System.out.println(" *** No of given facts : "+FA.length);
        Facts_array = new String[FA.length];
        System.arraycopy(FA, 0, Facts_array, 0, FA.length);
        
    }
    
    Connection conn = null;  
    String url = "jdbc:mysql://localhost:3306/";  
    String dbName = "ai_project";  
    String driver = "com.mysql.jdbc.Driver";  
    String userName = "root";  
    String password = "";
    public static String[] Inference_array = new String[20];
        
    public static int[] Fired_rules = new int[20];
    static int m=0,n=0;
        
    public void frwd() throws SQLException{
       
        int j=0;
             
        PreparedStatement pst=null;  
        ResultSet rs=null,rs1=null; 
        try{
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
            
            for(int i=0; i<Facts_array.length;i++){
                String query = "SELECT FactIndex,BasicorDerived FROM factsinfo WHERE Facts=\"" + Facts_array[i] + "\";" ; 
                pst = conn.prepareStatement(query);
                rs= pst.executeQuery();
               // for(int l=0;l<Facts_array.length;l++){ System.out.println(Facts_array[l]);}
                rs.next();
                //System.out.println("This is rs" + rs.getString(1).trim());
                
                if(rs.getString(2).trim().equals("D")){
                    System.out.println(" Error!! Derived Fact given!!");
                    return;
                }
                
                String query1 = "SELECT FactInLHS FROM factslhs WHERE FactIndex=" + rs.getInt(1) + ";" ; 
                pst = conn.prepareStatement(query1);
                rs1= pst.executeQuery();
                
                
                while(rs1.next()){
                     
                    //System.out.println("This is rs1" + rs1.getString(1));
                    int ruleIndex = rs1.getInt(1);
                    if(!IsFired(ruleIndex,Fired_rules)){
                         //System.out.println("Process FRWDCH on : "+ruleIndex);
                        Process_forward_chain(Facts_array,ruleIndex,Fired_rules);
                        //System.out.println("Process FRWDCH done on : "+ruleIndex);
                    }
                }
            }
        }
        catch (Exception e) {
	}
        int p =0;
        System.out.println("PRINTING INFERENCES : ");
        
        while(Inference_array[p] != null){
            System.out.println(Inference_array[p++]);
        }
        
        conn.close();
      }
    
    public void cleanup()
    {
    	java.util.Arrays.fill(Inference_array, null);
        java.util.Arrays.fill(Fired_rules, 0);
        
        /*while(Inference_array[p] != null){
             System.out.println("XXXXX"+Inference_array[p++]);
         }
         * 
         */

        m=0;
        n=0;
    }
    
    static boolean IsFired(int ri, int[] ra){
        for(int i=0;i<ra.length;i++){
            if(ra[i] == ri){
                return true;
            }
                
        }
        return false;
    }
    
    void Process_forward_chain(String[] FA, int ri,int[] FR){
        PreparedStatement pst,pst1,pst2;  
        ResultSet rs=null,rs1=null,rs2=null;
        int i=1;
        
        //System.out.println("**");
        
        Check_depFacts obj = new Check_depFacts();
        boolean t = obj.getTruthValue(Facts_array,Inference_array,ri);
        
        if(t){
            if(!IsFired(ri,FR)){
                
                
                try{
                    String query = "SELECT DerivedFact FROM ruleinfo WHERE RuleIndex=" + ri + ";" ; 
                    pst = conn.prepareStatement(query);
                    rs= pst.executeQuery();
                    rs.next();
                    int fi =rs.getInt(1);
                    String query1 ="SELECT Facts FROM factsinfo WHERE FactIndex = "+ fi +";";
                    pst1 = conn.prepareStatement(query1);
                    rs1= pst1.executeQuery();
                    rs1.next();
                    Inference_array[m++]=rs1.getString(1).trim();
                    Fired_rules[n++]=ri;
                    String query2 ="SELECT RuleTo FROM ruleleadingto WHERE RuleIndex= "+ ri +";";
                    pst2 = conn.prepareStatement(query2);
                    rs2= pst2.executeQuery();
                    while(rs2.next()){
                        Process_forward_chain(Facts_array,rs2.getInt(1),Fired_rules);
                    }
                }
                catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static String[] getInferenceArray(){
        return Inference_array;
    }
    
    public String[] getFiredRules(){
    	String x[]=new String[Fired_rules.length];
    	try{
	    	Class.forName(driver).newInstance();
	        conn = DriverManager.getConnection(url + dbName, userName, password);
	    	PreparedStatement pst=null;  
	        ResultSet rs=null; 
	        for(int i=0; i<Fired_rules.length;i++){
	    	String query = "SELECT Rule FROM ruleinfo WHERE RuleIndex=" + Fired_rules[i] + ";" ; 
	        pst = conn.prepareStatement(query);
	        rs= pst.executeQuery();
	       // for(int l=0;l<Facts_array.length;l++){ System.out.println(Facts_array[l]);}
	        rs.next();
	        x[i]=rs.getString(1);
        }
    	}catch(Exception e)
    	{
    		
    	}
        return x;
    }
}
