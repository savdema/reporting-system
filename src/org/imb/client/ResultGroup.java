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


public class ResultGroup {
		
	private Vector<Result>  results;
	private FormElementS    formElement;
	private Vector<String>  labels            = null;
	private boolean         calculatedResults = false;
	private String          name              = "";
	private Vector<ValueGroup> valueGroups;
	
	private class ValueGroup{
		private String label;
		private Vector<Result> results;
		
		public ValueGroup( String label ){
			this.label = label;
			this.results = new Vector<Result>();
		}
		
		public void addResult( Result result){
			this.results.add( result );
		}
		
		public int getCount(){
			return this.results.size();
		}
		
	}
		
	public ResultGroup(FormElementS formElement){
		this.formElement = formElement;
		this.name        = formElement.getName();
		this.results     = new Vector<Result>();
	}

	public FormElementS getFormElement() {
		return formElement;
	}

	public void setFormElement(FormElementS formElement) {
		this.formElement = formElement;
	}

	public void addResult(Result result) {
		if( results == null ){
			results = new Vector<Result>();
		}
		if( formElement.getType() == FormElementS.TYPE_CHECKBOX ){
			splitResult( result );
		}
		else{
			results.add( result );
		}
		
	}
	
	private void splitResult(Result result) {
		String[] aValue = result.getValue().split(";");
		for( String v : aValue ){
			results.add( new Result( v , result.getFormElement() , result.getUser() ) );
		}
	}

	public Vector<Result> getResults(){
		return results;
	}
	
	private void calculateResults(){
		defineLabels();
		valueGroups = createValueGroups( labels );	
		for ( Result result : results ){
			for( ValueGroup vg : valueGroups ){
				if( result.getValue().equals( vg.label ) ){
					vg.addResult(result);
				}
			}
		}
		calculatedResults = true;
	}
	
	private Vector<ValueGroup> createValueGroups(Vector<String> labels2) {
		Vector<ValueGroup> vg = new Vector<ValueGroup>();
		for( String l : labels2 ){
			vg.add( new ValueGroup( l ) );
		}
		return vg;
	}

	private void defineLabels(){
		labels = new Vector<String>();
		if( formElement != null ){
			if( formElement.getType() == FormElementS.TYPE_STARRATING ){
				for( String l : TextContainer.STARRATING_LABELS ){
					labels.add( l );
				}
			}
			else if( formElement.getType() == FormElementS.TYPE_CHECKBOX  ||
					 formElement.getType() == FormElementS.TYPE_CHOICE
					){
				for( Result r : results ){
					if( indexOfString( labels , r.getValue() ) < 0 ){
						labels.add( r.getValue() );
					}
				}
			}
		}
	}

	public int getTotalCount() {
		return results.size();
	}

	public String getName() {
		return name;
	}
	
	public void setName( String name ){
		this.name = name;
	}
	
	public Vector<String> getLabels(){
		if( labels == null ){
			defineLabels();
		}
		return labels;
	}
	
	public Vector<Result> getResults( String str ){
		if( !calculatedResults ){
			calculateResults();
		}
		for( ValueGroup vg : valueGroups ){
			if( vg.label.equals( str ) ){
				return vg.results;
			}
		}
		return new Vector<Result>();
	}

	public void setResults(Vector<Result> results2) {
		this.results = results2;
	}
	
	private int indexOfString(Vector<String> valueLabels, String value) {
		for ( int i = 0 ; i < valueLabels.size() ; i++ ){
			if( value.equals( valueLabels.get( i ) ) ){
				return i;
			}
		}
		return -1;
	}

	public int getAbsoluteCount(String str) {
		if( !calculatedResults ){
			calculateResults();
		}
		for( ValueGroup vg : valueGroups ){
			if( vg.label.equals( str ) ){
				return vg.getCount();
			}
		}
		return 0;
	}
	
	public double getDue(String str) {
		return (double)getAbsoluteCount(str) / (double)getTotalCount();
	}

}
