package ai_project_final;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class choose_rule extends JFrame{
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	Connection conn = null;  
    PreparedStatement pst = null, pst2 =null, pst3=null;  
    ResultSet rs = null, rs1 = null,rs0=null,rs3=null;
    String url = "jdbc:mysql://localhost:3306/";  
    String dbName = "AI_Project";  
    String driver = "com.mysql.jdbc.Driver";  
    String userName = "root";  
    String password = "";
    String fact_number,fact,basic_derived;
    JCheckBox j[]=new JCheckBox[100];
    
    /*public static void main(String args[]) throws IOException
    {
    	choose_rule obj=new choose_rule();
    	obj.display();
    }*/
    
    
	public void display(int x)
	{
		setSize(dim.width/2,dim.height-200);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		if(x==0 || x==1 || x==2)
		{
		try
		{
		Class.forName(driver).newInstance();
		conn = DriverManager  
                .getConnection(url + dbName, userName, password);
		String query = "SELECT * from factsinfo";
		pst = conn.prepareStatement(query);
		rs= pst.executeQuery();
		}catch(Exception e)
		{
			System.out.print("Connection Error!");
		}
		}
		else
		{
			try
			{
			Class.forName(driver).newInstance();
			conn = DriverManager  
	                .getConnection(url + dbName, userName, password);
			String query = "SELECT * from ruleinfo";
			pst = conn.prepareStatement(query);
			rs= pst.executeQuery();
			}catch(Exception e)
			{
				System.out.print("Connection Error!");
			}
		}
		
		if(x==0 || x==1)
		{
		String[] columnNames = {"Name of Fact", "Fact","Basic/Derived","True/False"};
		DefaultTableModel model = new DefaultTableModel() {

			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return Boolean.class;
				default:
					return String.class;
				}
			}
		};
		model.addColumn("Name of Fact");
		model.addColumn("Fact");
		model.addColumn("Basic/Derived");
		model.addColumn("True/False");
		
		try
		{
			int i =0;
			while(rs.next())
			{
				fact_number = rs.getString("FactIndex"); 
				fact = rs.getString("Facts"); 
				basic_derived= rs.getString("BasicorDerived");
				j[i]=new JCheckBox();
				
				if(x==0 && basic_derived.equals("B"))
				{
					model.addRow(new Object[0]);
					//{fact_number,fact,basic_derived,j[i]}
					model.setValueAt(fact_number, i, 0);
					model.setValueAt(fact, i, 1);
					model.setValueAt(basic_derived, i, 2);
					model.setValueAt(false, i, 3);
					i++;
				}
				else if(x==1 && basic_derived.equals("D"))
				{
					model.addRow(new Object[0]);
					//{fact_number,fact,basic_derived,j[i]}
					model.setValueAt(fact_number, i, 0);
					model.setValueAt(fact, i, 1);
					model.setValueAt(basic_derived, i, 2);
					model.setValueAt(false, i, 3);
					i++;
				}
			}
						
			if(i <1)
			{
				JOptionPane.showMessageDialog(null, "No Record Found","Error",JOptionPane.ERROR_MESSAGE);
			}
			
			final JTable table = new JTable();
			table.setModel(model); 
			
			if(x==0)
			{
			// Get Row Selected
			JButton btnGetRowSelected = new JButton("Forward Chaining");
			btnGetRowSelected.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int j=0;
					int z=0;
					for (int i = 0; i < table.getRowCount(); i++) {
						Boolean chked = Boolean.valueOf(table.getValueAt(i, 3).toString());
						if(chked)
							z++;
					}
					
					String data[]=new String[z];
					for (int i = 0; i < table.getRowCount(); i++) {
						Boolean chked = Boolean.valueOf(table.getValueAt(i, 3).toString());
						String dataCol1 = table.getValueAt(i, 1).toString().trim();
						if (chked==true) {
							data[j++]=dataCol1;
							//JOptionPane.showMessageDialog(null, dataCol1);
						}
					}
					
				//CREATE CLASS FOR FORWARD CHAINING
					// data array has the strings
					
					frwd_chaining FC;
					 String rules[];
                    try {
                        FC = new frwd_chaining(data);
                        //System.out.println("XXXyyy");
                        FC.frwd();
                        //System.out.println("lnlln");
                        rules=FC.getInferenceArray();
                        for(int i=0;i<rules.length;i++)
                            System.out.println("i="+i+" "+rules[i]);
                        String rule_no[]=FC.getFiredRules();
                        for(int i=0;i<rule_no.length;i++)
                            System.out.println(rule_no[i]);
                        FC=null;
                        System.gc();
                        
                        back_chain ob=new back_chain("Forward Chaining");
                        ob.display(data,rules,rule_no);
                        
                    } catch (Exception ex) {
                        Logger.getLogger(choose_rule.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
				}
			});
			btnGetRowSelected.setBounds(this.getSize().width/2-100,this.getSize().height-100,200,30);
			//btnGetRowSelected.setBounds((this.getSize().width/2)-100,this.getSize().height-100,200,30);
			this.add(btnGetRowSelected);
			}
			else if(x==1)
			{
			// Get Row Selected
						JButton btnGetRowSelected1 = new JButton("Backward Chaining");
						btnGetRowSelected1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								int j=0;
								String data[]=new String[table.getRowCount()];
								for (int i = 0; i < table.getRowCount(); i++) {
									Boolean chked = Boolean.valueOf(table.getValueAt(i, 3).toString());
									String dataCol1 = table.getValueAt(i, 0).toString().trim();
									if (chked==true) {
										data[j++]=dataCol1;
										JOptionPane.showMessageDialog(null, dataCol1);
									}
								}
								
							//CREATE CLASS FOR FORWARD CHAINING
								// data array has the strings
									//System.out.print("backward");
								  BackwardChaining bc=new BackwardChaining();
								  Stack<String> FiredFacts=bc.getFacts(data[0]);
							}
							
						});
						btnGetRowSelected1.setBounds(this.getSize().width/2-100,this.getSize().height-100,200,30);
						this.add(btnGetRowSelected1);
			}
			
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.setFillsViewportHeight(true);
			JScrollPane scroll = new JScrollPane(table);
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
			scroll.setBounds(0,0,500,500);
			this.add(scroll);
			this.setVisible(true);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
		else if(x==2)
		{
			DefaultTableModel model = new DefaultTableModel() {

				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return String.class;
					case 1:
						return String.class;
					case 2:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			model.addColumn("Name of Fact");
			model.addColumn("Fact");
			model.addColumn("Basic/Derived");
			
			try
			{
				int i =0;
				while(rs.next())
				{
					fact_number = "F"+rs.getString("FactIndex"); 
					fact = rs.getString("Facts"); 
					basic_derived= rs.getString("BasicorDerived");
					j[i]=new JCheckBox();
					
					if(basic_derived.equals("B"))
					{
						model.addRow(new Object[0]);
						//{fact_number,fact,basic_derived,j[i]}
						model.setValueAt(fact_number, i, 0);
						model.setValueAt(fact, i, 1);
						model.setValueAt("Basic", i, 2);
						i++;
					}
					else if(basic_derived.equals("D"))
					{
						model.addRow(new Object[0]);
						//{fact_number,fact,basic_derived,j[i]}
						model.setValueAt(fact_number, i, 0);
						model.setValueAt(fact, i, 1);
						model.setValueAt("Derived", i, 2);
						i++;
					}
				}		
				if(i <1)
				{
					JOptionPane.showMessageDialog(null, "No Record Found","Error",JOptionPane.ERROR_MESSAGE);
				}
				
				final JTable table = new JTable();
				table.setModel(model); 
				
				// Get Row Selected
				JButton btnGetRowSelected = new JButton("Ok");
				btnGetRowSelected.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnGetRowSelected.setBounds(this.getSize().width/2-100,this.getSize().height-100,200,30);
				//btnGetRowSelected.setBounds((this.getSize().width/2)-100,this.getSize().height-100,200,30);
				this.add(btnGetRowSelected);
				
				
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				JScrollPane scroll = new JScrollPane(table);
				scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
				scroll.setBounds(0,0,500,500);
				this.add(scroll);
				this.setVisible(true);
			
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
		else if(x==3)
		{
			DefaultTableModel model = new DefaultTableModel() {

				public Class<?> getColumnClass(int column) {
					switch (column) {
					case 0:
						return String.class;
					case 1:
						return String.class;
					default:
						return String.class;
					}
				}
			};
			model.addColumn("Rule Index");
			model.addColumn("Rule");
			
			try
			{
				int i =0;
				while(rs.next())
				{
					fact_number = "F"+rs.getString("RuleIndex"); 
					fact = rs.getString("Rule"); 
					j[i]=new JCheckBox();
					
						model.addRow(new Object[0]);
						//{fact_number,fact,basic_derived,j[i]}
						model.setValueAt(fact_number, i, 0);
						model.setValueAt(fact, i, 1);
						i++;
				}		
				if(i <1)
				{
					JOptionPane.showMessageDialog(null, "No Record Found","Error",JOptionPane.ERROR_MESSAGE);
				}
				
				final JTable table = new JTable();
				table.setModel(model); 
				
				// Get Row Selected
				JButton btnGetRowSelected = new JButton("Ok");
				btnGetRowSelected.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnGetRowSelected.setBounds(this.getSize().width/2-100,this.getSize().height-100,200,30);
				//btnGetRowSelected.setBounds((this.getSize().width/2)-100,this.getSize().height-100,200,30);
				this.add(btnGetRowSelected);
				
				
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.setFillsViewportHeight(true);
				JScrollPane scroll = new JScrollPane(table);
				scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
				scroll.setBounds(0,0,500,500);
				this.add(scroll);
				this.setVisible(true);
			
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
			
		}
}
}
