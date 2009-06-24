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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class StarRating extends FormElement {
	
	protected static final Image URL_IMG_GREY   = new Image(GWT.getModuleBaseURL()+"star_grey.png");
	protected static final Image URL_IMG_ORANGE = new Image(GWT.getModuleBaseURL()+"star_orange.png");
	private static final String  NAME_WIDTH     = "75px";
	private String nameWidth = NAME_WIDTH;

	public StarRating(int position, String name, String description, int id){
		super( position, name, description, id);
	}
	
	@Override
	public Panel createPanel( boolean editable )
	{
		HorizontalPanel hpStarRating = new HorizontalPanel();
		final StarRating thisStarRating = this;
		final RateItWidget rateIt = new RateItWidget( 0 , 2 , URL_IMG_GREY, URL_IMG_ORANGE, URL_IMG_GREY, URL_IMG_ORANGE, URL_IMG_GREY);
		rateIt.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  thisStarRating.setResult( TextContainer.STARRATING_LABELS[rateIt.getUserRating()+1] );
				  rateIt.setRating( rateIt.getUserRating() );
			  }
		});
		if( editable ){
			Panel pNameDescription = new HorizontalPanel();
			final TextBox tbName        = new TextBox();
			final TextBox tbDescription = new TextBox();
			tbName.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  thisStarRating.setName( tbName.getText() );
					  thisStarRating.updateFormElement();
				  }
			});
			tbDescription.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  thisStarRating.setDescription( tbDescription.getText() );
					  thisStarRating.updateFormElement();
				  }
			});
			tbName.setText( getName() );
			tbDescription.setText( getDescription() );
			pNameDescription.add( tbName);
			pNameDescription.add( tbDescription);
			hpStarRating.add( pNameDescription );
		}
		else{
			Label lName = new Label( getName() );
			lName.setWidth( nameWidth );
			if( !getDescription().equals( "") ){
				lName.addMouseListener( new TooltipListener( getDescription(), 5000 /* timeout in milliseconds*/,"tooltip") );
			}
			hpStarRating.add( lName );
		}
		hpStarRating.add( rateIt );
		return hpStarRating;
	}

	public Widget getPanel(int labelWidth) {
		nameWidth = labelWidth+"px";
		return getPanel();
	}
	
}
