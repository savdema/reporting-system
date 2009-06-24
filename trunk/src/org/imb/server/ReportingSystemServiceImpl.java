package org.imb.server;

import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.imb.client.Config;
import org.imb.client.FormElementS;
import org.imb.client.Page;
import org.imb.client.ReportingSystemService;
import org.imb.client.Result;
import org.imb.client.TextContainer;
import org.imb.client.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReportingSystemServiceImpl extends RemoteServiceServlet implements ReportingSystemService {

	private static final long serialVersionUID = -3570814963763845376L;

	private static final String SELECT_FORMELEMENTS_BY_PARENT_ID = 
		"SELECT * FROM formelement INNER JOIN formelement_has_formelement " +
		"ON formelement.id = formelement_has_formelement.child_id " +
		"WHERE formelement_has_formelement.parent_id = ? " +
		"AND formelement.deleted = 0";
	
	private static final String SELECT_FORMELEMENT_BY_CHILD_ID = 
		"SELECT * FROM formelement INNER JOIN formelement_has_formelement " +
		"ON formelement.id = formelement_has_formelement.parent_id " +
		"WHERE formelement_has_formelement.child_id = ? " +
		"AND formelement.deleted = 0";
	
	private static final String INSERT_FORMELEMENT = 
		"INSERT INTO formelement values (NULL,?,?,?,?,0)";
	
	private static final String SELECT_ALL_FORMS = 
		"SELECT * FROM formelement " +
		"WHERE type = 1 AND deleted = 0";
	
	private static final String SELECT_FORMELEMENT_BY_ID = 
		"SELECT * FROM formelement " +
		"WHERE id = ? AND deleted = 0";
	
	private static final String INSERT_FORMELEMENT_RELATION = 
		"INSERT INTO formelement_has_formelement values (?,?)";//parent_id , child_id
	
	private static final String UPDATE_FORMELEMENT_BY_ID =
		"UPDATE formelement " +
		"SET position = ?, name = ?, description = ?" +
		"WHERE id = ?";
	
	private static final String DELETE_FORMELEMENT_BY_ID =
		"UPDATE formelement " +
		"SET deleted = 1 " +
		"WHERE id = ?";

	private static final String INSERT_RESULT = 
		"INSERT INTO result values (NULL,?,?,?)";//user_id , formElement_id , value

	private static final String SELECT_RESLUT_BY_FORMELEMENT_ID =
		"SELECT * FROM result WHERE formelement_id = ? " +
		"ORDER BY value DESC ";

	private static final String SELECT_USER_BY_ID =
		"SELECT * FROM user WHERE id=?";

	private static final String INSERT_PAGE_ACCESS =
		"INSERT INTO page_access values (?,?,?,NULL,NULL,?)";//page_id , user_id , submitted_form , timestamp, formElement_id

	private static final String SELECT_USER_BY_HASHVALUE = 
		"SELECT * FROM user WHERE hashvalue=?";

	private static final String SELECT_PAGE_BY_URL = 
		"SELECT * FROM page WHERE url=?";

	private static final String INSERT_PAGE = 
		"INSERT INTO page values (NULL,?)";//url

	private static final String INSERT_USER = 
		"INSERT INTO user values (NULL,?)";//hashvalue

	private static final String SELECT_FORMELEMENT_HAS_PAGE_BY_FORMELEMENT_ID_AND_PAGE_ID =
		"SELECT * FROM formelement_has_page WHERE formelement_id = ? AND page_id = ?";

	private static final String INSERT_FORMELEMENT_HAS_PAGE = 
		"INSERT INTO formelement_has_page values (?,?)";// formelement_id , page_id

	private static final String SELECT_ALL_PAGES =
		"SELECT * FROM page";

	private static final String SELECT_PAGES_BY_FORMELEMENT_ID = 
		"SELECT * FROM page INNER JOIN formelement_has_page " +
		"ON page.id = formelement_has_page.page_id " +
		"WHERE formelement_has_page.formelement_id = ?";

	private static final String DELETE_FORMELEMENT_HAS_PAGE_BY_FORMELEMENT_ID_AND_PAGE_ID = 
		"DELETE FROM formelement_has_page WHERE formelement_id = ? AND page_id = ?";// formelement_id , page_id

	private static final String SELECT_PAGE_ACCESS_BY_URL_AND_HASHVALUE = 
		"SELECT * FROM (page_access pa INNER JOIN page p ON pa.page_id = p.id) " +
		"INNER JOIN user u ON  pa.user_id = u.id " +
		"WHERE p.url = ? AND u.hashvalue = ?"  ;

	private static final String SELECT_FORMELEMENT_BY_URL = 
		"SELECT * FROM (formelement f " +
		"INNER JOIN formelement_has_page fp ON f.id = formelement_id) " +
		"INNER JOIN page p ON fp.page_id = p.id " +
		"WHERE p.url = ? AND f.deleted = 0"; //url
	
	private static final String INSERT_CONFIG = 
		"INSERT INTO config values (NULL,?,?)";// name , value
	
	private static final String SELECT_CONFIG_BY_NAME = 
		"SELECT * FROM config WHERE name = ?"; //name
	
	private static final String UPDATE_CONFIG_BY_NAME =
		"UPDATE config " +
		"SET    value = ? " +
		"WHERE  name  = ?";
	
	private static DataSource getDataSource(){
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			return (DataSource) ctx.lookup("java:comp/env/jdbc/MyDataSource");
		} catch (NamingException e) {
			throw new InvocationException("Exception getting datasoure",e);
		}
	}
	
	public FormElementS getFormElement(int id) {
		FormElementS   form     = null;
		DataSource ds = getDataSource();
		try {
			Connection conn = ds.getConnection();
			//get Form name and other attributes
			PreparedStatement ps = conn.prepareStatement(SELECT_FORMELEMENT_BY_ID);
			ps.setInt(1, id );
			FormElementS[] tmpfes = parseFormElementSelection( ps.executeQuery() );
			if( tmpfes.length == 1 ){
				form = tmpfes[0];
			}
			//get all children of the Form
			form.setChildren( getSubFormElements( form ) );
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption fetching form via getForm",e);
		}
		return form;
	}
	
	public FormElementS getForm(String url) {
		FormElementS form = null;
		DataSource ds = getDataSource();
		try {
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(SELECT_FORMELEMENT_BY_URL);
			ps.setString( 1 , url );
			FormElementS[] tmpfes = parseFormElementSelection( ps.executeQuery() );
			if( tmpfes.length == 1 ){
				form = tmpfes[0];
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption getForm(String url)",e);
		}
		//get all children of the Form
		if( form != null ){
			form.setChildren( getSubFormElements( form ) );
		}
		return form;
	}
	
	public FormElementS[] getAllForms(){
		DataSource ds = getDataSource();
		FormElementS[] forms = null;
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( SELECT_ALL_FORMS );
			ResultSet rs         = ps.executeQuery();
			forms                = parseFormElementSelection( rs );
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption saving stocks",e);
		}
		return forms;
	}

	public FormElementS addFormElement( FormElementS fes){
		DataSource ds = getDataSource();
		try {
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_FORMELEMENT);
			ps.setInt( 1 , fes.getPosition() );
			ps.setString( 2 , fes.getName() );
			ps.setString( 3 , fes.getDescription() );
			ps.setInt( 4 , fes.getType() );
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()){
				fes.setId( rs.getInt(1) );
			}
			// Check if the formelement has a parent and insert the relation
			if( fes.getParent() != null ){
				ps = conn.prepareStatement(INSERT_FORMELEMENT_RELATION);
				ps.setInt( 1 , fes.getParent().getId() );
				ps.setInt( 2 , fes.getId() );
				ps.execute();
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption saving stocks",e);
		}
		return fes;
	}
	
	private FormElementS[] parseFormElementSelection( ResultSet rs ){
		FormElementS[] forms = null;
		List<FormElementS> formList = new LinkedList<FormElementS>();
		try {
			while (rs.next()){
				FormElementS fes = new FormElementS( rs.getInt(2) ,
													 rs.getString( 3 ),
													 rs.getString( 4 ),
													 rs.getInt( 5 ),
													 rs.getInt( 1 )
													);
				fes.setChildren( getSubFormElements( fes ) );
				formList.add( fes );
			}
			forms = new FormElementS[formList.size()];
			int cnt = 0;
			for (FormElementS fes : formList){
				forms[cnt++] = fes;
			}
		} catch (SQLException e) {
			throw new InvocationException("Exeption saving stocks",e);
		}
		return forms;
	}

	@Override
	public boolean addResults(Result[] results) {
		DataSource ds         = getDataSource();
		boolean    successful = false;
		try {
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_RESULT);
			for (Result result : results){
				ps.setInt( 1 , result.getUser().getId() );
				ps.setInt( 2 , result.getFormElement().getId() );
				ps.setString( 3 , result.getValue() );
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			conn.close();
			successful = true;
		} catch (SQLException e) {
			throw new InvocationException("Exeption saving stocks",e);
		}
		return successful;
	}

	@Override
	public FormElementS updateFormElement(FormElementS fes) {
		DataSource ds = getDataSource();
		try {
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( UPDATE_FORMELEMENT_BY_ID );
			ps.setInt( 1 , fes.getPosition() );
			ps.setString( 2 , fes.getName() );
			ps.setString( 3 , fes.getDescription() );
			ps.setInt( 4 , fes.getId() );
			ps.execute();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption updateing the FormElement: ",e);
		}
		return fes;
	}
	
	
	private FormElementS[] getSubFormElements( FormElementS fes ){
		DataSource     ds     = getDataSource();
		FormElementS[] tmpfes = new FormElementS[0];
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(SELECT_FORMELEMENTS_BY_PARENT_ID);
			ps.setInt(1, fes.getId() );
			tmpfes = parseFormElementSelection( ps.executeQuery() );
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption fetching form via getForm",e);
		}
		
		return tmpfes;
	}

	@Override
	public Result[] getResults(int id) {
		DataSource   ds         = getDataSource();
		Result[]     results    = new Result[0];
		List<Result> resultList = new LinkedList<Result>();
		FormElementS fes        = getFormElement(id);
		//get results of the children if there are any..
		if( fes.getChildren() != null ){
			for( FormElementS child : fes.getChildren() ){
				Result[] childResults = getResults( child.getId() );
				for( Result r : childResults ){
					resultList.add( r );
				}
			}
		}
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(SELECT_RESLUT_BY_FORMELEMENT_ID);
			ps.setInt(1, id );
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Result r = new Result( 	rs.getString( 4 ),
										fes ,
										getUser( rs.getInt(2) )
									);
				r.setId( rs.getInt( 1 ));
				resultList.add( r );
			}
			results = new Result[resultList.size()];
			int cnt = 0;
			for (Result r : resultList){
				results[cnt++] = r;
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption fetching form via getForm",e);
		}
		return results;
	}

	private User getUser(int userId) {
		DataSource ds = getDataSource();
		User user     = new User();
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = conn.prepareStatement(SELECT_USER_BY_ID);
			ps.setInt(1, userId );
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				user.setId( rs.getInt(1));
				user.setHashvalue( rs.getString( 2 ));
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption loading user",e);
		}
		return user;
	}


	private boolean addPageAccess(String url, String userHashValue, int formId) {
		DataSource   ds    = getDataSource();
		boolean successful = false;
		User user = getUser( userHashValue );
		Page page = getPage( url );
		int submittedForm = 0;
		if( formId > 0 ){
			submittedForm = 1;
		}
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( INSERT_PAGE_ACCESS );
			ps.setInt(1, page.getId() );
			ps.setInt(2, user.getId() );
			ps.setInt(3, submittedForm );
			ps.setInt(4, formId );
			ps.execute();
			conn.commit();
			conn.close();
			successful = true;
			
		} catch (SQLException e) {
			throw new InvocationException("Exeption in method addPageAccess",e);
		}
		return successful;
	}

	private Page getPage(String url) {
		DataSource ds = getDataSource();
		Page page     = new Page();
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = conn.prepareStatement(SELECT_PAGE_BY_URL);
			ps.setString( 1, url );
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				page.setId( rs.getInt(1));
				page.setUrl( rs.getString( 2 ));
			}
			else{
				ps   = conn.prepareStatement(INSERT_PAGE);
				ps.setString( 1, url );
				ps.execute();
				rs = ps.getGeneratedKeys();
				if (rs.next()){
					page.setId( rs.getInt(1) );
					page.setUrl( url );
				}
				conn.commit();
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption loading user",e);
		}
		return page;
	}

	public User getUser(String userHashValue) {
		DataSource ds = getDataSource();
		User user     = new User();
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = conn.prepareStatement(SELECT_USER_BY_HASHVALUE);
			ps.setString( 1, userHashValue );
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				user.setId( rs.getInt(1));
				user.setHashvalue( rs.getString( 2 ));
			}
			else{
				ps   = conn.prepareStatement(INSERT_USER);
				ps.setString( 1, userHashValue );
				ps.execute();
				rs = ps.getGeneratedKeys();
				if (rs.next()){
					user.setId( rs.getInt(1) );
					user.setHashvalue( userHashValue );
				}
				conn.commit();
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption loading user",e);
		}
		return user;
	}

	@Override
	public boolean addPageAssociation(int pageId, FormElementS fes) {
		boolean saved = false;
		DataSource ds = getDataSource();
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = conn.prepareStatement(SELECT_FORMELEMENT_HAS_PAGE_BY_FORMELEMENT_ID_AND_PAGE_ID);
			ps.setInt( 1, fes.getId() );
			ps.setInt( 2, pageId );
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				// page formelement association already exists -> nothing to do ;-)
			}
			else{
				ps   = conn.prepareStatement(INSERT_FORMELEMENT_HAS_PAGE);
				ps.setInt( 1, fes.getId() );
				ps.setInt( 2, pageId );
				ps.execute();
				conn.commit();
				saved = true;
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption in addPageAssociation",e);
		}
		return saved;
	}

	@Override
	public Page[] getPages() {
		DataSource ds = getDataSource();
		List<Page> pageList = new LinkedList<Page>();
		Page[]     pages    = new Page[0];
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( SELECT_ALL_PAGES );
			ResultSet rs         = ps.executeQuery();
			while (rs.next()){
				Page p = new Page( rs.getInt(1) , rs.getString( 2 ) );
				pageList.add( p );
			}
			pages = new Page[pageList.size()];
			int cnt = 0;
			for (Page p : pageList){
				pages[cnt++] = p;
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption saving stocks",e);
		}
		return pages;
	}

	@Override
	public Page[] getAssociatedPages(FormElementS fes) {
		DataSource ds = getDataSource();
		List<Page> pageList = new LinkedList<Page>();
		Page[]     pages    = new Page[0];
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( SELECT_PAGES_BY_FORMELEMENT_ID );
			ps.setInt( 1, fes.getId() );
			ResultSet rs         = ps.executeQuery();
			while (rs.next()){
				Page p = new Page( rs.getInt(1) , rs.getString( 2 ) );
				pageList.add( p );
			}
			pages = new Page[pageList.size()];
			int cnt = 0;
			for (Page p : pageList){
				pages[cnt++] = p;
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption saving stocks",e);
		}
		return pages;
	}

	@Override
	public boolean removeAssociatedPage(int pageId, FormElementS fes) {
		boolean successful = false;
		DataSource ds = getDataSource();
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = conn.prepareStatement(DELETE_FORMELEMENT_HAS_PAGE_BY_FORMELEMENT_ID_AND_PAGE_ID);
			ps.setInt( 1, fes.getId() );
			ps.setInt( 2, pageId );
			ps.execute();
			conn.commit();
			conn.close();
			successful = true;
		} catch (SQLException e) {
			throw new InvocationException("Exeption in addPageAssociation",e);
		}	
		return successful;
	}

	@Override
	public int addPageAccessAndGetCount(String url, String userHashValue) {
		addPageAccess(url, userHashValue , 0);
		DataSource ds    = getDataSource();
		int accessCount = 0;
		try {
			Connection conn      = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( SELECT_PAGE_ACCESS_BY_URL_AND_HASHVALUE );
			ps.setString( 1 , url);
			ps.setString( 2 , userHashValue);
			ResultSet rs         = ps.executeQuery();
			while (rs.next()){
				accessCount++;
				if( rs.getInt(3)== 1 ){
					accessCount = -1;
					break;
				}
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption in addPageAssociation",e);
		}
		return accessCount;
	}
	
	@Override
	public boolean addSubmittedFormAccess(String url, String userHashValue, int formId) {
		return addPageAccess(url, userHashValue , formId);
	}

	@Override
	public FormElementS getParent(FormElementS fes) {
		FormElementS   parent     = null;
		DataSource ds = getDataSource();
		try {
			Connection conn = ds.getConnection();
			//get Form name and other attributes
			PreparedStatement ps = conn.prepareStatement( SELECT_FORMELEMENT_BY_CHILD_ID );
			ps.setInt(1, fes.getId() );
			FormElementS[] tmpfes = parseFormElementSelection( ps.executeQuery() );
			if( tmpfes.length == 1 ){
				parent = tmpfes[0];
			}
			//get all children of the Form
			parent.setChildren( getSubFormElements( parent ) );
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption fetching form via getParent",e);
		}
		if( parent != null ){
			fes.setParent( parent );
		}
		return fes;
	}

	@Override
	public FormElementS deleteFormElement(FormElementS formElementS) {
		DataSource ds = getDataSource();
		try {
			Connection conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement( DELETE_FORMELEMENT_BY_ID );
			ps.setInt( 1 , formElementS.getId() );
			ps.execute();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption in the method deleteFormElement: ",e);
		}
		return formElementS;
	}

	@Override
	public Config getConfig(String name) {
		DataSource ds = getDataSource();
		Config config = new Config();
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = conn.prepareStatement(SELECT_CONFIG_BY_NAME);
			ps.setString( 1, name );
			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				config.setId( rs.getInt(1) );
				config.setName( rs.getString(2));
				config.setValue( rs.getString( 3 ));
			}
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption in the method getConfig(String name)",e);
		}
		return config;
	}

	@Override
	public Config setConfig(String name, String value) {
		DataSource ds      = getDataSource();
		Config config      = getConfig( name );
		if( name.equals( TextContainer.ADMIN_PASSWORD_CONFIG_NAME ) ){
			value = BCrypt.hashpw( value, BCrypt.gensalt() );
		}
		try {
			Connection        conn = ds.getConnection();
			PreparedStatement ps   = null; 
			if( config.getValue() == null ){
				ps = conn.prepareStatement( INSERT_CONFIG );
				ps.setString( 1, name );
				ps.setString( 2, value );
			}
			else{
				ps = conn.prepareStatement( UPDATE_CONFIG_BY_NAME );
				ps.setString( 1, value );
				ps.setString( 2, name );
			}
			ps.execute();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			throw new InvocationException("Exeption in the method setConfig(String name, String value)",e);
		}
		return config;
	}
	
	public boolean checkAdminPassword( String password ){
		Config adminPassword    = getConfig( TextContainer.ADMIN_PASSWORD_CONFIG_NAME  );
		if( adminPassword == null || adminPassword.getValue() == null ){
			return false;
		}
		return BCrypt.checkpw(password, adminPassword.getValue() );
	}

}
