package br.ufes.inf.nemo.ocl2owl_swrl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ocl.uml.impl.ExpressionInOCLImpl;
import org.eclipse.uml2.uml.Constraint;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLAtom;

import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;
import br.ufes.inf.nemo.ocl.parser.OCLParser;
import br.ufes.inf.nemo.ocl2owl_swrl.exceptions.NonInitialized;
import br.ufes.inf.nemo.ocl2owl_swrl.exceptions.NonSupported;
import br.ufes.inf.nemo.ocl2owl_swrl.factory.ocl.uml.impl.ExpressionInOCLImplFactory;
import br.ufes.inf.nemo.ocl2owl_swrl.tags.Tag;

public class OCL2OWL_SWRL {
	private String nameSpace = null;
	private String oclRules = null;
	private OntoUMLParser ontoParser = null;
	private OWLOntologyManager manager = null;
	private OWLDataFactory factory = null;
	private OWLOntology ontology = null;
	public String errors = "";
	
	//public OCL2SWRL(OCLParser oclParser, OntoUMLParser refParser, OWLOntologyManager manager, String nameSpace) {
	/**
	 * Constructor.
	 * 
	 * @param oclRules - contains the String with all ocl rules
	 * @param ontoParser - contains the OntoUML parser
	 * @param manager - contains the OWLOntologyManager, used to get the OWLDataFactory and the OwlOntology
	 * @param nameSpace
	 */
	public OCL2OWL_SWRL(String oclRules, OntoUMLParser ontoParser, OWLOntologyManager manager, String nameSpace) throws NonInitialized {	
		this.nameSpace = nameSpace;
		//this.oclParser = oclParser;
		this.oclRules = oclRules;
		this.ontoParser = ontoParser;
		this.manager = manager;
		this.factory = manager.getOWLDataFactory();
		this.ontology = manager.getOntology(IRI.create(nameSpace.substring(0, nameSpace.length()-1)));
		
		//verify if all variables were initialized
		this.verifyVariablesInitialization();
	}
	
	/**
	 * Constructor.
	 */
	@SuppressWarnings("unused")
	private OCL2OWL_SWRL() {
		//this constructor is private to force the use of the specific constructor
	}
	
	/**
	 * This method verifies if all variables were initialized.
	 */
	//this function verifies if all necessary attributes were initialized
	private void verifyVariablesInitialization() throws NonInitialized{
		if(this.nameSpace == null){
			throw new NonInitialized("nameSpace");
		}else if(this.nameSpace.equals("")){
			throw new NonInitialized("nameSpace");
		}
		
		/*if(this.oclParser == null){
			throw new NonInitialized("oclParser");
		}*/
		
		if(this.oclRules == null){
			throw new NonInitialized("oclRules");
		}else if(this.oclRules.equals("")){
			throw new NonInitialized("oclRules");
		}
		
		if(this.ontoParser == null){
			throw new NonInitialized("refParser");
		}
		
		if(this.manager == null){
			throw new NonInitialized("manager");
		}
		
		if(this.factory == null){
			throw new NonInitialized("factory");
		}
		
		if(this.ontology == null){
			throw new NonInitialized("ontology");
		}
	}
	
	//
	
	/**
	 * This main function transform OCL constraints in SWRL rules.
	 */
	@SuppressWarnings("unchecked")
	public void Transformation () throws Exception{
		//counters of successfully and unsuccessfully transformed rules 
		int successfullyTransformedRules = 0;
		int unsuccessfullyTransformedRules = 0;
		
		/*
		OCLParser oclParser = null;
		try {
			oclParser = new OCLParser(oclRules, this.refParser, "company.uml");
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		//get the begin and the end of the first line (package PackageName) 
		int iPackage = oclRules.indexOf("package");
		int endLinePackage = oclRules.indexOf("\n", iPackage);
		
		//reset the indexes if doesn't exists
		if(iPackage < 0 || endLinePackage < 0){iPackage=0;endLinePackage=0;}
		
		//get the string containing "package PackageName"
		String strPackage = oclRules.substring(iPackage, endLinePackage);
		//and remove the string from oclRules
		oclRules = oclRules.replace(strPackage, "");
		/*		
		int iEndPackage = oclRules.indexOf("endpackage");
		int endLineEndPackage = iEndPackage+10;//oclRules.indexOf("\n", iEndPackage);
		
		String strEndPackage = oclRules.substring(iEndPackage, endLineEndPackage);
		*/
		//remove the string "endpackage" from oclRules
		oclRules = oclRules.replace("endpackage", "");
		/*
		int firstTag = oclRules.indexOf("--@");
		int i = oclRules.indexOf("--@");
		int j = oclRules.indexOf("\n", i);
		String tag = oclRules.substring(i+3, j);
		System.out.println();
		*/
		//Now, the rules are treated by blocks.
		//Is expected that rules without tags comes first and then blocks of rules with the same tag
		//the first block is considered from the position zero to the first occurrence of a tag (--@)
		int blockBegin = 0;
		int blockEnd = oclRules.indexOf("--@");
		OCLParser oclParser  = null;
		
		try{
		//create a ocl parser to create a referent uml model based on the ontouml model
		oclParser = new OCLParser(ontoParser, null, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		while(true){
			//if doesn't exist more tags, the of block is setted as the end of the oclRules
			if(blockEnd < 0){
				blockEnd = oclRules.length();
				//when the begin and the end of the block are equal, the while loop is terminated
				if(blockBegin == blockEnd){
					break;
				}
			}
			
			//since the string "--@" starts at the blockBegin, the tag name will end at the occurrence of '\n'
			int endOfTag = oclRules.indexOf("\n", blockBegin);
			//get the tag and replace all unexpected chars
			String tag = oclRules.substring(blockBegin, endOfTag);
			tag = tag.replace("--@", "");
			tag = tag.replace("\n", "");
			tag = tag.replace("\r", "");
			tag = tag.replace(" ", "");
			tag = tag.toLowerCase();
			//get the block of rules desconsidering the tag
			String strBlockOclConstraints = oclRules.substring(endOfTag, blockEnd);
			
			//set the begining of the next block as the end of the actual block
			blockBegin = blockEnd;
			//get the index of the next tag as the end of the next block
			blockEnd = oclRules.indexOf("--@", blockBegin+3);
			
			//parse the ocl constraints
			oclParser.parse(strBlockOclConstraints);
			//parse the block of ocl rules
			//OCLParser oclParser = null;
			//try {
				//oclParser = new OCLParser(strBlockOclConstraints, this.ontoParser, "temp.uml");
			/*}catch (ParserException e) {
				e.printStackTrace();
				//return e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				//return e.getMessage();
			}*/
			
			//treats all constraints of ocl parser
			for(Constraint ct: oclParser.getConstraints())
			{
				String stereotype = "";
				//get the stereotype of constraint
				stereotype = oclParser.getUMLReflection().getStereotype(ct);
				//if the is tagged, the stereotype is setted as the tag
				if(!tag.equals("") && org.eclipse.ocl.utilities.UMLReflection.INVARIANT.equals(stereotype)){
					stereotype = tag;
				}else{
					//stereotype = oclParser.getUMLReflection().getStereotype(ct);
				}
				
				//verify if the stereotype is unsupported
				if(stereotypeIsUnsupported(stereotype)){
					throw new NonSupported(stereotype, tag);
				}else{
					//get the element inside of the constraint
					ExpressionInOCLImpl expr = (ExpressionInOCLImpl) ct.getSpecification();
					
					//create a factory based on the element
					ExpressionInOCLImplFactory exprFactory = new ExpressionInOCLImplFactory(expr);
					
					//set the element
					if(ct.getConstrainedElements().size() > 0){
						exprFactory.setElement(ct.getConstrainedElements().get(0));
					}
					
					//create the antecedent and consequents of the resultant swrl
					Set<SWRLAtom> antecedent = new HashSet<SWRLAtom>();
					Set<SWRLAtom> consequent = new HashSet<SWRLAtom>();
					
					try {
						//call the solve method
						exprFactory.solve(stereotype, this.ontoParser, this.nameSpace, this.manager, this.factory, this.ontology, antecedent, consequent, null, false, 1, false);
						//increment the successfully transformed rules
						successfullyTransformedRules++;
					}catch (Exception e) {
						//increment the error message and the unsuccessfully transformed rules
						this.errors += e.getMessage() + "\n";
						unsuccessfullyTransformedRules++;
					}
				}
				//System.out.println(this.errors);				
			}
		}
		
		String successMessage = "\n\n" + successfullyTransformedRules + " rule(s) successfully transformed.\n" + unsuccessfullyTransformedRules + " rule(s) unsuccessfully transformed.\n"; 
		this.errors = successMessage + this.errors;
	}
	
	/**
	 * This function verifies if an especific stereotype is supported by this transformation.
	 * 
	 *  @param stereotype - contains the stereotype to be verified
	 */
	public Boolean stereotypeIsUnsupported(String stereotype){
		//verifify if the stereotype is a tag
		Boolean isTag = Tag.isTag(stereotype);
		/*Boolean isTag = true;
		try {
			@SuppressWarnings("unused")
			Tag tag = Tag.valueOf(stereotype);
		} catch (Exception e) {
			isTag = false;
		}
		*/
		//if is a tag, OK
		if(isTag){
			return false;
		}
		
		//if is an invariant or a derivation, OK
		if(org.eclipse.ocl.utilities.UMLReflection.INVARIANT.equals(stereotype) || org.eclipse.ocl.utilities.UMLReflection.DERIVATION.equals(stereotype)){
			return false;
		}
		
		//is unsupported!
		return true;
	}
}