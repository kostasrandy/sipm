package net.java.sip.communicator.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FriendSplash extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String CMD_CANCEL = "cmd.cancel";

	private String CMD_REMOVE = "cmd.remove";

	private String CMD_ADD = "cmd.add";

	JTextArea friends;
	JTextField friendControlTextField;
	JTextField relationControlTextField;
	JButton removeButton;
	JButton addButton;

	protected String toUser;
	protected String action;
	protected int relation = 0;
	

	protected void friendList(String friendlist) {
		System.out.println(friendlist);
		friends.setText(friendlist);
		friends.setEditable(false);
		removeButton.setEnabled(true);
	}

	public FriendSplash(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		pack();
	}

	private void initComponents() {
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());

		String title = "Friend lists";

		setTitle(title);
		setResizable(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dialogDone(CMD_CANCEL);
			}
		});

		// Accessibility -- all frames, dialogs, and applets should
		// have a description
		getAccessibleContext().setAccessibleDescription("Friendlist Splash");

		String authPromptLabelValue = "Please enter the contact you wish to add to your friend lists.";

		JLabel splashLabel = new JLabel(authPromptLabelValue);
		splashLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		splashLabel.setHorizontalAlignment(SwingConstants.CENTER);
		splashLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		contents.add(splashLabel, BorderLayout.NORTH);

		JPanel centerPane = new JPanel();
		centerPane.setLayout(new GridBagLayout());

		friendControlTextField = new JTextField(); // needed below
		friends = new JTextArea();// new JLabel();

		// user name label
		JLabel addfriendLabel = new JLabel();
		addfriendLabel.setDisplayedMnemonic('U');
		// setLabelFor() allows the mnemonic to work
		addfriendLabel.setLabelFor(friendControlTextField);
		
		int gridy = 0;
		JLabel friendListLabel = new JLabel();
		friendListLabel.setText("Contacts in lists:");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridy;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(12, 12, 0, 0);
		centerPane.add(friendListLabel, c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.insets = new Insets(12, 7, 0, 11);
		centerPane.add(friends, c);

		addfriendLabel.setText("Select contact:");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridy;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(12, 12, 0, 0);
		centerPane.add(addfriendLabel, c);
		// user name text
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = gridy++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.insets = new Insets(12, 7, 0, 11);
		centerPane.add(friendControlTextField, c);
		
		//realtion comboBox
		
		JLabel addrelationLabel = new JLabel("Select list to add to:");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = gridy;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(12, 12, 0, 0);
		centerPane.add(addrelationLabel, c);
		
		String[] relationStrings = { "Friends", "Family" };

		//Create the combo box, select item at index 3.
		//Indices start at 0, so 3 specifies the pig.
		JComboBox relationsList = new JComboBox(relationStrings);
		relationsList.setSelectedIndex(0);
		relationsList.addActionListener(this);
		

		c.gridx = 1;
		c.gridy = gridy++;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(12, 12, 0, 0);
		centerPane.add(relationsList, c);
		relationControlTextField = new JTextField();
		
		// Buttons along bottom of window
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
		// register
		buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		addButton = new JButton();
		addButton.setEnabled(true);
		addButton.setText("Add");
		addButton.setActionCommand(CMD_ADD);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dialogDone(event);
			}
		});
		buttonPanel.add(addButton);

		removeButton = new JButton();
		removeButton.setEnabled(false);
		removeButton.setText("Remove from lists");
		removeButton.setActionCommand(CMD_REMOVE);
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dialogDone(event);
			}
		});
		buttonPanel.add(removeButton);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		c.insets = new Insets(11, 12, 20, 11);

		centerPane.add(buttonPanel, c);

		contents.add(centerPane, BorderLayout.CENTER);
		getRootPane().setDefaultButton(removeButton);

		// setFocusTraversalPolicy(new FocusTraversalPol());

	} // initComponents()

	/**
	 * The user has selected an option. Here we close and dispose the dialog. If
	 * actionCommand is an ActionEvent, getCommandString() is called, otherwise
	 * toString() is used to get the action command.
	 * 
	 * @param actionCommand
	 *            may be null
	 */
	private void dialogDone(Object actionCommand) {
		String cmd = null;
		if (actionCommand != null) {
			if (actionCommand instanceof ActionEvent) {
				cmd = ((ActionEvent) actionCommand).getActionCommand();
			} else {
				cmd = actionCommand.toString();
			}
		}
		if (cmd == null) {
			// do nothing
		} else if (cmd.equals(CMD_CANCEL)) {
			toUser = null;
		} else if (cmd.equals(CMD_REMOVE)) {
			toUser = friendControlTextField.getText();
			action = "remove";
			//relation = relationControlTextField.getText();
		} else if (cmd.equals(CMD_ADD)) {
			toUser = friendControlTextField.getText();
			action = "add";
			//relation = relationControlTextField.getText();
		}
		setVisible(false);
		dispose();
	} // dialogDone()

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox)e.getSource();
		this.relation = cb.getSelectedIndex();
	}

}
