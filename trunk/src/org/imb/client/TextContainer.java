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

/**
 * TextContainer contains all localized text strings and could be replaced
 * somehow using the GWT integrated localization during the next refactoring ;-)
 * 
 * @author Johannes Metscher
 */
public class TextContainer {
	protected static final String   APPLICATION_VERSION                = "beta v0.5.11";
	
	protected static final String   NEW_CHOICE_NAME                    = "Neues Auswahlfeld";
	protected static final String   NEW_CHECKBOX_NAME                  = "Neues Häckchenfeld";
	protected static final String   NEW_STARRATING_NAME                = "Neues Sternbewertungsfeld";
	protected static final String   NEW_TEXTLABEL_NAME                 = "Neuer Text";
	protected static final String   NEW_TEXTINPUT_NAME                 = "Neues Eingabefeld";
	protected static final String   NEW_STARRATING_SUBFORM_NAME        = "Neue Sternbewertungseinheit";
	protected static final String   NEW_CHOICE_STARRATING_SUBFORM_NAME = "Neue Auswahl- und Sternbewertungseinheit";	
	protected final static String   CAPTION_SELECT_FORM                = "Auswahl der Formularelemente";
	protected static final String   ADD_NEW_ELEMENT_CHOICE             = "Neues Element hinzufügen";
	protected static final String   PAGEBREAK                          = "Seitenumbruch";
	protected static final String   NEW_ELEMENT_PAGEBREAK              = "Neuen " + PAGEBREAK;
	protected static final String   LATER                              = "Später";
	protected static final String   CANCEL                             = "Abbrechen";
	protected static final String   SUBMIT                             = "Absenden";
	protected static final String   NO_PAGE_ASSOCIATION                = "Keine Seitenzuordnung";
	protected static final String   CONDITIONAL_ANALYSIS               = "Bedingte Auswertung";
	protected static final String   NOT_SPECIFIED                      = "keine Angabe";
	protected static final String[] STARRATING_LABELS                  = { NOT_SPECIFIED , "mangelhaft bis ungenügend", "befriedigend bis ausreichend", "sehr gut bis gut" };
	protected static final String   CREATE_NEW_FEEDBACK_FORM           = "Erstelle neues Feedback Formular";
	protected static final String   NEW_FEEDBACK_FORM                  = "Neues Feedback Formular";
	protected static final String   APPLICATION_NAME                   = "Reporting System";
	protected static final String   NAME_FORMELEMENT                   = "Name der Formularelements";
	protected static final String   LOGIN_LABEL                        = "Anmelden";
	public    static final String   ADMIN_LOGIN_LABEL                  = APPLICATION_NAME + " Administration";
	public    static final String   PASSWORD_LABEL                     = "Passwort: ";
	public    static final String   LOGIN_DESCRIPTION_HTML             = "<p>Willkommen im Administrationsbereich des " + APPLICATION_NAME + ".<br><br> Bitte melden Sie sich mit dem Administratorpasswort an</p>";
	public    static final String   POSITION_UP_BUTTON_LABEL           = "-";
	public    static final String   POSITION_DOWN_BUTTON_LABEL         = "+";
	protected static final String[] COLORS                             = {"7FFFD4","76EEC6","66CDAA","458B74","F0FFFF","E0EEEE","C1CDCD","838B8B","0000FF","0000EE","0000CD","00008B","8A2BE2","5F9EA0","98F5FF","8EE5EE","7AC5CD","53868B","6495ED","00FFFF","00EEEE","00CDCD","008B8B","00008B","008B8B","483D8B","00CED1","00BFFF","00B2EE","009ACD","00688B","1E90FF","1C86EE","1874CD","104E8B","ADD8E6","BFEFFF","B2DFEE","9AC0CD","68838B","E0FFFF","D1EEEE","B4CDCD","7A8B8B","87CEFA","B0E2FF","A4D3EE","8DB6CD","607B8B","8470FF","B0C4DE","CAE1FF","BCD2EE","A2B5CD","6E7B8B","66CDAA","0000CD","7B68EE","48D1CC","191970","AFEEEE","BBFFFF","AEEEEE","96CDCD","668B8B","B0E0E6","41690","4876FF","436EEE","3A5FCD","27408B","87CEEB","87CEFF","7EC0EE","6CA6CD","4A708B","6A5ACD","836FFF","7A67EE","6959CD","473C8B","4682B4","63B8FF","5CACEE","4F94CD","36648B","40E0D0","00F5FF","00E5EE","00C5CD","00868B"};
	public    static final String   ANALYSIS_TAB_LABEL                 = "Auswerten";
	public    static final String   EDIT_TAB_LABEL                     = "Bearbeiten";
	public    static final String   PREVIEW_TAB_LABEL                  = "Anzeigen";
	public    static final String   FORM_SELECTION_TAB_LABEL           = "Auswahl";
	public    static final String   FREQUENCY_LABEL                    = "Anzeige Häufigkeit: ";
	public    static final String   ADMIN_TAB_LABEL                    = "Administration";
	public    static final String   EXPORT_LABEL                       = "CSV Export";
	protected static final String   CSV_DELIMITER                      = "\t";
	protected static final String   CSV_LINEBREAK                      = "\n";
	public    static final String   ADMIN_PASSWORD_CONFIG_NAME         = "adminPassword";
	public    static final String   NEXT                               = "Nächste Seite";
	protected static final String   PASSWORD_SAVED_FEEDBACK            = "Das Passwort wurde erfolgreich geändert";
	public    static final String   SAVE_PASSWORD_LABEL                = "Passwort speichern";
	protected static final String   PASSWORD_NOT_EQUALS_MESSAGE        = "Die Passwörter müssen übereinstimmen";
}
