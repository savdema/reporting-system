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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.GlassPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ReportingSystem implements EntryPoint {
	private static final boolean DEBUG_ENABLED = false;
	protected static final String FREQUENCY_CONFIG_NAME = "frequency";

	Panel pPreview;
	Panel pEdit;
	ReportingChart pAnalysis;
	Panel pAdmin;
	Button btnEdit;
	Button btnPreview;
	Form form;
	static ListBox lbFormSelection;
	static TabPanel tPanel;
	GlassPanel glassPanel;
	DialogBox dbAdminLogin;

	ReportingSystemService service;
	public static ReportingSystemServiceAsync rsService;

	AsyncCallback<FormElementS> formCallback;

	FlowPanel fpPreview;
	FlowPanel fpEdit;
	FlowPanel fpAnalysis;
	FlowPanel fpAdmin;
	
	private int accessCount = 0;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// Init rsService
		rsService = (ReportingSystemServiceAsync) GWT
				.create(ReportingSystemService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) rsService;
		String moduleRelativeURL = GWT.getModuleBaseURL()
				+ "reportingSystemService";
		endpoint.setServiceEntryPoint(moduleRelativeURL);
		//initAdminPanel();
		if ( adminMode() ) {
			showAdminLoginDialog();
		} else {
			initFeedbackForm();
		}
		if (DEBUG_ENABLED) {
			RootPanel.get().add(new Label("Reporting System is enabled"));
		}

	}
	
	public static boolean adminMode(){
		return getURL().contains("ReportingSystemAdmin.html");
	}

	public native static String getUserHashValue()/*-{
	  		return $wnd.hs;
	  	}-*/;

	public native static String getURL()/*-{
			return $wnd.document.URL;
		}-*/;

	private void updatePanels() {
		pPreview.removeFromParent();
		pPreview = form.getPanel();
		fpPreview.add(pPreview);

		updateEditPanel();

		pAnalysis.removeFromParent();
		pAnalysis = new ReportingChart(form);
		fpAnalysis.add(pAnalysis);
	}
	
	protected void updateEditPanel(){
		pEdit.removeFromParent();
		pEdit = form.getEditPanel();
		fpEdit.add(pEdit);
	}

	public Form createExampleForm() {
		int id = 0;
		String formName = "Kurzfeedback zum Bereich \"Fortbildung\"";
		Form exampleForm = new Form(formName);
		exampleForm.addElement(new TextLabel(
						0,
						"Willkommen und Erklärung",
						"Unterstützen Sie uns, die Fortbildungsplattform weiter zu verbessern, indem Sie uns eine Rückmeldung geben. Klicken Sie dazu die Sterne an.",
						id++));
		SubForm starRatings = new StarRatingSubForm(1, "Bereichsbewertung",
				"Am Angebot im Bereich Fortbildung gefällt mir:", id++);
		starRatings
				.addElement(new StarRating(0, "Lernpfade", "Lernpfade", id++));
		starRatings.addElement(new StarRating(1, "Vielfalt", "Vielfalt", id++));
		exampleForm.addElement(starRatings);
		Vector<String> frequencyCaptions = new Vector<String>();
		frequencyCaptions.add("");
		frequencyCaptions.add("nie");
		frequencyCaptions.add("selten");
		frequencyCaptions.add("oft");
		SubForm choiceStarRatings = new ChoiceStarRatingSubForm(
				2,
				"Häufigkeit und Unterstützung",
				"Wie häufig haben Sie nachfolgende Angebote genutzt und wie sehr haben diese Ihr Lernen unterstützt?",
				id++, "Nutzen", "Häufigkeit");
		choiceStarRatings.addElement(new StarRating(1, "Lernpfade",
				"Lernpfade", id++));

		exampleForm.addElement(choiceStarRatings);
		exampleForm.addElement( new Pagebreak( 2 , "" , "" , -1 ) );
		exampleForm.addElement(new CheckBoxElement( 3 , "testcheckbox" , "test1;test2;test3" , -1 , new Vector<String>()));
		exampleForm
				.addElement(new TextInput(
						3,
						"Fortbildungsoptimierung",
						"Was ist an den Angeboten im Bereich \"Fortbildung\" zu optimieren:",
						id++));
		return exampleForm;
	}

	public static void debugMessage(String str) {
		if (DEBUG_ENABLED) {
			Window.alert(str);
		}
	}

	private void initAdminPanel() {
		form = new Form("Noch kein Formular ausgewählt");
		pPreview = form.getPanel();
		pEdit = form.getEditPanel();
		pAnalysis = new ReportingChart(form);
		btnPreview = new Button("Vorschau");

		formCallback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				Window.alert("Sorry there was an error: " + caught.getMessage());
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS) result;
				form = new Form(fes);
				updatePanels();
				tPanel.selectTab(2);
			}
		};

		final AsyncCallback<FormElementS> newFormCallback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				Window.alert("Sorry there was an error: " + caught.getMessage());
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS) result;
				form             = new Form(fes);
				updatePanels();
				tPanel.selectTab(2);
				lbFormSelection.setItemText(lbFormSelection.getItemCount() - 1,	fes.getName());
				lbFormSelection.setValue(lbFormSelection.getItemCount() - 1, fes.getId() + "");
				lbFormSelection.setSelectedIndex(lbFormSelection.getItemCount() - 1);
				lbFormSelection.addItem( TextContainer.CREATE_NEW_FEEDBACK_FORM, "0");

			}
		};

		final AsyncCallback<FormElementS[]> formsCallback = new AsyncCallback<FormElementS[]>() {
			public void onFailure(Throwable caught) {
				Window.alert("Sorry there was an error: " + caught.getMessage());
			}

			public void onSuccess(FormElementS[] result) {
				FormElementS[] forms = (FormElementS[]) result;
				for (int i = 0; i < forms.length; i++) {
					lbFormSelection.addItem(forms[i].getName(), forms[i]
							.getId()
							+ "");
				}
				lbFormSelection.addItem( TextContainer.CREATE_NEW_FEEDBACK_FORM, "0");
			}
		};

		rsService.getAllForms(formsCallback);

		tPanel     = new TabPanel();
		fpPreview  = new FlowPanel();
		fpEdit     = new FlowPanel();
		fpAnalysis = new FlowPanel();
		fpAdmin    = new FlowPanel();

		FlowPanel fpFormSelection = new FlowPanel();
		
		tPanel.addTabListener(new TabListener() {
		      public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		    	  updatePanels();
		      }
		    
		      public boolean onBeforeTabSelected(SourcesTabEvents sender,  int tabIndex) {
		        return true;
		        
		      }
		    });

		lbFormSelection = new ListBox();
		lbFormSelection.addItem("Auswahl des Feedback Formulars");
		lbFormSelection.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				int formId = Integer.valueOf(
						lbFormSelection.getValue(lbFormSelection
								.getSelectedIndex())).intValue();
				if (formId > 0) {
					rsService.getFormElement(formId, formCallback);
				} else {
					rsService.addFormElement(new FormElementS(0,
							TextContainer.NEW_FEEDBACK_FORM, TextContainer.NEW_FEEDBACK_FORM,
							FormElementS.TYPE_FORM), newFormCallback);
				}

			}
		});
		fpFormSelection.add(lbFormSelection);

		Button btnRefresh = new Button("Aktualisieren");
		btnRefresh.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				updatePanels();
			}
		});
		
		fpPreview.add(pPreview);
		pEdit = form.getEditPanel();
		fpEdit.add(pEdit);
		fpAnalysis.add(pAnalysis);
		fpAdmin.add( createAdminPanel() );
		
		tPanel.add( fpFormSelection , TextContainer.FORM_SELECTION_TAB_LABEL );
		tPanel.add( fpPreview       , TextContainer.PREVIEW_TAB_LABEL );
		tPanel.add( fpEdit          , TextContainer.EDIT_TAB_LABEL );
		tPanel.add( fpAnalysis      , TextContainer.ANALYSIS_TAB_LABEL );
		tPanel.add( fpAdmin         , TextContainer.ADMIN_TAB_LABEL );

		tPanel.selectTab(0);

		tPanel.setSize("500px", "250px");
		tPanel.addStyleName("table-center");
		HTML hTilte = new HTML("<h1>" + TextContainer.APPLICATION_NAME + "</h1>"
				+ TextContainer.APPLICATION_VERSION + "<br><br><br>");
		RootPanel.get().add(hTilte);
		RootPanel.get().add(tPanel);
	}

	private Widget createAdminPanel() {
		VerticalPanel         vpAdmin         = new VerticalPanel();
		HorizontalPanel       hpFrequency     = new HorizontalPanel();
		HorizontalPanel       hpPassword      = new HorizontalPanel();
		HorizontalPanel       hpPassword2     = new HorizontalPanel();		
		Label                 lFrequency      = new Label( TextContainer.FREQUENCY_LABEL );
		final TextBox         tbFrequency     = new TextBox();
		Label                 lPassword       = new Label( TextContainer.PASSWORD_LABEL );
		Label                 lPassword2      = new Label( TextContainer.PASSWORD_LABEL );
		final PasswordTextBox ptbPassword     = new PasswordTextBox();
		final PasswordTextBox ptbPassword2    = new PasswordTextBox();
		final Button          btnSavePassword = new Button( TextContainer.SAVE_PASSWORD_LABEL );
		final Label           lStatus         = new Label("");
		
		
		final AsyncCallback<Config> configFrequencyCallback = new AsyncCallback<Config>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Config result) {
				Config config = (Config)result;
				tbFrequency.setText( config.getValue() );
			}
		};
		
		final AsyncCallback<Config> configPasswordCallback = new AsyncCallback<Config>() {
			public void onFailure(Throwable caught) {}
			public void onSuccess(Config result) {
				lStatus.setText( TextContainer.PASSWORD_SAVED_FEEDBACK );
			}
		};
				
		rsService.getConfig( FREQUENCY_CONFIG_NAME , configFrequencyCallback);
		
		tbFrequency.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				rsService.setConfig( FREQUENCY_CONFIG_NAME , tbFrequency.getText() , configFrequencyCallback );
			}
		});
		
		ptbPassword.addChangeListener( new ChangeListener() {
			public void onChange(Widget sender) {
				if( ptbPassword.getText().equals( ptbPassword2.getText() ) ){
					btnSavePassword.setEnabled( true );
					lStatus.setText("");
				}
				else{
					btnSavePassword.setEnabled( false );
					lStatus.setText( TextContainer.PASSWORD_NOT_EQUALS_MESSAGE);
				}
			}
		});
		
		ptbPassword2.addChangeListener( new ChangeListener() {
			public void onChange(Widget sender) {
				if( ptbPassword.getText().equals( ptbPassword2.getText() ) ){
					btnSavePassword.setEnabled( true );
					lStatus.setText("");
				}
				else{
					btnSavePassword.setEnabled( false );
					lStatus.setText( TextContainer.PASSWORD_NOT_EQUALS_MESSAGE);
				}
			}
		});
		
		btnSavePassword.setEnabled( false );
		btnSavePassword.addClickListener( new ClickListener() {
			public void onClick(Widget sender) {
				if( ptbPassword.getText().equals( ptbPassword2.getText() ) ){
					rsService.setConfig( TextContainer.ADMIN_PASSWORD_CONFIG_NAME , ptbPassword.getText() , configPasswordCallback );
				}	
			}
		});
		
		hpFrequency.add( lFrequency );
		hpFrequency.add( tbFrequency );
		hpPassword.add( lPassword );
		hpPassword.add( ptbPassword );
		hpPassword2.add( lPassword2 );
		hpPassword2.add( ptbPassword2 );
		vpAdmin.add( new HTML( "<br><h2>" + TextContainer.FREQUENCY_LABEL + "</h2>") );
		vpAdmin.add( hpFrequency );
		vpAdmin.add( new HTML( "<br><h2>" + TextContainer.PASSWORD_LABEL + "</h2>") );
		vpAdmin.add( hpPassword );
		vpAdmin.add( hpPassword2 );
		vpAdmin.add( btnSavePassword );
		vpAdmin.add( lStatus );
		return vpAdmin;
	}

	private void initFeedbackForm() {
		final AsyncCallback<FormElementS> formCallback = new AsyncCallback<FormElementS>() {
			public void onFailure(Throwable caught) {
				debugMessage("Sorry there was an error: " + caught.getMessage());
			}

			public void onSuccess(FormElementS result) {
				FormElementS fes = (FormElementS) result;
				if (fes != null) {
					form = new Form(fes);
					form.showFeedbackDialog();
				}
			}
		};
		
		final AsyncCallback<Config> frequencyCallback = new AsyncCallback<Config>() {
			public void onFailure(Throwable caught) {
				debugMessage("Sorry there was an error: " + caught.getMessage());
			}

			public void onSuccess(Config result) {
				Config cFrequency = (Config) result;
				int    iFrequency = Integer.valueOf( cFrequency.getValue() ).intValue();
				if (   getAccessCount() > 0 && 
					 ( getAccessCount() % iFrequency ) == 0 
					) {
					rsService.getForm(getURL(), formCallback);
				}
			}
		};

		final AsyncCallback<Integer> paCallback = new AsyncCallback<Integer>() {
			public void onFailure(Throwable caught) {
				debugMessage("Sorry there was an error: " + caught.getMessage());
			}

			public void onSuccess(Integer result) {
				int accessCount = (Integer) result;
				debugMessage("That's the " + accessCount + ". time accessing this page");
				setAccessCount( accessCount );
				rsService.getConfig( FREQUENCY_CONFIG_NAME , frequencyCallback);
			}
		};
		

		
		rsService.addPageAccessAndGetCount(getURL(), getUserHashValue(), paCallback);
	}

	public static void updateFormChoice() {
		lbFormSelection.removeItem( lbFormSelection.getSelectedIndex() );
		lbFormSelection.setSelectedIndex( 0 );
		tPanel.selectTab(0);
	}
	
	public void showAdminLoginDialog() {
		// Create a glass panel with `autoHide = true`
		glassPanel = new GlassPanel(false);
		// Attach (display) the glass panel
		RootPanel.get().add( glassPanel, 0, 0);
		
		
		final AsyncCallback<Boolean> psswrdCallback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				Window.alert( "Failure" );
			}
			public void onSuccess(Boolean result) {
				boolean correctPassword = (Boolean)result;
				if( correctPassword ){
					dbAdminLogin.hide();
					glassPanel.removeFromParent();
					initAdminPanel();
				}
				else{
					dbAdminLogin.setWidget( new HTML( "Sorry wrong password"));
				}
			}
		};

		
		VerticalPanel         vpLoginPanel = new VerticalPanel();
		HorizontalPanel       hpPasswordPanel = new HorizontalPanel();
		final PasswordTextBox ptbPassword = new PasswordTextBox();
		Button                bLogin      = new Button( TextContainer.LOGIN_LABEL );
		
		bLogin.addClickListener(new ClickListener() {
			  public void onClick(Widget sender) {
				  rsService.checkAdminPassword( ptbPassword.getText() , psswrdCallback);
			  }
			});
		
		hpPasswordPanel.add( new Label( TextContainer.PASSWORD_LABEL ) );
		hpPasswordPanel.add( ptbPassword );
		vpLoginPanel.add( new HTML( TextContainer.LOGIN_DESCRIPTION_HTML ));
		vpLoginPanel.add( hpPasswordPanel );
		vpLoginPanel.add( new HTML( "<br><br>") );
		vpLoginPanel.add( bLogin );
		
		dbAdminLogin = new DialogBox();
		dbAdminLogin.setAnimationEnabled( true );
		dbAdminLogin.setPopupPosition( 200 , 200 );
		dbAdminLogin.setWidget( vpLoginPanel );
		dbAdminLogin.setText( TextContainer.ADMIN_LOGIN_LABEL );
		dbAdminLogin.center();
		dbAdminLogin.show();
	}

	/**
	 * @param accessCount the accessCount to set
	 */
	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	/**
	 * @return the accessCount
	 */
	public int getAccessCount() {
		return accessCount;
	}
}