package gui.otherdialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MainFrame;
import gui.otherpanels.TypePanel;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentType;

public class TypeAddEditDialog extends JDialog {
	private static final long serialVersionUID = -780596202714142314L;
	private TreatmentType editT;
	private JPanel parent;
	
	public TypeAddEditDialog(TreatmentType type, JPanel parent) {
		super(MainFrame.getInstance(), true);
		this.parent = parent;
		setTitle("Izmena tipa");
		this.editT = type;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		initGUI();
		pack();
	}

	private void initGUI() {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]");
		setLayout(ml);
		
		JTextField name;
		
		if (editT != null) {
			name = new JTextField(editT.name, 14);
		}
		else {
			name = new JTextField(14);
		}

		add(new JLabel("Ime"));
		add(name, "span 2");
		
		add(new JLabel());

		JButton btnCancel = new JButton("Odustani");
		add(btnCancel);

		JButton btnOK = new JButton("OK");
		add(btnOK);

		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreatmentType t;
				if (name.getText().equals("") || name.getText() == null) {
					JOptionPane.showMessageDialog(null, "Ime ne sme biti prazno!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				t = new TreatmentType(name.getText());
				if (editT != null) {
					t.setDeleted(editT.isDeleted());
					if (TreatmentManager.getInstance().getTreatmentType(name.getText()) == null)
						TreatmentManager.getInstance().updateTreatmentType(editT, t);
					else {
						JOptionPane.showMessageDialog(null, "Tip sa tim imenom već postoji!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
				}
				else {
					t.setDeleted(false);
					TreatmentManager.getInstance().addTreatmentType(t);		
				}
				((TypePanel) parent).refreshData();
				TypeAddEditDialog.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TypeAddEditDialog.this.dispose();
			}
		});
	}

}
