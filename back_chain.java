package ai_project_final;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class back_chain extends JFrame{

	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	String rules[];
	JLabel rule_label[];
	
	public back_chain(String x)
	{
		super(x);
		setLayout(null);
	}
	
	/*void display(String[] arg,String[] type)
	{
		Set<String> mySet = new HashSet<String>(Arrays.asList(arg));
		String[] arg1 = mySet.toArray(new String[mySet.size()]);
		setSize(dim.width/2,dim.height-200);
		
		rules=new String[arg1.length];
		rule_label=new JLabel[arg1.length];
		int z=30;
		for(int i=0;i<arg1.length;i++)
		{
			System.out.println(arg1[i]);
			rule_label[i]= new JLabel(arg1[i]+" is TRUE and is "+type[i]+" fact");
			rule_label[i].setBounds(100,z,300,25);
			add(rule_label[i]);
			z=z+35;
		}
		
		setVisible(true);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	*/
	
	void display(String[] arg,String[] true_rule, String[] rule_no)
	{
		Set<String> mySet = new HashSet<String>(Arrays.asList(arg));
		String[] arg1 = mySet.toArray(new String[mySet.size()]);
		setSize(dim.width/2,dim.height-200);
		
		JLabel title=new JLabel();
		title= new JLabel("<HTML><U>True Facts<U><HTML>");
		title.setFont(new Font("Serif", Font.BOLD, 22));
		title.setBounds(100,20,300,25);
		add(title);
		
		rules=new String[arg1.length];
		rule_label=new JLabel[arg1.length];
		int z=50;
		for(int i=0;i<arg1.length;i++)
		{
			//System.out.println(arg1[i]);
			rule_label[i]= new JLabel(arg1[i]+" is TRUE");
			rule_label[i].setBounds(100,z,300,25);
			add(rule_label[i]);
			z=z+25;
		}
		
		
		Set<String> mySet1 = new HashSet<String>(Arrays.asList(true_rule));
		String[] arg2 = mySet1.toArray(new String[mySet1.size()]);
		
		JLabel title2=new JLabel();
		title2= new JLabel("<HTML><U>Derived Facts<U><HTML>");
		title2.setFont(new Font("Serif", Font.BOLD, 22));
		title2.setBounds(100,z+10,300,25);
		add(title2);
		
		rules=new String[arg2.length];
		rule_label=new JLabel[arg2.length];
		z=z+50;
		for(int i=1;i<arg2.length;i++)
		{
			System.out.println(arg2[i]);
			rule_label[i]= new JLabel(arg2[i]+" is TRUE");
			rule_label[i].setBounds(100,z,300,25);
			add(rule_label[i]);
			z=z+25;
		}
		
		JLabel title3=new JLabel();
		title3= new JLabel("<HTML><U>Rules Fired<U><HTML>");
		title3.setFont(new Font("Serif", Font.BOLD, 22));
		title3.setBounds(100,z+10,300,25);
		add(title3);
		
		rules=new String[rule_no.length];
		rule_label=new JLabel[rule_no.length];
		z=z+50;
		
		int ss=0;
		for(int i=0;i<rule_no.length;i++)
			if(rule_no[i]!=null)
				ss++;
			else
				break;
		
		for(int i=0;i<ss;i++)
		{
			System.out.println(rule_no[i]);
			rule_label[i]= new JLabel(rule_no[i]+"");
			rule_label[i].setBounds(100,z,300,25);
			add(rule_label[i]);
			z=z+25;
		}
		
		setVisible(true);
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
