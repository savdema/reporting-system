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
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ChoiceStarRatingSubForm extends SubForm {
	
	private static final String TEXTBOX_WIDTH_CAPTION = "60px";
	private String choiceCaption;
	private String starRatingCaption;
	private FlexTable          grid;
	private FormElementS     fesColumnCaption;
	private FormElementS     fesChoiceLabels;
	private Vector<String>   vChoiceLabels    = new Vector<String>();
	private Vector<ListBox>  vChoiceListBoxes = new Vector<ListBox>();
		  
	public ChoiceStarRatingSubForm(int position, String name, String description, int id, String choiceCaption , String starRatingCaption, Collection<FormElement> elements ){
		super( position, name, description, id, elements);
		setColumnCaption( choiceCaption , starRatingCaption );
		initChoiceLabels( );
	}

	public ChoiceStarRatingSubForm(int position, String name, String description, int id, String choiceCaption , String starRatingCaption ){
		super( position, name, description, id);
		elements = new Vector<FormElement>();
		setColumnCaption( choiceCaption , starRatingCaption );
		initChoiceLabels( );
	}
	
	private void initChoiceLabels(){
		setChoiceLabels( new FormElementS( 0 , "1;2;3" , "1;2;3" , FormElementS.TYPE_CHOICE_LABELS ) );
	}
	
	private void setColumnCaption(String choiceCaption, String starRatingCaption) {
		this.choiceCaption     = choiceCaption;
		this.starRatingCaption = starRatingCaption;
		String columnCaption   = choiceCaption + ";" + starRatingCaption;
		fesColumnCaption  = new FormElementS( 0 , columnCaption , columnCaption , FormElementS.TYPE_COLUMN_CAPTION );
		fesColumnCaption.setParent( new FormElementS( this ) );
	}
	
	public void setColumnCaption( FormElementS fesColumnCaption ) {
		int limitIndex         = fesColumnCaption.getDescription().indexOf(";");
		int length             = fesColumnCaption.getDescription().length();
		this.choiceCaption     = fesColumnCaption.getDescription().substring(0, limitIndex);
		this.starRatingCaption = fesColumnCaption.getDescription().substring(limitIndex+1, length);
		this.fesColumnCaption  = fesColumnCaption;
		fesColumnCaption.setParent( new FormElementS( this ) );
	}
	
	public void setChoiceLabels ( FormElementS fesChoiceLabels){
		if( fesChoiceLabels.getType() == FormElementS.TYPE_CHOICE_LABELS ){
			this.fesChoiceLabels = fesChoiceLabels;
			String[] sChoiceLabels = fesChoiceLabels.getDescription().split( ";" );
			vChoiceLabels = new Vector<String>();
			for ( String scl : sChoiceLabels ){
				vChoiceLabels.add( scl);
			}
			for( ListBox lbc : vChoiceListBoxes ){
				lbc.clear();
				for( String scl: vChoiceLabels ){
					lbc.addItem( scl );
				}
			}
		}
	}
		

	@Override
	public Panel createPanel( boolean editable )
	{
		grid     = new FlexTable( );
		VerticalPanel vPanel = new VerticalPanel();
		iLabelWidth =  getMaxLabelWidth();
		HorizontalPanel hPanel = new HorizontalPanel();
		Label l = new Label("");
		l.setWidth( (iLabelWidth+3)+"px" );
		hPanel.add( l );
		final TextBox tbChoiceLabels = new TextBox(  );
		final TextBox tbChoiceCaption = new TextBox(  );
		if ( editable){
			//Description
			final TextArea tbDescription = new TextArea(  );
			tbDescription.setWidth( TEXTAREA_WIDTH );
			final ChoiceStarRatingSubForm stsf = this;
			tbDescription.addChangeListener(new ChangeListener() {
				  
				public void onChange(Widget sender) {
					  ChoiceStarRatingSubForm owner = stsf;
					  owner.setName( tbDescription.getText() );
					  owner.setDescription( tbDescription.getText() );
					  owner.updateFormElement();
				  }
			});
			tbDescription.setText( description );
			
			//ChoiceLabels
			tbChoiceLabels.setWidth( TEXTAREA_WIDTH );
			tbChoiceLabels.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  updateChoiceLabels( tbChoiceLabels.getText() );
				  }
			});
			tbChoiceLabels.setText( fesChoiceLabels.getDescription() );
			
			//ChoiceCaption
			tbChoiceCaption.setWidth( TEXTBOX_WIDTH_CAPTION );
			tbChoiceCaption.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  ChoiceStarRatingSubForm owner = stsf;
					  owner.setChoiceCaption( tbChoiceCaption.getText() );
					  owner.updateColumnCaption();
				  }
			});
			tbChoiceCaption.setText( choiceCaption );
			
			//starRatingCaption
			final TextBox tbStarRatingCaption = new TextBox(  );
			tbStarRatingCaption.setWidth( TEXTBOX_WIDTH_CAPTION );
			tbStarRatingCaption.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  ChoiceStarRatingSubForm owner = stsf;
					  owner.setStarRatingCaption( tbStarRatingCaption.getText() );
					  owner.updateColumnCaption();
				  }
			});
			tbStarRatingCaption.setText( starRatingCaption );
			vPanel.add( tbDescription );
			vPanel.add( tbChoiceLabels );
			hPanel.add( tbChoiceCaption  );
			hPanel.add( tbStarRatingCaption  );
			
			l.setWidth( "154px" );
			tbChoiceCaption.setWidth( getChoiceLabelWidth()+"px" );
		}
		else{
			vPanel.add( new HTML( description ) );
			
			Label lChoiceCaption = new Label(choiceCaption  );
			lChoiceCaption.setWidth( getChoiceLabelWidth() + "px");
			hPanel.add( lChoiceCaption );
			hPanel.add( new Label( starRatingCaption ) );
			
		}
		vPanel.add( hPanel);

		Iterator<FormElement> iterator = elements.iterator (); 

		while( iterator.hasNext() )
		{
			final FormElement currentFE = iterator.next();
			if ( currentFE instanceof StarRating ){
				setFormElement( currentFE.getPosition() , 2 , currentFE , editable);
			}else{
				setFormElement( currentFE.getPosition() , 1 , currentFE , editable);
			}
		}
		vPanel.add( grid );
		if ( editable){
			Button button = new Button("Neues Element");
	        button.addClickListener(new ClickListener() {
				  public void onClick(Widget sender) {
					  addNewElement();
					  }
					});
	        vPanel.add( button );
		}
		return vPanel;
		
	}
	
	protected void updateChoiceLabels( String sChoiceLabels ) {
		if( fesChoiceLabels != null){
			fesChoiceLabels.setName( sChoiceLabels );
			fesChoiceLabels.setDescription( sChoiceLabels );
			fesChoiceLabels.setParent( new FormElementS( this ));
		}
				
    	AsyncCallback<FormElementS> callback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
				setChoiceLabels( fes );
			}
			  
		};
		
		if( fesChoiceLabels.getId() > 0 ){
			ReportingSystem.rsService.updateFormElement( fesChoiceLabels , callback );
		}
		else{
			ReportingSystem.rsService.addFormElement( fesChoiceLabels , callback );
		}
		
	}

	protected void updateColumnCaption() {
		String columnCaption   = choiceCaption + ";" + starRatingCaption;
		if( fesColumnCaption != null){
			fesColumnCaption.setName( columnCaption );
			fesColumnCaption.setDescription( columnCaption );
		}
		else{
			fesColumnCaption  = new FormElementS( 0 , columnCaption , columnCaption , FormElementS.TYPE_COLUMN_CAPTION );
			fesColumnCaption.setParent( new FormElementS( this ) );
		}
		
    	AsyncCallback<FormElementS> callback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
				fesColumnCaption = fes;
			}
			  
		};
		
		if( fesColumnCaption.getId() > 0 ){
			ReportingSystem.rsService.updateFormElement( fesColumnCaption , callback );
		}
		else{
			ReportingSystem.rsService.addFormElement( fesColumnCaption , callback );
		}
	}

	private void addNewElement(){
		int position   = elements.size() / 2 + 1;
		StarRating nst = new StarRating( position , "Neues Element","Neues Element", -1 );
		final Choice nc      = new Choice( position , "Neues Element","Neues Element", -1 , vChoiceLabels );
		final FormElement parent = this;
		
		final AsyncCallback<FormElementS> acChoice = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}
			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				FormElement  fe  = fes.getFormElement();
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
				elements.add( fe );
				setFormElement( fe.getPosition() , 0, fe , true);
			} 
		};
		
		AsyncCallback<FormElementS> acStarRating = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}
			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				FormElement  fe  = fes.getFormElement();
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
				elements.add( fe );
				FormElementS ncs = new FormElementS( nc );
				ncs.setParent( new FormElementS( parent ) );
				ReportingSystem.debugMessage( "Send to server:  NAME=" + ncs.getName() +" PARENT_ID=" + ncs.getParent().getId() );
				ReportingSystem.rsService.addFormElement( ncs , acChoice );
			} 
		};
		
		
		FormElementS nsts = new FormElementS( nst );
		nsts.setParent( new FormElementS( this ) );
		ReportingSystem.debugMessage( "Send to server:  NAME=" + nsts.getName() +" PARENT_ID=" + nsts.getParent().getId() );
		ReportingSystem.rsService.addFormElement( nsts , acStarRating );
		

	}
	
	private void setFormElement( int row , int column , FormElement fe , boolean editable ){
		
		if( fe instanceof Choice ){
			Choice     choice     = (Choice)fe;
			StarRating starRating = (StarRating)getPartnerElement( fe );
			choice.setItems( vChoiceLabels );
			if( starRating != null ){
				ChoiceStarRating csr = new ChoiceStarRating( choice , starRating );
				csr.setParent( this );
				if( editable ){
					grid.setWidget( row , 0 , csr.getEditPanel() );
				}
				else{
					grid.setWidget( row , 0 , csr.getPanel( iLabelWidth ) );
				}
			}
		}
	}
	
	private FormElement getPartnerElement(FormElement fe) {
		FormElement fePartner = null;
		for( FormElement e : elements ){
			if( !e.equals( fe ) &&
				e.getPosition() == fe.getPosition() &&
				e.getName().equals( fe.getName() )
			  ){
				fePartner = e;
			}
		}
		return fePartner;
	}

	protected FormElement getSecondOwner(FormElement fe1 ) {
		FormElement fe2 = null;
		for ( FormElement e : elements){
			if( e.getPosition() == fe1.getPosition() && !fe1.equals(e) ){
				fe2 = e;
			}
		}
		return fe2;
	}

	public void setChoiceCaption( String cc ){
		this.choiceCaption = cc;
	}
	
	public void setStarRatingCaption( String src ){
		this.starRatingCaption = src;
	}
	
	public String[] getChoiceLabels(){
		return fesChoiceLabels.getDescription().split( ";" );
	}
	
	private int getChoiceLabelWidth() {
		int maxLabelLength = TextContainer.NOT_SPECIFIED.length();
		for( String s : vChoiceLabels ){
			if( maxLabelLength < s.length() ){
				maxLabelLength = s.length();
			}
		}
		return maxLabelLength*CHAR_AVERAGE_WIDTH + 25;
	}
}
