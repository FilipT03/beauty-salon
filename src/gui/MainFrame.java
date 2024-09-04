package gui;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import app.Application;
import gui.userpanels.BeauticianPanel;
import gui.userpanels.ClientPanel;
import gui.userpanels.ManagerPanel;
import gui.userpanels.ReceptionistPanel;
import gui.userpanels.UserPanel;
import users.Beautician;
import users.Client;
import users.Manager;
import users.Receptionist;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 3521750995772681610L;
	
	protected CardLayout layout;
	protected JPanel panelContainer;
	protected UserPanel up = null;

	private static MainFrame instance;
	public static MainFrame getInstance() {
		if (instance == null)
			instance = new MainFrame();
		return instance;
	}
	
	public static void createInstance() {
		if (instance == null)
			instance = new MainFrame();
	}
	public static void deleteInstance() {
		instance.setVisible(false);
		instance.dispose();
		instance = null;
	}
	
	private MainFrame() {
		this.setTitle("Kozmetički salon");
		this.setSize(800, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		layout = new CardLayout();
		panelContainer = new JPanel(layout);
		
		if (Application.currentUser instanceof Client)
			up = new ClientPanel();
		else if (Application.currentUser instanceof Beautician)
			up = new BeauticianPanel();
		else if (Application.currentUser instanceof Receptionist)
			up = new ReceptionistPanel();
		else if (Application.currentUser instanceof Manager)
			up = new ManagerPanel();
		
		panelContainer.add(up, PanelName.USER_PANEL.toString());

		up.addPanels(panelContainer);
		
		this.add(panelContainer);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Application.saveAllData();
			}
		});
		
		this.setVisible(true);
	}
	public void switchPanel(PanelName name) {
		if (name == PanelName.USER_PANEL)
			up.updateData();
		layout.show(panelContainer, name.toString());
	}
	public void switchPanel(String name) {
		layout.show(panelContainer, name);
	}
	public void home() {
		switchPanel(PanelName.USER_PANEL);
	}
}
