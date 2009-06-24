package org.imb.client;

//Copyright (c) 2008 Johannes Metscher <Johannes.Metscher@imb-uni-augsburg.de>
//
// Permission to use, copy, modify, and distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

import java.util.Collection;
import java.util.Vector;


public class SubForm extends FormElement {
	
	protected static final int CHAR_AVERAGE_WIDTH = 7;
	protected            int iLabelWidth        = 75;
	
	
	public SubForm(int position, String name, String description, int id, Collection<FormElement> elements ){
		super( position, name, description, id);
		this.elements = elements;
	}
	
	public SubForm(int position, String name, String description, int id ){
		super( position, name, description, id);
		elements = new Vector<FormElement>();
	}

	/** 
	 * @uml.property name="elements"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="composite" inverse="subForm:org.imb.client.FormElement"
	 */
	protected Collection<FormElement> elements;

	/** 
	 * Getter of the property <tt>elements</tt>
	 * @return  Returns the elements.
	 * @uml.property  name="elements"
	 */
	public Collection<FormElement> getElements() {
		return elements;
	}

	/** 
	 * Setter of the property <tt>elements</tt>
	 * @param elements  The elements to set.
	 * @uml.property  name="elements"
	 */
	public void setElements(Collection<FormElement> elements) {
		this.elements = elements;
	}

	
	public void addElement( FormElement formElement )
	{
		elements.add( formElement );
	}

	public void removeElement(FormElement fe) {
		for( FormElement e : elements){
			if( fe.equals( e ) || e.getId() == fe.getId() ){
				elements.remove( e );
			}
		}
		
	}
	
	protected int getMaxLabelWidth() {
		int maxLabelLength = 0;
		for( FormElement e : elements ){
			if( maxLabelLength < e.getName().length() ){
				maxLabelLength = e.getName().length();
			}
		}
		return maxLabelLength*CHAR_AVERAGE_WIDTH;
	}
			
}
