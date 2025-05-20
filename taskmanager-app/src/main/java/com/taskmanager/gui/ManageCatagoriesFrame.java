package com.taskmanager.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.taskmanager.model.Category;

/**
 * Frame for managing categories
 */
public class ManageCatagoriesFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable categoryTable;
	private DefaultTableModel tableModel;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton closeButton;
	
	// Form fields for add/edit panel
	private JTextField nameField;
	private JTextArea descriptionArea;
	private JTextField colorField;
	private JButton colorPickerButton;
	private JPanel colorPreviewPanel;
	
	// Panel and card layout for switching between list and edit views
	private JPanel cardPanel;
	private CardLayout cardLayout;
	
	// Currently edited category ID
	private int currentCategoryId = -1;
	
	// UI Color scheme
	private final Color BACKGROUND_COLOR = new Color(22, 34, 52);
	private final Color ACCENT_COLOR = Color.PINK;
	private final Color TEXT_COLOR = new Color(180, 200, 255);
	private final Color FIELD_TEXT_COLOR = new Color(80, 80, 80);
	
	private final MainMenuFrame parentFrame;
	
	/**
	 * Creates a new ManageCatagoriesFrame
	 * @param parentFrame The parent MainMenuFrame
	 */
	public ManageCatagoriesFrame(MainMenuFrame parentFrame) {
		this.parentFrame = parentFrame;
		initComponents();
		loadCategoryData();
	}
	
	/**
	 * Initialize components
	 */
	private void initComponents() {
		setTitle("Manage Categories");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/logo.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 500);
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
		JPanel editPanel = createEditPanel();
		cardPanel.add(editPanel, "EDIT");
		
		// Start with list view
		cardLayout.show(cardPanel, "LIST");
	}
	
	/**
	 * Creates the list panel with category table
	 * @return The list panel
	 */
	private JPanel createListPanel() {
		JPanel listPanel = new JPanel(new BorderLayout(10, 10));
		listPanel.setBackground(BACKGROUND_COLOR);
		listPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// Title label
		JLabel titleLabel = new JLabel("Category Management");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		
		// Instruction label
		JLabel instructionLabel = new JLabel("Add, edit, or delete categories");
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
	 * Creates the table panel with category list
	 * @return The table panel
	 */
	private JPanel createTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setOpaque(false);
		
		// Create columns for the table
		String[] columns = {"ID", "Name", "Description", "Color"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make table non-editable
			}
		};
		
		categoryTable = new JTable(tableModel);
		categoryTable.setFillsViewportHeight(true);
		categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		categoryTable.setRowHeight(25);
		categoryTable.setGridColor(new Color(150, 150, 150));
		categoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		
		// Hide ID column
		categoryTable.getColumnModel().getColumn(0).setMinWidth(0);
		categoryTable.getColumnModel().getColumn(0).setMaxWidth(0);
		categoryTable.getColumnModel().getColumn(0).setWidth(0);
		
		// Create a scroll pane for the table
		JScrollPane scrollPane = new JScrollPane(categoryTable);
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
		
		// Add button
		addButton = new JButton("Add Category");
		styleButton(addButton);
		buttonPanel.add(addButton);
		
		// Edit button
		editButton = new JButton("Edit Category");
		styleButton(editButton);
		buttonPanel.add(editButton);
		
		// Delete button
		deleteButton = new JButton("Delete Category");
		styleButton(deleteButton);
		buttonPanel.add(deleteButton);
		
		// Close button
		closeButton = new JButton("Close");
		styleButton(closeButton);
		buttonPanel.add(closeButton);
		
		// Add action listeners to buttons
		addButtonActions();
		
		return buttonPanel;
	}
	
	/**
	 * Creates the edit panel for adding/editing categories
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
		JLabel editTitleLabel = new JLabel("Add/Edit Category");
		editTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		editTitleLabel.setForeground(Color.WHITE);
		editPanel.add(editTitleLabel, BorderLayout.NORTH);
		
		// Add form fields
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		// Category Name Label
		JLabel nameLabel = new JLabel("Category Name:");
		nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		nameLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		formPanel.add(nameLabel, gbc);
		
		// Category Name Field
		nameField = new JTextField(20);
		nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		nameField.setForeground(FIELD_TEXT_COLOR);
		nameField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		formPanel.add(nameField, gbc);
		
		// Description Label
		JLabel descLabel = new JLabel("Description:");
		descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		descLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		formPanel.add(descLabel, gbc);
		
		// Description Area
		descriptionArea = new JTextArea(4, 20);
		descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		descriptionArea.setForeground(FIELD_TEXT_COLOR);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(descriptionArea);
		scrollPane.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		formPanel.add(scrollPane, gbc);
		
		// Color Label
		JLabel colorLabel = new JLabel("Color:");
		colorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		colorLabel.setForeground(TEXT_COLOR);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		formPanel.add(colorLabel, gbc);
		
		// Color Field
		colorField = new JTextField(10);
		colorField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		colorField.setForeground(FIELD_TEXT_COLOR);
		colorField.setBorder(new LineBorder(ACCENT_COLOR, 2, true));
		colorField.setText("#FF5733"); // Default color
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		formPanel.add(colorField, gbc);
		
		// Color Picker Button
		colorPickerButton = new JButton("Choose Color");
		colorPickerButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		colorPickerButton.setFocusPainted(false);
		colorPickerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color currentColor = Color.decode(colorField.getText());
				Color selectedColor = JColorChooser.showDialog(ManageCatagoriesFrame.this, 
															 "Choose Category Color", 
															 currentColor);
				if (selectedColor != null) {
					String hexColor = String.format("#%02X%02X%02X", 
												  selectedColor.getRed(),
												  selectedColor.getGreen(),
												  selectedColor.getBlue());
					colorField.setText(hexColor);
					colorPreviewPanel.setBackground(selectedColor);
				}
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 2;
		formPanel.add(colorPickerButton, gbc);
		
		// Color Preview Panel
		colorPreviewPanel = new JPanel();
		colorPreviewPanel.setBackground(Color.decode("#FF5733")); // Default color
		colorPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		colorPreviewPanel.setPreferredSize(new Dimension(50, 25));
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		formPanel.add(colorPreviewPanel, gbc);
		
		editPanel.add(formPanel, BorderLayout.CENTER);
		
		// Button panel for edit form
		JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		editButtonPanel.setOpaque(false);
		
		JButton saveButton = new JButton("Save Category");
		styleButton(saveButton);
		editButtonPanel.add(saveButton);
		
		JButton cancelEditButton = new JButton("Cancel");
		styleButton(cancelEditButton);
		editButtonPanel.add(cancelEditButton);
		
		// Save button action
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveCategoryChanges();
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
		button.setPreferredSize(new Dimension(150, 40));
	}
	
	/**
	 * Add action listeners to buttons
	 */
	private void addButtonActions() {
		// Add button action
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddCategoryForm();
			}
		});
		
		// Edit button action
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSelectedCategory();
			}
		});
		
		// Delete button action
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedCategory();
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
	 * Load category data into the table
	 */
	private void loadCategoryData() {
		// Clear existing data
		tableModel.setRowCount(0);
		
		// Load categories from the Category model's static list
		for (Category category : Category.getAllCategories()) {
			Object[] row = {
				category.getId(),
				category.getName(),
				category.getDescription(),
				category.getColor()
			};
			tableModel.addRow(row);
		}
	}
	
	/**
	 * Show the add category form
	 */
	private void showAddCategoryForm() {
		// Reset the current category ID
		currentCategoryId = -1;
		
		// Clear all fields
		nameField.setText("");
		descriptionArea.setText("");
		colorField.setText("#FF5733");
		colorPreviewPanel.setBackground(Color.decode("#FF5733"));
		
		// Switch to edit panel
		cardLayout.show(cardPanel, "EDIT");
	}
	
	/**
	 * Edit the selected category
	 */
	private void editSelectedCategory() {
		int selectedRow = categoryTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this,
				"Please select a category to edit.",
				"No Category Selected",
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Get the category ID from the selected row
		int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
		
		// Find the category in the model
		Category category = Category.findCategoryById(categoryId);
		if (category == null) {
			JOptionPane.showMessageDialog(this,
				"Error: Category not found.",
				"Error",
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Save current category ID
		currentCategoryId = categoryId;
		
		// Populate the edit form with category data
		nameField.setText(category.getName());
		descriptionArea.setText(category.getDescription());
		colorField.setText(category.getColor());
		
		try {
			colorPreviewPanel.setBackground(Color.decode(category.getColor()));
		} catch (NumberFormatException e) {
			colorPreviewPanel.setBackground(Color.decode("#FF5733"));
		}
		
		// Show edit panel
		cardLayout.show(cardPanel, "EDIT");
	}
	
	/**
	 * Delete the selected category
	 */
	private void deleteSelectedCategory() {
		int selectedRow = categoryTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this,
				"Please select a category to delete.",
				"No Category Selected",
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Get the category ID from the selected row
		int categoryId = (int) tableModel.getValueAt(selectedRow, 0);
		String categoryName = (String) tableModel.getValueAt(selectedRow, 1);
		
		// Confirm deletion
		int confirm = JOptionPane.showConfirmDialog(this,
			"Are you sure you want to delete the category '" + categoryName + "'?",
			"Confirm Deletion",
			JOptionPane.YES_NO_OPTION);
			
		if (confirm == JOptionPane.YES_OPTION) {
			boolean deleted = Category.deleteCategory(categoryId);
			
			if (deleted) {
				JOptionPane.showMessageDialog(this,
					"Category deleted successfully.",
					"Success",
					JOptionPane.INFORMATION_MESSAGE);
					
				// Refresh data
				loadCategoryData();
			} else {
				JOptionPane.showMessageDialog(this,
					"Cannot delete default categories.",
					"Deletion Failed",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Save category changes
	 */
	private void saveCategoryChanges() {
		// Validate form
		if (nameField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Please enter a category name.",
				"Validation Error",
				JOptionPane.ERROR_MESSAGE);
			nameField.requestFocus();
			return;
		}
		
		// Validate color format
		String colorText = colorField.getText().trim();
		if (!colorText.matches("^#[0-9A-Fa-f]{6}$")) {
			JOptionPane.showMessageDialog(this,
				"Please enter a valid hex color code (e.g. #FF5733).",
				"Validation Error",
				JOptionPane.ERROR_MESSAGE);
			colorField.requestFocus();
			return;
		}
		
		// Get form data
		String name = nameField.getText().trim();
		String description = descriptionArea.getText().trim();
		String color = colorField.getText().trim();
		
		boolean success = false;
		
		if (currentCategoryId == -1) {
			// Adding new category
			Category existingCategory = Category.findCategoryByName(name);
			if (existingCategory != null) {
				JOptionPane.showMessageDialog(this,
					"A category with this name already exists.",
					"Duplicate Name",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Category newCategory = new Category(name, description, color);
			Category.addCategory(newCategory);
			success = true;
		} else {
			// Updating existing category
			success = Category.updateCategory(currentCategoryId, name, description, color);
			
			if (!success) {
				JOptionPane.showMessageDialog(this,
					"Error updating category. A category with this name may already exist.",
					"Update Failed",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if (success) {
			JOptionPane.showMessageDialog(this,
				"Category saved successfully.",
				"Success",
				JOptionPane.INFORMATION_MESSAGE);
				
			// Return to list view and refresh data
			cardLayout.show(cardPanel, "LIST");
			loadCategoryData();
			currentCategoryId = -1;
		}
	}
	
	/**
	 * Cancel editing and return to list view
	 */
	private void cancelEdit() {
		int confirm = JOptionPane.showConfirmDialog(this,
			"Discard changes and return to category list?",
			"Confirm Cancel",
			JOptionPane.YES_NO_OPTION);
			
		if (confirm == JOptionPane.YES_OPTION) {
			cardLayout.show(cardPanel, "LIST");
			currentCategoryId = -1;
		}
	}
	
	public JTable getCategoryTable() {
		return categoryTable;
	}
}
