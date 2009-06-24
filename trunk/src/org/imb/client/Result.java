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

import com.google.gwt.user.client.rpc.IsSerializable;


public class Result implements IsSerializable {

	/**
	 * @uml.property  name="value"
	 */
	private String value;
	
	/**
	 * @uml.property  name="formElement"
	 * @uml.associationEnd  inverse="result1:org.imb.client.FormElement"
	 */
	private FormElementS formElement;
	
	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="result:org.imb.client.User"
	 */
	private User user;
	
	private int  id = -1;

	
	public Result(){
	}
	
	public Result( String value , FormElementS formElement , User user ){
		this.value       = value;
		this.formElement = formElement;
		this.user        = user;
	}

	/**
	 * Getter of the property <tt>value</tt>
	 * @return  Returns the value.
	 * @uml.property  name="value"
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter of the property <tt>value</tt>
	 * @param value  The value to set.
	 * @uml.property  name="value"
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * Getter of the property <tt>formElement</tt>
	 * @return  Returns the formElement.
	 * @uml.property  name="formElement"
	 */
	public FormElementS getFormElement() {
		return formElement;
	}

	/**
	 * Setter of the property <tt>formElement</tt>
	 * @param formElement  The formElement to set.
	 * @uml.property  name="formElement"
	 */
	public void setFormElement(FormElementS formElement) {
		this.formElement = formElement;
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

	
	public void setId(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}


}
