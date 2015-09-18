import javax.swing.*;

import info.clearthought.layout.*;
/*
 * Created by JFormDesigner on Sat Mar 07 18:31:32 EET 2015
 */

/**
 * @author sergey efimov
 */
public class forms extends JPanel
{
	public forms()
	{
		initComponents();
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - sergey efimov
		label1 = new JLabel();
		textField2 = new JTextField();
		textField1 = new JTextField();
		comboBox1 = new JComboBox();
		scrollPane1 = new JScrollPane();
		editorPane1 = new JEditorPane();
		toggleButton1 = new JToggleButton();
		radioButton1 = new JRadioButton();
		scrollPane2 = new JScrollPane();
		table1 = new JTable();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(
			new javax.swing.border.CompoundBorder(
				new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
					"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
					javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog",
					java.awt.Font.BOLD, 12), java.awt.Color.red), getBorder()));
		addPropertyChangeListener(
			new java.beans.PropertyChangeListener()
			{
				public void propertyChange(java.beans.PropertyChangeEvent e)
				{
					if("border".equals(e.getPropertyName())) { throw new RuntimeException(); }
				}
			});

		setLayout(
			new TableLayout(new double[][] {{TableLayout.PREFERRED, TableLayout.PREFERRED, 63,
				TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
				TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
				TableLayout.PREFERRED, TableLayout.PREFERRED}, {TableLayout.PREFERRED,
				TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
				TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
				TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
				TableLayout.PREFERRED}}));
		((TableLayout) getLayout()).setHGap(5);
		((TableLayout) getLayout()).setVGap(5);

		//---- label1 ----
		label1.setText("text");
		add(label1, new TableLayoutConstraints(2, 0, 2, 0, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));
		add(textField2, new TableLayoutConstraints(2, 2, 2, 2, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));
		add(textField1, new TableLayoutConstraints(7, 2, 7, 2, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));
		add(comboBox1, new TableLayoutConstraints(10, 2, 10, 2, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(editorPane1);
		}
		add(scrollPane1, new TableLayoutConstraints(2, 7, 2, 7, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));

		//---- toggleButton1 ----
		toggleButton1.setText("text");
		add(toggleButton1, new TableLayoutConstraints(5, 7, 5, 7, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));

		//---- radioButton1 ----
		radioButton1.setText("text");
		add(radioButton1, new TableLayoutConstraints(2, 9, 2, 9, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));

		//======== scrollPane2 ========
		{
			scrollPane2.setViewportView(table1);
		}
		add(scrollPane2, new TableLayoutConstraints(8, 10, 8, 10, TableLayoutConstraints.FULL,
				TableLayoutConstraints.FULL));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - sergey efimov
	private JLabel label1;
	private JTextField textField2;
	private JTextField textField1;
	private JComboBox comboBox1;
	private JScrollPane scrollPane1;
	private JEditorPane editorPane1;
	private JToggleButton toggleButton1;
	private JRadioButton radioButton1;
	private JScrollPane scrollPane2;
	private JTable table1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
