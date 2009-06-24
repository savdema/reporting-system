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

import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FormElementS implements IsSerializable {
	private int            id = -1;
	private int            position;
	private String         name;
	private String         description;
	private FormElementS   parent   = null;
	private FormElementS[] children = new FormElementS[0];
	private int            type;
	
	protected static final int TYPE_FORM                      = 1;
	protected static final int TYPE_LABEL                     = 2;
	protected static final int TYPE_INPUT                     = 3;
	protected static final int TYPE_STARRATING                = 4;
	public    static final int TYPE_CHOICE                    = 5;
	protected static final int TYPE_STARRATING_SUBFORM        = 6;
	protected static final int TYPE_CHOICE_STARRATING_SUBFORM = 7;
	protected static final int TYPE_COLUMN_CAPTION            = 8;
	protected static final int TYPE_CHOICE_LABELS             = 9;
	protected static final int TYPE_CHECKBOX                  = 10;
	protected static final int TYPE_PAGEBREAK                 = 11;
	
	private static final String[] TYPE_NAMES = { "","Formular" , "Beschriftung" , "Textfeld" , "Sternbewertung" , "Auswahlfeld" , 
											     "Sternbwertungsseinheit" , "Auswahl- und Sternbewertungseinheit" , "Spaltenbeschriftung" , 
											     "Auswahlbeschriftung" , "Häkchenfeld" , "Seitenumbruch" };
	
	public FormElementS( ){
	}
	
	public FormElementS( int position, String name, String description, int type ){
		this.position    = position;
		this.name        = name;
		this.description = description;
		this.type        = type;
	}
	
	public FormElementS( int position, String name, String description, int type , int id ){
		this.position    = position;
		this.name        = name;
		this.description = description;
		this.type        = type;
		this.id          = id;
	}
	
	public FormElementS( FormElement fe){
		this.name        = fe.getName();
		this.description = fe.getDescription();
		this.position    = fe.getPosition();
		this.type        = -1;
		this.id          = fe.getId();
		
		if ( fe instanceof TextLabel ){
			this.type = TYPE_LABEL;
    	}
    	else if ( fe instanceof TextInput ){
			this.type = TYPE_INPUT;
    	}
    	else if ( fe instanceof StarRating ){
			this.type = TYPE_STARRATING;
    	}
    	else if ( fe instanceof Choice ){
			this.type = TYPE_CHOICE;
    	}
    	else if ( fe instanceof StarRatingSubForm ){
			this.type = TYPE_STARRATING_SUBFORM;
    	}
    	else if ( fe instanceof ChoiceStarRatingSubForm ){
			this.type = TYPE_CHOICE_STARRATING_SUBFORM;
    	}
    	else if ( fe instanceof CheckBoxElement ){
			this.type = TYPE_CHECKBOX;
    	}
    	else if ( fe instanceof Form ){
			this.type = TYPE_FORM;
    	}
    	else if ( fe instanceof Pagebreak ){
			this.type = TYPE_PAGEBREAK;
    	}
	}
	
	/**
	 * Getter of the property <tt>id</tt>
	 * @return  Returns the id.
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter of the property <tt>id</tt>
	 * @param id  The id to set.
	 * @uml.property  name="id"
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * Getter of the property <tt>position</tt>
	 * @return  Returns the position.
	 * @uml.property  name="position"
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Setter of the property <tt>position</tt>
	 * @param position  The position to set.
	 * @uml.property  name="position"
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Getter of the property <tt>name</tt>
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * @param name  The name to set.
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter of the property <tt>description</tt>
	 * @return  Returns the description.
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter of the property <tt>description</tt>
	 * @param description  The description to set.
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setType( int type ) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void setChildren( FormElementS[] children ){
		this.children = children;
	}
	
	public FormElementS[] getChildren(){
		return children;
	}
	
	@Override
	public String toString(){
		return "id=" + id + "position="+position+" name="+name+" description="+description+"type="+type;
	}

	public void setParent(FormElementS formElementS) {
		this.parent = formElementS;	
	}

	public FormElementS getParent() {
		return parent;
	}

	public FormElement getFormElement() {
		FormElement fe = null;
		switch( type ){
			case TYPE_FORM:
				fe = new Form( name  ) ;
				break;
			case TYPE_LABEL:
				fe = new TextLabel( position , name , description , id ) ;
				break;
			case TYPE_INPUT:
				fe = new TextInput( position , name , description , id ) ;
				break;
			case TYPE_STARRATING:
				fe = new StarRating( position , name , description , id ) ;
				break;
			case TYPE_CHOICE:
				fe = new Choice( position , name , description , id , new Vector<String>() ) ;
				break;
			case TYPE_STARRATING_SUBFORM:
				fe = new StarRatingSubForm( position , name , description , id  ) ;
				defineSubFormElements( (SubForm)fe );
				break;
			case TYPE_CHOICE_STARRATING_SUBFORM:
				fe = new ChoiceStarRatingSubForm( position , name , description , id , "test" , "test" ) ;
				defineSubFormElements( (SubForm)fe );
				break;
			case TYPE_CHECKBOX:
				fe = new CheckBoxElement( position , name , description , id , new Vector<String>() ) ;
				break;
			case TYPE_PAGEBREAK:
				fe = new Pagebreak( position , name , description , id ) ;
				break;
		}
		if( parent != null ){
			fe.setParent( parent.getFormElement() );
		}
		return fe;
	}
	
	private SubForm defineSubFormElements( SubForm sf ){
		if( children != null ){
			for( FormElementS child : children ){
				if( child.getType() == TYPE_STARRATING ||
				    child.getType() == TYPE_CHOICE
					){
					sf.addElement( child.getFormElement() );
				}
				else if( child.getType() == TYPE_COLUMN_CAPTION && sf instanceof ChoiceStarRatingSubForm ){
					((ChoiceStarRatingSubForm)sf).setColumnCaption( child );
				}
				else if( child.getType() == TYPE_CHOICE_LABELS && sf instanceof ChoiceStarRatingSubForm ){
					((ChoiceStarRatingSubForm)sf).setChoiceLabels( child );
				}
			}
		}
		return sf;
	}

	public String getTypeName() {
		return TYPE_NAMES[type];
	}
		 
}