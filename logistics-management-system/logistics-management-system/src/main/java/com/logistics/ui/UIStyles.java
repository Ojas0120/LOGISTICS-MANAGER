package com.logistics.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * Modern UI styling utility for consistent design across the application
 */
public class UIStyles {
    // Color Palette - Modern and Minimal
    public static final Color PRIMARY_COLOR = new Color(37, 99, 235);      // Blue #2563eb
    public static final Color ACCENT_COLOR = new Color(139, 92, 246);     // Purple #8b5cf6
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129);   // Green #10b981
    public static final Color WARNING_COLOR = new Color(245, 158, 11);   // Amber #f59e0b
    public static final Color BG_COLOR = new Color(248, 250, 252);        // Light slate #f8fafc
    public static final Color TEXT_COLOR = new Color(30, 41, 59);         // Dark slate
    public static final Color BORDER_COLOR = new Color(226, 232, 240);    // Light border
    public static final Color PANEL_COLOR = new Color(241, 245, 249);     // Panel background
    
    /**
     * Creates a styled primary button with hover effects and shadow
     */
    public static JButton createPrimaryButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Add shadow effect
                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.translate(0, 2);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
        });
        return button;
    }
    
    /**
     * Creates a styled secondary button with rounded corners
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(BG_COLOR);
                } else {
                    g2.setColor(Color.WHITE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Border
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setContentAreaFilled(false);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
        });
        return button;
    }
    
    /**
     * Creates a styled text field with rounded corners
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                    g2.setColor(BORDER_COLOR);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(Color.WHITE);
        return field;
    }
    
    /**
     * Creates a styled text area
     */
    public static JTextArea createStyledTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        return area;
    }
    
    /**
     * Creates a styled label with specified font size and weight
     */
    public static JLabel createLabel(String text, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        if (bold) {
            label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        } else {
            label.setFont(new Font("Segoe UI", Font.PLAIN, fontSize));
        }
        label.setForeground(TEXT_COLOR);
        return label;
    }
    
    /**
     * Creates a styled title label with shadow effect
     */
    public static JLabel createTitleLabel(String text, Color color) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x + 2, y + 2);
                
                // Actual text
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(color);
        return label;
    }
    
    /**
     * Creates a styled table
     */
    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(37, 99, 235, 20));
        table.setSelectionForeground(PRIMARY_COLOR);
    }
    
    /**
     * Creates a styled panel with background color
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BG_COLOR);
    }
    
    /**
     * Creates a styled panel with gradient and shadow
     */
    public static JPanel createStyledPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,
                    0, getHeight(), new Color(248, 250, 252)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Light shadow
                g2.setColor(new Color(0, 0, 0, 5));
                g2.fillRoundRect(2, 2, getWidth(), getHeight(), 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        return panel;
    }
    
    /**
     * Creates a header panel with gradient
     */
    public static JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient from primary color
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    0, getHeight(), new Color(37, 99, 235).brighter()
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        return panel;
    }
    
    /**
     * Adds border shadow effect to panels
     */
    public static void addShadowBorder(Component component) {
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 0, 0, 5), 1),
                    BorderFactory.createEmptyBorder(3, 3, 3, 3)
                ),
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    "", 0, 0, null
                )
            ));
        }
    }
}

