package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.taskmanager.model.Task;

/**
 * Frame for deleting tasks
 */
public class DeleteTaskFrame extends JFrame {
	private JTable taskTable;
	private DefaultTableModel tableModel;
	private JButton deleteButton;
	private JButton closeButton;
	private JButton refreshButton;
	
	// UI Color scheme
	private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
	private final Color ACCENT_COLOR = Color.PINK;
	private final Color TEXT_COLOR = new Color(180, 200, 255);
	private final Color FIELD_TEXT_COLOR = new Color(80, 80, 80);
	
	private final MainMenuFrame parentFrame;
	
	/**
	 * Creates a new DeleteTaskFrame
	 * @param parentFrame The parent MainMenuFrame
	 */
	public DeleteTaskFrame(MainMenuFrame parentFrame) {
		this.parentFrame = parentFrame;
		initComponents();
		loadTaskData();
	}
	
	/**
	 * Initialize components
	 */
	private void initComponents() {
		setTitle("Delete Tasks");
		 setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(700, 500);
		setLocationRelativeTo(null);
		setResizable(false);
		
		// Main panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		setContentPane(mainPanel);
		
		// Title label
		JLabel titleLabel = new JLabel("Delete Tasks");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		mainPanel.add(titleLabel, BorderLayout.NORTH);
		
		// Button panel
		JPanel buttonPanel = createButtonPanel();
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		// Table panel
		JPanel tablePanel = createTablePanel();
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
		// Instruction label
		JLabel instructionLabel = new JLabel("Select a task and click 'Delete' to remove it");
		instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		instructionLabel.setForeground(TEXT_COLOR);
		instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		instructionPanel.setOpaque(false);
		instructionPanel.add(instructionLabel);
		mainPanel.add(instructionPanel, BorderLayout.NORTH);
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
	 * Creates the button panel at the bottom
	 * @return The button panel
	 */
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setOpaque(false);
		
		// Delete button
		deleteButton = new JButton("Delete Selected Task");
		styleButton(deleteButton);
		buttonPanel.add(deleteButton);
		
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
		// Delete button action
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedTask();
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
	 * Delete the selected task
	 */
	private void deleteSelectedTask() {
		int selectedRow = taskTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this,
				"Please select a task to delete.",
				"No Task Selected",
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Get the task ID from the selected row
		int taskId = (int) tableModel.getValueAt(selectedRow, 0);
		String taskTitle = (String) tableModel.getValueAt(selectedRow, 1);
		
		// Confirm deletion
		int confirm = JOptionPane.showConfirmDialog(this,
			"Are you sure you want to delete the task:\n" + taskTitle + "?",
			"Confirm Deletion",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE);
		
		if (confirm == JOptionPane.YES_OPTION) {
			// Delete the task
			boolean deleted = Task.deleteTask(taskId);
			
			if (deleted) {
				JOptionPane.showMessageDialog(this,
					"Task deleted successfully.",
					"Success",
					JOptionPane.INFORMATION_MESSAGE);
				
				// Refresh the table to show the updated task list
				loadTaskData();
			} else {
				JOptionPane.showMessageDialog(this,
					"Error deleting task. Task not found.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
