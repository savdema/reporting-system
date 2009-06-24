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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


/** 
 * ChoiceStarRating implements all necessary methods to visualize 
 * the form-element choice and star-rating in the normal and edit mode. 
 * 
 * @author Johannes Metscher
 *
 */
public class ChoiceStarRating extends FormElement {
	private Choice     choice;
	private StarRating starRating;
	private static final String  NAME_WIDTH = "75px";
	private String                nameWidth = NAME_WIDTH;

	/**
	 * Constructor 
	 * 
	 * @param choice	corresponding choice form-element
	 * @param starRating	corresponding star-rating form-element
	 */
	public ChoiceStarRating( Choice choice , StarRating starRating ){
		super( choice.getId() , choice.getName() , choice.getDescription() , -1 );
		this.choice     = choice;
		this.starRating = starRating;
		
	}
	
	/* (non-Javadoc)
	 * @see org.imb.client.FormElement#createPanel(boolean)
	 */
	@Override
	public Panel createPanel( boolean editable )
	{
		HorizontalPanel    hpChoicStarR = new HorizontalPanel();
		final RateItWidget rateIt       = new RateItWidget( 0 , 2 , StarRating.URL_IMG_GREY, StarRating.URL_IMG_ORANGE, StarRating.URL_IMG_GREY, StarRating.URL_IMG_ORANGE, StarRating.URL_IMG_GREY);
		final ListBox      lbChoice     = new ListBox();
		final Label        lName        = new Label( choice.getName() );
		final TextBox      tbName       = new TextBox( );
		
		//Define initial values
		lbChoice.addItem( TextContainer.NOT_SPECIFIED );
		for( String sc : choice.getItems() ){
			lbChoice.addItem( sc );
		}
		tbName.setText( choice.getName() );
		lName.setWidth( nameWidth );
		tbName.setWidth( TEXTBOX_WIDTH );
		
		//Define listener
		rateIt.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  starRating.setResult( TextContainer.STARRATING_LABELS[rateIt.getUserRating()+1] );
					  rateIt.setRating( rateIt.getUserRating() );
				  }
			});
		lbChoice.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  choice.setResult( "" + lbChoice.getValue( lbChoice.getSelectedIndex() ) );
				  }
			});
		tbName.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  choice.setName( tbName.getText() );
				  choice.setDescription( tbName.getText() );
				  choice.updateFormElement();
				  starRating.setName( tbName.getText() );
				  starRating.setDescription( tbName.getText() );
				  starRating.updateFormElement();
			  }
		});

		//Add widgets to the panel
		if( editable ){
			hpChoicStarR.add( tbName );
		}
		else{
			hpChoicStarR.add( lName );
		}
		
		hpChoicStarR.add( lbChoice );
		hpChoicStarR.add( rateIt );
		return hpChoicStarR;
	}
	
	/* (non-Javadoc)
	 * @see org.imb.client.FormElement#deleteFormElement()
	 */
	@Override
	protected void deleteFormElement(){
		choice.deleteFormElement();
		starRating.deleteFormElement();
		hpFormElementePanel.removeFromParent();
		if( getParent()!= null && getParent() instanceof SubForm ){
			SubForm parent = (SubForm) getParent();
			parent.removeElement( choice );
			parent.removeElement( starRating );
		}
	}
	
	/**
	 * Method to define the width of the name
	 * 
	 * @param labelWidth	the width of the name's label
	 * @return panel		the normal panel of the form-item		
	 */
	public Widget getPanel(int labelWidth) {
		nameWidth = labelWidth+"px";
		return getPanel();
	}

}
