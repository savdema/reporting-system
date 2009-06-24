package org.imb.client;

// Copyright (c) 2008 Johannes Metscher <Johannes.Metscher@imb-uni-augsburg.de>
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

import java.util.Vector;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * CheckBoxElement implements all necessary methods to visualize 
 * the form-element checkbox in the normal and edit mode. 
 * 
 * @author Johannes Metscher
 */
public class CheckBoxElement extends FormElement {
	
	private String[] labels                     = new String[0];
	private Vector<CheckBox> checkBoxes         = new Vector<CheckBox>();
	private VerticalPanel vpCheckBoxes          = new VerticalPanel();
	private FlexTable  ftEdit                   = new FlexTable();
	private Vector<CheckBox> selectedCheckBoxes = new Vector<CheckBox>();
	
	/**
	 * Constructor of the class CheckBoxElement
	 * 
	 * @param position		the position of the form-element in the (sub)form 
	 * @param name			the name of the form-element
	 * @param description	the description of the form-element contains all 
	 * @param id			the id of the form-element (will be set via db)
	 * @param items			the String-typed items of the check-box (specific parameter)
	 */
	public CheckBoxElement(int position, String name, String description, int id, Vector<String> items){
		super( position, name, description, id );
	}
	
	
	/* (non-Javadoc)
	 * @see org.imb.client.FormElement#createPanel(boolean)
	 */
	@Override
	public Panel createPanel( boolean editable )
	{
		VerticalPanel panel = new VerticalPanel();
		TextBox tbName        = new TextBox();
		TextBox tbDescription = new TextBox();
		Label   lName         = new Label( name );
		final FormElement thisFE = this;
		
	    tbName.setText( getName() );
		tbDescription.setText( getDescription() );
		tbName.setWidth( TEXTAREA_WIDTH );
		tbDescription.setWidth( TEXTAREA_WIDTH );
	 
		tbName.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  thisFE.setName( ((TextBox) sender).getText() );
				  thisFE.updateFormElement();
			  }
		});
		tbDescription.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  thisFE.setDescription( ((TextBox) sender).getText() );
				  addCheckBoxes( vpCheckBoxes , true );
				  thisFE.updateFormElement();
			  }
		});
		
		if ( editable){
			panel.add( tbName );
			panel.add( tbDescription );
			addCheckBoxes( vpCheckBoxes , editable );
			panel.add( vpCheckBoxes );
		}
		else{
			panel.add( lName );
			VerticalPanel vpTest = new VerticalPanel();
			addCheckBoxes( vpTest , editable );
			panel.add( vpTest );
			
		}
		return panel;
	}

	/**
	 * Internal method to create the check-boxes for each item
	 * 
	 * @param vpTest	the panel which contains the check-boxes
	 * @param editable	switch between the normal and editable visualization
	 */
	private void addCheckBoxes(VerticalPanel vpTest, boolean editable) {
		labels = description.split(";");
		FlexTable ft = new FlexTable();
		if( editable ){
			for( CheckBox cbOld : checkBoxes ){
				cbOld.removeFromParent();
			}
			ft = ftEdit;
		}
		final CheckBoxElement thisFE = this;
		int columnCount = 3;
		int counter = 0;
		for( String s : labels ){
			CheckBox cb = new CheckBox( s );
		    cb.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			    	  CheckBox cb     = (CheckBox)sender;
			    	  boolean checked = cb.isChecked();
			    	  if( checked ){
			    		  thisFE.addSelectedCheckBox( cb );
			    	  }
			    	  else{
			    		  thisFE.removeSelectedCheckBox(cb);
			    	  }
			      }
			    });
		    ft.setWidget( counter/columnCount, counter%columnCount, cb);
		    counter++;
			if( editable ){
				checkBoxes.add( cb );
			}
		}
		vpTest.add( ft );
	}
	
	/**
	 * Add a selected check-box to selectedCheckBoxes
	 * 
	 * @param cb	the selected check-box which should be added
	 */
	protected void addSelectedCheckBox( CheckBox cb ){
		if( selectedCheckBoxes.indexOf( cb ) < 0 ){
			selectedCheckBoxes.add( cb );
		}
		
	}
	
	/**
	 * Remove a selected check-box from selectedCheckBoxes
	 * 
	 * @param cb	the non-selected check-box which should be removed
	 */
	protected void removeSelectedCheckBox( CheckBox cb ){
		selectedCheckBoxes.remove( cb );
	}
	
	/* (non-Javadoc)
	 * @see org.imb.client.FormElement#getResult()
	 */
	@Override
	public String getResult() {
		if( selectedCheckBoxes.size() == 0 ){
			return TextContainer.NOT_SPECIFIED;
		}
		String result = "";
		for( CheckBox cb : selectedCheckBoxes){
			result += cb.getText() + ";";
		}
		result = result.substring( 0 , result.length() - 1 );
		return result;
	}
}
