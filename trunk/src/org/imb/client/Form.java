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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;


public class Form extends FormElement{



	/** 
	 * @uml.property name="elements"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="composite" inverse="form:org.imb.client.FormElement"
	 */
	private Vector<FormElement> elements;
	
	private VerticalPanel vPanel;
	
	private FormElementS fes = null;
	
	private FeedbackDialog fd;
	
	private GlassPanel glassPanel;
	
	private VerticalPanel vpFormElements;
	
	private Vector<Widget> formWidgets = new Vector<Widget>();
	private Vector<Widget> pagebreakWidgets = new Vector<Widget>();
	
	private Button btnSubmit;
	private Button btnNext;
	private Panel pSubmitButtons;



	/**
	 * @uml.property  name="name"
	 */
	private String name = "";
	
	/**
	 * @uml.property  name="results"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="reportingSystem:org.imb.client.Result"
	 */
	private Collection<Result> results;
	
	private ListBox lbPageSelection;
	
	private Collection<Page> associatedPages = new Vector<Page>();
	
	private Page[]           allPages        = new Page[0];
	
	/**
	 */
	public Form(String name, Vector<FormElement> elements){
		super( -1, name, "", -1);
		this.name     = name;
		setElements( elements );
		getAssociatedPages();
	}
	
	public Form( FormElementS fes){
		super( fes.getPosition() , fes.getName() , fes.getDescription() , fes.getId() );
		this.name     = fes.getName();
		this.fes      = fes;
		this.elements = new Vector<FormElement>();
		parseSubFormElements( fes.getChildren() );
		getAssociatedPages();
	}
	
	/**
	*/
	public Form(String name){
		super( -1, name, "", -1);
		this.name = name; 
		this.elements = new Vector<FormElement>();
		getAssociatedPages();
	}


	/**
	 * Getter of the property <tt>name</tt>
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/** 
	 * Getter of the property <tt>elements</tt>
	 * @return  Returns the elements.
	 * @uml.property  name="elements"
	 */
	public Collection<FormElement>  getElements() {
		return elements;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * @param name  The name to set.
	 * @uml.property  name="name"
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	


		

		private void parseSubFormElements(FormElementS[] children) {
			if( children != null){
				for( int i=0 ; i < children.length ; i++ ){
					FormElementS fes = children[i];
					addElement( fes.getFormElement() );
				}
			}
		}

		/** 
		 * Setter of the property <tt>elements</tt>
		 * @param elements  The elements to set.
		 * @uml.property  name="elements"
		 */
		public void setElements(Vector<FormElement>  elements) {
			for (FormElement e : elements){
				e.setParent( this );
			}
			this.elements = elements;
		}

			

		
		public void addElement( FormElement formElement )
		{
			formElement.setParent( this );
			elements.add( formElement );
		}
		
		public void removeElement(FormElement fe) {
			for( FormElement e : elements){
				if( fe.equals( e ) || e.getId() == fe.getId() ){
					elements.remove( e );
				}
			}
			
		}
		
		@Override
		public Panel createPanel( boolean editable ){
			vPanel = new VerticalPanel();
			vPanel.setStyleName( "shortFeedback" );
			vPanel.setWidth( "500px");
			vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			vPanel.setSpacing( 20 );
			if( !editable ){
				//Create form elements
				vpFormElements = new VerticalPanel();
				createFormElements();
				vPanel.add( vpFormElements );
				pSubmitButtons = createSubmitButtons();
				vPanel.add( pSubmitButtons );
				return vPanel;
			}
			else{
								final TextBox tbTitle = new TextBox(  );
				tbTitle.setWidth( "400px");
				final Form stsf = this;
				tbTitle.addChangeListener(new ChangeListener() {
					  public void onChange(Widget sender) {
						  Form owner = stsf;
						  owner.setName( tbTitle.getText() );
						  fes.setName( name );
						  updateFormElement();
					  }
				});
				tbTitle.setText( name );
				vPanel.add( tbTitle );
				
				//Create editable form elements
				vpFormElements = new VerticalPanel();
				createEditFormElements();
				vPanel.add( vpFormElements );

				//Create new Element input...
				String[] creatableForms = { TextContainer.ADD_NEW_ELEMENT_CHOICE , 
											TextContainer.NEW_TEXTLABEL_NAME ,
											TextContainer.NEW_TEXTINPUT_NAME , 
											TextContainer.NEW_STARRATING_SUBFORM_NAME , 
											TextContainer.NEW_CHOICE_STARRATING_SUBFORM_NAME ,
											TextContainer.NEW_CHOICE_NAME ,
											TextContainer.NEW_CHECKBOX_NAME ,
											TextContainer.NEW_STARRATING_NAME ,
											TextContainer.NEW_ELEMENT_PAGEBREAK 
											};
				final ListBox listbox = new ListBox();
				for ( int i=0 ; i < creatableForms.length ; i++){
					listbox.addItem( creatableForms[i] );
				}
				listbox.addChangeListener(new ChangeListener() {
					public void onChange(Widget sender) {
						addNewElement( listbox.getSelectedIndex() );
						listbox.setSelectedIndex(0);
					}
				});
				
				vPanel.add( listbox );
				
				lbPageSelection = new ListBox(true);
				lbPageSelection.addChangeListener(new ChangeListener() {
					public void onChange(Widget sender) {
						for ( int i= 0 ; i < lbPageSelection.getItemCount() ; i++ ){
							int pageId = Integer.valueOf( lbPageSelection.getValue(i) ).intValue(); 
							if ( lbPageSelection.isItemSelected( i) ){
								
								setPageAssocaition( pageId );
							}
							else{
								removePageAssociation( pageId );
							}
						}
					}
				});
			
				
				final AsyncCallback<Page[]> pagesCallback = new AsyncCallback<Page[]>() {
	
					public void onFailure(Throwable caught) {
						ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
					}
	
					public void onSuccess(Page[] result) {
						Page[] pages = (Page[])result;
						allPages = pages;
						ReportingSystem.debugMessage( "Loaded all available pages successfully(#ass=" + associatedPages.size() + " ,#all=" + allPages.length + ")" );
						lbPageSelection.addItem( TextContainer.NO_PAGE_ASSOCIATION , -1+"" );
						for ( Page page : pages ){
							lbPageSelection.addItem( page.getUrl() , page.getId()+"" );
						}
						setPageAssociationsSelection();
					}
				  };
				  
				ReportingSystem.rsService.getPages( pagesCallback );
				VerticalPanel vp = new VerticalPanel();
				vp.add( vPanel );
				vp.add( listbox );
				vp.add( lbPageSelection );
				return vp;
			}
		}
		


		private void createFormElements() {
			sortElements();
			formWidgets      = new Vector<Widget>();
			pagebreakWidgets = new Vector<Widget>();
			boolean visible  = true;
			for( FormElement fe : elements)
			{
				if( fe.getPanel() != null ){
					Panel pFe = fe.getPanel();
					pFe.setVisible( visible );
					vpFormElements.add( pFe );
					formWidgets.add( pFe );
					if( fe instanceof Pagebreak ){
						visible = false;
						pagebreakWidgets.add( pFe );
					}
				}
			}
		}

		private void createEditFormElements() {
			sortElements();
			for( FormElement fe : elements)
			{
				if( fe.getPanel() != null ){
					Panel pFe = fe.getEditPanel();
					vpFormElements.add( pFe );
				}
			}
		}

		private void sortElements() {
			//sort elements by position
			String str = "";
			for( FormElement e : elements ){
				str += "POS=" + e.getPosition() + " NAME=" + e.getName() + "\n";   //ID=" + e.getId() + 
			}
			Collections.sort( elements , new Comparator<Object>() {
				public int compare(Object o1, Object o2) {
					return ((FormElement)o1).getPosition() - ((FormElement)o2).getPosition();
				}
			});
			str += "\n ******** sorting *************\n";
			for( FormElement e : elements ){
				str += "POS=" + e.getPosition() + " NAME=" + e.getName() + "\n";   //ID=" + e.getId() + 
			}
		}

		private void addNewElement( int type){
			FormElement e = null;
			final Form        thisform = this;
			int position = elements.size();
			switch( type ){
			  	case 1:
			  		e = new TextLabel( position , TextContainer.NEW_TEXTLABEL_NAME , TextContainer.NEW_TEXTLABEL_NAME , -1 );
			  		break;
			  	case 2:
			  		e = new TextInput( position , TextContainer.NEW_TEXTINPUT_NAME , TextContainer.NEW_TEXTINPUT_NAME , -1 );
			  		break;
			  	case 3:
			  		e = new StarRatingSubForm( position , TextContainer.NEW_STARRATING_SUBFORM_NAME , TextContainer.NEW_STARRATING_SUBFORM_NAME , -1 );	  		
			  		break;
			  	case 4:
			  		e = new ChoiceStarRatingSubForm( position , TextContainer.NEW_CHOICE_STARRATING_SUBFORM_NAME , TextContainer.NEW_CHOICE_STARRATING_SUBFORM_NAME , -1, "Auswahl", "Sterne" );	  		
			  		break;
			  	case 5:
			  		e = new Choice( position , TextContainer.NEW_CHOICE_NAME , TextContainer.NEW_CHOICE_NAME , -1 , new Vector<String>() );
			  		break;
			  	case 6:
			  		e = new CheckBoxElement( position , TextContainer.NEW_CHECKBOX_NAME , TextContainer.NEW_CHECKBOX_NAME , -1 , new Vector<String>() );
			  		break;
			  	case 7:
			  		e = new StarRating( position , TextContainer.NEW_STARRATING_NAME , TextContainer.NEW_STARRATING_NAME , -1 );
			  		break;
			  	case 8:
			  		e = new Pagebreak( position , "" , "" , -1 );
			  		break;
			}
			if( e != null){
				AsyncCallback<FormElementS> callback = new AsyncCallback<FormElementS>() {
					public void onFailure(Throwable caught) {
						ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
					}

					public void onSuccess(FormElementS result) {
						FormElementS fes = (FormElementS)result;
						ReportingSystem.debugMessage( "ID=" + fes.getId() + " NAME=" + fes.getName() );
						FormElement e = fes.getFormElement();
						ReportingSystem.debugMessage( "NAME=" + e.getName() );
						vPanel.add( e.getEditPanel() );
						thisform.addElement(e);
					}
					  
				};
				FormElementS tmpfes = new FormElementS( e );
				if ( fes != null ){
					tmpfes.setParent( fes );
				}
				ReportingSystem.rsService.addFormElement( tmpfes , callback );
				
			}
		}
		
		private Panel createSubmitButtons() {
			DockPanel dp = new DockPanel();
			dp.setWidth( "500px" );
			Button btnLater  = new Button( TextContainer.LATER );
			Button btnCancel = new Button( TextContainer.CANCEL );
			btnSubmit = new Button( TextContainer.SUBMIT );
			btnNext   = new Button( TextContainer.NEXT );
			
			btnLater.addClickListener(new ClickListener() {
				  public void onClick(Widget sender) {
					  hideFeedbackDialog();
				  }
				});
			btnCancel.addClickListener(new ClickListener() {
				  public void onClick(Widget sender) {
					  hideFeedbackDialog();
				  }
				});
			btnSubmit.addClickListener(new ClickListener() {
				  public void onClick(Widget sender) {
					  getResultsFromFormElements();
				  }
				});
			btnNext.addClickListener(new ClickListener() {
				  public void onClick(Widget sender) {
					  showNextPage();
				  }
				});
			
			dp.add( btnLater  , DockPanel.WEST );
			dp.add( btnCancel , DockPanel.WEST );
			if( pagebreakWidgets.size() > 0 ){
				dp.add( btnNext , DockPanel.EAST );
			}
			else{
				dp.add( btnSubmit , DockPanel.EAST );
			}
			return dp;
		}
		
		protected void showNextPage() {
			if( pagebreakWidgets.size() > 0 ){
				Widget wPagebreak = pagebreakWidgets.firstElement();
				int c = 0;
				for( Widget w : formWidgets){
					w.setVisible( false );
					w.removeFromParent();
					c++;
					if( w.equals( wPagebreak ) ){
						break;
					}
				}
				for( ; c < formWidgets.size() ; c++ ){
					formWidgets.get(c).setVisible( true );
					if( pagebreakWidgets.size()>1 && formWidgets.get(c).equals( pagebreakWidgets.get(1) )){
						break;
					}
				}
				pagebreakWidgets.remove( wPagebreak );
			}
			if( pagebreakWidgets.size() == 0 ){
				pSubmitButtons.remove( btnNext );
				((DockPanel)pSubmitButtons).add( btnSubmit , DockPanel.EAST );
			}
					
		}

		protected void hideFeedbackDialog() {
			fd.hide();
			// Some time later remove (hide) the glass panel
			glassPanel.removeFromParent();
		}

		private void getResultsFromFormElements(){
			AsyncCallback<User> callback = new AsyncCallback<User>() {
				public void onFailure(Throwable caught) {
					ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
				}

				public void onSuccess(User result) {
					User user = (User)result;
					getResultsFromFormElements( user );
				}
				  
			};
			ReportingSystem.rsService.getUser( ReportingSystem.getUserHashValue() , callback );			 
		}
		
		private void getResultsFromFormElements(User user) {
			List<Result> resultList = new LinkedList<Result>();
			String str = "";
			for( FormElement fe : elements){
				if( fe.getResult() != null     &&
					!( fe instanceof SubForm ) &&
					!( fe instanceof TextLabel )  
				  ){
					resultList.add( new Result( fe.getResult() , new FormElementS( fe ) , user  ) );
				}
				if( fe instanceof SubForm ){
					for( FormElement sfe: ((SubForm)fe).getElements() ){
						if( sfe.getResult() != null ){
							resultList.add( new Result( sfe.getResult() , new FormElementS( sfe ) , user  ) );
						}
					}
				}
			}
			Result[] results = new Result[resultList.size()];
			int      cnt     = 0;
			for (Result result : resultList){
				str += result.getFormElement().getName() + " " + result.getValue() + "\n";
				results[cnt++] = result;
			}
			saveResults( results );
			ReportingSystem.debugMessage( str );
		}
		
		private void saveResults( Result[] results ){
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
				}

				public void onSuccess(Boolean result) {
					ReportingSystem.debugMessage( "The result has been saved ==" + result );
					hideFeedbackDialog();
				}
				  
			};
			ReportingSystem.rsService.addResults(results, callback);
			ReportingSystem.rsService.addSubmittedFormAccess( ReportingSystem.getURL() , ReportingSystem.getUserHashValue() , this.id , callback );
		}
		

		/**
		 * Getter of the property <tt>results</tt>
		 * @return  Returns the results.
		 * @uml.property  name="results"
		 */
		public Collection<Result> getResults() {
			return results;
		}

		/**
		 * Setter of the property <tt>results</tt>
		 * @param results  The results to set.
		 * @uml.property  name="results"
		 */
		public void setResults(Collection<Result> results) {
			this.results = results;
		}
		
		public FormElementS getFormElementS(){
			return fes;
		}
		
		private void setPageAssocaition(int pageId ) {
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
				}

				public void onSuccess(Boolean result) {
					ReportingSystem.debugMessage( "Page Association has been saved successfully == " + result );
				}
				  
			};
			//Check if the page is already associated with this form
			for ( int i= 0 ; i < lbPageSelection.getItemCount() ; i++ ){
				for ( Page ap : associatedPages ){
					if(  pageId == ap.getId() ){
						return;
					}
				}
			} 
			ReportingSystem.rsService.addPageAssociation( pageId , fes  , callback );
			
		}
		
		private void getAssociatedPages() {
			AsyncCallback<Page[]> callback = new AsyncCallback<Page[]>() {
				public void onFailure(Throwable caught) {
					ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
				}

				public void onSuccess(Page[] result) {
					Page[] pages = (Page[])result;
					if( associatedPages.size() == 0 ){
						for ( Page page : pages ){
							associatedPages.add( page );
						}
						setPageAssociationsSelection();
					}

				}
				  
			};
			if( fes != null ){
				ReportingSystem.rsService.getAssociatedPages( fes  , callback );
			}
			
		}
		  
		private void setPageAssociationsSelection() {
			for ( int i= 0 ; i < lbPageSelection.getItemCount() ; i++ ){
				int pageId = Integer.valueOf( lbPageSelection.getValue(i) );
				for ( Page ap : associatedPages ){
					if(  pageId == ap.getId() ){
						lbPageSelection.setItemSelected( i , true );
					}
				}
			}
		}
		
		protected void removePageAssociation(int pageId) {
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					ReportingSystem.debugMessage("Sorry there was an error" + caught.getMessage() );
				}
				public void onSuccess(Boolean result) {
					ReportingSystem.debugMessage( "Page Association has been removed successfully == " + result +" (#ass=" + associatedPages.size() + " ,#all=" + allPages.length + ")" );
				} 
			};
			//Check if the page is an association with this form
			for ( Page ap : associatedPages ){
				if(  pageId == ap.getId() ){
					associatedPages.remove( ap );
					ReportingSystem.rsService.removeAssociatedPage( pageId , fes  , callback );
				}
			}
		}
		
		 @Override
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
		    	ReportingSystem.rsService.updateFormElement( fes , callback );
		    	
			}

		public void showFeedbackDialog() {
			// Create a glass panel with `autoHide = true`
			glassPanel = new GlassPanel(false);
			// Attach (display) the glass panel
			RootPanel.get().add( glassPanel, 0, 0);
			
			fd = new FeedbackDialog();
			fd.setAnimationEnabled( true );
			fd.setPopupPosition( 200 , 200 );
			fd.setWidget( getPanel() );
			fd.setText( name );
			fd.center();
			fd.show();
		}
		
		
		private static class FeedbackDialog extends DialogBox {

		    public FeedbackDialog() {
		      // Set the dialog box's caption.
		      setText("My First Dialog");

		      // DialogBox is a SimplePanel, so you have to set its widget property to
		      // whatever you want its contents to be.
		      Button ok = new Button("OK");
		      ok.addClickListener(new ClickListener() {
		        public void onClick(Widget sender) {
		        	FeedbackDialog.this.hide();
		        }
		      });
		      setWidget(ok);
		    }
		  }

		  public void onClick(Widget sender) {
		    // Instantiate the dialog box and show it.
		    new FeedbackDialog().show();
		  }

		public void updateFormElements() {
			vpFormElements.clear();
			createEditFormElements();
		}
		
		public void moveUp( FormElement fe ){
			int index = elements.indexOf( fe );
			if( index > 0 ){
				FormElement feAbove = elements.get( index-1 );
				int newPos = feAbove.getPosition();
				feAbove.setPosition( fe.getPosition() );
				fe.setPosition( newPos );
				feAbove.updateFormElement();
				fe.updateFormElement();
			}
			updateFormElements();
		}

		public void moveDown(FormElement fe) {
			int index = elements.indexOf( fe );
			if( index < elements.size()-1 ){
				FormElement feBelow = elements.get( index+1 );
				int newPos = feBelow.getPosition();
				feBelow.setPosition( fe.getPosition() );
				fe.setPosition( newPos );
				feBelow.updateFormElement();
				fe.updateFormElement();
			}
			updateFormElements();
		}
}
