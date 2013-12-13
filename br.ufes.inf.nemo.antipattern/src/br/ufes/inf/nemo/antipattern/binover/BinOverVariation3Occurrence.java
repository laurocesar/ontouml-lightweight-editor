package br.ufes.inf.nemo.antipattern.binover;

import RefOntoUML.Association;
import br.ufes.inf.nemo.common.ontoumlparser.OntoUMLParser;

public class BinOverVariation3Occurrence extends BinOverOccurrence {

	public BinOverVariation3Occurrence(Association a, OntoUMLParser parser) {
		super(a,parser);
	}

	@Override
	public OntoUMLParser setSelected() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return "[VAR 3]\n"+super.toString();
	}

}