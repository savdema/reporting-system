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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gchart.client.GChart;


public class ReportingChart extends GChart {
	
	private final int MAX_REVENUE    = 100; 
	private final int WIDTH          = 600;
	private final int HEIGHT         = 300;
	
	private Vector<ResultGroup> resultGroups = new Vector<ResultGroup>();
	
	private ListBox lbFormElements;
	private Form form = null;
	private Button btnCombine;
	private HTML chartTitle = new HTML();
	private TextArea taCSVExport;
  	
  	public ReportingChart( final Form form ) { 
  		this.form = form;
  		setChartSize(WIDTH, HEIGHT);
  		chartTitle.setHTML( "<b><big><big>" + form.getName() + "</big></big><br>&nbsp;</b>" );
  		setChartTitle( chartTitle );
  		final HorizontalPanel hpChartFootnotes = new HorizontalPanel();
  		final VerticalPanel vpChartFootnotes = new VerticalPanel();
  		btnCombine = new Button( TextContainer.CONDITIONAL_ANALYSIS);
  		btnCombine.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		    	   Vector<ResultGroup> crg = createConditionalResultGroups( resultGroups );
		    	   createResultCurve( crg );
		      }
		    });
  		
	    lbFormElements = new ListBox();
	    lbFormElements.addItem( TextContainer.CAPTION_SELECT_FORM );
	    if( form.getFormElementS() != null && form.getFormElementS().getChildren() != null ){
	    	addEvaluableFormElements( form.getFormElementS().getChildren() );
	    }
	    
	    taCSVExport = new TextArea();
	    taCSVExport.setWidth( WIDTH + "px");
	    taCSVExport.setHeight( HEIGHT + "px");
	    taCSVExport.setStyleName( "csvexport" );
	    taCSVExport.setVisible( false );
    
	    final AsyncCallback<Result[]> exportCallback = new AsyncCallback<Result[]>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error: " + caught.getMessage() );
			}
			public void onSuccess(Result[] result) {
				Result[] results = (Result[])result;
				displayCSVExport( results );
				ReportingSystem.debugMessage("Results with " + results.length + "elements" );

			}  
	    };
	    
	    Button btnExport = new Button( TextContainer.EXPORT_LABEL );
	    btnExport.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		    	  ReportingSystem.rsService.getResults( form.getId() , exportCallback );
		      }
		    });
	    
	    final AsyncCallback<Result[]> callback = new AsyncCallback<Result[]>() {
			public void onFailure(Throwable caught) {
				ReportingSystem.debugMessage("Sorry there was an error: " + caught.getMessage() );
			}
			public void onSuccess(Result[] result) {
				Result[] results = (Result[])result;
				resultGroups = createResultGroups( results );
				createResultCurve( resultGroups );
				ReportingSystem.debugMessage("Results with " + results.length + "elements" );

			}  
	    };
	    lbFormElements.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				int formId = Integer.valueOf( lbFormElements.getValue( lbFormElements.getSelectedIndex() ) ).intValue();
				ReportingSystem.debugMessage("Try to load results of the form (ID=" +  formId + ")" );
				ReportingSystem.rsService.getResults( formId , callback );
				lbFormElements.setFocus( false );
			}
		});
	    
	    hpChartFootnotes.add( lbFormElements );
	    hpChartFootnotes.add( btnCombine );
	    hpChartFootnotes.add( btnExport );
	    vpChartFootnotes.add( hpChartFootnotes );
    	vpChartFootnotes.add( taCSVExport );
		setChartFootnotes(vpChartFootnotes);
		FormElementS fes2    = new FormElementS( 124 , "Kein Formular ausgewählt" , "Kein Formular ausgewählt" , FormElementS.TYPE_STARRATING );
		User         user    = new User( 123 , "hashvalue" );
		Result[]     results = {new Result( "" , fes2 , user )};
		resultGroups = createResultGroups( results );
		createResultCurve( resultGroups );
  	}

	protected void displayCSVExport(Result[] results) {
    	String sCSVExport = "";
    	sCSVExport += "FormElementId" + TextContainer.CSV_DELIMITER;
    	sCSVExport += "FormElementName" + TextContainer.CSV_DELIMITER;
    	sCSVExport += "UserId" + TextContainer.CSV_DELIMITER;
    	sCSVExport += "ResultId" + TextContainer.CSV_DELIMITER;
    	sCSVExport += "Value" + TextContainer.CSV_LINEBREAK;
    	for( Result r : results ){
    		sCSVExport += r.getFormElement().getId() + TextContainer.CSV_DELIMITER;
    		sCSVExport += r.getFormElement().getName() + TextContainer.CSV_DELIMITER;
    		sCSVExport += r.getUser().getId() + TextContainer.CSV_DELIMITER;
    		sCSVExport += r.getId() + TextContainer.CSV_DELIMITER;
    		sCSVExport += r.getValue() + TextContainer.CSV_LINEBREAK;
    	}
    	taCSVExport.setVisible( true );
    	taCSVExport.setText( sCSVExport );
	}

	private void addEvaluableFormElements(FormElementS[] aFes) {
		for ( FormElementS fes : aFes ){
			if( fes.getType() == FormElementS.TYPE_CHOICE ||
				fes.getType() == FormElementS.TYPE_CHOICE_STARRATING_SUBFORM ||
				fes.getType() == FormElementS.TYPE_STARRATING ||
				fes.getType() == FormElementS.TYPE_STARRATING_SUBFORM ||
				fes.getType() == FormElementS.TYPE_CHECKBOX
			   ){
				String sName =  fes.getName();
				if( sName.length() > 30){
					sName = sName.substring(0 , 30 ) + "..";
				}
				lbFormElements.addItem( sName + "(" + fes.getTypeName() + ")" , fes.getId()+"" );
			}
			addEvaluableFormElements( fes.getChildren() );
	    }
	}

	private void createResultCurve( Vector<ResultGroup> results) {
		//clear current Curves and Ticks
		clearCurves();
		getXAxis().clearTicks();
		
		//if there arent't any results groups we can leave
		if( results.size() == 0 ){
			return;
		}
		chartTitle.setHTML( "<b><big><big>" + form.getName() + "</big></big><br>&nbsp;</b>(n=" );
		
		Vector<String> barLabels = createBarLabels( results );
		
	    for (int iCurve=0; iCurve < barLabels.size(); iCurve++) {
   	        addCurve();     // one curve per quarter
	        getCurve().getSymbol().setSymbolType( SymbolType.VBAR_SOUTHWEST);
	        getCurve().getSymbol().setBackgroundColor( getCorrespondingColor( barLabels.get(iCurve) , iCurve ) );
	        getCurve().setLegendLabel(barLabels.get(iCurve));
	        getCurve().getSymbol().setModelWidth(1.0);
	        getCurve().getSymbol().setBorderColor("black");
	        getCurve().getSymbol().setBorderWidth(1);
	        for (int jGroup=0; jGroup < results.size(); jGroup++) { 
	        	// the '+1' creates a bar-sized gap between groups 
	        	ResultGroup rg = results.get( jGroup );
	        	double      y  = rg.getDue( barLabels.get(iCurve) );
	        	double      a  = rg.getAbsoluteCount( barLabels.get(iCurve) );
	        	getCurve().addPoint(1+iCurve+jGroup*(barLabels.size()+1), y*MAX_REVENUE);
	        	getCurve().getPoint().setAnnotationLocation( AnnotationLocation.NORTH);
	        	if( a > 0){
	        		getCurve().getSymbol().setHovertextTemplate(  barLabels.get(iCurve) + " ${y}" );
	        	}
	        }
	      }
	    for (int i = 0; i < results.size(); i++) {
	    	double tickPosition = barLabels.size()/2. + i*(barLabels.size()+1);
	    	String tickLabel    = results.get(i).getName() + " (" +  results.get(i).getTotalCount() + ")";
	    	getXAxis().addTick(	tickPosition , tickLabel ); 
	    	chartTitle.setHTML( chartTitle.getHTML() + results.get(i).getTotalCount() + "," );
	    }
	    chartTitle.setHTML( chartTitle.getHTML().substring( 0 , chartTitle.getHTML().length() -1 ) + ")" );
	    
		getXAxis().setTickLabelFontSize(20);
		getXAxis().setTickLength(6);    // small tick-like gap... 
		getXAxis().setTickThickness(0); // but with invisible ticks
		getXAxis().setAxisMin(0);       // keeps first bar on chart
		getYAxis().setAxisMin(0);           // Based on sim revenue range
		getYAxis().setAxisMax(MAX_REVENUE); // of 0 to MAX_REVENUE.
		getYAxis().setTickCount(11);
		getYAxis().setHasGridlines(true);
		getYAxis().setTickLabelFormat("#'%'");
		update();
	}
	

	
	private String getCorrespondingColor(String string, int index) {
		String[] colors = TextContainer.COLORS;
		String color = "";
		if( string.equals( TextContainer.STARRATING_LABELS[0]) ){
			color = "grey";
		}else if( string.equals( TextContainer.STARRATING_LABELS[1]) ){
			color = "red";
		}else if( string.equals( TextContainer.STARRATING_LABELS[2]) ){
			color = "yellow";
		}else if( string.equals( TextContainer.STARRATING_LABELS[3]) ){
			color = "green";
		}else{
			if( index < colors.length){
				color = "#" + colors[index];
			}
		}
		return color;
	}

	private Vector<String> createBarLabels(Vector<ResultGroup> results) {
		Vector<String> barLabels = new Vector<String>();
		for( ResultGroup rg : results ){
			for( String barLabel : rg.getLabels() ){
				if( !arrayContains( barLabels ,  barLabel ) ){
					barLabels.add( barLabel );
				}
			}
		}
		return barLabels;
	}

	private Vector<ResultGroup> createResultGroups( Result[] results){
		Vector<ResultGroup> vRG = new Vector<ResultGroup>();
		FormElementS fesCurrent = null;
		ResultGroup  rgCurrent  = null;
		if( results.length > 0 && results[0].getFormElement() != null){
			fesCurrent = results[0].getFormElement();
			rgCurrent  = new ResultGroup( fesCurrent );
		}
		for ( Result result : results ){
			if( !fesCurrent.equals( result.getFormElement() ) ){
				vRG.add( rgCurrent );
				fesCurrent = result.getFormElement();
				rgCurrent  = new ResultGroup( fesCurrent );
			}
			rgCurrent.addResult( result );
		}
		if( rgCurrent != null ){
			vRG.add( rgCurrent );
		}
		return vRG;
	}
	
	private Vector<ResultGroup> createConditionalResultGroups(	Vector<ResultGroup> vRG ) {
		Vector<ResultGroup> vConditionalRG = new Vector<ResultGroup>();
		ResultGroup rgChoice = null;
		ResultGroup rgStarRating = null;
		//Try to find the first choice form element
		for( ResultGroup rg : vRG ){
			if( rg.getFormElement().getType() == FormElementS.TYPE_CHOICE ){
				rgChoice = rg;
				break;
			}
		}
		//Try to find the corresponding starrating
		if( rgChoice != null ){
			for( ResultGroup rg : vRG ){
				if( rg.getFormElement().getType() == FormElementS.TYPE_STARRATING && 
					rg.getFormElement().getPosition() == rgChoice.getFormElement().getPosition() &&
					rg.getFormElement().getName().equals( rgChoice.getFormElement().getName() )
				   ){
					rgStarRating = rg;
					break;
				}
			}
		}
		// Now let's split the Choice group into several other result groups according to each field
		// And then reallocate the results of the StarRating according to the user
		for( String s : rgChoice.getLabels() ){
			ResultGroup rg = new ResultGroup( rgStarRating.getFormElement() );
			rg.setName( rg.getName() + " " + s );
			Vector<Result> resultsByValue = rgChoice.getResults( s );
			for( Result rChoice : resultsByValue ){
				for( Result rStarRating : rgStarRating.getResults() ){
					if( rChoice.getUser().getId() == rStarRating.getUser().getId() ){
						rg.addResult( rStarRating );
					}
				}
			}
			if( rg.getResults().size() > 0 ){
				vConditionalRG.add( rg );
			}
			ReportingSystem.debugMessage( "Added ResultGroup " + rg.getName() + " with " + rg.getResults().size() + " results");
		}
		return vConditionalRG;
	}

	private boolean arrayContains(Vector<String> valueLabels, String value) {
		for ( String valueLabel : valueLabels ){
			if( value.equals( valueLabel ) ){
				return true;
			}
		}
		return false;
	}
}
