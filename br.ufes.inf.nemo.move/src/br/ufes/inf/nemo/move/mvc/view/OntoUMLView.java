package br.ufes.inf.nemo.move.mvc.view;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.move.mvc.model.OntoUMLModel;
import br.ufes.inf.nemo.move.ui.TheFrame;
import br.ufes.inf.nemo.move.ui.ontouml.OntoUMLCheckBoxTree;
import br.ufes.inf.nemo.move.ui.ontouml.OntoUMLTreeBar;
import br.ufes.inf.nemo.move.ui.util.TitleTextField;
import br.ufes.inf.nemo.move.ui.util.TreeScrollPane;

/**
 * This class represents a View for OntoUML Model.
 * 
 * @author John Guerson
 */

public class OntoUMLView extends JPanel {

	private static final long serialVersionUID = -8391565085376481547L;

	@SuppressWarnings("unused")
	private OntoUMLModel ontoumlModel;
	
	private TheFrame frame;	
	private TitleTextField tltxtfldOntouml;
	private OntoUMLTreeBar ontobar;
	private TreeScrollPane treeScrollPane;
	private CheckboxTree modeltree;
	
	/** 
	 * Creates a View from a OntoUML model and the main frame application.
	 * 
	 * @param ontoumlModel
	 */
	public OntoUMLView(OntoUMLModel ontoumlModel, TheFrame frame)
	{
		this();
		
		this.ontoumlModel = ontoumlModel;
		this.frame = frame;
		
		setPath(ontoumlModel.getOntoUMLPath(),ontoumlModel.getOntoUMLModelInstance());
		setModelTree(ontoumlModel.getOntoUMLModelInstance(),ontoumlModel.getOntoUMLParser());
		
		validate();
		repaint();
	}

	/**
	 * Creates an Empty View for OntoUML Model.
	 */
	public OntoUMLView() 
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 0, 0, 0));
		panel.setLayout(new BorderLayout(0, 0));
				
		tltxtfldOntouml = new TitleTextField();
		tltxtfldOntouml.setText("OntoUML - Ontology-based Conceptual Model");
		panel.add(BorderLayout.NORTH,tltxtfldOntouml);
		
		ontobar = new OntoUMLTreeBar();
		ontobar.btnShowUnique.setToolTipText("");
		ontobar.setToolTipText("Show Aliases");
		panel.add(BorderLayout.CENTER,ontobar);
		
		add(BorderLayout.NORTH,panel);
		
		treeScrollPane = new TreeScrollPane();
		add(BorderLayout.CENTER,treeScrollPane);
	}
		
	/**
	 * Set Model Tree from a Package and the OntoUML Parser.
	 * 
	 * @param refmodel
	 * @param refparser
	 */
	public void setModelTree(RefOntoUML.Package refmodel,OntoUMLParser refparser)
	{	
		if (refmodel!=null)
		{
			if(modeltree!=null) treeScrollPane.treePanel.remove(modeltree);			
			modeltree = OntoUMLCheckBoxTree.createCheckBoxTree(refmodel,refparser);					
			treeScrollPane.treePanel.add(BorderLayout.CENTER,modeltree);
			treeScrollPane.validate();
			treeScrollPane.repaint();
		}
	}
		
	/** 
	 * Get the Check Box Model Tree. 
	 */
	public CheckboxTree getModelTree() { return modeltree; }
	
	/** 
	 * Set Path View from a absolute Path and a OntoUML Package Model.
	 * 
	 * @param path
	 * @param refmodel
	 */
	public void setPath(String path, RefOntoUML.Package refmodel)
	{
		if (path==null && refmodel!=null)
			ontobar.textPath.setText("Loaded...");
		else if (path!= null && refmodel !=null)
			ontobar.textPath.setText(path);
	}
	
	/** 
	 * Get the main frame of application.
	 * 
	 * @return
	 */
	public TheFrame getTheFrame() { return frame; }
	
	/**
	 * Add Load OntoUML Action listener.
	 * 
	 * @param actionListener
	 */
	public void addLoadOntoUMLListener(ActionListener actionListener) 
	{
		ontobar.btnOpen.addActionListener(actionListener);
	}	    
	
	/**
	 * Add Verify Model Action Listener.
	 * 
	 * @param actionListener
	 */
	public void addVerifyModelListener(ActionListener actionListener) 
	{
		ontobar.btnVerify.addActionListener(actionListener);
	}	
	
	/**
	 * Add Show Unique Names Action Listener.
	 * 
	 * @param actionListener
	 */
	public void addShowUniqueNamesListener(ActionListener actionListener) 
	{
		ontobar.btnShowUnique.addActionListener(actionListener);
	}
	
	/**
	 * Export Model Action Listener.
	 * 
	 * @param actionListener
	 */
	public void addSaveAsModelListener(ActionListener actionListener)
	{
		ontobar.btnSaveAs.addActionListener(actionListener);
	}

	/**
	 * Get OntoUML Path Location.
	 */
	public String getOntoUMLPathLocation()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open OntoUML");
		FileNameExtensionFilter ontoumlFilter = new FileNameExtensionFilter("Reference OntoUML Model (*.refontouml)", "refontouml");
		fileChooser.addChoosableFileFilter(ontoumlFilter);
		fileChooser.setFileFilter(ontoumlFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
		{
			if (fileChooser.getFileFilter() == ontoumlFilter) 
			{
				return fileChooser.getSelectedFile().getPath();
			}
		}
		return null;
	}
	
	/**
	 * Save OntoUML Path Location.
	 */
	public String saveOntoUMLPathLocation()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save OntoUML");
		FileNameExtensionFilter ontoumlFilter = new FileNameExtensionFilter("Reference OntoUML Model (*.refontouml)", "refontouml");
		fileChooser.addChoosableFileFilter(ontoumlFilter);
		fileChooser.setFileFilter(ontoumlFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) 
		{
			if (fileChooser.getFileFilter() == ontoumlFilter) 
			{
				String path = fileChooser.getSelectedFile().getPath();
				if (path.contains(".refontouml"))
					return fileChooser.getSelectedFile().getPath();
				else
					return fileChooser.getSelectedFile().getPath()+".refontouml";				
			}
		}
		return null;
	}
}