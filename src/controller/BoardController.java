package controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.msk.Action;

import board.BoardDBBean;
import board.BoardDataBean;

public class BoardController extends Action{
	public String index(HttpServletRequest request,
			 HttpServletResponse response)  throws Throwable { 
			 return  "/view/index.jsp"; 
			} 

	
	public String writeForm(HttpServletRequest req, HttpServletResponse res) throws Exception {
	      
	      int num =0, ref=0, re_step=0, re_level=0;
	      String boardid = req.getParameter("boardid");
	      
	      
	      /*if(boardid==null || boardid.equals("")) {boardid = "1";}*/
	      if(boardid==null) boardid = "1";

	      if(req.getParameter("num")!=null){
	      num = Integer.parseInt(req.getParameter("num")) ;
	      ref = Integer.parseInt(req.getParameter("ref")) ;
	      re_step = Integer.parseInt(req.getParameter("re_step")) ;
	      re_level = Integer.parseInt(req.getParameter("re_level")) ;
	      }
	      
	      req.setAttribute("num", num);
	      req.setAttribute("ref", ref);
	      req.setAttribute("re_step", re_step);
	      req.setAttribute("re_level", re_level);
	      req.setAttribute("pageNum", 1);
	      req.setAttribute("boardid", boardid);
	      

	      return "/view/writeForm.jsp";
	   }

	  public String writePro(HttpServletRequest req, HttpServletResponse res) throws Exception {
		   
	      String pageNum = req.getParameter("pageNum");
	      String boardid=req.getParameter("boardid");
	      if(pageNum == null || pageNum == ""){  pageNum = "1";
	      }
	      BoardDataBean article = new BoardDataBean();
	      if(req.getParameter("num") != null && !req.getParameter("num").equals("")) {
	         article.setNum(Integer.parseInt(req.getParameter("num")));
	         article.setRef(Integer.parseInt(req.getParameter("ref")));
	         article.setRe_step(Integer.parseInt(req.getParameter("re_step")));
	         article.setRe_level(Integer.parseInt(req.getParameter("re_level")));
	      }
	         article.setWriter(req.getParameter("writer"));
	         article.setEmail(req.getParameter("email"));
	         article.setSubject(req.getParameter("subject"));
	         article.setPasswd(req.getParameter("passwd"));
	         article.setContent(req.getParameter("content"));
	         article.setBoardid(req.getParameter("boardid"));
	         article.setIp(req.getRemoteAddr());   
	         System.out.println(article);
	         BoardDBBean dbPro = BoardDBBean.getInstance();
	         dbPro.insertArticle(article);
	         req.setAttribute("pageNum", pageNum);
	         res.sendRedirect(req.getContextPath()+"/board/list?pageNum="+pageNum+"&boardid="+boardid);
	      return null;
	   }
	

	  public String list(HttpServletRequest req, HttpServletResponse res) throws Exception {
	      String boardid = req.getParameter("boardid");
	      if(boardid == null) boardid="1";

	      int pageSize=5;
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	      String pageNum = req.getParameter("pageNum");
	      if(pageNum == null || pageNum == ""){
	         pageNum = "1";}
	      int currentPage = Integer.parseInt(pageNum);
	      int startRow = (currentPage-1)*pageSize+1;
	      int endRow = currentPage* pageSize;
	      int count = 0;
	      int number = 0;
	      List articleList = null;
	      BoardDBBean dbPro = BoardDBBean.getInstance();
	      count = dbPro.getArticleCount(boardid);
	      if(count > 0){
	         articleList = dbPro.getArticles(startRow, endRow, boardid);}
	            number=count - (currentPage-1)*pageSize;
	      
	      int bottomLine=3;
	      int pageCount=count/pageSize+(count%pageSize==0?0:1);
	      int startPage = 1+(currentPage-1)/bottomLine*bottomLine;
	      int endPage = startPage+bottomLine-1;
	      if(endPage>pageCount) endPage=pageCount;
	      
	      req.setAttribute("boardid", boardid);
	      req.setAttribute("count", count);
	      req.setAttribute("articleList", articleList);
	      req.setAttribute("currentPage", currentPage);
	      req.setAttribute("startPage", startPage);
	      req.setAttribute("bottomLine", bottomLine);
	      req.setAttribute("endPage", endPage);
	      req.setAttribute("number", number);
	            
	      return "/view/list.jsp";
	   }
}
