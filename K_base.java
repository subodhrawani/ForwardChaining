package ai_project_final;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.swing.*;
import java.awt.Font;

public class K_base extends JFrame implements ActionListener, MouseListener{
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	JLabel title;
	JLabel kb_name_label, rules_heading;
	//JTextField kb_name, rule_number;
	JButton save,check_rules,check_facts, cancel;
	public static int countOf = 0;
	JLabel domain_expert_name;
	JComboBox domain_expert_list;
	String[] domain_experts= { "Ashish Soni", "Shirish Kumar", "Suman Saurabh","Utkarsh jain" };
	
	JLabel rule_list,rule_list1;
	JButton add_rule,sub_rule;
	JComboBox rule_number_list;
	String[] number_of_rules= {"1","2","3","4","5","6","7","8","9","10"};
	JTextField[] rules=rules= new JTextField[10];
	JLabel[] rule_name= new JLabel[10];
	static int j=0,z=110;
	
	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem menuItem;
	
	private boolean checkError(String str){
		String[] result = str.split("\\s");
		if (!(result[0].equals("IF"))){
			return false;
		}
		else if(result[1].equals("AND") || result[1].equals("OR") || result[1].equals("THEN")){
			return false;
		}
		int k =2;
		boolean complete = false;
		while(k<result.length){
			if(result[k].equals("THEN")){
				complete = true;
				//System.out.println("Intothe loop");
			}
			if(result[k].equals("AND") || result[k].equals("OR") || result[k].equals("THEN")){
				if(result[k-1].equals("AND") || result[k-1].equals("OR") || result[k-1].equals("THEN")){
					return false;
				}
				
			}
			k++;
		}
		return complete;

	}
	
	public K_base(String x)
	{
		super(x);
		setLayout(null);
	}

	public static void main(String[] args) throws IOException
	{
		K_base ob=new K_base("Knowledge Base");
		ob.gui(900,900);
	}

	public void gui(int h, int w)
	{
		setSize(dim.width/2,dim.height-200);
		
		//MenuBar
		menuBar = new JMenuBar();
		menu = new JMenu("About");
		menuBar.add(menu);
		menuItem= new JMenuItem("About");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		setJMenuBar(menuBar);

		kb_name_label= new JLabel("<HTML><U>Forward and Backward Chaining Engine<U><HTML>");
		kb_name_label.setFont(new Font("Serif", Font.BOLD, 22));
		kb_name_label.setBounds(this.getSize().width/2-200,10,400,30);
		add(kb_name_label);
		
		//Knowledge base name
		kb_name_label= new JLabel("Name of Knowledge Base: Generic Engine");
		kb_name_label.setFont(new Font("Serif", Font.PLAIN, 16));
		kb_name_label.setBounds(25,50,300,25);
		add(kb_name_label);

		//kb_name= new JTextField();
		//kb_name.setBounds(175,25,150,25);	
		//add(kb_name);

		// Domain expert name
		domain_expert_name= new JLabel("Name of Domain Expert:");
		domain_expert_name.setBounds(25+this.getSize().width/2,50,300,25);
		add(domain_expert_name);

		domain_expert_list = new JComboBox(domain_experts);
		domain_expert_list.setBounds(175+this.getSize().width/2,50,100,25);
		add(domain_expert_list);

		//Rule Heading
		rules_heading= new JLabel("Rules:");
		rules_heading.setBounds(25,90,300,25);
		rules_heading.setVisible(false);
		add(rules_heading);
		
		//Rule number list
		rule_list= new JLabel("Add Rules:");
		rule_list.setBounds(25+this.getSize().width/2,100,300,25);
		add(rule_list);
		
		add_rule=new JButton("+");
		add_rule.addMouseListener(this);
		add_rule.setBounds(175+this.getSize().width/2,100,50,25);
		add(add_rule);
		
		rule_list1= new JLabel("Delete Rules:");
		rule_list1.setBounds(25+this.getSize().width/2,150,300,25);
		add(rule_list1);
		
		sub_rule=new JButton("-");
		sub_rule.addMouseListener(this);
		sub_rule.setBounds(175+this.getSize().width/2,150,50,25);
		add(sub_rule);
		
		/*rule_number_list= new JComboBox(number_of_rules);
		rule_number_list.addActionListener(this);
		rule_number_list.addMouseListener(this);
		rule_number_list.setBounds(175+this.getSize().width/2,75,50,25);
		add(rule_number_list);*/
		
		// Save and cancel buttons
		save= new JButton("Save");
		save.addActionListener(this);
		save.setBounds(50,this.getSize().height-100,100,30);
		add(save);
		
		check_rules= new JButton("See Facts");
		check_rules.addActionListener(this);
		check_rules.setBounds(200,this.getSize().height-100,100,30);
		add(check_rules);
		
		check_facts= new JButton("See Rules");
		check_facts.addActionListener(this);
		check_facts.setBounds(350,this.getSize().height-100,100,30);
		add(check_facts);

		cancel= new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setBounds(150+this.getSize().width/2,this.getSize().height-100,100,30);
		add(cancel);

		setVisible(true);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==save)
		{
			NewRules instance = new NewRules();
			String[] response = new String[j];
			String[] rules_fetch = new String[j];
			boolean[] Error = new boolean[j];
			// Get all fields and Save to database.
			for(int i=0; i < j; i++){
				rules_fetch[i] = rules[i].getText();
				Error[i] = checkError(rules_fetch[i]);
				System.out.println(Error[i]);
				if(!Error[i]){
					// Use Joption panel
					System.out.println("There is an error in syntax of rule" + i);
				}
				else{
					
					response[i] = instance.EnterRule(rules_fetch[i]);	
				}
				}
				
			//Custom button text
			Object[] options = {"Do chaining","Add more rules"};
			int n = JOptionPane.showOptionDialog(this,"Would you like to do chaining or add more rules?","A Silly Question",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);

			if(n==0)
			{
				Object[] options1 = {"Forward Chaining","Backward Chaining"};
				int n1 = JOptionPane.showOptionDialog(this,"Would you like to do chaining or add more rules?","A Silly Question",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options1,options1[1]);

				choose_rule ch=new choose_rule();
				ch.display(n1);
			}
				
		}
		
		if(ae.getSource()==check_rules)
		{
			choose_rule ch=new choose_rule();
			ch.display(2);
		}
		
		if(ae.getSource()==check_facts)
		{
			choose_rule ch=new choose_rule();
			ch.display(3);
		}
		
		if(ae.getSource()==cancel)
		{
			//kb_name.setText("");
			for(int i=0;i<j;i++)
				rules[i].setText("");	
		}
		
		if(ae.getSource()==menuItem)
		{
			JOptionPane.showMessageDialog(this," Made by:-\n Ashish Soni\n Shirish Kumar\n Suman Saurabh\n Utkarsh jain");
		}
	}

	public void mousePressed(MouseEvent e)
	{
			
	}

	public void mouseReleased(MouseEvent e)
	{

	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
    {
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getSource()==add_rule)
		{
			rules_heading.setVisible(true);
			if(j<=9)
			{
				rule_name[j]= new JLabel("R"+(j+1));
				rule_name[j].setBounds(25,z,50,20);
				this.add(rule_name[j]);
				//System.out.print(j);
				rules[j]=new JTextField();
				rules[j].setBounds(75,z,250,20);
				
				this.add(rules[j]);
				System.out.print(rules[j].getText());
				z=z+35;
				j++;
				repaint();
			}
			else
			{
				JOptionPane.showMessageDialog(this,
				"No more rules can be added!",
				"Warning",
				JOptionPane.WARNING_MESSAGE);
			}
		}
		
		if(e.getSource()==sub_rule)
		{
			if(j>0)
			{
				rule_name[j-1].setText("");
				rule_name[j-1].setVisible(false);
				//System.out.print(j);
				rules[j-1].setText("");
				rules[j-1].setVisible(false);
				z=z-35;
				j--;
				repaint();
			}
			else
			{
				JOptionPane.showMessageDialog(this,
				"No more rules can be deleted!",
				"Warning",
				JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
