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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportingSystemServiceAsync {
	public void getFormElement( int id, AsyncCallback<FormElementS> callback);
	public void addFormElement( FormElementS fes , AsyncCallback<FormElementS> callback);
	public void getAllForms( AsyncCallback<FormElementS[]> callback );
	public void addResults( Result[] results , AsyncCallback<Boolean> callback );
	public void updateFormElement(FormElementS formElementS, AsyncCallback<FormElementS> callback);
	public void getResults(int formId, AsyncCallback<Result[]> callback);
	public void addPageAssociation(int selectedIndex, FormElementS fes, AsyncCallback<Boolean> callback);
	public void getPages(AsyncCallback<Page[]> pagesCallback);
	public void getAssociatedPages(FormElementS fes, AsyncCallback<Page[]> callback);
	public void removeAssociatedPage(int pageId, FormElementS fes,AsyncCallback<Boolean> callback);
	public void addPageAccessAndGetCount(String url, String userHashValue, AsyncCallback<Integer> paCallback);
	public void getForm(String url, AsyncCallback<FormElementS> formCallback);
	public void getParent(FormElementS fes, AsyncCallback<FormElementS> formCallback);
	public void deleteFormElement(FormElementS formElementS, AsyncCallback<FormElementS> callback);
	public void addSubmittedFormAccess(String url, String userHashValue, int formId, AsyncCallback<Boolean> callback);
	public void getUser( String userHashValue , AsyncCallback<User> callback);
	public void getConfig( String name , AsyncCallback<Config> configCallback);
	public void setConfig( String name, String value  , AsyncCallback<Config> callback);
	public void checkAdminPassword( String password , AsyncCallback<Boolean> callback);
}
