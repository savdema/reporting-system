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

import com.google.gwt.user.client.rpc.RemoteService;

public interface ReportingSystemService extends RemoteService {
	public FormElementS   getFormElement(int id);
	public FormElementS   addFormElement( FormElementS fes );
	public FormElementS[] getAllForms();
	public boolean        addResults( Result[] results);
	public FormElementS   updateFormElement( FormElementS formElementS );
	public Result[]       getResults(int formId );
	public boolean        addPageAssociation(int selectedIndex, FormElementS fes );
	public Page[]         getPages(); 
	public Page[]         getAssociatedPages(FormElementS fes ); 
	public boolean        removeAssociatedPage(int pageId, FormElementS fes); 
	public int            addPageAccessAndGetCount(String url, String userHashValue ); 
	public FormElementS   getForm(String url ); 
	public FormElementS   getParent(FormElementS fes);
	public FormElementS   deleteFormElement(FormElementS formElementS );
	public boolean        addSubmittedFormAccess(String url, String userHashValue, int formId);
	public User           getUser( String userHashValue );
	public Config         getConfig( String name );
	public Config         setConfig( String name, String value );
	public boolean        checkAdminPassword( String password );
}
