package dna;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This is the export component of the Discourse Analyzer (DNA). You 
 * can use it to convert a .dna file into a network file. Bipartite 
 * graphs, co-occurrence networks as well as some advanced algorithms 
 * are offered. Network data can be exported as DL files (Ucinet), 
 * graphML (visone) or CSV files (spreadsheet).
 * 
 * @author Philip Leifeld
 * @version 1.21 - 23 August 2010
 */
public class Export extends JFrame {
	
	Container c;
	StatementContainer sCont;
	
	CardNetworkTypePanel cardNetworkTypePanel;
	TwoModeTypePanel twoModeTypePanel;
	OneModeTypePanel oneModeTypePanel;
	FormatPanel formatPanel;
	AlgorithmPanel algorithmPanel;
	DatePanel datePanel;
	AgreementPanel agreementPanel;
	ButtonPanel buttonPanel;
	TimeWindowPanel timeWindowPanel;
	CommetrixPanel commetrixPanel;
	SoniaPanel soniaPanel;
	CoOccurrencePanel coOccurrencePanel;
	NormalizationPanel normalizationPanel;
	//EmptyPanel emptyPanel;
	CardPanel cardPanel;
	ExcludePanel excludePanel;
	
	/**
	 * Constructor.
	 * 
	 * @param infile
	 */
	public Export() {
		sCont = new StatementContainer();
		sCont = dna.Dna.mainProgram.sc;
		exportWindow();
		resetAll();
	}
	
	public void exportWindow() {
		this.setTitle("DNA Network Export");
		ImageIcon exportIcon = new ImageIcon(getClass().getResource("/icons/chart_organisation.png"));
		this.setIconImage(exportIcon.getImage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		appearance();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void resetAll() {
		algorithmPanel.reset();
		datePanel.reset();
		agreementPanel.reset();
		oneModeTypePanel.reset();
		twoModeTypePanel.reset();
		excludePanel.reset();
		normalizationPanel.reset();
		commetrixPanel.reset();
		soniaPanel.reset();
		timeWindowPanel.reset();
		coOccurrencePanel.reset();
		formatPanel.reset();
		pack();
	}
	
	public void appearance() {
		
		c = getContentPane();
		
		JPanel appearancePanel = new JPanel(new BorderLayout()); //overall layout
		JPanel basicOptionsPanel = new JPanel(new GridBagLayout()); //center of the options panel
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(2,2,2,2);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		cardNetworkTypePanel = new CardNetworkTypePanel();
		CardLayout cntl = (CardLayout)(cardNetworkTypePanel.getLayout());
	    cntl.show(cardNetworkTypePanel, "oneModeTypePanel");
		basicOptionsPanel.add(cardNetworkTypePanel, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		algorithmPanel = new AlgorithmPanel();
		basicOptionsPanel.add(algorithmPanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		formatPanel = new FormatPanel();
		basicOptionsPanel.add(formatPanel, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		JPanel rightPanel = new JPanel(new BorderLayout());
		datePanel = new DatePanel();
		agreementPanel = new AgreementPanel();
		rightPanel.add(datePanel, BorderLayout.NORTH);
		rightPanel.add(agreementPanel, BorderLayout.SOUTH);
		basicOptionsPanel.add(rightPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		cardPanel = new CardPanel();
		CardLayout cl = (CardLayout)(cardPanel.getLayout());
	    cl.show(cardPanel, "emptyPanel");
		basicOptionsPanel.add(cardPanel, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		normalizationPanel = new NormalizationPanel();
		basicOptionsPanel.add(normalizationPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		excludePanel = new ExcludePanel();
		basicOptionsPanel.add(excludePanel, gbc);
		
		gbc.gridy = 4;
		buttonPanel = new ButtonPanel();
		basicOptionsPanel.add(buttonPanel, gbc);
		
		appearancePanel.add(basicOptionsPanel, BorderLayout.NORTH);
		
		c.add(appearancePanel);
	}
	
	/**
	 * Panel with exclude lists.
	 */
	public class ExcludePanel extends JPanel {
		
		JList person, organization, category;
		DefaultListModel personModel, organizationModel, categoryModel;
		JScrollPane personScroller, organizationScroller, categoryScroller;
		
		public ExcludePanel() {
			
			setBorder( new TitledBorder( new EtchedBorder(), "Exclude actors or categories (press ctrl)" ) );
			
			JPanel personPanel = new JPanel(new BorderLayout());
			personModel = new DefaultListModel();
			person = new JList(personModel);
			person.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			person.setLayoutOrientation(JList.VERTICAL);
			person.setVisibleRowCount(5);
			person.setFixedCellWidth(30);
			personScroller = new JScrollPane(person);
			JLabel personLabel = new JLabel("persons");
			personPanel.add(personLabel, BorderLayout.NORTH);
			personPanel.add(personScroller, BorderLayout.CENTER);
			
			JPanel organizationPanel = new JPanel(new BorderLayout());
			organizationModel = new DefaultListModel();
			organization = new JList(organizationModel);
			organization.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			organization.setLayoutOrientation(JList.VERTICAL);
			organization.setVisibleRowCount(5);
			organization.setFixedCellWidth(30);
			organizationScroller = new JScrollPane(organization);
			JLabel organizationLabel = new JLabel("organizations");
			organizationPanel.add(organizationLabel, BorderLayout.NORTH);
			organizationPanel.add(organizationScroller, BorderLayout.CENTER);
			
			JPanel categoryPanel = new JPanel(new BorderLayout());
			categoryModel = new DefaultListModel();
			category = new JList(categoryModel);
			category.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			category.setLayoutOrientation(JList.VERTICAL);
			category.setVisibleRowCount(5);
			category.setFixedCellWidth(30);
			categoryScroller = new JScrollPane(category);
			JLabel categoryLabel = new JLabel("categories");
			categoryPanel.add(categoryLabel, BorderLayout.NORTH);
			categoryPanel.add(categoryScroller, BorderLayout.CENTER);
			
			String toolTipText = "<html>From these three lists, you can select actors and/or <br>" +
					"categories that you do not want to export. DNA will act as <br>" +
					"if any statement containing the selected items were not <br>" +
					"encoded in the project file. Select multiple entries for <br>" +
					"exclusion by pressing Ctrl-V while clicking on an entry.</html>";
			setToolTipText(toolTipText);
			person.setToolTipText(toolTipText);
			personLabel.setToolTipText(toolTipText);
			organization.setToolTipText(toolTipText);
			organizationLabel.setToolTipText(toolTipText);
			category.setToolTipText(toolTipText);
			categoryLabel.setToolTipText(toolTipText);
			
			setLayout(new GridLayout(2,1));
			JPanel horizontalPanel = new JPanel(new GridLayout(1,2));
			horizontalPanel.add(personPanel);
			horizontalPanel.add(organizationPanel);
			add(horizontalPanel);
			add(categoryPanel);
		}
		
		public void reset() {
			personModel.clear();
			organizationModel.clear();
			categoryModel.clear();
			
			try {
				ArrayList<String> persons = sCont.getPersonList();
				ArrayList<String> organizations = sCont.getOrganizationList();
				ArrayList<String> categories = sCont.getCategoryList();
				
				for (int i = 0; i < persons.size(); i++) {
					personModel.addElement(persons.get(i));
				}
				for (int i = 0; i < organizations.size(); i++) {
					organizationModel.addElement(organizations.get(i));
				}
				for (int i = 0; i < categories.size(); i++) {
					categoryModel.addElement(categories.get(i));
				}
			} catch (NullPointerException npe) {
				//no file was open
			}
			
		}
	}
	
	/**
	 * Panel with card layout for custom options.
	 */
	public class CardPanel extends JPanel {
		
		public CardPanel() {
			
			setLayout(new CardLayout());
			
			timeWindowPanel = new TimeWindowPanel();
			commetrixPanel = new CommetrixPanel();
			soniaPanel = new SoniaPanel();
			//emptyPanel = new EmptyPanel();
			coOccurrencePanel = new CoOccurrencePanel();
			
			add(timeWindowPanel, "timeWindowPanel");
			add(commetrixPanel, "commetrixPanel");
			add(soniaPanel, "soniaPanel");
			//add(emptyPanel, "emptyPanel");
			add(coOccurrencePanel, "coOccurrencePanel");
		}
		
	}
	
	/**
	 * Custom option panel without any options.
	 */
	/*
	public class EmptyPanel extends JPanel {
		
		public EmptyPanel() {
			
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setBorder( new TitledBorder( new EtchedBorder(), "Custom options" ) );
			
			JLabel label = new JLabel("(no options available)");
			
			String toolTipText = "<html>This panel displays specific options for each algorithm. The <br>" +
					"algorithm you have selected does not have any such parameters.</html>";
			setToolTipText(toolTipText);
			
			add(label);
		}
	}
	*/
	
	/**
	 * Custom option panel for co-occurrence and attenuation options.
	 */
	public class CoOccurrencePanel extends JPanel {
		
		JCheckBox includeIsolates;
		JCheckBox ignoreDuplicates;
		
		public CoOccurrencePanel() {
			
			setLayout(new GridLayout(2,1));
			setBorder( new TitledBorder( new EtchedBorder(), "Custom options" ) );
			
			includeIsolates = new JCheckBox("include isolates", false);
			ignoreDuplicates = new JCheckBox("ignore duplicate statements", true);
			
			includeIsolates.setToolTipText("<html>If you export a time slice (i.e. you set a certain <br>" +
					"start or end date), there may be inactive vertices <br>" +
					"in the data set which do not connect to the other <br>" +
					"vertices in the time period you are exporting. Inac-<br>" +
					"tive vertices can also occur if you use the exclude <br>" +
					"lists. This option determines whether the isolates<br>" +
					"should be included in the export file.</html>");
			
			ignoreDuplicates.setToolTipText("<html>Disable this option to count repeated occurrences of the same<br>" +
					"statement (albeit with a different text) within the time range.<br>" +
					"Warning: If this option is disabled, you can no longer distinguish<br>" +
					"between many different statements occurring only once each, and<br>" +
					"few statements occurring rather frequently in the same article.</html>");
			
			add(ignoreDuplicates);
			add(includeIsolates);
		}
		
		public void reset() {
			includeIsolates.setSelected(false);
			ignoreDuplicates.setSelected(true);
		}
	}
	
	/**
	 * Custom option panel for co-occurrence and attenuation options.
	 */
	public class NormalizationPanel extends JPanel {
		
		JCheckBox normalization;
		
		public NormalizationPanel() {
			
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setBorder( new TitledBorder( new EtchedBorder(), "Normalization" ) );
			
			normalization = new JCheckBox("normalize", false);
			
			String toolTipText = "<html>Normalization renders the edge weight independent from the <br" +
					"propensity of an actor to make statements. The trick is to use the <br>" +
					"empirically observed number of statements of each actor as a proxy <br>" +
					"of this propensity. Normalization may be sensible e.g. if you do <br>" +
					"not want the measured similarity of actors to be affected by their <br>" +
					"institutional roles. In the case of the co-occurrence algorithm, <br>" +
					"normalization will divide the edge weight by the average number of <br>" +
					"different categories of the two actors involved in an edge. In the <br>" +
					"case of the time window algorithm, normalization will then, on top <br>" +
					"of that, divide the result by the number of time windows (i.e. the <br>" +
					"'shift' parameter. In the case of the attenuation algorithm, normali- <br>" +
					"zation will divide each incremental portion of the edge weight of <br>" +
					"the directed graph by the number of different dates of the statement <br>" +
					"currently being processed before adding up these portions. A more <br>" +
					"detailed description of the algorithms and the normalization proce- <br>" +
					"dures will be provided in the documentation.</html>";
			setToolTipText(toolTipText);
			
			normalization.setToolTipText(toolTipText);
			add(normalization);
		}
		
		public void reset() {
			normalization.setSelected(false);
		}
	}
	
	/**
	 * Custom option panel for the Commetrix algorithm.
	 */
	public class CommetrixPanel extends JPanel {
		
		JSpinner chain;
		SpinnerNumberModel chainModel;
		JTextField networkName;
		
		public CommetrixPanel() {
			
			setLayout(new GridLayout(2,1));
			setBorder( new TitledBorder( new EtchedBorder(), "Custom options: Commetrix" ) );
			
			JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			JLabel days1 = new JLabel(" days");
			chainModel = new SpinnerNumberModel(getChDur(), 1, datePanel.getDuration(), 1);
			chain = new JSpinner(chainModel);
			JLabel chainLabel = new JLabel("backward window of ");
			
			String chainToolTipText = "<html>Whenever an actor makes a statement, previous occurrences of this <br>" +
					"statement of other actors are used to establish edges. These edges are <br>" +
					"tagged with the date of the current statement. The backward window para- <br>" +
					"meter restricts the time period of previous statements, i.e. how many <br>" +
					"days should we go back in time in order to establish edges?</html>";
			chain.setToolTipText(chainToolTipText);
			chainLabel.setToolTipText(chainToolTipText);
			days1.setToolTipText(chainToolTipText);
			
			upperPanel.add(chainLabel);
			upperPanel.add(chain);
			upperPanel.add(days1);
			
			JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			JLabel name = new JLabel("network name:");
			networkName = new JTextField("DNA_CMX", 15);
			
			String nameToolTipText = "<html>This is the name of the network inside the Commetrix file. <br>" +
					"If you don't know a suitable name, you can use the default value.</html>";
			name.setToolTipText(nameToolTipText);
			networkName.setToolTipText(nameToolTipText);
			
			lowerPanel.add(name);
			lowerPanel.add(networkName);
			
			add(upperPanel);
			add(lowerPanel);
		}
		
		private double getChDur() {
			double chDur;
			if (datePanel.getDuration() < 20) {
				chDur = datePanel.getDuration();
			} else {
				chDur = 20;
			}
			return chDur;
		}
		
		public void reset() {
			chainModel = new SpinnerNumberModel(getChDur(), 1, datePanel.getDuration(), 1);
			chain.setModel(chainModel);
		}
	}
	
	/**
	 * Custom option panel for the SoNIA algorithm.
	 */
	public class SoniaPanel extends JPanel {
		
		JSpinner backwardWindow, forwardWindow;
		SpinnerNumberModel backwardModel, forwardModel;
		
		public SoniaPanel() {
			
			setLayout(new GridLayout(2,1));
			setBorder( new TitledBorder( new EtchedBorder(), "Custom options: SoNIA" ) );
			
			JPanel upperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			JLabel days1 = new JLabel(" days");
			JLabel days2 = new JLabel(" days");
			backwardModel = new SpinnerNumberModel(getChDur(), 1, datePanel.getDuration(), 1);
			forwardModel = new SpinnerNumberModel(1, 1, datePanel.getDuration(), 1);
			backwardWindow = new JSpinner(backwardModel);
			forwardWindow = new JSpinner(forwardModel);
			JLabel backwardLabel = new JLabel("backward window of ");
			JLabel forwardLabel = new JLabel("forward window of ");
			
			String backwardToolTipText = "<html>Whenever an actor makes a statement, previous occurrences of this <br>" +
					"statement of other actors are used to establish edges. These edges are <br>" +
					"tagged with the date of the current statement. The backward window para- <br>" +
					"meter restricts the time period of previous statements, i.e. how many <br>" +
					"days should we go back in time in order to establish edges?</html>";
			
			String forwardToolTipText = "<html>This parameter determines how long an edge is valid before it is<br>" +
					"disposed, i.e. how long should an edge be displayed after being established?</html>";
			
			backwardWindow.setToolTipText(backwardToolTipText);
			backwardLabel.setToolTipText(backwardToolTipText);
			forwardWindow.setToolTipText(forwardToolTipText);
			forwardLabel.setToolTipText(forwardToolTipText);
			days1.setToolTipText(backwardToolTipText);
			days2.setToolTipText(forwardToolTipText);
			
			upperPanel.add(backwardLabel);
			upperPanel.add(backwardWindow);
			upperPanel.add(days1);
			
			JPanel lowerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			lowerPanel.add(forwardLabel);
			lowerPanel.add(forwardWindow);
			lowerPanel.add(days2);
			
			add(upperPanel);
			add(lowerPanel);
		}
		
		private double getChDur() {
			double chDur;
			if (datePanel.getDuration() < 100) {
				chDur = datePanel.getDuration();
			} else {
				chDur = 100;
			}
			return chDur;
		}
		
		public void reset() {
			backwardModel = new SpinnerNumberModel(getChDur(), 1, datePanel.getDuration(), 1);
			backwardWindow.setModel(backwardModel);
			forwardModel = new SpinnerNumberModel(1, 1, datePanel.getDuration(), 1);
			forwardWindow.setModel(forwardModel);
		}
	}
	
	/**
	 * Custom option panel for the time window algorithm.
	 */
	public class TimeWindowPanel extends JPanel {
		
		JSpinner chain, shift;
		SpinnerNumberModel chainModel, shiftModel;
		
		public TimeWindowPanel() {
			
			setLayout(new BorderLayout());
			setBorder( new TitledBorder( new EtchedBorder(), "Custom options: Time window" ) );
			
			JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			ChangeListener cl = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (e.getSource().equals(chain)) {
						Double chValue = (Double)chain.getValue();
						shiftModel.setMaximum(chValue);
						if (chValue < (Double)shift.getValue()) {
							shiftModel.setValue(chValue);
						}
						
					}
				}
			};
			
			JLabel days1 = new JLabel(" days,");
			chainModel = new SpinnerNumberModel(getChDur(), 1, datePanel.getDuration(), 1);
			chain = new JSpinner(chainModel);
			chain.addChangeListener(cl);
			JLabel chainLabel = new JLabel("moving time window of");
			
			String chainToolTipText = "<html>When using the time window algorithm, there are two parameters. <br>" +
					"This parameter is called the chaining parameter. It specifies the <br>" +
					"size of a window that moves through the whole discourse. At each <br>" +
					"time step, only those edges will be established or considered that <br>" +
					"are within the time window. A useful parameter value is 20 days <br>" +
					"because this often reflects the time until the debate changes.</html>";
			
			chain.setToolTipText(chainToolTipText);
			chainLabel.setToolTipText(chainToolTipText);
			days1.setToolTipText(chainToolTipText);
			
			top.add(chainLabel);
			top.add(chain);
			top.add(days1);
			add(top, BorderLayout.NORTH);
			
			JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			shiftModel = new SpinnerNumberModel(1, 1, datePanel.getDuration(), 1);
			shift = new JSpinner(shiftModel);
			JLabel shiftLabel = new JLabel("which is shifted by");
			JLabel days2 = new JLabel(" days");

			String shiftToolTipText = "<html>The second parameter of the time window algorithm is the shift <br>" +
			"parameter. It specifies by how many days the time window should <br>" +
			"move after every round. If you set a value of 1 day, the results <br>" +
			"will be precise, but calculations are slow. If you prefer non- <br>" +
			"overlapping time windows (i.e. discrete time units), you should <br>" +
			"set the shift parameter to the same value as the chaining para- <br>" +
			"meter, which is the maximum possible value.</html>";
			
			shift.setToolTipText(shiftToolTipText);
			shiftLabel.setToolTipText(shiftToolTipText);
			days2.setToolTipText(shiftToolTipText);
			
			bottom.add(shiftLabel);
			bottom.add(shift);
			bottom.add(days2);
			add(bottom, BorderLayout.SOUTH);
			shiftModel.setMaximum(getChDur());
			
		}
		
		private double getChDur() {
			double chDur;
			if (datePanel.getDuration() < 20) {
				chDur = datePanel.getDuration();
			} else {
				chDur = 20;
			}
			return chDur;
		}
		
		public void reset() {
			chainModel = new SpinnerNumberModel(getChDur(), 1, datePanel.getDuration(), 1);
			chain.setModel(chainModel);
			shiftModel = new SpinnerNumberModel(1, 1, getChDur(), 1);
			shift.setModel(shiftModel);
		}
	}
	
	/**
	 * Create a panel where the network format (CSV, DL, graphML) can be selected.
	 */
	public class FormatPanel extends JPanel {
		
		ButtonGroup formatGroup;
		JRadioButton csv, csvmatrix, mat, gml, comsql, son;
		
		public FormatPanel() {
			setLayout(new GridLayout(6,1)); //change from 6 to 7 to re-activate DL edgelist support
			setBorder( new TitledBorder( new EtchedBorder(), "Export format" ) );
			
			ActionListener formatButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == son) {
						CardLayout cl = (CardLayout)(cardPanel.getLayout());
					    cl.show(cardPanel, "soniaPanel");
					} else if (e.getSource() == comsql) {
						CardLayout cl = (CardLayout)(cardPanel.getLayout());
					    cl.show(cardPanel, "commetrixPanel");
					} else if (e.getSource() == csv && algorithmPanel.affil.isSelected()) {
						CardLayout cl = (CardLayout)(cardPanel.getLayout());
					    cl.show(cardPanel, "emptyPanel");
					} else if (algorithmPanel.affil.isSelected()) {
						CardLayout cl = (CardLayout)(cardPanel.getLayout());
					    cl.show(cardPanel, "coOccurrencePanel");
					}
				}
			};
			
			formatGroup = new ButtonGroup();
			csvmatrix = new JRadioButton("CSV matrix", false);
			csvmatrix.addActionListener(formatButtonListener);
			csvmatrix.setToolTipText("<html>The network will be exported as a socio-matrix <br>" +
					"in a text file with comma-separated values.</html");
			formatGroup.add(csvmatrix);
			add(csvmatrix);
			mat = new JRadioButton("DL fullmatrix", true);
			mat.addActionListener(formatButtonListener);
			mat.setToolTipText("<html>The DL format can be imported by UCINET and visone. <br>" +
					"DL fullmatrix corresponds to the DL file specification <br>" +
					"where networks are stored as socio-matrices.</html>");
			formatGroup.add(mat);
			add(mat);
			
			gml = new JRadioButton("graphML", false);
			gml.addActionListener(formatButtonListener);
			gml.setToolTipText("<html>The graphML format is the native format of visone and other <br>" +
					"graph-drawing software packages. The export filter will not <br>" +
					"export any spatial positions of the vertices, so they will <br>" +
					"at first appear all at the same position. Please use layout <br>" +
					"algorithms to place your nodes in an appropriate way.</html>");
			formatGroup.add(gml);
			add(gml);
			comsql = new JRadioButton("Commetrix SQL", false);
			comsql.addActionListener(formatButtonListener);
			comsql.setToolTipText("<html>Commetrix is a software for the dynamic visualization of <br>" +
					"networks with continuous-time measures. DNA will export a format that <br>" +
					"can be read by the developers of Commetrix or by a tool called CMX <br>" +
					"Producer. They will produce a CMX file which can be read by the CMX <br>" +
					"Analyzer. The advantage of Commetrix is that you can see how the <br>" +
					"discourse evolves over time. You can directly evaluate when important <br>" +
					"changes happen in the discourse network and to what extent.</html>");
			formatGroup.add(comsql);
			add(comsql);
			son = new JRadioButton("SoNIA", false);
			son.addActionListener(formatButtonListener);
			son.setToolTipText("<html>SoNIA is a free software for the dynamic visualization of networks.</html>");
			formatGroup.add(son);
			add(son);
			csv = new JRadioButton("CSV list", false);
			csv.addActionListener(formatButtonListener);
			csv.setToolTipText("<html>" +
					"Comma-separated text file for use <br>" +
					"in spreadsheet/office or statistics <br>" +
					"software packages. This is <i>not</i> a <br>" +
					"network format! It only lists statements <br>" +
					"or persons with their affiliations.</html>");
			formatGroup.add(csv);
			add(csv);
		}
		
		public void reset() {
			mat.setSelected(true);
		}
	}
	
	public class AlgorithmPanel extends JPanel {
		
		public JRadioButton sl, el, affil, xSec, tWind, atten, sonia;
		
		public AlgorithmPanel() {
			setLayout(new GridLayout(6,1));
			setBorder( new TitledBorder( new EtchedBorder(), "Algorithm" ) );

			ActionListener algoListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(el)) {
						elAdjust();
					} else if (e.getSource().equals(affil)) {
						affilAdjust();
					} else if (e.getSource().equals(tWind)) {
						tWindAdjust();
					} else if (e.getSource().equals(atten)) {
						attenAdjust();
					} else {
						xSecAdjust();
					}
				}
			};
			
			ButtonGroup algoGroup = new ButtonGroup();
			affil = new JRadioButton("Affiliation network", false);
			affil.addActionListener(algoListener);
			affil.setToolTipText("The affiliation algorithm will produce a bipartite graph with binary edges.");
			algoGroup.add(affil);
			add(affil);
			xSec = new JRadioButton("Number of co-occurrences", true);
			xSec.addActionListener(algoListener);
			xSec.setToolTipText("<html>The co-occurrence algorithm will produce a one-mode network with the <br>" +
					"number of co-occurrences between two vertices as the edge weight. This <br>" +
					"can be interpreted as a measure of similarity between the vertices.</html>");
			algoGroup.add(xSec);
			add(xSec);
			tWind = new JRadioButton("Time window", false);
			tWind.addActionListener(algoListener);
			tWind.setToolTipText("<html>The time window algorithm will move a window through the discourse, i.e. from <br>" +
					"the beginning to the end of the discourse, and will establish links at each <br>" +
					"time step only if the statements of the source vertex and the target vertex <br>" +
					"are made within the time period of the window. This approach can account for <br>" +
					"the context of a political discourse since the meaning of statements can <br>" +
					"change over time. The result is a weighted network because values are added <br>" +
					"to the existing edge weights at each time step if a new edge is detected.</html>");
			algoGroup.add(tWind);
			add(tWind);
			atten = new JRadioButton("Attenuation", false);
			atten.addActionListener(algoListener);
			atten.setToolTipText("<html>The attenuation algorithm employs a decay function for edge weights. <br>" +
					"An additional assumption could be that political actors refer to each <br>" +
					"other by making a statement that somebody else made before. The proba- <br>" +
					"bility that the actor is really referring to the other actor can be <br>" +
					"conceptualized as being inversely proportional to the time that has <br>" +
					"passed since the earlier statement. The algorithm will compare the dates <br>" +
					"of statements and create a network of referrals between actors, where <br>" +
					"the edge weight is determined by the inverse duration between each <br>" +
					"pair of statements. Inverse weights are aggregated to a nuanced directed <br>" +
					"network of referrals between actors.</html>");
			algoGroup.add(atten);
			add(atten);
			el = new JRadioButton("Dynamic visualization", false);
			el.addActionListener(algoListener);
			el.setToolTipText("<html>This produces a list of edges, where each edge is affiliated with <br>" +
					"the date of the vertex with the statement that occurred later <br>" +
					"in the discourse, i.e. if actor A makes a statement on the 12th <br>" +
					"of June and Actor B makes a statement on the 24th of June, a <br>" +
					"binary edge between A and B will be recorded, and the edge will <br>" +
					"have the 24th of July as the date of its creation. This algorithm <br>" +
					"is useful for dynamic, continuous-time visualization of network <br>" +
					"evolution as featured in Commetrix or SoNIA. If you use SoNIA, <br>" +
					"you additionally have to select a duration after which an edge <br>" +
					"expires. If you use Commetrix, this can be done inside Commetrix.</html>");
			algoGroup.add(el);
			add(el);
		}
		
		public void elAdjust() {
			formatPanel.csvmatrix.setEnabled(false);
			formatPanel.mat.setEnabled(false);
			formatPanel.gml.setEnabled(false);
			formatPanel.comsql.setEnabled(true);
			formatPanel.csv.setEnabled(false);
			formatPanel.comsql.setSelected(true);
			formatPanel.son.setEnabled(true);
			agreementPanel.conflict.setEnabled(true);
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
		    cl.show(cardPanel, "commetrixPanel");
		    CardLayout cntl = (CardLayout)(cardNetworkTypePanel.getLayout());
		    cntl.show(cardNetworkTypePanel, "oneModeTypePanel");
		    cardNetworkTypePanel.setEnabled(true);
		    coOccurrencePanel.reset();
		    normalizationPanel.normalization.setEnabled(false);
		    normalizationPanel.normalization.setSelected(false);
		    oneModeTypePanel.reset();
		}
		
		public void affilAdjust() {
			formatPanel.csvmatrix.setEnabled(true);
			formatPanel.mat.setEnabled(true);
			formatPanel.gml.setEnabled(true);
			formatPanel.comsql.setEnabled(false);
			formatPanel.csv.setEnabled(true);
			formatPanel.mat.setSelected(true);
			formatPanel.son.setEnabled(true);
			if (agreementPanel.conflict.isSelected()) {
				agreementPanel.yes.setSelected(true);
			}
			agreementPanel.conflict.setEnabled(false);
			CardLayout cntl = (CardLayout)(cardNetworkTypePanel.getLayout());
		    cntl.show(cardNetworkTypePanel, "twoModeTypePanel");
			cardNetworkTypePanel.setEnabled(true);
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
		    cl.show(cardPanel, "coOccurrencePanel");
		    coOccurrencePanel.reset();
		    normalizationPanel.normalization.setEnabled(false);
		    normalizationPanel.normalization.setSelected(false);
		    oneModeTypePanel.reset();
		}
		
		public void tWindAdjust() {
			formatPanel.csvmatrix.setEnabled(true);
			formatPanel.mat.setEnabled(true);
			formatPanel.gml.setEnabled(true);
			formatPanel.comsql.setEnabled(false);
			formatPanel.csv.setEnabled(false);
			formatPanel.mat.setSelected(true);
			formatPanel.son.setEnabled(false);
			CardLayout cntl = (CardLayout)(cardNetworkTypePanel.getLayout());
		    cntl.show(cardNetworkTypePanel, "oneModeTypePanel");
			cardNetworkTypePanel.setEnabled(true);
			if (agreementPanel.conflict.isSelected()) {
				agreementPanel.comb.setSelected(true);
			}
			agreementPanel.conflict.setEnabled(false);
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
		    cl.show(cardPanel, "timeWindowPanel");
		    coOccurrencePanel.reset();
		    normalizationPanel.normalization.setEnabled(true);
		    oneModeTypePanel.reset();
		}
		
		public void attenAdjust() {
			formatPanel.csvmatrix.setEnabled(true);
			formatPanel.mat.setEnabled(true);
			formatPanel.gml.setEnabled(true);
			formatPanel.comsql.setEnabled(false);
			formatPanel.csv.setEnabled(false);
			formatPanel.mat.setSelected(true);
			formatPanel.son.setEnabled(false);
			CardLayout cntl = (CardLayout)(cardNetworkTypePanel.getLayout());
		    cntl.show(cardNetworkTypePanel, "oneModeTypePanel");
			cardNetworkTypePanel.setEnabled(true);
			if (agreementPanel.conflict.isSelected()) {
				agreementPanel.comb.setSelected(true);
			}
			agreementPanel.conflict.setEnabled(false);
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
			cl.show(cardPanel, "emptyPanel");
			coOccurrencePanel.reset();
			normalizationPanel.normalization.setEnabled(true);
			oneModeTypePanel.oneModeCombo.removeItemAt(2);
			oneModeTypePanel.oneModeCombo.setSelectedIndex(1);
			oneModeTypePanel.changeViaCombo();
		}
		
		public void xSecAdjust() {
			formatPanel.csvmatrix.setEnabled(true);
			formatPanel.mat.setEnabled(true);
			formatPanel.gml.setEnabled(true);
			formatPanel.comsql.setEnabled(false);
			formatPanel.csv.setEnabled(false);
			formatPanel.mat.setSelected(true);
			formatPanel.son.setEnabled(false);
			CardLayout cntl = (CardLayout)(cardNetworkTypePanel.getLayout());
		    cntl.show(cardNetworkTypePanel, "oneModeTypePanel");
			cardNetworkTypePanel.setEnabled(true);
			agreementPanel.conflict.setEnabled(true);
			CardLayout cl = (CardLayout)(cardPanel.getLayout());
			cl.show(cardPanel, "coOccurrencePanel");
			coOccurrencePanel.reset();
			normalizationPanel.normalization.setEnabled(true);
			oneModeTypePanel.reset();
		}
		
		public void reset() {
			xSec.setSelected(true);
			xSecAdjust();
		}
	}
	
	public class AgreementPanel extends JPanel {
		
		JRadioButton yes, no, comb, conflict;
		
		public AgreementPanel() {
			setLayout(new GridLayout(4,1));
			setBorder( new TitledBorder( new EtchedBorder(), "Agreement" ) );

			ActionListener agreementListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//put action here
				}
			};

			ButtonGroup agreeGroup = new ButtonGroup();
			yes = new JRadioButton("yes", false);
			yes.addActionListener(agreementListener);
			yes.setToolTipText("Only establish an edge if both statements are positive.");
			agreeGroup.add(yes);
			add(yes);
			no = new JRadioButton("no", false);
			no.addActionListener(agreementListener);
			no.setToolTipText("Only establish an edge if both statements are negative.");
			agreeGroup.add(no);
			add(no);
			comb = new JRadioButton("combined", true);
			comb.addActionListener(agreementListener);
			comb.setToolTipText("<html>Only establish an edge if both statements <br>" +
					"are positive or both statements are negative.</html>");
			agreeGroup.add(comb);
			add(comb);
			conflict = new JRadioButton("conflict", false);
			conflict.addActionListener(agreementListener);
			conflict.setToolTipText("<html>Only establish an edge if one statement is <br>" +
					"positive and the other one is negative. This <br>" +
					"yields a network of direct contradictions.</html>");
			agreeGroup.add(conflict);
			add(conflict);
		}
		
		public void reset() {
			comb.setSelected(true);
		}
	}
	
	/**
	 * Panel with card layout for network types.
	 */
	public class CardNetworkTypePanel extends JPanel {
		
		public CardNetworkTypePanel() {
			
			setLayout(new CardLayout());
			
			twoModeTypePanel = new TwoModeTypePanel();
			oneModeTypePanel = new OneModeTypePanel();
			
			add(twoModeTypePanel, "twoModeTypePanel");
			add(oneModeTypePanel, "oneModeTypePanel");
		}
		
		public void setEnabled(boolean active) {
			twoModeTypePanel.po.setEnabled(active);
			twoModeTypePanel.pc.setEnabled(active);
			twoModeTypePanel.oc.setEnabled(active);
			oneModeTypePanel.oneModeCombo.setEnabled(active);
			oneModeTypePanel.viaLabel.setEnabled(active);
			oneModeTypePanel.viaCombo.setEnabled(active);
		}
	}
	
	public class TwoModeTypePanel extends JPanel {
		
		JRadioButton po, pc, oc;
		
		public TwoModeTypePanel() {
			
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setBorder( new TitledBorder( new EtchedBorder(), "Network type (bipartite)" ) );
			
			ButtonGroup bipartiteGroup = new ButtonGroup();
			oc = new JRadioButton("org x cat", true);
			oc.setToolTipText("Create a bipartite network of organizations x categories.");
			bipartiteGroup.add(oc);
			add(oc);
			pc = new JRadioButton("pers x cat", false);
			pc.setToolTipText("Create a bipartite network of persons x categories.");
			bipartiteGroup.add(pc);
			add(pc);
			po = new JRadioButton("pers x org", false);
			po.setToolTipText("Create a bipartite network of persons x organizations.");
			bipartiteGroup.add(po);
			add(po);
		}
		
		public void reset() {
			oc.setSelected(true);
		}
	}
	
	public class OneModeTypePanel extends JPanel {
		
		//JRadioButton oo, pp, cc;
		JComboBox oneModeCombo, viaCombo;
		JLabel viaLabel;
		
		public OneModeTypePanel() {
			
			setLayout(new FlowLayout(FlowLayout.LEFT));
			setBorder( new TitledBorder( new EtchedBorder(), "Network type (one-mode)" ) );
			
			ActionListener networkTypeListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(oneModeCombo)) {
						changeViaCombo();
					}
				}
			};
			
			String[] classes = new String[] {"persons x persons", "organizations x organizations", "categories x categories"};
			oneModeCombo = new JComboBox(classes);
			oneModeCombo.setSelectedIndex(1);
			oneModeCombo.addActionListener(networkTypeListener);
			oneModeCombo.setToolTipText("Select the type of adjacency network to be created.");
			
			viaLabel = new JLabel("via");
			
			viaCombo = new JComboBox();
			String viaToolTipText = "Select what the vertices should have in common.";
			viaLabel.setToolTipText(viaToolTipText);
			viaCombo.setToolTipText(viaToolTipText);
			changeViaCombo();
			
			add(oneModeCombo);
			add(viaLabel);
			add(viaCombo);
		}
		
		public void changeViaCombo() {
			viaCombo.removeAllItems();
			if (oneModeCombo.getSelectedIndex() == 0) {
				viaCombo.addItem("org");
				viaCombo.addItem("cat");
				viaCombo.setSelectedItem("cat");
			} else if (oneModeCombo.getSelectedIndex() == 1) {
				viaCombo.addItem("pers");
				viaCombo.addItem("cat");
				viaCombo.setSelectedItem("cat");
			} else {
				viaCombo.addItem("pers");
				viaCombo.addItem("org");
				viaCombo.setSelectedItem("org");
			}
			
			try {
				if (algorithmPanel.atten.isSelected()) {
					viaCombo.removeAllItems();
					viaCombo.addItem("cat");
				}
			} catch (NullPointerException npe) {}
		}
		
		public void reset() {
			if (oneModeTypePanel.oneModeCombo.getItemCount() < 3) {
				oneModeTypePanel.oneModeCombo.addItem("categories x categories");
			}
			oneModeCombo.setSelectedIndex(1);
			changeViaCombo();
		}
	}
	
	public class DatePanel extends JPanel {
		
		String datePattern, maskPattern;
		JLabel startLabel, stopLabel;
		GregorianCalendar fDate, lDate;
		SpinnerDateModel startModel, stopModel;
		JSpinner startSpinner, stopSpinner;
		ChangeListener dateListener;
		Date startDate, stopDate;
		
		public DatePanel() {
			setLayout(new GridLayout(4,1));
			setBorder( new TitledBorder( new EtchedBorder(), "Time period" ) );
			
			dateListener = new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					Double chValue = (Double)timeWindowPanel.chain.getValue();
					if (getDuration() < chValue) {
						commetrixPanel.chainModel.setValue(getDuration());
						commetrixPanel.chainModel.setMaximum(getDuration());
						
						timeWindowPanel.chainModel.setValue(getDuration());
						timeWindowPanel.chainModel.setMaximum(getDuration());
						timeWindowPanel.shift.setValue(getDuration());
						timeWindowPanel.shift.setModel(timeWindowPanel.shiftModel);
					} else {
						commetrixPanel.chainModel.setMaximum(getDuration());
						timeWindowPanel.chainModel.setMaximum(getDuration());
					}
					commetrixPanel.chain.setModel(timeWindowPanel.chainModel);
					timeWindowPanel.chain.setModel(timeWindowPanel.chainModel);
				}
			};
			
			startLabel = new JLabel("start:");
			try {
				fDate = sCont.getFirstDate();
			} catch (NullPointerException npe) {
				fDate = new GregorianCalendar(1900, 1, 1);
			}
			startDate = fDate.getTime();
			startSpinner = new JSpinner();
			startSpinner.setToolTipText("The start date of the time period that you want to consider for export.");
			startLabel.setToolTipText("The start date of the time period that you want to consider for export.");
			startModel = new SpinnerDateModel();
			startModel.setCalendarField( Calendar.DAY_OF_YEAR );
			startModel.setValue(startDate);
			startSpinner.setModel( startModel );
			startSpinner.setEditor(new JSpinner.DateEditor(startSpinner, "dd.MM.yyyy"));
			startSpinner.addChangeListener(dateListener);
			add(startLabel);
			add(startSpinner);
			
			stopLabel = new JLabel("stop:");
			try {
				lDate = sCont.getLastDate();
			} catch (NullPointerException npe) {
				lDate = new GregorianCalendar(2099, 1, 1);
			}
			stopDate = lDate.getTime();
			stopSpinner = new JSpinner();
			stopSpinner.setToolTipText("The end date of the time period that you want to consider for export.");
			stopLabel.setToolTipText("The end date of the time period that you want to consider for export.");
			stopModel = new SpinnerDateModel();
			stopModel.setCalendarField( Calendar.DAY_OF_YEAR );
			stopModel.setValue(stopDate);
			stopSpinner.setModel( stopModel );
			stopSpinner.setEditor(new JSpinner.DateEditor(stopSpinner, "dd.MM.yyyy"));
			stopSpinner.addChangeListener(dateListener);
			add(stopLabel);
			add(stopSpinner);
		}
		
		private double getDuration() {
			Date date1 = (Date)startSpinner.getValue();
			Date date2 = (Date)stopSpinner.getValue();
			GregorianCalendar d1cal = new GregorianCalendar();
			d1cal.setTime(date1);
			GregorianCalendar d2cal = new GregorianCalendar();
			d2cal.setTime(date2);
			double dur = (d2cal.getTimeInMillis() - d1cal.getTimeInMillis()) / (24*60*60*1000) + 1;
			dur = Math.abs(dur);
			return dur;
		}
		
		public void reset() {
			try {
				fDate = sCont.getFirstDate();
			} catch (NullPointerException npe) {
				fDate = new GregorianCalendar(1900, 1, 1);
			}
			startDate = fDate.getTime();
			startModel.setValue(startDate);
			
			try {
				lDate = sCont.getLastDate();
			} catch (NullPointerException npe) {
				lDate = new GregorianCalendar(2099, 1, 1);
			}
			stopDate = lDate.getTime();
			stopModel.setValue(stopDate);
		}
	}
	
	public class ButtonPanel extends JPanel {
		
		JButton reset, export;
		JCheckBox help;
		
		public ButtonPanel() {
			setLayout(new BorderLayout());
			
			ActionListener buttonListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource().equals(reset)) {
						resetAll();
					} else if (e.getSource().equals(help)) {
						toggleHelp();
					} else if (e.getSource().equals(export)) {
						Thread exp = new Thread( new FileExporter() );
						exp.start();
					}
				}
			};
			
			help = new JCheckBox("display help");
			help.addActionListener(buttonListener);
			help.setToolTipText("<html>If you check this button, context-sensitive help is <br>" +
					"enabled. Tooltips will show up if the mouse is moved <br>" +
					"over an option.</html>");
			
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			
			Icon resetIcon = new ImageIcon(getClass().getResource("/icons/arrow_rotate_clockwise.png"));
			reset = new JButton("Reset", resetIcon);
			reset.addActionListener(buttonListener);
			reset.setToolTipText("Revert all options to their default settings.");
			Icon okIcon = new ImageIcon(getClass().getResource("/icons/tick.png"));
			export = new JButton("Export...", okIcon);
			export.setToolTipText("Choose a file name and export with the selected options.");
			export.addActionListener(buttonListener);
			export.setEnabled(true);
			buttons.add(reset);
			buttons.add(export);
			
			add(help, BorderLayout.WEST);
			add(buttons, BorderLayout.EAST);
			
			toggleHelp();
		}
		
		public void toggleHelp() {
			javax.swing.ToolTipManager.sharedInstance().setInitialDelay(10);
			if (help.isSelected() == true) {
				javax.swing.ToolTipManager.sharedInstance().setEnabled(true);
				javax.swing.ToolTipManager.sharedInstance().setDismissDelay(30000);
			} else {
				javax.swing.ToolTipManager.sharedInstance().setEnabled(false);
				javax.swing.ToolTipManager.sharedInstance().setDismissDelay(0);
			}
		}
	}
	
	class FileExporter implements Runnable {
		
		JFrame progressFrame;
		String extension, description, outfile;
		StatementContainer sc;
		ArrayList<String> class1, class2, class3, s_agree, s_text;
		ArrayList<GregorianCalendar> s_date;
		DnaGraph graph;
		GregorianCalendar start, stop;
		
		//declarations for normalization procedures
		int sourceCounter;
		int targetCounter;
		String sourceLabel;
		String targetLabel;
		ArrayList<String> tabuSource = new ArrayList<String>();
		ArrayList<String> tabuTarget = new ArrayList<String>();
		
		//isolates lists
		ArrayList<String> persIsolates = new ArrayList<String>();
		ArrayList<String> orgIsolates = new ArrayList<String>();
		ArrayList<String> catIsolates = new ArrayList<String>();
		ArrayList<String> isolatesList = new ArrayList<String>();
		
		public void run() {
			outfile = getFileName();
			if (outfile.equals("null")) {
				//canceled export
			} else {
				buttonPanel.export.setEnabled(false);
				buttonPanel.reset.setEnabled(false);
				System.out.println("Exporting - this can take a long time...");
				showProgress();
				
				System.out.print("  Creating internal copy of the statements... ");
				sc = (StatementContainer)sCont.clone();
				System.out.println("done.");
				applyFilters();
				createGraph();
				exportControl();
				
				progressFrame.dispose();
				buttonPanel.export.setEnabled(true);
				buttonPanel.reset.setEnabled(true);
			}
		}
		
		public void showProgress() {
			progressFrame = new JFrame();
			Container progressContent = progressFrame.getContentPane();
			progressFrame.setTitle("Calculating...");
			ImageIcon progressIcon = new ImageIcon(getClass().getResource("/icons/dna16.png"));
			progressFrame.setIconImage(progressIcon.getImage());
			progressFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			JProgressBar progress = new JProgressBar();
			progress.setPreferredSize(new Dimension(200, 30));
			progress.setString("Calculating");
			progress.setStringPainted(true);
			progress.setIndeterminate(true);
			progressContent.add(progress);
			progressFrame.pack();
			progressFrame.setLocationRelativeTo(null);
			progressFrame.setVisible(true);
			progressFrame.setAlwaysOnTop(true);
		}
		
		@SuppressWarnings("unchecked")
		public void applyFilters() {
			
			//apply exclude filter
			System.out.print("  Applying exclude list filter... ");
			int oldSize = sc.size();
			Object[] excludePers = excludePanel.person.getSelectedValues();
			Object[] excludeOrg = excludePanel.organization.getSelectedValues();
			Object[] excludeCat = excludePanel.category.getSelectedValues();
			for (int i = sc.size() - 1; i >= 0; i--) {
				boolean exclude = false;
				for (int j = 0; j < excludePers.length; j++) {
					if (sc.get(i).getPerson().equals((String)excludePers[j])) {
						exclude = true;
					}
				}
				for (int j = 0; j < excludeOrg.length; j++) {
					if (sc.get(i).getOrganization().equals((String)excludeOrg[j])) {
						exclude = true;
					}
				}
				for (int j = 0; j < excludeCat.length; j++) {
					if (sc.get(i).getCategory().equals((String)excludeCat[j])) {
						exclude = true;
					}
				}
				if (exclude == true) {
					sc.remove(i);
				}
			}
			int newSize = sc.size();
			System.out.println("Keeping " + newSize + " out of " + oldSize + " statements.");
			
			//keep backup in case the includeIsolates option is enabled (must be between exclude and date filer)
			persIsolates = (ArrayList<String>) sc.getPersonList().clone();
			orgIsolates = (ArrayList<String>) sc.getOrganizationList().clone();
			catIsolates = (ArrayList<String>) sc.getCategoryList().clone();
			
			//apply date filter
			System.out.print("  Applying date filter... ");
			oldSize = sc.size();
			start = new GregorianCalendar();
			start.setTime((Date)datePanel.startSpinner.getValue());
			stop = new GregorianCalendar();
			stop.setTime((Date)datePanel.stopSpinner.getValue());
			for (int i = sc.size() - 1; i >= 0; i--) {
				GregorianCalendar currentDate = new GregorianCalendar();
				currentDate.setTime(sc.get(i).getDate());
				if (currentDate.before(start) || currentDate.after(stop)) {
					sc.remove(i);
				}
			}
			newSize = sc.size();
			System.out.println("Keeping " + newSize + " out of " + oldSize + " statements.");
			
			//agreement filter
			System.out.print("  Applying agreement filter... ");
			oldSize = sc.size();
			for (int i = sc.size() - 1; i >= 0; i--) {
				if ((sc.get(i).getAgreement().equals("no") && agreementPanel.yes.isSelected())
						|| (sc.get(i).getAgreement().equals("yes") && agreementPanel.no.isSelected())) {
					sc.remove(i);
				}
			}
			newSize = sc.size();
			System.out.println("Keeping " + newSize + " out of " + oldSize + " statements.");
			
			//undesirable character filter
			System.out.print("  Applying undesirable character filter... ");
			String regex1 = "'|^[ ]+|[ ]+$|[ ]*;|;[ ]*";
			String regex2 = "\\s+";
			for (int i = 0; i < sc.size(); i++) {
				sc.get(i).setPerson(sc.get(i).getPerson().replaceAll(regex1, "").replaceAll(regex2, " "));
				sc.get(i).setOrganization(sc.get(i).getOrganization().replaceAll(regex1, "").replaceAll(regex2, " "));
				sc.get(i).setCategory(sc.get(i).getCategory().replaceAll(regex1, "").replaceAll(regex2, " "));
			}
			for (int i = 0; i < persIsolates.size(); i++) {
				persIsolates.set(i, persIsolates.get(i).replaceAll(regex1, "").replaceAll(regex2, " "));
			}
			for (int i = 0; i < orgIsolates.size(); i++) {
				orgIsolates.set(i, orgIsolates.get(i).replaceAll(regex1, "").replaceAll(regex2, " "));
			}
			for (int i = 0; i < catIsolates.size(); i++) {
				catIsolates.set(i, catIsolates.get(i).replaceAll(regex1, "").replaceAll(regex2, " "));
			}
			System.out.println("done.");
		}
		
		/**
		 * File chooser.
		 * 
		 * @return string of the file name
		 */
		public String getFileName() {
			if (formatPanel.csv.isSelected() || formatPanel.csvmatrix.isSelected()) {
				extension = ".csv";
				description = "Comma-separated values (*.csv)";
			} else if (formatPanel.mat.isSelected()) {
				extension = ".dl";
				description = "UCINET DL file (*.dl)";
			} else if (formatPanel.gml.isSelected()) {
				extension = ".graphml";
				description = "GraphML file (*.graphml)";
			} else if (formatPanel.comsql.isSelected()) {
				extension = ".sql";
				description = "Commetrix SQL file (*.sql)";
			} else if (formatPanel.son.isSelected()) {
				extension = ".son";
				description = "SoNIA file (*.son)";
			}
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(extension) 
					|| f.isDirectory();
				}
				public String getDescription() {
					return description;
				}
			});

			int returnVal = fc.showSaveDialog(Export.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String filename = new String(file.getPath());
				if ( !file.getPath().endsWith(extension) ) {
					filename = filename + extension;
				}
				return filename;
			} else {
				System.out.println("Export cancelled.");
				return "null";
			}
		}
		
		public void createGraph() {
			if (algorithmPanel.affil.isSelected()) {
				if (formatPanel.son.isSelected()) {
					exportFilterSoniaAffiliation(outfile);
				} else {
					generateGraphAffiliation();
				}
			} else if (algorithmPanel.el.isSelected()) {
				if (formatPanel.comsql.isSelected()) {
					exportFilterCommetrix(outfile);
				} else if (formatPanel.son.isSelected()) {
					exportFilterSonia(outfile);
				}
			} else if (algorithmPanel.xSec.isSelected()) {
				generateGraphCoOccurrence();
			} else if (algorithmPanel.tWind.isSelected()) {
				generateGraphTimeWindow();
			} else if (algorithmPanel.atten.isSelected()) {
				generateGraphAttenuation();
			}
		}
		
		public void exportControl() {
			if (formatPanel.csvmatrix.isSelected()) {
				exportCsvMatrix(outfile);
			} else if (formatPanel.mat.isSelected()) {
				exportDlFullMatrix(outfile);
			} else if (formatPanel.gml.isSelected()) {
				graphMl(outfile);
			} else if (formatPanel.csv.isSelected()) {
				exportCsvAffiliationList(outfile);
			}
		}
		
		/**
		 * Generate bipartite graph from lists of statements.
		 */
		private void generateGraphAffiliation() {
			
			//create graph
			graph = new DnaGraph();
			graph.removeAllEdges();
			graph.removeAllVertices();
			
			//create vertices
			int count = 0;
			class1 = new ArrayList<String>();
			class2 = new ArrayList<String>();
			s_agree = new ArrayList<String>();
			if (twoModeTypePanel.oc.isSelected()) {
				System.out.print("  Creating vertices for organizations... ");
				class1 = sc.getStringList("o");
				for (int i = 0; i < sc.getOrganizationList().size(); i++) {
					if (!sc.getOrganizationList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getOrganizationList().get(i), "o"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < orgIsolates.size(); i++) {
						if (!graph.containsVertex(orgIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, orgIsolates.get(i), "o"));
							count++;
						}
					}
				}
				System.out.println(graph.countVertexType("o") + " vertices were added.");
				System.out.print("  Creating vertices for categories... ");
				class2 = sc.getStringList("c");
				for (int i = 0; i < sc.getCategoryList().size(); i++) {
					if (!sc.getCategoryList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getCategoryList().get(i), "c"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < catIsolates.size(); i++) {
						if (!graph.containsVertex(catIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, catIsolates.get(i), "c"));
							count++;
						}
					}
				}
				System.out.println(graph.countVertexType("c") + " vertices were added.");
			} else if (twoModeTypePanel.po.isSelected()) {
				System.out.print("  Creating vertices for persons... ");
				class1 = sc.getStringList("p");
				for (int i = 0; i < sc.getPersonList().size(); i++) {
					if (!sc.getPersonList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getPersonList().get(i), "p"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < persIsolates.size(); i++) {
						if (!graph.containsVertex(persIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, persIsolates.get(i), "p"));
							count++;
						}
					}
				}
				System.out.println(graph.countVertexType("p") + " vertices were added.");
				System.out.print("  Creating vertices for organizations... ");
				class2 = sc.getStringList("o");
				for (int i = 0; i < sc.getOrganizationList().size(); i++) {
					if (!sc.getOrganizationList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getOrganizationList().get(i), "o"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < orgIsolates.size(); i++) {
						if (!graph.containsVertex(orgIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, orgIsolates.get(i), "o"));
							count++;
						}
					}
				}
				System.out.println(graph.countVertexType("o") + " vertices were added.");
			} else if (twoModeTypePanel.pc.isSelected()) {
				System.out.print("  Creating vertices for persons... ");
				class1 = sc.getStringList("p");
				for (int i = 0; i < sc.getPersonList().size(); i++) {
					if (!sc.getPersonList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getPersonList().get(i), "p"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < persIsolates.size(); i++) {
						if (!graph.containsVertex(persIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, persIsolates.get(i), "p"));
							count++;
						}
					}
				}
				System.out.println(graph.countVertexType("p") + " vertices were added.");
				System.out.print("  Creating vertices for categories... ");
				class2 = sc.getStringList("c");
				for (int i = 0; i < sc.getCategoryList().size(); i++) {
					if (!sc.getCategoryList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getCategoryList().get(i), "c"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < catIsolates.size(); i++) {
						if (!graph.containsVertex(catIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, catIsolates.get(i), "c"));
							count++;
						}
					}
				}
				System.out.println(graph.countVertexType("c") + " vertices were added.");
			}
			s_agree = sc.getStringList("a");

			//add bipartite edges
			System.out.print("  Adding bipartite edges... ");
			count = 0;
			for (int i = 0; i < class1.size(); i++) {
				if (!class1.get(i).equals("") && !class2.get(i).equals("")) {
					int sourceId = graph.getVertex(class1.get(i)).getId();
					int targetId = graph.getVertex(class2.get(i)).getId();
					String agree = s_agree.get(i);
					if (!coOccurrencePanel.ignoreDuplicates.isSelected()) { //duplicates are counted, so there can be no multiplexity
						int weight = 0;
						if (agree.equals("yes")) {
							weight = 1;
						} else if (agreementPanel.comb.isSelected()) { //negative weight if combined is selected and agreement=no
							weight = -1;
						} else { //positive weight if combined is not selected but agreement=no
							weight = 1;
						} //the result will be a weighted, signed network; positive values indicate many agreements with statement, negative many disagreements
						if (graph.containsEdge(sourceId, targetId)) {
							graph.getEdge(sourceId, targetId).addToWeight(weight);
						} else {
							graph.addEdge(new DnaGraphEdge(count, weight, graph.getVertex(sourceId), graph.getVertex(targetId)));
							count++;
						}
					} else { //duplicates are ignored, relations are binary
						if (agreementPanel.comb.isSelected()) { //if combined is selected, a multiplex network is created
							if (graph.containsEdge(sourceId, targetId)) {
								if ((graph.getEdge(sourceId, targetId).getWeight() == 1 && agree.equals("no")) || (graph.getEdge(sourceId, targetId).getWeight() == 2 && agree.equals("yes"))) {
									graph.getEdge(sourceId, targetId).setWeight(3); //edge weight = 3 means both
								}
							} else { //add new edge
								if (agree.equals("yes")) { //edge weight = 1 means agreement
									graph.addEdge(new DnaGraphEdge(count, 1, graph.getVertex(sourceId), graph.getVertex(targetId)));
									count++;
								} else { //edge weight = 2 means disagreement
									graph.addEdge(new DnaGraphEdge(count, 2, graph.getVertex(sourceId), graph.getVertex(targetId)));
									count++;
								}
							}
						} else { //yes or no is selected, so there will be no multiplexity
							if (graph.containsEdge(sourceId, targetId)) {
								//do nothing
							} else { //add new edge
								graph.addEdge(new DnaGraphEdge(count, 1, graph.getVertex(sourceId), graph.getVertex(targetId)));
								count++;
							}
						}
					}
				}
			}
			System.out.println(graph.countEdges() + " edges were added. Mean edge weight: " + String.format(new Locale("en"), "%.2f", graph.getMeanWeight()));
		}
		
		public class tabuItem {
			
			int sourceId, targetId;
			String via;
			
			public tabuItem(int sourceId, int targetId, String via) {
				this.sourceId = sourceId;
				this.targetId = targetId;
				this.via = via;
			}

			public int getSourceId() {
				return sourceId;
			}

			public void setSourceId(int sourceId) {
				this.sourceId = sourceId;
			}

			public int getTargetId() {
				return targetId;
			}

			public void setTargetId(int targetId) {
				this.targetId = targetId;
			}

			public String getVia() {
				return via;
			}

			public void setVia(String via) {
				this.via = via;
			}
		}
		
		public class tabuList {
			
			ArrayList<tabuItem> l;
			
			public tabuList() {
				l = new ArrayList<tabuItem>();
			}
			
			public boolean contains(int sourceId, int targetId, String via) {
				boolean condition = false;
				for (int i = 0; i < l.size(); i++) {
					if (l.get(i).getSourceId() == sourceId && l.get(i).getTargetId() == targetId && l.get(i).getVia().equals(via)) {
						condition = true;
					}
				}
				return condition;
			}
			
			public void add(tabuItem t) {
				if (!l.contains(t)) {
					l.add(t);
				}
			}
		}
		
		public void prepareSimpleGraph() {
			System.out.print("  Creating vertices... ");
			
			//create graph
			graph = new DnaGraph();
			graph.removeAllEdges();
			graph.removeAllVertices();
			
			//create vertices and get lists from statementCollection
			class1 = new ArrayList<String>();
			int count = 1;
			if (oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons")) {
				class1 = sc.getStringList("p");
				for (int i = 0; i < sc.getPersonList().size(); i++) {
					if (!sc.getPersonList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getPersonList().get(i), "p"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < persIsolates.size(); i++) {
						if (!graph.containsVertex(persIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, persIsolates.get(i), "p"));
							count++;
						}
					}
					isolatesList = persIsolates;
				}
			} else if (oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations")) {
				class1 = sc.getStringList("o");
				for (int i = 0; i < sc.getOrganizationList().size(); i++) {
					if (!sc.getOrganizationList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getOrganizationList().get(i), "o"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < orgIsolates.size(); i++) {
						if (!graph.containsVertex(orgIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, orgIsolates.get(i), "o"));
							count++;
						}
					}
					isolatesList = orgIsolates;
				}
			} else if (oneModeTypePanel.oneModeCombo.getSelectedItem().equals("categories x categories")) {
				class1 = sc.getStringList("c");
				for (int i = 0; i < sc.getCategoryList().size(); i++) {
					if (!sc.getCategoryList().get(i).equals("")) {
						graph.addVertex(new DnaGraphVertex(count, sc.getCategoryList().get(i), "c"));
						count++;
					}
				}
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					for (int i = 0; i < catIsolates.size(); i++) {
						if (!graph.containsVertex(catIsolates.get(i))) {
							graph.addVertex(new DnaGraphVertex(count, catIsolates.get(i), "c"));
							count++;
						}
					}
					isolatesList = catIsolates;
				}
			}
			
			if (oneModeTypePanel.viaCombo.getSelectedItem().equals("org")) {
				class3 = sc.getStringList("o");
			} else if (oneModeTypePanel.viaCombo.getSelectedItem().equals("pers")) {
				class3 = sc.getStringList("p");
			} else {
				class3 = sc.getStringList("c");
			}
			
			s_agree = sc.getStringList("a");
			s_text = sc.getStringList("t");
			s_date = sc.getDateList();
			System.out.println(graph.countVertices() + " vertices were added.");
		}
		
		/**
		 * Generate a simple graph where edge weights are the number of different co-occurrences.
		 */
		public void generateGraphCoOccurrence() {
			
			prepareSimpleGraph();
			
			System.out.print("  Computing edge weights... ");
			tabuList tl = new tabuList();
			int count = 0;
			if (agreementPanel.conflict.isSelected()) {
				for (int i = 0; i < class1.size(); i++) {
					for (int j = 0; j < class1.size(); j++) {
						if (class3.get(i).equals(class3.get(j))
								&& !class1.get(i).equals(class1.get(j))
								&& !class1.get(i).equals("")
								&& !class1.get(j).equals("")
								&& !class3.get(i).equals("")
								&& !class3.get(j).equals("")
								&& ! s_agree.get(i).equals(s_agree.get(j))) {
							int sourceId = graph.getVertex(class1.get(i)).getId();
							int targetId = graph.getVertex(class1.get(j)).getId();
							if (graph.containsEdge(sourceId, targetId) && !tl.contains(sourceId, targetId, class3.get(i))) {
								graph.getEdge(sourceId, targetId).addToWeight(1);
							} else if (!graph.containsEdge(sourceId, targetId)) {
								graph.addEdge(new DnaGraphEdge(count, 1, graph.getVertex(sourceId), graph.getVertex(targetId)));
							}
							if (coOccurrencePanel.ignoreDuplicates.isSelected()) {
								tl.add(new tabuItem(sourceId, targetId, class3.get(i)));
							}
							count++;
						}
					}
				}
			} else {
				for (int i = 0; i < class1.size(); i++) {
					for (int j = 0; j < class1.size(); j++) {
						if (class3.get(i).equals(class3.get(j))
								&& !class1.get(i).equals(class1.get(j))
								&& !class1.get(i).equals("")
								&& !class1.get(j).equals("")
								&& !class3.get(i).equals("")
								&& !class3.get(j).equals("")
								&& s_agree.get(i).equals(s_agree.get(j))) {
							int sourceId = graph.getVertex(class1.get(i)).getId();
							int targetId = graph.getVertex(class1.get(j)).getId();
							if (graph.containsEdge(sourceId, targetId) && !tl.contains(sourceId, targetId, class3.get(i))) {
								graph.getEdge(sourceId, targetId).addToWeight(1);
							} else if (!graph.containsEdge(sourceId, targetId)) {
								graph.addEdge(new DnaGraphEdge(count, 1, graph.getVertex(sourceId), graph.getVertex(targetId)));
							}
							if (coOccurrencePanel.ignoreDuplicates.isSelected()) {
								tl.add(new tabuItem(sourceId, targetId, class3.get(i)));
							}
							count++;
						}
					}
				}
			}
			System.out.println(graph.countEdges() + " edges with a mean edge weight of " + String.format(new Locale("en"), "%.2f", graph.getMeanWeight()) + " were created.");
			
			//normalization procedure starts here
			if (normalizationPanel.normalization.isSelected()) {
				System.out.print("  Normalization... ");
				for (int i = 0; i < graph.e.size(); i++) {
					sourceCounter = 0;
					targetCounter = 0;
					tabuSource.clear();
					tabuTarget.clear();
					sourceLabel = graph.e.get(i).getSource().getLabel();
					targetLabel = graph.e.get(i).getTarget().getLabel();
					for (int j = 0; j < class1.size(); j++) {
						if (class1.get(j).equals(sourceLabel) && !tabuSource.contains(class3.get(j))) {
							sourceCounter++;
							tabuSource.add(class3.get(j));
						}
						if (class1.get(j).equals(targetLabel) && !tabuTarget.contains(class3.get(j))) {
							targetCounter++;
							tabuTarget.add(class3.get(j));
						}
					}
					graph.e.get(i).setWeight(graph.e.get(i).getWeight() / ((sourceCounter + targetCounter) / 2)); //normalization
				}
				System.out.println("done. Mean edge weight: " + String.format(new Locale("en"), "%.2f", graph.getMeanWeight()) + ", median: " + String.format(new Locale("en"), "%.2f", graph.getMedianWeight()) + ", minimum: " + String.format(new Locale("en"), "%.2f", graph.getMinimumWeight()) + ", maximum: " + String.format(new Locale("en"), "%.2f", graph.getMaximumWeight()) + ".");
			}
		}
		
		/**
		 * Generate simple graph from lists of statements. Run a time window 
		 * through the selected period and aggregate simple graph.
		 */
		public void generateGraphTimeWindow() {
			prepareSimpleGraph();
			
			System.out.print("  Computing edge weights... ");
			
			Double chainD = (Double)timeWindowPanel.chain.getValue();
			int chain = new Double(chainD).intValue();
			Double shiftD = (Double)timeWindowPanel.shift.getValue();
			int shift = new Double(shiftD).intValue();
			
			int runningCount = 0;
			GregorianCalendar startOfPeriod = (GregorianCalendar)start.clone();
			startOfPeriod.add(Calendar.DATE, -chain);
			GregorianCalendar endOfPeriod = (GregorianCalendar)start.clone();
			while (!startOfPeriod.after(stop)) {
				for (int i = 0; i < class1.size(); i++) {
					for (int j = 0; j < class1.size(); j++) {
						if (class3.get(i).equals(class3.get(j))
								&& !class1.get(i).equals(class1.get(j))
								&& s_agree.get(i).equals(s_agree.get(j))
								&& !startOfPeriod.after(s_date.get(i))
								&& !s_date.get(i).after(endOfPeriod)
								&& !startOfPeriod.after(s_date.get(j))
								&& !s_date.get(j).after(endOfPeriod)
								&& !class1.get(i).equals("")
								&& !class1.get(j).equals("")
								&& !class3.get(i).equals("")
								&& !class3.get(j).equals("")) {
							int sourceId = graph.getVertex(class1.get(i)).getId();
							int targetId = graph.getVertex(class1.get(j)).getId();
							if (graph.containsEdge(sourceId, targetId)) {
								graph.getEdge(sourceId, targetId).addToWeight(1);
							} else {
								graph.addEdge(new DnaGraphEdge(runningCount, 1, graph.getVertex(sourceId), graph.getVertex(targetId)));
							}
							runningCount++;
						}
					}
				}
				startOfPeriod.add(Calendar.DATE, shift);
				endOfPeriod.add(Calendar.DATE, shift);
			}
			System.out.println(graph.countEdges() + " edges with a mean edge weight of " + String.format(new Locale("en"), "%.2f", graph.getMeanWeight()) + " were created.");
			
			//time window normalization procedure starts here
			if (normalizationPanel.normalization.isSelected()) {
				System.out.print("  Normalization... ");
				for (int i = 0; i < graph.e.size(); i++) {
					sourceCounter = 0;
					targetCounter = 0;
					tabuSource.clear();
					tabuTarget.clear();
					sourceLabel = graph.e.get(i).getSource().getLabel();
					targetLabel = graph.e.get(i).getTarget().getLabel();
					for (int j = 0; j < class1.size(); j++) {
						if (class1.get(j).equals(sourceLabel) && !tabuSource.contains(class3.get(j))) {
							sourceCounter++;
							tabuSource.add(class3.get(j));
						}
						if (class1.get(j).equals(targetLabel) && !tabuTarget.contains(class3.get(j))) {
							targetCounter++;
							tabuTarget.add(class3.get(j));
						}
					}
					double numerator = graph.e.get(i).getWeight();
					double denom1 = (sourceCounter + targetCounter) / 2;
					double denom2 = (datePanel.getDuration() + chain) / shift;
					graph.e.get(i).setWeight(numerator * 100 / (denom1 * denom2)); //normalization
				}
				System.out.println("done. Mean edge weight: " + String.format(new Locale("en"), "%.2f", graph.getMeanWeight()) + ", median: " + String.format(new Locale("en"), "%.2f", graph.getMedianWeight()) + ", minimum: " + String.format(new Locale("en"), "%.2f", graph.getMinimumWeight()) + ", maximum: " + String.format(new Locale("en"), "%.2f", graph.getMaximumWeight()) + ".");
			}
		}
		
		/**
		 * Generate a simple graph using the attenuation algorithm.
		 */
		public void generateGraphAttenuation() {
			prepareSimpleGraph();
			
			ArrayList<String> concepts = sc.getCategoryList();
			
			System.out.println("  Total number of concepts: " + concepts.size());
			
			ActorList actorList = new ActorList();
			for (int i = 0; i < class1.size(); i++) {
				if (!actorList.containsActor(class1.get(i)) && !class1.get(i).equals("")) {
					actorList.add(new Actor(class1.get(i)));
				}
			}
			
			System.out.println("  Total number of actors: " + actorList.size());
			
			for (int i = 0; i < actorList.size(); i++) {
				for (int j = 0; j < concepts.size(); j++) {
					actorList.get(i).addConcept(new Concept(concepts.get(j)));
				}
			}
			
			double[][][] referrals = new double[actorList.size()][actorList.size()][2];
			for (int i = 0; i < actorList.size(); i++) {
				for (int j = 0; j < actorList.size(); j++) {
					for (int k = 0; k < 1; k++) {
						referrals[i][j][k] = 0;
					}
				}
			}
			int m;
			int n;
			long time;
			double days;
			String agree;
			
			for (int l = 0; l < 2; l++) {
				
				if (l == 0) {
					agree = "yes";
					System.out.print("  Computing positive edge weights... ");
				} else {
					agree = "no";
					System.out.print("  Computing negative edge weights... ");
				}
				
				for (int i = 0; i < actorList.size(); i++) {
					for (int j = 0; j < actorList.get(i).conceptList.size(); j++) {
						actorList.get(i).conceptList.get(j).dateList.clear();
					}
				}
				
				for (int i = 0; i < actorList.size(); i++) {
					for (int j = 0; j < actorList.get(i).conceptList.size(); j++) {
						for (int k = 0; k < class3.size(); k++) {
							if (actorList.get(i).getId().equals(class1.get(k)) && actorList.get(i).conceptList.get(j).getName().equals(class3.get(k)) && s_agree.get(k).equals(agree)) {
								actorList.get(i).conceptList.get(j).addDate(s_date.get(k));
							}
						}
					}	
				}
				
				for (int i = 0; i < actorList.size(); i++) {
					for (int j = 0; j < actorList.get(0).conceptList.size(); j++) {
						Collections.sort(actorList.get(i).conceptList.get(j).dateList);
					}
				}
				
				//the actual algorithm follows
				for (int i = 0; i < actorList.size(); i++) {
					for (int j = 0; j < actorList.size(); j++) {
						for (int k = 0; k < concepts.size(); k++) {
							if (i != j && actorList.get(i).conceptList.get(k).dateList.size() > 0 && actorList.get(j).conceptList.get(k).dateList.size() > 0) {
								m = 0;
								n = 0;
								while (m < actorList.get(i).conceptList.get(k).dateList.size()) {
									if (n + 1 >= actorList.get(j).conceptList.get(k).dateList.size()) {
										if (actorList.get(i).conceptList.get(k).dateList.get(m).after(actorList.get(j).conceptList.get(k).dateList.get(n))) {
											time = actorList.get(i).conceptList.get(k).dateList.get(m).getTime().getTime() - actorList.get(j).conceptList.get(k).dateList.get(n).getTime().getTime();
											days = (double)Math.round( (double)time / (24. * 60.*60.*1000.) );
											if (days == 0) {
												days = 1;
											}
											if (normalizationPanel.normalization.isSelected()) {
												referrals[i][j][l] = referrals[i][j][l] + ((1/days) / actorList.get(i).conceptList.get(k).dateList.size());
											} else {
												referrals[i][j][l] = referrals[i][j][l] + 1/days; //without normalization
											}
										}
										m++;
									} else if (actorList.get(i).conceptList.get(k).dateList.get(m).after(actorList.get(j).conceptList.get(k).dateList.get(n)) && ! actorList.get(i).conceptList.get(k).dateList.get(m).after(actorList.get(j).conceptList.get(k).dateList.get(n + 1))) {
										time = actorList.get(i).conceptList.get(k).dateList.get(m).getTime().getTime() - actorList.get(j).conceptList.get(k).dateList.get(n).getTime().getTime();
										days = (int)Math.round( (double)time / (24. * 60.*60.*1000.) );
										if (days == 0) {
											days = 1;
										}
										if (normalizationPanel.normalization.isSelected()) {
											referrals[i][j][l] = referrals[i][j][l] + ((1/days) / actorList.get(i).conceptList.get(k).dateList.size());
										} else {
											referrals[i][j][l] = referrals[i][j][l] + 1/days; //without normalization
										}
										m++;
									} else if (actorList.get(i).conceptList.get(k).dateList.get(m).after(actorList.get(j).conceptList.get(k).dateList.get(n + 1))) {
										n++;
									} else {
										m++;
									}
								}
							}
						}
					}
					
				}
				System.out.println("done.");
			}
			
			System.out.print("  Assembling graph from edge-weight matrix... ");
			int count = 0;
			if (agreementPanel.yes.isSelected()) {
				for (int i = 0; i < referrals.length; i++) {
					for (int j = 0; j < referrals[0].length; j++) {
						if (referrals[i][j][0] > 0) {
							graph.addEdge(new DnaGraphEdge(count, referrals[i][j][0], graph.getVertex(actorList.get(i).getId()), graph.getVertex(actorList.get(j).getId())));
							count++;
						}
					}
				}
			} else if (agreementPanel.no.isSelected()) {
				for (int i = 0; i < referrals.length; i++) {
					for (int j = 0; j < referrals[0].length; j++) {
						if (referrals[i][j][1] > 0) {
							graph.addEdge(new DnaGraphEdge(count, referrals[i][j][1], graph.getVertex(actorList.get(i).getId()), graph.getVertex(actorList.get(j).getId())));
							count++;
						}
					}
				}
			} else if (agreementPanel.comb.isSelected()) {
				for (int i = 0; i < referrals.length; i++) {
					for (int j = 0; j < referrals[0].length; j++) {
						if (referrals[i][j][0] + referrals[i][j][1] > 0) {
							graph.addEdge(new DnaGraphEdge(count, (referrals[i][j][0] + referrals[i][j][1]), graph.getVertex(actorList.get(i).getId()), graph.getVertex(actorList.get(j).getId())));
							count++;
						}
					}
				}
			}
			System.out.println("done.");
			System.out.println("  " + graph.countVertices() + " vertices and " + graph.countEdges() + " edges with a mean edge weight of " + String.format(new Locale("en"), "%.2f", graph.getMeanWeight()) + ".");
		}
		
		/**
		 * This class represents a list of actors. It is needed for the attenuation algorithm.
		 */
		class ActorList extends ArrayList<Actor> {
			
			public boolean containsActor(String id) {
				boolean flag = false;
				for (int i = 0; i < this.size(); i++) {
					if (this.get(i).getId().equals(id)) {
						flag = true;
					}
				}
				return flag;
			}
			
			public Actor getActor(String id) {
				int count = -1;
				for (int i = 0; i < this.size(); i++) {
					if (this.get(i).getId().equals(id)) {
						count = i;
					}
				}
				return this.get(count);
			}
		}
		
		/**
		 * This class represents an actor. It is needed for the attenuation algorithm.
		 */
		class Actor {
			String id;
			ArrayList<Concept> conceptList;
			
			public Actor(String id) {
				this.id = id;
				conceptList = new ArrayList<Concept>();
			}
			
			public void addConcept(Concept concept) {
				conceptList.add(concept);
			}
			
			public boolean containsConcept(String id) {
				boolean flag = false;
				for (int i = 0; i < conceptList.size(); i++) {
					if (conceptList.get(i).getName().equals(id)) {
						flag = true;
					}
				}
				return flag;
			}
			
			public String getId() {
				return id;
			}
			
			public Concept getConcept(String id) {
				int count = -1;
				for (int i = 0; i < conceptList.size(); i++) {
					if (conceptList.get(i).getName().equals(id)) {
						count = i;
					}
				}
				return conceptList.get(count);
			}
		}
		
		/**
		 * This class represents a concept. It is needed for the attenuation algorithm.
		 */
		class Concept {
			ArrayList<GregorianCalendar> dateList;
			String name;
			
			public Concept(String name) {
				dateList = new ArrayList<GregorianCalendar>();
				this.name = name;
			}
			
			public void addDate(GregorianCalendar date) {
				dateList.add(date);
			}
			
			public String getName() {
				return name;
			}
		}
		
		/**
		 * This class represents a single edge and its duration for the SoNIA algorithm.
		 */
		public class SoniaSlice {
			
			double startTime;
			double endTime;
			int strength;
			
			public SoniaSlice(double start, double end, int strength) {
				this.startTime = start;
				this.endTime = end;
				this.strength = strength;
			}
			
		}
		
		/**
		 * This class represents a dyad and its edges for the SoNIA algorithm.
		 */
		public class SoniaDyad {
			
			ArrayList<SoniaSlice> slices = new ArrayList<SoniaSlice>();
			ArrayList<SoniaSlice> reducedSlices = new ArrayList<SoniaSlice>();
			
			public SoniaDyad addSlice(double start, double end) {
				slices.add(new SoniaSlice(start, end, 1));
				return this;
			}
			
			public double getNextPoint(double currentPoint) {
				ArrayList<Double> nextList = new ArrayList<Double>();
				for (int i = 0; i < slices.size(); i++) {
					nextList.add(slices.get(i).startTime);
					nextList.add(slices.get(i).endTime);
				}
				Collections.sort(nextList);
				double nextValue = nextList.get(nextList.size()-1);
				for (int i = nextList.size() - 1; i >= 0; i--) {
					if (nextList.get(i) > currentPoint) {
						nextValue = nextList.get(i);
					}
				}
				return nextValue;
			}
			
			public boolean isNextEndPoint(double currentPoint) {
				double temp = currentPoint;
				boolean end = true;
				try {
					temp = slices.get(0).endTime;
				} catch (NullPointerException npe) {}
				for (int i = 0; i < slices.size(); i++) {
					if (slices.get(i).startTime > currentPoint && slices.get(i).endTime <= temp) {
						temp = slices.get(i).startTime;
						end = false;
					} else if (slices.get(i).endTime > currentPoint && slices.get(i).endTime <= temp) {
						temp = slices.get(i).endTime;
						end = true;
					}
				}
				return end;
			}
			
			public int numberOfEndsAtPoint(double currentPoint) {
				int counter = 0;
				for (int i = 0; i < slices.size(); i++) {
					if (slices.get(i).endTime == currentPoint) {
						counter++;
					}
				}
				return counter;
			}
			
			public int numberOfStartsAtPoint(double currentPoint) {
				int counter = 0;
				for (int i = 0; i < slices.size(); i++) {
					if (slices.get(i).startTime == currentPoint) {
						counter++;
					}
				}
				return counter;
			}
			
			public boolean hasLaterPoint(double point) {
				for (int i = 0; i < slices.size(); i++) {
					if (slices.get(i).startTime > point || slices.get(i).endTime > point) {
						return true;
					}
				}
				return false;
			}
			
			public double getFirstPoint() {
				if (slices.size() > 0) {
					double p = slices.get(0).startTime;
					for (int i = 0; i < slices.size(); i++) {
						if (slices.get(i).startTime < p) {
							p = slices.get(i).startTime;
						}
					}
					return p;
				} else {
					return 0.0;
				}
			}
			
			public double getLastPoint() {
				if (slices.size() > 0) {
					double p = slices.get(0).endTime;
					for (int i = 0; i < slices.size(); i++) {
						if (slices.get(i).endTime > p) {
							p = slices.get(i).endTime;
						}
					}
					return p;
				} else {
					return 2099.0;
				}
			}
			
			public SoniaDyad reduceSlices() {
				
				reducedSlices.clear();
				double firstPoint = getFirstPoint();
				int strength = numberOfStartsAtPoint(getFirstPoint());
				double secondPoint;
				while (hasLaterPoint(firstPoint) == true) {
					secondPoint = getNextPoint(firstPoint);
					reducedSlices.add(new SoniaSlice(firstPoint, secondPoint, strength));
					strength = strength + numberOfStartsAtPoint(secondPoint) - numberOfEndsAtPoint(secondPoint);
					firstPoint = secondPoint;
				}
				for (int i = reducedSlices.size() - 1; i >= 0 ; i--) {
					if (reducedSlices.get(i).strength == 0) {
						reducedSlices.remove(i);
					}
				}
				return this;
			}
		}
		
		//convert a date into double
		public double timeToDouble(GregorianCalendar cal) {
			int days = 364;
			if (cal.isLeapYear(cal.get(Calendar.YEAR)) == true) {
				days = 365;
			}
			double dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			double year = cal.get(Calendar.YEAR);
			double date = year + (dayOfYear / days);
			return date;
		}
		
		/**
		 * SoNIA export filter.
		 * 
		 * @param name of the output file
		 */
		public void exportFilterSonia (String outfile) {
			
			double forwardDays = (Double) soniaPanel.forwardWindow.getValue();
			
			prepareSimpleGraph();
			
			System.out.print("  Computing SoNIA graph... ");
			
			ArrayList<String> actors = new ArrayList<String>();
			for (int i = 0; i < class1.size(); i++) {
				if (!actors.contains(class1.get(i)) && !class1.get(i).equals("")) {
					actors.add(class1.get(i));
				}
			}
			SoniaDyad[][] matrix = new SoniaDyad[actors.size()][actors.size()];
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					matrix[i][j] = new SoniaDyad();
				}
			}
			
			Double daysD = (Double) soniaPanel.backwardModel.getValue();
			int days = new Double(daysD).intValue();
			double duration;
			for (int i = 0; i < class1.size(); i++) {
				for (int j = 0; j < class1.size(); j++) {
					if (class3.get(i).equals(class3.get(j))
							&& !class1.get(i).equals(class1.get(j))
							&& !class1.get(i).equals("")
							&& !class1.get(j).equals("")
							&& !class3.get(i).equals("")
							&& !class3.get(j).equals("")) {
						if ((agreementPanel.conflict.isSelected() && !s_agree.get(i).equals(s_agree.get(j))) || (!agreementPanel.conflict.isSelected() && s_agree.get(i).equals(s_agree.get(j)))) {
							duration = (s_date.get(i).getTimeInMillis() - s_date.get(j).getTimeInMillis()) / (24*60*60*1000) + 1;
							if (0 <= duration && duration <= days) {
								int firstIndex = 0;
								int secondIndex = 0;
								for (int k = 0; k < actors.size(); k++) {
									if (actors.get(k).equals(class1.get(i))) {
										firstIndex = k;
									} else if (actors.get(k).equals(class1.get(j))) {
										secondIndex = k;
									}
								}
								double startD = timeToDouble(s_date.get(i));
								boolean leap = s_date.get(i).isLeapYear(s_date.get(i).get(Calendar.YEAR));
								double leapDays;
								if (leap == true) {
									leapDays = 365.0;
								} else {
									leapDays = 364.0;
								}
								double forwardWindow = forwardDays / leapDays;
								double endD = startD + forwardWindow;
								matrix[firstIndex][secondIndex] = matrix[firstIndex][secondIndex].addSlice(startD, endD);
							}
						}
					}
				}
			}
			int counter = 0;
			for (int i = 0; i < actors.size(); i++) {
				for (int j = 0; j < actors.size(); j++) {
					if (i != j) {
						matrix[i][j] = matrix[i][j].reduceSlices();
						counter = counter + matrix[i][j].reducedSlices.size();
					}
				}
			}
			System.out.println("A dynamic graph with " + actors.size() + " vertices and " + counter + " weighted edges was generated.");
			
			System.out.print("  Adjusting time line... ");
			HashMap<String,Double> vertexStart = new HashMap<String,Double>();
			HashMap<String,Double> vertexEnd = new HashMap<String,Double>();
			for (int i = 0; i < s_date.size(); i++) {
				if (!vertexStart.containsKey(class1.get(i)) || timeToDouble(s_date.get(i)) < vertexStart.get(class1.get(i))) {
					vertexStart.put(class1.get(i), timeToDouble(s_date.get(i)));
				}
				if (!vertexEnd.containsKey(class1.get(i)) || timeToDouble(s_date.get(i)) > vertexEnd.get(class1.get(i))) {
					boolean leap = s_date.get(i).isLeapYear(s_date.get(i).get(Calendar.YEAR));
					double leapDays;
					if (leap == true) {
						leapDays = 365.0;
					} else {
						leapDays = 364.0;
					}
					double forwardWindow = forwardDays / leapDays;
					double backwardWindow = (Double)soniaPanel.backwardWindow.getValue() / leapDays;
					vertexEnd.put(class1.get(i), (timeToDouble(s_date.get(i)) + forwardWindow + backwardWindow));
				}
			}
			System.out.println("done.");
			
			try {
				System.out.print("  Writing data to disk... ");
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
				out.write("NodeId\tLabel\tStartTime\tEndTime\tNodeSize\tNodeShape\tColorName\tBorderWidth\tBorderColor");
				out.newLine();
				for (int i = 0; i < actors.size(); i++) {
					out.write((i + 1) + "\t" + actors.get(i) + "\t" + vertexStart.get(actors.get(i)) + "\t" + vertexEnd.get(actors.get(i)) + "\t15\tellipse\torange\t1.5\tblack");
					out.newLine();
				}
				out.write("FromId\tToId\tStartTime\tEndTime\tArcWeight\tArcWidth\tColorName");
				for (int i = 0; i < actors.size(); i++) {
					for (int j = 0; j < actors.size(); j++) {
						for (int k = 0; k < matrix[i][j].reducedSlices.size(); k++) {
							out.newLine();
							out.write((i + 1) + "\t" + (j + 1) + "\t" + matrix[i][j].reducedSlices.get(k).startTime + "\t" + matrix[i][j].reducedSlices.get(k).endTime + "\t" + matrix[i][j].reducedSlices.get(k).strength + "\t" + matrix[i][j].reducedSlices.get(k).strength + "\tblack");
						}
					}
				}
				out.close();
				System.out.println("File has been exported to \"" + outfile + "\".");
			} catch (IOException e) {
				System.err.println("Error while saving SoNIA file.");
			}
		}
		
		/**
		 * Commetrix SQL export filter.
		 * 
		 * @param name of the output file
		 */
		public void exportFilterCommetrix (String outfile) {
			String networkSubName = commetrixPanel.networkName.getText();
			if (networkSubName.equals("")) {
				networkSubName = "DNA_CMX";
			}
			String nodeName = "Name";
			String nodeNumber = "Number";
			String nodeNameDescription = nodeName;
			String nodeNumberDescription = nodeNumber;
			if (oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons")) {
				nodeNameDescription = "Name of the person.";
				nodeNumberDescription = "Person number.";
			} else if (oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations")) {
				nodeNameDescription = "Name of the organization.";
				nodeNumberDescription = "Organization number.";
			} else if (oneModeTypePanel.oneModeCombo.getSelectedItem().equals("categories x categories")) {
				nodeNameDescription = "Name of the category.";
				nodeNumberDescription = "Category number.";
			}
			
			prepareSimpleGraph();
			System.out.print("  Computing Commetrix graph... ");
			int counter = 0;
			Double daysD = (Double)commetrixPanel.chain.getValue();
			int days = new Double(daysD).intValue();
			double duration;
			for (int i = 0; i < class1.size(); i++) {
				for (int j = 0; j < class1.size(); j++) {
					if (class3.get(i).equals(class3.get(j))
							&& !class1.get(i).equals(class1.get(j))
							&& !class1.get(i).equals("")
							&& !class1.get(j).equals("")
							&& !class3.get(i).equals("")
							&& !class3.get(j).equals("")) {
						if ((agreementPanel.conflict.isSelected() && !s_agree.get(i).equals(s_agree.get(j))) || (!agreementPanel.conflict.isSelected() && s_agree.get(i).equals(s_agree.get(j)))) {
							duration = (s_date.get(i).getTimeInMillis() - s_date.get(j).getTimeInMillis()) / (24*60*60*1000) + 1;
							if (0 <= duration && duration <= days) {
								counter++;
								//graph.addEdge(new DnaGraphEdge(counter, 1, graph.getVertex(class1.get(i)), graph.getVertex(class1.get(j)), s_date.get(i), class3.get(i), s_text.get(i)));
								//substituted for an edge without text detail because memory consumption was too high in MySQL:
								graph.addEdge(new DnaGraphEdge(counter, 1, graph.getVertex(class1.get(i)), graph.getVertex(class1.get(j)), s_date.get(i), class3.get(i), "null"));
							}
						}
					}
				}
			}
			System.out.println("A dynamic graph with " + graph.v.size() + " vertices and " + graph.e.size() + " binary edges was generated.");
			
			try {
				System.out.print("  Writing data to disk... ");
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
				out.write("-- Commetrix SQL file.");
				out.newLine();
				out.write("-- Produced by Discourse Network Analyzer (DNA).");
				out.newLine();
				out.write("-- http://www.philipleifeld.de");
				out.newLine();
				out.newLine();
				out.newLine();
				out.newLine();
				out.write("-- Data for table `network`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `network` (`networkid`,`ElementTypeID`,`Name`,`Detail1`,`Detail2`,`Detail3`,`Detail4`,`Detail5`) VALUES ");
				out.newLine();
				out.write(" (1,0,'" + networkSubName + "','" + networkSubName + "',NULL,NULL,NULL,NULL);");
				out.newLine();
				out.newLine();
				out.newLine();
				out.write("-- Data for table `node`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `node` (`nodeID`,`networkid`,`AliasID`,`Detail1`,`Detail2`,`Detail3`,`Detail4`,`Detail5`) VALUES ");
				
				for (int i = 0; i < graph.v.size(); i++) {
					out.newLine();
					out.write(" (" + graph.v.get(i).getId() + ",1,0,'" + graph.v.get(i).getLabel() + "','" + graph.v.get(i).getId() + "','null','null','null')");
					if (i == graph.v.size() - 1) {
						out.write(";");
					} else {
						out.write(",");
					}
				}
				
				out.newLine();
				out.newLine();
				out.newLine();
				out.write("-- Data for table `linkevent`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `linkevent` (`linkeventID`,`networkid`,`LinkeventDate`,`Subject`,`Content`,`Detail1`,`Detail2`,`Detail3`,`Detail4`,`Detail5`) VALUES"); 
				
				SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				for (int i = 0; i < graph.e.size(); i++) {
					out.newLine();
					out.write(" (" + graph.e.get(i).getId() + ",1,'" + df.format(graph.e.get(i).getDate().getTime()) + "','" + graph.e.get(i).getCategory() + "','" + graph.e.get(i).getDetail() + "','null','null','null','null','null')");
					if (i == graph.e.size() - 1) {
						out.write(";");
					} else {
						out.write(",");
					}
				}
				
				out.newLine();
				out.newLine();
				out.newLine();
				out.write("-- Data for table `linkeventsender`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `linkeventsender` (`linkeventID`,`senderNodeID`,`networkid`) VALUES ");
				
				for (int i = 0; i < graph.e.size(); i++) {
					out.newLine();
					out.write(" (" + graph.e.get(i).getId() + "," + graph.e.get(i).getSource().getId() + ",1)"); 
					if (i == graph.e.size() - 1) {
						out.write(";");
					} else {
						out.write(",");
					}
				}
				
				out.newLine();
				out.newLine();
				out.newLine();
				out.write("-- Data for table `linkeventrecipient`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `linkeventrecipient` (`linkeventID`,`recipientNodeID`,`networkID`) VALUES ");
				
				for (int i = 0; i < graph.e.size(); i++) {
					out.newLine();
					out.write(" (" + graph.e.get(i).getId() + "," + graph.e.get(i).getTarget().getId() + ",1)"); 
					if (i == graph.e.size() - 1) {
						out.write(";");
					} else {
						out.write(",");
					}
				}
				
				out.newLine();
				out.newLine();
				
				out.newLine();
				out.write("-- Metadata for table `network`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `networkdetailconfig` (`detail`,`label`,`description`,`isReadOnly`,`networkid`,`isSize`,`isColor`,`isNumeric`) VALUES");
				out.newLine();
				out.write(" (1,'" + networkSubName + "','" + networkSubName + "',1,1,0,0,0);");
				out.newLine();
				out.newLine();
				out.write("-- Metadata for table `node`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `nodedetailconfig` (`detail`,`label`,`description`,`isReadOnly`,`isColor`,`isSize`,`isNumeric`,`networkid`) VALUES");
				out.newLine();
				out.write(" (1,'" + nodeName + "','" + nodeNameDescription +"',1,1,0,0,1),");
				out.newLine();
				out.write(" (2,'" + nodeNumber + "','" + nodeNumberDescription + "',1,1,0,1,1);");
				out.newLine();
				out.newLine();
				out.write("-- Metadata for table `linkevent`");
				out.newLine();
				out.newLine();
				out.write("INSERT INTO `linkeventdetailconfig` (`detail`,`label`,`description`,`isReadOnly`,`networkid`,`isSize`,`isColor`,`isNumeric`) VALUES");
				out.newLine();
				out.write(" (2,'numeric test','numeric test values',0,1,1,1,1),");
				out.newLine();
				out.write(" (3,'alphanum test','alphanumeric test values',0,1,0,1,0);");
				out.newLine();
				out.newLine();
				
				out.close();
				System.out.println("File has been exported to \"" + outfile + "\".");
			} catch (IOException e) {
				System.err.println("Error while saving Commetrix SQL file.");
			}
		}
		
		/**
		 * CSV affiliation list export filter.
		 * 
		 * @param name of the output file
		 */
		public void exportCsvAffiliationList (String outfile) {
			System.out.print("  Writing data to disk... ");
			@SuppressWarnings("unused")
			int nc1 = 0;
			@SuppressWarnings("unused")
			int nc2 = 0;
			ArrayList<String> c1list;
			@SuppressWarnings("unused")
			ArrayList<String> c2list;
			if (twoModeTypePanel.oc.isSelected()) {
				nc1 = graph.countVertexType("o");
				nc2 = graph.countVertexType("c");
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					c1list = orgIsolates;
					c2list = catIsolates;
				} else {
					c1list = sc.getOrganizationList();
					c2list = sc.getCategoryList();
				}
			} else if (twoModeTypePanel.po.isSelected()) {
				nc1 = graph.countVertexType("p");
				nc2 = graph.countVertexType("o");
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					c1list = persIsolates;
					c2list = orgIsolates;
				} else {
					c1list = sc.getPersonList();
					c2list = sc.getOrganizationList();
				}
			} else {
				nc1 = graph.countVertexType("p");
				nc2 = graph.countVertexType("c");
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					c1list = persIsolates;
					c2list = catIsolates;
				} else {
					c1list = sc.getPersonList();
					c2list = sc.getCategoryList();
				}
			}
			
			try {
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
				for (int i = 0; i < c1list.size(); i++) {
					out.write(c1list.get(i));
					for (int j = 0; j < graph.e.size(); j++) {
						if (graph.e.get(j).getSource().getLabel().equals(c1list.get(i))) {
							out.write(";" + graph.e.get(j).getTarget().getLabel());
						}
					}
					out.write("\n");
				}
				out.close();
				System.out.println("File has been exported to \"" + outfile + "\".");
			} catch (IOException e) {
				System.err.println("Error while saving CSV matrix file.");
			}
		}
		
		/**
		 * CSV matrix export filter.
		 * 
		 * @param name of the output file
		 */
		public void exportCsvMatrix (String outfile) {
			System.out.print("  Writing data to disk... ");
			if (!algorithmPanel.affil.isSelected()) {
				int nc1 = graph.countVertices();
				double[][] csvmat = new double[nc1][nc1];
				
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					class1 = isolatesList;
				}
				
				ArrayList<String> c1list = new ArrayList<String>();
				for (int i = 0; i < class1.size(); i++) {
					if (!c1list.contains(class1.get(i)) && !class1.get(i).equals("")) {
						c1list.add(class1.get(i));
					}
				}
				
				for (int e = 0; e < graph.e.size(); e++) {
					for (int i = 0; i < nc1; i++) {
						for (int j = 0; j < nc1; j++) {
							if (c1list.get(i).equals(graph.e.get(e).source.getLabel()) && c1list.get(j).equals(graph.e.get(e).target.getLabel())) {
								csvmat[i][j] = graph.e.get(e).getWeight();
							}
						}
					}
				}
				
				try {
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
					out.write("Label");
					for (int i = 0; i < nc1; i++) {
						out.write(";" + c1list.get(i));
					}
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write(c1list.get(i));
						for (int j = 0; j < nc1; j++) {
							out.write(";" + String.format(new Locale("en"), "%.6f", csvmat[i][j]));
						}
					}
					out.close();
					System.out.println("File has been exported to \"" + outfile + "\".");
				} catch (IOException e) {
					System.err.println("Error while saving CSV matrix file.");
				}
			} else {
				int nc1 = 0;
				int nc2 = 0;
				ArrayList<String> c1list;
				ArrayList<String> c2list;
				if (twoModeTypePanel.oc.isSelected()) {
					nc1 = graph.countVertexType("o");
					nc2 = graph.countVertexType("c");
					if (coOccurrencePanel.includeIsolates.isSelected()) {
						c1list = orgIsolates;
						c2list = catIsolates;
					} else {
						c1list = sc.getOrganizationList();
						c2list = sc.getCategoryList();
					}
				} else if (twoModeTypePanel.po.isSelected()) {
					nc1 = graph.countVertexType("p");
					nc2 = graph.countVertexType("o");
					if (coOccurrencePanel.includeIsolates.isSelected()) {
						c1list = persIsolates;
						c2list = orgIsolates;
					} else {
						c1list = sc.getPersonList();
						c2list = sc.getOrganizationList();
					}
				} else {
					nc1 = graph.countVertexType("p");
					nc2 = graph.countVertexType("c");
					if (coOccurrencePanel.includeIsolates.isSelected()) {
						c1list = persIsolates;
						c2list = catIsolates;
					} else {
						c1list = sc.getPersonList();
						c2list = sc.getCategoryList();
					}
				}
				
				double[][] csvmat = new double[nc1][nc2];
				
				for (int k = 0; k < graph.countEdges(); k++) {
					for (int i = 0; i < nc1; i++) {
						for (int j = 0; j < nc2; j++) {
							DnaGraphVertex sv = graph.e.get(k).getSource();
							String sl = sv.label;
							DnaGraphVertex tv = graph.e.get(k).getTarget();
							String tl = tv.getLabel();
							if (c1list.get(i).equals(sl) && c2list.get(j).equals(tl)) {
								csvmat[i][j] = graph.e.get(k).getWeight();
							}
						}
					}
				}
				
				try {
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
					out.write("Label");
					for (int i = 0; i < nc2; i++) {
						out.write(";" + c2list.get(i));
					}
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write(c1list.get(i));
						for (int j = 0; j < nc2; j++) {
							out.write(";" + new Integer(new Double(csvmat[i][j]).intValue()));
						}
					}
					out.close();
					System.out.println("File has been exported to \"" + outfile + "\".");
				} catch (IOException e) {
					System.err.println("Error while saving CSV matrix file.");
				}
			}
		}
		
		/**
		 * DL fullmatrix export filter.
		 * 
		 * @param name of the output file
		 */
		private void exportDlFullMatrix (String outfile) {
			System.out.print("  Writing data to disk... ");
			if (!algorithmPanel.affil.isSelected()) {
				int nc1 = graph.countVertices();
				double[][] csvmat = new double[nc1][nc1];
				
				if (coOccurrencePanel.includeIsolates.isSelected()) {
					class1 = isolatesList;
				}
				ArrayList<String> c1list = new ArrayList<String>();
				for (int i = 0; i < class1.size(); i++) {
					if (!c1list.contains(class1.get(i)) && !class1.get(i).equals("")) {
						c1list.add(class1.get(i));
					}
				}
				
				for (int e = 0; e < graph.e.size(); e++) {
					for (int i = 0; i < nc1; i++) {
						for (int j = 0; j < nc1; j++) {
							if (c1list.get(i).equals(graph.e.get(e).source.getLabel()) && c1list.get(j).equals(graph.e.get(e).target.getLabel())) {
								csvmat[i][j] = graph.e.get(e).getWeight();
							}
						}
					}
				}
				
				try {
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
					out.write("DL");
					out.newLine();
					out.write("N=" + nc1);
					out.newLine();
					out.write("FORMAT = FULLMATRIX DIAGONAL PRESENT");
					out.newLine();
					out.write("ROW LABELS:");
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write("\"" + c1list.get(i) + "\"");
					}
					out.newLine();
					out.write("ROW LABELS EMBEDDED");
					out.newLine();
					out.write("COLUMN LABELS:");
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write("\"" + c1list.get(i) + "\"");
					}
					out.newLine();
					out.write("COLUMN LABELS EMBEDDED");
					out.newLine();
					out.write("DATA:");
					out.newLine();
					out.write("      ");
					for (int i = 0; i < nc1; i++) {
						out.write(" \"" + c1list.get(i) + "\"");
					}
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write("\"" + c1list.get(i) + "\"");
						for (int j = 0; j < nc1; j++) {
							out.write(" " + String.format(new Locale("en"), "%.6f", csvmat[i][j]));
						}
					}
					out.close();
					System.out.println("File has been exported to \"" + outfile + "\".");
				} catch (IOException e) {
					System.err.println("Error while saving DL matrix file.");
				}
			} else {
				int nc1 = 0;
				int nc2 = 0;
				ArrayList<String> c1list;
				ArrayList<String> c2list;
				if (twoModeTypePanel.oc.isSelected()) {
					nc1 = graph.countVertexType("o");
					nc2 = graph.countVertexType("c");
					if (coOccurrencePanel.includeIsolates.isSelected()) {
						c1list = orgIsolates;
						c2list = catIsolates;
					} else {
						c1list = sc.getOrganizationList();
						c2list = sc.getCategoryList();
					}
				} else if (twoModeTypePanel.po.isSelected()) {
					nc1 = graph.countVertexType("p");
					nc2 = graph.countVertexType("o");
					if (coOccurrencePanel.includeIsolates.isSelected()) {
						c1list = persIsolates;
						c2list = orgIsolates;
					} else {
						c1list = sc.getPersonList();
						c2list = sc.getOrganizationList();
					}
				} else {
					nc1 = graph.countVertexType("p");
					nc2 = graph.countVertexType("c");
					if (coOccurrencePanel.includeIsolates.isSelected()) {
						c1list = persIsolates;
						c2list = catIsolates;
					} else {
						c1list = sc.getPersonList();
						c2list = sc.getCategoryList();
					}
				}
				
				double[][] csvmat = new double[nc1][nc2];
				
				for (int k = 0; k < graph.countEdges(); k++) {
					for (int i = 0; i < nc1; i++) {
						for (int j = 0; j < nc2; j++) {
							DnaGraphVertex sv = graph.e.get(k).getSource();
							String sl = sv.label;
							DnaGraphVertex tv = graph.e.get(k).getTarget();
							String tl = tv.getLabel();
							if (c1list.get(i).equals(sl) && c2list.get(j).equals(tl)) {
								csvmat[i][j] = graph.e.get(k).getWeight();
							}
						}
					}
				}
				
				try {
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
					out.write("DL");
					out.newLine();
					out.write("NR=" + nc1 + ", NC=" + nc2);
					out.newLine();
					out.write("FORMAT = FULLMATRIX DIAGONAL PRESENT");
					out.newLine();
					out.write("ROW LABELS:");
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write("\"" + c1list.get(i) + "\"");
					}
					out.newLine();
					out.write("ROW LABELS EMBEDDED");
					out.newLine();
					out.write("COLUMN LABELS:");
					for (int i = 0; i < nc2; i++) {
						out.newLine();
						out.write("\"" + c2list.get(i) + "\"");
					}
					out.newLine();
					out.write("COLUMN LABELS EMBEDDED");
					out.newLine();
					out.write("DATA:");
					out.newLine();
					out.write("      ");
					for (int i = 0; i < nc2; i++) {
						out.write(" \"" + c2list.get(i) + "\"");
					}
					out.newLine();
					for (int i = 0; i < nc1; i++) {
						out.newLine();
						out.write("\"" + c1list.get(i) + "\"");
						for (int j = 0; j < nc2; j++) {
							out.write(" " + new Integer(new Double(csvmat[i][j]).intValue()));
						}
					}
					out.close();
					System.out.println("File has been exported to \"" + outfile + "\".");
				} catch (IOException e) {
					System.err.println("Error while saving DL matrix file.");
				}
			}
		}
		
		public String colToHex(Color col) {
			String r = Integer.toHexString(col.getRed());
			String g = Integer.toHexString(col.getGreen());
			String b = Integer.toHexString(col.getBlue());
			if (r.equals("0")) {
				r = "00";
			}
			if (g.equals("0")) {
				g = "00";
			}
			if (b.equals("0")) {
				b = "00";
			}
			String hex = "#" + r + g + b;
			return hex;
		}
		
		/**
		 * Export filter for graphML files.
		 * 
		 * @param name of the output file
		 */
		private void graphMl(String outfile) {
			System.out.print("  Writing data to disk... ");
			
			Namespace xmlns = Namespace.getNamespace("http://graphml.graphdrawing.org/xmlns");
			Element graphml = new Element("graphml", xmlns);
			Namespace visone = Namespace.getNamespace("visone", "http://visone.info/xmlns");
	        graphml.addNamespaceDeclaration(visone);
			Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	        graphml.addNamespaceDeclaration(xsi);
	        Namespace yNs = Namespace.getNamespace("y", "http://www.yworks.com/xml/graphml");
	        graphml.addNamespaceDeclaration(yNs);	        
	        Attribute attSchema = new Attribute("schemaLocation", "http://graphml.graphdrawing.org/xmlns/graphml http://www.yworks.com/xml/schema/graphml/1.0/ygraphml.xsd ", xsi);
	        graphml.setAttribute(attSchema);
			Document document = new Document(graphml);
			
			Comment dataSchema = new Comment(" data schema ");
			graphml.addContent(dataSchema);
			
			Element keyVisoneNode = new Element("key", xmlns);
			keyVisoneNode.setAttribute(new Attribute("for", "node"));
			keyVisoneNode.setAttribute(new Attribute("id", "d0"));
			keyVisoneNode.setAttribute(new Attribute("yfiles.type", "nodegraphics"));
			graphml.addContent(keyVisoneNode);

			Element keyVisoneEdge = new Element("key", xmlns);
			keyVisoneEdge.setAttribute(new Attribute("for", "edge"));
			keyVisoneEdge.setAttribute(new Attribute("id", "e0"));
			keyVisoneEdge.setAttribute(new Attribute("yfiles.type", "edgegraphics"));
			graphml.addContent(keyVisoneEdge);

			Element keyVisoneGraph = new Element("key", xmlns);
			keyVisoneGraph.setAttribute(new Attribute("for", "graph"));
			keyVisoneGraph.setAttribute(new Attribute("id", "prop"));
			keyVisoneGraph.setAttribute(new Attribute("visone.type", "properties"));
			graphml.addContent(keyVisoneGraph);
			
			Element keyId = new Element("key", xmlns);
			keyId.setAttribute(new Attribute("id", "id"));
			keyId.setAttribute(new Attribute("for", "node"));
			keyId.setAttribute(new Attribute("attr.name", "id"));
			keyId.setAttribute(new Attribute("attr.type", "string"));
			graphml.addContent(keyId);
			
			if ((algorithmPanel.affil.isSelected()) || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations")) || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons"))) {
				Element keyType = new Element("key", xmlns);
				keyType.setAttribute(new Attribute("id", "type"));
				keyType.setAttribute(new Attribute("for", "node"));
				keyType.setAttribute(new Attribute("attr.name", "type"));
				keyType.setAttribute(new Attribute("attr.type", "string"));
				graphml.addContent(keyType);
				
				Element keyAlias = new Element("key", xmlns);
				keyAlias.setAttribute(new Attribute("id", "alias"));
				keyAlias.setAttribute(new Attribute("for", "node"));
				keyAlias.setAttribute(new Attribute("attr.name", "alias"));
				keyAlias.setAttribute(new Attribute("attr.type", "string"));
				graphml.addContent(keyAlias);
				
				Element keyNote = new Element("key", xmlns);
				keyNote.setAttribute(new Attribute("id", "note"));
				keyNote.setAttribute(new Attribute("for", "node"));
				keyNote.setAttribute(new Attribute("attr.name", "note"));
				keyNote.setAttribute(new Attribute("attr.type", "string"));
				graphml.addContent(keyNote);
			}
			
			Element keyClass = new Element("key", xmlns);
			keyClass.setAttribute(new Attribute("id", "class"));
			keyClass.setAttribute(new Attribute("for", "node"));
			keyClass.setAttribute(new Attribute("attr.name", "class"));
			keyClass.setAttribute(new Attribute("attr.type", "string"));
			graphml.addContent(keyClass);
			
			if (algorithmPanel.affil.isSelected() && coOccurrencePanel.ignoreDuplicates.isSelected() && ! twoModeTypePanel.po.isSelected()) {
				Element keyAttribute = new Element("key", xmlns);
				keyAttribute.setAttribute(new Attribute("id", "agreement"));
				keyAttribute.setAttribute(new Attribute("for", "edge"));
				keyAttribute.setAttribute(new Attribute("attr.name", "agreement"));
				keyAttribute.setAttribute(new Attribute("attr.type", "string"));
				graphml.addContent(keyAttribute);
			} else {
				Element keyWeight = new Element("key", xmlns);
				keyWeight.setAttribute(new Attribute("id", "weight"));
				keyWeight.setAttribute(new Attribute("for", "edge"));
				keyWeight.setAttribute(new Attribute("attr.name", "weight"));
				keyWeight.setAttribute(new Attribute("attr.type", "double"));
				graphml.addContent(keyWeight);
			}
			
			Element graphElement = new Element("graph", xmlns);
			if (algorithmPanel.affil.isSelected()) {
				graphElement.setAttribute(new Attribute("edgedefault", "directed"));
			} else {
				graphElement.setAttribute(new Attribute("edgedefault", "undirected"));
			}
			
			graphElement.setAttribute(new Attribute("id", "G"));
			graphElement.setAttribute(new Attribute("parse.edges", String.valueOf(graph.countEdges())));
			graphElement.setAttribute(new Attribute("parse.nodes", String.valueOf(graph.countVertices())));
			graphElement.setAttribute(new Attribute("parse.order", "free"));
			Element properties = new Element("data", xmlns);
			properties.setAttribute(new Attribute("key", "prop"));
			Element labelAttribute = new Element("labelAttribute", visone);
			if (algorithmPanel.affil.isSelected() && coOccurrencePanel.ignoreDuplicates.isSelected() && ! twoModeTypePanel.po.isSelected()) {
				labelAttribute.setAttribute("edgeLabel", "agreement");
			} else {
				labelAttribute.setAttribute("edgeLabel", "weight");
			}
			labelAttribute.setAttribute("nodeLabel", "id");
			properties.addContent(labelAttribute);
			graphElement.addContent(properties);
			
			Comment nodes = new Comment(" nodes ");
			graphElement.addContent(nodes);
			
			for (int i = 0; i < graph.getVertices().size(); i++) {
				Element node = new Element("node", xmlns);
				node.setAttribute(new Attribute("id", "v" + String.valueOf(graph.getVertices().get(i).getId())));
				
				Element id = new Element("data", xmlns);
				id.setAttribute(new Attribute("key", "id"));
				id.setText(graph.getVertices().get(i).getLabel());
				node.addContent(id);
				
				String org = graph.getVertices().get(i).getLabel();
				String hex = "#000000";
				Color col;
				
				if (graph.getVertices().get(i).getType().equals("o") || 
						graph.getVertices().get(i).getType().equals("p") ||
						(! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations")) ||
						(! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons"))
				) {
					
					Element type = new Element("data", xmlns);
					type.setAttribute(new Attribute("key", "type"));
					
					Element alias = new Element("data", xmlns);
					alias.setAttribute(new Attribute("key", "alias"));
					
					Element note = new Element("data", xmlns);
					note.setAttribute(new Attribute("key", "note"));
					
					String t,a,n;
					
					if (graph.getVertices().get(i).getType().equals("o") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations"))) {
						col = Dna.mainProgram.om.getColor(org);
						hex =  colToHex(col);
						t = Dna.mainProgram.om.getActor(org).getType();
						a = Dna.mainProgram.om.getActor(org).getAlias();
						n = Dna.mainProgram.om.getActor(org).getNote();
					} else {
						col = Dna.mainProgram.pm.getColor(org);
						hex =  colToHex(col);
						t = Dna.mainProgram.pm.getActor(org).getType();
						a = Dna.mainProgram.pm.getActor(org).getAlias();
						n = Dna.mainProgram.pm.getActor(org).getNote();
					}
					
					type.setText(t);
					node.addContent(type);
					alias.setText(a);
					node.addContent(alias);
					note.setText(n);
					node.addContent(note);
				}
				
				Element vClass = new Element("data", xmlns);
				vClass.setAttribute(new Attribute("key", "class"));
				if (graph.getVertices().get(i).getType().equals("c") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("categories x categories"))) {
					vClass.setText("concept");
				} else if (graph.getVertices().get(i).getType().equals("p") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons"))) {
					vClass.setText("person");
				} else if (graph.getVertices().get(i).getType().equals("o") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations"))) {
					vClass.setText("organization");
				}
				node.addContent(vClass);
				
				Element vis = new Element("data", xmlns);
				vis.setAttribute(new Attribute("key", "d0"));
				Element visoneShapeNode = new Element("shapeNode", visone);
				Element yShapeNode = new Element("ShapeNode", yNs);
				Element geometry = new Element("Geometry", yNs);
				geometry.setAttribute(new Attribute("height", "20.0"));
				geometry.setAttribute(new Attribute("width", "20.0"));
				geometry.setAttribute(new Attribute("x", String.valueOf(Math.random()*800)));
				geometry.setAttribute(new Attribute("y", String.valueOf(Math.random()*600)));
				yShapeNode.addContent(geometry);
				Element fill = new Element("Fill", yNs);
				if (graph.getVertices().get(i).getType().equals("o") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations"))) {
					fill.setAttribute(new Attribute("color", hex));
				} else if (graph.getVertices().get(i).getType().equals("c") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("categories x categories"))) {
					fill.setAttribute(new Attribute("color", "#3399FF")); //light blue
				} else if (graph.getVertices().get(i).getType().equals("p") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons"))) {
					fill.setAttribute(new Attribute("color", hex));
				}
				fill.setAttribute(new Attribute("transparent", "false"));
				yShapeNode.addContent(fill);
				Element borderStyle = new Element("BorderStyle", yNs);
				borderStyle.setAttribute(new Attribute("color", "#000000"));
				borderStyle.setAttribute(new Attribute("type", "line"));
				borderStyle.setAttribute(new Attribute("width", "1.0"));
				yShapeNode.addContent(borderStyle);
				
				Element nodeLabel = new Element("NodeLabel", yNs);
				nodeLabel.setAttribute(new Attribute("alignment", "center"));
				nodeLabel.setAttribute(new Attribute("autoSizePolicy", "content"));
				nodeLabel.setAttribute(new Attribute("backgroundColor", "#FFFFFF"));
				nodeLabel.setAttribute(new Attribute("fontFamily", "Dialog"));
				nodeLabel.setAttribute(new Attribute("fontSize", "12"));
				nodeLabel.setAttribute(new Attribute("fontStyle", "plain"));
				nodeLabel.setAttribute(new Attribute("hasLineColor", "false"));
				nodeLabel.setAttribute(new Attribute("height", "19.0"));
				nodeLabel.setAttribute(new Attribute("modelName", "eight_pos"));
				nodeLabel.setAttribute(new Attribute("modelPosition", "n"));
				nodeLabel.setAttribute(new Attribute("textColor", "#000000"));
				nodeLabel.setAttribute(new Attribute("visible", "true"));
				nodeLabel.setText(graph.getVertices().get(i).getLabel());
				yShapeNode.addContent(nodeLabel);
				
				Element shape = new Element("Shape", yNs);
				if (graph.getVertices().get(i).getType().equals("o") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("organizations x organizations"))) {
					shape.setAttribute(new Attribute("type", "ellipse"));
				} else if (graph.getVertices().get(i).getType().equals("c") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("categories x categories"))) {
					shape.setAttribute(new Attribute("type", "roundrectangle"));
				} else if (graph.getVertices().get(i).getType().equals("p") || (! algorithmPanel.affil.isSelected() && oneModeTypePanel.oneModeCombo.getSelectedItem().equals("persons x persons"))) {
					shape.setAttribute(new Attribute("type", "diamond"));
				}
				yShapeNode.addContent(shape);
				visoneShapeNode.addContent(yShapeNode);
				vis.addContent(visoneShapeNode);
				node.addContent(vis);
				
				graphElement.addContent(node);
			}
			
			Comment edges = new Comment(" edges ");
			graphElement.addContent(edges);
			
			for (int i = 0; i < graph.getEdges().size(); i++) {
				Element edge = new Element("edge", xmlns);
				edge.setAttribute(new Attribute("source", "v" + String.valueOf(graph.getEdges().get(i).getSource().getId())));
				edge.setAttribute(new Attribute("target", "v" + String.valueOf(graph.getEdges().get(i).getTarget().getId())));
				if (algorithmPanel.affil.isSelected() && coOccurrencePanel.ignoreDuplicates.isSelected() && ! twoModeTypePanel.po.isSelected()) {
					Element agree = new Element("data", xmlns);
					agree.setAttribute(new Attribute("key", "agreement"));
					if (agreementPanel.yes.isSelected()) {
						agree.setText("yes");
					} else if (agreementPanel.no.isSelected()) {
						agree.setText("yes");
					} else if (agreementPanel.comb.isSelected()) {
						if (graph.getEdges().get(i).getWeight() == 1.0) {
							agree.setText("yes");
						} else if (graph.getEdges().get(i).getWeight() == 2.0) {
							agree.setText("no");
						} else if (graph.getEdges().get(i).getWeight() == 3.0) {
							agree.setText("mixed");
						}
					}
					edge.addContent(agree);
				} else {
					Element weight = new Element("data", xmlns);
					weight.setAttribute(new Attribute("key", "weight"));
					weight.setText(String.valueOf(graph.getEdges().get(i).getWeight()));
					edge.addContent(weight);
				}
				
				Element visEdge = new Element("data", xmlns);
				visEdge.setAttribute("key", "e0");
				Element visPolyLineEdge = new Element("polyLineEdge", visone);
				Element yPolyLineEdge = new Element("PolyLineEdge", yNs);
				Element yLineStyle = new Element("LineStyle", yNs);
				if (algorithmPanel.affil.isSelected() && coOccurrencePanel.ignoreDuplicates.isSelected() && ! twoModeTypePanel.po.isSelected()) {
					if (agreementPanel.yes.isSelected()) {
						yLineStyle.setAttribute("color", "#00ff00");
					} else if (agreementPanel.no.isSelected()) {
						yLineStyle.setAttribute("color", "#ff0000");
					} else if (agreementPanel.comb.isSelected()) {
						if (graph.getEdges().get(i).getWeight() == 1.0) {
							yLineStyle.setAttribute("color", "#00ff00");
						} else if (graph.getEdges().get(i).getWeight() == 2.0) {
							yLineStyle.setAttribute("color", "#ff0000");
						} else if (graph.getEdges().get(i).getWeight() == 3.0) {
							yLineStyle.setAttribute("color", "#0000ff");
						}
					}
				} else {
					yLineStyle.setAttribute("color", "#000000");
				}
				yLineStyle.setAttribute(new Attribute("type", "line"));
				yLineStyle.setAttribute(new Attribute("width", "2.0"));
				yPolyLineEdge.addContent(yLineStyle);
				visPolyLineEdge.addContent(yPolyLineEdge);
				visEdge.addContent(visPolyLineEdge);
				edge.addContent(visEdge);
				
				graphElement.addContent(edge);
			}
			
			graphml.addContent(graphElement);
			
			File dnaFile = new File (outfile);
			try {
				FileOutputStream outStream = new FileOutputStream(dnaFile);
				XMLOutputter outToFile = new XMLOutputter();
				Format format = Format.getPrettyFormat();
				format.setEncoding("utf-8");
				outToFile.setFormat(format);
				outToFile.output(document, outStream);
				outStream.flush();
				outStream.close();
			} catch (IOException e) {
				System.err.println("Cannot save \"" + dnaFile + "\":" + e.getMessage());
				JOptionPane.showMessageDialog(dna.Dna.mainProgram, "Error while saving the file!\n" + e.getStackTrace());
			}
			System.out.println("The file \"" + outfile + "\" has been saved.");
		}
		
		/**
		 * SoNIA affiliation export filter.
		 * 
		 * @param name of the output file
		 */
		public void exportFilterSoniaAffiliation (String outfile) {
			
			double forwardDays = (Double) soniaPanel.forwardWindow.getValue(); //how long is an edge valid?
			
			prepareSimpleGraph();
			
			System.out.print("  Computing SoNIA graph... ");
			
			//create list of actors
			ArrayList<String> actors = new ArrayList<String>();
			for (int i = 0; i < class1.size(); i++) {
				if (!actors.contains(class1.get(i)) && !class1.get(i).equals("")) {
					actors.add(class1.get(i));
				}
			}
			
			//create list of categories
			ArrayList<String> categories = new ArrayList<String>();
			for (int i = 0; i < class3.size(); i++) {
				if (!categories.contains(class3.get(i)) && !class3.get(i).equals("")) {
					categories.add(class3.get(i));
				}
			}
			
			//create matrices for Sonia slices
			SoniaDyad[][] matrixYes = new SoniaDyad[actors.size()][categories.size()];
			for (int i = 0; i < matrixYes.length; i++) {
				for (int j = 0; j < matrixYes[0].length; j++) {
					matrixYes[i][j] = new SoniaDyad();
				}
			}
			SoniaDyad[][] matrixNo = new SoniaDyad[actors.size()][categories.size()];
			for (int i = 0; i < matrixNo.length; i++) {
				for (int j = 0; j < matrixNo[0].length; j++) {
					matrixNo[i][j] = new SoniaDyad();
				}
			}
			SoniaDyad[][] matrixCombined = new SoniaDyad[actors.size()][categories.size()];
			for (int i = 0; i < matrixCombined.length; i++) {
				for (int j = 0; j < matrixCombined[0].length; j++) {
					matrixCombined[i][j] = new SoniaDyad();
				}
			}
			
			//fill matrices
			for (int i = 0; i < class1.size(); i++) {
				
				//calculate size of the time window/expiration date of the edge
				double startD = timeToDouble(s_date.get(i));
				boolean leap = s_date.get(i).isLeapYear(s_date.get(i).get(Calendar.YEAR));
				double leapDays;
				if (leap == true) {
					leapDays = 365.0;
				} else {
					leapDays = 364.0;
				}
				double forwardWindow = forwardDays / leapDays;
				double endD = startD + forwardWindow;
				
				//find out which matrix cell to use
				int firstIndex = 0;
				int secondIndex = 0;
				for (int j = 0; j < actors.size(); j++) {
					if (actors.get(j).equals(class1.get(i))) {
						firstIndex = j;
					}
				}
				for (int j = 0; j < categories.size(); j++) {
					if (categories.get(j).equals(class3.get(i))) {
						secondIndex = j;
					}
				}
				
				//add the edge with its start and end point to the matrix
				if (s_agree.get(i).equals("yes")) {
					matrixYes[firstIndex][secondIndex] = matrixYes[firstIndex][secondIndex].addSlice(startD, endD);
				} else {
					matrixNo[firstIndex][secondIndex] = matrixNo[firstIndex][secondIndex].addSlice(startD, endD);
				}
			}
			
			//compute reduced slices/edges for both matrices
			int counterYes = 0;
			for (int i = 0; i < actors.size(); i++) {
				for (int j = 0; j < categories.size(); j++) {
					matrixYes[i][j] = matrixYes[i][j].reduceSlices();
					counterYes = counterYes + matrixYes[i][j].reducedSlices.size();
				}
			}
			int counterNo = 0;
			for (int i = 0; i < actors.size(); i++) {
				for (int j = 0; j < categories.size(); j++) {
					matrixNo[i][j] = matrixNo[i][j].reduceSlices();
					counterNo = counterNo + matrixNo[i][j].reducedSlices.size();
				}
			}
			System.out.println("A dynamic graph with " + actors.size() + " vertices and " + counterYes + " positive and " + counterNo + " negative weighted edges was generated.");
			
			/*
			//determine entry and exit of actors
			System.out.print("  Adjusting time line... ");
			HashMap<String,Double> vertexStart = new HashMap<String,Double>();
			HashMap<String,Double> vertexEnd = new HashMap<String,Double>();
			for (int i = 0; i < s_date.size(); i++) {
				if (!vertexStart.containsKey(class1.get(i)) || timeToDouble(s_date.get(i)) < vertexStart.get(class1.get(i))) {
					vertexStart.put(class1.get(i), timeToDouble(s_date.get(i)));
				}
				if (!vertexEnd.containsKey(class1.get(i)) || timeToDouble(s_date.get(i)) > vertexEnd.get(class1.get(i))) {
					boolean leap = s_date.get(i).isLeapYear(s_date.get(i).get(Calendar.YEAR));
					double leapDays;
					if (leap == true) {
						leapDays = 365.0;
					} else {
						leapDays = 364.0;
					}
					double forwardWindow = forwardDays / leapDays;
					double backwardWindow = (Double)soniaPanel.backwardWindow.getValue() / leapDays;
					vertexEnd.put(class1.get(i), (timeToDouble(s_date.get(i)) + forwardWindow + backwardWindow));
				}
			}
			System.out.println("done.");
			*/
			
			//set entry and exit of categories
			GregorianCalendar d1 = s_date.get(0);
			GregorianCalendar d2 = s_date.get(0);
			for (int i = 1; i < s_date.size(); i++) {
				if (s_date.get(i).before(d1)) {
					d1 = s_date.get(i);
				}
				if (s_date.get(i).after(d2)) {
					d2 = s_date.get(i);
				}
			}
			double d1d = timeToDouble(d1);
			double d2d = timeToDouble(d2);
			
			//combine the two matrices?
			
			//write to file
			try {
				System.out.print("  Writing data to disk... ");
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), "UTF8"));
				out.write("NodeId\tLabel\tStartTime\tEndTime\tNodeSize\tNodeShape\tColorName\tBorderWidth\tBorderColor");
				out.newLine();
				for (int i = 0; i < actors.size(); i++) {
					//out.write((i + 1) + "\t" + actors.get(i) + "\t" + vertexStart.get(actors.get(i)) + "\t" + vertexEnd.get(actors.get(i)) + "\t15\tellipse\torange\t1.5\tblack");
					out.write((i + 1) + "\t" + actors.get(i) + "\t" + d1d + "\t" + d2d + "\t15\tellipse\torange\t1.5\tblack");
					out.newLine();
				}
				for (int i = 0; i < categories.size(); i++) {
					out.write((i + 1 + actors.size()) + "\t" + categories.get(i) + "\t" + d1d + "\t" + d2d + "\t20\tsquare\tblue\t1.5\tblack");
					out.newLine();
				}
				out.write("FromId\tToId\tStartTime\tEndTime\tArcWeight\tArcWidth\tColorName");
				for (int i = 0; i < actors.size(); i++) {
					for (int j = 0; j < categories.size(); j++) {
						for (int k = 0; k < matrixYes[i][j].reducedSlices.size(); k++) {
							out.newLine();
							out.write((i + 1) + 
									"\t" + 
									(j + 1 + actors.size()) + 
									"\t" + 
									matrixYes[i][j].reducedSlices.get(k).startTime + 
									"\t" + 
									matrixYes[i][j].reducedSlices.get(k).endTime + 
									"\t" + 
									matrixYes[i][j].reducedSlices.get(k).strength + 
									"\t" + 
									matrixYes[i][j].reducedSlices.get(k).strength + 
									"\tblue");
						}
					}
				}
				for (int i = 0; i < actors.size(); i++) {
					for (int j = 0; j < categories.size(); j++) {
						for (int k = 0; k < matrixNo[i][j].reducedSlices.size(); k++) {
							out.newLine();
							out.write(
									(i + 1) + 
									"\t" + 
									(j + 1 + actors.size()) + 
									"\t" + 
									matrixNo[i][j].reducedSlices.get(k).startTime + 
									"\t" + 
									matrixNo[i][j].reducedSlices.get(k).endTime + 
									"\t" + 
									matrixNo[i][j].reducedSlices.get(k).strength + 
									"\t" + 
									matrixNo[i][j].reducedSlices.get(k).strength + 
									"\tred"
							);
						}
					}
				}
				out.close();
				System.out.println("File has been exported to \"" + outfile + "\".");
			} catch (IOException e) {
				System.err.println("Error while saving SoNIA file.");
			}
			
		}
		
	}
}