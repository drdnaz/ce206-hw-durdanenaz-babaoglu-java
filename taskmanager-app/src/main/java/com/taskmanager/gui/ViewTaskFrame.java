package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.taskmanager.model.Task;

/**
 * Frame for viewing tasks
 */
public class ViewTaskFrame extends JFrame {
	private JTable taskTable;
	private DefaultTableModel tableModel;
	private JTextField searchField;
	private JComboBox<String> categoryFilterComboBox;
	private JComboBox<String> priorityFilterComboBox;
	private JButton refreshButton;
	private JButton closeButton;
	private JButton detailsButton;
	
	// UI Color scheme
	private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
	private final Color ACCENT_COLOR = Color.PINK;
	private final Color TEXT_COLOR = new Color(180, 200, 255);
	private final Color FIELD_TEXT_COLOR = new Color(80, 80, 80);
	
	private final MainMenuFrame parentFrame;
	
	/**
	 * Creates a new ViewTaskFrame
	 * @param parentFrame The parent MainMenuFrame
	 */
	public ViewTaskFrame(MainMenuFrame parentFrame) {
		this.parentFrame = parentFrame;
		initComponents();
		loadTaskData();
	}
	
	/**
	 * Initialize components
	 */
	private void initComponents() {
		setTitle("View Tasks");
		 setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(true);
		
		// Main panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		setContentPane(mainPanel);
		
		// Title label
		JLabel titleLabel = new JLabel("Task List");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		mainPanel.add(titleLabel, BorderLayout.NORTH);
		
		// Create filter panel
		JPanel filterPanel = createFilterPanel();
		mainPanel.add(filterPanel, BorderLayout.SOUTH);
		
		// Create table panel
		JPanel tablePanel = createTablePanel();
		mainPanel.add(tablePanel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates the filter panel with search and filter options
	 * @return The filter panel
	 */
	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel(new GridBagLayout());
		filterPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		
		// Search field
		JLabel searchLabel = new JLabel("Search:");
		searchLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		filterPanel.add(searchLabel, gbc);
		
		searchField = new JTextField(15);
		searchField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		filterPanel.add(searchField, gbc);
		
		JButton searchButton = new JButton("Search");
		styleButton(searchButton);
		searchButton.setPreferredSize(new Dimension(100, 30));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		filterPanel.add(searchButton, gbc);
		
		// Category filter
		JLabel categoryLabel = new JLabel("Category:");
		categoryLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 1;
		filterPanel.add(categoryLabel, gbc);
		
		String[] categories = {"All", "Work", "Personal", "Study", "Health", "Other"};
		categoryFilterComboBox = new JComboBox<>(categories);
		categoryFilterComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		filterPanel.add(categoryFilterComboBox, gbc);
		
		// Priority filter
		JLabel priorityLabel = new JLabel("Priority:");
		priorityLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 3;
		gbc.gridy = 0;
		filterPanel.add(priorityLabel, gbc);
		
		String[] priorities = {"All", "Low", "Medium", "High", "Urgent"};
		priorityFilterComboBox = new JComboBox<>(priorities);
		priorityFilterComboBox.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		filterPanel.add(priorityFilterComboBox, gbc);
		
		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setOpaque(false);
		
		refreshButton = new JButton("Refresh");
		styleButton(refreshButton);
		refreshButton.setPreferredSize(new Dimension(100, 30));
		buttonPanel.add(refreshButton);
		
		detailsButton = new JButton("Details");
		styleButton(detailsButton);
		detailsButton.setPreferredSize(new Dimension(100, 30));
		buttonPanel.add(detailsButton);
		
		closeButton = new JButton("Close");
		styleButton(closeButton);
		closeButton.setPreferredSize(new Dimension(100, 30));
		buttonPanel.add(closeButton);
		
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		filterPanel.add(buttonPanel, gbc);
		
		// Add action listeners
		addFilterActionListeners(searchButton);
		
		return filterPanel;
	}
	
	/**
	 * Creates the table panel with task list
	 * @return The table panel
	 */
	private JPanel createTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		
		// Create columns for the table
		String[] columns = {"ID", "Title", "Description", "Due Date", "Category", "Priority", "Status"};
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
	 * Style button with consistent look and feel
	 * @param button Button to style
	 */
	private void styleButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, 12));
		button.setBackground(Color.WHITE);
		button.setForeground(new Color(80, 80, 80));
		button.setFocusPainted(false);
		button.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	/**
	 * Add action listeners to filter components
	 * @param searchButton The search button
	 */
	private void addFilterActionListeners(JButton searchButton) {
		// Search field action
		searchField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterTasks();
			}
		});
		
		// Search button action
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterTasks();
			}
		});
		
		// Category filter action
		categoryFilterComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterTasks();
			}
		});
		
		// Priority filter action
		priorityFilterComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				filterTasks();
			}
		});
		
		// Refresh button action
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadTaskData();
			}
		});
		
		// Details button action
		detailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTaskDetails();
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
			row.add(task.getDescription());
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
	 * Filter tasks based on search field and combo box selections
	 */
	private void filterTasks() {
		String searchText = searchField.getText().toLowerCase();
		String categoryFilter = (String) categoryFilterComboBox.getSelectedItem();
		String priorityFilter = (String) priorityFilterComboBox.getSelectedItem();
		
		// Create a new sorter for the table
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		taskTable.setRowSorter(sorter);
		
		// Create a filter
		RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
			@Override
			public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
				DefaultTableModel model = entry.getModel();
				int row = entry.getIdentifier();
				
				// Get values from the row
				String title = model.getValueAt(row, 1).toString().toLowerCase();
				String description = model.getValueAt(row, 2).toString().toLowerCase();
				String category = model.getValueAt(row, 4).toString();
				String priority = model.getValueAt(row, 5).toString();
				
				// Check search text
				boolean matchesSearch = searchText.isEmpty() || 
					title.contains(searchText) || 
					description.contains(searchText);
				
				// Check category
				boolean matchesCategory = categoryFilter.equals("All") || 
					category.equals(categoryFilter);
				
				// Check priority
				boolean matchesPriority = priorityFilter.equals("All") || 
					priority.equals(priorityFilter);
				
				// Row must match all active filters
				return matchesSearch && matchesCategory && matchesPriority;
			}
		};
		
		// Apply the filter
		sorter.setRowFilter(filter);
		
		// Force the table to update its view
		taskTable.invalidate();
		taskTable.revalidate();
		taskTable.repaint();
	}
	
	/**
	 * Show details of the selected task
	 */
	private void showTaskDetails() {
		int selectedRow = taskTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this,
				"Please select a task to view details.",
				"No Task Selected",
				JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		// Convert view index to model index in case table is sorted
		int modelRow = taskTable.convertRowIndexToModel(selectedRow);
		
		// Get task details
		int id = (int) tableModel.getValueAt(modelRow, 0);
		String title = (String) tableModel.getValueAt(modelRow, 1);
		String description = (String) tableModel.getValueAt(modelRow, 2);
		String dueDate = (String) tableModel.getValueAt(modelRow, 3);
		String category = (String) tableModel.getValueAt(modelRow, 4);
		String priority = (String) tableModel.getValueAt(modelRow, 5);
		String status = (String) tableModel.getValueAt(modelRow, 6);
		
		// Create a formatted message
		String message = String.format(
			"Task ID: %d\n" +
			"Title: %s\n" +
			"Description: %s\n" +
			"Due Date: %s\n" +
			"Category: %s\n" +
			"Priority: %s\n" +
			"Status: %s",
			id, title, description, dueDate, category, priority, status
		);
		
		// Display details in a dialog
		JOptionPane.showMessageDialog(this,
			message,
			"Task Details",
			JOptionPane.INFORMATION_MESSAGE);
	}

	public JTable getTaskTable() {
		return taskTable;
	}
	
	public JTextField getSearchField() {
		return searchField;
	}
	
	public JComboBox<String> getCategoryFilterComboBox() {
		return categoryFilterComboBox;
	}
	
	public JComboBox<String> getPriorityFilterComboBox() {
		return priorityFilterComboBox;
	}
}
