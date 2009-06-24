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

/**
 * User contains the attributes id and hash-value 
 * 
 * @author Johannes Metscher
 */
public class User implements IsSerializable {
	
	private int    id;
	private String hashvalue;
	
	/**
	 * Constructor
	 */
	public User(){
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param id		the id of the user (db generated)
	 * @param hashvalue	the hash-value of the user
	 */
	public User( int id , String hashvalue ){
		this.id        = id;
		this.hashvalue = hashvalue;
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
	 * Getter of the property <tt>hashvalue</tt>
	 * @return  Returns the hashvalue.
	 * @uml.property  name="hashvalue"
	 */
	public String getHashvalue(){
		return hashvalue;
	}
	
	/**
	 * Setter of the property <tt>hashvalue</tt>
	 * @param hashvalue  The hashvalue to set.
	 * @uml.property  name="hashvalue"
	 */
	public void setHashvalue( String hashvalue ){
		this.hashvalue = hashvalue;
	}


}
