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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * TextLabel implements all necessary methods to visualize 
 * the form-element label in the normal and edit mode. 
 * 
 * @author Johannes Metscher
 */
public class TextLabel extends FormElement {
	
	/**
	 * Constructor of the class TextLabel
	 * 
	 * @param position		the position of the form-element in the (sub)form 
	 * @param name			the name of the form-element
	 * @param description	the description of the form-element contains all 
	 * @param id			the id of the form-element (will be set via db)
	 */
	public TextLabel(int position, String name, String description, int id){
		super( position, name, description, id);

	}
	
	/* (non-Javadoc)
	 * @see org.imb.client.FormElement#createPanel(boolean)
	 */
	@Override
	public Panel createPanel( boolean editable )
	{
		VerticalPanel vPanel = new VerticalPanel();
		if ( editable){
			final TextArea tbDescription = new TextArea(  );
			tbDescription.setWidth( TEXTAREA_WIDTH );
			final FormElement stsf = this;
			tbDescription.addChangeListener(new ChangeListener() {
				  public void onChange(Widget sender) {
					  FormElement owner = stsf;
					  owner.setName( tbDescription.getText() );
					  owner.setDescription( tbDescription.getText() );
					  updateFormElement();
				  }
			});
			tbDescription.setText( description );
			vPanel.add( tbDescription );
		}
		else{
			HTML htmlDescription = new HTML( description );
			vPanel.add( htmlDescription );
		};
		return vPanel;
	}
}
