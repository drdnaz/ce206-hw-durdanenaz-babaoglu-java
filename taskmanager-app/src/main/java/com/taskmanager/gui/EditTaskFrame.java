package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.taskmanager.model.Task;

/**
 * Frame for editing tasks
 */
public class EditTaskFrame extends JFrame {
	private JTable taskTable;
	private DefaultTableModel tableModel;
	private JButton editButton;
	private JButton closeButton;
	private JButton refreshButton;
	
	// Edit form components
	private JTextField taskTitleField;
	private JTextArea taskDescriptionArea;
	private JSpinner dateSpinner;
	private JComboBox<String> categoryComboBox;
	private JComboBox<String> priorityComboBox;
	private JComboBox<String> statusComboBox;
	private JPanel editPanel;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	
	// Currently edited task ID
	private int currentTaskId = -1;
	
	// UI Color scheme
	private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
	private final Color ACCENT_COLOR = Color.PINK;
	private final Color TEXT_COLOR = new Color(180, 200, 255);
	private final Color FIELD_TEXT_COLOR = new Color(80, 80, 80);
	
	private final MainMenuFrame parentFrame;
	
	/**
	 * Creates a new EditTaskFrame
	 * @param parentFrame The parent MainMenuFrame
	 */
	public EditTaskFrame(MainMenuFrame parentFrame) {
		this.parentFrame = parentFrame;
		initComponents();
		loadTaskData();
	}
	
	/**
	 * Initialize components
	 */
	private void initComponents() {
		setTitle("Edit Tasks");
		 setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		
		// Main panel with card layout to switch between list and edit views
		cardPanel = new JPanel();
		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);
		setContentPane(cardPanel);
		
		// Create list panel
		JPanel listPanel = createListPanel();
		cardPanel.add(listPanel, "LIST");
		
		// Create edit panel
		editPanel = createEditPanel();
		cardPanel.add(editPanel, "EDIT");
		
		// Start with list view
		cardLayout.show(cardPanel, "LIST");
	}
	
	/**
	 * Creates the list panel with task table
	 * @return The list panel
	 */
	private JPanel createListPanel() {
		JPanel listPanel = new JPanel(new BorderLayout(10, 10));
		listPanel.setBackground(BACKGROUND_COLOR);
		listPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// Title label
		JLabel titleLabel = new JLabel("Edit Tasks");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		
		// Instruction label
		JLabel instructionLabel = new JLabel("Select a task and click 'Edit' to modify it");
		instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		instructionLabel.setForeground(TEXT_COLOR);
		
		// Header panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);
		headerPanel.add(titleLabel, BorderLayout.WEST);
		headerPanel.add(instructionLabel, BorderLayout.EAST);
		listPanel.add(headerPanel, BorderLayout.NORTH);
		
		// Table panel
		JPanel tablePanel = createTablePanel();
		listPanel.add(tablePanel, BorderLayout.CENTER);
		
		// Button panel
		JPanel buttonPanel = createButtonPanel();
		listPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		return listPanel;
	}
	
	/**
	 * Creates the table panel with task list
	 * @return The table panel
	 */
	private JPanel createTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		
		// Create columns for the table
		String[] columns = {"ID", "Title", "Due Date", "Category", "Priority", "Status"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make table non-editable
			}
		};
		
		taskTable = new JTable(tableModel);
		taskTable.setFillsViewportHeight(true);
		taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskTable.setRowHeight(25);
		taskTable.setGridColor(new Color(150, 150, 150));
		taskTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		
		// Create a scroll pane for the table
		JScrollPane scrollPane = new JScrollPane(taskTable);
		scrollPane.setBorder(new LineBorder(ACCENT_COLOR, 3, true));
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		
		return tablePanel;
	}
	
	/**
	 * Creates the button panel for the list view
	 * @return The button panel
	 */
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setOpaque(false);
		
		// Edit button
		editButton = new JButton("Edit Selected Task");
		styleButton(editButton);
		buttonPanel.add(editButton);
		
		// Refresh button
		refreshButton = new JButton("Refresh");
		styleButton(refreshButton);
		buttonPanel.add(refreshButton);
		
		// Close button
		closeButton = new JButton("Close");
		styleButton(closeButton);
		buttonPanel.add(closeButton);
		
		// Add action listeners to buttons
		addButtonActions();
		
		return buttonPanel;
	}
	
	/**
	 * Creates the edit panel for editing tasks
	 * @return The edit panel
	 */
	private JPanel createEditPanel() {
		JPanel editPanel = new JPanel(new BorderLayout(10, 10));
		editPanel.setBackground(BACKGROUND_COLOR);
		editPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// Edit form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);
		
		// Title label for edit panel
		JLabel editTitleLabel = new JLabel("Edit Task");
		editTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		editTitleLabel.setForeground(Color.WHITE);
		editPanel.add(editTitleLabel, BorderLayout.NORTH);
		
		// Add form fields
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		// Task Title Label
		JLabel taskTitleLabel = new JLabel("Task Title:");
		taskTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		taskTitleLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		formPanel.add(taskTitleLabel, gbc);
		
		// Task Title Field
		taskTitleField = new JTextField(20);
		taskTitleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		taskTitleField.setForeground(FIELD_TEXT_COLOR);
		taskTitleField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		formPanel.add(taskTitleField, gbc);
		
		// Task Description Label
		JLabel taskDescLabel = new JLabel("Description:");
		taskDescLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		taskDescLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		formPanel.add(taskDescLabel, gbc);
		
		// Task Description Area
		taskDescriptionArea = new JTextArea(5, 20);
		taskDescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		taskDescriptionArea.setForeground(FIELD_TEXT_COLOR);
		taskDescriptionArea.setLineWrap(true);
		taskDescriptionArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(taskDescriptionArea);
		scrollPane.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		formPanel.add(scrollPane, gbc);
		
		// Due Date Label
		JLabel dueDateLabel = new JLabel("Due Date:");
		dueDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		dueDateLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		formPanel.add(dueDateLabel, gbc);
		
		// Date Spinner
		SpinnerDateModel dateModel = new SpinnerDateModel();
		dateModel.setCalendarField(Calendar.DAY_OF_MONTH);
		dateSpinner = new JSpinner(dateModel);
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
		dateSpinner.setEditor(dateEditor);
		dateSpinner.setValue(new Date());
		dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		dateSpinner.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		formPanel.add(dateSpinner, gbc);
		
		// Category Label
		JLabel categoryLabel = new JLabel("Category:");
		categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		categoryLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		formPanel.add(categoryLabel, gbc);
		
		// Category Combo Box
		String[] categories = {"Work", "Personal", "Study", "Health", "Other"};
		categoryComboBox = new JComboBox<>(categories);
		categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		categoryComboBox.setForeground(FIELD_TEXT_COLOR);
		categoryComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		formPanel.add(categoryComboBox, gbc);
		
		// Priority Label
		JLabel priorityLabel = new JLabel("Priority:");
		priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		priorityLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 4;
		formPanel.add(priorityLabel, gbc);
		
		// Priority Combo Box
		String[] priorities = {"Low", "Medium", "High", "Urgent"};
		priorityComboBox = new JComboBox<>(priorities);
		priorityComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		priorityComboBox.setForeground(FIELD_TEXT_COLOR);
		priorityComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 4;
		formPanel.add(priorityComboBox, gbc);
		
		// Status Label
		JLabel statusLabel = new JLabel("Status:");
		statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		statusLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 2;
		gbc.gridy = 3;
		formPanel.add(statusLabel, gbc);
		
		// Status Combo Box
		statusComboBox = new JComboBox<>(Task.getStatusOptions());
		statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		statusComboBox.setForeground(FIELD_TEXT_COLOR);
		statusComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 2;
		gbc.gridy = 4;
		formPanel.add(statusComboBox, gbc);
		
		editPanel.add(formPanel, BorderLayout.CENTER);
		
		// Button panel for edit form
		JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		editButtonPanel.setOpaque(false);
		
		JButton saveButton = new JButton("Save Changes");
		styleButton(saveButton);
		editButtonPanel.add(saveButton);
		
		JButton cancelEditButton = new JButton("Cancel");
		styleButton(cancelEditButton);
		editButtonPanel.add(cancelEditButton);
		
		// Save button action
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveTaskChanges();
			}
		});
		
		// Cancel button action
		cancelEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelEdit();
			}
		});
		
		editPanel.add(editButtonPanel, BorderLayout.SOUTH);
		
		return editPanel;
	}
	
	/**
	 * Style button with consistent look and feel
	 * @param button Button to style
	 */
	private void styleButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setBackground(Color.WHITE);
		button.setForeground(new Color(80, 80, 80));
		button.setFocusPainted(false);
		button.setBorder(new LineBorder(ACCENT_COLOR, 3, true));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(180, 40));
	}
	
	/**
	 * Add action listeners to buttons
	 */
	private void addButtonActions() {
		// Edit button action
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelectedTask();
			}
		});
		
		// Refresh button action
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadTaskData();
			}
		});
		
		// Close button action
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	/**
	 * Load task data into the table
	 */
	private void loadTaskData() {
		// Clear existing data
		tableModel.setRowCount(0);
		
		// Format for displaying dates
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		// Load tasks from the Task model's static list
		for (Task task : Task.getAllTasks()) {
			Vector<Object> row = new Vector<>();
			row.add(task.getId());
			row.add(task.getTitle());
			row.add(dateFormat.format(task.getDueDate()));
			row.add(task.getCategory());
			row.add(task.getPriority());
			row.add(task.getStatus());
			tableModel.addRow(row);
		}
		
		// If no tasks were found, show a message
		if (tableModel.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this,
				"No tasks found. Add some tasks first!",
				"No Tasks",
				JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * Edit the selected task
	 */
	private void editSelectedTask() {
		int selectedRow = taskTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this,
				"Please select a task to edit.",
				"No Task Selected",
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Get the task ID from the selected row
		int taskId = (int) tableModel.getValueAt(selectedRow, 0);
		
		// Find the task in the model
		Task task = Task.findTaskById(taskId);
		if (task == null) {
			JOptionPane.showMessageDialog(this,
				"Error: Task not found.",
				"Error",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Save current task ID
		currentTaskId = taskId;
		
		// Populate the edit form with task data
		taskTitleField.setText(task.getTitle());
		taskDescriptionArea.setText(task.getDescription());
		dateSpinner.setValue(task.getDueDate());
		
		// Set category
		for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
			if (categoryComboBox.getItemAt(i).equals(task.getCategory())) {
				categoryComboBox.setSelectedIndex(i);
				break;
			}
		}
		
		// Set priority
		for (int i = 0; i < priorityComboBox.getItemCount(); i++) {
			if (priorityComboBox.getItemAt(i).equals(task.getPriority())) {
				priorityComboBox.setSelectedIndex(i);
				break;
			}
		}
		
		// Set status
		for (int i = 0; i < statusComboBox.getItemCount(); i++) {
			if (statusComboBox.getItemAt(i).equals(task.getStatus())) {
				statusComboBox.setSelectedIndex(i);
				break;
			}
		}
		
		// Show edit panel
		cardLayout.show(cardPanel, "EDIT");
	}
	
	/**
	 * Save task changes
	 */
	private void saveTaskChanges() {
		if (currentTaskId == -1) {
			JOptionPane.showMessageDialog(this,
				"No task selected for editing.",
				"Error",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Validate form
		if (taskTitleField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Please enter a task title.",
				"Validation Error",
				JOptionPane.ERROR_MESSAGE);
			taskTitleField.requestFocus();
			return;
		}
		
		// Get form data
		String title = taskTitleField.getText().trim();
		String description = taskDescriptionArea.getText().trim();
		Date dueDate = (Date) dateSpinner.getValue();
		String category = (String) categoryComboBox.getSelectedItem();
		String priority = (String) priorityComboBox.getSelectedItem();
		String status = (String) statusComboBox.getSelectedItem();
		
		// Update task
		boolean updated = Task.updateTask(currentTaskId, title, description, dueDate, 
										 category, priority, status);
		
		if (updated) {
			JOptionPane.showMessageDialog(this,
				"Task updated successfully.",
				"Success",
				JOptionPane.INFORMATION_MESSAGE);
			
			// Return to list view and refresh data
			cardLayout.show(cardPanel, "LIST");
			loadTaskData();
			currentTaskId = -1;
		} else {
			JOptionPane.showMessageDialog(this,
				"Error updating task. Task not found.",
				"Error",
				JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancel editing and return to list view
	 */
	private void cancelEdit() {
		int confirm = JOptionPane.showConfirmDialog(this,
			"Discard changes and return to task list?",
			"Confirm Cancel",
			JOptionPane.YES_NO_OPTION);
			
		if (confirm == JOptionPane.YES_OPTION) {
			cardLayout.show(cardPanel, "LIST");
			currentTaskId = -1;
		}
	}

	public JTable getTaskTable() {
		return taskTable;
	}
}
