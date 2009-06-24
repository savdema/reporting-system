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

import java.util.*;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Choice implements all necessary methods to visualize 
 * the form-element choice in the normal and edit mode. 
 * 
 * @author Johannes Metscher
 */
public class Choice extends FormElement {

	private Vector<String> items;

	/**
	 * Constructor of the class Choice
	 * 
	 * @param position		the position of the form-element in the (sub)form 
	 * @param name			the name of the form-element
	 * @param description	the description of the form-element contains all 
	 * @param id			the id of the form-element (will be set via db)
	 * @param items			the String-typed items of the check-box (specific parameter)
	 */
	public Choice(int position, String name, String description, int id, Vector<String> items){
		super( position, name, description, id);
		this.items = items;

	}

	/**
	 * Getter of the attribute items
	 * 
	 * @return items	the items of the choice element
	 */
	public Vector<String>  getItems() {
		return items;
	}

	/**
	 * Setter of the attribute items
	 * 
	 * @param items	the items of the choice element
	 */
	public void setItems(Vector<String>  items) {
		this.items = items;
	}
	
	/* (non-Javadoc)
	 * @see org.imb.client.FormElement#createPanel(boolean)
	 */
	public Panel createPanel( boolean editable )
	{
		HorizontalPanel panel = new HorizontalPanel();
		final ListBox      lbChoice     = new ListBox();
		final FormElement thisFE = this;   
		//Define initial values
		lbChoice.addItem( TextContainer.NOT_SPECIFIED );
		for( String sc : description.split(";")){
			lbChoice.addItem( sc );
		}
		tbName.setText( getName() );
		tbName.setWidth( TEXTBOX_WIDTH );
		
		//Define listener
		lbChoice.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  thisFE.setResult( lbChoice.getValue( lbChoice.getSelectedIndex() ) );
				  }
			});
		tbDescription.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  thisFE.setDescription( ((TextBox) sender).getText() );
				  thisFE.updateFormElement();
				  lbChoice.clear();
					lbChoice.addItem( TextContainer.NOT_SPECIFIED );
					for( String sc : description.split(";")){
						lbChoice.addItem( sc );
					}
			  }
		});
		
		if ( editable){
			panel.add( tbName );
			panel.add( tbDescription );	
		}
		else{
			panel.add( new Label( getName()) );			
		}
		panel.add( lbChoice );
		return panel;
	}

}
