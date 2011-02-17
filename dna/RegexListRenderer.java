package dna;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class RegexListRenderer extends DefaultListCellRenderer {
	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		label.setText((String)((RegexTerm)value).getPattern());
		label.setForeground((Color)((RegexTerm)value).getColor());
		return label;
	}
}