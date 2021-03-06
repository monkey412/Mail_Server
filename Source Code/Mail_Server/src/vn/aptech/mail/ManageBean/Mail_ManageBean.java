package vn.aptech.mail.ManageBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.Part;



import vn.aptech.mail.DAO.Attachs_DAO;
import vn.aptech.mail.DAO.Mail_DAO;
import vn.aptech.mail.DAO.User_DAO;
import vn.aptech.mail.Entities.Attachs;
import vn.aptech.mail.Entities.Mail;
import vn.aptech.mail.Entities.Profiles;
import vn.aptech.mail.Entities.Users;
import vn.aptech.mail.utils.HttpUtils;

@ManagedBean(name = "mailBean")
@SessionScoped
@ViewScoped
public class Mail_ManageBean {

	private Mail mail;
	private List<Mail> listMailInbox;
	private Long countMailReceived;
	private RepeatPaginator paginator;
	private RepeatPaginator paginatorSendMail;
	private Map<Long, Boolean> checked = new HashMap<Long, Boolean>();
	private String username;
	private List<Mail> searchResult;
	private List<Mail> listSendMail;
	private Users users;
	private Part part;
	private String statusMessage;
	private List<Users> selected;
	private String txt1;
	private List<Mail> listMailInboxSearch;
	private String userMail;
	private List<Mail> listMailSearch;
	
	
	public List<Mail> getListMailSearch() {
		return listMailSearch;
	}

	public void setListMailSearch(List<Mail> listMailSearch) {
		this.listMailSearch = listMailSearch;
	}

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public List<Mail> getListMailInboxSearch() {
		return listMailInboxSearch;
	}

	public void setListMailInboxSearch(List<Mail> listMailInboxSearch) {
		this.listMailInboxSearch = listMailInboxSearch;
	}

	public List<Users> getSelected() {
		return selected;
	}

	public void setSelected(List<Users> selected) {
		this.selected = selected;
	}

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	public List<Mail> getListSendMail() {
		return listSendMail;
	}

	public void setListSendMail(List<Mail> listSendMail) {
		this.listSendMail = listSendMail;
	}

	public List<Mail> getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(List<Mail> searchResult) {
		this.searchResult = searchResult;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<Long, Boolean> getChecked() {
		return checked;
	}

	public void setChecked(Map<Long, Boolean> checked) {
		this.checked = checked;
	}

	public RepeatPaginator getPaginatorSendMail() {
		return paginatorSendMail;
	}

	public Long getCountMailReceived() {
		countMailReceived = Mail_DAO.getInstance()
				.countMailReceived(users.getAccountId());
		return countMailReceived;
	}

	public void setCountMailReceived(Long countMailReceived) {
		this.countMailReceived = countMailReceived;
	}

	public List<Mail> getListMail() {
		return listMailInbox;
	}

	public void setListMailInbox(List<Mail> listMailInbox) {
		this.listMailInbox = listMailInbox;
	}


	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public RepeatPaginator getPaginator() {
		return paginator;
	}

	public void setPaginator(RepeatPaginator paginator) {
		this.paginator = paginator;
	}

	public List<Mail> getListMailInbox() {
		return listMailInbox;
	}

	public Mail_ManageBean() {
		// TODO Auto-generated constructor stub
		users= (Users) HttpUtils.getFromSession("users");
		listMailInbox = Mail_DAO.getInstance().findMailByAccountReceiveId(users.getAccountId());
		paginator = new RepeatPaginator(listMailInbox);
		mail=new Mail();
		listSendMail = Mail_DAO.getInstance().findMailByAccountSendId(users.getAccountId());
		paginatorSendMail = new RepeatPaginator(listSendMail);
//		listMailInboxSearch=new ArrayList<Mail>();
		// count mail
//		countMail();
		listMailSearch =new ArrayList<Mail>();
		
	}
	
	public Long getCountMail() {
		countMailReceived = Mail_DAO.getInstance()
				.countMailReceived(users.getAccountId());
		return countMailReceived;
	}

	public List<Mail> getViewMailInBox() {

		return paginator.getModel();
	}

	public List<Mail> getViewSendMail() {

		return paginatorSendMail.getModel();
	}

	public Mail getViewMailDetail() {

		Map<String, String> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		int mailId = 0;
		if (map.get("id") != null) {
			mailId = Integer.parseInt(map.get("id"));
		}
		mail = Mail_DAO.getInstance().findMailById(mailId);
		if (mail.isStatus() == false) {
			mail.setStatus(true);
			Mail_DAO.getInstance().updateMail(mail);
		}
		return mail;
	}

	public String getDeleteMail() {
		List<Mail> checkedMail = new ArrayList<Mail>();

		for (Mail mail : listMailInbox) {
			if (checked.get(mail.getMailId())) {
				checkedMail.add(mail);
			}
		}

		checked.clear();
		boolean deleteMail = false;
		for (Mail mail : checkedMail) {
			deleteMail = Mail_DAO.getInstance().deleteMail(mail);
		}
		if (deleteMail = true) {
			System.out.println("success");
		}
		return "Inbox";
	}
	
	
	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}

		return results;
	}

	public List<Users> completeTheme(String query) {
		List<Users> ls = User_DAO.getInstance().findAllStudent();
		List<Users> filteredThemes = new ArrayList<Users>();
		for (int i = 0; i < ls.size(); i++) {
			Users skin = ls.get(i);
			if (skin.getUsername().toLowerCase().startsWith(query)) {
				filteredThemes.add(skin);
			}
		}

		return filteredThemes;
	}
	
	
	public String searchMail()
	{
		listMailInboxSearch =Mail_DAO.getInstance().searchMailByUsername("William");

		return null;
	}
	
	public String insert_Mail()
	{
		
		 String msg=null;
		
		  String re=users.getUsername();
		  Users usergetid =new Users();
		  List<Mail> list=new ArrayList<Mail>();
			 for(Users username:selected)
			 {
			usergetid = User_DAO.getInstance().findIdByUser(
					username.getUsername());
			Users ur = new Users();
			ur.setAccountId(usergetid.getAccountId());

			users = (Users) HttpUtils.getFromSession("users");
			Users u = new Users();
			u.setAccountId(users.getAccountId());

			mail.setUsersByAccountSendId(u);

			mail.setUsersByAccountReceiveId(ur);

			if (Mail_DAO.getInstance().insertMail(mail)) {
//			mail=Mail_DAO.getInstance().findMailByIdLast();
				list.add(mail);	
				msg = "Created Successfully!";
				System.out.println(list.size());
			} else {
				msg = "Error. Please check again!";

			}
			 }
			 
			  FacesMessage massage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
		      FacesContext.getCurrentInstance().addMessage(null, massage);

	      return null;
	}
	
	
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		System.out.println("***** partHeader: " + partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim()
						.replace("\"", "");
			}
		}
		  return null;
	}
	
	

	public String uploadFile() throws IOException {

	
		String fileName = getFileName(part);
		System.out.println("***** fileName: " + fileName);

		String basePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "images" + "\\";
		System.out.println(basePath);
		
		File outputFilePath = new File(basePath + fileName);
		System.out.println(outputFilePath);

		// Copy uploaded file to destination path
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = part.getInputStream();
			outputStream = new FileOutputStream(outputFilePath);

			int read = 0;
			final byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			HttpUtils.putToSession("savePart",getFileName(part) );
			statusMessage = "File upload successfull !!";
		} catch (IOException e) {
			e.printStackTrace();
			statusMessage = "File upload failed !!";
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return null; // return to same page
	}

	
	public String insertMany()
	{
		
		Attachs attachs =new Attachs();
		String msg=null;
		String parts=(String) HttpUtils.getFromSession("savePart");
		List<Profiles> listPro=new ArrayList<Profiles>();
		listPro = (List<Profiles>) HttpUtils.getFromSession("listProfile");
		if(parts!=null)
		{
			String re = users.getUsername();
		
			List<Mail> list = new ArrayList<Mail>();
			for(Profiles pro:listPro)
			{
				users = (Users) HttpUtils.getFromSession("users");
				Users u = new Users();
				u.setAccountId(users.getAccountId());
				Users ur = new Users();
				ur.setAccountId(pro.getAccountId());
				mail.setUsersByAccountSendId(u);
		
				mail.setUsersByAccountReceiveId(ur);
		
				if (Mail_DAO.getInstance().insertMail(mail)) {
				
					msg = "Created Successfully!";
					
				}else
				{
					msg = "Fail!";
				}
		
			}
			System.out.println(listPro.size());
			list = Mail_DAO.getInstance().findMailByIdLast(listPro.size());
			
			attachs.setAttachName(parts);
		
			for(Mail ma:list)
			{
				Mail m=new Mail();
				m.setMailId(ma.getMailId());
				int b=ma.getMailId();
				attachs.setMail(m);
				if(Attachs_DAO.getInstance().insertAttachs(attachs))
				{
				HttpUtils.deleteFromSession("savePart");
					 msg = "Created Successfully!";
					 
		        }else{
		             msg = "Error. Please check again!";
		        
				}
			}
		}else
		{
			for(Profiles pro:listPro)
			{
				users = (Users) HttpUtils.getFromSession("users");
				Users u = new Users();
				u.setAccountId(users.getAccountId());
				Users ur = new Users();
				ur.setAccountId(pro.getAccountId());
				mail.setUsersByAccountSendId(u);
		
				mail.setUsersByAccountReceiveId(ur);
		
				if (Mail_DAO.getInstance().insertMail(mail)) {
				
					msg = "Created Successfully!";
					
				}else
				{
					msg = "Fail!";
				}
		}
		}
		
	 FacesMessage massage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
     FacesContext.getCurrentInstance().addMessage(null, massage);
		return null;
	}
	
	
	
	public void insert() throws IOException
	{
		
		Attachs attachs =new Attachs();
		String msg=null;
		String parts=(String) HttpUtils.getFromSession("savePart");
		
		if(parts!=null)
		{
			String re = users.getUsername();
			Users usergetid = new Users();
			List<Mail> list = new ArrayList<Mail>();
			for (Users username : selected) {
				usergetid = User_DAO.getInstance().findIdByUser(
						username.getUsername());
				Users ur = new Users();
				ur.setAccountId(usergetid.getAccountId());

				users = (Users) HttpUtils.getFromSession("users");
				Users u = new Users();
				u.setAccountId(users.getAccountId());

				mail.setUsersByAccountSendId(u);

				mail.setUsersByAccountReceiveId(ur);

				if (Mail_DAO.getInstance().insertMail(mail)) {

					msg = "Created Successfully!";
				} else {
					msg = "Error. Please check again!";

				}
			}
			System.out.println(selected.size());
			list = Mail_DAO.getInstance().findMailByIdLast(selected.size());
			
			attachs.setAttachName(parts);
		
			for(Mail ma:list)
			{
				Mail m=new Mail();
				m.setMailId(ma.getMailId());
				int b=ma.getMailId();
				System.out.println(b);
				attachs.setMail(m);
				if(Attachs_DAO.getInstance().insertAttachs(attachs))
				{
				HttpUtils.deleteFromSession("savePart");
					 msg = "Created Successfully!";
					 
		        }else{
		             msg = "Error. Please check again!";
		        
				}
			}
		}else
		{
			insert_Mail();
		}
		



FacesMessage massage = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
FacesContext.getCurrentInstance().addMessage(null, massage);
		
		
	}
	

	
	@FacesConverter(forClass=Users.class)
	public class UserConverter implements Converter {

	
		@Override
		public Object getAsObject(FacesContext facesContext, UIComponent arg1, String key) {
			return User_DAO.getInstance().findById(key);
			}

		@Override
		public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
			// TODO Auto-generated method stub
			Users u = (Users) value;
			return ""+u.getAccountId();
		}

		
		
		
	}
//	public String SearchMail() {
//
////		listMailInboxSearch = new ArrayList<Mail>();
////		listSendMailSearch = new ArrayList<Mail>();
////		searchResult = Mail_DAO.getInstance().searchMailByUsername(users.getDisplayName());
//	
//		listMailInboxSearch= Mail_DAO.getInstance().searchMailByUsername(username);
////		for (Mail mail : listMail) {
////			if (mail.getUsersByAccountReceiveId().getUsername()
////					.equals(username) && mail.getUsersByAccountSendId().getUsername().equals(users.getDisplayName())) {
////				
////				listMailInboxSearch.add(mail);
////			}
////			if (mail.getUsersByAccountSendId().getUsername()
////					.equals(username) && mail.getUsersByAccountReceiveId().getUsername().equals(users.getDisplayName())) {
////				
////				listSendMail.add(mail);
////			}
////		}
//
//		System.out.println(listMailInboxSearch.size());
//
//		return "/template/Staff/viewSearch";
//	
//	}
	// public String getAbc() {
	// System.out.println(mailSelected.length);
	// return null;
	// }

}
