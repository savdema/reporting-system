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

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

public class Pagebreak extends FormElement {

	public Pagebreak(int position, String name, String description, int id) {
		super(position, name, description, id);
	}
	
	@Override
	public Panel createPanel( boolean editable ){
		hpFormElementePanel = new HorizontalPanel();
		if( editable ){
			Label lPageBreak = new Label( "----------------- " + TextContainer.PAGEBREAK + " ------------------" );
			lPageBreak.setWidth( "406px" );
			lPageBreak.setStyleName( "rp_admin_pagebreak");
			lPageBreak.setHorizontalAlignment(  HasHorizontalAlignment.ALIGN_CENTER );
			hpFormElementePanel.add( lPageBreak );
		}
		return hpFormElementePanel;
	}
}
