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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class StarRatingSubForm extends SubForm {

	private FlexTable          grid;

	  
	public StarRatingSubForm(int position, String name, String description, int id, Collection<FormElement> elements ){
		super( position, name, description, id, elements);
		this.elements = elements;
	}
	
	public StarRatingSubForm(int position, String name, String description, int id ){
		super( position, name, description, id);
		elements = new Vector<FormElement>();
	}
	
	@Override
	protected Panel createPanel( boolean editable ){
		grid     = new FlexTable( );
		VerticalPanel starRatingPanel = new VerticalPanel();
		if ( editable){
			final TextArea tbDescription = new TextArea(  );
			tbDescription.setWidth( TEXTAREA_WIDTH );
			final StarRatingSubForm stsf = this;
			tbDescription.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  FormElement owner = stsf;
					  owner.setName( tbDescription.getText() );
					  owner.setDescription( tbDescription.getText() );
					  updateFormElement();
				  }
			});
			tbDescription.setText( description );
			starRatingPanel.add( tbDescription );
		}
		else{
			starRatingPanel.add( new HTML( description ) );
		}
		iLabelWidth = getMaxLabelWidth();
		int i = 0;
		for( FormElement currentFE : elements ){
			setFormElement( i , currentFE , editable);
			i++;
		}
		starRatingPanel.add( grid );
		if ( editable){
			Button button = new Button("Neues Element");
	        button.addClickListener(new ClickListener() {
				  public void onClick(Widget sender) {
					  addNewElement();
					  }
					});
	        starRatingPanel.add( button );
		}
		return starRatingPanel;
	}
	


	private void addNewElement(){
		StarRating nst = new StarRating( elements.size()+1 , "Neues Rating","ToolTip", -1 );
		final StarRatingSubForm thisStarRatingSubForm = this;
		AsyncCallback<FormElementS> callback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}
			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
				FormElement nst = fes.getFormElement();
				nst.setParent( thisStarRatingSubForm );
				setFormElement( elements.size() , nst , true );
				elements.add( nst );
			}
		};
		FormElementS tmpfes = new FormElementS( nst );
		tmpfes.setParent( new FormElementS( this ) );
		ReportingSystem.debugMessage( "Send to server:  NAME=" + tmpfes.getName() +" PARENT_ID=" + tmpfes.getParent().getId() );
		ReportingSystem.rsService.addFormElement( tmpfes , callback );
	}
	
	private void setFormElement( int row , FormElement fe , boolean editable ){
		if( fe instanceof StarRating ){
			StarRating sr = (StarRating)fe;
			if( editable ){
				grid.setWidget( row , 0 ,  sr.getEditPanel() );
			}
			else{
				grid.setWidget( row , 0 ,  sr.getPanel( iLabelWidth ) );
			}
		}
	}
			
}
