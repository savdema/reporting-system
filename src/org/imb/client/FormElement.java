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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class FormElement extends Composite{

	private static final String DELETE_BUTTON_LABEL = "X";
	protected static final String TEXTAREA_WIDTH    = "400px";
	protected static final String TEXTBOX_WIDTH    = "150px";
	protected static final String MOD_ELEMENT_WIDTH = "26px";

	private FormElement parent = null;
	private String result = TextContainer.NOT_SPECIFIED;
	private int position;
	protected String name = "";
	protected String description = "";
	protected int id;
	
	protected HorizontalPanel hpFormElementePanel;
	protected TextBox tbName       ;
	protected TextBox tbDescription;
	protected Label   lName        ;
	
	public FormElement(int position, String name, String description, int id){
		this.position    = position;
		this.name        = name;
		this.description = description;
		this.id          = id;
	
		tbName        = new TextBox();
		tbDescription = new TextBox();
		lName         = new Label( name );
		
	    tbName.setText( getName() );
		tbDescription.setText( getDescription() );

		final FormElement thisFE = this;
		
		tbName.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  thisFE.setName( ((TextBox) sender).getText() );
				  thisFE.updateFormElement();
			  }
		});
		tbDescription.addChangeListener(new ChangeListener() {
			  public void onChange(Widget sender) {
				  thisFE.setDescription( ((TextBox) sender).getText() );
				  thisFE.updateFormElement();
			  }
		});
	}

	public int getPosition() {
		return position;
	}
	
	public void setParent( FormElement parent ){
		this.parent = parent;
	}
	
	@Override
	public FormElement getParent(){
		return this.parent;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Panel getPanel(){
		return createPanel( false );
	}
	
	public Panel getEditPanel(){
		hpFormElementePanel = new HorizontalPanel();
		hpFormElementePanel.add( createPanel( true ) );
		hpFormElementePanel.add( createDeleteButton() );
		if( !(this instanceof Form) && this.parent != null && !(this.parent instanceof SubForm) ){
			hpFormElementePanel.add( createPositionUpButton() );
			hpFormElementePanel.add( createPositionTextBox() );
			hpFormElementePanel.add( createPositionDownButton() );
		}
		return hpFormElementePanel;
	}

	protected Panel createPanel( boolean editable ){
		return new VerticalPanel();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	

    
    protected void updateFormElement() {
    	AsyncCallback<FormElementS> callback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
			}
			  
		};
    	ReportingSystem.rsService.updateFormElement( new FormElementS(this) , callback );
    	
	}
    
    protected void deleteFormElement(){
    	AsyncCallback<FormElementS> callback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS)result;
				ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() + " has been removed successfully");
				hpFormElementePanel.removeFromParent();
				if( getParent()!= null ){
					
					if( getParent() instanceof SubForm ){
						SubForm parent = (SubForm) getParent();
						parent.removeElement( fes.getFormElement() );
					}
					else if ( getParent() instanceof Form ){
						Form parent = (Form) getParent();
						parent.removeElement( fes.getFormElement() );
					}
				}
				else{
					if( fes.getType() == FormElementS.TYPE_FORM ){
						ReportingSystem.updateFormChoice();
					}
					Window.alert( "The parent is undefined");
				}
				
			}
			  
		};
    	ReportingSystem.rsService.deleteFormElement( new FormElementS(this) , callback );
	}
    
    protected Button createDeleteButton(){
    	Button btnDelete = new Button( DELETE_BUTTON_LABEL );
    	btnDelete.setWidth( MOD_ELEMENT_WIDTH );
    	btnDelete.setHeight( MOD_ELEMENT_WIDTH );
    	btnDelete.addClickListener(new ClickListener() {
			  public void onClick(Widget sender) {
				  deleteFormElement();
			  }
			});
    	return btnDelete;
    }
    
    protected Button createPositionUpButton(){
    	Button btnPositionUp = new Button( TextContainer.POSITION_UP_BUTTON_LABEL );
    	btnPositionUp.setWidth( MOD_ELEMENT_WIDTH );
    	btnPositionUp.setHeight( MOD_ELEMENT_WIDTH );
    	btnPositionUp.addClickListener(new ClickListener() {
			  public void onClick(Widget sender) {
				  updatePosition( -1 );
			  }
			});
    	return btnPositionUp;
    }
    
	private Widget createPositionTextBox() {
    	TextBox tbPosition = new TextBox();
    	tbPosition.setText( this.position + "" );
    	tbPosition.setWidth( MOD_ELEMENT_WIDTH );
    	tbPosition.setHeight( MOD_ELEMENT_WIDTH );
    	tbPosition.addChangeListener( new ChangeListener() {
			  public void onChange(Widget sender) {
				  
				  setPosition( Integer.valueOf( ((TextBox)sender).getText()  ).intValue() );
				  updateFormElement();
				  if( getParent() instanceof Form ){
					  ((Form)getParent()).updateFormElements();
				  }
			  }
			});
    	return tbPosition;
	}
    
    protected void updatePosition(int i) {
    	if( getParent() instanceof Form ){
    		if( i < 0 ){
    			((Form)getParent()).moveUp( this );
    		}
    		else{
    			((Form)getParent()).moveDown( this );
    		}
    	}
	}


	protected Button createPositionDownButton(){
    	Button btnPositionUp = new Button( TextContainer.POSITION_DOWN_BUTTON_LABEL );
    	btnPositionUp.setWidth( MOD_ELEMENT_WIDTH );
    	btnPositionUp.setHeight( MOD_ELEMENT_WIDTH );
    	btnPositionUp.addClickListener(new ClickListener() {
			  public void onClick(Widget sender) {
				  updatePosition( 1 );
			  }
			});
    	return btnPositionUp;
    }
}
